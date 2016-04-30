package emhs.db;

import emhs.db.components.ComponentRegion;
import emhs.db.components.UIElement;
import emhs.db.components.UIImage;
import emhs.db.components.UIRectangle;

import java.awt.*;
import java.util.HashMap;

abstract class GBField extends UIElement {
    protected HashMap<String, Color> palette;
    protected boolean active;
    protected UIRectangle iconBound;
    protected UIRectangle fieldBound;
    protected UIRectangle fieldBack;
    private boolean isMandatory;

    public GBField(boolean mandatory, boolean useImage, UIImage img, int width, int height, HashMap<String, Color> palette) {
        iconBound = (UIRectangle) new UIRectangle().setHollow(true).setStroke(new BasicStroke(2)).setPaint(palette.get("Bound")).setVisible(useImage).setSize(42, 42).setPos(1, 1).setRegion(ComponentRegion.TOP_LEFT);
        fieldBound = (UIRectangle) new UIRectangle().setHollow(true).setStroke(new BasicStroke(2)).setPaint(palette.get("Bound")).setSize(width - (useImage ? 42 : 0) + 2, height).setPos(useImage ? 43 : 1, 1).setRegion(ComponentRegion.TOP_LEFT);

        fieldBack = (UIRectangle) new UIRectangle().setStroke(new BasicStroke(2)).setPaint(palette.get("Background")).setSize(width - (useImage ? 42 : 0) + 1, height).setPos(useImage ? 43 : 1, 1).setRegion(ComponentRegion.TOP_LEFT);
        add(fieldBack, fieldBound);

        if (useImage) {
            img.setPos(2, 2).setRegion(ComponentRegion.TOP_LEFT);
            add(iconBound, img);
        }

        isMandatory = mandatory;

        pack();
    }

    public boolean isMandatory() {
        return isMandatory;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public abstract void reset();

    public String getData() {
        return "";
    }
}
