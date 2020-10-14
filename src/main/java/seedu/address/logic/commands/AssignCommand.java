package seedu.address.logic.commands;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ROLE;
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
import seedu.address.model.tag.Role;

/**
 * Assigns a person to a shift.
 */
public class AssignCommand extends Command {
    public static final String COMMAND_WORD = "assign";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Assigns the specified person to the specified shift"
            + "by the index numbers used in the last person and shift listings. "
            + "\nParameters: "
            + PREFIX_SHIFT + "SHIFT_INDEX (must be a positive integer) "
            + PREFIX_WORKER + "PERSON_INDEX (must be a positive integer) "
            + PREFIX_ROLE + "ROLE\n"
            + "Example: " + COMMAND_WORD
            + " s/4 "
            + "w/1 "
            + "r/Cashier";

    public static final String MESSAGE_ASSIGN_SUCCESS = "New shift assignment added:\n"
            + "Shift: %1$s, Person: %2$s, Role: %3$s";

    private final Index shiftIndex;
    private final Index personIndex;
    private final Role role;

    /**
     * Creates an AssignCommand to assign the specified {@code Person} to the specified {@code Shift}.
     *
     * @param shiftIndex of the shift in the filtered shift list to assign the person to.
     * @param personIndex of the person in the filtered person list to assign to the shift.
     * @param role of the person in the shift.
     */
    public AssignCommand(Index shiftIndex, Index personIndex, Role role) {
        requireAllNonNull(shiftIndex, personIndex, role);

        this.shiftIndex = shiftIndex;
        this.personIndex = personIndex;
        this.role = role;
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

        Person personToAssign = lastShownPersonList.get(personIndex.getZeroBased());
        Shift shiftToAssign = lastShownShiftList.get(shiftIndex.getZeroBased());

        Person assignedPerson = createAssignedPerson(personToAssign, shiftToAssign, role);
        Shift assignedShift = createAssignedShift(shiftToAssign, personToAssign, role);

        model.setPerson(personToAssign, assignedPerson);
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

        Set<ShiftRoleAssignment> updatedShiftRoleAssignments = new HashSet<>(
                personToAssign.getShiftRoleAssignments());

        ShiftRoleAssignment shiftRoleToAssign = new ShiftRoleAssignment(shiftToAssign, role);
        updatedShiftRoleAssignments.add(shiftRoleToAssign);

        return new Person(personToAssign.getName(), personToAssign.getPhone(), personToAssign.getPay(),
                personToAssign.getAddress(), personToAssign.getRoles(), updatedShiftRoleAssignments);
    }

    /**
     * Creates and returns a {@code Shift} with the {@code personToAssign} and {@code role} assigned.
     */
    private static Shift createAssignedShift(Shift shiftToAssign, Person personToAssign, Role role) {
        assert shiftToAssign != null;

        Set<PersonRoleAssignment> updatedPersonRoleAssignments = new HashSet<>(
                shiftToAssign.getPersonRoleAssignments());

        PersonRoleAssignment personRoleToAssign = new PersonRoleAssignment(personToAssign, role);
        updatedPersonRoleAssignments.add(personRoleToAssign);

        return new Shift(shiftToAssign.getShiftDay(), shiftToAssign.getShiftTime(),
                shiftToAssign.getRoleRequirements(), updatedPersonRoleAssignments);
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
                && personIndex.equals(e.personIndex)
                && role.equals(e.role);
    }
}
