package emhs.db;

import emhs.db.components.ComponentRegion;
import emhs.db.components.UIElement;
import emhs.db.components.UIImage;
import emhs.db.components.UIText;
import emhs.db.handlers.UIInteractionHandler;
import emhs.db.handlers.UIMouseHandler;
import emhs.db.handlers.UITimedUpdateHandler;
import emhs.db.model.GBEntryBook;
import emhs.db.util.Pointer;
import emhs.db.util.ViewLayout;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;
import java.awt.*;
import java.io.*;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * @author Daniel Bogorad
 */
public class GBUILayout extends ViewLayout {
    public static OnScreenKeyboard FULL;
    public static OnScreenKeyboard NUMERIC;

    HashMap<String, Color> palette = new HashMap<>();
    ArrayList<GBNotification> notifications = new ArrayList<>();
    HashMap<Long, GBExcelEntry> entries = new HashMap<>();
    HashMap<Long, String[]> fullInfo = new HashMap<>();

    float kbLerpStep = 0.02f;
    UIElement msgBoard;

    /*
    Palette color attributes:
    - Background
    - Bound
    - Selection
    - Font_Dark
    - Font_Light
    - Header_Back
    - Header_Text
    - Reject_Button_Base
    - Reject_Button_Pressed
    - Accept_Button_Base
    - Accept_Button_Pressed
    - Key_Button_Back
    - Key_Button_Overlay
    - Keyboard_Back
     */

    public GBUILayout(final String parentPath, final Runnable quitHandler, final GBEntryBook entryBook, final XSSFWorkbook emergencyBook) throws IOException, FontFormatException, URISyntaxException, AWTException {
        /**
         * Color palette setup
         **/

        palette.put("Background", new Color(230, 255, 232));
        palette.put("Bound", new Color(171, 194, 173));
        palette.put("Selection", new Color(69, 77, 70));
        palette.put("Font_Dark", new Color(103, 115, 103));
        palette.put("Font_Light", new Color(202, 230, 202));
        palette.put("Header_Back", new Color(75, 180, 0));
        palette.put("Header_Text", Color.WHITE);
        palette.put("Reject_Button_Base", new Color(173, 41, 41));
        palette.put("Reject_Button_Pressed", new Color(128, 32, 32));
        palette.put("Accept_Button_Base", new Color(61, 145, 66));
        palette.put("Accept_Button_Pressed", new Color(47, 115, 53));
        palette.put("Key_Button_Back", new Color(255, 255, 255, 128));
        palette.put("Key_Button_Overlay", new Color(153, 214, 255));
        palette.put("Keyboard_Back", new Color(117, 176, 188, 225));

        /**
         * Fonts
         **/

        final Font GIDOLE = Font.createFont(Font.TRUETYPE_FONT, new File(parentPath, "resource/Font_Gidole/Gidole-Regular.ttf")).deriveFont(18.f);
        final Font MODERNE_SANS = Font.createFont(Font.TRUETYPE_FONT, new File(parentPath, "resource/Font_ModerneSans/MODERNE_SANS.ttf")).deriveFont(250.f);

        /**
         * Contact info
         **/

        File contactInfo = new File(parentPath, "Contact_The_Creator.txt");
        if (!contactInfo.exists()) {
            PrintWriter FOS = new PrintWriter(contactInfo);
            FOS.println("All questions regarding the app (Broken features, feature requests, etc.) can be sent to the e-mail below:\nmentecode@gmail.com\n");
            FOS.println("Please provide as detailed a description as you can with your request. I will try to reply within a week.");
            FOS.close();
        }

        /**
         * Keyboard initialization
         **/

        final OnScreenKeyboard[] activeKB = new OnScreenKeyboard[1];
        final boolean[] kbRequested = {false};

        FULL = (OnScreenKeyboard) new OnScreenKeyboard(GIDOLE.deriveFont(21.f), 70, 10, palette).setRegion(ComponentRegion.BOTTOM_MIDDLE);
        NUMERIC = (OnScreenKeyboard) new OnScreenKeyboard(GIDOLE.deriveFont(21.f), 70, 10, palette).setLayout("123", "456", "789", "0d").setRegion(ComponentRegion.BOTTOM_MIDDLE);

        FULL.addTimedUpdateHandler(new UITimedUpdateHandler(FULL) {
            float pos = 0;

            public void intervalUpdate() {
                parent.setPos(0, (int) (parent.getSize().height * GBUtilities.in_out_interpolate(1.f - pos) - 15 * GBUtilities.in_out_interpolate(pos)));

                if (activeKB[0] == parent) {
                    pos = Math.max(Math.min(pos += kbLerpStep, 1), 0);
                } else {
                    pos = Math.max(Math.min(pos -= kbLerpStep, 1), 0);
                }
            }
        }).setPos(0, 1000);

        NUMERIC.addTimedUpdateHandler(new UITimedUpdateHandler(NUMERIC) {
            float pos = 0;

            public void intervalUpdate() {
                parent.setPos(0, (int) (parent.getSize().height * GBUtilities.in_out_interpolate(1.f - pos) - 15 * GBUtilities.in_out_interpolate(pos)));

                if (activeKB[0] == parent) {
                    pos = Math.max(Math.min(pos += kbLerpStep, 1), 0);
                } else {
                    pos = Math.max(Math.min(pos -= kbLerpStep, 1), 0);
                }
            }
        }).setPos(0, 1000);

        RequestKeyboardInterface kbRequestor = new RequestKeyboardInterface() {
            public void requestKeyboard(OnScreenKeyboard kb) {
                if (activeKB[0] != kb) {
                    activeKB[0] = kb;
                }

                kbRequested[0] = true;
            }
        };

        /**
         * Notification board
         **/

        msgBoard = new UIElement() {
        };

        /**
         * Decorative elements
         **/

        final GriffinLogo griffinLogo = (GriffinLogo) new GriffinLogo().setPaint(new Color(0, 0, 0, 100)).setAntiAliased(true).setSize(473, 656).setRegion(ComponentRegion.MIDDLE_MIDDLE);
        final DBLogo dbLogo = (DBLogo) new DBLogo().setPaint(new Color(0, 0, 0, 0)).setRegion(ComponentRegion.BOTTOM_RIGHT).setPos(-25, -25).setSize(80, 80);
        final UIText schoolInitials = (UIText) new UIText().setFont(MODERNE_SANS).setText("EM  HS").setPaint(new Color(0, 0, 0, 0)).setAntiAliased(true).setRegion(ComponentRegion.BOTTOM_MIDDLE);

        /**
         * Interface panel initialization
         **/

        int entryFieldWidth = 346;
        int adminPanelButtonWidth = 250;
        int returnFieldWidth = 276;
        int pastEntriesFieldWidth = 310;
        int maxViewEntryPageWidth = 500;
        final long[] id = {0};

        final Pointer<Boolean> startingUp = new Pointer<>(true);
        final Pointer<Long> idPointer = new Pointer<>(null);

        final GBReturnForm returnPanel = (GBReturnForm) new GBReturnForm(MODERNE_SANS.deriveFont(32.f), GIDOLE, returnFieldWidth, 8, palette, idPointer)
                .setPos(-500, -1000);

        final GBPastEntriesPage pastEntriesPage = new GBPastEntriesPage(GIDOLE, pastEntriesFieldWidth, 8, palette);

        returnPanel.setReturnHandler(new Runnable() {
            public void run() {
                GBExcelEntry entry = entries.get(idPointer.value);

                LocalDateTime date = LocalDateTime.now();
                String dateTime = DateTimeFormat.forPattern("MMM d__ yyyy HH:mm:ss").print(date);
                int dateOM = date.getDayOfMonth();
                dateTime = dateTime.replace("__", (dateOM >= 4 && dateOM <= 20) || dateOM % 10 > 3 || dateOM % 10 == 0 ? "th" : dateOM % 10 == 1 ? "st" : dateOM % 10 == 2 ? "nd" : "rd");
                String time = DateTimeFormat.forPattern("HH:mm").print(date);

                switch (returnPanel.getSelectedType()) {
                    case "Guest":
                        entryBook.signOutGuest(entry.row, dateTime);
                        break;
                    case "Staff":
                        entryBook.signInStaff(entry.row, dateTime);
                        break;
                }

                String[] arr = fullInfo.get(idPointer.value);
                arr[arr.length - 1] = time;
                fullInfo.put(idPointer.value, arr);
            }
        });

        ArrayList<String[]> excelEntries = entryBook.getEntries();

        for (int i = excelEntries.size() - 1; i >= 0; i--) {
            String[] e = excelEntries.get(i);
            if (e[e.length - 1].trim().length() == 0) {
                returnPanel.addEntry(e[0], e[1], e[2], id[0]);
                entries.put(id[0], new GBExcelEntry(e[0], e[1], e[2], Integer.parseInt(e[3])));
            }

            ArrayList<String> eL = new ArrayList<>(Arrays.asList(e));
            eL.add(id[0] + "");
            fullInfo.put(id[0], eL.toArray(new String[eL.size()]));

            id[0]++;
        }

        ArrayList<String[]> sortedEntries = new ArrayList<>(fullInfo.values());

        Collections.sort(sortedEntries, new Comparator<String[]>() {
            public int compare(String[] o1, String[] o2) {
                return o1[2].compareTo(o2[2]);
            }
        });

        for (String[] e : sortedEntries) {
            pastEntriesPage.addEntry(e[0], e[1], e[2], Integer.parseInt(e[e.length - 1]));
        }

        Set<Map.Entry<Long, String[]>> fullEntries = fullInfo.entrySet();
        Iterator<Map.Entry<Long, String[]>> iterator = fullEntries.iterator();
        Map.Entry<Long, String[]> entryVal;

        while(iterator.hasNext()) {
            entryVal = iterator.next();

            fullInfo.put(entryVal.getKey(), Arrays.copyOfRange(entryVal.getValue(), 0, entryVal.getValue().length - 1));
        }

        final Runnable emergencyHandler = new Runnable() {
            public void run() {
                int inIdx = 1;
                int outIdx = 1;

                XSSFSheet inSheet = emergencyBook.getSheet("In the building");
                XSSFSheet outSheet = emergencyBook.getSheet("Out of the building");
                if (inSheet == null) inSheet = emergencyBook.createSheet("In the building");
                if (outSheet == null) outSheet = emergencyBook.createSheet("Out of the building");

                XSSFRow headerRow = inSheet.createRow(0);
                inSheet.setColumnWidth(0, 4096);
                inSheet.setColumnWidth(1, 2816);

                headerRow.createCell(0).setCellValue("Full Name");
                headerRow.createCell(1).setCellValue("Time of Sign-in");

                headerRow = outSheet.createRow(0);
                outSheet.setColumnWidth(0, 4096);
                outSheet.setColumnWidth(1, 2816);

                headerRow.createCell(0).setCellValue("Full Name");
                headerRow.createCell(1).setCellValue("Time of Sign-out");

                for (GBExcelEntry e : entries.values()) {
                    XSSFRow row;

                    switch (e.type) {
                        case "Guest":
                        case "Substitute":
                            row = inSheet.createRow(inIdx);
                            row.createCell(0).setCellValue(e.name);
                            row.createCell(1).setCellValue(e.time);
                            inIdx++;
                            break;
                        case "Staff":
                            row = outSheet.createRow(outIdx);
                            row.createCell(0).setCellValue(e.name);
                            row.createCell(1).setCellValue(e.time);
                            outIdx++;
                            break;
                    }
                }

                try {
                    File f = new File(parentPath, "emergency_sheet.xlsx");
                    f.delete();
                    f.createNewFile();
                    FileOutputStream emergencyOutput = new FileOutputStream(f);
                    emergencyBook.write(emergencyOutput);
                    emergencyOutput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        emergencyHandler.run();

        final GBTextField nameField = new GBTextField(new UIImage().setImage(ImageIO.read(new File(parentPath, "resource/icons/person.png"))), GIDOLE, "Full Name", true, entryFieldWidth, kbRequestor, FULL, palette);
        final GBTextField absentTeachField = new GBTextField(new UIImage().setImage(ImageIO.read(new File(parentPath, "resource/icons/swap.png"))), GIDOLE, "Name of Absent Teacher", true, entryFieldWidth, kbRequestor, FULL, palette);
        final GBSelection substitutePeriodField = new GBSelection(true, new UIImage().setImage(ImageIO.read(new File(parentPath, "resource/icons/clock.png"))), GIDOLE, palette, entryFieldWidth, "Full Day", "Half Day");
        final GBTextField phoneNumberField = new GBTextField(new UIImage().setImage(ImageIO.read(new File(parentPath, "resource/icons/phone.png"))), GIDOLE, "Phone Number", true, entryFieldWidth, kbRequestor, NUMERIC, palette);
        final GBTextField jobNumberField = new GBTextField(new UIImage().setImage(ImageIO.read(new File(parentPath, "resource/icons/briefcase.png"))), GIDOLE, "Job Number", true, entryFieldWidth, kbRequestor, NUMERIC, palette);
        final GBTextField licenceNoField = new GBTextField(new UIImage().setImage(ImageIO.read(new File(parentPath, "resource/icons/car.png"))), GIDOLE, "Vehicle Licence (recommended)", false, entryFieldWidth, kbRequestor, FULL, palette);
        final GBTextField reasonField = new GBTextField(new UIImage().setImage(ImageIO.read(new File(parentPath, "resource/icons/magnifying_glass.png"))), GIDOLE, "Reason", true, entryFieldWidth, kbRequestor, FULL, palette);

        final GBEntryForm entryForm = (GBEntryForm) new GBEntryForm(MODERNE_SANS.deriveFont(36.f), GIDOLE, entryFieldWidth, palette, "Guest", "Staff", "Substitute")
                .addField(nameField, "Guest", "Staff", "Substitute")
                .addField(absentTeachField, "Substitute")
                .addField(substitutePeriodField, "Substitute")
                .addField(phoneNumberField, "Substitute")
                .addField(jobNumberField, "Substitute")
                .addField(licenceNoField, "Guest", "Substitute")
                .addField(reasonField, "Guest", "Staff")
                .finish().setPos(0, -1000);

        entryForm.setSaveHandler(new Runnable() {
            public void run() {
                LocalDateTime date = LocalDateTime.now();
                String dateTime = DateTimeFormat.forPattern("MMM d__ yyyy HH:mm:ss").print(date);
                String time = DateTimeFormat.forPattern("HH:mm").print(date);
                int dateOM = date.getDayOfMonth();
                dateTime = dateTime.replace("__", (dateOM >= 4 && dateOM <= 20) || dateOM % 10 > 3 || dateOM % 10 == 0 ? "th" : dateOM % 10 == 1 ? "st" : dateOM % 10 == 2 ? "nd" : "rd");

                String entryType = entryForm.getSelectedType();
                returnPanel.addEntry(nameField.getData(), entryType, time, id[0]);

                int idx = 0;

                switch (entryType) {
                    case "Guest":
                        idx = entryBook.addGuest(nameField.getData(), licenceNoField.getData(), reasonField.getData(), dateTime);
                        fullInfo.put(id[0], new String[]{nameField.getData(), entryType, time, idx + "", licenceNoField.getData(), reasonField.getData(), ""});
                        break;
                    case "Staff":
                        idx = entryBook.addStaff(nameField.getData(), reasonField.getData(), dateTime);
                        fullInfo.put(id[0], new String[]{nameField.getData(), entryType, time, idx + "", reasonField.getData(), ""});
                        break;
                    case "Substitute":
                        idx = entryBook.addSubstitute(nameField.getData(), absentTeachField.getData(), substitutePeriodField.getData(), phoneNumberField.getData(), jobNumberField.getData(), licenceNoField.getData(), dateTime);
                        fullInfo.put(id[0], new String[]{nameField.getData(), entryType, time, idx + "", absentTeachField.getData(), substitutePeriodField.getData(), phoneNumberField.getData(), jobNumberField.getData(), licenceNoField.getData()});
                        break;
                }

                pastEntriesPage.addEntry(nameField.getData(), entryType, time, id[0]);
                entries.put(id[0], new GBExcelEntry(nameField.getData(), time, entryType, idx));

                id[0]++;

                displayNotification(new GBNotification(GIDOLE.deriveFont(18.f), "Thank you. " + (entryType.equals("Substitute") ? "As you are a substitute, we do not require your return time." : "Please remember to sign " + (entryType.equals("Guest") ? "out" : "in") + " on the left when you're done."), GBNotification.NOTIFICATION, 300, 15, 375, 0.02f));

                emergencyHandler.run();
            }
        });

        entryForm.setIncompleteFormHandler(new Runnable() {
            public void run() {
                displayNotification(new GBNotification(GIDOLE.deriveFont(18.f), "Please fill in all the required fields.", GBNotification.WARNING, 300, 15, 250, 0.02f));
            }
        });

        final Pointer<Float> pwLerp = new Pointer<>(0.f);
        final GBTextField pwField = new GBTextField(null, GIDOLE, "Password", false, adminPanelButtonWidth, kbRequestor, FULL, palette);

        final boolean[] exitButtonPressed = {false};
        final GBButton exitButton = new GBButton("Exit Application", GIDOLE, null, adminPanelButtonWidth, 75, palette).setTask(new Runnable() {
            public void run() {
                exitButtonPressed[0] = true;

                try {
                    MessageDigest sha = MessageDigest.getInstance("SHA-256");
                    sha.update((pwField.getData() + "D\\b|gU$A.1!?E=hT ").getBytes("UTF-8"));
                    byte[] shaResult = sha.digest();

                    if (DatatypeConverter.printHexBinary(shaResult).equals("0A85E964723B0845C231F61788ADEB6C337A1909FC181824FFC510EBADAD8640")) {
                        if (quitHandler != null) quitHandler.run();
                    }
                } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });

        final GBButton showPastEntriesButton;

        HashMap<String, Integer> pageIndexer = new HashMap<>();
        pageIndexer.put("adminPage", 0);
        pageIndexer.put("pastEntriesPage", 1);
        pageIndexer.put("viewEntry", 2);

        ArrayList<GBUIPage> pages = new ArrayList<>();
        pages.add(new GBAdminPage()
                .addField(showPastEntriesButton = new GBButton("Show Past Entries", GIDOLE, null, adminPanelButtonWidth, 75, palette))
                .addField(exitButton)
                .addField(pwField, pwLerp)
                .finish());

        pages.add(pastEntriesPage);

        final GBViewEntryPage viewEntryPage = new GBViewEntryPage(GIDOLE, maxViewEntryPageWidth, palette);
        pages.add(viewEntryPage);

        final GBAdminPanel adminPanel = (GBAdminPanel) new GBAdminPanel(MODERNE_SANS.deriveFont(36.f), pages, pageIndexer, "adminPage", palette).setPos(500, -1000);

        showPastEntriesButton.setTask(new Runnable() {
            @Override
            public void run() {
                adminPanel.showPage("pastEntriesPage");
                adminPanel.showFooter("Back");
                adminPanel.setFooterClickHandler(new Runnable() {
                    public void run() {
                        adminPanel.showPage("adminPage");
                        adminPanel.hideFooter();
                    }
                });
            }
        });

        final Pointer<Long> retrieveEntryPointer = new Pointer<>(0l);

        pastEntriesPage.setEntryClickHandler(new Runnable() {
            public void run() {
                String[] info = fullInfo.get(retrieveEntryPointer.value);

                ArrayList<Pair<String, String>> infoMap = new ArrayList<>();

                switch (info[1]) {
                    case "Guest":
                        infoMap.add(new Pair<>("Name:", info[0]));
                        infoMap.add(new Pair<>("Type:", info[1]));
                        infoMap.add(new Pair<>("Sign-in Time:", info[2]));
                        infoMap.add(new Pair<>("Licence Plate:", info[4]));
                        infoMap.add(new Pair<>("Reason:", info[5]));
                        infoMap.add(new Pair<>("Sign-out Time:", info[6]));
                        break;
                    case "Staff":
                        infoMap.add(new Pair<>("Name:", info[0]));
                        infoMap.add(new Pair<>("Type:", info[1]));
                        infoMap.add(new Pair<>("Sign-out Time:", info[2]));
                        infoMap.add(new Pair<>("Reason:", info[4]));
                        infoMap.add(new Pair<>("Sign-in Time:", info[5]));
                        break;
                    case "Substitute":
                        infoMap.add(new Pair<>("Name:", info[0]));
                        infoMap.add(new Pair<>("Type:", info[1]));
                        infoMap.add(new Pair<>("Sign-in Time:", info[2]));
                        infoMap.add(new Pair<>("Absent Teacher:", info[4]));
                        infoMap.add(new Pair<>("Replacement Time:", info[5]));
                        infoMap.add(new Pair<>("Phone Number:", info[6]));
                        infoMap.add(new Pair<>("Job Number:", info[7]));
                        infoMap.add(new Pair<>("Licence Plate:", info[8]));
                        break;
                }

                viewEntryPage.displayEntry(infoMap);
                adminPanel.showPage("viewEntry");
                adminPanel.setFooterClickHandler(new Runnable() {
                    public void run() {
                        adminPanel.showPage("pastEntriesPage");
                        adminPanel.setFooterClickHandler(new Runnable() {
                            public void run() {
                                adminPanel.showPage("adminPage");
                                adminPanel.hideFooter();
                            }
                        });
                    }
                });
            }
        }, retrieveEntryPointer);

        final boolean[] pwFieldActive = {false};

        pwField.addInteractionHandler(new UIMouseHandler(pwField) {
            public void onMousePress(int mX, int mY, int button) {
                if (!exitButtonPressed[0]) pwFieldActive[0] = true;
            }

            public void onExternMousePress(int button) {
                pwFieldActive[0] = false;
            }
        });

        adminPanel.addTimedUpdateHandler(new UITimedUpdateHandler(adminPanel) {
            int tick = 0;
            int increment;
            boolean displayPWField = false;

            public void intervalUpdate() {
                pwLerp.value = GBUtilities.in_out_interpolate(Math.min(Math.max(tick / 50.f, 0), 1));

                pwField.setClickActive(tick >= 50);

                if (tick >= 625 && displayPWField) {
                    increment = -1;
                    displayPWField = false;
                }

                if (exitButtonPressed[0] && tick <= 0 && !pwFieldActive[0]) {
                    tick = 0;
                    increment = 1;
                    displayPWField = true;
                } else if (pwFieldActive[0]) {
                    tick = 51;
                    increment = -1;
                } else if (tick < 0) {
                    pwField.reset();
                }

                exitButtonPressed[0] = false;

                tick += increment;
            }
        });

        /**
         * Startup animations
         **/

        addTimedUpdateHandler(new UITimedUpdateHandler(this) {
            float tick = 0;

            public void intervalUpdate() {
                float val = GBUtilities.in_interpolate(Math.max((tick - 125) / 100.f, 0), 2.1f);
                float val2 = GBUtilities.in_interpolate(Math.max((tick - 100) / 125.f, 0), 1.7f);

                griffinLogo.setPos(0, (int) ((parent.getSize().height - griffinLogo.getSize().height - 50) * val * 0.5f)).setSize((int) (473 - 250 * val2), (int) (656 - 346 * val2));
                schoolInitials.setPaint(new Color(0, 0, 0, (int) (100 * val)));
                dbLogo.setPaint(new Color(0, 0, 0, (int) (50 * val)));

                if (val >= 1) {
                    griffinLogo.setPos(0, -25).setRegion(ComponentRegion.BOTTOM_MIDDLE);
                    parent.removeTimedUpdateHandler(this);

                    parent.addTimedUpdateHandler(new UITimedUpdateHandler(parent) {
                        float tick = 0;

                        public void intervalUpdate() {
                            float val = GBUtilities.in_interpolate(1 - tick / 80.f, 3.5f);

                            entryForm.setPos(0, (int) (-150 - 850 * val));
                            returnPanel.setPos(-500, (int) (-150 - 850 * val));
                            adminPanel.setPos(500, (int) (-150 - 850 * val));

                            if (tick == 80) {
                                startingUp.value = false;
                                parent.removeTimedUpdateHandler(this);
                            }

                            tick++;
                        }
                    });

                    return;
                }

                tick++;
            }
        });

        add(
                new GBUIBackground(parentPath),
                dbLogo, schoolInitials, griffinLogo,
                entryForm, adminPanel, returnPanel,
                msgBoard, NUMERIC, FULL
        );

        addInteractionHandler(new UIMouseHandler(this) {
            public void onMousePress(int mX, int mY, int button) {
                if (kbRequested[0]) kbRequested[0] = false;
                else if (activeKB[0] != null && !activeKB[0].isPressed())
                    activeKB[0] = null;
            }
        });

        addInteractionHandler(new UIInteractionHandler(this) {
            public boolean update(AWTEvent e, String s) {
                if (s.equals("WindowResized")) {
                    Component c = (Component) e.getSource();
                    parent.setSize(c.getWidth(), c.getHeight());
                }

                return false;
            }
        });
    }

    private void displayNotification(final GBNotification msg) {
        for (final GBNotification n : notifications) {
            n.addTimedUpdateHandler(new UITimedUpdateHandler(n) {
                float tick = 0;

                public void intervalUpdate() {
                    n.translate(0, 2);
                    tick++;

                    if (tick * 2 >= msg.getSize().height + 10) {
                        n.removeTimedUpdateHandler(this);
                    }
                }
            });
        }

        notifications.add(msg);
        msgBoard.add(msg.setPos(0, 125 + msg.getSize().height / 2));
        msg.start();

        msg.addTimedUpdateHandler(new UITimedUpdateHandler(msg) {
            public void intervalUpdate() {
                if (msg.isFinished()) msgBoard.remove(msg);
            }
        });
    }
}