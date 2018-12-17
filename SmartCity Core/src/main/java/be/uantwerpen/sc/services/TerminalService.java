package be.uantwerpen.sc.services;

import be.uantwerpen.sc.models.Bot;
import be.uantwerpen.sc.models.Job;
import be.uantwerpen.sc.models.JobList;
import be.uantwerpen.sc.models.links.Link;
import be.uantwerpen.sc.tools.Terminal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TerminalService {
    @Autowired
    JobListService jobListService;

    private Terminal terminal;

    /**
     *
     */
    public TerminalService() {
        terminal = new Terminal() {
            @Override
            public void executeCommand(String commandString) {
                parseCommand(commandString);
            }
        };
    }

    public void systemReady() {
        terminal.printTerminal("\nSmartCity Core [Version " + getClass().getPackage().getImplementationVersion() + "]\n(c) 2015-2018 University of Antwerp. All rights reserved.");
        terminal.printTerminal("Type 'help' to display the possible commands.");

        terminal.activateTerminal();
    }

    /**
     * @param commandString The string entered in the console
     */
    private void parseCommand(String commandString) {
        String command = commandString.split(" ", 2)[0].toLowerCase();

        switch (command) {
            case "show":
                if (commandString.split(" ", 2).length <= 1) {
                    terminal.printTerminalInfo("Missing arguments! 'show {deliveries,jobs}'");
                } else {
                    if (commandString.split(" ", 2)[1].equals("deliveries")) {
                        this.printAllDeliveries();
                    } else {
                        terminal.printTerminalInfo("Unknown arguments! 'show {bots}'");
                    }
                }
                break;
//            case "reset":
//                this.resetBots();
//                break;
//            case "delete":
//                if (commandString.split(" ", 2).length <= 1) {
//                    terminal.printTerminalInfo("Missing arguments! 'delete {botId}'");
//                } else {
//                    int parsedInt;
//
//                    try {
//                        parsedInt = this.parseInteger(commandString.split(" ", 2)[1]);
//
//                        this.deleteBot(parsedInt);
//                    } catch (Exception e) {
//                        terminal.printTerminalError(e.getMessage());
//                    }
//                }
//                break;
            case "exit":
                exitSystem();
                break;
            case "help":
            case "?":
                printHelp("");
                break;
            default:
                terminal.printTerminalInfo("Command: '" + command + "' is not recognized.");
                break;
        }
    }

    private void exitSystem() {
        System.exit(0);
    }

    private void printHelp(String command) {
        switch (command) {
            default:
                terminal.printTerminal("Available commands:");
                terminal.printTerminal("-------------------");
                //terminal.printTerminal("'job {botId} {command}' : send a job to the bot with the given id.");
                terminal.printTerminal("'show {deliveries,jobs}' : show all bots in the database.");
                //terminal.printTerminal("'reset' : remove all bots from the database and release all point locks.");
                //terminal.printTerminal("'delete {botId}' : remove the bot with the given id from the database.");
                terminal.printTerminal("'exit' : shutdown the server.");
                terminal.printTerminal("'help' / '?' : show all available commands.\n");
                break;
        }
    }

    private void printAllDeliveries() {
        List<JobList> jobLists = jobListService.findAll();
        for(JobList list : jobLists) {

            terminal.printTerminal("** Delivery "+list.getIdDelivery());
            for(Job job : list.getJobs()) {
                terminal.printTerminal("\t Job"+job.toString());
            }
        }
    }

    private int parseInteger(String value) throws Exception {
        int parsedInt;

        try {
            parsedInt = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new Exception("'" + value + "' is not an integer value!");
        }

        return parsedInt;
    }

    private long parseLong(String value) throws Exception {
        Long parsedLong;

        try {
            parsedLong = Long.parseLong(value);
        } catch (NumberFormatException e) {
            throw new Exception("'" + value + "' is not a numeric value!");
        }

        return parsedLong;
    }
}
