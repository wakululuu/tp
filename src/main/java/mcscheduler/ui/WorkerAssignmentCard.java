package mcscheduler.ui;

import mcscheduler.model.assignment.Assignment;

public class WorkerAssignmentCard extends AssignmentCard {

    /**
     * Creates an {@code AssignmentCode} with the given {@code Assignment} to display.
     */
    public WorkerAssignmentCard(Assignment assignment) {
        super(assignment);
        label.setText(assignment.getShift().toCondensedString() + " [" + assignment.getRole() + "]");
    }

}
