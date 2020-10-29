package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static seedu.address.logic.commands.CommandTestUtil.DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
//import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PAY_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ROLE_CHEF;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.WorkerEditCommand.EditWorkerDescriptor;
import seedu.address.testutil.EditWorkerDescriptorBuilder;

public class EditWorkerDescriptorTest {

    @Test
    public void equals() {
        // same values -> returns true
        EditWorkerDescriptor descriptorWithSameValues = new EditWorkerDescriptor(DESC_AMY);
        assertEquals(descriptorWithSameValues, DESC_AMY);

        // same object -> returns true
        assertEquals(DESC_AMY, DESC_AMY);

        // null -> returns false
        assertNotEquals(DESC_AMY, null);

        // different types -> returns false
        assertNotEquals(DESC_AMY, 5);

        // different values -> returns false
        assertNotEquals(DESC_BOB, DESC_AMY);

        // different name -> returns false
        EditWorkerDescriptor editedAmy = new EditWorkerDescriptorBuilder(DESC_AMY).withName(VALID_NAME_BOB).build();
        assertNotEquals(editedAmy, DESC_AMY);

        // different phone -> returns false
        editedAmy = new EditWorkerDescriptorBuilder(DESC_AMY).withPhone(VALID_PHONE_BOB).build();
        assertNotEquals(editedAmy, DESC_AMY);

        // different pay -> returns false
        editedAmy = new EditWorkerDescriptorBuilder(DESC_AMY).withPay(VALID_PAY_BOB).build();
        assertNotEquals(editedAmy, DESC_AMY);

        // different address -> returns false
        editedAmy = new EditWorkerDescriptorBuilder(DESC_AMY).withAddress(VALID_ADDRESS_BOB).build();
        assertNotEquals(editedAmy, DESC_AMY);

        // different roles -> returns false
        editedAmy = new EditWorkerDescriptorBuilder(DESC_AMY).withRoles(VALID_ROLE_CHEF).build();
        assertNotEquals(editedAmy, DESC_AMY);
    }
}
