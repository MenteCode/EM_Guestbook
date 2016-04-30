package emhs.db;

import emhs.db.components.ComponentRegion;
import emhs.db.components.UIElement;
import emhs.db.handlers.UITimedUpdateHandler;
import emhs.db.util.Pointer;

import java.util.ArrayList;

public class GBAdminPage extends GBUIPage {
    private ArrayList<GBField> fields;
    private int fieldIdx = 0;
    private int height = 0;

    public GBAdminPage() {
        fields = new ArrayList<>();
    }

    public GBAdminPage addField(final GBField field) {
        return addField(field, new Pointer<>(1.f));
    }

    public GBAdminPage addField(final GBField field, final Pointer<Float> lerp) {
        fields.add(field);
        field.setRegion(ComponentRegion.TOP_LEFT);

        UITimedUpdateHandler handler = new UITimedUpdateHandler(field) {
            final int thisIdx = fieldIdx;

            public void intervalUpdate() {
                if (thisIdx == 0) height = 0;

                height -= (field.getSize().height + 14) * (1 - lerp.value);
                field.setPos(0, height);
                height += (field.getSize().height + 14);

                pack();
            }
        };

        handler.intervalUpdate();
        this.addTimedUpdateHandler(handler);

        fieldIdx++;

        return this;
    }

    public GBAdminPage finish() {
        for (int i = fields.size() - 1; i >= 0; i--) {
            add(fields.get(i));
        }

        pack();

        return this;
    }

    public GBAdminPage setActive(boolean active) {
        for (GBField f : fields) {
            f.setActive(active);
        }

        return this;
    }
}