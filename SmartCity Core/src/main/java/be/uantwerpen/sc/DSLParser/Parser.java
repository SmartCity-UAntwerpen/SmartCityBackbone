package be.uantwerpen.sc.DSLParser;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * This class is meant to parse a map described in Point_Car.txt and generate valid mysql syntax from it.
 */
public class Parser {
    ArrayList<Point_Car> pointcar = new ArrayList<>();
    ArrayList<Point_Robot> pointrobot = new ArrayList<>();
    ArrayList<Link> link = new ArrayList<>();
    ArrayList<Link_Robot> linkrobot = new ArrayList<>();
    ArrayList<Point> point = new ArrayList<>();
    ArrayList<Point_Drone> pointdrone = new ArrayList<>();
    String ip = "smartcity.ddns.net";

    public static void main(String[] args) {
        Parser p = new Parser();
    }

    public Parser() {
        String dir = System.getProperty("user.dir");
        String type = "Link";
        parseFile(readFile(dir + "/Smartcity Core/src/main/java/be/uantwerpen/sc/DSLParser/Link.txt"), type);
        type = "Link_Robot";
        parseFile(readFile(dir + "/Smartcity Core/src/main/java/be/uantwerpen/sc/DSLParser/Link_Robot.txt"), type);
        type = "Point";
        parseFile(readFile(dir + "/Smartcity Core/src/main/java/be/uantwerpen/sc/DSLParser/Point.txt"), type);
        type = "Point_Car";
        parseFile(readFile(dir + "/Smartcity Core/src/main/java/be/uantwerpen/sc/DSLParser/Point_Car.txt"), type);
        type = "Point_Drone";
        parseFile(readFile(dir + "/Smartcity Core/src/main/java/be/uantwerpen/sc/DSLParser/Point_Drone.txt"), type);
        type = "Point_Robot";
        parseFile(readFile(dir + "/Smartcity Core/src/main/java/be/uantwerpen/sc/DSLParser/Point_Robot.txt"), type);


        System.out.println("-------------------Done Reading files---------------");
        System.out.println("-------------------Writing to database--------------");
        writeToDB();
        System.out.println("----------------------Done writing------------------");
    }

    /**
     * Reads the file specified by path and return a BufferedReader
     *
     * @param path The path from which the file should be read
     * @return br a bufferedReader, which then can be read
     */
    private BufferedReader readFile(String path) {
        File file = new File(path);
        System.out.println();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return br;
    }

    /**
     * Reads a file and parses each
     *
     * @param br   A buffered reader for a map
     * @param type The type of points to be read
     */
    private void parseFile(BufferedReader br, String type) {
        String line;

        /**
         * Switch for all different types of points to be parsed
         */
        switch (type) {
            case "Point_Car":
                try {
                    while ((line = br.readLine()) != null) {
                        //Strings start with '#' so ignore these lines
                        if (line.trim().startsWith("#")) {
                            //these lines are comments, so ignore
                        } else {
                            //split string based on tabs
                            String parts[] = line.split("\t");
                            pointcar.add(new Point_Car(Integer.parseInt(parts[0]), Float.parseFloat(parts[1]), Float.parseFloat(parts[2]), Float.parseFloat(parts[3]), Float.parseFloat(parts[4]), parts[5]));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case "Point_Robot":
                try {
                    while ((line = br.readLine()) != null) {
                        //Strings start with '#' so ignore these lines
                        if (line.trim().startsWith("#")) {
                            //these lines are comments, so ignore
                        } else {
                            //split string based on tabs
                            String parts[] = line.split("\t");

                            pointrobot.add(new Point_Robot(Integer.parseInt(parts[0]), parts[1], Integer.parseInt(parts[2])));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case "Link":
                try {
                    while ((line = br.readLine()) != null) {
                        //Strings start with '#' so ignore these lines
                        if (line.trim().startsWith("#")) {
                            //these lines are comments, so ignore
                        } else {
                            //split string based on tabs
                            String parts[] = line.split("\t");
                            if (parts[5].equals("NULL")) {
                                Integer test = null;
                                link.add(new Link(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), Integer.parseInt(parts[3]), parts[4], test));
                            } else {
                                link.add(new Link(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), Integer.parseInt(parts[3]), parts[4], Integer.parseInt(parts[5])));
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case "Link_Robot":
                try {
                    while ((line = br.readLine()) != null) {
                        //Strings start with '#' so ignore these lines
                        if (line.trim().startsWith("#")) {
                            //these lines are comments, so ignore
                        } else {
                            //split string based on tabs
                            String parts[] = line.split("\t");
                            linkrobot.add(new Link_Robot(Integer.parseInt(parts[0]), parts[1], parts[2]));

                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case "Point":
                try {
                    while ((line = br.readLine()) != null) {
                        //Strings start with '#' so ignore these lines
                        if (line.trim().startsWith("#")) {
                            //these lines are comments, so ignore
                        } else {
                            //split string based on tabs
                            String parts[] = line.split("\t");
                            point.add(new Point(Integer.parseInt(parts[0]), parts[1], parts[2], Integer.parseInt(parts[3]), Integer.parseInt(parts[4]), Integer.parseInt(parts[5])));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case "Point_Drone":
                try {
                    while ((line = br.readLine()) != null) {
                        //Strings start with '#' so ignore these lines
                        if (line.trim().startsWith("#")) {
                            //these lines are comments, so ignore
                        } else {
                            //split string based on tabs
                            String parts[] = line.split("\t");
                            pointdrone.add(new Point_Drone(Float.parseFloat(parts[0]), Float.parseFloat(parts[1]), Float.parseFloat(parts[2]), Integer.parseInt(parts[3])));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            default:
                System.out.println("Unknown type");
                break;
        }
    }

    /**
     * Writes all previously read arrayLists to the MySQL core database hosted at smartcity.ddns.net:3306
     */
    private void writeToDB() {
        String entry = "";
        try {
            //load jdbc driver
            Class.forName("com.mysql.jdbc.Driver");
            //establish connection with mysql server
            Connection con = DriverManager.getConnection("jdbc:mysql://" + "smartcity.ddns.net" + ":3306/core", "root", "smartcity");
            Statement stmt = con.createStatement();
            //stop checking foreign keys
            stmt.executeUpdate("SET foreign_key_checks = 0;");

            /*
                Update table link
             */
            //first discard old
            stmt.executeUpdate("truncate core.link;");
            //compose string to add entries
            entry = "INSERT INTO `link` VALUES ";
            for (int i = 0; i < link.size() - 1; i++) {
                entry = entry + link.get(i).toSqlAddEntry() + ",";
            }
            //last entry ends with ';'
            entry = entry + link.get(link.size() - 1).toSqlAddEntry() + ";";
            System.out.println(entry);
            //execute entries
            stmt.executeUpdate(entry);

            /*
                Update table link_robot
             */
            //first discard old
            stmt.executeUpdate("truncate core.link_robot;");
            //compose string to add entries
            entry = "INSERT INTO `link_robot` VALUES ";
            for (int i = 0; i < linkrobot.size() - 1; i++) {
                entry = entry + linkrobot.get(i).toSqlAddEntry() + ",";
            }
            //last entry ends with ';'
            entry = entry + linkrobot.get(linkrobot.size() - 1).toSqlAddEntry() + ";";
            System.out.println(entry);
            //execute entries
            stmt.executeUpdate(entry);

            /*
                Update table point
             */
            //first discard old
            stmt.executeUpdate("truncate core.point;");
            //compose string to add entries
            entry = "INSERT INTO `point` VALUES ";
            for (int i = 0; i < point.size() - 1; i++) {
                entry = entry + point.get(i).toSqlAddEntry() + ",";
            }
            //last entry ends with ';'
            entry = entry + point.get(point.size() - 1).toSqlAddEntry() + ";";
            System.out.println(entry);
            //execute entries
            stmt.executeUpdate(entry);

             /*
                Update table point_car
             */
            //first discard old
            stmt.executeUpdate("truncate core.point_car;");
            //compose string to add entries
            entry = "INSERT INTO `point_car` VALUES ";
            for (int i = 0; i < pointcar.size() - 1; i++) {
                entry = entry + pointcar.get(i).toSqlAddEntry() + ",";
            }
            //last entry ends with ';'
            entry = entry + pointcar.get(pointcar.size() - 1).toSqlAddEntry() + ";";
            System.out.println(entry);
            //execute entries
            stmt.executeUpdate(entry);

            /*
                Update table point_drone
             */
            //first discard old
            stmt.executeUpdate("truncate core.point_drone;");
            //compose string to add entries
            entry = "INSERT INTO `point_drone` VALUES ";
            for (int i = 0; i < pointdrone.size() - 1; i++) {
                entry = entry + pointdrone.get(i).toSqlAddEntry() + ",";
            }
            //last entry ends with ';'
            entry = entry + pointdrone.get(pointdrone.size() - 1).toSqlAddEntry() + ";";
            System.out.println(entry);
            //execute entries
            stmt.executeUpdate(entry);

            /*
                Update table point_robot
             */
            //first discard old
            stmt.executeUpdate("truncate core.point_robot;");
            //compose string to add entries
            entry = "INSERT INTO `point_robot` VALUES ";
            for (int i = 0; i < pointrobot.size() - 1; i++) {
                entry = entry + pointrobot.get(i).toSqlAddEntry() + ",";
            }
            //last entry ends with ';'
            entry = entry + pointrobot.get(pointrobot.size() - 1).toSqlAddEntry() + ";";
            System.out.println(entry);
            //execute entries
            stmt.executeUpdate(entry);


            //Check foreign keys again when done
            stmt.executeUpdate("SET foreign_key_checks = 1;");

            //close the connection when done
            con.close();
        } catch (SQLException e) {
            System.out.println(e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}