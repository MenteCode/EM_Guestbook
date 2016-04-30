package emhs.db;

import emhs.db.components.UIClosedShape;
import emhs.db.components.UIComponent;
import emhs.db.util.RenderProcedure;
import emhs.db.util.UIFace;

import java.awt.*;
import java.awt.geom.AffineTransform;

class DBLogo extends UIClosedShape {
    static {
        UIFace.addRenderProcedure(DBLogo.class, new RenderProcedure() {
            public boolean setup(Graphics2D g, UIComponent component) {
                return UIFace.getRenderProcedure(UIClosedShape.class).setup(g, component);
            }

            public void draw(Graphics2D g, UIComponent component) {
                DBLogo logo = (DBLogo) component;

                Point pos = logo.getPos();
                Dimension size = logo.getSize();

                AffineTransform transform = new AffineTransform(size.width / 90.f, 0, 0, size.height / 90.f, pos.x, pos.y);

                Polygon d1 = new Polygon(
                        new int[]{0, 30, 26, 10, 10, 22, 26, 0},
                        new int[]{0, 0, 10, 10, 80, 80, 90, 90},
                        8);

                Polygon d2 = new Polygon(
                        new int[]{30, 40, 40, 26, 22, 30, 30, 26},
                        new int[]{0, 10, 76, 90, 80, 72, 14, 10},
                        8
                );

                Polygon b1 = new Polygon(
                        new int[]{50, 60, 60, 76, 80, 90, 80, 40, 50},
                        new int[]{20, 30, 80, 80, 76, 80, 90, 90, 80},
                        9
                );

                Polygon b2 = new Polygon(
                        new int[]{60, 80, 90, 90, 80, 80, 76, 60},
                        new int[]{50, 50, 60, 80, 76, 66, 60, 60},
                        8
                );

                AffineTransform oldTransform = g.getTransform();
                g.setTransform(transform);

                g.fillPolygon(d1);
                g.fillPolygon(d2);
                g.fillPolygon(b1);
                g.fillPolygon(b2);

                g.setTransform(oldTransform);
            }
        });
    }

    public DBLogo() {
        super(DBLogo.class);
        setSize(90, 90);
    }
}
