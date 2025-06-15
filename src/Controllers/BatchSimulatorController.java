package Controllers;

import javax.swing.*;
import java.awt.*;
import screens.scenes.BatchSimulationScenePanel;

public class BatchSimulatorController {

    private Timer fillTimer;
    private Timer drainTimer;

    private static final int TANK_X = 178;
    private static final int TANK_Y_BASE = 330;
    private static final int TANK_WIDTH = 321;

    private final BatchSimulationScenePanel panel;

    public BatchSimulatorController(BatchSimulationScenePanel panel) {
        this.panel = panel;
    }

    // Desenhar o nível atual de preenchimento sem gap
    public void drawTankFill(Graphics2D g2d, int tankFillHeight) {
        int fillTop = TANK_Y_BASE - tankFillHeight;
        int fillHeight = tankFillHeight;

        g2d.setColor(new Color(255, 255, 0, 200));

        g2d.fillRect(TANK_X, fillTop, TANK_WIDTH, fillHeight);
    }

    public void startFilling(FillHeight fillHeight) {
        fillTimer = new Timer(50, e -> {
            fillHeight.value += 2;
            if (fillHeight.value >= FillHeight.MAX_VALUE) {
                fillHeight.value = FillHeight.MAX_VALUE;
            }
            panel.repaint();
        });
        fillTimer.start();
    }

    public void startDraining(FillHeight fillHeight) {
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

    public static class FillHeight {

        public static final int MAX_VALUE = 220;
        public int value;

        public FillHeight(int value) {
            this.value = value;
        }

        public boolean isAtHighLevel() {
            return value >= MAX_VALUE;
        }

        public boolean isAtLowLevel() {
            return value >= 180;
        }
    }

    public enum LedType {
        RUN,
        IDLE,
        FULL
    }
}
