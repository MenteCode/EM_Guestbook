package emhs.db;

import emhs.db.components.*;
import emhs.db.handlers.UIMouseHandler;
import emhs.db.handlers.UITimedUpdateHandler;
import emhs.db.util.Pointer;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class GBReturnForm extends UIElement {
    private GBSelection typeSelector;
    private ArrayList<GBList> entryLists;
    private Runnable returnHandler;

    public GBReturnForm(Font headerFont, Font fieldFont, final int fieldWidth, int visibleNames, HashMap<String, Color> palette, final Pointer<Long> idPointer) {
        entryLists = new ArrayList<>();

        Color headerBackColor, headerTextColor;
        Color signOutBaseColor;
        Color signOutPressedColor;
        headerBackColor = palette.get("Header_Back");
        headerTextColor = palette.get("Header_Text");
        signOutBaseColor = palette.get("Accept_Button_Base");
        signOutPressedColor = palette.get("Accept_Button_Pressed");

        typeSelector = (GBSelection) new GBSelection(false, null, fieldFont, palette, fieldWidth, "Guest", "Staff").setPos(12, 72).setRegion(ComponentRegion.TOP_LEFT);
        typeSelector.setActive(true);

        entryLists.add((GBList) new GBList(fieldWidth, visibleNames, 10, fieldFont, palette).setPos(12, 128).setRegion(ComponentRegion.TOP_LEFT));
        entryLists.add((GBList) new GBList(fieldWidth, visibleNames, 10, fieldFont, palette).setPos(fieldWidth + 24, 128).setRegion(ComponentRegion.TOP_LEFT));

        final UIRoundRectangle signOutBtnBack;
        final UIText signInText, signOutText;
        UIRectangle signOutClickPane;
        UIClipEffect textClip = (UIClipEffect) new UIClipEffect().setClip(new Rectangle(200, 60)).setActive(true).setSize(200, 60);
        UIRestoreClipEffect textRestore = textClip.genRestoreClipEffect();

        UIElement signOutButton = (UIElement) new UIElement() {
        }.add(
                signOutBtnBack = (UIRoundRectangle) new UIRoundRectangle().setArcSize(10, 10).setPaint(signOutBaseColor).setAntiAliased(true).setSize(fieldWidth + 28, 70).setPos(0, 0).setRegion(ComponentRegion.TOP_LEFT),
                textClip, signOutText = (UIText) new UIText().setFont(headerFont.deriveFont(36.f)).setText("Sign Out").setPaint(Color.WHITE).setAntiAliased(true).setPos(0, 7).setIncludeInPack(false),
                signInText = (UIText) new UIText().setFont(headerFont.deriveFont(36.f)).setText("Sign In").setPaint(Color.WHITE).setAntiAliased(true).setPos(0, 75).setIncludeInPack(false), textRestore,
                signOutClickPane = (UIRectangle) new UIRectangle().setVisible(false).setSize(fieldWidth + 27, 60).setPos(1, 11).setRegion(ComponentRegion.TOP_LEFT)
        ).pack().setPos(0, entryLists.get(0).getSize().height + 132).setRegion(ComponentRegion.TOP_LEFT);

        final Color finalSignOutPressedColor = signOutPressedColor;
        final Color finalSignOutBaseColor = signOutBaseColor;
        signOutClickPane.addInteractionHandler(new UIMouseHandler(signOutClickPane) {
            public void onMousePress(int x, int y, int button) {
                signOutBtnBack.setPaint(finalSignOutPressedColor);
            }

            public void onMouseEnter() {
                if (isPressed()) {
                    signOutBtnBack.setPaint(finalSignOutPressedColor);
                }
            }

            public void onMouseExit() {
                signOutBtnBack.setPaint(finalSignOutBaseColor);
            }

            public void onMouseRelease(int x, int y, int button) {
                signOutBtnBack.setPaint(finalSignOutBaseColor);
            }

            public void onExternMouseRelease(int button) {
                signOutBtnBack.setPaint(finalSignOutBaseColor);
            }

            public void onMouseClick(int x, int y, int button) {
                int selected = 0;

                switch (typeSelector.getData()) {
                    case "Guest":
                        selected = 0;
                        break;
                    case "Staff":
                        selected = 1;
                        break;
                }

                Long data;
                if ((data = entryLists.get(selected).removeSelected()) == null) return;
                if (idPointer != null) idPointer.value = data;
                if (returnHandler != null) returnHandler.run();
            }
        });

        add(
                new UIRoundRectangle().setArcSize(10, 10).setPaint(headerBackColor).setAntiAliased(true).setSize(fieldWidth + 28, 70).setRegion(ComponentRegion.TOP_LEFT),
                new UIText().setFont(headerFont).setText("ACTIVITY TODAY").setPaint(headerTextColor).setAntiAliased(true).setRegion(ComponentRegion.TOP_MIDDLE).setPos(0, 18), signOutButton,
                new UIRectangle().setPaint(palette.get("Background")).setSize(fieldWidth + 28, entryLists.get(0).getSize().height + 82).setPos(0, 60).setRegion(ComponentRegion.TOP_LEFT),
                typeSelector
        ).pack();

        Rectangle clipShape = new Rectangle(getSize());
        clipShape.x += 6;
        clipShape.y += 6;

        clipShape.width -= 12;
        clipShape.height -= 12;

        UIClipEffect clip = (UIClipEffect) new UIClipEffect().setClip(clipShape).setRegion(ComponentRegion.TOP_LEFT);
        UIRestoreClipEffect restore = clip.genRestoreClipEffect();

        clip.setActive(true);

        add(clip);
        add(entryLists.toArray(new GBList[entryLists.size()]));
        add(restore);

        addTimedUpdateHandler(new UITimedUpdateHandler(this) {
            int previousSelection;
            float lerp = 0;
            boolean ready = true;

            public void intervalUpdate() {
                int selected = 0;

                switch (typeSelector.getData()) {
                    case "Guest":
                        selected = 0;
                        break;
                    case "Staff":
                        selected = 1;
                        break;
                }

                for (int i = 0; i < entryLists.size(); i++) {
                    entryLists.get(i).setActive(i == selected);
                }

                int diff = (previousSelection - selected);
                if (diff != 0) {
                    typeSelector.setActive(false);
                    if (ready) {
                        ready = false;
                    }

                    for (int i = 0; i < entryLists.size(); i++) {
                        GBList l = entryLists.get(i);
                        l.setPos((int) ((-previousSelection + GBUtilities.in_out_interpolate(lerp) * diff + i) * (fieldWidth + 24)) + 12, 128);
                    }

                    lerp = Math.min(1, Math.max(lerp += 0.0135f, 0));

                    if (lerp == 1.0) {
                        previousSelection = selected;
                        lerp = 0;
                        typeSelector.setActive(true);
                    }
                } else {
                    ready = true;
                    previousSelection = selected;
                }
            }
        });

        addTimedUpdateHandler(new UITimedUpdateHandler(this) {
            int previousSelection;
            float lerp = 0;

            public void intervalUpdate() {
                int selected = 0;

                switch (typeSelector.getData()) {
                    case "Guest":
                        selected = 0;
                        break;
                    case "Staff":
                        selected = 2;
                        break;
                }

                int diff = (previousSelection - selected);
                if (diff != 0) {
                    signOutText.setPos(0, (int) ((-previousSelection + GBUtilities.in_out_interpolate(lerp) * diff) * 50) + 7);
                    signInText.setPos(0, (int) ((-previousSelection + GBUtilities.in_out_interpolate(lerp) * diff + 2) * 50) + 7);

                    lerp = Math.min(1, Math.max(lerp += 0.0135f, 0));

                    if (lerp == 1.0) {
                        previousSelection = selected;
                        lerp = 0;
                    }
                } else {
                    previousSelection = selected;
                }
            }
        });
    }

    public void addEntry(String entry, String type, String time, long id) {
        int entryType;

        switch (type) {
            case "Guest":
                entryType = 0;
                break;
            case "Staff":
                entryType = 1;
                break;
            default:
                return;
        }

        entryLists.get(entryType).addEntry(entry, time, id);
    }

    public GBReturnForm setReturnHandler(Runnable handler) {
        returnHandler = handler;

        return this;
    }

    public String getSelectedType() {
        return typeSelector.getData();
    }
}