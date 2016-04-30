package emhs.db;

import emhs.db.components.ComponentRegion;
import emhs.db.components.UIClipEffect;
import emhs.db.components.UIRectangle;
import emhs.db.components.UIText;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class GBViewEntryPage extends GBUIPage {
    private ArrayList<GBMultiLineText> values;
    private ArrayList<UIText> keys;
    private Color fieldColor;
    private Color boundColor;
    private Font font;

    private int maxFieldWidth;

    public GBViewEntryPage(Font dataFont, final int maxFieldWidth, HashMap<String, Color> palette) {
        fieldColor = palette.get("Font_Dark");
        boundColor = palette.get("Bound");
        this.maxFieldWidth = maxFieldWidth;
        values = new ArrayList<>();
        keys = new ArrayList<>();
        this.font = dataFont;
    }

    public void displayEntry(ArrayList<Pair<String, String>> fieldMap) {
        clear();

        keys = new ArrayList<>();
        values = new ArrayList<>();

        int height = 0;
        int entryWidth = 0;
        int width = maxFieldWidth/3;

        UIClipEffect textClip = new UIClipEffect();

        for (Pair<String, String> e : fieldMap) {
            UIText t = (UIText) new UIText().setFont(font).setText(e.key).setPaint(fieldColor).setAntiAliased(true).setRegion(ComponentRegion.TOP_LEFT).setPos(0, height);

            GBMultiLineText val;

            keys.add(t);
            values.add(val = (GBMultiLineText) new GBMultiLineText()
                    .setSpacing(5)
                    .setMaxWidth(maxFieldWidth - width - 20)
                    .setFont(font)
                    .setText(e.value.trim().length() == 0 ? "n/a" : e.value)
                    .setPaint(fieldColor)
                    .setPos(0, height)
                    .setRegion(ComponentRegion.TOP_LEFT)
                    .setIncludeInPack(false));

            height += val.getSize().height + 24;
            entryWidth = Math.max(t.getSize().width, entryWidth);
        }

        for(UIText t : keys) {
            t.translate(10, 7);
        }

        int maxValueWidth = 0;

        for(GBMultiLineText t : values) {
            t.translate(entryWidth + 20, 7);
            maxValueWidth = Math.max(maxValueWidth, t.getSize().width);
        }

        maxValueWidth = Math.min(maxValueWidth, maxFieldWidth - width);

        entryWidth += maxValueWidth + 30;

        Rectangle textClipBound = new Rectangle(entryWidth - 10, height);

        textClip.setClip(textClipBound)
                .setActive(true)
                .setSize(entryWidth - 10, height).setIncludeInPack(false);

        for (GBMultiLineText t : values) {
            add(new UIRectangle().setHollow(true).setStroke(new BasicStroke(2)).setPaint(boundColor).setSize(entryWidth, t.getSize().height + 12).setPos(1, t.getLocalPos().y - 6).setRegion(ComponentRegion.TOP_LEFT));
        }

        add(textClip);

        add(keys.toArray(new UIText[keys.size()]));
        add(values.toArray(new GBMultiLineText[values.size()]));

        add(textClip.genRestoreClipEffect());

        pack();
    }

    public GBViewEntryPage setActive(boolean active) {
        return this;
    }
}