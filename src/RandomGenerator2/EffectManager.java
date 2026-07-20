package RandomGenerator2;

import javax.swing.*;
import java.awt.*;

import static RandomGenerator2.RT2.resultAreaDefaultFontSize;

public class EffectManager {
    private static Timer currentTimer;
    private static Font baseFont = new Font("微软雅黑", Font.BOLD, resultAreaDefaultFontSize);
    private static final String[] SYSTEM_FONTS = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

    public static void stopCurrentEffect(JTextArea targetArea,Font toFont) {
        if (currentTimer != null && currentTimer.isRunning()) {
            currentTimer.stop();
        }
        if (targetArea != null) {
            targetArea.setFont(toFont);
        }
    }

    public static void startBlinkEffect(JTextArea targetArea, int intervalMs) {
        baseFont = targetArea.getFont();
        stopCurrentEffect(targetArea,baseFont);
        currentTimer = new Timer(intervalMs, _ -> {
            int r = (int) (Math.random() * 255);
            int g = (int) (Math.random() * 255);
            int b = (int) (Math.random() * 255);
            targetArea.setForeground(new Color(r, g, b));
        });
        currentTimer.start();
    }

    public static void startBreathSizeEffect(JTextArea targetArea, int minSize, int maxSize) {
        baseFont = targetArea.getFont();
        stopCurrentEffect(targetArea,baseFont);

        final int[] frame = {0};
        int centerSize = (minSize + maxSize) / 2;
        int amplitude = (maxSize - minSize) / 2;

        currentTimer = new Timer(40, _ -> {
            frame[0]++;
            double scale = Math.sin(frame[0] * 0.1);
            int currentSize = (int) (centerSize + scale * amplitude);

            targetArea.setFont(new Font(baseFont.getName(), baseFont.getStyle(), currentSize));
        });
        currentTimer.start();
    }

    public static void applyStaticColorAndSize(JTextArea targetArea, Color staticColor, int targetSize) {
        baseFont = targetArea.getFont();
        stopCurrentEffect(targetArea,baseFont);

        targetArea.setForeground(staticColor);
        targetArea.setFont(new Font(baseFont.getName(), baseFont.getStyle(), targetSize));
    }


    public static void startRandomFontEffect(JTextArea targetArea, int intervalMs) {
        baseFont = targetArea.getFont();
        stopCurrentEffect(targetArea, baseFont);
        if (SYSTEM_FONTS == null || SYSTEM_FONTS.length == 0) {
            return;
        }
        currentTimer = new Timer(intervalMs, _ -> {
            int randomIndex = (int) (Math.random() * SYSTEM_FONTS.length);
            String randomFontName = SYSTEM_FONTS[randomIndex];
            targetArea.setFont(new Font(randomFontName, baseFont.getStyle(), baseFont.getSize()));
        });
        currentTimer.start();
    }
}