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
import mcscheduler.model.worker.Worker;

/**
 * Panel containing the list of workers.
 */
public class WorkerListPanel extends UiPart<Region> {
    private static final String FXML = "WorkerListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(WorkerListPanel.class);
    private final ObservableList<Assignment> assignmentList;

    @FXML
    private ListView<Worker> workerListView;

    /**
     * Creates a {@code WorkerListPanel} with the given {@code ObservableList}.
     */
    public WorkerListPanel(ObservableList<Worker> workerList, ObservableList<Assignment> assignmentList) {
        super(FXML);
        workerListView.setItems(workerList);
        workerListView.setCellFactory(listView -> new WorkerListViewCell());
        this.assignmentList = assignmentList;
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Worker} using a {@code WorkerCard}.
     */
    class WorkerListViewCell extends ListCell<Worker> {
        @Override
        protected void updateItem(Worker worker, boolean empty) {
            super.updateItem(worker, empty);

            if (empty || worker == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new WorkerCard(worker, getIndex() + 1, assignmentList).getRoot());
            }
        }
    }

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
