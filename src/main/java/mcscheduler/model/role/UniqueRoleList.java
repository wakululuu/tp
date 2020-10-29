package mcscheduler.model.role;

import static java.util.Objects.requireNonNull;

import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mcscheduler.commons.util.CollectionUtil;
import mcscheduler.model.role.exceptions.DuplicateRoleException;
import mcscheduler.model.role.exceptions.RoleNotFoundException;

/**
 * A list of roles that enforces uniqueness between its elements and does not allow nulls.
 * An role is considered unique by comparing using {@code Role#equals(Role)}.
 *
 * Supports a minimal set of list operations.
 */
public class UniqueRoleList implements Iterable<Role> {

    private final ObservableList<Role> internalList = FXCollections.observableArrayList();
    private final ObservableList<Role> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);

    /**
     * Returns true if the list contains an equivalent role as the given argument.
     */
    public boolean contains(Role toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::equals);
    }

    /**
     * Adds a role to the list.
     * The role must not already exist in the list.
     */
    public void add(Role toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateRoleException();
        }
        internalList.add(toAdd);
    }

    /**
     * Replaces the role {@code target} in the list with {@code editedRole}.
     * {@code target} must exist in the list.
     * The role identity of {@code editedRole} must not be the same as another existing role in the
     * list.
     */
    public void setRole(Role target, Role editedRole) {
        CollectionUtil.requireAllNonNull(target, editedRole);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new RoleNotFoundException();
        }

        if (!target.equals(editedRole) && contains(editedRole)) {
            throw new DuplicateRoleException();
        }

        internalList.set(index, editedRole);
    }

    /**
     * Removes the equivalent role from the list.
     * The role must exist in the list.
     */
    public void remove(Role toRemove) {
        requireNonNull(toRemove);
        if (!internalList.remove(toRemove)) {
            throw new RoleNotFoundException();
        }
    }

    public void setRoles(UniqueRoleList replacement) {
        requireNonNull(replacement);
        internalList.setAll(replacement.internalList);
    }

    /**
     * Replaces the contents of this list with {@code roles}.
     * {@code roles} must not contain duplicate roles.
     */
    public void setRoles(List<Role> roles) {
        CollectionUtil.requireAllNonNull(roles);
        if (!rolesAreUnique(roles)) {
            throw new DuplicateRoleException();
        }

        internalList.setAll(roles);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Role> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    @Override
    public Iterator<Role> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniqueRoleList // instanceof handles nulls
                && internalList.equals(((UniqueRoleList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    /**
     * Returns true if {@code roles} contains only unique roles.
     */
    private boolean rolesAreUnique(List<Role> roles) {
        for (int i = 0; i < roles.size() - 1; i++) {
            for (int j = i + 1; j < roles.size(); j++) {
                if (roles.get(i).equals(roles.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }
}
