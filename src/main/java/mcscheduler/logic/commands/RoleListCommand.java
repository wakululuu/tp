package mcscheduler.logic.commands;

import static java.util.Objects.requireNonNull;
import static mcscheduler.model.Model.PREDICATE_SHOW_ALL_ROLES_WITHOUT_LEAVE;

import javafx.collections.ObservableList;
import mcscheduler.model.Model;
import mcscheduler.model.role.Role;

/**
 * Lists all roles in the address book to the user.
 */
public class RoleListCommand extends Command {

    public static final String COMMAND_WORD = "role-list";

    public static final String MESSAGE_SUCCESS = "All valid roles:\n%1$s";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        model.updateFilteredRoleList(PREDICATE_SHOW_ALL_ROLES_WITHOUT_LEAVE);
        ObservableList<Role> roleList = model.getFilteredRoleList();

        String printableRoleList = "";
        for (int i = 1; i <= roleList.size(); i++) {
            printableRoleList += i + ". " + roleList.get(i - 1) + "\n";
        }

        return new CommandResult(String.format(MESSAGE_SUCCESS, printableRoleList));
    }
}
