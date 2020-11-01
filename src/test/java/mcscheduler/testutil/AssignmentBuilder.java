package mcscheduler.testutil;

import mcscheduler.model.assignment.Assignment;
import mcscheduler.model.role.Role;
import mcscheduler.model.shift.Shift;
import mcscheduler.model.worker.Worker;

public class AssignmentBuilder {

    public static final String DEFAULT_ROLE = "Cashier";

    private Shift shift;
    private Worker worker;
    private Role role;

    /**
     * Creates an {@code AssignmentBuilder} with the default details.
     */
    public AssignmentBuilder() {
        shift = new ShiftBuilder().build();
        worker = new WorkerBuilder().build();
        role = Role.createRole(DEFAULT_ROLE);
    }

    /**
     * Initializes the AssignmentBuilder with the data of {@code assignmentToCopy}.
     */
    public AssignmentBuilder(Assignment assignmentToCopy) {
        shift = assignmentToCopy.getShift();
        worker = assignmentToCopy.getWorker();
        role = assignmentToCopy.getRole();
    }

    /**
     * Sets the {@code Shift} of the {@code Assignment} that we are building.
     */
    public AssignmentBuilder withShift(Shift shift) {
        this.shift = shift;
        return this;
    }

    /**
     * Sets the {@code Worker} of the {@code Assignment} that we are building.
     */
    public AssignmentBuilder withWorker(Worker worker) {
        this.worker = worker;
        return this;
    }

    /**
     * Sets the {@code Role} of the {@code Assignment} that we are building.
     */
    public AssignmentBuilder withRole(String role) {
        this.role = Role.createRole(role);
        return this;
    }

    public Assignment build() {
        return new Assignment(shift, worker, role);
    }

}
