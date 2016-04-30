package emhs.db;

import emhs.db.components.ComponentRegion;
import emhs.db.components.UIElement;
import emhs.db.components.UIText;
import emhs.db.util.AppWindow;
import emhs.db.util.FontData;

import java.awt.*;
import java.util.ArrayList;

public class GBMultiLineText extends UIElement {
    private ArrayList<UIText> textLines = new ArrayList<>();
    private Font font;
    private String text;
    private Paint paint;
    private int maxWidth;
    private int spacing;

    public GBMultiLineText() {
    }

    public GBMultiLineText setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
        update();

        return this;
    }

    public GBMultiLineText setSpacing(int spacing) {
        this.spacing = spacing;
        update();

        return this;
    }

    public GBMultiLineText setFont(Font font) {
        this.font = font;
        update();

        return this;
    }

    public GBMultiLineText setText(String text) {
        this.text = text;
        update();

        return this;
    }

    public GBMultiLineText setPaint(Paint p) {
        this.paint = p;
        update();

        return this;
    }

    private void update() {
        if(font == null || text == null || maxWidth <= 0 || spacing < 0) return;
        FontData fDims = AppWindow.getFontDimensions(font, text);

        clear();
        textLines = new ArrayList<>();

        int fontHeight = fDims.height + spacing;
        int maxLineWidth = 0;
        int wordStartIdx = 0;
        int wordEndIdx;
        int i = 0;

        text += " ";

        while (wordStartIdx < text.length()) {
            String line = "";

            String word;

            boolean noWordsUsed = true;
            while (wordStartIdx < text.length() && (AppWindow.getFontDimensions(font, line + (word = text.substring(wordStartIdx, (wordEndIdx = text.indexOf(' ', wordStartIdx))))).width < maxWidth || noWordsUsed)) {
                line += " " + word;
                line = line.trim();
                wordStartIdx = wordEndIdx + 1;
                noWordsUsed = false;
            }

            wordStartIdx += line.length() + 1;

            UIText text = (UIText) new UIText().setFont(font).setText(line).setAntiAliased(true).setPos(0, fontHeight * i).setRegion(ComponentRegion.TOP_LEFT);
            textLines.add(text);
            maxLineWidth = Math.max(maxLineWidth, text.getSize().width);

            i++;
        }

        UIElement e = new UIElement() {};

        add(e);

        for (UIText t : textLines) {
            add(t.setPaint(paint));
        }

        pack();
    }
}