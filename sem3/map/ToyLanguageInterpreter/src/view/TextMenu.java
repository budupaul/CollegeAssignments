package view;

import utils.exceptions.ExitTextMenuException;
import utils.exceptions.InterpreterException;
import view.commands.Command;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by mirko on 14/11/2016.
 */
public class TextMenu {
    private Map<String, Command> commands;
    private Scanner scanner;

    public TextMenu(Scanner scanner) {
        this.commands = new HashMap<>();
        this.scanner = scanner;
    }

    public void addCommand(Command command) {
        commands.put(command.getKey(), command);
    }

    private void printMenu() {
        for (Command command : commands.values()) {
            String line = String.format("%4s : %s", command.getKey(), command.getDescription());
            System.out.println(line);
        }
    }

    public void show() {
        while (true) {
            printMenu();

            System.out.printf("Type an option: ");
            String key = scanner.nextLine();
            Command command = commands.get(key);

            if (command == null) {
                System.out.println("Option doesn't exist.");
                continue;
            }

            try {
                command.execute(scanner);
            } catch (ExitTextMenuException err) {
                break;
            } catch (InterpreterException err) {
                System.out.println(err);
            }
        }
    }
}
