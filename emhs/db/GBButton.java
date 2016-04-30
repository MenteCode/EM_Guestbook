package emhs.db;

import emhs.db.components.UIImage;
import emhs.db.components.UIText;
import emhs.db.handlers.UIMouseHandler;
import emhs.db.handlers.UITimedUpdateHandler;

import java.awt.*;
import java.util.HashMap;

public class GBButton extends GBField {
    private Color buttonBase;
    private Color buttonPressed;
    private Color fontDark;
    private Color fontLight;
    private boolean pressed;
    private Runnable task;
    private float lerp;

    public GBButton(String text, Font font, UIImage img, int width, int height, HashMap<String, Color> palette) {
        super(false, img != null, img, width, height, palette);

        buttonBase = palette.get("Background");
        buttonPressed = palette.get("Selection");
        fontDark = palette.get("Font_Dark");
        fontLight = palette.get("Font_Light");

        fieldBack.addInteractionHandler(new UIMouseHandler(this) {
            public void onMousePress(int mX, int mY, int button) {
                if (!isActive()) return;
                pressed = true;
                lerp = 1;
                if (task == null) return;
                task.run();
            }

            public void onExternMouseRelease(int button) {
                if (!isActive()) return;
                pressed = false;
            }

            public void onMouseClick(int mX, int mY, int button) {
                if (!isActive()) return;
                pressed = false;
            }
        });

        setActive(true);

        final UIText uiText;
        add(uiText = (UIText) new UIText().setFont(font).setText(text).setAntiAliased(true));

        addTimedUpdateHandler(new UITimedUpdateHandler(this) {
            public void intervalUpdate() {
                fieldBack.setPaint(GBUtilities.lerpColor(buttonBase, buttonPressed, lerp));
                uiText.setPaint(GBUtilities.lerpColor(fontDark, fontLight, lerp));

                lerp = Math.max(lerp - 0.05f, 0);
            }
        });
    }

    public GBButton setTask(Runnable task) {
        this.task = task;

        return this;
    }

    public boolean isPressed() {
        return pressed;
    }

    public void reset() {
    }
}
