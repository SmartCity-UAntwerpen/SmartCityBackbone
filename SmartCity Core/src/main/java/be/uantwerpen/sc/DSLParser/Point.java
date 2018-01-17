package be.uantwerpen.sc.DSLParser;

public class Point {
    private int pid;
    private String type;
    private String access;
    private int xcoord;
    private int ycoord;
    private int hub;

    public Point(int pid, String type, String access, int xcoord, int ycoord, int hub) {

        this.pid = pid;
        this.type = type;
        this.access = access;
        this.xcoord = xcoord;
        this.ycoord = ycoord;
        this.hub = hub;
    }

    /**
     * Formats data for a proper MySQL entry
     * In the format: (int, 'String', ... )
     *
     * @return A valid entry for inserting MySQL VALUES
     */
    public String toSqlAddEntry() {
        String entry = "(" + pid + ",'" + type + "','" + access + "'," + xcoord + "," + ycoord + "," + hub + ")";
        return entry;
    }
}
