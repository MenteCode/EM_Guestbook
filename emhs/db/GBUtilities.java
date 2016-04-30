package emhs.db;

import java.awt.*;

public class GBUtilities {
    public static float in_out_interpolate(float x) {
        return in_out_interpolate(x, 2.5f);
    }

    public static float in_out_interpolate(float x, float ease) {
        float p = (float) Math.pow(x, ease);
        return (float) (p / (p + Math.pow(1 - x, ease)));
    }

    public static float in_interpolate(float x, float ease) {
        return (float) Math.pow(x, ease);
    }

    public static Color lerpColor(Color c1, Color c2, float p) {
        int c1i = c1.getRGB(), c2i = c2.getRGB();
        int i = (int) Math.pow(c1i >> 24 & 0xFF, 2);
        int a = (int) Math.sqrt(i + (Math.pow(c2i >> 24 & 0xFF, 2) - i) * p);
        i = (int) Math.pow(c1i >> 16 & 0xFF, 2);
        int r = (int) Math.sqrt(i + (Math.pow(c2i >> 16 & 0xFF, 2) - i) * p);
        i = (int) Math.pow(c1i >> 8 & 0xFF, 2);
        int g = (int) Math.sqrt(i + (Math.pow(c2i >> 8 & 0xFF, 2) - i) * p);
        i = (int) Math.pow(c1i & 0xFF, 2);
        int b = (int) Math.sqrt(i + (Math.pow(c2i & 0xFF, 2) - i) * p);

        return new Color(r, g, b, a);
    }

    public static float map(float value, float inRangeStart, float inRangeEnd, float outRangeStart, float outRangeEnd) {
        return outRangeStart + (value - inRangeStart)/(inRangeEnd - inRangeStart) * (outRangeEnd - outRangeStart);
    }
}
