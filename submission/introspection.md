# Design Introspection

## 1. What you built

I built the `AgeMonths` class, which is responsible for representing the age of an animal in months and providing a string representation of that age. I also built the `Animal` class, which represents an animal with a name, species, age, and intake date, and includes validation in its constructor to ensure that all fields are properly initialized.

## 2. Design decisions

In the metod `toString` of the class `AgeMonths`, I took the decision to create a different method to add the plural "s", transforming "year" into "years" and "month" into "months". I did this because in my first code I thought I was doing a repetitive code to add the plural. In this case, I think the code is more readable and easier to understand. The cost of this decision was the creation of a new method, which could have been avoided if I had used a different approach.

## 3. Invariants

In the class `AgeMonths` months never can be negative, line `AgeMonths.java:47`, and never can exceed the variable MAX_MONTHS = 480, line `AgeMonths.java:50`. It is impossible to create this variables without those validations, because the constructor is private, line `AgeMonths.java:31`. And the variable is immutable, because it is private final, line `AgeMonths.java:20`.

In the class `Animal` the variable name never can be null, need to have at least one caracter and dont have blank spaces in the begginer or in the end, lines `Animal.java:57`, `Animal.java:59`, `Animal.java:72`. The variables species, age and intakeDate never can be null, lines `Animal.java:63`, `Animal.java:66`, `Animal.java:69`.

One think is you don't need to verifed if the age is not negative in the class Animal, because this is already validate in AgeMonths.

## 4. Testing

In `AgeMonthsOwnTest` I tested the upper boundary (`MAX_MONTHS - 1` gives
`"39 years, 11 months"` and `MAX_MONTHS` gives `"40 years"`),
if we put a `MAX_MONTHS + 1` throws a error,
there are some tests for when we try to use `AgeMonths.of` with a invalid number and the message error contains the invalid number,
`"2 years, 2 months"`, because none of the provided tests combines plural years with plural months.
I also wrote two tests that loop over every valid age from 0 to 480: one checks that
`remainderMonths()` is always between 0 and 11, and the other checks that
`isUnderOneYear()` always agrees with `years() == 0`.

`AnimalEdgeOwnTest`, I tested if the code refuse to create a Animal with a null or blank name, species, age and intakeDate. I tested aas well with that name are trimmed, I tested if you can create a animal with only one caracter as a name. Another test was to see If the age doesn't change after create the object. Another one was to see if the method `toString()` print the correct answer, with different names.

**Hardest test.** The one for `" "` (an em space). It failed, and it showed a real bug: my
constructor used `name.trim().isEmpty()`, but `trim()` only removes characters up to code 32, so a name
made only of Unicode whitespace was accepted, even though the Javadoc says the name must contain at
least one non-whitespace character. I changed it to `isBlank()` and `strip()` (`Animal.java:59` and
`Animal.java:72`).

My own first attempt at a message test was also wrong: I wrote `assertEquals("...", AgeMonths.of(-1))`,
and the exception was thrown before `assertEquals` could even run. That is how I learned that
`assertThrows` returns the exception, so I can check its message afterwards.

**Still untested.** Nothing checks the order of validation when more than one argument is invalid at
the same time (for example, a null name and a null species), so the code checks name first, but no test pins that order down. I
would add a test that passes two bad arguments and checks that the message names the first one. 

## 5. What you would change

**Read the Javadoc as the specification before writing code.** I wrote `Animal` by looking at the
provided tests, and the tests only use plain spaces in names. The Javadoc says the name must contain
"at least one non-whitespace character", which is a stronger rule, and I only noticed my `trim()` bug
when a test with `" "` failed. Next time I would turn every sentence of the Javadoc into a test
before I write the method.

Make `AgeMonths.toString()` simpler. I spent a time trying to find the best and clean way to write and I'm still not satisfied.

## 6. What you found hard

**`AgeMonths.toString()` took the longest, but not because I could not make it work.**. Most of the time went into rewriting
a version that already worked, because I did not like how it looked. My first working version had a
separate block for the case with zero years and repeated the "number, space, word, maybe an s" logic
three times. In the second version I put the years first and decided whether to add the comma by
checking whether the message was still empty with `isBlank()`, which worked but was a strange way to
decide it. In the third version I moved the plural into `formatUnit`, so the rule is written in only
one place and `toString()` only decides which parts to join.

**Writing the tests was easy; deciding what to test was hard.** The difficult part was thinking about what could go wrong that the provided tests did not
already check. The provided suite already covers almost every line, so the coverage report could not
tell me what was missing. I had to read the Javadoc and ask myself questions like "what happens right
at the limit?", "does every error message name the right argument?" and some exemple I need to ask a AI for a Inspiration.