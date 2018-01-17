package be.uantwerpen.sc.DSLParser;

public class Link {
    private int lid;
    private int start_point;
    private int stop_point;
    private int weight;
    private String access;
    private Integer length;

    public Link(int lid, int start_point, int stop_point, int weight, String access, Integer length) {

        this.lid = lid;
        this.start_point = start_point;
        this.stop_point = stop_point;
        this.weight = weight;
        this.access = access;
        this.length = length;
    }

    /**
     * Unused
     * Compiles a string to update a MySQL entry. Necessary to add ';'. Optional to add WHERE.
     *
     * @return a valid mysql update query
     */
    public String toSqlUpdateString() {
        String sql = "update core.linkTest set lid=\"" + lid + "\", start_point=\"" + start_point + "\", stop_point=\"" + stop_point + "\", weight=\"" + weight + "\", access=\"" + access + "\", length=\"" + length;
        return sql;
    }

    /**
     * Formats data for a proper MySQL entry
     * In the format: (int, 'String', ... )
     *
     * @return A valid entry for inserting MySQL VALUES
     */
    public String toSqlAddEntry() {
        String entry = "(" + lid + "," + start_point + "," + stop_point + "," + weight + ",'" + access + "'," + length + ")";
        return entry;
    }
}
