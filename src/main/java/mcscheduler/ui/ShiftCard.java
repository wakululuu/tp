package mcscheduler.ui;

import java.util.Comparator;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import mcscheduler.model.assignment.Assignment;
import mcscheduler.model.role.Leave;
import mcscheduler.model.shift.Shift;

/**
 * An UI component that displays information of a {@code Shift}.
 */
public class ShiftCard extends UiPart<Region> {

    private static final String FXML = "ShiftListCard.fxml";

    public final Shift shift;

    @FXML
    private HBox cardPane;
    @FXML
    private Label dayTime;
    @FXML
    private Label id;
    @FXML
    private VBox roleRequirements;
    @FXML
    private FlowPane shiftAssignments;
    @FXML
    private FlowPane leaveShiftAssignments;

    /**
     * Creates a {@code ShiftCard} with the given {@code Shift} and index to display.
     */
    public ShiftCard(Shift shift, int displayedIndex, ObservableList<Assignment> assignmentList) {
        super(FXML);
        this.shift = shift;

        id.setText(displayedIndex + ". ");
        dayTime.setText(shift.getShiftDay().toString() + " " + shift.getShiftTime().toString());
        shift.getRoleRequirements().stream()
                .sorted(Comparator.comparing(roleRequirement -> roleRequirement.getRole().getRole()))
                .forEach(roleRequirement -> {
                    Label childNode = new Label(roleRequirement.toString());
                    childNode.setWrapText(true);
                    roleRequirements.getChildren().add(childNode);
                });

        ShiftAssignmentListPanel assignmentListPanel = new ShiftAssignmentListPanel(
                assignmentList.filtered(assignment -> !Leave.isLeave(assignment.getRole())), shift);
        shiftAssignments.getChildren().add(assignmentListPanel.getRoot());

        ShiftAssignmentListPanel leaveAssignmentListPanel = new ShiftAssignmentListPanel(
                assignmentList.filtered(assignment -> Leave.isLeave(assignment.getRole())), shift);
        leaveShiftAssignments.getChildren().add(leaveAssignmentListPanel.getRoot());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof ShiftCard)) {
            return false;
        }

        ShiftCard card = (ShiftCard) other;
        return id.getText().equals(card.id.getText())
                && shift.equals(card.shift);
    }

}
