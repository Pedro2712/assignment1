package edu.northeastern.shelter;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * My own tests for Animal. These cover the behaviors the provided suite does not pin down: the
 * wording of every refusal message, whitespace other than plain spaces, and the toString formats
 * the provided tests never exercise.
 */
@Tag("current")
class AnimalEdgeOwnTest {

  private static final LocalDate INTAKE = LocalDate.of(2026, 9, 21);

  @Test
  void nullNameRefusalNamesTheName() {
    IntakeException e =
        assertThrows(
            IntakeException.class, () -> new Animal(null, Species.CAT, AgeMonths.of(12), INTAKE));
    assertTrue(e.getMessage().toLowerCase().contains("name"));
  }

  @Test
  void blankNameRefusalSaysItIsBlank() {
    IntakeException e =
        assertThrows(
            IntakeException.class, () -> new Animal("   ", Species.CAT, AgeMonths.of(12), INTAKE));
    assertTrue(e.getMessage().toLowerCase().contains("blank"));
  }

  @Test
  void nullSpeciesRefusalNamesTheSpecies() {
    IntakeException e =
        assertThrows(
            IntakeException.class, () -> new Animal("Rex", null, AgeMonths.of(1), INTAKE));
    assertTrue(e.getMessage().toLowerCase().contains("species"));
  }

  @Test
  void nullAgeRefusalNamesTheAge() {
    IntakeException e =
        assertThrows(IntakeException.class, () -> new Animal("Rex", Species.DOG, null, INTAKE));
    assertTrue(e.getMessage().toLowerCase().contains("age"));
  }

  @Test
  void nullIntakeDateRefusalNamesTheIntakeDate() {
    IntakeException e =
        assertThrows(
            IntakeException.class, () -> new Animal("Rex", Species.DOG, AgeMonths.of(1), null));
    assertTrue(e.getMessage().toLowerCase().contains("intake"));
  }

  @Test
  void aNameOfOnlyTabsAndNewlinesIsRefused() {
    assertThrows(
        IntakeException.class, () -> new Animal("\t\n", Species.DOG, AgeMonths.of(1), INTAKE));
  }

  @Test
  void tabsAndNewlinesAroundTheNameAreTrimmed() {
    Animal luna = new Animal("\tLuna\n", Species.CAT, AgeMonths.of(1), INTAKE);
    assertEquals("Luna", luna.name());
  }

  @Test
  void aNameOfOnlyUnicodeWhitespaceIsRefused() {
    assertThrows(
        IntakeException.class, () -> new Animal("\u2003", Species.DOG, AgeMonths.of(1), INTAKE));
  }

  @Test
  void aOneCharacterNameIsAccepted() {
    assertEquals("A", new Animal("A", Species.DOG, AgeMonths.of(1), INTAKE).name());
  }

  @Test
  void theAgeSurvivesTheRoundTrip() {
    AgeMonths age = AgeMonths.of(7);
    assertSame(age, new Animal("Rex", Species.DOG, age, INTAKE).age());
  }

  @Test
  void toStringWithZeroMonths() {
    Animal kiwi = new Animal("Kiwi", Species.BIRD, AgeMonths.of(0), INTAKE);
    assertEquals("Kiwi (Bird, 0 months, intake 2026-09-21)", kiwi.toString());
  }

  @Test
  void toStringPadsSingleDigitMonthAndDay() {
    Animal rex = new Animal("Rex", Species.DOG, AgeMonths.of(1), LocalDate.of(2026, 1, 5));
    assertEquals("Rex (Dog, 1 month, intake 2026-01-05)", rex.toString());
  }

  @Test
  void toStringUsesTheTrimmedName() {
    Animal luna = new Animal("  Luna  ", Species.CAT, AgeMonths.of(1), INTAKE);
    assertEquals("Luna (Cat, 1 month, intake 2026-09-21)", luna.toString());
  }
}
