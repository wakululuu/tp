package seedu.address.model.tag;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class LeaveTest {

    @Test
    public void equals() {
        assertEquals(new Leave(), new Leave());

        // Leave should be equal to a Role with tagName == "Leave" - even though
        // allowing Role to initialize with
        assertEquals(new RoleStub("Leave"), new Leave());

        // Leave should not be equal to a Tag with tagName == "Leave"
        assertNotEquals(new Tag("Leave"), new Leave());
    }

    private class RoleStub extends Role {

        public RoleStub(String s) {
            super(s);
        }

    }

}
