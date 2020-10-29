package mcscheduler.ui;

import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import mcscheduler.model.assignment.Assignment;
import mcscheduler.model.worker.Worker;

public class WorkerAssignmentListPanel extends AssignmentListPanel {

    /**
     * Creates an {@code AssignmentListPanel} with the given {@code ObservableList} and {@code Worker}.
     */
    public WorkerAssignmentListPanel(ObservableList<Assignment> assignmentList, Worker worker) {
        super(assignmentList.filtered(assignment -> assignment.getWorker().isSameWorker(worker)));
        assignmentListView.setCellFactory(listView -> new WorkerAssignmentListViewCell());
    }

    /**
     * Custom {@code ListCell} that displays the graphics of an {@code Assignment} using an {@code AssignmentCard}.
     */
    class WorkerAssignmentListViewCell extends ListCell<Assignment> {
        @Override
        protected void updateItem(Assignment assignment, boolean empty) {
            super.updateItem(assignment, empty);

            if (empty || assignment == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new WorkerAssignmentCard(assignment).getRoot());
            }
        }
    }

}
