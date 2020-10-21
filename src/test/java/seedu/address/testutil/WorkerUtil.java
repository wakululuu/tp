package seedu.address.testutil;

import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
//import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PAY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ROLE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_UNAVAILABILITY;
//import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Set;

import seedu.address.logic.commands.WorkerAddCommand;
import seedu.address.logic.commands.WorkerEditCommand.EditWorkerDescriptor;
import seedu.address.model.tag.Role;
//import seedu.address.model.tag.Tag;
import seedu.address.model.worker.Unavailability;
import seedu.address.model.worker.Worker;

/**
 * A utility class for Worker.
 */
public class WorkerUtil {

    /**
     * Returns an add command string for adding the {@code worker}.
     */
    public static String getAddCommand(Worker worker) {
        return WorkerAddCommand.COMMAND_WORD + " " + getWorkerDetails(worker);
    }

    /**
     * Returns the part of command string for the given {@code worker}'s details.
     */
    public static String getWorkerDetails(Worker worker) {
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_NAME + worker.getName().fullName + " ");
        sb.append(PREFIX_PHONE + worker.getPhone().value + " ");
        sb.append(PREFIX_PAY + String.valueOf(worker.getPay().value) + " ");
        sb.append(PREFIX_ADDRESS + worker.getAddress().value + " ");
        worker.getRoles().stream().forEach(
            s -> sb.append(PREFIX_ROLE + s.tagName + " ")
        );
        worker.getUnavailableTimings().stream().forEach(
            s -> sb.append(PREFIX_ROLE + s.getString() + " ")
        );
        return sb.toString();
    }

    /**
     * Returns the part of command string for the given {@code EditWorkerDescriptor}'s details.
     */
    public static String getEditWorkerDescriptorDetails(EditWorkerDescriptor descriptor) {
        StringBuilder sb = new StringBuilder();
        descriptor.getName().ifPresent(name -> sb.append(PREFIX_NAME).append(name.fullName).append(" "));
        descriptor.getPhone().ifPresent(phone -> sb.append(PREFIX_PHONE).append(phone.value).append(" "));
        descriptor.getPay().ifPresent(pay -> sb.append(PREFIX_PAY).append(pay.value).append(" "));
        descriptor.getAddress().ifPresent(address -> sb.append(PREFIX_ADDRESS).append(address.value).append(" "));
        if (descriptor.getRoles().isPresent()) {
            Set<Role> roles = descriptor.getRoles().get();
            if (roles.isEmpty()) {
                sb.append(PREFIX_ROLE);
            } else {
                roles.forEach(s -> sb.append(PREFIX_ROLE).append(s.tagName).append(" "));
            }
        }
        if (descriptor.getUnavailableTimings().isPresent()) {
            Set<Unavailability> unavailabilities = descriptor.getUnavailableTimings().get();
            if (unavailabilities.isEmpty()) {
                sb.append(PREFIX_UNAVAILABILITY);
            } else {
                unavailabilities.forEach(s -> sb.append(PREFIX_UNAVAILABILITY).append(s.getString()).append(" "));
            }
        }
        return sb.toString();
    }
}
