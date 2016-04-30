package emhs.db;

import emhs.db.components.*;
import emhs.db.handlers.UIInteractionHandler;
import emhs.db.handlers.UIMouseHandler;

import java.awt.*;
import java.util.HashMap;

class GBTextField extends GBField {
    private boolean clickActive = true;
    private UIClipEffect clip;
    private InputBox input;

    public GBTextField(UIImage img, Font f, String defaultText, boolean mandatory, int width, final RequestKeyboardInterface kbRequestor, final OnScreenKeyboard kb, HashMap<String, Color> palette) {
        super(mandatory, img != null, img, width, 42, palette);
        input = (InputBox) new InputBox(palette.get("Font_Dark"), f, defaultText, fieldBack.getSize().width - 20).setPos(img != null ? 60 : 18, 0).setRegion(ComponentRegion.MIDDLE_LEFT).setIncludeInPack(false);

        fieldBound.addInteractionHandler(new UIMouseHandler(fieldBound) {
            public void onMousePress(int mX, int mY, int button) {
                if (!isActive() || !clickActive) return;
                kbRequestor.requestKeyboard(kb);
                input.setActivated(true);
            }

            public void onExternMousePress(int button) {
                if (!kb.isPressed()) input.setActivated(false);
            }
        });

        addInteractionHandler(new UIInteractionHandler(this) {
            public boolean update(AWTEvent e, String eventType) {
                if (!eventType.equals("KeyPressed")) return false;

                if (!input.isActive()) return false;

                fieldBack.setSize(fieldBack.getSize().width, input.getSize().height + 12);
                fieldBound.setSize(fieldBound.getSize().width, input.getSize().height + 12);

                Dimension size = fieldBack.getSize();
                size.width -= 15;
                size.height = 1000;

                clip.setClip(new Rectangle(size)).setActive(true);

                pack();

                return false;
            }
        });

        clip = (UIClipEffect) new UIClipEffect().setPos(img != null ? 55 : 13, 0).setRegion(ComponentRegion.TOP_LEFT);
        UIRestoreClipEffect restore = clip.genRestoreClipEffect();

        add(clip, input, restore);
    }

    public String getData() {
        return input.getText();
    }

    public void reset() {
        input.setText("");

        fieldBound.setSize(fieldBound.getSize().width, 42);
        fieldBack.setSize(fieldBound.getSize().width, 42);

        pack();
    }

    public void setClickActive(boolean clickActive) {
        this.clickActive = clickActive;
    }
}
