package seedu.address.model.tag;

public class Leave extends Role {

    public static final String ROLE_NAME = "Leave";

    public Leave() {
        super("Leave");
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || other instanceof Leave;
    }

}
