package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.AddressBook;
import seedu.address.testutil.AddressBookBuilder;

public class JsonSerializableAddressBookTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonSerializableAddressBookTest");
    private static final Path TYPICAL_WORKERS_FILE = TEST_DATA_FOLDER.resolve("typicalWorkersAddressBook.json");
    private static final Path INVALID_WORKER_FILE = TEST_DATA_FOLDER.resolve("invalidWorkerAddressBook.json");
    private static final Path DUPLICATE_WORKER_FILE = TEST_DATA_FOLDER.resolve("duplicateWorkerAddressBook.json");

    @Test
    public void toModelType_typicalWorkersFile_success() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(TYPICAL_WORKERS_FILE,
                JsonSerializableAddressBook.class).get();
        AddressBook addressBookFromFile = dataFromFile.toModelType();
        AddressBook typicalWorkersAddressBook = AddressBookBuilder.getTypicalAddressBook();
        assertEquals(addressBookFromFile, typicalWorkersAddressBook);
    }

    @Test
    public void toModelType_invalidWorkerFile_throwsIllegalValueException() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(INVALID_WORKER_FILE,
                JsonSerializableAddressBook.class).get();
        assertThrows(IllegalValueException.class, dataFromFile::toModelType);
    }

    @Test
    public void toModelType_duplicateWorkers_throwsIllegalValueException() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(DUPLICATE_WORKER_FILE,
                JsonSerializableAddressBook.class).get();
        assertThrows(IllegalValueException.class, JsonSerializableAddressBook.MESSAGE_DUPLICATE_WORKER,
                dataFromFile::toModelType);
    }

}
