package be.uantwerpen.sc.DSLParser;

public class Point_Drone {
    private float x;
    private float y;
    private float z;
    private int pid;

    public Point_Drone(float x, float y, float z, int pid) {

        this.x = x;
        this.y = y;
        this.z = z;
        this.pid = pid;
    }

    /**
     * Formats data for a proper MySQL entry
     * In the format: (int, 'String', ... )
     *
     * @return A valid entry for inserting MySQL VALUES
     */
    public String toSqlAddEntry() {
        String entry = "(" + x + "," + y + "," + z + "," + pid + ")";
        return entry;
    }
}
