package mcscheduler.model.worker;

import java.util.List;
import java.util.function.Predicate;

import mcscheduler.commons.util.StringUtil;

/**
 * Tests that a {@code Worker}'s {@code Name} matches any of the keywords given.
 */
public class NameContainsKeywordsPredicate implements Predicate<Worker> {
    private final List<String> keywords;

    public NameContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Worker worker) {
        return keywords.stream()
                .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(worker.getName().fullName, keyword));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof NameContainsKeywordsPredicate // instanceof handles nulls
                && keywords.equals(((NameContainsKeywordsPredicate) other).keywords)); // state check
    }

}
