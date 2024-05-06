package lu.forex.system.entities;

import lu.forex.system.enums.Currency;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SymbolTest {

  private static final String NAME = "NAME";
  private static final Currency CURRENCY_BASE = Currency.EUR;
  private static final Currency CURRENCY_QUOTE = Currency.EUR;
  private static final int DIGITS = 25;

  @Test
  void testName() {
    //given
    final Symbol symbol = new Symbol();
    final String name = "EURUSD";

    //when
    symbol.setName(name);

    //then
    Assertions.assertEquals(name, symbol.getName());
  }

  @Test
  void testCurrency() {
    //given
    final Symbol symbol = new Symbol();
    final Currency currencyBase = Currency.EUR;
    final Currency currencyQuote = Currency.USD;

    //when
    symbol.setCurrencyBase(currencyBase);
    symbol.setCurrencyQuote(currencyQuote);

    //then
    Assertions.assertEquals(Currency.EUR, symbol.getCurrencyBase());
    Assertions.assertEquals(Currency.USD, symbol.getCurrencyQuote());
  }

  @Test
  void testDescription() {
    //given
    final Symbol symbol = new Symbol();

    //when
    symbol.setCurrencyBase(Currency.EUR);
    symbol.setCurrencyQuote(Currency.USD);

    //then
    Assertions.assertEquals("Euro vs US Dollar", symbol.getDescription());
  }

  @Test
  void testDescriptionBaseThrow() {
    //given
    final Symbol symbol = new Symbol();

    //when
    symbol.setCurrencyQuote(Currency.USD);
    final Executable executable = symbol::getDescription;

    //then
    Assertions.assertThrows(NullPointerException.class, executable);
  }

  @Test
  void testDescriptionQuoteThrow() {
    //given
    final Symbol symbol = new Symbol();

    //when
    symbol.setCurrencyQuote(Currency.USD);
    final Executable executable = symbol::getDescription;

    //then
    Assertions.assertThrows(NullPointerException.class, executable);
  }

  @Test
  void testDigits() {
    //given
    final Symbol symbol = new Symbol();
    final int digits = 5;

    //when
    symbol.setDigits(digits);

    //then
    Assertions.assertEquals(digits, symbol.getDigits());
  }

  @Test
  void testSymbolSwapLong() {
    //given
    final Symbol symbol = new Symbol();
    final double tax = 0.25;

    //when
    symbol.setSwapLong(tax);

    //then
    Assertions.assertEquals(tax, symbol.getSwapLong());
  }

  @Test
  void testSymbolSwapShort() {
    //given
    final Symbol symbol = new Symbol();
    final double tax = -0.1;

    //when
    symbol.setSwapShort(tax);

    //then
    Assertions.assertEquals(tax, symbol.getSwapShort());
  }

  @Test
  void testSymbolEqualsAndHashCode() {
    //given
    final String name = "EURUSD";
    final Currency currencyBase = Currency.EUR;
    final Currency currencyQuote = Currency.USD;
    final Symbol symbol1 = new Symbol();
    final Symbol symbol2 = new Symbol();

    //when
    symbol1.setName(name);
    symbol1.setCurrencyBase(currencyBase);
    symbol1.setCurrencyQuote(currencyQuote);

    symbol2.setName(name);
    symbol2.setCurrencyBase(currencyBase);
    symbol2.setCurrencyQuote(currencyQuote);

    //then
    Assertions.assertEquals(symbol1, symbol2);
    Assertions.assertEquals(symbol1.hashCode(), symbol2.hashCode());
  }

  @Test
  void testNameNotEqualsAndHashCode() {
    //given
    final Currency currencyBase = Currency.EUR;
    final Currency currencyQuote = Currency.USD;
    final Symbol symbol1 = new Symbol();
    final Symbol symbol2 = new Symbol();

    //when
    symbol1.setName("name");
    symbol1.setCurrencyBase(currencyBase);
    symbol1.setCurrencyQuote(currencyQuote);

    symbol2.setName("name1");
    symbol2.setCurrencyBase(currencyBase);
    symbol2.setCurrencyQuote(currencyQuote);

    //then
    Assertions.assertNotEquals(symbol1, symbol2);
    Assertions.assertNotEquals(symbol1.hashCode(), symbol2.hashCode());
  }

  @Test
  void testBaseNotEqualsAndHashCode() {
    //given
    final String name = "EURUSD";
    final Currency currencyQuote = Currency.USD;
    final Symbol symbol1 = new Symbol();
    final Symbol symbol2 = new Symbol();

    //when
    symbol1.setName(name);
    symbol1.setCurrencyBase(Currency.EUR);
    symbol1.setCurrencyQuote(currencyQuote);

    symbol2.setName(name);
    symbol2.setCurrencyBase(Currency.GBP);
    symbol2.setCurrencyQuote(currencyQuote);

    //then
    Assertions.assertNotEquals(symbol1, symbol2);
    Assertions.assertNotEquals(symbol1.hashCode(), symbol2.hashCode());
  }

  @Test
  void testQuoteNotEqualsAndHashCode() {
    //given
    final String name = "EURUSD";
    final Currency currencyBase = Currency.EUR;
    final Symbol symbol1 = new Symbol();
    final Symbol symbol2 = new Symbol();

    //when
    symbol1.setName(name);
    symbol1.setCurrencyBase(currencyBase);
    symbol1.setCurrencyQuote(Currency.USD);

    symbol2.setName(name);
    symbol2.setCurrencyBase(currencyBase);
    symbol2.setCurrencyQuote(Currency.GBP);

    //then
    Assertions.assertNotEquals(symbol1, symbol2);
    Assertions.assertNotEquals(symbol1.hashCode(), symbol2.hashCode());
  }
}