package Controllers;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class BatchSimulatorController {

    private Timer fillTimer;
    private Timer drainTimer;

    private final int tankMaxHeight = 220;  // Altura máxima fixa por enquanto

    private final Map<Integer, Boolean> circleVisibility = new HashMap<>();

    public BatchSimulatorController() {
        // Inicialmente, todos os círculos estão invisíveis
        for (int i = 1; i <= 3; i++) {
            circleVisibility.put(i, false);
        }
    }

    public void drawCircles(Graphics2D g2d) {
        g2d.setColor(Color.RED);
        int circleDiameter = 16;

        Point[] circlePositions = new Point[]{
            new Point(46, 224),
            new Point(46, 242),
            new Point(46, 261)
        };

        for (int i = 0; i < circlePositions.length; i++) {
            if (circleVisibility.getOrDefault(i + 1, false)) {
                Point p = circlePositions[i];
                g2d.fillOval(p.x, p.y, circleDiameter, circleDiameter);
            }
        }
    }

    // Métodos públicos para ligar/desligar os círculos
    public void showCircle(int circleNumber, JPanel panel) {
        if (circleVisibility.containsKey(circleNumber)) {
            circleVisibility.put(circleNumber, true);
            panel.repaint();
        }
    }

    public void hideCircle(int circleNumber, JPanel panel) {
        if (circleVisibility.containsKey(circleNumber)) {
            circleVisibility.put(circleNumber, false);
            panel.repaint();
        }
    }

    // Desenhar o nível atual de preenchimento
    public void drawTankFill(Graphics2D g2d, int tankX, int tankYBase, int tankWidth, int tankFillHeight) {
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

    // Começa a encher continuamente até o usuário mandar parar ou atingir o limite
    public void startContinuousFill(JPanel panel, IntWrapper fillHeight) {
        fillTimer = new Timer(50, e -> {
            fillHeight.value += 2;
            if (fillHeight.value >= tankMaxHeight) {
                fillHeight.value = tankMaxHeight;
            }
            panel.repaint();
        });
        fillTimer.start();
    }

    public void startDrain(JPanel panel, IntWrapper fillHeight) {
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

    public void stopFilling() {
        if (fillTimer != null && fillTimer.isRunning()) {
            fillTimer.stop();
            System.out.println(">>> Parou apenas o enchimento");
        }
    }

    public void stopDraining() {
        if (drainTimer != null && drainTimer.isRunning()) {
            drainTimer.stop();
            System.out.println(">>> Parou apenas a drenagem");
        }
    }

    public void stopAll() {
        stopFilling();
        stopDraining();
    }

    // Wrapper de inteiro mutável
    public static class IntWrapper {

        public int value;

        public IntWrapper(int value) {
            this.value = value;
        }
    }
}
