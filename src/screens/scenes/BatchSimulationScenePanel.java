package screens.scenes;

import Controllers.BatchSimulatorController;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.Timer;

public class BatchSimulationScenePanel extends javax.swing.JPanel implements IScenePanel {

    private ScenePanelInputEventListener inputListener;

    private Image backgroundImage;

    private final BatchSimulatorController controller = new BatchSimulatorController();
    private final BatchSimulatorController.IntWrapper tankFillHeightWrapper = new BatchSimulatorController.IntWrapper(0);
    private final int tankMaxHeight = 220; // ou qualquer valor correspondente

    public BatchSimulationScenePanel() {

        initComponents();

        backgroundImage = new ImageIcon(getClass().getResource("/Assets/Batch.jpg")).getImage();

        controller.startFillAnimation(this, tankFillHeightWrapper, tankMaxHeight, () -> {
            controller.startDrainAnimation(this, tankFillHeightWrapper);
        });
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
        int tankYBase = 108 + tankMaxHeight;
        int tankWidth = 284;

        // Delega o desenho do enchimento ao controller
        controller.drawTankFill(g2d, tankX, tankYBase, tankWidth, tankMaxHeight, tankFillHeightWrapper.value);

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
