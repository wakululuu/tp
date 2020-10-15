package seedu.address.logic.commands;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SHIFT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_WORKER;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_SHIFTS;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.ShiftRoleAssignment;
import seedu.address.model.shift.PersonRoleAssignment;
import seedu.address.model.shift.Shift;

/**
 * Removes a person from a shift.
 */
public class UnassignCommand extends Command {
    public static final String COMMAND_WORD = "unassign";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Unassigns the specified person from the specified "
            + "shift by the index numbers used in the last person and shift listings. "
            + "\nParameters: "
            + PREFIX_SHIFT + "SHIFT_INDEX (must be a positive integer) "
            + PREFIX_WORKER + "PERSON_INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD
            + " s/1 "
            + "w/4";

    public static final String MESSAGE_UNASSIGN_SUCCESS = "Shift assignment removed:\n"
            + "Shift: %1$s, Person: %2$s";

    private final Index shiftIndex;
    private final Index personIndex;

    /**
     * Creates an UnassignCommand to unassign the specified {@code Person} from the specified {@code Shift}.
     *
     * @param shiftIndex of the shift in the filtered shift list to unassign the person from.
     * @param personIndex of the person in the filtered person list to unassign from the shift.
     */
    public UnassignCommand(Index shiftIndex, Index personIndex) {
        requireAllNonNull(shiftIndex, personIndex);

        this.shiftIndex = shiftIndex;
        this.personIndex = personIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        List<Person> lastShownPersonList = model.getFilteredPersonList();
        List<Shift> lastShownShiftList = model.getFilteredShiftList();

        if (shiftIndex.getZeroBased() >= lastShownShiftList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_SHIFT_DISPLAYED_INDEX);
        }
        if (personIndex.getZeroBased() >= lastShownPersonList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToUnassign = lastShownPersonList.get(personIndex.getZeroBased());
        Shift shiftToUnassign = lastShownShiftList.get(shiftIndex.getZeroBased());

        Person unassignedPerson = createUnassignedPerson(personToUnassign, shiftToUnassign);
        Shift unassignedShift = createUnassignedShift(shiftToUnassign, personToUnassign);

        model.setPerson(personToUnassign, unassignedPerson);
        model.setShift(shiftToUnassign, unassignedShift);

        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        model.updateFilteredShiftList(PREDICATE_SHOW_ALL_SHIFTS);

        return new CommandResult(String.format(MESSAGE_UNASSIGN_SUCCESS, unassignedShift.toCondensedString(),
                unassignedPerson.getName()));
    }

    /**
     * Creates and returns a {@code Person} with the {@code shiftToUnassign} unassigned.
     */
    private static Person createUnassignedPerson(Person personToUnassign, Shift shiftToUnassign) {
        assert personToUnassign != null;

        Set<ShiftRoleAssignment> updatedShiftRoleAssignments = new HashSet<>(
                personToUnassign.getShiftRoleAssignments());
        for (ShiftRoleAssignment assignment : updatedShiftRoleAssignments) {
            Shift shift = assignment.getShift();
            if (shift.isSameShift(shiftToUnassign)) {
                updatedShiftRoleAssignments.remove(assignment);
            }
        }

        return new Person(personToUnassign.getName(), personToUnassign.getPhone(), personToUnassign.getPay(),
                personToUnassign.getAddress(), personToUnassign.getRoles(), updatedShiftRoleAssignments);
    }

    /**
     * Creates and returns a {@code Shift} with the {@code personToUnassign} unassigned.
     */
    private static Shift createUnassignedShift(Shift shiftToUnassign, Person personToUnassign) {
        assert shiftToUnassign != null;

        Set<PersonRoleAssignment> updatedPersonRoleAssignments = new HashSet<>(
                shiftToUnassign.getPersonRoleAssignments());
        for (PersonRoleAssignment assignment : updatedPersonRoleAssignments) {
            Person person = assignment.getPerson();
            if (person.isSamePerson(personToUnassign)) {
                updatedPersonRoleAssignments.remove(assignment);
            }
        }

        return new Shift(shiftToUnassign.getShiftDay(), shiftToUnassign.getShiftTime(),
                shiftToUnassign.getRoleRequirements(), updatedPersonRoleAssignments);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UnassignCommand)) {
            return false;
        }

        // state check
        UnassignCommand e = (UnassignCommand) other;
        return shiftIndex.equals(e.shiftIndex)
                && personIndex.equals(e.personIndex);
    }
}

