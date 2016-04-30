package emhs.db;

import emhs.db.components.ComponentRegion;
import emhs.db.components.UIElement;
import emhs.db.components.UIRoundRectangle;
import emhs.db.components.UIText;
import emhs.db.handlers.UITimedUpdateHandler;
import emhs.db.util.AppWindow;
import emhs.db.util.FontData;

import java.awt.*;
import java.util.ArrayList;

public class GBNotification extends UIElement {
    public static final Color NOTIFICATION = new Color(181, 255, 181, 192);
    public static final Color WARNING = new Color(255, 255, 179, 192);
    public static final Color URGENT = new Color(255, 181, 181, 192);

    private boolean started;
    private boolean finished;

    private float displaceHeight;

    public GBNotification(Font font, String msg, Color backgroundColor, int maxWidth, int padding, final int displayTicks, final float fadeRate) {
        FontData fDims = AppWindow.getFontDimensions(font, msg);

        int height = fDims.height;

        final ArrayList<UIText> textLines = new ArrayList<>();

        int fontHeight = fDims.height + 5;
        int maxLineWidth = 0;
        int wordStartIdx = 0;
        int wordEndIdx;
        int i = 0;

        msg += " ";

        while (wordStartIdx < msg.length()) {
            String line = "";

            String word;

            boolean noWordsUsed = true;
            while (wordStartIdx < msg.length() && (AppWindow.getFontDimensions(font, line + (word = msg.substring(wordStartIdx, (wordEndIdx = msg.indexOf(' ', wordStartIdx))))).width < maxWidth - 2 * padding || noWordsUsed)) {
                line += " " + word;
                line = line.trim();
                wordStartIdx = wordEndIdx + 1;
                noWordsUsed = false;
            }

            UIText text = (UIText) new UIText().setFont(font).setText(line).setAntiAliased(true).setPos(padding, padding + fontHeight * i).setRegion(ComponentRegion.TOP_LEFT);
            textLines.add(text);
            maxLineWidth = Math.max(maxLineWidth, text.getSize().width);

            height += fDims.height + 5;
            i++;
        }

        final UIRoundRectangle back;
        add(back = (UIRoundRectangle) new UIRoundRectangle().setArcSize(10, 10).setPaint(backgroundColor).setAntiAliased(true).setSize(maxLineWidth + 2 * padding, height - fDims.height - 5 + 2 * padding));

        for (UIText t : textLines) {
            add(t);
        }

        addTimedUpdateHandler(new UITimedUpdateHandler(this) {
            int tick = 0;
            float alpha = 0;

            public void intervalUpdate() {
                if (!started) return;

                if (alpha < 1 && tick == 0) {
                    alpha = Math.min(alpha + fadeRate, 1);
                } else {
                    if (tick > displayTicks) {
                        alpha -= fadeRate;

                        if (alpha < 0) {
                            finished = true;
                            return;
                        }
                    }

                    tick++;
                }

                for (UIText t : textLines) {
                    t.setOpacity(alpha);
                }

                back.setOpacity(alpha);
            }
        });

        pack();
    }

    public GBNotification start() {
        started = true;

        return this;
    }

    public boolean isFinished() {
        return finished;
    }

    public void addDisplacementHeight(float height) {
        displaceHeight += height;
    }

    public float getDisplacementHeight() {
        return displaceHeight;
    }
}
