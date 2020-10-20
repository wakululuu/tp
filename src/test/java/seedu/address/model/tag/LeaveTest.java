package seedu.address.model.tag;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class LeaveTest {

    @Test
    public void equals() {
        assertTrue(new Leave().equals(new Leave()));

        // Leave should be equal to a Role with tagName == "Leave" - even though
        // allowing Role to initialize with
        assertTrue(new Leave().equals(new RoleStub("Leave")));

        // Leave should not be equal to a Tag with tagName == "Leave"
        assertFalse(new Leave().equals(new Tag("Leave")));
    }

    private class RoleStub extends Role {

        public RoleStub(String s) {
            super(s);
        }

    }

}
