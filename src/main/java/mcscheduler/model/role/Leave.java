package mcscheduler.model.role;

public class Leave extends Role {

    public static final String ROLE_NAME = "Leave";

    public Leave() {
        super("Leave");
    }

    /**
     * Returns true if role is non-null and is a leave.
     */
    public static boolean isLeave(Role role) {
        return role != null
                && role.roleName.equalsIgnoreCase(ROLE_NAME);
    }

    /**
     * In this implementation, a {@code Role} with {@code tagName == Leave.ROLE_NAME} is equal to {@code Leave}.
     * Future implementations which require a distinction between them should edit the below implementation.
     */
    @Override
    public boolean equals(Object other) {
        return other == this
                || other instanceof Leave
                || (other instanceof Role
                && ((Role) other).roleName.equalsIgnoreCase(ROLE_NAME));
    }

}
