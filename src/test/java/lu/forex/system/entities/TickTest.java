package lu.forex.system.entities;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TickTest {

  private static UUID uuid;
  private static ValidatorFactory validatorFactory;

  @BeforeAll
  static void setUpBeforeClass() {
    validatorFactory = Validation.buildDefaultValidatorFactory();
    uuid = UUID.randomUUID();
  }

  @AfterAll
  static void tearDownAfterClass() {
    validatorFactory.close();
  }

  @Test
  void testTickAnyIdIsValid() {
    //given
    final Validator validator = validatorFactory.getValidator();
    final Tick tick = new Tick();

    //when
    tick.setId(uuid);
    final Set<ConstraintViolation<Tick>> validate = validator.validate(tick);

    //then
    Assertions.assertFalse(validate.stream().anyMatch(tickConstraintViolation -> "id".equals(tickConstraintViolation.getPropertyPath().toString())));
  }

  @Test
  void testTickWithNullIdIsInvalid() {
    //given
    final Validator validator = validatorFactory.getValidator();
    final Tick tick = new Tick();

    //when
    tick.setId(null);
    final Set<ConstraintViolation<Tick>> validate = validator.validate(tick);

    //then
    Assertions.assertTrue(validate.stream().anyMatch(tickConstraintViolation -> "id".equals(tickConstraintViolation.getPropertyPath().toString())
                                                                                && "{jakarta.validation.constraints.NotNull.message}".equals(
        tickConstraintViolation.getMessageTemplate())));
  }

  @Test
  void testTickId() {
    //given
    final Tick tick = new Tick();

    //when
    tick.setId(uuid);

    //then
    Assertions.assertEquals(uuid, tick.getId());
  }

  @Test
  void testTickAnySymbolIsValid() {
    //given
    final Validator validator = validatorFactory.getValidator();
    final Tick tick = new Tick();
    final Symbol symbol = new Symbol();

    //when
    tick.setSymbol(symbol);
    final Set<ConstraintViolation<Tick>> validate = validator.validate(tick);

    //then
    Assertions.assertFalse(
        validate.stream().anyMatch(tickConstraintViolation -> "symbol".equals(tickConstraintViolation.getPropertyPath().toString())));
  }

  @Test
  void testTickWithNullSymbolIsInvalid() {
    //given
    final Validator validator = validatorFactory.getValidator();
    final Tick tick = new Tick();

    //when
    tick.setSymbol(null);
    final Set<ConstraintViolation<Tick>> validate = validator.validate(tick);

    //then
    Assertions.assertTrue(validate.stream().anyMatch(tickConstraintViolation -> "symbol".equals(tickConstraintViolation.getPropertyPath().toString())
                                                                                && "{jakarta.validation.constraints.NotNull.message}".equals(
        tickConstraintViolation.getMessageTemplate())));
  }

  @Test
  void testTickSymbol() {
    //given
    final Tick tick = new Tick();
    final Symbol symbol = new Symbol();
    symbol.setName("EURUSD");

    //when
    tick.setSymbol(symbol);

    //then
    Assertions.assertEquals(symbol, tick.getSymbol());
  }

  @Test
  void testTickAnyTimestampIsValid() {
    //given
    final Validator validator = validatorFactory.getValidator();
    final Tick tick = new Tick();
    final LocalDateTime timestamp = LocalDateTime.now();

    //when
    tick.setTimestamp(timestamp);
    final Set<ConstraintViolation<Tick>> validate = validator.validate(tick);

    //then
    Assertions.assertFalse(
        validate.stream().anyMatch(tickConstraintViolation -> "timestamp".equals(tickConstraintViolation.getPropertyPath().toString())));
  }

  @Test
  void testTickWithNullTimestampIsInvalid() {
    //given
    final Validator validator = validatorFactory.getValidator();
    final Tick tick = new Tick();

    //when
    tick.setTimestamp(null);
    final Set<ConstraintViolation<Tick>> validate = validator.validate(tick);

    //then
    Assertions.assertTrue(validate.stream().anyMatch(
        tickConstraintViolation -> "timestamp".equals(tickConstraintViolation.getPropertyPath().toString())
                                   && "{jakarta.validation.constraints.NotNull.message}".equals(tickConstraintViolation.getMessageTemplate())));
  }

  @Test
  void testTickTimestamp() {
    //given
    final Tick tick = new Tick();
    final LocalDateTime timestamp = LocalDateTime.now();

    //when
    tick.setTimestamp(timestamp);

    //then
    Assertions.assertEquals(timestamp, tick.getTimestamp());
  }

  @ParameterizedTest
  @ValueSource(doubles = {1d, 2d, Double.MAX_VALUE})
  void testTickPositiveAndNotZeroBidIsValid(double bid) {
    //given
    final Validator validator = validatorFactory.getValidator();
    final Tick tick = new Tick();

    //when
    tick.setBid(bid);
    final Set<ConstraintViolation<Tick>> validate = validator.validate(tick);

    //then
    Assertions.assertFalse(validate.stream().anyMatch(tickConstraintViolation -> "bid".equals(tickConstraintViolation.getPropertyPath().toString())));
  }

  @ParameterizedTest
  @ValueSource(doubles = {0d, -1d, Double.MIN_EXPONENT})
  void testTickNegativeOrZeroBidIsValid(double bid) {
    //given
    final Validator validator = validatorFactory.getValidator();
    final Tick tick = new Tick();

    //when
    tick.setBid(bid);
    final Set<ConstraintViolation<Tick>> validate = validator.validate(tick);

    //then
    Assertions.assertTrue(validate.stream().anyMatch(tickConstraintViolation -> "bid".equals(tickConstraintViolation.getPropertyPath().toString())
                                                                                && "{jakarta.validation.constraints.Positive.message}".equals(
        tickConstraintViolation.getMessageTemplate())));
  }

  @ParameterizedTest
  @ValueSource(doubles = {1d, 2d, Double.MAX_VALUE})
  void testTickBid(double bid) {
    //given
    final Tick tick = new Tick();

    //when
    tick.setBid(bid);

    //then
    Assertions.assertEquals(bid, tick.getBid());
  }

  @ParameterizedTest
  @ValueSource(doubles = {1d, 2d, Double.MAX_VALUE})
  void testTickPositiveAndNotZeroAskIsValid(double ask) {
    //given
    final Validator validator = validatorFactory.getValidator();
    final Tick tick = new Tick();

    //when
    tick.setAsk(ask);
    final Set<ConstraintViolation<Tick>> validate = validator.validate(tick);

    //then
    Assertions.assertFalse(validate.stream().anyMatch(tickConstraintViolation -> "ask".equals(tickConstraintViolation.getPropertyPath().toString())));
  }

  @ParameterizedTest
  @ValueSource(doubles = {0d, -1d, Double.MIN_EXPONENT})
  void testTickNegativeOrZeroAskIsValid(double ask) {
    //given
    final Validator validator = validatorFactory.getValidator();
    final Tick tick = new Tick();

    //when
    tick.setAsk(ask);
    final Set<ConstraintViolation<Tick>> validate = validator.validate(tick);

    //then
    Assertions.assertTrue(validate.stream().anyMatch(tickConstraintViolation -> "bid".equals(tickConstraintViolation.getPropertyPath().toString())
                                                                                && "{jakarta.validation.constraints.Positive.message}".equals(
        tickConstraintViolation.getMessageTemplate())));
  }

  @Test
  void testTickRepresentationAskHighThanBidIsValid() {
    //given
    final Validator validator = validatorFactory.getValidator();
    final Tick tick = new Tick();

    //when
    tick.setAsk(2d);
    tick.setBid(1d);
    final Set<ConstraintViolation<Tick>> validate = validator.validate(tick);

    //when
    Assertions.assertFalse(validate.stream().anyMatch(
        tickConstraintViolation -> "{lu.forex.system.annotations.TickRepresentation}".equals(tickConstraintViolation.getMessageTemplate())));
  }

  @Test
  void testTickRepresentationBidHighThanAskIsInvalid() {
    //given
    final Validator validator = validatorFactory.getValidator();
    final Tick tick = new Tick();

    //when
    tick.setAsk(1d);
    tick.setBid(2d);
    final Set<ConstraintViolation<Tick>> validate = validator.validate(tick);

    //when
    Assertions.assertTrue(validate.stream().anyMatch(
        tickConstraintViolation -> "{lu.forex.system.annotations.TickRepresentation}".equals(tickConstraintViolation.getMessageTemplate())));
  }

  @ParameterizedTest
  @ValueSource(doubles = {1d, 2d, Double.MAX_VALUE})
  void testTickAsk(double ask) {
    //given
    final Tick tick = new Tick();

    //when
    tick.setAsk(ask);

    //then
    Assertions.assertEquals(ask, tick.getAsk());
  }

  @Test
  void testTickEqualsAndHashCode() {
    //given
    final Tick tick1 = new Tick();
    final Tick tick2 = new Tick();
    final LocalDateTime timestamp = LocalDateTime.now();
    final Symbol symbol = new Symbol();

    //when
    tick1.setId(uuid);
    tick1.setSymbol(symbol);
    tick1.setTimestamp(timestamp);
    tick1.setBid(1d);
    tick1.setAsk(2d);

    UUID randomUUID = UUID.randomUUID();
    while (randomUUID.equals(tick1.getId())) {
      randomUUID = UUID.randomUUID();
    }
    tick2.setId(randomUUID);
    tick2.setSymbol(symbol);
    tick2.setTimestamp(timestamp);
    tick2.setBid(3d);
    tick2.setAsk(4d);

    //then
    Assertions.assertEquals(tick1, tick2);
    Assertions.assertEquals(tick1.hashCode(), tick2.hashCode());
  }

  @Test
  void testTickWithSymbolNotEqualsAndHashCode() {
    //given
    final Tick tick1 = new Tick();
    final Tick tick2 = new Tick();
    final LocalDateTime timestamp = LocalDateTime.now();
    final Symbol symbol = new Symbol();
    final Symbol symbol1 = new Symbol();

    //when
    symbol1.setName("FGHIJK");
    tick1.setSymbol(symbol1);
    tick1.setTimestamp(timestamp);

    symbol.setName("ABCDEF");
    tick2.setSymbol(symbol);
    tick2.setTimestamp(timestamp);

    //then
    Assertions.assertNotEquals(tick1, tick2);
    Assertions.assertNotEquals(tick1.hashCode(), tick2.hashCode());
  }

  @Test
  void testTickTimestampNotEqualsAndHashCode() {
    //given
    final Tick tick1 = new Tick();
    final Tick tick2 = new Tick();
    final LocalDateTime timestamp1 = LocalDateTime.now();
    final LocalDateTime timestamp2 = LocalDateTime.now().plusYears(1);
    final Symbol symbol = new Symbol();

    //when
    tick1.setSymbol(symbol);
    tick1.setTimestamp(timestamp1);

    tick2.setSymbol(symbol);
    tick2.setTimestamp(timestamp2);

    //then
    Assertions.assertNotEquals(tick1, tick2);
    Assertions.assertNotEquals(tick1.hashCode(), tick2.hashCode());
  }

  @Test
  void testTickEquals() {
    //given
    final Tick tick = new Tick();

    //when
    final boolean equals = tick.equals(tick);

    //then
    Assertions.assertTrue(equals);
  }

  @Test
  void testTickEqualsNull() {
    //given
    final Tick tick = new Tick();

    //when
    final boolean equals = tick.equals(null);

    //then
    Assertions.assertFalse(equals);
  }

  @Test
  void testTickEqualsWrongObjectType() {
    //given
    final Tick tick = new Tick();

    //when
    final boolean equals = tick.equals(new Object());

    //then
    Assertions.assertFalse(equals);
  }

  @Test
  void testTickToString() {
    //given
    final Tick tick = new Tick();

    //when
    final String toString = tick.toString();

    //then
    Assertions.assertEquals("Tick(id=null, timestamp=null, bid=0.0, ask=0.0)", toString);
  }
}