package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.shift.RoleRequirement;
import seedu.address.model.shift.ShiftDay;
import seedu.address.model.shift.ShiftTime;
import seedu.address.model.tag.Role;
import seedu.address.model.tag.Tag;
import seedu.address.model.worker.Address;
import seedu.address.model.worker.Email;
import seedu.address.model.worker.Name;
import seedu.address.model.worker.Pay;
import seedu.address.model.worker.Phone;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!Name.isValidName(trimmedName)) {
            throw new ParseException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(trimmedName);
    }

    /**
     * Parses a {@code String phone} into a {@code Phone}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code phone} is invalid.
     */
    public static Phone parsePhone(String phone) throws ParseException {
        requireNonNull(phone);
        String trimmedPhone = phone.trim();
        if (!Phone.isValidPhone(trimmedPhone)) {
            throw new ParseException(Phone.MESSAGE_CONSTRAINTS);
        }
        return new Phone(trimmedPhone);
    }

    /**
     * Parses a {@code String address} into an {@code Address}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code address} is invalid.
     */
    public static Address parseAddress(String address) throws ParseException {
        requireNonNull(address);
        String trimmedAddress = address.trim();
        if (!Address.isValidAddress(trimmedAddress)) {
            throw new ParseException(Address.MESSAGE_CONSTRAINTS);
        }
        return new Address(trimmedAddress);
    }

    /**
     * Parses a {@code String email} into an {@code Email}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code email} is invalid.
     */
    public static Email parseEmail(String email) throws ParseException {
        requireNonNull(email);
        String trimmedEmail = email.trim();
        if (!Email.isValidEmail(trimmedEmail)) {
            throw new ParseException(Email.MESSAGE_CONSTRAINTS);
        }
        return new Email(trimmedEmail);
    }

    /**
     * Parses a {@code String pay} into an {@code Pay}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code pay} is invalid.
     */
    public static Pay parsePay(String pay) throws ParseException {
        requireNonNull(pay);
        String trimmedPay = pay.trim();
        if (!Pay.isValidPay(pay)) {
            throw new ParseException(Pay.MESSAGE_CONSTRAINTS);
        }
        return new Pay(trimmedPay);
    }

    /**
     * Parses a {@code String tag} into a {@code Tag}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code tag} is invalid.
     */
    public static Tag parseTag(String tag) throws ParseException {
        requireNonNull(tag);
        String trimmedTag = tag.trim();
        if (!Tag.isValidTagName(trimmedTag)) {
            throw new ParseException(Tag.MESSAGE_CONSTRAINTS);
        }
        return new Tag(trimmedTag);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>}.
     */
    public static Set<Tag> parseTags(Collection<String> tags) throws ParseException {
        requireNonNull(tags);
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(parseTag(tagName));
        }
        return tagSet;
    }

    /**
     * Parses a {@code String day} into a {@code ShiftDay}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code day} is invalid.
     */
    public static ShiftDay parseShiftDay(String day) throws ParseException {
        requireNonNull(day);
        String trimmedShiftDay = day.trim();
        if (!ShiftDay.isValidDay(trimmedShiftDay)) {
            throw new ParseException(ShiftDay.MESSAGE_CONSTRAINTS);
        }
        return new ShiftDay(day);
    }

    /**
     * Parses a {@code String time} into a {@code ShiftTime}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code time} is invalid.
     */
    public static ShiftTime parseShiftTime(String time) throws ParseException {
        requireNonNull(time);
        String trimmedShiftTime = time.trim();
        if (!ShiftTime.isValidTime(trimmedShiftTime)) {
            throw new ParseException(ShiftTime.MESSAGE_CONSTRAINTS);
        }
        return new ShiftTime((time));
    }

    /**
     * Parses a {@code String role} into a {@code Role}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code role} is invalid.
     */
    public static Role parseRole(String role) throws ParseException {
        requireNonNull(role);
        String trimmedRole = role.trim();
        if (!Role.isValidTagName(trimmedRole)) {
            throw new ParseException(Role.MESSAGE_CONSTRAINTS);
        }
        return Role.createRole(trimmedRole);
    }

    /**
     * Parses {@code Collection<String> roles} into a {@code Set<Role>}.
     */
    public static Set<Role> parseRoles(Collection<String> roles) throws ParseException {
        requireNonNull(roles);
        final Set<Role> roleSet = new HashSet<>();
        for (String tagName : roles) {
            roleSet.add(parseRole(tagName));
        }
        return roleSet;
    }

    /**
     * Parses a {@code String roleRequirement} into a {@code RoleRequirement}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code roleRequirement} is invalid.
     */
    public static RoleRequirement parseRoleRequirement(String roleRequirement) throws ParseException {
        requireNonNull(roleRequirement);
        String trimmedRoleRequirement = roleRequirement.trim();
        if (!RoleRequirement.isValidRoleRequirement(trimmedRoleRequirement)) {
            throw new ParseException(RoleRequirement.MESSAGE_CONSTRAINTS);
        }
        return new RoleRequirement(trimmedRoleRequirement);
    }

    /**
     * Parses {@code Collection<String> roleRequirements} into a {@code Set<RoleRequirement>}.
     */
    public static Set<RoleRequirement> parseRoleRequirements(
            Collection<String> roleRequirements) throws ParseException {
        requireNonNull(roleRequirements);
        final Set<RoleRequirement> roleRequirementSet = new HashSet<>();
        for (String roleRequirementString : roleRequirements) {
            roleRequirementSet.add(parseRoleRequirement(roleRequirementString));
        }
        return roleRequirementSet;
    }
}
