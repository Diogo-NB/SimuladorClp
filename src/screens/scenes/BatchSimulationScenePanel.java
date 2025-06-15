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
import javax.swing.Timer;

public class BatchSimulationScenePanel extends javax.swing.JPanel implements IScenePanel {

    private InputEventListener inputListener;

    private final Image backgroundImage;

    private final BatchSimulatorController controller;
    private final BatchSimulatorController.IntWrapper tankFillHeightWrapper;

    private final PushButton startBt, stopBt;
    private final PushButton[] buttons;

    private final RedIndicator runLed, idleLed, fullLed;
    private final RedIndicator pump1Indicator, pump3Indicator, mixerIndicator, hiLevelIndicator, loLevelIndicator;

    private final RedIndicator[] indicators;

    public BatchSimulationScenePanel() {
        backgroundImage = new ImageIcon(getClass().getResource("/Assets/batch_bg.png")).getImage();

        controller = new BatchSimulatorController(this);
        tankFillHeightWrapper = new BatchSimulatorController.IntWrapper(0);

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

        // simulateBatchProcess();
        // simulateCirclesSequence();
    }

    private void simulateCirclesSequence() {
        // Liga o LED Run imediatamente
        controller.setRunLedOn();
        // System.out.println(">>> LED Run ativado");

        // Após 5 segundos, liga o LED Idle
        Timer timerIdleOn = new Timer(5000, e -> {
            controller.setIdleLedOn();
            // System.out.println(">>> LED Idle ativado");
        });
        timerIdleOn.setRepeats(false);
        timerIdleOn.start();

        // Após 10 segundos, liga o LED Full
        Timer timerFullOn = new Timer(10000, e -> {
            controller.setFullLedOn();
            // System.out.println(">>> LED Full ativado");
        });
        timerFullOn.setRepeats(false);
        timerFullOn.start();

        // Após 15 segundos, desliga o LED Run
        Timer timerRunOff = new Timer(15000, e -> {
            controller.setRunLedOff();
            // System.out.println(">>> LED Run apagado");
        });
        timerRunOff.setRepeats(false);
        timerRunOff.start();

        // Após 20 segundos, desliga o LED Full
        Timer timerFullOff = new Timer(20000, e -> {
            controller.setFullLedOff();
            // System.out.println(">>> LED Full apagado");
        });
        timerFullOff.setRepeats(false);
        timerFullOff.start();

        // Após 25 segundos, desliga o LED Idle
        Timer timerIdleOff = new Timer(25000, e -> {
            controller.setIdleLedOff();
            // System.out.println(">>> LED Idle apagado");
        });
        timerIdleOff.setRepeats(false);
        timerIdleOff.start();

        // Após 30 segundos, liga o LED Full novamente
        Timer timerFullOnAgain = new Timer(30000, e -> {
            controller.setFullLedOn();
            // System.out.println(">>> LED Full reativado");
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
            // System.out.println(">>> Parou o enchimento após 10s");

            // 3 - Após mais 5 segundos, iniciar drenagem
            Timer startDrainTimer = new Timer(5000, evt -> {
                // System.out.println(">>> Iniciou drenagem após 15s totais");
                controller.startDrain(tankFillHeightWrapper);
            });
            startDrainTimer.setRepeats(false); // Dispara só uma vez
            startDrainTimer.start();

        });
        stopFillTimer.setRepeats(false); // Dispara só uma vez
        stopFillTimer.start();
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
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

        controller.drawTankFill(g2d, tankFillHeightWrapper.value);

        controller.drawCircles(g2d);
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
