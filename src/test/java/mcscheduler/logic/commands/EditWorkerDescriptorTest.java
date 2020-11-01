package mcscheduler.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import mcscheduler.logic.commands.WorkerEditCommand.EditWorkerDescriptor;
import mcscheduler.testutil.EditWorkerDescriptorBuilder;

public class EditWorkerDescriptorTest {

    @Test
    public void equals() {
        // same values -> returns true
        EditWorkerDescriptor descriptorWithSameValues = new EditWorkerDescriptor(CommandTestUtil.DESC_AMY);
        assertEquals(descriptorWithSameValues, CommandTestUtil.DESC_AMY);

        // same object -> returns true
        assertEquals(CommandTestUtil.DESC_AMY, CommandTestUtil.DESC_AMY);

        // null -> returns false
        assertNotEquals(CommandTestUtil.DESC_AMY, null);

        // different types -> returns false
        assertNotEquals(CommandTestUtil.DESC_AMY, 5);

        // different values -> returns false
        assertNotEquals(CommandTestUtil.DESC_BOB, CommandTestUtil.DESC_AMY);

        // different name -> returns false
        EditWorkerDescriptor editedAmy = new EditWorkerDescriptorBuilder(CommandTestUtil.DESC_AMY).withName(
            CommandTestUtil.VALID_NAME_BOB).build();
        assertNotEquals(editedAmy, CommandTestUtil.DESC_AMY);

        // different phone -> returns false
        editedAmy = new EditWorkerDescriptorBuilder(CommandTestUtil.DESC_AMY).withPhone(CommandTestUtil.VALID_PHONE_BOB)
            .build();
        assertNotEquals(editedAmy, CommandTestUtil.DESC_AMY);

        // different pay -> returns false
        editedAmy =
            new EditWorkerDescriptorBuilder(CommandTestUtil.DESC_AMY).withPay(CommandTestUtil.VALID_PAY_BOB).build();
        assertNotEquals(editedAmy, CommandTestUtil.DESC_AMY);

        // different address -> returns false
        editedAmy = new EditWorkerDescriptorBuilder(CommandTestUtil.DESC_AMY).withAddress(
            CommandTestUtil.VALID_ADDRESS_BOB).build();
        assertNotEquals(editedAmy, CommandTestUtil.DESC_AMY);

        // different roles -> returns false
        editedAmy = new EditWorkerDescriptorBuilder(CommandTestUtil.DESC_AMY).withRoles(CommandTestUtil.VALID_ROLE_CHEF)
            .build();
        assertNotEquals(editedAmy, CommandTestUtil.DESC_AMY);
    }
}
