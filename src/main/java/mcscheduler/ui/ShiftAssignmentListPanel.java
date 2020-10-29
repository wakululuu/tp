package mcscheduler.ui;

import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import mcscheduler.model.assignment.Assignment;
import mcscheduler.model.shift.Shift;

public class ShiftAssignmentListPanel extends AssignmentListPanel {

    /**
     * Creates an {@code AssignmentListPanel} with the given {@code ObservableList} and {@code Shift}.
     */
    public ShiftAssignmentListPanel(ObservableList<Assignment> assignmentList, Shift shift) {
        super(assignmentList.filtered(assignment -> assignment.getShift().isSameShift(shift)));
        assignmentListView.setCellFactory(listView -> new ShiftAssignmentListViewCell());
    }

    /**
     * Custom {@code ListCell} that displays the graphics of an {@code Assignment} using an {@code AssignmentCard}.
     */
    class ShiftAssignmentListViewCell extends ListCell<Assignment> {
        @Override
        protected void updateItem(Assignment assignment, boolean empty) {
            super.updateItem(assignment, empty);

            if (empty || assignment == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new ShiftAssignmentCard(assignment).getRoot());
            }
        }
    }
}
