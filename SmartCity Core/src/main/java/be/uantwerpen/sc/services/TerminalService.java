package be.uantwerpen.sc.services;

import be.uantwerpen.sc.models.BackendInfo;
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

    @Autowired
    BackendInfoService backendInfoService;

    @Autowired
    BackendService backendService;

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
                    terminal.printTerminalInfo("Missing arguments! 'show {deliveries,backends}'");
                } else {
                    if (commandString.split(" ", 2)[1].equals("deliveries")) {
                        this.printAllDeliveries();
                    } else if (commandString.split(" ", 2)[1].equals("backends")) {
                        this.printAllBackends();
                    } else {
                        terminal.printTerminalInfo("Unknown arguments! 'show {deliveries,backends}'");
                    }
                }
                break;
            case "reset":
                this.resetAll();
                break;
            case "delete":
                if (commandString.split(" ", 2).length <= 1) {
                    terminal.printTerminalInfo("Missing arguments! 'delete {id_delivery}'");
                } else {
                    try {
                        this.deleteDelivery(commandString.split(" ", 2)[1]);
                    } catch (Exception e) {
                        terminal.printTerminalError(e.getMessage());
                    }
                }
                break;
            case "weight":
                if (commandString.split(" ", 4).length != 4) {
                    terminal.printTerminalInfo("wrong arguments! 'weight {mapid startpid stoppid}'");
                } else {
                    String[] splitCommands = commandString.split(" ", 4);
                    int[] commandsArray = new int[splitCommands.length-1];
                    for(int i = 1; i < splitCommands.length; i++ ){
                        commandsArray[i-1] = Integer.parseInt(splitCommands[i]);
                    }
                    BackendInfo backendInfo = backendInfoService.getInfoByMapId(commandsArray[0]);
                    if(backendInfo == null){
                        terminal.printTerminalInfo("Map doesn't exist, available backends");
                        this.printAllBackends();
                        break;
                    }else{
                        float weight = backendService.getWeight(backendInfo, commandsArray[1], commandsArray[2]);
                        terminal.printTerminal("map " + backendInfo.getMapId() + " " + backendInfo.getName() + backendInfo.getHostname()+":"+backendInfo.getPort() + ":");
                        terminal.printTerminal("weight between:" + commandsArray[1]+ " and " + commandsArray[2] + " is "+ weight);
                    }
                }
                break;
            case "exit":
                exitSystem();
                break;
            case "help":
                printHelp("");
                break;
            case "?":
                printHelp("");
                break;
            default:
                terminal.printTerminalInfo("Command: '" + command + "' is not recognized.");
                break;
        }
    }

    private void deleteDelivery(String id) {
        Long count = jobListService.deleteByIdDelivery(id);
        if(count > 0) {
            terminal.printTerminal("Deleted "+count+" items.");
        }
        else {
            terminal.printTerminal("ID not found. Nothing was deleted.");
        }
    }

    private void resetAll() {
        jobListService.deleteAll();
        terminal.printTerminal("Reset done");
    }

    private void printAllBackends() {
        List<BackendInfo> infoList = backendInfoService.findAll();

        if(infoList.isEmpty()) {
            terminal.printTerminal("No backends configured");
            return;
        }

        terminal.printTerminal("**Backends");
        for(BackendInfo info : infoList) {
            terminal.printTerminal("\t"+info.toString());
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
//                terminal.printTerminal("'add backend {name}' : add a new backend interactively");
                terminal.printTerminal("'show {deliveries,backends}' : show all data in the database.");
                terminal.printTerminal("'reset' : remove all deliveries from the database.");
                terminal.printTerminal("'delete {id_delivery}' : remove the delivery with the given id from the database.");
                terminal.printTerminal("'weight {mapid startpid stoppid}' : request  the weight between two points on a given map");
                terminal.printTerminal("'exit' : shutdown the server.");
                terminal.printTerminal("'help' / '?' : show all available commands.\n");
                break;
        }
    }

    private void printAllDeliveries() {
        List<JobList> jobLists = jobListService.findAll();

        if(jobLists.isEmpty()) {
            terminal.printTerminal("No deliveries present");
            return;
        }

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
