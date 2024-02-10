package com.fges.todoapp;


import org.apache.commons.cli.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Hello world!
 */
public class App {

    /**
     * Do not change this method
     */
    public static void main(String[] args) throws Exception {
        System.exit(exec(args));
    }

    public static int exec(String[] args) throws IOException {
        CommandLine cmd;
        try {
            cmd = CommandeParser.parse(args);
        } catch (java.text.ParseException e) {
            throw new IllegalArgumentException("Erreur lors de l'analyse des arguments en ligne de commande.", e);
        }

        String fileName = cmd.getOptionValue("s");

        List<String> positionalArgs = cmd.getArgList();
        if (positionalArgs.isEmpty()) {
            System.err.println("Missing Command");
            return 1;
        }

        String command = positionalArgs.get(0);


        Path filePath = Paths.get(fileName);
        String fileContent = FileClass.readFileContent(fileName);
        JsonTodoCheck jsonTodoChecker = new JsonTodoCheck();
        CsvTodoCheck csvTodoChecker = new CsvTodoCheck();

        if (command.equals("insert")) {
            if (positionalArgs.size() < 2) {
                System.err.println("Missing TODO name");
                return 1;
            }
            Todo todoObject = new Todo(positionalArgs.get(1));
            if (cmd.hasOption("done")) {
                todoObject.setText("Done: " + todoObject.toString());
            }


            if (fileName.endsWith("csv")) {
                csvTodoChecker.insertTodo(fileName, fileContent, todoObject);
            } else if (fileName.endsWith("json")) {
                jsonTodoChecker.insertTodo(fileName, fileContent, todoObject);
            }
        }

        if (command.equals("list")) {
            boolean doneOnly = cmd.hasOption("done");
            if (fileName.endsWith("csv")) {
                csvTodoChecker.listTodos(fileName, fileContent, doneOnly);
            } else if (fileName.endsWith("json")) {
                jsonTodoChecker.listTodos(fileName, fileContent, doneOnly);
            }


        }
        System.err.println("Done.");
        return 0;

    }
}
