package Controllers;

import javax.swing.*;
import java.awt.*;

public class BatchSimulatorController {

    private Timer fillTimer;
    private Timer drainTimer;

    public void drawTankFill(Graphics2D g2d, int tankX, int tankYBase, int tankWidth, int tankMaxHeight, int tankFillHeight) {
        int gapX = tankX + 109;
        int gapWidth = 82;
        int fillTop = tankYBase - tankFillHeight;
        int fillHeight = tankFillHeight;
        int ignoreLimit = 23;

        g2d.setColor(Color.YELLOW);

        if (tankFillHeight <= ignoreLimit) {
            g2d.fillRect(tankX, fillTop, gapX - tankX, fillHeight); // esquerda
            int rightX = gapX + gapWidth;
            int rightWidth = (tankX + tankWidth) - rightX;
            g2d.fillRect(rightX, fillTop, rightWidth, fillHeight); // direita
        } else {
            int bottomFillHeight = ignoreLimit;
            int bottomY = tankYBase - bottomFillHeight;
            g2d.fillRect(tankX, bottomY, gapX - tankX, bottomFillHeight); // esquerda
            int rightX = gapX + gapWidth;
            int rightWidth = (tankX + tankWidth) - rightX;
            g2d.fillRect(rightX, bottomY, rightWidth, bottomFillHeight); // direita

            int topFillHeight = tankFillHeight - ignoreLimit;
            int topY = fillTop;
            g2d.fillRect(tankX, topY, tankWidth, topFillHeight);
        }
    }

    public void startFillAnimation(JPanel panel, IntWrapper fillHeight, int maxHeight, Runnable onComplete) {
        fillTimer = new Timer(50, e -> {
            fillHeight.value += 2;
            if (fillHeight.value >= maxHeight) {
                fillHeight.value = maxHeight;
                fillTimer.stop();

                // Aguarda 2s e chama o esvaziamento
                Timer waitTimer = new Timer(2000, evt -> onComplete.run());
                waitTimer.setRepeats(false);
                waitTimer.start();
            }
            panel.repaint();
        });
        fillTimer.start();
    }

    public void startDrainAnimation(JPanel panel, IntWrapper fillHeight) {
        drainTimer = new Timer(50, e -> {
            fillHeight.value -= 2;
            if (fillHeight.value <= 0) {
                fillHeight.value = 0;
                drainTimer.stop();
            }
            panel.repaint();
        });
        drainTimer.start();
    }

    // Classe simples para encapsular um inteiro mutável (por referência)
    public static class IntWrapper {

        public int value;

        public IntWrapper(int value) {
            this.value = value;
        }
    }
}
