package mcscheduler.model.worker;

import static java.util.Objects.requireNonNull;

import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mcscheduler.commons.util.CollectionUtil;
import mcscheduler.model.worker.exceptions.DuplicateWorkerException;
import mcscheduler.model.worker.exceptions.WorkerNotFoundException;

/**
 * A list of workers that enforces uniqueness between its elements and does not allow nulls.
 * A worker is considered unique by comparing using {@code Worker#isSameWorker(Worker)}. As such, adding and updating of
 * workers uses Worker#isSameWorker(Worker) for equality so as to ensure that the worker being added or updated is
 * unique in terms of identity in the UniqueWorkerList. However, the removal of a worker uses Worker#equals(Object) so
 * as to ensure that the worker with exactly the same fields will be removed.
 *
 * Supports a minimal set of list operations.
 *
 * @see Worker#isSameWorker(Worker)
 */
public class UniqueWorkerList implements Iterable<Worker> {

    private final ObservableList<Worker> internalList = FXCollections.observableArrayList();
    private final ObservableList<Worker> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);

    /**
     * Returns true if the list contains an equivalent worker as the given argument.
     */
    public boolean contains(Worker toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::isSameWorker);
    }

    /**
     * Adds a worker to the list.
     * The worker must not already exist in the list.
     */
    public void add(Worker toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateWorkerException();
        }
        internalList.add(toAdd);
    }

    /**
     * Replaces the worker {@code target} in the list with {@code editedWorker}.
     * {@code target} must exist in the list.
     * The worker identity of {@code editedWorker} must not be the same as another existing worker in the list.
     */
    public void setWorker(Worker target, Worker editedWorker) {
        CollectionUtil.requireAllNonNull(target, editedWorker);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new WorkerNotFoundException();
        }

        if (!target.isSameWorker(editedWorker) && contains(editedWorker)) {
            throw new DuplicateWorkerException();
        }

        internalList.set(index, editedWorker);
    }

    /**
     * Removes the equivalent worker from the list.
     * The worker must exist in the list.
     */
    public void remove(Worker toRemove) {
        requireNonNull(toRemove);
        if (!internalList.remove(toRemove)) {
            throw new WorkerNotFoundException();
        }
    }

    public void setWorkers(UniqueWorkerList replacement) {
        requireNonNull(replacement);
        internalList.setAll(replacement.internalList);
    }

    /**
     * Replaces the contents of this list with {@code workers}.
     * {@code workers} must not contain duplicate workers.
     */
    public void setWorkers(List<Worker> workers) {
        CollectionUtil.requireAllNonNull(workers);
        if (!workersAreUnique(workers)) {
            throw new DuplicateWorkerException();
        }

        internalList.setAll(workers);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Worker> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    @Override
    public Iterator<Worker> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniqueWorkerList // instanceof handles nulls
                        && internalList.equals(((UniqueWorkerList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    /**
     * Returns true if {@code workers} contains only unique workers.
     */
    private boolean workersAreUnique(List<Worker> workers) {
        for (int i = 0; i < workers.size() - 1; i++) {
            for (int j = i + 1; j < workers.size(); j++) {
                if (workers.get(i).isSameWorker(workers.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }
}
