package lu.forex.system.services;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import lu.forex.system.dtos.OrderDto;
import lu.forex.system.dtos.TickDto;
import lu.forex.system.dtos.TradeDto;
import lu.forex.system.enums.OrderStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public interface OrderService {

  @Transactional
  @NotNull
  List<OrderDto> getOrders(final @NotNull UUID symbolId, final @NotNull OrderStatus orderStatus);

  @Transactional
  void updateOrders(final @NotNull TickDto tickDto);

  @Transactional
  void cleanOrdersCloseAfterDays(final @NotNull String symbolName, final @Positive int days);

  @Transactional
  @NotNull
  Stream<OrderDto> processingInitOrders(final @NotNull List<TickDto> tickDtoList, final @NotNull Stream<TradeDto> tradeDtos);
}