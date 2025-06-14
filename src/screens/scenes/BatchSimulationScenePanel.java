package screens.scenes;

import Controllers.BatchSimulatorController;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.util.Map;
import javax.swing.ImageIcon;

import javax.swing.Timer;

public class BatchSimulationScenePanel extends javax.swing.JPanel implements IScenePanel {

    private ScenePanelInputEventListener inputListener;

    private Image backgroundImage;

    private final BatchSimulatorController controller = new BatchSimulatorController();
    private final BatchSimulatorController.IntWrapper tankFillHeightWrapper = new BatchSimulatorController.IntWrapper(0);

    public BatchSimulationScenePanel() {
        initComponents();
        backgroundImage = new ImageIcon(getClass().getResource("/Assets/Batch.jpg")).getImage();
        simulateBatchProcess();
        simulateCirclesSequence();
    }

    private void simulateCirclesSequence() {
        // Ativa o círculo 1 imediatamente
        controller.showCircle(1, this);
        System.out.println(">>> Círculo 1 ativado");

        // Timer para ativar círculo 2 após 5 segundos
        Timer timer2 = new Timer(5000, e2 -> {
            controller.showCircle(2, this);
            System.out.println(">>> Círculo 2 ativado");
        });
        timer2.setRepeats(false);
        timer2.start();

        // Timer para ativar círculo 3 após 10 segundos (5s depois do círculo 2)
        Timer timer3 = new Timer(10000, e3 -> {
            controller.showCircle(3, this);
            System.out.println(">>> Círculo 3 ativado");
        });
        timer3.setRepeats(false);
        timer3.start();

        // Timer para apagar círculo 1 após 15 segundos
        Timer timerHide1 = new Timer(15000, e4 -> {
            controller.hideCircle(1, this);
            System.out.println(">>> Círculo 1 apagado");
        });
        timerHide1.setRepeats(false);
        timerHide1.start();

        // Timer para apagar círculo 3 após 20 segundos
        Timer timerHide3 = new Timer(20000, e5 -> {
            controller.hideCircle(3, this);
            System.out.println(">>> Círculo 3 apagado");
        });
        timerHide3.setRepeats(false);
        timerHide3.start();

        // Timer para apagar círculo 2 após 25 segundos
        Timer timerHide2 = new Timer(25000, e6 -> {
            controller.hideCircle(2, this);
            System.out.println(">>> Círculo 2 apagado");
        });
        timerHide2.setRepeats(false);
        timerHide2.start();

        // Timer para ligar o círculo 3 novamente após 30 segundos
        Timer timerShow3Again = new Timer(30000, e7 -> {
            controller.showCircle(3, this);
            System.out.println(">>> Círculo 3 reativado");
        });
        timerShow3Again.setRepeats(false);
        timerShow3Again.start();
    }

    private void simulateBatchProcess() {
        // 1 - Começar enchimento
        controller.startContinuousFill(this, tankFillHeightWrapper);

        // 2 - Após 3 segundos, parar o enchimento
        new Timer(10000, e -> {
            controller.stopFilling();
            System.out.println(">>> Parou o enchimento após 3s");

            // 3 - Após mais 5 segundos, iniciar drenagem
            Timer startDrainTimer = new Timer(5000, evt -> {
                System.out.println(">>> Iniciou drenagem após 8s totais");
                controller.startDrain(this, tankFillHeightWrapper);
            });
            startDrainTimer.setRepeats(false);   // IMPORTANTE: O Timer de 5s deve disparar só uma vez
            startDrainTimer.start();

        }).start();
    }

    @Override
    public void updateUIState(Map<String, Integer> inputsType, Map<String, Boolean> inputs, Map<String, Boolean> outputs) {

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

        // Parâmetros do tanque
        int tankX = 201;
        int tankYBase = 328;
        int tankWidth = 284;

        // Delega o desenho do enchimento ao controller
        controller.drawTankFill(g2d, tankX, tankYBase, tankWidth, tankFillHeightWrapper.value);

        g2d.setColor(Color.RED);

        int circleDiameter = 16;  // Tamanho dos círculos

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
