package emhs.db;

import emhs.db.model.GBEntryBook;
import emhs.db.util.AppWindow;
import emhs.db.util.UISurface;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;

public class Main {
    public static void main(String[] args) throws IOException {
        String parentPath = null;
        try {
            parentPath = new File(GBUILayout.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        File debug = new File(parentPath, "debug.txt");
        debug.createNewFile();
        final PrintStream stackTrace = new PrintStream(debug);
        System.setErr(stackTrace);

        final String finalParentPath = parentPath;
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                final AppWindow window = new AppWindow("Ernest Manning Sign-in Utility", 1920, 1080, true);

                UISurface surface = null;

                LocalDateTime date = LocalDateTime.now();
                String month = DateTimeFormat.forPattern("MMMM").print(date);

                GBEntryBook book = null;
                final File bookFile = new File(finalParentPath, "Records/" + date.getYear() + "/" + month + "/guestbook_sheet" + date.getDayOfMonth() + ".xlsx");
                bookFile.getParentFile().mkdirs();

                try {
                    book = new GBEntryBook(bookFile);
                } catch (Exception e) {
                    bookFile.delete();
                    try {
                        book = new GBEntryBook(bookFile);
                    } catch (IOException | InvalidFormatException e1) {
                        e1.printStackTrace();
                    }
                }

                final XSSFWorkbook emergencyBook = new XSSFWorkbook();

                final GBEntryBook finalBook = book;

                Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                    public void run() {
                        try {
                            if (finalBook != null) {
                                finalBook.compressRows();
                                finalBook.flush();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }));

                try {
                    surface = new GBUILayout(new File(GBUILayout.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent(), new Runnable() {
                        public void run() {
                            window.close();
                            System.exit(0);
                        }
                    }, book, emergencyBook).generateSurface();
                } catch (IOException | FontFormatException | AWTException | URISyntaxException e) {
                    e.printStackTrace();
                }

                window.bindUI(surface);

                window.beginRender();
                window.setFullscreen(true);
            }
        });
    }
}

