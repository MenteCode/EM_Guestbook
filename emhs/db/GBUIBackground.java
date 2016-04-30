package emhs.db;

import emhs.db.components.UIElement;
import emhs.db.components.UIImage;
import emhs.db.handlers.UIInteractionHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

class GBUIBackground extends UIElement {
    UIImage img;

    public GBUIBackground(String parentPath) throws IOException {
        img = (UIImage) new UIImage().setImage(ImageIO.read(new File(parentPath, "resource/background.png"))).setSize(2560, 1440).setIncludeInPack(false);

        addInteractionHandler(new UIInteractionHandler(this) {
            public boolean update(AWTEvent event, String s) {
                if (s.equals("WindowResized")) {
                    Component e = (Component) event.getSource();
                    setSize(e.getWidth(), e.getHeight());
                }

                return false;
            }
        });

        add(img);
        pack();
    }
}