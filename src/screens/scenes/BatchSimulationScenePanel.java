package screens.scenes;

import Controllers.BatchSimulatorController;
import ilcompiler.input.Input.InputType;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class BatchSimulationScenePanel extends javax.swing.JPanel implements IScenePanel {

    private InputEventListener inputListener;

    private final Image backgroundImage;

    private final BatchSimulatorController controller;
    private final BatchSimulatorController.FillHeight tankFillHeightWrapper;

    private final PushButton startBt, stopBt;
    private final PushButton[] buttons;

    private final RedIndicator runLed, idleLed, fullLed;
    private final RedIndicator pump1Indicator, pump3Indicator, mixerIndicator, hiLevelIndicator, loLevelIndicator;

    private final RedIndicator[] indicators;

    public BatchSimulationScenePanel() {
        backgroundImage = new ImageIcon(getClass().getResource("/Assets/batch_bg.png")).getImage();

        controller = new BatchSimulatorController(this);
        tankFillHeightWrapper = new BatchSimulatorController.FillHeight(0);

        startBt = new PushButton("I0.0", InputType.NO);
        stopBt = new PushButton("I0.1", InputType.NC, PushButton.ButtonPalette.RED);

        buttons = new PushButton[] { startBt, stopBt };

        runLed = new RedIndicator("Q1.0", RedIndicator.IndicatorType.LED);
        idleLed = new RedIndicator("Q1.1", RedIndicator.IndicatorType.LED);
        fullLed = new RedIndicator("Q1.2", RedIndicator.IndicatorType.LED);

        pump1Indicator = new RedIndicator("Q0.1");
        mixerIndicator = new RedIndicator("Q0.2");
        pump3Indicator = new RedIndicator("Q0.3");

        hiLevelIndicator = new RedIndicator("I1.0");
        loLevelIndicator = new RedIndicator("I1.1");

        indicators = new RedIndicator[] { runLed, idleLed, fullLed, pump1Indicator, pump3Indicator, mixerIndicator,
                hiLevelIndicator, loLevelIndicator };

        initComponents();
    }

    @Override
    public void updateUIState(Map<String, InputType> inputsType, Map<String, Boolean> inputs,
            Map<String, Boolean> outputs) {
        for (PushButton button : buttons) {
            inputsType.put(button.getInputKey(), button.getType());
        }

        for (RedIndicator indicator : indicators) {
            String key = indicator.getKey();
            boolean updatedValue = false;

            if (key.startsWith("Q")) {
                updatedValue = outputs.getOrDefault(key, false);
            } else if (key.startsWith("I")) {
                updatedValue = inputs.getOrDefault(key, false);
            }

            indicator.setActive(updatedValue);
        }

        if (outputs.getOrDefault(pump1Indicator.getKey(), false)) {
            controller.startFilling(tankFillHeightWrapper);
        } else {
            controller.stopFilling();
        }

        if (outputs.getOrDefault(pump3Indicator.getKey(), false)) {
            controller.startDraining(tankFillHeightWrapper);
        } else {
            controller.stopDraining();
        }

        hiLevelIndicator.setActive(tankFillHeightWrapper.isAtHighLevel());
        loLevelIndicator.setActive(tankFillHeightWrapper.isAtLowLevel());

        inputs.put(hiLevelIndicator.getKey(), hiLevelIndicator.isActive());
        inputs.put(loLevelIndicator.getKey(), loLevelIndicator.isActive());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

        controller.drawTankFill(g2d, tankFillHeightWrapper.value);
    }

    @Override
    public void setInputListener(InputEventListener listener) {
        inputListener = listener;
        startBt.setInputEventListener(inputListener);
        stopBt.setInputEventListener(inputListener);
    }

    private void initComponents() {
        setBackground(new java.awt.Color(142, 177, 199));
        setMaximumSize(new java.awt.Dimension(624, 394));
        setMinimumSize(new java.awt.Dimension(624, 394));
        setName("");
        setPreferredSize(new java.awt.Dimension(624, 394));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 624, Short.MAX_VALUE));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 394, Short.MAX_VALUE));

        addButtonsPanel();
        addLedsPanel();
        addSensorIndicators();
    }

    private void addButtonsPanel() {
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setOpaque(false);
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        buttonsPanel.add(startBt);
        buttonsPanel.add(Box.createVerticalStrut(5));
        buttonsPanel.add(stopBt);

        setLayout(null);
        int panelWidth = 60;
        int panelHeight = 65;
        int x = 81;
        int y = getPreferredSize().height - panelHeight - 117;
        buttonsPanel.setBounds(x, y, panelWidth, panelHeight);

        this.add(buttonsPanel);
    }

    private void addLedsPanel() {
        JPanel ledsPanel = new JPanel();
        ledsPanel.setOpaque(false);
        ledsPanel.setLayout(new BoxLayout(ledsPanel, BoxLayout.Y_AXIS));

        ledsPanel.add(runLed);
        ledsPanel.add(idleLed);
        ledsPanel.add(fullLed);

        int panelWidth = 25;
        int panelHeight = 65;
        int x = 81 - 26 - panelWidth;
        int y = getPreferredSize().height - panelHeight - 100;
        ledsPanel.setBounds(x, y, panelWidth, panelHeight);

        this.add(ledsPanel);
    }

    private void addSensorIndicators() {
        pump1Indicator.setBounds(125, 45, 30, 30);
        mixerIndicator.setBounds(395, 40, 30, 30);
        pump3Indicator.setBounds(550, 378, 30, 30);

        hiLevelIndicator.setBounds(468, 60, 30, 30);
        loLevelIndicator.setBounds(490, 90, 30, 30);

        this.add(pump1Indicator);
        this.add(mixerIndicator);
        this.add(pump3Indicator);
        this.add(hiLevelIndicator);
        this.add(loLevelIndicator);
    }
}
