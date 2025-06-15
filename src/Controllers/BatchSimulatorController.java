package Controllers;

import javax.swing.*;
import java.awt.*;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import screens.scenes.BatchSimulationScenePanel;

public class BatchSimulatorController {

    private Timer fillTimer;
    private Timer drainTimer;

    private final int tankMaxHeight = 220;

    private static final int CIRCLE_DIAMETER = 16;

    private static final Map<LedType, Point> ledPositions = Map.of(
            LedType.RUN, new Point(46, 224),
            LedType.IDLE, new Point(46, 242),
            LedType.FULL, new Point(46, 261));

    private final Map<LedType, Boolean> ledVisibility = new EnumMap<>(LedType.class);

    private static final int TANK_X = 201;
    private static final int TANK_Y_BASE = 328;
    private static final int TANK_WIDTH = 284;
    private static final int GAP_X_OFFSET = 109;
    private static final int GAP_WIDTH = 82;
    private static final int IGNORE_LIMIT = 23;

    private final Map<Integer, Boolean> circleVisibility = new HashMap<>();

    private final BatchSimulationScenePanel panel;

    public BatchSimulatorController(BatchSimulationScenePanel panel) {
        this.panel = panel;
        for (LedType led : LedType.values()) {
            ledVisibility.put(led, false);
        }
    }

    public void drawCircles(Graphics2D g2d) {
        g2d.setColor(Color.RED);

        for (LedType led : LedType.values()) {
            if (ledVisibility.getOrDefault(led, false)) {
                Point p = ledPositions.get(led);
                g2d.fillOval(p.x, p.y, CIRCLE_DIAMETER, CIRCLE_DIAMETER);
            }
        }
    }

    // ---- Métodos específicos para cada LED ----
    public void setRunLedOn() {
        ledVisibility.put(LedType.RUN, true);
        panel.repaint();
    }

    public void setRunLedOff() {
        ledVisibility.put(LedType.RUN, false);
        panel.repaint();
    }

    public void setIdleLedOn() {
        ledVisibility.put(LedType.IDLE, true);
        panel.repaint();
    }

    public void setIdleLedOff() {
        ledVisibility.put(LedType.IDLE, false);
        panel.repaint();
    }

    public void setFullLedOn() {
        ledVisibility.put(LedType.FULL, true);
        panel.repaint();
    }

    public void setFullLedOff() {
        ledVisibility.put(LedType.FULL, false);
        panel.repaint();
    }

    // Desenhar o nível atual de preenchimento
    public void drawTankFill(Graphics2D g2d, int tankFillHeight) {
        int gapX = TANK_X + GAP_X_OFFSET;
        int fillTop = TANK_Y_BASE - tankFillHeight;
        int fillHeight = tankFillHeight;

        g2d.setColor(Color.YELLOW);

        if (tankFillHeight <= IGNORE_LIMIT) {
            g2d.fillRect(TANK_X, fillTop, gapX - TANK_X, fillHeight); // esquerda
            int rightX = gapX + GAP_WIDTH;
            int rightWidth = (TANK_X + TANK_WIDTH) - rightX;
            g2d.fillRect(rightX, fillTop, rightWidth, fillHeight); // direita
        } else {
            int bottomFillHeight = IGNORE_LIMIT;
            int bottomY = TANK_Y_BASE - bottomFillHeight;
            g2d.fillRect(TANK_X, bottomY, gapX - TANK_X, bottomFillHeight); // esquerda
            int rightX = gapX + GAP_WIDTH;
            int rightWidth = (TANK_X + TANK_WIDTH) - rightX;
            g2d.fillRect(rightX, bottomY, rightWidth, bottomFillHeight); // direita

            int topFillHeight = tankFillHeight - IGNORE_LIMIT;
            int topY = fillTop;
            g2d.fillRect(TANK_X, topY, TANK_WIDTH, topFillHeight);
        }
    }

    public void startContinuousFill(IntWrapper fillHeight) {
        fillTimer = new Timer(50, e -> {
            fillHeight.value += 2;
            if (fillHeight.value >= tankMaxHeight) {
                fillHeight.value = tankMaxHeight;
            }
            panel.repaint();
        });
        fillTimer.start();
    }

    public void startDrain(IntWrapper fillHeight) {
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
            // System.out.println(">>> Parou apenas o enchimento");
        }
    }

    public void stopDraining() {
        if (drainTimer != null && drainTimer.isRunning()) {
            drainTimer.stop();
            // System.out.println(">>> Parou apenas a drenagem");
        }
    }

    public void stopAll() {
        stopFilling();
        stopDraining();
    }

    public static class IntWrapper {

        public int value;

        public IntWrapper(int value) {
            this.value = value;
        }
    }

    public enum LedType {
        RUN,
        IDLE,
        FULL
    }
}
