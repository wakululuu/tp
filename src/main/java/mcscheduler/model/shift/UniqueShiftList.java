package mcscheduler.model.shift;

import static java.util.Objects.requireNonNull;

import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mcscheduler.commons.util.CollectionUtil;
import mcscheduler.model.shift.exceptions.DuplicateShiftException;
import mcscheduler.model.shift.exceptions.ShiftNotFoundException;

/**
 * A list of shifts that enforces uniqueness between its elements.
 * Supports a minimal set of shift operations.
 *
 * @see Shift#isSameShift(Shift)
 */
public class UniqueShiftList implements Iterable<Shift> {

    private final ObservableList<Shift> internalList = FXCollections.observableArrayList();
    private final ObservableList<Shift> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);

    /**
     * Returns true if the list contains an equivalent shift as {@code toCheck}.
     */
    public boolean contains(Shift toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::isSameShift);
    }

    /**
     * Returns true if the list contains a strictly equivalent shift as {@code toCheck}.
     */
    public boolean containsExact(Shift toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::equals);
    }

    /**
     * Adds a shift to the list.
     * The shift must not already exist in the list.
     */
    public void add(Shift toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateShiftException();
        }
        internalList.add(toAdd);
    }

    /**
     * Replaces the shift {@code target} in the list with {@code editedShift}.
     * {@code target} must exist in the list.
     * The shift identity of {@code editedShift} must not be the same as any other shift in the list.
     */
    public void setShift(Shift target, Shift editedShift) {
        CollectionUtil.requireAllNonNull(target, editedShift);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new ShiftNotFoundException();
        }

        if (!target.isSameShift(editedShift) && contains(editedShift)) {
            throw new DuplicateShiftException();
        }

        internalList.set(index, editedShift);
    }

    /**
     * Removes the equivalent shift from the list.
     * The shift must exist in the list.
     * @param toRemove
     */
    public void remove(Shift toRemove) {
        requireNonNull(toRemove);
        if (!internalList.remove(toRemove)) {
            throw new ShiftNotFoundException();
        }
    }

    public void setShifts(UniqueShiftList replacement) {
        requireNonNull(replacement);
        internalList.setAll(replacement.internalList);
    }

    public void setShifts(List<Shift> shifts) {
        CollectionUtil.requireAllNonNull(shifts);
        if (!shiftsAreUnique(shifts)) {
            throw new DuplicateShiftException();
        }

        internalList.setAll(shifts);
    }

    public ObservableList<Shift> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    @Override
    public Iterator<Shift> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof UniqueShiftList
                && internalList.equals(((UniqueShiftList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    private boolean shiftsAreUnique(List<Shift> shifts) {
        for (int i = 0; i < shifts.size() - 1; i++) {
            for (int j = i + 1; j < shifts.size(); j++) {
                if (shifts.get(i).isSameShift(shifts.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }
}
