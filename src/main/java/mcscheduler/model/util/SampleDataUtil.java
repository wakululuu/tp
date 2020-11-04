package mcscheduler.model.util;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import mcscheduler.model.McScheduler;
import mcscheduler.model.ReadOnlyMcScheduler;
import mcscheduler.model.assignment.Assignment;
import mcscheduler.model.role.Role;
import mcscheduler.model.shift.RoleRequirement;
import mcscheduler.model.shift.Shift;
import mcscheduler.model.shift.ShiftDay;
import mcscheduler.model.shift.ShiftTime;
import mcscheduler.model.worker.Address;
import mcscheduler.model.worker.Name;
import mcscheduler.model.worker.Pay;
import mcscheduler.model.worker.Phone;
import mcscheduler.model.worker.Unavailability;
import mcscheduler.model.worker.Worker;

//@@author
/**
 * Contains utility methods for populating {@code McScheduler} with sample data.
 */
public class SampleDataUtil {

    private static final Worker SAMPLE_WORKER_ALEX = new Worker(
            new Name("Alex Yeoh"), new Phone("87438807"), new Pay("10"),
            new Address("Blk 30 Geylang Street 29, #06-40"),
            getRoleSet("cashier"),
            getUnavailabilitySet("MON PM"));
    private static final Worker SAMPLE_WORKER_BERNICE = new Worker(
            new Name("Bernice Yu"), new Phone("99272758"), new Pay("11.2"),
            new Address("Blk 30 Lorong 3 Serangoon Gardens, #07-18"),
            getRoleSet("cashier", "janitor"),
            getUnavailabilitySet("TUE AM"));
    private static final Worker SAMPLE_WORKER_CHARLOTTE = new Worker(
            new Name("Charlotte Oliveiro"), new Phone("93210283"), new Pay("9.87"),
            new Address("Blk 11 Ang Mo Kio Street 74, #11-04"),
            getRoleSet("chef"),
            getUnavailabilitySet("MON AM", "TUE PM"));
    private static final Worker SAMPLE_WORKER_DAVID = new Worker(
            new Name("David Li"), new Phone("91031282"), new Pay("0.99"),
            new Address("Blk 436 Serangoon Gardens Street 26, #16-43"),
            getRoleSet("chef"),
            getUnavailabilitySet("FRI AM"));
    private static final Worker SAMPLE_WORKER_IRFAN = new Worker(
            new Name("Irfan Ibrahim"), new Phone("92492021"), new Pay("23"),
            new Address("Blk 47 Tampines Street 20, #17-35"),
            getRoleSet("cashier", "chef"),
            getUnavailabilitySet("SAT AM", "SUN AM"));
    private static final Worker SAMPLE_WORKER_ROY = new Worker(
            new Name("Roy Balakrishnan"), new Phone("92624417"), new Pay("11.10"),
            new Address("Blk 45 Aljunied Street 85, #11-31"),
            getRoleSet("janitor"),
            getUnavailabilitySet("SUN AM", "MON AM"));

    private static final Shift SAMPLE_SHIFT_MON_AM = new Shift(
            new ShiftDay("Mon"), new ShiftTime("AM"), getRoleRequirementSet("Cashier 2 1"));
    private static final Shift SAMPLE_SHIFT_TUE_PM = new Shift(
            new ShiftDay("Tue"), new ShiftTime("PM"), getRoleRequirementSet("janitor 2 2"));

    private static final Role SAMPLE_ROLE_CASHIER = Role.createRole("cashier");
    private static final Role SAMPLE_ROLE_CHEF = Role.createRole("chef");
    private static final Role SAMPLE_ROLE_JANITOR = Role.createRole("janitor");

    private static final Assignment SAMPLE_ASSIGNMENT_1 = new Assignment(
            SAMPLE_SHIFT_MON_AM, SAMPLE_WORKER_ALEX, SAMPLE_ROLE_CASHIER);
    private static final Assignment SAMPLE_ASSIGNMENT_2 = new Assignment(
            SAMPLE_SHIFT_TUE_PM, SAMPLE_WORKER_BERNICE, SAMPLE_ROLE_JANITOR);
    private static final Assignment SAMPLE_ASSIGNMENT_3 = new Assignment(
            SAMPLE_SHIFT_TUE_PM, SAMPLE_WORKER_ROY, SAMPLE_ROLE_JANITOR);

    public static Worker[] getSampleWorkers() {
        return new Worker[] {
            SAMPLE_WORKER_ALEX, SAMPLE_WORKER_BERNICE, SAMPLE_WORKER_CHARLOTTE, SAMPLE_WORKER_DAVID,
            SAMPLE_WORKER_IRFAN, SAMPLE_WORKER_ROY
        };
    }

    public static Shift[] getSampleShifts() {
        return new Shift[] {
            SAMPLE_SHIFT_MON_AM, SAMPLE_SHIFT_TUE_PM
        };
    }

    //@@author wakululuu
    public static Role[] getSampleRoles() {
        return new Role[] {
            SAMPLE_ROLE_CASHIER, SAMPLE_ROLE_CHEF, SAMPLE_ROLE_JANITOR
        };
    }

    public static Assignment[] getSampleAssignments() {
        return new Assignment[] {
            SAMPLE_ASSIGNMENT_1, SAMPLE_ASSIGNMENT_2, SAMPLE_ASSIGNMENT_3
        };
    }

    //@@author
    public static ReadOnlyMcScheduler getSampleMcScheduler() {
        McScheduler sampleAb = new McScheduler();
        for (Worker sampleWorker : getSampleWorkers()) {
            sampleAb.addWorker(sampleWorker);
        }
        for (Shift sampleShift : getSampleShifts()) {
            sampleAb.addShift(sampleShift);
        }
        for (Role sampleRole : getSampleRoles()) {
            sampleAb.addRole(sampleRole);
        }
        for (Assignment sampleAssignment : getSampleAssignments()) {
            sampleAb.addAssignment(sampleAssignment);
        }
        return sampleAb;
    }

    /**
     * Returns a role set containing the list of strings given.
     */
    public static Set<Role> getRoleSet(String... strings) {
        return Arrays.stream(strings)
                .map(Role::createRole)
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

}
