package mcscheduler.storage;

import static mcscheduler.storage.JsonAdaptedWorker.MISSING_FIELD_MESSAGE_FORMAT;
import static mcscheduler.testutil.Assert.assertThrows;
import static mcscheduler.testutil.TypicalWorkers.BENSON;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import mcscheduler.commons.exceptions.IllegalValueException;
import mcscheduler.model.worker.Address;
import mcscheduler.model.worker.Name;
import mcscheduler.model.worker.Pay;
import mcscheduler.model.worker.Phone;

public class JsonAdaptedWorkerTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_ADDRESS = " ";
    private static final String INVALID_PAY = "10.111";
    private static final String INVALID_ROLE = "#chef";

    private static final String VALID_NAME = BENSON.getName().toString();
    private static final String VALID_PHONE = BENSON.getPhone().toString();
    private static final String VALID_PAY = String.format("%.2f", BENSON.getPay().getValue());
    private static final String VALID_ADDRESS = BENSON.getAddress().toString();
    private static final List<JsonAdaptedRole> VALID_ROLES = BENSON.getRoles().stream()
            .map(JsonAdaptedRole::new)
            .collect(Collectors.toList());
    private static final List<JsonAdaptedUnavailability> VALID_UNAVAILABILITIES = BENSON.getUnavailableTimings()
            .stream()
            .map(JsonAdaptedUnavailability::new)
            .collect(Collectors.toList());

    @Test
    public void toModelType_validWorkerDetails_returnsWorker() throws Exception {
        JsonAdaptedWorker worker = new JsonAdaptedWorker(BENSON);
        assertEquals(BENSON, worker.toModelType());
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedWorker worker = new JsonAdaptedWorker(INVALID_NAME, VALID_PHONE, VALID_PAY, VALID_ADDRESS,
                VALID_ROLES, VALID_UNAVAILABILITIES);
        String expectedMessage = Name.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, worker::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedWorker worker = new JsonAdaptedWorker(null, VALID_PHONE, VALID_PAY, VALID_ADDRESS, VALID_ROLES,
                VALID_UNAVAILABILITIES);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, worker::toModelType);
    }

    @Test
    public void toModelType_invalidPhone_throwsIllegalValueException() {
        JsonAdaptedWorker worker = new JsonAdaptedWorker(VALID_NAME, INVALID_PHONE, VALID_PAY, VALID_ADDRESS,
                VALID_ROLES, VALID_UNAVAILABILITIES);
        String expectedMessage = Phone.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, worker::toModelType);
    }

    @Test
    public void toModelType_nullPhone_throwsIllegalValueException() {
        JsonAdaptedWorker worker = new JsonAdaptedWorker(VALID_NAME, null, VALID_PAY, VALID_ADDRESS, VALID_ROLES,
                VALID_UNAVAILABILITIES);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, worker::toModelType);
    }

    @Test
    public void toModelType_invalidPay_throwsIllegalValueException() {
        JsonAdaptedWorker worker = new JsonAdaptedWorker(VALID_NAME, VALID_PHONE, INVALID_PAY, VALID_ADDRESS,
                VALID_ROLES, VALID_UNAVAILABILITIES);
        String expectedMessage = Pay.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, worker::toModelType);
    }

    @Test
    public void toModelType_nullPay_throwsIllegalValueException() {
        JsonAdaptedWorker worker = new JsonAdaptedWorker(VALID_NAME, VALID_PHONE, null, VALID_ADDRESS, VALID_ROLES,
                VALID_UNAVAILABILITIES);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Pay.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, worker::toModelType);
    }

    @Test
    public void toModelType_invalidAddress_throwsIllegalValueException() {
        JsonAdaptedWorker worker = new JsonAdaptedWorker(VALID_NAME, VALID_PHONE, VALID_PAY, INVALID_ADDRESS,
                VALID_ROLES, VALID_UNAVAILABILITIES);
        String expectedMessage = Address.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, worker::toModelType);
    }

    @Test
    public void toModelType_nullAddress_throwsIllegalValueException() {
        JsonAdaptedWorker worker = new JsonAdaptedWorker(VALID_NAME, VALID_PHONE, VALID_PAY, null, VALID_ROLES,
                VALID_UNAVAILABILITIES);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, worker::toModelType);
    }

    @Test
    public void toModelType_invalidRoles_throwsIllegalValueException() {
        List<JsonAdaptedRole> invalidRoles = new ArrayList<>(VALID_ROLES);
        invalidRoles.add(new JsonAdaptedRole(INVALID_ROLE));
        JsonAdaptedWorker worker = new JsonAdaptedWorker(VALID_NAME, VALID_PHONE, VALID_PAY, VALID_ADDRESS,
                invalidRoles, VALID_UNAVAILABILITIES);
        assertThrows(IllegalValueException.class, worker::toModelType);
    }

}
