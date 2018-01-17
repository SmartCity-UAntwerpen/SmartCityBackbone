package be.uantwerpen.sc.DSLParser;

public class Point_Robot {
    private final int pid, pointLock;
    private final String rfid;


    public Point_Robot(int pid, String rfid, int pointLock) {
        this.pid = pid;
        this.rfid = rfid;
        this.pointLock = pointLock;
    }

    /**
     * Formats data for a proper MySQL entry
     * In the format: (int, 'String', ... )
     *
     * @return A valid entry for inserting MySQL VALUES
     */
    public String toSqlAddEntry() {
        String entry = "(" + pid + ",'" + rfid + "'," + pointLock + ")";
        return entry;
    }
}
