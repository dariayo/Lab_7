package client.commands.available.commands;

import client.ClientManager;
import client.commands.Command;
import server.PersonCollection;


/**
 * add_if_min {element} :
 */
public class AddIfMin extends Command {
    private final PersonCollection personCollection;

    public AddIfMin(PersonCollection personCollection) {
        this.personCollection = personCollection;
    }

    /**
     * dd a new element to the collection if its value is less than the smallest element of that collection
     *
     * @param args
     */
    @Override
    public void execute(String[] args) {
        try {
            //if (ExecuteScript.getFlag()) {
            //if (PersonCollection.addIfMaxForScript(args[1])) {
            //ExecuteScript.getPersonList().set(6, args[1]);
            //PersonCollection.addPerson(ClientManager.createPersonFromScript(ExecuteScript.getPersonList()));
            //}
            //}
            if (args.length > 2) {
                System.out.println("Вы неправильно ввели команду");
            } else {
                PersonCollection personCollection = new PersonCollection();
                personCollection.loadCollection();
                if (!personCollection.toHeight(Integer.parseInt(args[1]))) {
                    Scanner sc = new Scanner(System.in);
                    Person newPerson = ClientManager.getNewPerson(sc);
                    Request<Person> request = new Request<>(getName(), newPerson);
                    CommandResult result = requestManager.sendRequest(request);

                    if (result.status) {
                        System.out.println((result.message));
                        System.out.println("Ваш персонаж теперь в коллекции");
                    } else
                        System.out.println("Ошибка");
                } else {
                    System.out.println("Ваш персонаж не самый низкий!!");
                }

            }
        } catch (ArrayIndexOutOfBoundsException | JAXBException e) {
            System.out.println("Недостаточно аргументов, обратитесь к команде help");
            e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return "add_if_min";
    }

    @Override
    public String getDescription() {
        return "добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции";
    }
}
