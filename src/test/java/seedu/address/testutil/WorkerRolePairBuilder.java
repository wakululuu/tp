package seedu.address.testutil;

import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_WORKER;

import seedu.address.commons.core.index.Index;
import seedu.address.model.assignment.WorkerRolePair;
import seedu.address.model.tag.Role;

public class WorkerRolePairBuilder {

    public static final String DEFAULT_ROLE = "Cashier";

    private Index workerIndex;
    private Role role;

    /**
     * Creates an {@code WorkerRolePairBuilder} with the default details.
     */
    public WorkerRolePairBuilder() {
        workerIndex = INDEX_FIRST_WORKER;
        role = Role.createRole(DEFAULT_ROLE);
    }

    /**
     * Initializes the WorkerRolePairBuilder with the data of {@code pairToCopy}.
     */
    public WorkerRolePairBuilder(WorkerRolePair pairToCopy) {
        workerIndex = pairToCopy.getWorkerIndex();
        role = pairToCopy.getRole();
    }

    /**
     * Sets the {@code workerIndex} of the {@code WorkerRolePair} that we are building.
     */
    public WorkerRolePairBuilder withWorkerIndex(Index workerIndex) {
        this.workerIndex = workerIndex;
        return this;
    }

    /**
     * Sets the {@code Role} of the {@code WorkerRolePair} that we are building.
     */
    public WorkerRolePairBuilder withRole(String role) {
        this.role = Role.createRole(role);
        return this;
    }

    public WorkerRolePair build() {
        return new WorkerRolePair(workerIndex, role);
    }
}
