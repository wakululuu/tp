package mcscheduler.ui;

import mcscheduler.model.assignment.Assignment;

public class ShiftAssignmentCard extends AssignmentCard {

    /**
     * Creates an {@code AssignmentCode} with the given {@code Assignment} to display.
     */
    public ShiftAssignmentCard(Assignment assignment) {
        super(assignment);
        label.setText(assignment.getWorker().getName() + " [" + assignment.getRole() + "]");
    }

}
