package mcscheduler.testutil;

import static mcscheduler.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static mcscheduler.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static mcscheduler.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static mcscheduler.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static mcscheduler.logic.commands.CommandTestUtil.VALID_PAY_AMY;
import static mcscheduler.logic.commands.CommandTestUtil.VALID_PAY_BOB;
import static mcscheduler.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static mcscheduler.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static mcscheduler.logic.commands.CommandTestUtil.VALID_ROLE_CASHIER;
import static mcscheduler.logic.commands.CommandTestUtil.VALID_ROLE_CHEF;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mcscheduler.model.worker.Worker;

/**
 * A utility class containing a list of {@code Worker} objects to be used in tests.
 */
public class TypicalWorkers {

    public static final Worker ALICE = new WorkerBuilder().withName("Alice Pauline")
            .withAddress("123, Jurong West Ave 6, #08-111").withPay("9.75")
            .withPhone("94351253")
            .withRoles("cashier")
            .withUnavailableTimings("TUE AM")
            .build();
    public static final Worker BENSON = new WorkerBuilder().withName("Benson Meier")
            .withAddress("311, Clementi Ave 2, #02-25")
            .withPay("10.20").withPhone("98765432")
            .withRoles("chef", "cashier")
            .withUnavailableTimings("MON AM").build();
    public static final Worker CARL = new WorkerBuilder().withName("Carl Kurz").withPhone("95352563")
            .withPay("15.01").withAddress("wall street").withRoles("cashier").build();
    public static final Worker DANIEL = new WorkerBuilder().withName("Daniel Meier").withPhone("87652533")
            .withPay("16").withAddress("10th street").withRoles("chef").build();
    public static final Worker ELLE = new WorkerBuilder().withName("Elle Meyer").withPhone("94812224")
            .withPay("5.99").withAddress("michegan ave").withRoles("chef").build();
    public static final Worker FIONA = new WorkerBuilder().withName("Fiona Kunz").withPhone("94824247")
            .withPay("21.86").withAddress("little tokyo").withRoles("chef").build();
    public static final Worker GEORGE = new WorkerBuilder().withName("George Best").withPhone("94812442")
            .withPay("24").withAddress("4th street").withRoles("chef").build();

    // Manually added
    public static final Worker HOON = new WorkerBuilder().withName("Hoon Meier").withPhone("84824324")
            .withPay("11.11").withAddress("little india").withRoles("chef").build();
    public static final Worker IDA = new WorkerBuilder().withName("Ida Mueller").withPhone("84823131")
            .withPay("10.10").withAddress("chicago ave").withRoles("chef").build();

    // Manually added - Worker's details found in {@code CommandTestUtil}
    public static final Worker AMY = new WorkerBuilder().withName(VALID_NAME_AMY).withPhone(VALID_PHONE_AMY)
            .withPay(VALID_PAY_AMY).withAddress(VALID_ADDRESS_AMY).withRoles(VALID_ROLE_CASHIER).build();
    public static final Worker BOB = new WorkerBuilder().withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
            .withPay(VALID_PAY_BOB).withAddress(VALID_ADDRESS_BOB).withRoles(VALID_ROLE_CASHIER, VALID_ROLE_CHEF)
            .build();

    private TypicalWorkers() {
    } // prevents instantiation

    public static List<Worker> getTypicalWorkers() {
        return new ArrayList<>(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE));
    }
}
