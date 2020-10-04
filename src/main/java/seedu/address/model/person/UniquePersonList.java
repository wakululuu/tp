package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;

/**
 * A list of persons that enforces uniqueness between its elements and does not allow nulls.
 * A worker is considered unique by comparing using {@code Worker#isSamePerson(Worker)}. As such, adding and updating of
 * persons uses Worker#isSamePerson(Worker) for equality so as to ensure that the worker being added or updated is
 * unique in terms of identity in the UniquePersonList. However, the removal of a worker uses Worker#equals(Object) so
 * as to ensure that the worker with exactly the same fields will be removed.
 *
 * Supports a minimal set of list operations.
 *
 * @see Worker#isSamePerson(Worker)
 */
public class UniquePersonList implements Iterable<Worker> {

    private final ObservableList<Worker> internalList = FXCollections.observableArrayList();
    private final ObservableList<Worker> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);

    /**
     * Returns true if the list contains an equivalent worker as the given argument.
     */
    public boolean contains(Worker toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::isSamePerson);
    }

    /**
     * Adds a worker to the list.
     * The worker must not already exist in the list.
     */
    public void add(Worker toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicatePersonException();
        }
        internalList.add(toAdd);
    }

    /**
     * Replaces the worker {@code target} in the list with {@code editedWorker}.
     * {@code target} must exist in the list.
     * The worker identity of {@code editedWorker} must not be the same as another existing worker in the list.
     */
    public void setPerson(Worker target, Worker editedWorker) {
        requireAllNonNull(target, editedWorker);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new PersonNotFoundException();
        }

        if (!target.isSamePerson(editedWorker) && contains(editedWorker)) {
            throw new DuplicatePersonException();
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
            throw new PersonNotFoundException();
        }
    }

    public void setPersons(UniquePersonList replacement) {
        requireNonNull(replacement);
        internalList.setAll(replacement.internalList);
    }

    /**
     * Replaces the contents of this list with {@code workers}.
     * {@code workers} must not contain duplicate workers.
     */
    public void setPersons(List<Worker> workers) {
        requireAllNonNull(workers);
        if (!personsAreUnique(workers)) {
            throw new DuplicatePersonException();
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
                || (other instanceof UniquePersonList // instanceof handles nulls
                        && internalList.equals(((UniquePersonList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    /**
     * Returns true if {@code workers} contains only unique workers.
     */
    private boolean personsAreUnique(List<Worker> workers) {
        for (int i = 0; i < workers.size() - 1; i++) {
            for (int j = i + 1; j < workers.size(); j++) {
                if (workers.get(i).isSamePerson(workers.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }
}
