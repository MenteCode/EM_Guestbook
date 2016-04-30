package emhs.db.model;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class GBEntryBook {
    private XSSFWorkbook workbook;
    private XSSFSheet guestSheet;
    private XSSFSheet staffSheet;
    private XSSFSheet substituteSheet;
    private File path;

    private int guestRowIdx;
    private int staffRowIdx;
    private int substituteRowIdx;

    public GBEntryBook(File path) throws IOException, InvalidFormatException {
        this.path = path;

        guestRowIdx = staffRowIdx = substituteRowIdx = 1;

        if (!path.exists()) {
            workbook = new XSSFWorkbook();
            workbook.createSheet("Guests");
            workbook.createSheet("Staff");
            workbook.createSheet("Substitutes");

            guestSheet = workbook.getSheet("Guests");
            staffSheet = workbook.getSheet("Staff");
            substituteSheet = workbook.getSheet("Substitutes");

            guestSheet.setColumnWidth(0, 4096);
            guestSheet.setColumnWidth(1, 4096);
            guestSheet.setColumnWidth(2, 4096);
            guestSheet.setColumnWidth(3, 6144);
            guestSheet.setColumnWidth(4, 6144);

            staffSheet.setColumnWidth(0, 4096);
            staffSheet.setColumnWidth(1, 4096);
            staffSheet.setColumnWidth(2, 6144);
            staffSheet.setColumnWidth(3, 6144);

            substituteSheet.setColumnWidth(0, 4096);
            substituteSheet.setColumnWidth(1, 4096);
            substituteSheet.setColumnWidth(2, 4096);
            substituteSheet.setColumnWidth(3, 4096);
            substituteSheet.setColumnWidth(4, 4096);
            substituteSheet.setColumnWidth(5, 4096);
            substituteSheet.setColumnWidth(6, 4096);

            flush();
        } else {
            FileInputStream fileIn = new FileInputStream(path);
            workbook = new XSSFWorkbook(fileIn);
            fileIn.close();

            guestSheet = workbook.getSheet("Guests");
            staffSheet = workbook.getSheet("Staff");
            substituteSheet = workbook.getSheet("Substitutes");

            guestRowIdx = guestSheet.getPhysicalNumberOfRows();
            staffRowIdx = staffSheet.getPhysicalNumberOfRows();
            substituteRowIdx = substituteSheet.getPhysicalNumberOfRows();
        }

        XSSFRow guestRow = guestSheet.createRow(0);
        guestRow.createCell(0).setCellValue("Full Name");
        guestRow.createCell(1).setCellValue("Vehicle License");
        guestRow.createCell(2).setCellValue("Reason");
        guestRow.createCell(3).setCellValue("Sign In Time");
        guestRow.createCell(4).setCellValue("Sign Out Time");

        XSSFRow staffRow = staffSheet.createRow(0);
        staffRow.createCell(0).setCellValue("Full Name");
        staffRow.createCell(1).setCellValue("Reason");
        staffRow.createCell(2).setCellValue("Sign Out Time");
        staffRow.createCell(3).setCellValue("Sign In Time");

        XSSFRow subRow = substituteSheet.createRow(0);
        subRow.createCell(0).setCellValue("Full Name");
        subRow.createCell(1).setCellValue("Absent Teacher");
        subRow.createCell(2).setCellValue("Replacement Time");
        subRow.createCell(3).setCellValue("Phone Number");
        subRow.createCell(4).setCellValue("Job Number");
        subRow.createCell(5).setCellValue("Vehicle License");
        subRow.createCell(6).setCellValue("Sign In Time");
    }

    private static boolean isRowSafe(Row row, int[] cells) {
        if (row == null) return false;

        for (int c : cells) {
            Cell cell = row.getCell(c);

            if (cell == null)
                return false;
        }

        return true;
    }

    private static boolean isRowEmpty(Row row, int cellCount) {
        if (row == null) return true;

        for (int c = 0; c < cellCount; c++) {
            Cell cell = row.getCell(c);

            if (cell != null && !cell.getStringCellValue().trim().equals(""))
                return false;
        }

        return true;
    }

    public int addGuest(String name, String licencePlateNo, String reason, String timeIn) {
        XSSFRow row = guestSheet.createRow(guestRowIdx);
        row.createCell(0).setCellValue(name);
        row.createCell(1).setCellValue(licencePlateNo);
        row.createCell(2).setCellValue(reason);
        row.createCell(3).setCellValue(timeIn);

        return guestRowIdx++;
    }

    public void signOutGuest(int idx, String timeOut) {
        guestSheet.getRow(idx).createCell(4).setCellValue(timeOut);
    }

    public int addStaff(String name, String reason, String timeIn) {
        XSSFRow row = staffSheet.createRow(staffRowIdx);
        row.createCell(0).setCellValue(name);
        row.createCell(1).setCellValue(reason);
        row.createCell(2).setCellValue(timeIn);

        return staffRowIdx++;
    }

    public void signInStaff(int idx, String timeIn) {
        staffSheet.getRow(idx).createCell(3).setCellValue(timeIn);
    }

    public int addSubstitute(String name, String absentTeacher, String time, String phoneNumber, String jobNumber, String licencePlateNo, String timeIn) {
        XSSFRow row = substituteSheet.createRow(substituteRowIdx);
        row.createCell(0).setCellValue(name);
        row.createCell(1).setCellValue(absentTeacher);
        row.createCell(2).setCellValue(time);
        row.createCell(3).setCellValue(phoneNumber);
        row.createCell(4).setCellValue(jobNumber);
        row.createCell(5).setCellValue(licencePlateNo);
        row.createCell(6).setCellValue(timeIn);

        return substituteRowIdx++;
    }

    public ArrayList<String[]> getEntries() {
        ArrayList<String[]> entries = new ArrayList<>();

        for (int i = guestSheet.getPhysicalNumberOfRows() - 1; i > 0; i--) {
            Row r = guestSheet.getRow(i);
            if (isRowEmpty(r, 4) || !isRowSafe(r, new int[]{0, 2, 3})) continue;

            DateTime startOfToday = DateTime.now().withTimeAtStartOfDay();

            DateTime entryTime = DateTimeFormat.forPattern("MMM d yyyy HH:mm:ss").parseDateTime(r.getCell(3).getStringCellValue().replaceAll("(\\d{1,2})(?:st|nd|rd|th)", "$1"));
            DateTime returnTime = null;
            if (r.getCell(4) != null && r.getCell(4).getStringCellValue().trim().length() != 0)
                returnTime = DateTimeFormat.forPattern("MMM d yyyy HH:mm:ss").parseDateTime(r.getCell(4).getStringCellValue().replaceAll("(\\d{1,2})(?:st|nd|rd|th)", "$1"));

            if (!new Interval(startOfToday, startOfToday.plusDays(1)).contains(entryTime)) break;

            entries.add(
                    new String[]{
                            r.getCell(0).getStringCellValue(),
                            "Guest",
                            DateTimeFormat.forPattern("HH:mm").print(entryTime),
                            i + "",
                            r.getCell(1) == null ? "" : r.getCell(1).getStringCellValue(),
                            r.getCell(2).getStringCellValue(),
                            returnTime == null ? "" : DateTimeFormat.forPattern("HH:mm").print(returnTime)
                    }
            );
        }

        for (int i = staffSheet.getPhysicalNumberOfRows() - 1; i > 0; i--) {
            Row r = staffSheet.getRow(i);
            if (isRowEmpty(r, 3) || isRowSafe(r, new int[]{0, 1, 2})) continue;

            DateTime startOfToday = DateTime.now().withTimeAtStartOfDay();

            DateTime entryTime = DateTimeFormat.forPattern("MMM d yyyy HH:mm:ss").parseDateTime(r.getCell(2).getStringCellValue().replaceAll("(\\d{1,2})(?:st|nd|rd|th)", "$1"));
            DateTime returnTime = null;
            if (r.getCell(3) != null && r.getCell(3).getStringCellValue().trim().length() != 0)
                returnTime = DateTimeFormat.forPattern("MMM d yyyy HH:mm:ss").parseDateTime(r.getCell(3).getStringCellValue().replaceAll("(\\d{1,2})(?:st|nd|rd|th)", "$1"));

            if (!new Interval(startOfToday, startOfToday.plusDays(1)).contains(entryTime)) break;
            entries.add(
                    new String[]{
                            r.getCell(0).getStringCellValue(),
                            "Staff",
                            DateTimeFormat.forPattern("HH:mm").print(entryTime),
                            i + "",
                            r.getCell(1).getStringCellValue(),
                            returnTime == null ? "" : DateTimeFormat.forPattern("HH:mm").print(returnTime)
                    }
            );
        }

        for (int i = substituteSheet.getPhysicalNumberOfRows() - 1; i > 0; i--) {
            Row r = substituteSheet.getRow(i);
            if (isRowEmpty(r, 6) || isRowSafe(r, new int[]{0, 1, 2, 3, 4, 5})) continue;
            DateTime startOfToday = DateTime.now().withTimeAtStartOfDay();

            DateTime entryTime = DateTimeFormat.forPattern("MMM d yyyy HH:mm:ss").parseDateTime(r.getCell(6).getStringCellValue().replaceAll("(\\d{1,2})(?:st|nd|rd|th)", "$1"));

            if (!new Interval(startOfToday, startOfToday.plusDays(1)).contains(entryTime)) break;
            entries.add(
                    new String[]{
                            r.getCell(0).getStringCellValue(),
                            "Substitute",
                            DateTimeFormat.forPattern("HH:mm").print(entryTime),
                            i + "",
                            r.getCell(1).getStringCellValue(),
                            r.getCell(2).getStringCellValue(),
                            r.getCell(3).getStringCellValue(),
                            r.getCell(4).getStringCellValue(),
                            r.getCell(5) == null ? "" : r.getCell(5).getStringCellValue()
                    }
            );
        }

        return entries;
    }

    public void compressRows() {
        int cycles = 1048576;

        for (int i = 0; i < guestSheet.getPhysicalNumberOfRows(); i++) {
            if (isRowEmpty(guestSheet.getRow(i), 5)) {
                guestSheet.shiftRows(i-- + 1, guestSheet.getPhysicalNumberOfRows(), -1);
            }

            if (cycles-- == 0) break;
        }

        cycles = 1048576;

        for (int i = 0; i < staffSheet.getPhysicalNumberOfRows(); i++) {
            if (isRowEmpty(staffSheet.getRow(i), 4)) {
                staffSheet.shiftRows(i-- + 1, staffSheet.getPhysicalNumberOfRows(), -1);
            }

            if (cycles-- == 0) break;
        }

        cycles = 1048576;

        for (int i = 0; i < substituteSheet.getPhysicalNumberOfRows(); i++) {
            if (isRowEmpty(substituteSheet.getRow(i), 7)) {
                substituteSheet.shiftRows(i-- + 1, substituteSheet.getPhysicalNumberOfRows(), -1);
            }

            if (cycles-- == 0) break;
        }

        try {
            flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void flush() throws IOException {
        FileOutputStream excelFileStream = new FileOutputStream(path);
        workbook.write(excelFileStream);
        excelFileStream.close();
    }
}
