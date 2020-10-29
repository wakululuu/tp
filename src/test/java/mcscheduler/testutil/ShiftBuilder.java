package mcscheduler.testutil;

import java.util.HashSet;
import java.util.Set;

import mcscheduler.model.shift.RoleRequirement;
import mcscheduler.model.shift.Shift;
import mcscheduler.model.shift.ShiftDay;
import mcscheduler.model.shift.ShiftTime;
import mcscheduler.model.util.SampleDataUtil;

/**
 * A utility class to help with building Shift objects.
 */
public class ShiftBuilder {

    public static final String DEFAULT_DAY = "MON";
    public static final String DEFAULT_TIME = "AM";

    private ShiftDay day;
    private ShiftTime time;
    private Set<RoleRequirement> roleRequirements;

    /**
     * Creates a {@code ShiftBuilder} with the default details.
     */
    public ShiftBuilder() {
        day = new ShiftDay(DEFAULT_DAY);
        time = new ShiftTime(DEFAULT_TIME);
        roleRequirements = new HashSet<>();
    }

    /**
     * Initializes the ShiftBuilder with the data of {@code shiftToCopy}.
     */
    public ShiftBuilder(Shift shiftToCopy) {
        day = shiftToCopy.getShiftDay();
        time = shiftToCopy.getShiftTime();
        roleRequirements = new HashSet<>(shiftToCopy.getRoleRequirements());
    }

    /**
     * Sets the {@code ShiftDay} of the {@code Shift} that we are building.
     */
    public ShiftBuilder withShiftDay(String day) {
        this.day = new ShiftDay(day);
        return this;
    }

    /**
     * Sets the {@code ShiftTime} of the {@code Shift} that we are building.
     */
    public ShiftBuilder withShiftTime(String time) {
        this.time = new ShiftTime(time);
        return this;
    }

    /**
     * Parses the {@code roleRequirements} into a {@code Set<RoleRequirements>} and set it to the {@code Shift} that
     * we are building.
     */
    public ShiftBuilder withRoleRequirements(String... roleRequirements) {
        this.roleRequirements = SampleDataUtil.getRoleRequirementSet(roleRequirements);
        return this;
    }

    public Shift build() {
        return new Shift(day, time, roleRequirements);
    }

}
