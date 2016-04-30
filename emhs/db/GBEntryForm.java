package emhs.db;

import emhs.db.components.*;
import emhs.db.handlers.UIMouseHandler;
import emhs.db.handlers.UITimedUpdateHandler;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class GBEntryForm extends UIElement {
    private HashMap<String, Color> palette;
    private UIElement cancelBtn;
    private UIElement saveBtn;
    private UIRoundRectangle cancelBtnBack;
    private UIRoundRectangle saveBtnBack;
    private UIRectangle btnMask;
    private UILine dividingLine;
    private ArrayList<UIRectangle> backs;
    private ArrayList<GBField> fields;
    private UIRectangle generalBack;
    private GBSelection typeSelector;

    private Runnable incompleteFormHandler;
    private Runnable saveHandler;

    private int fieldIdx = 0;
    private int height = 13;
    private int width;

    public GBEntryForm(final Font headerFont, Font dataFont, int fieldWidth, HashMap<String, Color> palette, String... types) {
        backs = new ArrayList<>();
        fields = new ArrayList<>();
        this.palette = palette;
        this.width = fieldWidth += 28;

        Color cancelBaseColor, cancelPressedColor;
        cancelBaseColor = palette.get("Reject_Button_Base");
        cancelPressedColor = palette.get("Reject_Button_Pressed");

        Color saveBaseColor, savePressedColor;
        saveBaseColor = palette.get("Accept_Button_Base");
        savePressedColor = palette.get("Accept_Button_Pressed");

        Color headerBackColor, headerTextColor;
        headerBackColor = palette.get("Header_Back");
        headerTextColor = palette.get("Header_Text");

        UIRectangle cancelClickPane;
        cancelBtn = (UIElement) new UIElement() {
        }.add(
                cancelBtnBack = (UIRoundRectangle) new UIRoundRectangle().setArcSize(10, 10).setPaint(cancelBaseColor).setAntiAliased(true).setSize(fieldWidth / 2, 70),
                new UIText().setFont(headerFont).setText("Clear").setPaint(Color.WHITE).setAntiAliased(true).setPos(0, 10),
                cancelClickPane = (UIRectangle) new UIRectangle().setVisible(false).setSize(fieldWidth / 2 - 1, 60).setPos(1, 11).setRegion(ComponentRegion.TOP_LEFT)
        ).pack().setPos(0, 390).setRegion(ComponentRegion.TOP_LEFT);

        UIRectangle saveClickPane;
        saveBtn = (UIElement) new UIElement() {
        }.add(
                saveBtnBack = (UIRoundRectangle) new UIRoundRectangle().setArcSize(10, 10).setPaint(saveBaseColor).setAntiAliased(true).setSize(fieldWidth / 2 + 13, 70),
                new UIText().setFont(headerFont).setText("Save").setPaint(headerTextColor).setAntiAliased(true).setPos(6, 10),
                saveClickPane = (UIRectangle) new UIRectangle().setVisible(false).setSize(fieldWidth / 2 - 1, 60).setPos(14, 11).setRegion(ComponentRegion.TOP_LEFT)
        ).pack().setPos(fieldWidth / 2 - 13, 390).setRegion(ComponentRegion.TOP_LEFT);

        btnMask = (UIRectangle) new UIRectangle().setPaint(cancelBaseColor).setSize(30, 70).setPos(fieldWidth / 2 - 30, 390).setRegion(ComponentRegion.TOP_LEFT);

        dividingLine = (UILine) new UILine().setStart(0, 1).setEnd(0, 59).setStroke(new BasicStroke(2)).setPaint(new Color(10, 10, 10, 75)).setPos(fieldWidth / 2, 400).setRegion(ComponentRegion.TOP_LEFT);
        generalBack = (UIRectangle) new UIRectangle().setPaint(palette.get("Background")).setSize(fieldWidth, 0).setPos(0, 60).setRegion(ComponentRegion.TOP_LEFT);

        typeSelector = new GBSelection(false, null, dataFont, palette, fieldWidth - 28, types) {
            public void reset() {
            }
        };

        addField(typeSelector, types);
        add(
                new UIRoundRectangle().setArcSize(10, 10).setPaint(headerBackColor).setAntiAliased(true).setSize(fieldWidth, 70).setRegion(ComponentRegion.TOP_LEFT),
                new UIText().setFont(headerFont).setText("NEW ENTRY").setPaint(Color.WHITE).setAntiAliased(true).setRegion(ComponentRegion.TOP_MIDDLE).setPos(0, 18),
                cancelBtn, saveBtn, btnMask, dividingLine, generalBack
        );

        final Color finalCancelPressedColor = cancelPressedColor;
        final Color finalCancelBaseColor = cancelBaseColor;
        cancelClickPane.addInteractionHandler(new UIMouseHandler(cancelClickPane) {
            public void onMousePress(int x, int y, int button) {
                cancelBtnBack.setPaint(finalCancelPressedColor);
                btnMask.setPaint(finalCancelPressedColor);
            }

            public void onMouseEnter() {
                if (isPressed()) {
                    cancelBtnBack.setPaint(finalCancelPressedColor);
                    btnMask.setPaint(finalCancelPressedColor);
                }
            }

            public void onMouseExit() {
                cancelBtnBack.setPaint(finalCancelBaseColor);
                btnMask.setPaint(finalCancelBaseColor);
            }

            public void onMouseRelease(int x, int y, int button) {
                cancelBtnBack.setPaint(finalCancelBaseColor);
                btnMask.setPaint(finalCancelBaseColor);
            }

            public void onExternMouseRelease(int button) {
                cancelBtnBack.setPaint(finalCancelBaseColor);
                btnMask.setPaint(finalCancelBaseColor);
            }

            public void onMouseClick(int x, int y, int button) {
                for (GBField f : fields) {
                    f.reset();
                }
            }
        });

        final Color finalSaveBaseColor = saveBaseColor;
        final Color finalSavePressedColor = savePressedColor;
        saveClickPane.addInteractionHandler(new UIMouseHandler(saveClickPane) {
            public void onMousePress(int x, int y, int button) {
                saveBtnBack.setPaint(finalSavePressedColor);
            }

            public void onMouseEnter() {
                if (isPressed()) saveBtnBack.setPaint(finalSavePressedColor);
            }

            public void onMouseExit() {
                saveBtnBack.setPaint(finalSaveBaseColor);
            }

            public void onMouseRelease(int x, int y, int button) {
                saveBtnBack.setPaint(finalSaveBaseColor);
            }

            public void onExternMouseRelease(int button) {
                saveBtnBack.setPaint(finalSaveBaseColor);
            }

            public void onMouseClick(int x, int y, int button) {
                for (GBField f : fields) {
                    if (f.isMandatory() && f.isActive() && f.getData().length() == 0) {
                        if (incompleteFormHandler != null) incompleteFormHandler.run();
                        return;
                    }
                }

                if (saveHandler != null) saveHandler.run();

                for (GBField f : fields) {
                    f.reset();
                }
            }
        });
    }

    public GBEntryForm addField(final GBField field, final String... activeTypes) {
        final UIRectangle back = (UIRectangle) new UIRectangle().setPaint(palette.get("Background")).setPos(0, 60).setRegion(ComponentRegion.TOP_LEFT);
        backs.add(back);
        fields.add(field);
        field.setRegion(ComponentRegion.TOP_LEFT);
        this.addTimedUpdateHandler(new UITimedUpdateHandler(field) {
            final int idx = fieldIdx;
            int percent = 0;
            int increment;
            String selectedType;

            public void intervalUpdate() {
                increment = -1;

                String type = typeSelector.getData();
                for (String s : activeTypes) {
                    if (s.equals(type)) increment = 1;
                }

                if (!type.equals(selectedType)) {
                    field.reset();
                    selectedType = type;
                }

                percent = Math.max(Math.min(percent + increment, 50), 0);

                if (idx == 0) {
                    height = 72;
                }

                float val = GBUtilities.in_out_interpolate(percent / 50.f);
                field.setActive(percent == 50);

                height -= (field.getSize().height + 13) * (1 - val);

                back.setSize(width, (int) Math.ceil(height - 53 + field.getSize().height));
                parent.setPos(12, (int) Math.ceil(height));

                height += (field.getSize().height + 13);

                generalBack.setSize(width, height - 58);
                cancelBtn.setPos(0, height - 8);
                saveBtn.setPos(width / 2 - 13, height - 8);
                btnMask.setPos(width / 2 - 30, height - 8);
                dividingLine.setPos(width / 2, height + 2);

                pack();
            }
        });

        fieldIdx++;

        return this;
    }

    public GBEntryForm finish() {
        for (int i = backs.size() - 1; i >= 0; i--) {
            add(backs.get(i), fields.get(i));
        }

        pack();

        return this;
    }

    public GBEntryForm setSaveHandler(Runnable handler) {
        saveHandler = handler;

        return this;
    }

    public GBEntryForm setIncompleteFormHandler(Runnable handler) {
        incompleteFormHandler = handler;

        return this;
    }

    public String getSelectedType() {
        return typeSelector.getData();
    }
}

