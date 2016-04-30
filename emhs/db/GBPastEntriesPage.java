package emhs.db;

import emhs.db.components.*;
import emhs.db.handlers.UITimedUpdateHandler;
import emhs.db.util.Pointer;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class GBPastEntriesPage extends GBUIPage {
    private GBSelection typeSelector;
    private ArrayList<GBList> entryLists;

    public GBPastEntriesPage(Font fieldFont, final int fieldWidth, int visibleNames, HashMap<String, Color> palette) {
        entryLists = new ArrayList<>();

        typeSelector = (GBSelection) new GBSelection(false, null, fieldFont, palette, fieldWidth, "Guests", "Staff", "Substitutes").setPos(0, 0).setRegion(ComponentRegion.TOP_MIDDLE);
        typeSelector.setActive(true);

        entryLists.add((GBList) new GBList(fieldWidth, visibleNames, 10, fieldFont, palette).setPos(1, 56).setRegion(ComponentRegion.TOP_MIDDLE).setIncludeInPack(false));
        entryLists.add((GBList) new GBList(fieldWidth, visibleNames, 10, fieldFont, palette).setPos(fieldWidth + 13, 56).setRegion(ComponentRegion.TOP_MIDDLE).setIncludeInPack(false));
        entryLists.add((GBList) new GBList(fieldWidth, visibleNames, 10, fieldFont, palette).setPos((fieldWidth << 1) + 25, 56).setRegion(ComponentRegion.TOP_MIDDLE).setIncludeInPack(false));

        add(
                new UIRectangle().setVisible(false).setSize(0, entryLists.get(0).getSize().height + 56),
                typeSelector
        ).pack();

        Rectangle clipShape = new Rectangle(getSize());

        clipShape.width += 2;
        clipShape.height += 2;

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
                    case "Guests":
                        selected = 0;
                        break;
                    case "Staff":
                        selected = 1;
                        break;
                    case "Substitutes":
                        selected = 2;
                        break;
                }

                int diff = (previousSelection - selected);
                if (diff != 0) {
                    typeSelector.setActive(false);
                    if (ready) {
                        ready = false;
                    }

                    for (int i = 0; i < entryLists.size(); i++) {
                        GBList l = entryLists.get(i);
                        l.setActive(i == selected);

                        l.setPos((int) ((-previousSelection + GBUtilities.in_out_interpolate(lerp) * diff + i) * (fieldWidth + 24)) + 1, 56);
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

        pack();
    }

    public void setEntryClickHandler(Runnable handler, Pointer<Long> entryID) {
        for (GBList l : entryLists) {
            l.setEntryClickHandler(handler, entryID);
        }
    }

    public GBPastEntriesPage addEntry(String entry, String type, String time, long id) {
        int entryType;

        switch (type) {
            case "Guest":
                entryType = 0;
                break;
            case "Staff":
                entryType = 1;
                break;
            case "Substitute":
                entryType = 2;
                break;
            default:
                return this;
        }

        entryLists.get(entryType).addEntry(entry, time, id);

        return this;
    }

    public GBPastEntriesPage setActive(boolean active) {
        typeSelector.setActive(active);

        for (GBList l : entryLists) {
            l.setActive(active);
        }

        return this;
    }
}
