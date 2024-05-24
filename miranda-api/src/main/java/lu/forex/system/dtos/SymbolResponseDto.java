package lu.forex.system.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;
import lu.forex.system.annotations.SymbolCurrencyRepresentation;
import lu.forex.system.enums.Currency;

/**
 * DTO for {@link lu.forex.system.entities.Symbol}
 */
@SymbolCurrencyRepresentation
public record SymbolResponseDto(@NotNull @NotEmpty @NotBlank @Size(max = 6, min = 6) String name, @NotNull Currency currencyBase,
                                @NotNull Currency currencyQuote, @Min(1) @Positive int digits, double swapLong, double swapShort,
                                @NotNull @NotEmpty @NotBlank String description) implements Serializable {

  @Serial
  private static final long serialVersionUID = 7822325714143808724L;
}