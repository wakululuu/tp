package mcscheduler.model.role;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class LeaveTest {

    @Test
    public void equals() {
        assertEquals(new Leave(), new Leave());

        // Leave should be equal to a Role with roleName == "Leave" - even though
        // allowing Role to initialize with
        assertEquals(new RoleStub("Leave"), new Leave());
    }

    private class RoleStub extends Role {

        public RoleStub(String s) {
            super(s);
        }

    }

}
