package screens.scenes;

import Controllers.BatchSimulatorController;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.Map;
import javax.swing.ImageIcon;

import javax.swing.Timer;

public class BatchSimulationScenePanel extends javax.swing.JPanel implements IScenePanel {

    private ScenePanelInputEventListener inputListener;

    private Image backgroundImage;

    private final BatchSimulatorController controller;
    private final BatchSimulatorController.IntWrapper tankFillHeightWrapper = new BatchSimulatorController.IntWrapper(0);

    public BatchSimulationScenePanel() {
        backgroundImage = new ImageIcon(getClass().getResource("/Assets/batch_bg.png")).getImage();
        controller = new BatchSimulatorController(this);
        
        initComponents();
        
        simulateBatchProcess();
        simulateCirclesSequence();
    }

    private void simulateCirclesSequence() {
        // Liga o LED Run imediatamente
        controller.setRunLedOn();
        System.out.println(">>> LED Run ativado");

        // Após 5 segundos, liga o LED Idle
        Timer timerIdleOn = new Timer(5000, e -> {
            controller.setIdleLedOn();
            System.out.println(">>> LED Idle ativado");
        });
        timerIdleOn.setRepeats(false);
        timerIdleOn.start();

        // Após 10 segundos, liga o LED Full
        Timer timerFullOn = new Timer(10000, e -> {
            controller.setFullLedOn();
            System.out.println(">>> LED Full ativado");
        });
        timerFullOn.setRepeats(false);
        timerFullOn.start();

        // Após 15 segundos, desliga o LED Run
        Timer timerRunOff = new Timer(15000, e -> {
            controller.setRunLedOff();
            System.out.println(">>> LED Run apagado");
        });
        timerRunOff.setRepeats(false);
        timerRunOff.start();

        // Após 20 segundos, desliga o LED Full
        Timer timerFullOff = new Timer(20000, e -> {
            controller.setFullLedOff();
            System.out.println(">>> LED Full apagado");
        });
        timerFullOff.setRepeats(false);
        timerFullOff.start();

        // Após 25 segundos, desliga o LED Idle
        Timer timerIdleOff = new Timer(25000, e -> {
            controller.setIdleLedOff();
            System.out.println(">>> LED Idle apagado");
        });
        timerIdleOff.setRepeats(false);
        timerIdleOff.start();

        // Após 30 segundos, liga o LED Full novamente
        Timer timerFullOnAgain = new Timer(30000, e -> {
            controller.setFullLedOn();
            System.out.println(">>> LED Full reativado");
        });
        timerFullOnAgain.setRepeats(false);
        timerFullOnAgain.start();
    }

    private void simulateBatchProcess() {
        // 1 - Começar enchimento
        controller.startContinuousFill(tankFillHeightWrapper);

        // 2 - Após 10 segundos, parar o enchimento
        Timer stopFillTimer = new Timer(10000, e -> {
            controller.stopFilling();
            System.out.println(">>> Parou o enchimento após 10s");

            // 3 - Após mais 5 segundos, iniciar drenagem
            Timer startDrainTimer = new Timer(5000, evt -> {
                System.out.println(">>> Iniciou drenagem após 15s totais");
                controller.startDrain(tankFillHeightWrapper);
            });
            startDrainTimer.setRepeats(false);   // Dispara só uma vez
            startDrainTimer.start();

        });
        stopFillTimer.setRepeats(false);   // Dispara só uma vez
        stopFillTimer.start();
    }

    @Override
    public void updateUIState(Map<String, Integer> inputsType, Map<String, Boolean> inputs, Map<String, Boolean> outputs) {
        
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

        // Delega o desenho do enchimento ao controller
        controller.drawTankFill(g2d, tankFillHeightWrapper.value);

        controller.drawCircles(g2d);
    }

    @Override
    public void setInputListener(ScenePanelInputEventListener listener) {
        inputListener = listener;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(new java.awt.Color(142, 177, 199));
        setMaximumSize(new java.awt.Dimension(624, 394));
        setMinimumSize(new java.awt.Dimension(624, 394));
        setName(""); // NOI18N
        setPreferredSize(new java.awt.Dimension(624, 394));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 624, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 394, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
