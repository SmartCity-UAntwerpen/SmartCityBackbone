package be.uantwerpen.sc.DSLParser;

public class Point_Car {
    private final int pid;
    private final float x, y, z, w;
    private final String map;

    public Point_Car(int pid, float x, float y, float z, float w, String map) {
        this.pid = pid;
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
        this.map = map;
    }

    /**
     * Formats data for a proper MySQL entry
     * In the format: (int, 'String', ... )
     *
     * @return A valid entry for inserting MySQL VALUES
     */
    public String toSqlAddEntry() {
        String entry = "(" + pid + "," + x + "," + y + "," + z + "," + w + ",'" + map + "')";
        return entry;
    }

}
