package mandelbrotsetdemo;

import java.awt.Color;

/**
 *
 * @author HuangDiWen
 * @created 2014-1-17 14:27:12
 */
public class HSBColorScheme implements ColorScheme {

    @Override
    public int getColor(int escapeValue, int maxEscapeVal) {
        float r = (float) escapeValue / maxEscapeVal;
        return Color.HSBtoRGB(r, 0.7f, (float) (1.0 - r * r / 1.2));
    }
}
