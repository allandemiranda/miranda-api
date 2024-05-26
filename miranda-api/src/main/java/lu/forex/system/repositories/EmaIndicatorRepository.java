package lu.forex.system.repositories;

import java.util.Optional;
import java.util.UUID;
import lu.forex.system.entities.EmaIndicator;
import lu.forex.system.enums.CandlestickApply;
import lu.forex.system.enums.TimeFrame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface EmaIndicatorRepository extends JpaRepository<EmaIndicator, UUID>, JpaSpecificationExecutor<EmaIndicator> {

  boolean existsByPeriodAndCandlestickApplyAndSymbolNameAndTimeFrameAndEmaNotNull(@NonNull int period, @NonNull CandlestickApply candlestickApply,
      @NonNull String symbolName, @NonNull TimeFrame timeFrame);

  @NonNull
  Optional<EmaIndicator> getFirstByPeriodAndCandlestickApplyAndSymbolNameAndTimeFrameOrderByTimestampDesc(@NonNull int period,
      @NonNull CandlestickApply candlestickApply, @NonNull String symbolName, @NonNull TimeFrame timeFrame);

}