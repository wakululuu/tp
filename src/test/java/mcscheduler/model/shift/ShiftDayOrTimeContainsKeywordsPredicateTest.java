package mcscheduler.model.shift;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import mcscheduler.testutil.ShiftBuilder;
import mcscheduler.testutil.TypicalShifts;

//@@author WangZijun97
public class ShiftDayOrTimeContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        List<String> keywords = Arrays.asList("mon");
        List<String> otherKeywords = Arrays.asList("AM", "MON");

        ShiftDayOrTimeContainsKeywordsPredicate predicate = new ShiftDayOrTimeContainsKeywordsPredicate(keywords);
        ShiftDayOrTimeContainsKeywordsPredicate otherPredicate = new ShiftDayOrTimeContainsKeywordsPredicate(
            otherKeywords);

        // null
        assertNotEquals(predicate, null);

        // same object
        assertEquals(predicate, predicate);

        // different type
        assertNotEquals(predicate, 123);

        // same values -> returns true
        assertEquals(new ShiftDayOrTimeContainsKeywordsPredicate(keywords), predicate);

        // different values
        assertNotEquals(otherPredicate, predicate);
    }

    @Test
    public void test_dayOrTimeContainsKeywords_returnsTrue() {
        // One keyword day
        ShiftDayOrTimeContainsKeywordsPredicate predicate =
            new ShiftDayOrTimeContainsKeywordsPredicate(Arrays.asList("MON"));
        assertTrue(predicate.test(new ShiftBuilder().withShiftDay("MON").build()));

        // One keyword time
        predicate = new ShiftDayOrTimeContainsKeywordsPredicate(Arrays.asList("PM"));
        assertTrue(predicate.test(new ShiftBuilder().withShiftTime("PM").build()));

        predicate = new ShiftDayOrTimeContainsKeywordsPredicate(Arrays.asList("MON", "TUE", "AM"));
        // match multiple keywords
        assertTrue(predicate.test(new ShiftBuilder().withShiftDay("MON").withShiftTime("AM").build()));

        // match one keyword
        assertTrue(predicate.test(new ShiftBuilder().withShiftDay("TUE").withShiftTime("PM").build()));
        assertTrue(predicate.test(new ShiftBuilder().withShiftDay("WED").withShiftTime("AM").build()));

        // mixed-case keywords
        predicate = new ShiftDayOrTimeContainsKeywordsPredicate(Arrays.asList("moN", "tUe", "Am"));
        assertTrue(predicate.test(new ShiftBuilder().withShiftDay("MON").withShiftTime("AM").build()));
        assertTrue(predicate.test(new ShiftBuilder().withShiftDay("TUE").withShiftTime("PM").build()));
        assertTrue(predicate.test(new ShiftBuilder().withShiftDay("WED").withShiftTime("AM").build()));
    }

    @Test
    public void test_dayAndTimeDoesNotContainKeywords_returnsFalse() {
        ShiftDayOrTimeContainsKeywordsPredicate predicate;
        // no keywords
        predicate = new ShiftDayOrTimeContainsKeywordsPredicate(Collections.emptyList());
        assertFalse(predicate.test(TypicalShifts.SHIFT_A));

        // non-matching keywords
        predicate = new ShiftDayOrTimeContainsKeywordsPredicate(Arrays.asList("MON", "TUE", "AM"));
        assertFalse(predicate.test(new ShiftBuilder().withShiftDay("WED").withShiftTime("PM").build()));

        // keywords match role requirement but not day nor time
        predicate = new ShiftDayOrTimeContainsKeywordsPredicate(Arrays.asList("cashier", "1"));
        assertFalse(predicate.test(new ShiftBuilder().withShiftDay("WED").withShiftTime("PM")
            .withRoleRequirements("cashier 2 0").build()));
        assertFalse(predicate.test(new ShiftBuilder().withShiftDay("WED").withShiftTime("PM")
            .withRoleRequirements("cashier 1 0").build()));
        assertFalse(predicate.test(new ShiftBuilder().withShiftDay("WED").withShiftTime("PM")
            .withRoleRequirements("chef 1 0").build()));
        assertFalse(predicate.test(new ShiftBuilder().withShiftDay("WED").withShiftTime("PM")
            .withRoleRequirements("cashier 3 0", "cleaner 1 0").build()));

    }

}
