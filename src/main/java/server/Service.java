package server;


import common.DataManager;
import common.network.CommandResult;
import common.network.Request;


import java.nio.file.AccessDeniedException;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * The class is responsible for calling commands
 */
public class Service {
    private HashMap<String, Executable> commands = new HashMap<>();
    private PersonCollection collection = new PersonCollection();
    private DataManager dataManager;
    private DBManager dbManager;

    public Service(DataManager dataManager, DBManager dbManager) {
        this.dataManager = dataManager;
        this.dbManager = dbManager;
        initCommands();
    }

    /**
     * add commands with a link to the method
     */
    private void initCommands() {
        commands.put("add", dataManager::add);
        commands.put("add_if_max", dataManager::addIfMax);
        commands.put("add_if_min", dataManager::addIfMin);
        commands.put("show", dataManager::show);
        commands.put("clear", dataManager::clear);
        commands.put("info", dataManager::info);
        commands.put("help", dataManager::help);
        commands.put("count_greater_than_eye_color", dataManager::countEyeColor);
        commands.put("filter_greater_than_location", dataManager::filterGreater);
        commands.put("print_unique_location", dataManager::printUniqueLocation);
        commands.put("remove_by_id", dataManager::remove_by_id);
        commands.put("remove_greater", dataManager::removeGreater);
        commands.put("update", dataManager::update);
        if (dbManager != null) {
            commands.put("login", dbManager::login);
            commands.put("register", dbManager::register);
            commands.put("check_user", dbManager::checkLogin);
            commands.put("check_register", dbManager::checkRegister);
        }

    }

    /**
     * check if there is a command on the server and execute it
     * If we pass a collection, we save it
     * if the command is null, we load collection from db and check height
     * if command "check_id" we check existence of id
     *
     * @param request request - command from client
     */
    public CommandResult executeCommand(Request<?> request) throws AccessDeniedException, SQLException {
        if (!commands.containsKey(request.command) && request.command != null)
            return new CommandResult(false, "Такой команды на сервере нет.");
        else if (request.command == null && request.personCollection != null) {
            collection.loadCollection(request.personCollection.getCollection());
            return new CommandResult(true, "правда");
        } else if (request.command == null) {
            collection.setCollection(dbManager.readCollection());
            if (collection.toHeight((int) request.type)) {
                return new CommandResult(true, "правда");
            } else {
                return new CommandResult(false, "неправда");
            }
        } else if (request.command.equals("check_id")) {
            if (collection.existID((int) request.type, request.user.getUsername())) {
                return new CommandResult(true, "правда");
            } else {
                return new CommandResult(false, "неправда");
            }
        }
        return commands.get(request.command).execute(request);
    }
}