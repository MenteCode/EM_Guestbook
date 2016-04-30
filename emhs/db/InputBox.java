package emhs.db;

import emhs.db.components.*;
import emhs.db.handlers.UIInteractionHandler;
import emhs.db.handlers.UIMouseHandler;
import emhs.db.handlers.UITimedUpdateHandler;
import emhs.db.util.AppWindow;
import emhs.db.util.FontData;
import emhs.db.util.UIUtilities;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class InputBox extends UIElement {
    private UIElement textContainer;
    private ArrayList<UIText> text;
    private UIText noInputText;

    private StringBuilder textData;
    private boolean caretVisible;
    private Color inactiveColor;
    private boolean activated;
    private UIRectangle back;
    private FontData fDims;
    private int maxWidth;
    private int caretPos;
    private UILine caret;
    private Font font;

    public InputBox(Color inactiveColor, final Font f, String noInputText, int maxWidth) {
        text = new ArrayList<>();
        this.noInputText = (UIText) new UIText().setFont(f).setText(noInputText).setPaint(inactiveColor).setAntiAliased(true).setRegion(ComponentRegion.MIDDLE_LEFT).setIncludeInPack(false);
        int txtHeight = this.noInputText.getSize().height;
        back = (UIRectangle) new UIRectangle().setPaint(new Color(255, 255, 255, 150)).setVisible(false).setSize(50, 50).setRegion(ComponentRegion.TOP_LEFT);
        caret = (UILine) new UILine().setEnd(0, txtHeight).setVisible(false).setRegion(ComponentRegion.TOP_LEFT);
        fDims = AppWindow.getFontDimensions(f, " ");
        this.inactiveColor = inactiveColor;
        textData = new StringBuilder();
        this.maxWidth = maxWidth;
        font = f;

        textContainer = (UIElement) new UIElement() {
        }.setRegion(ComponentRegion.TOP_LEFT);

        add(back, this.noInputText, textContainer, caret);
        pack();

        final int buttonPressed[] = new int[1];

        addInteractionHandler(new UIMouseHandler(this) {
            public void onMousePress(int mX, int mY, int button) {
                if (buttonPressed[0] == 0) buttonPressed[0] = button;

                if (button == 1) {
                    if (textData.length() > 0) {
                        reposCaret(mX + AppWindow.getFontDimensions(f, textData.charAt(Math.max(caretPos - 1, 0)) + "").width / 2, mY);
                    }
                }
            }

            public void onMouseRelease(int mX, int mY, int button) {
                buttonPressed[0] = 0;
            }

            public void onMouseDrag(int mX, int mY, int button) {
                if (buttonPressed[0] == 1) {
                    if (textData.length() > 0) {
                        reposCaret(mX + AppWindow.getFontDimensions(f, textData.charAt(Math.max(caretPos - 1, 0)) + "").width / 2, mY);
                    }
                }
            }

            public void reposCaret(int mX, int mY) {
                Point localClick = UIUtilities.toRelativeCoords(mX, mY, InputBox.this);
                int previous = caretPos;

                int caretX;
                int caretY = text.size() - 1;

                while (caretY > 0 && (fDims.height + 5) * caretY > localClick.y) {
                    caretY--;
                }

                caretX = text.get(caretY).getText().length();

                while (AppWindow.getFontDimensions(f, text.get(caretY).getText().substring(0, caretX)).width - 1 > localClick.x && caretX != 0)
                    caretX--;

                caretPos = 0;
                for (int i = 0; i < caretY; i++) {
                    caretPos += text.get(i).getText().length() + 1;
                }

                caretPos += caretX;

                if (previous != caretPos) reconfigUI();
            }
        }.setHoldDelay(400));

        final long[] lastEvent = {System.currentTimeMillis()};

        addInteractionHandler(new UIInteractionHandler(this) {
            public boolean update(AWTEvent e, String eventType) {
                if (!activated) return false;

                lastEvent[0] = System.currentTimeMillis();

                KeyEvent ke;

                switch (eventType) {
                    case "KeyPressed":
                        ke = (KeyEvent) e;
                        switch (ke.getKeyCode()) {
                            case KeyEvent.VK_BACK_SPACE:
                                textData.delete(Math.max(caretPos - 1, 0), caretPos);
                                caretPos = Math.max(--caretPos, 0);
                                break;
                            case KeyEvent.VK_DELETE:
                                textData.delete(caretPos, caretPos + 1);
                                break;
                            default:
                                int ch = ke.getKeyChar();
                                if (ch < 32 || ch >= 127) return false;
                                if (ke.isShiftDown())
                                    ch = Character.toUpperCase(ch);
                                textData.insert(caretPos, (char) ch);
                                caretPos++;
                                break;
                        }

                        reconfigUI();
                }

                return false;
            }
        });

        addTimedUpdateHandler(new UITimedUpdateHandler(this) {
            public void intervalUpdate() {
                long now = System.currentTimeMillis();
                switch ((int) (now - lastEvent[0]) / 500 % 2) {
                    case 0:
                        if (activated) {
                            caret.setVisible(true);
                            caretVisible = true;
                        }
                        break;
                    case 1:
                        caret.setVisible(false);
                        caretVisible = false;
                        break;
                }
            }
        });

        reconfigUI();
    }

    public void setActivated(boolean activated) {
        if (this.activated == activated) return;

        this.activated = activated;
        reconfigUI();
    }

    public String getText() {
        return textData.toString();
    }

    public InputBox setText(String text) {
        this.textData = new StringBuilder(text);
        caretPos = 0;
        reconfigUI();

        return this;
    }

    public void setInactiveColor(Color inactiveColor) {
        this.inactiveColor = inactiveColor;
    }

    public void reconfigUI() {
        int height = 0;
        int maxLineWidth = 0;

        int wordStartIdx = 0;
        int wordEndIdx;
        int i = 0;

        String msg = textData.toString();
        msg = msg + " ";

        text = new ArrayList<>();

        while (wordStartIdx < msg.length()) {
            String line = "";

            String word;
            boolean noWordsUsed = true;
            while (wordStartIdx < msg.length() && (AppWindow.getFontDimensions(font, line + (word = msg.substring(wordStartIdx, (wordEndIdx = msg.indexOf(' ', wordStartIdx))))).width < maxWidth || noWordsUsed)) {
                line += " " + word;
                wordStartIdx = wordEndIdx + 1;
                noWordsUsed = false;
            }

            text.add((UIText) new UIText().setFont(font).setText(line.substring(1)).setAntiAliased(true).setPos(0, (fDims.height + 5) * i + 5).setRegion(ComponentRegion.TOP_LEFT));

            maxLineWidth = Math.max(maxLineWidth, text.get(i).getSize().width);

            height += fDims.height + 5;
            i++;
        }

        textContainer.clear();
        textContainer.add(text.toArray(new UIText[text.size()]));
        textContainer.pack();

        if (activated) {
            for (UIText t : text) t.setPaint(Color.BLACK);

            back.setVisible(true);
            if (caretVisible) caret.setVisible(true);
            else caret.setVisible(false);
            noInputText.setVisible(false);

            int caretX;
            int lineLengthSum = 0;

            for (i = 0; i < text.size() - 1; i++) {
                UIText t = text.get(i);
                if (caretPos - (lineLengthSum + t.getText().length()) <= 0) break;
                lineLengthSum += t.getText().length() + 1;
            }

            caretX = caretPos - lineLengthSum;

            caret.setPos(AppWindow.getFontDimensions(font, text.get(i).getText().substring(0, Math.max(caretX, 0))).width - 1, (fDims.height + 5) * i + 5);
        } else {
            if (textData.length() == 0) noInputText.setVisible(true);
            else noInputText.setVisible(false);

            back.setVisible(false);
            for (UIText t : text) t.setPaint(inactiveColor);

            caret.setVisible(false);
            caretPos = 0;
        }

        back.setSize(maxLineWidth + 10, height + 5).setPos(-5, 0);

        pack();

        renderRequested = true;
    }

    public boolean isActive() {
        return activated;
    }
}
