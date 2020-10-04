package seedu.address.ui;

import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.shift.Shift;

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
    private FlowPane roleRequirements;

    /**
     * Creates a {@code ShiftCard} with the given {@code Shift} and index to display.
     */
    public ShiftCard(Shift shift, int displayedIndex) {
        super(FXML);
        this.shift = shift;
        id.setText(displayedIndex + ". ");
        dayTime.setText(shift.getShiftDay().toString() + " " + shift.getShiftTime().toString());
        shift.getRoleRequirements().stream()
                .sorted(Comparator.comparing(roleRequirement -> roleRequirement.getRole().getRole()))
                .forEach(roleRequirement -> roleRequirements.getChildren().add(new Label(roleRequirement.toString())));
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
