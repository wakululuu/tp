package mcscheduler.testutil;

import static mcscheduler.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static mcscheduler.logic.parser.CliSyntax.PREFIX_NAME;
import static mcscheduler.logic.parser.CliSyntax.PREFIX_PAY;
import static mcscheduler.logic.parser.CliSyntax.PREFIX_PHONE;
import static mcscheduler.logic.parser.CliSyntax.PREFIX_ROLE;
import static mcscheduler.logic.parser.CliSyntax.PREFIX_UNAVAILABILITY;

import java.util.Set;

import mcscheduler.logic.commands.WorkerAddCommand;
import mcscheduler.logic.commands.WorkerEditCommand.EditWorkerDescriptor;
import mcscheduler.model.role.Role;
import mcscheduler.model.worker.Unavailability;
import mcscheduler.model.worker.Worker;

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
        sb.append(PREFIX_PAY + String.valueOf(worker.getPay().getValue()) + " ");
        sb.append(PREFIX_ADDRESS + worker.getAddress().value + " ");
        worker.getRoles().stream().forEach(
            s -> sb.append(PREFIX_ROLE + s.roleName + " ")
        );
        worker.getUnavailableTimings().stream().forEach(
            s -> sb.append(PREFIX_UNAVAILABILITY + s.getString() + " ")
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
        descriptor.getPay().ifPresent(pay -> sb.append(PREFIX_PAY).append(pay.getValue()).append(" "));
        descriptor.getAddress().ifPresent(address -> sb.append(PREFIX_ADDRESS).append(address.value).append(" "));
        if (descriptor.getRoles().isPresent()) {
            Set<Role> roles = descriptor.getRoles().get();
            if (roles.isEmpty()) {
                sb.append(PREFIX_ROLE);
            } else {
                roles.forEach(s -> sb.append(PREFIX_ROLE).append(s.roleName).append(" "));
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
