package mcscheduler.ui;

import java.util.logging.Logger;

import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import mcscheduler.commons.core.LogsCenter;
import mcscheduler.model.assignment.Assignment;

/**
 * Panel containing the list of assignments.
 */
public class AssignmentListPanel extends UiPart<Region> {
    private static final String FXML = "AssignmentListPanel.fxml";

    @FXML
    protected ListView<Assignment> assignmentListView;

    private final Logger logger = LogsCenter.getLogger(AssignmentListPanel.class);

    /**
     * Creates an {@code AssignmentListPanel} with the given {@code ObservableList}.
     */
    public AssignmentListPanel(ObservableList<Assignment> assignmentList) {
        super(FXML);
        assignmentListView.setItems(assignmentList);
        assignmentListView.prefHeightProperty().bind(Bindings.size(assignmentList).multiply(18));
    }

}
