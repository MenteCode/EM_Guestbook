package emhs.db;

import emhs.db.components.ComponentRegion;
import emhs.db.components.UIElement;
import emhs.db.components.UIRoundRectangle;
import emhs.db.components.UIText;
import emhs.db.handlers.UIMouseHandler;
import emhs.db.handlers.UITimedUpdateHandler;
import emhs.db.util.Pointer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;

public class OnScreenKeyboard extends UIElement {
    static Component dummy = new JPanel();
    private String[] layout = {"1234567890-", "QWERTYUIOP", "ASDFGHJKL'", "`sZXCVBNM,.`d", "^^^^^ "};
    private Pointer<Integer> modField;
    private UIRoundRectangle back;
    private int keySize, spacing;
    private Font font;

    UIMouseHandler touchListener;
    HashMap<String, Color> palette;

    public OnScreenKeyboard(Font f, int keySize, int spacing, HashMap<String, Color> palette) {
        Color backColor = palette.get("Keyboard_Back");

        back = (UIRoundRectangle) new UIRoundRectangle().setPaint(backColor).setAntiAliased(true);
        modField = new Pointer<>(0);
        font = f;
        this.keySize = keySize;
        this.spacing = spacing;
        this.palette = palette;

        touchListener = new UIMouseHandler(this) {
            public void onMousePress(int x, int y, int button) {
                consumeEvent();
            }
        };

        addInteractionHandler(touchListener);

        applyLayout();
    }

    public void applyLayout() {
        UIElement[] rows = new UIElement[layout.length];

        for (int y = 0; y < layout.length; y++) {
            String row = layout[y];
            int xPos = 0;
            int width = keySize;

            rows[y] = (UIElement) new UIElement() {
            }.setRegion(ComponentRegion.TOP_MIDDLE);

            for (int x = 0; x < row.length(); x++) {
                KeyButton key = null;
                char letter = row.charAt(x);
                if (letter == '`') {
                    width += (keySize + spacing) / 2;
                    continue;
                } else if (letter == '^') {
                    width += keySize + spacing;
                    continue;
                } else {
                    if (!Character.isUpperCase(letter)) {
                        switch (letter) {
                            case 's':
                                key = new KeyButton("Shift", width, keySize, font, false, null);
                                key.addInteractionHandler(new UIMouseHandler(key) {
                                    public void onMousePress(int mX, int mY, int button) {
                                        modField.value ^= InputEvent.SHIFT_MASK;
                                        consumeEvent();
                                    }
                                });

                                final KeyButton finalKey = key;
                                key.addTimedUpdateHandler(new UITimedUpdateHandler(finalKey) {
                                    public void intervalUpdate() {
                                        if ((modField.value & InputEvent.SHIFT_MASK) == InputEvent.SHIFT_MASK)
                                            finalKey.opacity = 255;
                                    }
                                });

                                break;
                            case 'd':
                                key = new KeyButton("Delete", width, keySize, font, false, null);
                                final KeyButton finalKey1 = key;
                                key.addInteractionHandler(new UIMouseHandler(finalKey1) {
                                    public void onMousePress(int mX, int mY, int button) {
                                        int mods = 0;
                                        if (modField != null) {
                                            mods = modField.value;
                                            modField.value = 0;
                                        }

                                        consumeEvent();

                                        finalKey1.lastEvent = new KeyEvent(dummy, KeyEvent.KEY_PRESSED, 1, mods, KeyEvent.VK_BACK_SPACE, KeyEvent.CHAR_UNDEFINED);
                                        injectEvent(finalKey1.lastEvent);
                                    }
                                });

                                break;
                        }

                        if (key == null)
                            key = new KeyButton(letter + "", width, keySize, font, true, modField);
                    } else {
                        key = new KeyButton(letter + "", width, keySize, font, true, modField);
                    }

                    key.setPos(xPos, 0).setRegion(ComponentRegion.TOP_LEFT);

                    rows[y].add(key);
                }

                xPos += width + spacing;
                width = keySize;
            }

            rows[y].setPos(width != keySize ? keySize - width : 0, y * (keySize + spacing) + spacing);
            rows[y].pack();
        }

        clear();
        add(back.setArcSize(7, 7).setIncludeInPack(false));
        add(rows);
        pack();

        back.setSize(size.width + spacing * 2, size.height + spacing * 2);
        back.setIncludeInPack(true);
        pack();
    }

    public OnScreenKeyboard setLayout(String... layout) {
        this.layout = layout;
        applyLayout();

        return this;
    }

    public boolean isPressed() {
        return touchListener.isPressed();
    }

    class KeyButton extends UIElement {
        UIRoundRectangle overlay, back;
        AWTEvent lastEvent;
        UIText character;
        int opacity = 0;

        public KeyButton(final String character, int width, int height, Font f, boolean useDefaultHandler, final Pointer<Integer> modField) {
            Color overlayColor, backColor;

            backColor = palette.get("Key_Button_Back");
            overlayColor = palette.get("Key_Button_Overlay");

            overlay = (UIRoundRectangle) new UIRoundRectangle()
                    .setArcSize(3, 3)
                    .setPaint(new Color(0, 0, 0, 0))
                    .setAntiAliased(true)
                    .setSize(width, height);

            back = (UIRoundRectangle) new UIRoundRectangle()
                    .setArcSize(3, 3)
                    .setPaint(backColor)
                    .setAntiAliased(true)
                    .setSize(width - 4, height - 4);

            this.character = (UIText) new UIText().setFont(f).setText(character + "").setAntiAliased(true);

            if (useDefaultHandler) {
                addInteractionHandler(new UIMouseHandler(this) {
                    public void onMousePress(int x, int y, int button) {
                        int mods = 0;
                        if (modField != null) {
                            mods = modField.value;
                            modField.value &= ~InputEvent.SHIFT_MASK;
                        }

                        consumeEvent();

                        char ch = Character.toLowerCase(character.charAt(0));
                        lastEvent = new KeyEvent(dummy, KeyEvent.KEY_PRESSED, 1, mods, ch, ch);
                        injectEvent(lastEvent);
                    }
                });
            }

            final boolean[] buttonHeld = {false};

            final UIMouseHandler inHandler = new UIMouseHandler(this) {
                public void onMouseExit() {
                    buttonHeld[0] = false;
                }

                public void onMousePress(int x, int y, int button) {
                    opacity = 255;
                    consumeEvent();
                }

                public void onMouseHold(int x, int y, int button) {
                    buttonHeld[0] = true;
                    consumeEvent();
                }

                public void onMouseRelease(int x, int y, int button) {
                    buttonHeld[0] = false;
                    lastEvent = null;
                }

                public void onExternMouseRelease(int button) {
                    buttonHeld[0] = false;
                    lastEvent = null;
                }
            }.setHoldDelay(400);

            addInteractionHandler(inHandler);

            final Color finalBackColor = backColor;
            final Color finalOverlayColor = overlayColor;
            addTimedUpdateHandler(new UITimedUpdateHandler(this) {
                long timer = 0;
                int mod = 15;

                public void intervalUpdate() {
                    back.setPaint(new Color(finalBackColor.getRGB() & 0xffffff | (255 - opacity) / 2 << 24, true));
                    overlay.setPaint(new Color(finalOverlayColor.getRGB() & 0xffffff | opacity << 24, true));

                    if (!inHandler.isPressed() || buttonHeld[0]) opacity = Math.max(opacity - 12, 0);
                    if (buttonHeld[0]) {
                        if (timer % mod == 0) {
                            if (lastEvent != null) {
                                injectEvent(lastEvent);
                                mod = Math.max(mod - 1, 3);
                                timer = 0;
                            }
                        }

                        if (timer % 20 == 0) opacity = 255;

                        timer++;
                    } else {
                        mod = 15;
                        timer = 0;
                    }
                }
            });

            add(overlay, back, this.character);
            pack();
        }
    }
}
