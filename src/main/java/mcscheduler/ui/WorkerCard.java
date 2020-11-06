package mcscheduler.ui;

import java.util.Comparator;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import mcscheduler.model.assignment.Assignment;
import mcscheduler.model.role.Leave;
import mcscheduler.model.worker.Worker;

/**
 * An UI component that displays information of a {@code Worker}.
 */
public class WorkerCard extends UiPart<Region> {

    private static final String FXML = "WorkerListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/mcscheduler-level4/issues/336">The issue on McScheduler level 4</a>
     */

    public final Worker worker;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label pay;
    @FXML
    private FlowPane roles;
    @FXML
    private FlowPane workerAssignments;
    @FXML
    private FlowPane leaveWorkerAssignments;
    @FXML
    private FlowPane unavailableTimings;

    /**
     * Creates a {@code WorkerCode} with the given {@code Worker} and index to display.
     */
    public WorkerCard(Worker worker, int displayedIndex, ObservableList<Assignment> assignmentList) {
        super(FXML);
        this.worker = worker;
        id.setText(displayedIndex + ". ");
        name.setText(worker.getName().fullName);
        phone.setText(worker.getPhone().toReadableString());
        address.setText(worker.getAddress().value);
        pay.setText(worker.getPay().toString());
        worker.getRoles().stream()
                .sorted(Comparator.comparing(role -> role.roleName))
                .forEach(role -> roles.getChildren().add(new Label(role.roleName)));

        WorkerAssignmentListPanel assignmentListPanel = new WorkerAssignmentListPanel(
                assignmentList.filtered(assignment -> !Leave.isLeave(assignment.getRole())), worker);
        workerAssignments.getChildren().add(assignmentListPanel.getRoot());

        WorkerAssignmentListPanel leaveAssignmentListPanel = new WorkerAssignmentListPanel(
                assignmentList.filtered(assignment -> Leave.isLeave(assignment.getRole())), worker);
        leaveWorkerAssignments.getChildren().add(leaveAssignmentListPanel.getRoot());

        worker.getUnavailableTimings()
                .forEach(unavailability -> unavailableTimings.getChildren().add(new Label(
                        unavailability.getDay() + " " + unavailability.getTime())));
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof WorkerCard)) {
            return false;
        }

        // state check
        WorkerCard card = (WorkerCard) other;
        return id.getText().equals(card.id.getText())
                && worker.equals(card.worker);
    }
}
