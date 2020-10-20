package seedu.address.model.tag;

public class Leave extends Role {

    public static final String ROLE_NAME = "Leave";

    public Leave() {
        super("Leave");
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
                && ((Role) other).tagName == ROLE_NAME);
    }

}
