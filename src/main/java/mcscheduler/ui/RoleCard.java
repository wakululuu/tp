package mcscheduler.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import mcscheduler.model.role.Role;

/**
 * An UI component that displays information of a {@code Role}.
 */
public class RoleCard extends UiPart<Region> {

    private static final String FXML = "RoleListCard.fxml";

    public final Role role;

    @FXML
    private HBox cardPane;
    @FXML
    private Label roleLabel;
    @FXML
    private Label id;

    /**
     * Creates a {@code RoleCard} with the given {@code Role} and index to display.
     */
    public RoleCard(Role role, int displayedIndex) {
        super(FXML);
        this.role = role;
        id.setText(displayedIndex + ". ");
        roleLabel.setText(role.getRole());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof RoleCard)) {
            return false;
        }

        RoleCard card = (RoleCard) other;
        return id.getText().equals(card.id.getText())
                && role.equals(card.role);
    }

}
