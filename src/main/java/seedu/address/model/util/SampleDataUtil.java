package seedu.address.model.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import seedu.address.logic.parser.ParserUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.shift.RoleRequirement;
import seedu.address.model.shift.Shift;
import seedu.address.model.shift.ShiftDay;
import seedu.address.model.shift.ShiftTime;
import seedu.address.model.tag.Role;
import seedu.address.model.worker.Address;
import seedu.address.model.worker.Name;
import seedu.address.model.worker.Pay;
import seedu.address.model.worker.Phone;
import seedu.address.model.worker.ShiftRoleAssignment;
import seedu.address.model.worker.Unavailability;
import seedu.address.model.worker.Worker;
//import seedu.address.model.worker.Email;

//import seedu.address.model.tag.Tag;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {
    public static Worker[] getSampleWorkers() {
        return new Worker[] {
            new Worker(new Name("Alex Yeoh"), new Phone("87438807"), new Pay("10"),
                    new Address("Blk 30 Geylang Street 29, #06-40"),
                    getRoleSet("cashier"),
                    getUnavailabilitySet("MON PM")),
            new Worker(new Name("Bernice Yu"), new Phone("99272758"), new Pay("11.2"),
                    new Address("Blk 30 Lorong 3 Serangoon Gardens, #07-18"),
                    getRoleSet("cashier", "cleaner"),
                    getUnavailabilitySet("TUE AM")),
            new Worker(new Name("Charlotte Oliveiro"), new Phone("93210283"), new Pay("9.87"),
                new Address("Blk 11 Ang Mo Kio Street 74, #11-04"),
                getRoleSet("chef"),
                    getUnavailabilitySet("MON AM", "TUE PM")),
            new Worker(new Name("David Li"), new Phone("91031282"), new Pay("0.99"),
                new Address("Blk 436 Serangoon Gardens Street 26, #16-43"),
                getRoleSet("chef"),
                    getUnavailabilitySet("FRI AM")),
            new Worker(new Name("Irfan Ibrahim"), new Phone("92492021"), new Pay("23"),
                new Address("Blk 47 Tampines Street 20, #17-35"),
                getRoleSet("cashier", "chef"),
                    getUnavailabilitySet("SAT AM", "SUN AM")),
            new Worker(new Name("Roy Balakrishnan"), new Phone("92624417"), new Pay("11.10"),
                new Address("Blk 45 Aljunied Street 85, #11-31"),
                getRoleSet("cleaner"),
                    getUnavailabilitySet("SUN AM", "MON AM"))
        };
    }

    public static Shift[] getSampleShifts() {
        return new Shift[] {
            new Shift(new ShiftDay("Mon"), new ShiftTime("AM"),
                getRoleRequirementSet("Cashier 1"))
        };
    }

    public static ReadOnlyAddressBook getSampleAddressBook() {
        AddressBook sampleAb = new AddressBook();
        for (Worker sampleWorker : getSampleWorkers()) {
            sampleAb.addWorker(sampleWorker);
        }
        for (Shift sampleShift : getSampleShifts()) {
            sampleAb.addShift(sampleShift);
        }
        return sampleAb;
    }

    /**
     * Returns a role set containing the list of strings given.
     */
    public static Set<Role> getRoleSet(String... strings) {
        return Arrays.stream(strings)
                .map(Role::new)
                .collect(Collectors.toSet());
    }

    public static Set<Unavailability> getUnavailabilitySet(String... strings) {
        return Arrays.stream(strings)
                .map(Unavailability::new)
                .collect(Collectors.toSet());
    }

    /**
     * Returns a role requirement set containing the list of strings given.
     */
    public static Set<RoleRequirement> getRoleRequirementSet(String... string) {
        return Arrays.stream(string)
                .map(RoleRequirement::new)
                .collect(Collectors.toSet());
    }

    /**
     * Returns a {@code ShiftRoleAssignment} set containing the list of strings given.
     */
    public static Set<ShiftRoleAssignment> getShiftRoleAssignmentSet(String... string) {
        final Pattern format = Pattern.compile("(?<shiftDay>\\S+), (?<shiftTime>\\S+), (?<role>.+)");

        Set<ShiftRoleAssignment> shiftRoleAssignments = new HashSet<>();
        for (String str : string) {
            Matcher matcher = format.matcher(str);
            if (!matcher.matches()) {
                return null;
            }

            try {
                ShiftDay shiftDay = ParserUtil.parseShiftDay(matcher.group("shiftDay"));
                ShiftTime shiftTime = ParserUtil.parseShiftTime(matcher.group("shiftTime"));
                Set<RoleRequirement> roleRequirements = ParserUtil.parseRoleRequirements(new HashSet<>());
                Shift shift = new Shift(shiftDay, shiftTime, roleRequirements);
                Role role = ParserUtil.parseRole(matcher.group("role"));
                shiftRoleAssignments.add(new ShiftRoleAssignment(shift, role));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return shiftRoleAssignments;
    }

}
