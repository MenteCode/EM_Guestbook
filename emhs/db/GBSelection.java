package emhs.db;

import emhs.db.components.*;
import emhs.db.handlers.UIMouseHandler;
import emhs.db.handlers.UITimedUpdateHandler;
import emhs.db.util.UIUtilities;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

class GBSelection extends GBField {
    private ArrayList<BKSelectionButton> buttons;
    private UIRectangle selectionBox;
    private int previousSelection;
    private int selectedButton;
    private String[] options;
    private boolean enabled;
    private int selections;

    public GBSelection(final boolean useIcon, UIImage img, Font f, HashMap<String, Color> palette, int width, String... types) {
        super(false, useIcon, img, width, 42, palette);

        options = types;

        buttons = new ArrayList<>();
        final int fieldWidth = useIcon ? width - 42 : width;

        selections = types.length;
        if (selections == 0) return;

        final int defWidth = (int) Math.ceil(((float) fieldWidth + selections - 1) / selections);
        selectionBox = (UIRectangle) new UIRectangle().setPaint(palette.get("Selection")).setSize(fieldWidth / selections, 40).setPos((useIcon ? 43 : 1), 2).setRegion(ComponentRegion.TOP_LEFT);
        add(selectionBox);

        fieldBack.addInteractionHandler(new UIMouseHandler(fieldBack) {
            public void onMousePress(int x, int y, int button) {
                if(!enabled || !active) return;
                Point mouse = UIUtilities.toRelativeCoords(x - (useIcon ? 44 : 2), y, GBSelection.this);
                selectedButton = (int) Math.min(Math.max(mouse.x / (float) defWidth, 0), selections - 1);
            }
        });

        for (int i = 0; i < selections - 1; i++) {
            final int finalI = i;

            BKSelectionButton btn = (BKSelectionButton) new BKSelectionButton(f, types[i], defWidth, palette).setPos(defWidth * i + (useIcon ? 43 : 1), 1).setRegion(ComponentRegion.TOP_LEFT);

            btn.addInteractionHandler(new UIMouseHandler(btn) {
                final int idx = finalI;

                public void onMousePress(int mX, int mY, int button) {
                    if (!enabled || !active) return;
                    selectedButton = idx;
                }
            });

            buttons.add(btn);
            add(btn);
        }

        BKSelectionButton btn = (BKSelectionButton) new BKSelectionButton(f, types[selections - 1], defWidth, palette).setPos(defWidth * (selections - 1) + (useIcon ? 43 : 1), 1).setRegion(ComponentRegion.TOP_LEFT);

        btn.addInteractionHandler(new UIMouseHandler(btn) {
            final int idx = selections - 1;

            public void onMousePress(int mX, int mY, int button) {
                if (!enabled || !active) return;
                selectedButton = idx;
            }
        });

        buttons.add(btn);
        add(btn);
        pack();

        buttons.get(0).updateFontBrightness(1);

        addTimedUpdateHandler(new UITimedUpdateHandler(this) {
            float lerp = 0;
            boolean ready;

            public void intervalUpdate() {
                float diff = (selectedButton - previousSelection);

                if (diff != 0) {
                    enabled = false;
                    if (ready) {
                        ready = false;
                    }

                    float val = GBUtilities.in_out_interpolate(lerp, 3);
                    lerp = Math.min(1, Math.max(lerp += 0.02f, 0));

                    selectionBox.setPos((int) (previousSelection * defWidth + val * diff * defWidth + (useIcon ? 44 : 2)), selectionBox.getLocalPos().y);

                    for (int i = 0; i < buttons.size(); i++) {
                        buttons.get(i).updateFontBrightness(1 - Math.max(Math.min(Math.abs(i - (previousSelection + (selectedButton - previousSelection) * lerp)), 1), 0));
                    }

                    if (lerp == 1.0) {
                        previousSelection = selectedButton;
                        lerp = 0;
                        enabled = true;
                    }
                } else {
                    enabled = true;
                    ready = true;
                    previousSelection = selectedButton;
                }
            }
        });

        pack();
    }

    public String getData() {
        return options[selectedButton];
    }

    public void reset() {
    }

    class BKSelectionButton extends UIElement {
        private UIRectangle buttonBound;
        private UIText text;
        private Color darkFontColor;
        private Color lightFontColor;

        public BKSelectionButton(Font f, String text, int width, HashMap<String, Color> palette) {
            this.text = (UIText) new UIText().setFont(f).setText(text).setPaint(darkFontColor = palette.get("Font_Dark")).setAntiAliased(true);
            buttonBound = (UIRectangle) new UIRectangle().setHollow(true).setStroke(new BasicStroke(2)).setPaint(palette.get("Bound")).setSize(width, 42);
            lightFontColor = palette.get("Font_Light");

            add(buttonBound, this.text);
            pack();
        }

        public void updateFontBrightness(float val) {
            this.text.setPaint(GBUtilities.lerpColor(darkFontColor, lightFontColor, GBUtilities.in_out_interpolate(val)));
        }
    }
}
