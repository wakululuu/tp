package mcscheduler.model.shift;

import java.util.List;
import java.util.function.Predicate;

import mcscheduler.commons.util.StringUtil;

/**
 * Tests that a {@code Shift}'s {@code ShiftDay} or {@code ShiftTime} matches any of the keywords given.
 */
public class ShiftDayOrTimeContainsKeywordsPredicate implements Predicate<Shift> {
    private final List<String> keywords;

    public ShiftDayOrTimeContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Shift shift) {
        return keywords.stream()
                .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(shift.getShiftDay().toString(), keyword)
                        || StringUtil.containsWordIgnoreCase(shift.getShiftTime().toString(), keyword));
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof ShiftDayOrTimeContainsKeywordsPredicate
                && keywords.equals(((ShiftDayOrTimeContainsKeywordsPredicate) other).keywords));
    }
}
