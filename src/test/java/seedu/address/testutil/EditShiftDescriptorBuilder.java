package seedu.address.testutil;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.commands.ShiftEditCommand;
import seedu.address.model.shift.RoleRequirement;
import seedu.address.model.shift.Shift;
import seedu.address.model.shift.ShiftDay;
import seedu.address.model.shift.ShiftTime;



/**
 * A utility class to help with building EditShiftDescriptor objects.
 */
public class EditShiftDescriptorBuilder {

    private ShiftEditCommand.EditShiftDescriptor descriptor;

    public EditShiftDescriptorBuilder() {
        descriptor = new ShiftEditCommand.EditShiftDescriptor();
    }

    public EditShiftDescriptorBuilder(ShiftEditCommand.EditShiftDescriptor descriptor) {
        this.descriptor = new ShiftEditCommand.EditShiftDescriptor(descriptor);
    }

    /**
     * Returns an {@code EditShiftDescriptor} with fields containing {@code shift}'s details
     */
    public EditShiftDescriptorBuilder(Shift shift) {
        descriptor = new ShiftEditCommand.EditShiftDescriptor();
        descriptor.setShiftDay(shift.getShiftDay());
        descriptor.setShiftTime(shift.getShiftTime());
        descriptor.setRoleRequirements(shift.getRoleRequirements());
    }

    /**
     * Sets the {@code ShiftDay} of the {@code EditShiftDescriptor} that we are building.
     */
    public seedu.address.testutil.EditShiftDescriptorBuilder withShiftDay(String shiftDay) {
        descriptor.setShiftDay(new ShiftDay(shiftDay));
        return this;
    }

    /**
     * Sets the {@code ShiftTime} of the {@code EditShiftDescriptor} that we are building.
     */
    public seedu.address.testutil.EditShiftDescriptorBuilder withShiftTime(String shiftTime) {
        descriptor.setShiftTime(new ShiftTime(shiftTime));
        return this;
    }

    /**
     * Parses the {@code roleRequirements} into a {@code Set<RoleRequirement>} and set it to
     * the {@code EditShiftDescriptor} that we are building.
     */
    public seedu.address.testutil.EditShiftDescriptorBuilder withRoleRequirements(String... roleRequirements) {
        Set<RoleRequirement> roleRequirementsSet = Stream.of(roleRequirements)
                .map(RoleRequirement::new).collect(Collectors.toSet());
        descriptor.setRoleRequirements(roleRequirementsSet);
        return this;
    }

    public ShiftEditCommand.EditShiftDescriptor build() {
        return descriptor;
    }


}
