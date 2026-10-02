package edu.northeastern.shelter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * My own tests for the AgeMonths class. These tests are not part of the spec, but they are my own
 * tests to check the correctness of the AgeMonths class.
 * AgeMonthsOwnTest
 */
@Tag("current")
public class AgeMonthsOwnTest {

    @Test
    void toStringAtOneBelowTheMaximum() {
        AgeMonths age = AgeMonths.of(AgeMonths.MAX_MONTHS - 1);
        assertEquals("39 years, 11 months", age.toString());
    }

    @Test
    void toStringAtTheMaximum() {
        AgeMonths age = AgeMonths.of(AgeMonths.MAX_MONTHS);
        assertEquals("40 years", age.toString());
    }

    @Test
    void oneAboveTheMaximumIsRefused() {
        assertThrows(IntakeException.class, () -> AgeMonths.of(AgeMonths.MAX_MONTHS + 1));
    }

    @Test
    void refusalMessageNamesTheRejectedValue() {
        IntakeException e = assertThrows(IntakeException.class, () -> AgeMonths.of(-1));
        assertTrue(e.getMessage().contains("-1"));

        IntakeException e2 = assertThrows(IntakeException.class, () -> AgeMonths.of(AgeMonths.MAX_MONTHS + 1));
        assertTrue(e2.getMessage().contains(String.valueOf(AgeMonths.MAX_MONTHS + 1)));

        IntakeException e3 = assertThrows(IntakeException.class, () -> AgeMonths.of(-100));
        assertTrue(e3.getMessage().contains(String.valueOf(-100)));
    }

    @Test
    void toStringAtBothYearsAndMonthsIsInPlural() {
        AgeMonths age = AgeMonths.of(26);
        assertEquals("2 years, 2 months", age.toString());
    }

    @Test
    void remainderMonthsIsBetweenZeroAndElevenForAllValidAges() {
    for (int months = 0; months <= AgeMonths.MAX_MONTHS; months++) {
        AgeMonths age = AgeMonths.of(months);
        int remainder = age.remainderMonths();

        assertTrue(
            remainder >= 0 && remainder <= 11,
            "For age " + months + " months, remainder was " + remainder
        );
    }
    }

    @Test
    void isUnderOneYearMatchesYearsForAllValidAges() {
    for (int months = 0; months <= AgeMonths.MAX_MONTHS; months++) {
        AgeMonths age = AgeMonths.of(months);

        assertEquals(
            age.years() == 0,
            age.isUnderOneYear(),
            "Methods disagree for age " + months + " months"
        );
    }
    }
}
