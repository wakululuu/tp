package seedu.address.ui;

import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.worker.Worker;

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
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
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
    private FlowPane shiftRoleAssignments;
    @FXML
    private FlowPane shiftLeaveAssignments;

    /**
     * Creates a {@code WorkerCode} with the given {@code Worker} and index to display.
     */
    public WorkerCard(Worker worker, int displayedIndex) {
        super(FXML);
        this.worker = worker;
        id.setText(displayedIndex + ". ");
        name.setText(worker.getName().fullName);
        phone.setText(worker.getPhone().value);
        address.setText(worker.getAddress().value);
        pay.setText(worker.getPay().toString());
        //email.setText(worker.getEmail().value);
        worker.getRoles().stream()
                .sorted(Comparator.comparing(role -> role.tagName))
                .forEach(role -> roles.getChildren().add(new Label(role.tagName)));
        worker.getShiftRoleAssignmentsWithoutLeave()
                .forEach(shiftRoleAssignment -> shiftRoleAssignments.getChildren().add(new Label(
                        shiftRoleAssignment.getShift().toCondensedString() + " " + shiftRoleAssignment.getRole()
                )));
        worker.getShiftLeaveAssignments()
                .forEach(shiftLeaveAssignment -> shiftLeaveAssignments.getChildren().add(new Label(
                        shiftLeaveAssignment.getShift().toCondensedString() + " " + shiftLeaveAssignment.getRole()
                )));
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
