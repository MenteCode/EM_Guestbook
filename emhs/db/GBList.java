package emhs.db;

import emhs.db.components.*;
import emhs.db.handlers.UIMouseHandler;
import emhs.db.handlers.UITimedUpdateHandler;
import emhs.db.util.AppWindow;
import emhs.db.util.Pointer;
import emhs.db.util.UIUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class GBList extends GBField {
    private Color fontDark;
    private Color fontLight;
    private CopyOnWriteArrayList<GBListEntry> entries;
    private UIRoundRectangle scrollBar;
    private UIRectangle selectionBox;
    private UIElement entryContainer;
    private UIElement timeContainer;
    private int selectionIdx;
    private int selectionMax;
    private int lineHeight;
    private int spacing;
    private Font font;

    private float velocity;
    private float scroll;
    private float swipeMouseY;
    private boolean swipeActive;

    private Runnable entryClickHandler;
    private Pointer<Long> clickIndex;

    public GBList(int width, int visibleEntries, final int spacing, Font font, HashMap<String, Color> palette) {
        super(false, false, null, width, 42, palette);

        fontDark = palette.get("Font_Dark");
        fontLight = palette.get("Font_Light");

        entries = new CopyOnWriteArrayList<>();
        this.spacing = spacing;
        this.font = font;
        selectionIdx = -1;
        lineHeight = AppWindow.getFontDimensions(font, " ").height;

        final int height = (lineHeight + spacing) * visibleEntries + (spacing / 2);

        fieldBack.setSize(width + 1, height);
        fieldBound.setSize(width + 2, height);

        entryContainer = (UIElement) new UIElement() {
        }.setSize(width, 0);

        timeContainer = (UIElement) new UIElement() {
        }.setSize(width, 0);

        selectionBox = (UIRectangle) new UIRectangle().setPaint(palette.get("Selection")).setSize(width, lineHeight + spacing).setPos(2, 1 - (lineHeight + spacing)).setRegion(ComponentRegion.TOP_LEFT).setIncludeInPack(false);

        scrollBar = (UIRoundRectangle) new UIRoundRectangle().setArcSize(2, 2).setAntiAliased(true).setPaint(new Color(20, 20, 20, 50)).setSize(25, height / 3).setRegion(ComponentRegion.TOP_RIGHT);

        setActive(true);

        final Pointer<Boolean> scrollBarPressed = new Pointer<>(false);

        fieldBack.addInteractionHandler(new UIMouseHandler(fieldBack) {
            boolean scrollBarActive;
            boolean selecting;
            int gestureTailY;
            int pressY;
            int offset;

            public void onMousePress(int x, int y, int button) {
                gestureTailY = y;
                pressY = y;
                velocity = 0;
                offset = (int) scroll;
                swipeMouseY = scroll;
                swipeActive = true;
                selecting = true;
            }

            public void onMouseDrag(final int x, final int y, int button) {
                if (scrollBarPressed.value) {
                    scrollBarActive = true;
                    return;
                }

                if (Math.abs(pressY - y) > 10) selecting = false;

                swipeMouseY = offset - (y - pressY);

                Timer t = new Timer(100, new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        gestureTailY = y;
                    }
                });

                t.setRepeats(false);
                t.start();
            }

            public void onMouseHold(int x, int y, int button) {
                gestureTailY = y;
            }

            public void onMouseRelease(int x, int y, int button) {
                swipeActive = false;

                if (scrollBarActive) {
                    scrollBarActive = false;
                    return;
                }

                velocity = (y - gestureTailY) / 10.f;

                if (selecting) {
                    Point press = UIUtilities.toRelativeCoords(x, y, parent);
                    int pressIdx = (int) ((press.y + scroll) / (lineHeight + spacing));
                    if (pressIdx >= selectionMax) return;
                    if (!isActive()) return;

                    if (selectionIdx == pressIdx) {
                        if (clickIndex != null) clickIndex.value = entries.get(pressIdx).ID;
                        if (entryClickHandler != null) entryClickHandler.run();
                    }

                    selectionIdx = pressIdx;
                }
            }
        }.setHoldDelay(100));

        final Pointer<Float> scrollBarPressLoc = new Pointer<>(0.f);

        scrollBar.addInteractionHandler(new UIMouseHandler(scrollBar) {
            public void onMousePress(int mX, int mY, int button) {
                if (!isActive() && scrollBar.getLocalPos().x > 10) return;
                scrollBarPressLoc.value = GBUtilities.map(Math.min(Math.max(UIUtilities.toRelativeCoords(0, mY, fieldBack).y - (height >> 3), 0), height - (height / 3)), 0, height - (height / 3), 0, entries.size() * (lineHeight + spacing) - fieldBack.getSize().height);
                scrollBarPressed.value = true;
                consumeEvent();
            }

            public void onMouseDrag(int mX, final int mY, int button) {
                if (!isActive() && scrollBar.getLocalPos().x > 10) return;
                onMousePress(mX, mY, button);
                consumeEvent();
            }

            public void onMouseRelease(int mX, int mY, int button) {
                if (!isActive() && scrollBar.getLocalPos().x > 10) return;
                scrollBarPressed.value = false;
                consumeEvent();
            }
        });

        addTimedUpdateHandler(new UITimedUpdateHandler(this) {
            public void intervalUpdate() {
                if (scrollBarPressed.value) {
                    scroll += (scrollBarPressLoc.value - scroll) * 0.15f;
                } else if (!swipeActive) {
                    scroll -= velocity;
                    velocity *= 0.95f;
                } else {
                    scroll += (swipeMouseY - scroll) * 0.15f;
                }

                for (GBListEntry p : entries) {
                    p.name.setPos(12, (int) (p.position - scroll));
                    p.time.setPos(-6, (int) (p.position - scroll));
                }

                if (swipeActive) return;

                if (scroll < 0) {
                    scroll -= scroll / 6.f;
                }

                if (scroll > Math.max(entries.size() * (lineHeight + spacing) - fieldBack.getSize().height, 0)) {
                    scroll -= (scroll - Math.max(entries.size() * (lineHeight + spacing) - fieldBack.getSize().height, 0)) / 6.f;
                }
            }
        });

        selectionBox.addTimedUpdateHandler(new UITimedUpdateHandler(selectionBox) {
            int previousSelection = -1;
            float lerp = 0;

            public void intervalUpdate() {
                int diff = (previousSelection - selectionIdx);

                if (diff != 0) {
                    setActive(false);

                    selectionBox.setPos(2, (int) -((-previousSelection + GBUtilities.in_out_interpolate(lerp) * diff) * (lineHeight + spacing) + (selectionIdx > -1 ? scroll : 0) + 1));

                    for (int i = 0; i < entries.size(); i++) {
                        entries.get(i).name.setPaint(GBUtilities.lerpColor(fontDark, fontLight, 1 - Math.max(Math.min(Math.abs(i - (previousSelection + (selectionIdx - previousSelection) * GBUtilities.in_out_interpolate(lerp))), 1), 0)));
                        entries.get(i).time.setPaint(GBUtilities.lerpColor(fontDark, fontLight, 1 - Math.max(Math.min(Math.abs(i - (previousSelection + (selectionIdx - previousSelection) * GBUtilities.in_out_interpolate(lerp))), 1), 0)));
                    }

                    lerp = Math.min(1, Math.max(lerp += 0.02f, 0));

                    if (lerp == 1.0) {
                        previousSelection = selectionIdx;
                        lerp = 0;
                        setActive(true);
                    }
                } else {
                    previousSelection = selectionIdx;

                    selectionBox.setPos(2, (int) (selectionIdx * (lineHeight + spacing) - (selectionIdx > -1 ? scroll : 0) + 1));
                }
            }
        });

        scrollBar.addTimedUpdateHandler(new UITimedUpdateHandler(scrollBar) {
            int tick = 20;

            public void intervalUpdate() {
                scrollBar.setPos((int) (GBUtilities.in_out_interpolate(tick / 20.f) * 25), (int) GBUtilities.map(scroll, 0, entries.size() * (lineHeight + spacing) - fieldBack.getSize().height, 0, height - (height / 3)));

                if (entries.size() * (lineHeight + spacing) < fieldBack.getSize().height) tick = Math.min(tick + 1, 20);
                else tick = Math.max(tick - 1, 0);
            }
        });

        Rectangle clipRect = new Rectangle(fieldBack.getPos(), fieldBack.getSize());

        clipRect.x++;
        clipRect.y++;
        clipRect.width--;
        clipRect.height -= 2;

        UIClipEffect clip = (UIClipEffect) new UIClipEffect().setClip(clipRect).setRegion(ComponentRegion.TOP_LEFT);
        UIRestoreClipEffect restore = clip.genRestoreClipEffect();

        clipRect = new Rectangle(clipRect);

        clipRect.width -= new UIText().setFont(font).setText("88:88").getSize().width + 24; //88:88 is the widest (although impractical) time in a non-monospaced font. Extra width does no harm, anyway.

        UIClipEffect entryClip = (UIClipEffect) new UIClipEffect().setClip(clipRect).setRegion(ComponentRegion.TOP_LEFT);
        UIRestoreClipEffect entryRestore = entryClip.genRestoreClipEffect();

        clip.setActive(true);
        entryClip.setActive(true);

        add(clip, selectionBox, entryClip, entryContainer.setRegion(ComponentRegion.TOP_LEFT), entryRestore, timeContainer.setRegion(ComponentRegion.TOP_LEFT), scrollBar, restore);

        pack();
    }

    public void setEntryClickHandler(Runnable r, Pointer<Long> index) {
        entryClickHandler = r;
        clickIndex = index;
    }

    public void addEntry(String entry, String t, long ID) {
        UIText text = (UIText) new UIText().setFont(font).setText(entry).setPaint(fontDark).setAntiAliased(true).setPos(12, entries.size() * (lineHeight + spacing) + spacing / 2).setRegion(ComponentRegion.TOP_LEFT);
        UIText time = (UIText) new UIText().setFont(font).setText(t).setPaint(fontDark).setAntiAliased(true).setPos(-6, entries.size() * (lineHeight + spacing) + spacing / 2).setRegion(ComponentRegion.TOP_RIGHT);
        entries.add(selectionMax, new GBListEntry(text, time, entries.size() * (lineHeight + spacing) + spacing / 2, ID));
        entryContainer.add(text);
        timeContainer.add(time);
        selectionMax++;
    }

    public Long removeSelected() {
        if (!isActive()) return null;
        if (selectionIdx == -1) return null;

        for (int i = selectionIdx + 1; i < entries.size(); i++) {
            final int finalI = i;
            entries.get(i).name.addTimedUpdateHandler(new UITimedUpdateHandler(entries.get(i).name) {
                float lerp = 0;

                public void intervalUpdate() {
                    lerp = Math.min(1, Math.max(lerp += 0.02f, 0));

                    entries.get(finalI - 1).position = (int) ((finalI - GBUtilities.in_out_interpolate(lerp)) * (lineHeight + spacing) + spacing / 2);
                    if (lerp >= 1) parent.removeTimedUpdateHandler(this);
                }
            });
        }

        GBListEntry entry = entries.remove(selectionIdx);
        entryContainer.remove(entry.name);
        timeContainer.remove(entry.time);

        selectionIdx--;
        selectionMax--;

        return entry.ID;
    }

    public void reset() {
    }

    public class GBListEntry {
        public UIText name;
        public UIText time;
        public int position;
        public long ID;

        public GBListEntry(UIText name, UIText time, int position, long id) {
            this.name = name;
            this.time = time;
            this.position = position;
            ID = id;
        }
    }
}
