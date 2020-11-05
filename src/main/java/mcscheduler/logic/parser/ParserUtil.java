package mcscheduler.logic.parser;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import mcscheduler.commons.core.Messages;
import mcscheduler.commons.core.index.Index;
import mcscheduler.commons.util.StringUtil;
import mcscheduler.logic.parser.exceptions.ParseException;
import mcscheduler.model.assignment.WorkerRolePair;
import mcscheduler.model.role.Role;
import mcscheduler.model.shift.RoleRequirement;
import mcscheduler.model.shift.ShiftDay;
import mcscheduler.model.shift.ShiftTime;
import mcscheduler.model.worker.Address;
import mcscheduler.model.worker.Name;
import mcscheduler.model.worker.Pay;
import mcscheduler.model.worker.Phone;
import mcscheduler.model.worker.Unavailability;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    public static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

    /**
     * Returns true if any of the prefixes have {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    public static boolean notAllPrefixesAbsent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).anyMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

    /**
     * Returns true if none of the prefixes have {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    public static boolean allPrefixesAbsent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isEmpty());
    }

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(String.format(Messages.MESSAGE_INVALID_DISPLAYED_INDEX, trimmedIndex));
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Set<Index> parseIndexes(Collection<String> oneBasedIndexes) throws ParseException {
        requireNonNull(oneBasedIndexes);
        final Set<Index> indexSet = new HashSet<>();
        for (String oneBasedIndex : oneBasedIndexes) {
            indexSet.add(parseIndex(oneBasedIndex));
        }
        return indexSet;
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
        if (!Role.isValidRoleName(trimmedRole)) {
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
     * Parses a {@code String unavailability} into an {@code Unavailability}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code unavailability} is invalid.
     */
    public static Unavailability parseUnavailability(String unavailability) throws ParseException {
        requireNonNull(unavailability);
        String trimmedUnavailability = unavailability.trim();
        if (!Unavailability.isValidUnavailability(trimmedUnavailability)) {
            throw new ParseException(Unavailability.MESSAGE_CONSTRAINTS);
        }
        return new Unavailability(trimmedUnavailability);
    }

    /**
     * Generates an AM Unavailability using {@code String day}.
     */
    public static String createMorningUnavailabilityString(String day) {
        StringBuilder sb = new StringBuilder();
        sb.append(day);
        sb.append(UnavailabilitySyntax.MORNING);
        return sb.toString();
    }

    /**
     * Generates a PM Unavailability using {@code String day}.
     */
    public static String createAfternoonUnavailabilityString(String day) {
        StringBuilder sb = new StringBuilder();
        sb.append(day);
        sb.append(UnavailabilitySyntax.AFTERNOON);
        return sb.toString();
    }

    /**
     * Parses {@code Collection<String> unavailabilities} into an {@code Set<Unavailability>}.
     */
    public static Set<Unavailability> parseUnavailabilities(Collection<String> unavailabilities) throws ParseException {
        requireNonNull(unavailabilities);
        final Set<Unavailability> unavailabilitySet = new HashSet<>();
        for (String unavailability : unavailabilities) {
            if (unavailability.toUpperCase().contains(UnavailabilitySyntax.WHOLE_DAY)) {
                String[] tempString = unavailability.split(" ");
                String day = tempString[0];
                String morningUnavailability = createMorningUnavailabilityString(day);
                String afternoonUnavailability = createAfternoonUnavailabilityString(day);
                unavailabilitySet.add(parseUnavailability(morningUnavailability));
                unavailabilitySet.add(parseUnavailability(afternoonUnavailability));
            } else {
                unavailabilitySet.add(parseUnavailability(unavailability));
            }
        }
        return unavailabilitySet;
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

        int index = trimmedRoleRequirement.lastIndexOf(" ");
        Role role = parseRole(trimmedRoleRequirement.substring(0, index));
        int quantity = Integer.parseInt(trimmedRoleRequirement.substring(index + 1));

        return new RoleRequirement(role, quantity);
    }

    /**
     * Parses {@code Collection<String> roleRequirements} into a {@code Set<RoleRequirement>}.
     */
    public static Set<RoleRequirement> parseRoleRequirements(Collection<String> roleRequirements)
            throws ParseException {
        requireNonNull(roleRequirements);
        final Set<RoleRequirement> roleRequirementSet = new HashSet<>();
        for (String roleRequirementString : roleRequirements) {
            roleRequirementSet.add(parseRoleRequirement(roleRequirementString));
        }
        return roleRequirementSet;
    }

    /**
     * Parses a {@code String roleRequirement} into a {@code RoleRequirement}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code roleRequirement} is invalid.
     */
    public static WorkerRolePair parseWorkerRole(String workerRole) throws ParseException {
        requireNonNull(workerRole);
        String trimmedWorkerRole = workerRole.trim();
        if (!WorkerRolePair.isValidWorkerRolePair(trimmedWorkerRole)) {
            throw new ParseException(WorkerRolePair.MESSAGE_CONSTRAINTS);
        }
        return new WorkerRolePair(trimmedWorkerRole);
    }

    /**
     * Parses {@code Collection<String> workerRoles} into a {@code Set<WorkerRolePair>}.
     */
    public static Set<WorkerRolePair> parseWorkerRoles(
            Collection<String> workerRoles) throws ParseException {
        requireNonNull(workerRoles);
        final Set<WorkerRolePair> workerRolePairSet = new HashSet<>();
        for (String workerRoleString : workerRoles) {
            workerRolePairSet.add(parseWorkerRole(workerRoleString));
        }
        return workerRolePairSet;
    }
}
