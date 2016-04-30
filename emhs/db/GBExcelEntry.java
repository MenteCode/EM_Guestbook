package emhs.db;

public class GBExcelEntry {
    public String name;
    public String time;
    public String type;
    public int row;

    public GBExcelEntry(String name, String time, String type, int row) {
        this.name = name;
        this.time = time;
        this.type = type;
        this.row = row;
    }
}
