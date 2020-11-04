package mcscheduler.testutil;

import static mcscheduler.logic.commands.CommandTestUtil.VALID_ROLE_CASHIER;
import static mcscheduler.logic.commands.CommandTestUtil.VALID_ROLE_CHEF;
import static mcscheduler.logic.commands.CommandTestUtil.VALID_ROLE_JANITOR;

import mcscheduler.model.McScheduler;
import mcscheduler.model.assignment.Assignment;
import mcscheduler.model.role.Role;
import mcscheduler.model.shift.Shift;
import mcscheduler.model.worker.Worker;

/**
 * A utility class to help with building McScheduler objects.
 * Example usage: <br>
 * {@code McScheduler ab = new McSchedulerBuilder().withWorker("John", "Doe").build();}
 */
public class McSchedulerBuilder {

    private final McScheduler mcScheduler;

    public McSchedulerBuilder() {
        mcScheduler = new McScheduler();
    }

    public McSchedulerBuilder(McScheduler mcScheduler) {
        this.mcScheduler = mcScheduler;
    }

    /**
     * Returns an {@code McScheduler} with all the typical workers, shifts and roles.
     */
    public static McScheduler getTypicalMcScheduler() {
        McScheduler ab = new McScheduler();
        for (Worker worker : TypicalWorkers.getTypicalWorkers()) {
            ab.addWorker(worker);
        }
        for (Shift shift : TypicalShifts.getTypicalShifts()) {
            ab.addShift(shift);
        }
        ab.addRole(Role.createRole(VALID_ROLE_CASHIER));
        ab.addRole(Role.createRole(VALID_ROLE_CHEF));
        ab.addRole(Role.createRole(VALID_ROLE_JANITOR));
        return ab;
    }

    /**
     * Returns an {@code McScheduler} with all the typical workers, shifts, assignments and roles.
     */
    public static McScheduler getTypicalMcSchedulerWithAssignments() {
        McScheduler ab = new McScheduler();
        for (Worker worker : TypicalWorkers.getTypicalWorkers()) {
            ab.addWorker(worker);
        }
        for (Shift shift : TypicalShifts.getTypicalShifts()) {
            ab.addShift(shift);
        }
        for (Assignment assignment : TypicalAssignments.getTypicalAssignments()) {
            ab.addAssignment(assignment);
        }
        ab.addRole(Role.createRole(VALID_ROLE_CASHIER));
        ab.addRole(Role.createRole(VALID_ROLE_CHEF));
        ab.addRole(Role.createRole(VALID_ROLE_JANITOR));
        return ab;
    }

    /**
     * Adds a new {@code Worker} to the {@code McScheduler} that we are building.
     */
    public McSchedulerBuilder withWorker(Worker worker) {
        mcScheduler.addWorker(worker);
        return this;
    }

    /**
     * Adds a new {@code Shift} to the {@code McScheduler} that we are building.
     */
    public McSchedulerBuilder withShift(Shift shift) {
        mcScheduler.addShift(shift);
        return this;
    }

    public McScheduler build() {
        return mcScheduler;
    }
}
