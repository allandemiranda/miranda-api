package lu.forex.system.providers;

import jakarta.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lu.forex.system.dtos.ScopeDto;
import lu.forex.system.dtos.TickDto;
import lu.forex.system.dtos.TradeDto;
import lu.forex.system.entities.Order;
import lu.forex.system.entities.OrderProfit;
import lu.forex.system.entities.Scope;
import lu.forex.system.entities.Tick;
import lu.forex.system.entities.Trade;
import lu.forex.system.enums.OrderStatus;
import lu.forex.system.enums.OrderType;
import lu.forex.system.enums.TimeFrame;
import lu.forex.system.mappers.ScopeMapper;
import lu.forex.system.mappers.SymbolMapper;
import lu.forex.system.mappers.TickMapper;
import lu.forex.system.mappers.TradeMapper;
import lu.forex.system.repositories.TradeRepository;
import lu.forex.system.services.TradeService;
import lu.forex.system.utils.OrderUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Getter(AccessLevel.PRIVATE)
public class TradeProvider implements TradeService {

  private final TradeRepository tradeRepository;
  private final TradeMapper tradeMapper;
  private final ScopeMapper scopeMapper;
  private final SymbolMapper symbolMapper;
  private final TickMapper tickMapper;
  @Value("${trade.slot.minutes:15}")
  private int slotMinutes;
  @Value("#{${trade.slot.config}}")
  private Map<String, Map<String, List<Integer>>> tradeConfig;

  @Override
  public @NotNull Collection<TradeDto> generateTrades(final @NotNull Set<ScopeDto> scopeDtos) {

    final int subTime = 1440 / this.getSlotMinutes();
    final Collection<LocalTime[]> localTimes = IntStream.range(0, subTime).mapToObj(i -> {
      final int hourInitial = (i * this.getSlotMinutes()) / 60;
      final int minuteInitial = (i * this.getSlotMinutes()) % 60;
      final LocalTime initialTime = LocalTime.of(hourInitial, minuteInitial);
      final int hourFinal = ((i + 1) * this.getSlotMinutes()) / 60;
      final int minuteFinal = ((i + 1) * this.getSlotMinutes()) % 60;
      final LocalTime initialFinal = hourFinal == 24 ? LocalTime.of(23, 59, 59) : LocalTime.of(hourFinal, minuteFinal).minusSeconds(1);
      return new LocalTime[]{initialTime, initialFinal};
    }).toList();
    final Collection<DayOfWeek> validWeeks = Arrays.stream(DayOfWeek.values())
        .filter(dayOfWeek -> !DayOfWeek.SATURDAY.equals(dayOfWeek) && !DayOfWeek.SUNDAY.equals(dayOfWeek)).toList();

    final Collection<Trade> collection = this.getTradeConfig().entrySet().stream().flatMap(timeFrameInput -> {
      final TimeFrame timeFrame = TimeFrame.valueOf(timeFrameInput.getKey());

      final Collection<Integer> spreads = timeFrameInput.getValue().get("spread");
      final Collection<Integer> tps = timeFrameInput.getValue().get("tp");
      final Collection<Integer> sls = timeFrameInput.getValue().get("sl");

      return scopeDtos.parallelStream().filter(scopeDto -> scopeDto.timeFrame().equals(timeFrame))
          .map(scopeDto -> this.getScopeMapper().toEntity(scopeDto)).flatMap(scope -> spreads.parallelStream().flatMap(spread -> tps.parallelStream()
              .flatMap(tp -> sls.parallelStream().filter(sl -> sl <= tp && sl > spread)
                  .flatMap(sl -> validWeeks.parallelStream().flatMap(week -> localTimes.parallelStream().map(time -> {
                    final Trade trade = new Trade();
                    trade.setScope(scope);
                    trade.setStopLoss(sl);
                    trade.setTakeProfit(tp);
                    trade.setSpreadMax(spread);
                    trade.setSlotWeek(week);
                    trade.setSlotStart(time[0]);
                    trade.setSlotEnd(time[1]);
                    trade.setActivate(false);
                    return trade;
                  }))))));

    }).toList();

    return this.getTradeRepository().saveAll(collection).stream().map(trade -> this.getTradeMapper().toDto(trade)).toList();
  }

  @Override
  public @NotNull Collection<TradeDto> getTradesForOpenPosition(final @NotNull ScopeDto scopeDto, final @NotNull TickDto tickDto) {
    final Scope scope = this.getScopeMapper().toEntity(scopeDto);
    return this.getTradeRepository()
        .findTradeToOpenOrder(scope.getId(), (int) tickDto.spread(), tickDto.timestamp().getDayOfWeek(), tickDto.timestamp().toLocalTime()).stream()
        .map(this.getTradeMapper()::toDto).toList();
  }

  @Override
  public @NotNull TradeDto addOrder(final @NotNull TickDto openTick, final @NotNull OrderType orderType, final boolean isSimulator,
      final @NotNull TradeDto tradeDto) {
    final Tick tick = this.getTickMapper().toEntity(openTick);

    final Order order = new Order();
    order.setOpenTick(tick);
    order.setCloseTick(tick);
    order.setOrderType(orderType);
    order.setOrderStatus(OrderStatus.OPEN);
    order.setSimulator(isSimulator);
    final double profit = OrderUtils.getProfit(order);
    order.setProfit(profit);

    final OrderProfit orderProfit = new OrderProfit();
    orderProfit.setTimestamp(tick.getTimestamp());
    orderProfit.setProfit(order.getProfit());
    order.getHistoricProfit().add(orderProfit);

    final Trade trade = this.getTradeRepository().getReferenceById(tradeDto.id());
    order.setTrade(trade);
    trade.getOrders().add(order);
    final Trade saved = this.getTradeRepository().save(trade);
    return this.getTradeMapper().toDto(saved);
  }

  @Override
  public @NotNull List<TradeDto> managementEfficientTradesScenariosToBeActivated(final @NotNull String symbolName) {
    return this.getTradeRepository().findBySymbolName(symbolName).stream().filter(trade -> {
      if (trade.getOrders().stream().filter(order -> OrderStatus.OPEN.equals(order.getOrderStatus())).count() > 2) {
        if (trade.getBalance() > 0 && trade.getOrders().stream().noneMatch(order -> OrderStatus.STOP_LOSS.equals(order.getOrderStatus()))) {
          return true;
        } else if (trade.getBalance() > 0) {
          final long totalOrdersClose = trade.getOrders().stream().filter(order -> !OrderStatus.OPEN.equals(order.getOrderStatus())).count();
          final long totalOrdersTP = trade.getOrders().stream().filter(order -> OrderStatus.TAKE_PROFIT.equals(order.getOrderStatus())).count();
          final long percentage = (totalOrdersTP * 100L) / totalOrdersClose;
          return 66L > percentage;
        }
      }
      return false;
    }).sorted(Comparator.comparingDouble(Trade::getBalance)).map(trade -> this.getTradeMapper().toDto(trade)).toList();
  }
}
