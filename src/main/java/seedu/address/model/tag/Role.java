package seedu.address.model.tag;

/**
 * Represents a Worker Role in the App.
 * Guarantees: immutable, name is valid as declared in
 */
public class Role extends Tag{

    public static final String MESSAGE_CONSTRAINTS =
            "Role names should be alphanumeric";

    public Role(String roleName) {
        super(roleName);
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof Role
                && tagName.equals(((Role) other).tagName));
    }

}
