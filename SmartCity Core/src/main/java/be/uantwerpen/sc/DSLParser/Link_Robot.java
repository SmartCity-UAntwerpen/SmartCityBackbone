package be.uantwerpen.sc.DSLParser;

public class Link_Robot {
    private int lid;
    private String start_dir;
    private String stop_dir;

    public Link_Robot(int lid, String start_dir, String stop_dir) {

        this.lid = lid;
        this.start_dir = start_dir;
        this.stop_dir = stop_dir;
    }

    /**
     * Formats data for a proper MySQL entry
     * In the format: (int, 'String', ... )
     *
     * @return A valid entry for inserting MySQL VALUES
     */
    public String toSqlAddEntry() {
        String entry = "(" + lid + ",'" + start_dir + "','" + stop_dir + "')";
        return entry;
    }
}
