package mcscheduler.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import mcscheduler.model.assignment.Assignment;

/**
 * An UI component that displays information of an {@code Assignment}.
 */
public class AssignmentCard extends UiPart<Region> {

    private static final String FXML = "AssignmentListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/mcscheduler-level4/issues/336">The issue on McScheduler level 4</a>
     */

    public final Assignment assignment;

    @FXML
    protected Label label;
    @FXML
    private HBox cardPane;

    /**
     * Creates an {@code AssignmentCode} with the given {@code Assignment} to display.
     */
    public AssignmentCard(Assignment assignment) {
        super(FXML);
        this.assignment = assignment;
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AssignmentCard)) {
            return false;
        }

        // state check
        AssignmentCard card = (AssignmentCard) other;
        return assignment.equals(card.assignment);
    }
}
