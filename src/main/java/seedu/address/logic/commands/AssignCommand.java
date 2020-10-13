package seedu.address.logic.commands;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ROLE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SHIFT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_WORKER;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_SHIFTS;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.shift.Shift;
import seedu.address.model.tag.Role;

/**
 * Assigns a worker to a shift.
 */
public class AssignCommand extends Command {
    public static final String COMMAND_WORD = "assign";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Assigns the specified worker to the specified shift"
            + "by the index numbers used in the last worker and shift listings. "
            + "\nParameters: "
            + PREFIX_SHIFT + "SHIFT_INDEX (must be a positive integer) "
            + PREFIX_WORKER + "WORKER_INDEX (must be a positive integer) "
            + PREFIX_ROLE + "ROLE\n"
            + "Example: " + COMMAND_WORD
            + " s/4 "
            + "w/1 "
            + "r/Cashier";

    public static final String MESSAGE_ASSIGN_SUCCESS = "New shift assignment added:\n"
            + "Shift: %1$s, Worker: %2$s, Role: %3$s";

    private final Index shiftIndex;
    private final Index workerIndex;
    private final Role role;

    /**
     * Creates an AssignCommand to assign the specified {@code Person} to the specified {@code Shift}.
     *
     * @param shiftIndex of the shift in the filtered shift list to assign the worker to.
     * @param workerIndex of the worker in the filtered worker list to assign to the shift.
     * @param role of the person in the shift.
     */
    public AssignCommand(Index shiftIndex, Index workerIndex, Role role) {
        requireAllNonNull(shiftIndex, workerIndex, role);

        this.shiftIndex = shiftIndex;
        this.workerIndex = workerIndex;
        this.role = role;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        List<Person> lastShownPersonList = model.getFilteredPersonList();
        List<Shift> lastShownShiftList = model.getFilteredShiftList();

        if (shiftIndex.getZeroBased() >= lastShownShiftList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_SHIFT_DISPLAYED_INDEX);
        }
        if (workerIndex.getZeroBased() >= lastShownPersonList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person workerToAssign = lastShownPersonList.get(workerIndex.getZeroBased());
        Shift shiftToAssign = lastShownShiftList.get(shiftIndex.getZeroBased());

        Person assignedPerson = createAssignedPerson(workerToAssign, shiftToAssign, role);
        Shift assignedShift = createAssignedShift(shiftToAssign, workerToAssign, role);

        model.setPerson(workerToAssign, assignedPerson);
        model.setShift(shiftToAssign, assignedShift);

        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        model.updateFilteredShiftList(PREDICATE_SHOW_ALL_SHIFTS);

        return new CommandResult(String.format(MESSAGE_ASSIGN_SUCCESS, assignedShift.toCondensedString(),
                assignedPerson.getName(), role.getRole()));
    }

    /**
     * Creates and returns a {@code Person} with the {@code shiftToAssign} and {@code role} assigned.
     */
    private static Person createAssignedPerson(Person personToAssign, Shift shiftToAssign, Role role) {
        assert personToAssign != null;

        Map<Shift, Role> updatedShifts = new HashMap<>();
        updatedShifts.putAll(personToAssign.getShifts());
        updatedShifts.put(shiftToAssign, role);

        return new Person(personToAssign.getName(), personToAssign.getPhone(), personToAssign.getPay(),
                personToAssign.getAddress(), personToAssign.getRoles(), updatedShifts);
    }

    /**
     * Creates and returns a {@code Shift} with the {@code personToAssign} and {@code role} assigned.
     */
    private static Shift createAssignedShift(Shift shiftToAssign, Person personToAssign, Role role) {
        assert shiftToAssign != null;

        Map<Person, Role> updatedWorkers = new HashMap<>();
        updatedWorkers.putAll(shiftToAssign.getWorkers());
        updatedWorkers.put(personToAssign, role);

        return new Shift(shiftToAssign.getShiftDay(), shiftToAssign.getShiftTime(),
                shiftToAssign.getRoleRequirements(), updatedWorkers);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AssignCommand)) {
            return false;
        }

        // state check
        AssignCommand e = (AssignCommand) other;
        return shiftIndex.equals(e.shiftIndex)
                && workerIndex.equals(e.workerIndex)
                && role.equals(e.role);
    }
}
