package emhs.db;

import emhs.db.components.*;
import emhs.db.handlers.UIMouseHandler;
import emhs.db.handlers.UITimedUpdateHandler;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class GBAdminPanel extends UIElement {
    private UIRoundRectangle header;
    private UIRoundRectangle footer;
    private UIRectangle back;

    private HashMap<String, Integer> pageIndexer;
    private String selectedPage;

    private int contentPaneHeight;
    private float footerLerp;

    private Runnable footerClickHandler;
    private String footerLabelTxt;
    private UIText footerLabel;

    public GBAdminPanel(Font headerFont, final ArrayList<GBUIPage> pages, final HashMap<String, Integer> pageIndexer, String defaultPage, HashMap<String, Color> palette) {
        final Color headerBackColor, headerTextColor, footerBtnBaseColor, footerBtnPressedColor;
        headerBackColor = palette.get("Header_Back");
        headerTextColor = palette.get("Header_Text");
        footerBtnBaseColor = palette.get("Accept_Button_Base");
        footerBtnPressedColor = palette.get("Accept_Button_Pressed");

        this.pageIndexer = pageIndexer;

        selectedPage = defaultPage;

        final int[] contentPaneWidth = {pages.get(pageIndexer.get(defaultPage)).getSize().width + 26};
        contentPaneHeight = pages.get(pageIndexer.get(defaultPage)).getSize().height + 26;

        back = (UIRectangle) new UIRectangle().setPaint(palette.get("Background")).setSize(contentPaneWidth[0], contentPaneHeight).setPos(0, 60).setRegion(ComponentRegion.TOP_MIDDLE);
        footer = (UIRoundRectangle) new UIRoundRectangle().setArcSize(10, 10).setPaint(footerBtnBaseColor).setAntiAliased(true).setSize(contentPaneWidth[0], 70).setRegion(ComponentRegion.TOP_MIDDLE).setPos(0, contentPaneHeight);
        header = (UIRoundRectangle) new UIRoundRectangle().setArcSize(10, 10).setPaint(headerBackColor).setAntiAliased(true).setSize(contentPaneWidth[0], 70).setRegion(ComponentRegion.TOP_MIDDLE);

        footerLabel = (UIText) new UIText().setFont(headerFont).setPaint(headerTextColor).setAntiAliased(true).setRegion(ComponentRegion.TOP_MIDDLE);

        final UIClipEffect clip = (UIClipEffect) new UIClipEffect().setRegion(ComponentRegion.TOP_MIDDLE).setPos(0, 60).setIncludeInPack(false);
        final UIClipEffect footerClip = (UIClipEffect) new UIClipEffect().setRegion(ComponentRegion.TOP_MIDDLE);

        Rectangle footerClipBound = new Rectangle(contentPaneWidth[0], 50);
        footerClip.setClip(footerClipBound).setActive(true).setSize(contentPaneWidth[0], 50);

        add(
                header,
                new UIText().setFont(headerFont).setText("ADMIN").setPaint(headerTextColor).setAntiAliased(true).setRegion(ComponentRegion.TOP_MIDDLE).setPos(0, 18),
                footer, footerClip, footerLabel, footerClip.genRestoreClipEffect(), back, clip
        ).pack();

        int x = 12;
        int i = 0;

        for (GBUIPage e : pages) {
            add(e.setRegion(ComponentRegion.TOP_LEFT).setIncludeInPack(false).setPos(x, 72));
            e.setActive(i == pageIndexer.get(selectedPage));
            x += e.getSize().width + 26;
            i++;
        }

        add(clip.genRestoreClipEffect());

        Rectangle clipBound = new Rectangle(5, 5, contentPaneWidth[0] - 10, contentPaneHeight - 10);
        clip.setClip(clipBound).setSize(contentPaneWidth[0], contentPaneHeight);

        clip.setActive(true);

        footer.addInteractionHandler(new UIMouseHandler(footer) {
            public void onMousePress(int mX, int mY, int button) {
                if (footerLabelTxt == null) return;
                if (footerClickHandler != null) footerClickHandler.run();

                footer.setPaint(footerBtnPressedColor);
            }

            public void onMouseRelease(int x, int y, int button) {
                footer.setPaint(footerBtnBaseColor);
            }
        });

        addTimedUpdateHandler(new UITimedUpdateHandler(this) {
            int previousSelection;
            int lastPos = 12;
            float lerp = 0;
            boolean ready = true;

            public void intervalUpdate() {
                int selected = pageIndexer.get(selectedPage);

                int diff = (previousSelection - selected);

                float iol = GBUtilities.in_out_interpolate(lerp);

                contentPaneWidth[0] = (int) GBUtilities.map(iol, 0, 1, pages.get(previousSelection).getSize().width, pages.get(selected).getSize().width) + 26;
                contentPaneHeight = (int) GBUtilities.map(iol, 0, 1, pages.get(previousSelection).getSize().height, pages.get(selected).getSize().height) + 26;

                back.setSize(contentPaneWidth[0], contentPaneHeight);
                header.setSize(contentPaneWidth[0], 70);
                footer.setSize(contentPaneWidth[0], 70).setPos(0, (int) (contentPaneHeight + 10 + 40 * footerLerp));
                footerLabel.setPos(0, (int) (contentPaneHeight + 20 + 55 * footerLerp));

                Rectangle footerClipBound = new Rectangle(contentPaneWidth[0], 50);
                footerClip.setClip(footerClipBound).setSize(contentPaneWidth[0], 50).setPos(0, contentPaneHeight + 65);

                Rectangle clipBound = new Rectangle(5, 5, contentPaneWidth[0] - 10, contentPaneHeight - 10);
                clip.setClip(clipBound).setSize(contentPaneWidth[0], contentPaneHeight);

                pack();

                if (diff != 0) {
                    diff /= Math.abs(diff);

                    if (ready) {
                        ready = false;
                    }

                    int displaceMag = 0;

                    int min = Math.min(selected, previousSelection);
                    int max = Math.max(selected, previousSelection);

                    for (int i = min; i < max; i++) {
                        displaceMag += pages.get(i).getSize().width + 26;
                    }

                    int offset = 0;

                    int i = 0;
                    for (GBUIPage p : pages) {
                        p.setPos((int) (lastPos + offset + displaceMag * diff * GBUtilities.in_out_interpolate(lerp)), 72);
                        if (i < pages.size() - 1) offset += pages.get(i).getSize().width + 26;

                        p.setActive(i == pageIndexer.get(selectedPage));

                        i++;
                    }

                    lerp = Math.min(1, Math.max(lerp += 0.0135f, 0));

                    if (lerp == 1.0) {
                        lastPos = lastPos + displaceMag * diff;
                        previousSelection = selected;
                        lerp = 0;
                    }
                } else {
                    ready = true;
                    previousSelection = selected;
                }
            }
        });

        pack();
    }

    public void showPage(String page) {
        if (pageIndexer.get(page) == null) return;
        selectedPage = page;
    }

    public void showFooter(String label) {
        footerLabelTxt = label;
        footerLabel.setText(label);

        addTimedUpdateHandler(new UITimedUpdateHandler(this) {
            public void intervalUpdate() {
                if (footerLerp == 1) {
                    removeTimedUpdateHandler(this);
                    return;
                }

                footerLerp = Math.min(1, footerLerp += 0.0135f);
            }
        });
    }

    public void hideFooter() {
        footerLabelTxt = null;

        addTimedUpdateHandler(new UITimedUpdateHandler(this) {
            public void intervalUpdate() {
                if (footerLerp == 0) {
                    removeTimedUpdateHandler(this);
                    return;
                }

                footerLerp = Math.max(footerLerp -= 0.0135f, 0);
            }
        });
    }

    public void setFooterClickHandler(Runnable r) {
        footerClickHandler = r;
    }
}
