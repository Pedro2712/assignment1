## Declaration

- Which LLM and version you used:
ChatGPT-6.1 sol High
- Confirm you understand every line you submitted, regardless of who or what wrote it: 
yes

## Response of the LLM Evaluation
Score: 90/100. The value objects satisfy the stated validation and formatting requirements. The main weaknesses are incomplete tests and a missing explanation of Species.
This is a static review of the five attachments. Species, IntakeException, and the provided test suite were not attached, so I cannot verify their implementations or report a complete test run. Line numbers below refer to the attached files.
1. Immutability and encapsulation — 25/25
- Animal’s four fields are all private final (lines 28–31).
- AgeMonths stores its state in private final int months; (line 20).
- Neither class has setters or methods that mutate its fields after construction. Changing local variables inside toString() does not mutate the object.
- Returning String, AgeMonths, and LocalDate directly is safe because those types are immutable. Species is the supplied enum described in the rubric; its implementation is unavailable for inspection.
- public static final int MAX_MONTHS = 480; is an appropriate public constant, not exposed mutable state. The formatting helper is correctly private (line 145).
The test class AgeMonthsOwnTest is unnecessarily public (line 16), but that does not compromise either value object’s encapsulation; no deduction here.
2. Constructor validation — 25/25
Every required case is implemented:
Requirement	Evidence in Animal
Reject null name	Lines 56–57: if (name == null) followed by IntakeException("name must be non-null")
Reject empty name	Line 59: name.isBlank() includes ""
Reject whitespace-only name	Lines 59–60: name.isBlank() and "name must be non-blank"
Reject null species	Lines 62–63
Reject null age	Lines 65–66
Reject null intake date	Lines 68–69
Strip surrounding whitespace	Line 72: this.name = name.strip();


All validation finishes before the first field assignment at line 72. Every refusal explicitly throws IntakeException with a message identifying the invalid argument.
Correctly, Animal does not repeat the age range checks already enforced by AgeMonths.of().
3. Correctness — 15/15
Tracing AgeMonths.toString() gives all required outputs:
Months	Output
0	"0 months"
1	"1 month"
11	"11 months"
12	"1 year"
23	"1 year, 11 months"
24	"2 years"
25	"2 years, 1 month"


The helper’s if (value != 1) (line 147) handles singular/plural agreement. The early return when remainderMonths == 0 (lines 127–128) omits months for whole years.
Animal line 135 is:
return name() + " (" + species() + ", " + age() + ", intake " + intakeDate() + ")";

This produces the required shape:
Luna (Cat, 1 year, 11 months, intake 2026-09-21)

The exact "Cat" label depends on the supplied Species.toString(), whose source was not attached. No deduction for that unavailable dependency.
4. Testing and coverage — 10/15
The attached tests cover negative ages, the maximum, one past the maximum, every null argument, space-only names, name stripping, and several exact strings. All exception assertions correctly request IntakeException.class.
Deductions:
- −1: No empty-string name test. "   " is tested, but new Animal("", …) is a separate required case and is absent.
- −2: Missing exact formatting assertions at important transitions. There are no assertions for "11 months" at 11, "1 year" at 12, "1 year, 11 months" at 23, or "2 years, 1 month" at 25. The loops construct these ages but do not verify those strings.
- −1: The Unicode-whitespace test does not contain Unicode whitespace beyond an ordinary space. Despite its name, aNameOfOnlyUnicodeWhitespaceIsRefused, line 70 passes new Animal(" ", …). The literal is U+0020, not the em space U+2003 claimed in introspection.md lines 32–36. It therefore would not catch the described trim() bug.
- −1: Some assertions permit incorrect implementations.
  remainder >= 0 && remainder <= 11 (lines 59–62) passes if remainderMonths() always returns zero. Comparing age.years() == 0 with age.isUnderOneYear() (lines 71–75) passes if both methods are consistently wrong—for example, always returning zero and true. Message checks such as contains("-1") also do not establish that the message explains the rejection.
Three specific untested cases are:
1. An empty name: "".
2. A name consisting solely of an actual em space: "\u2003".
3. AgeMonths.of(25).toString() equalling "2 years, 1 month".
5. Code quality and style — 10/10
The public production API has Javadocs documenting useful contracts: normalization, non-null guarantees, complete-year truncation, remainder bounds, and exact formatting.
Animal.toString() does delegate. Concatenating age() invokes AgeMonths.toString(); Animal does not recalculate years or months. Consequently, an age-format change is reflected automatically in the animal description.
The private formatUnit() helper centralizes pluralization cleanly. No deductions for harmless local variables or minor stylistic preferences.
6. Scope discipline and code walk — 5/10
The student added no explicit inheritance hierarchy, collections, or equals/hashCode implementations. Overriding toString() is required and is not a scope violation.
−5: The required Species explanation is entirely missing from introspection.md. Line 15 only states:
“The variables species, age and intakeDate never can be null”

That discusses constructor validation, not why Species is an enum with a field and method. The reflection needs to explain how the enum defines a restricted, type-safe set of species, how its field stores a display label, and how its method exposes that label rather than merely relying on constant names.
Most important improvement next time: Turn each specification requirement into an assertion with an independently known expected result, and verify that each test’s input actually exercises the case its name claims.
One thing done genuinely well: The constructor establishes every invariant before assigning any field, and then safely relies on AgeMonths to guarantee a valid age.

**Coverage reported:** 100% line coverage (60 of 60 lines), from
`build/reports/jacoco/test/jacocoTestReport.xml`.

## My response

### Where it is right

The Unicode whitespace test did not contain Unicode whitespace. This was correct and it was the
most useful point in the review. When I copied the em space into the test, it was saved as a plain
space (U+0020), so `aNameOfOnlyUnicodeWhitespaceIsRefused` only repeated the provided
`aNameOfOnlyWhitespaceIsRefused`. It would not have caught the `trim()` bug I describe in my
introspection. I would never have noticed this by reading the code, because the two characters look
the same on screen.

**My loop tests are weak on their own.** `remainderMonthsIsBetweenZeroAndElevenForAllValidAges` would
still pass if `remainderMonths()` always returned 0, and `isUnderOneYearMatchesYearsForAllValidAges`
would pass if both methods were wrong in the same way. In my submission the provided tests check the
exact values, so a broken method would still be caught, but my own tests should not depend on that.

### Where it is wrong

**"−1: No empty-string name test."** The provided `AnimalTest.anEmptyNameIsRefused` already tests
`new Animal("", ...)`. The lab asks for tests that the provided suite does *not* cover, so adding the
same case again would not add anything.

**"−2: Missing exact formatting assertions at 11, 12, 23 and 25."** All of these are in the provided
`AgeMonthsTest`: `toStringUsesMonthsAloneUnderOneYear`, `toStringOmitsTheMonthsPartForWholeYears` and
`toStringCombinesYearsAndMonths`. The review also contradicts itself: it lists
`AgeMonths.of(25).toString()` as one of three untested cases, but that exact assertion is in
`toStringCombinesYearsAndMonths`.

**"−5: The Species explanation is missing from introspection.md."** The introspection template has no
question about `Species`.

### What it missed

- No test checks the order of validation when more than one argument is invalid, for example a null
  name and a null species together. The code checks the name first, but no test pins that down.
- The blank-name message is `"name must be non-blank"`, without the rejected value. `AgeMonths.of`
  does include the value (`"was -1"`), so the two classes report errors in different ways.

### What you changed

I replaced the plain space in `aNameOfOnlyUnicodeWhitespaceIsRefused` with the escape `" "`, so
the test now really uses an em space and would catch a `trim()`-based implementation. I checked that
the full suite still passes. I did not change the loop tests yet: the provided suite already catches
a wrong `remainderMonths()` or `isUnderOneYear()`.
