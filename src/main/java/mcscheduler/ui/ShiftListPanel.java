package mcscheduler.ui;

import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import mcscheduler.commons.core.LogsCenter;
import mcscheduler.model.assignment.Assignment;
import mcscheduler.model.shift.Shift;

public class ShiftListPanel extends UiPart<Region> {
    private static final String FXML = "ShiftListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(ShiftListPanel.class);
    private final ObservableList<Assignment> assignmentList;

    @FXML
    private ListView<Shift> shiftListView;

    /**
     * Creates a {@code ShiftListPanel} with the given {@code ObservableList}.
     */
    public ShiftListPanel(ObservableList<Shift> shiftList, ObservableList<Assignment> assignmentList) {
        super(FXML);
        shiftListView.setItems(shiftList);
        shiftListView.setCellFactory(shiftView -> new ShiftListViewCell());
        this.assignmentList = assignmentList;
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Shift} using a {@code ShiftCard}.
     */
    class ShiftListViewCell extends ListCell<Shift> {
        @Override
        protected void updateItem(Shift shift, boolean empty) {
            super.updateItem(shift, empty);

            if (empty || shift == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new ShiftCard(shift, getIndex() + 1, assignmentList).getRoot());
            }
        }
    }
}
