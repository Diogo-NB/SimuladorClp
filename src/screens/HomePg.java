package screens;

import screens.scenes.IScenePanel;
import screens.scenes.ScenePanelInputEventListener;
import screens.scenes.BatchSimulationScenePanel;
import screens.scenes.DefaultScenePanel;
import Controllers.HomePageController;
import Models.ExecutionMode;
import Models.HomePageModel;
import ilcompiler.edit.Colors;
import ilcompiler.edit.Language;
import javax.swing.ImageIcon;
import ilcompiler.input.InputActions;
import ilcompiler.memoryvariable.MemoryVariable;
import ilcompiler.output.OutputActions;
import ilcompiler.uppercasedocumentfilter.UpperCaseDocumentFilter;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import screens.scenes.ScenesEnum;
import static screens.scenes.ScenesEnum.BATCH_SIMULATION;
import static screens.scenes.ScenesEnum.DEFAULT;

public final class HomePg extends javax.swing.JFrame {

    private final HomePageController controller;

    ListaDeVariaveisPg tela2 = new ListaDeVariaveisPg();
    private JTextArea Lista_de_variaveis = null;
    private boolean updating = false;

    private IScenePanel currentScenePanel;
    private ScenesEnum currentScene = ScenesEnum.DEFAULT;
    private ScenePanelInputEventListener sceneInputEventListener;

    @SuppressWarnings("unchecked")
    public HomePg() {
        controller = new HomePageController(this);

        initComponents();
        Lista_de_variaveis = tela2.getListaDeVariaveis();

        ImageIcon iconCampo = new ImageIcon(getClass().getResource("/Assets/bloco_notas.png"));
        iconCampo.setImage(iconCampo.getImage().getScaledInstance(Codigo_Camp.getWidth(), Codigo_Camp.getHeight(), 1));
        Image_Camp.setIcon(iconCampo);

        Codigo_Camp.setOpaque(false);

        // Carrega e redimensiona o ícone do temporizador
        ImageIcon iconTimer = new ImageIcon(getClass().getResource("/Assets/temporizador.png"));
        Image imgTimer = iconTimer.getImage().getScaledInstance(Timer_1.getWidth(), Timer_1.getHeight(),
                java.awt.Image.SCALE_SMOOTH);
        iconTimer.setImage(imgTimer);

        // Aplica o ícone a todos os temporizadores
        JLabel[] timers = {Timer_1, Timer_2, Timer_3, Timer_4, Timer_5, Timer_6, Timer_7, Timer_8, Timer_9, Timer_10};
        for (JLabel timer : timers) {
            timer.setIcon(iconTimer);
        }

        // Carrega e redimensiona o ícone do contador
        ImageIcon iconCont = new ImageIcon(getClass().getResource("/Assets/contador.png"));
        Image imgCont = iconCont.getImage().getScaledInstance(Contador_1.getWidth(), Contador_1.getHeight(),
                java.awt.Image.SCALE_SMOOTH);
        iconCont.setImage(imgCont);

        // Aplica o ícone a todos os contadores
        JLabel[] contadores = {Contador_1, Contador_2, Contador_3, Contador_4, Contador_5, Contador_6, Contador_7,
            Contador_8, Contador_9, Contador_10};
        for (JLabel contador : contadores) {
            contador.setIcon(iconCont);
        }

        AbstractDocument doc = (AbstractDocument) Codigo_Camp.getDocument();
        doc.setDocumentFilter(new UpperCaseDocumentFilter());

        HomePageModel.setInputsType(InputActions.createType(new HashMap<>()));
        HomePageModel.setInputs(InputActions.create(new HashMap<>()));
        System.out.println("HashMap de entradas criado:" + HomePageModel.getInputs());
        HomePageModel.setOutputs(OutputActions.create(HomePageModel.getOutputs()));
        System.out.println("HashMap de saídas criado:" + HomePageModel.getOutputs());

        sceneInputEventListener = new ScenePanelInputEventListener() {
            @Override
            public void onPressed(String inputKey, MouseEvent evt) {
                handleInputButtonPressed(inputKey, evt);
            }

            @Override
            public void onReleased(String inputKey, MouseEvent evt) {
                handleInputButtonReleased(inputKey, evt);
            }
        };

        currentScenePanel = new DefaultScenePanel();
        currentScenePanel.setInputListener(sceneInputEventListener);
        sceneContainer.setLayout(new BorderLayout());
        sceneContainer.add((JPanel) currentScenePanel, BorderLayout.CENTER);
        sceneContainer.revalidate();
        sceneContainer.repaint();

        this.setResizable(false);

        pack();

        // Atualiza entradas e saídas na tela
        updateSceneUI();
    }

    private void handleInputButtonPressed(String inputKey, java.awt.event.MouseEvent evt) {
        if (evt.getButton() == java.awt.event.MouseEvent.BUTTON1) {
            switch (HomePageModel.getInputsType().getOrDefault(inputKey, 0)) {
                case 0 ->
                    HomePageModel.getInputs().put(inputKey, !HomePageModel.getInputs().get(inputKey));
                case 1 ->
                    HomePageModel.getInputs().put(inputKey, true);
                case 2 ->
                    HomePageModel.getInputs().put(inputKey, false);
            }
            updateSceneUI();
        } else if (evt.getButton() == java.awt.event.MouseEvent.BUTTON3) {
            int val = HomePageModel.getInputsType().get(inputKey) + 1;
            if (val >= 3) {
                val = 0;
            }
            HomePageModel.getInputsType().put(inputKey, val);
            HomePageModel.getInputs().put(inputKey, (val == 2));
            updateSceneUI();
        }
    }

    private void handleInputButtonReleased(String key, java.awt.event.MouseEvent evt) {
        if (evt.getButton() == java.awt.event.MouseEvent.BUTTON1) {
            int type = HomePageModel.getInputsType().get(key);
            if (type == 1) {
                HomePageModel.getInputs().put(key, false);
            } else if (type == 2) {
                HomePageModel.getInputs().put(key, true);
            }
            updateSceneUI();
        }
    }

    public void setColor(Boolean value, JLabel label) {
        if (value) {
            label.setForeground(Color.green);
        } else {
            label.setForeground(Color.red);
        }
    }

    private void setCurrentScene(ScenesEnum scene) {
        if (scene == null || scene == currentScene) {
            return;
        }

        currentScene = scene;

        if (currentScenePanel != null) {
            sceneContainer.remove((JPanel) currentScenePanel);
        }

        switch (scene) {
            case DEFAULT ->
                currentScenePanel = new DefaultScenePanel();
            case BATCH_SIMULATION ->
                currentScenePanel = new BatchSimulationScenePanel();
        }

        var currentSceneJPanel = (JPanel) currentScenePanel;

        currentScenePanel.setInputListener(sceneInputEventListener);

        sceneContainer.add(currentSceneJPanel, BorderLayout.CENTER);

        currentSceneJPanel.revalidate();
        currentSceneJPanel.repaint();

        sceneContainer.revalidate();
        sceneContainer.repaint();
        
        SwingUtilities.invokeLater(() -> controller.handleRefreshAction());
    }

    public void updateSceneUI() {
        currentScenePanel.updateUIState(
                HomePageModel.getInputsType(),
                HomePageModel.getInputs(),
                HomePageModel.getOutputs());
    }

    public void updateMode() {
        System.out.println("Modo atual: " + HomePageModel.getMode());

        boolean isRunningMode = HomePageModel.getMode() == ExecutionMode.RUNNING;
        refreshBt.setEnabled(!isRunningMode);
        simulationsComboBox.setEnabled(!isRunningMode);

        if (HomePageModel.getMode() == null) {
            Codigo_Camp.setEditable(false);
            ImageIcon icon1 = new ImageIcon(getClass().getResource("/Assets/start_green.png"));
            startBt.setIcon(icon1);
        } else {
            switch (HomePageModel.getMode()) {
                case IDLE -> {
                    Codigo_Camp.setEditable(true);
                    ImageIcon icon = new ImageIcon(getClass().getResource("/Assets/start.png"));
                    startBt.setIcon(icon);
                }
                case STOPPED -> {
                    Codigo_Camp.setEditable(false);
                    ImageIcon icon = new ImageIcon(getClass().getResource("/Assets/start.png"));
                    startBt.setIcon(icon);
                }
                case RUNNING -> {
                    Codigo_Camp.setEditable(false);
                    ImageIcon icon = new ImageIcon(getClass().getResource("/Assets/start_green.png"));
                    startBt.setIcon(icon);
                }
            }
        }
    }

    public void updateMemoryVariables() {
        Lista_de_variaveis.setText("");

        String line = "";
        List<MemoryVariable> tVariables = new ArrayList<>();
        List<MemoryVariable> cVariables = new ArrayList<>();

        for (Map.Entry<String, MemoryVariable> variable : HomePageModel.getMemoryVariables().entrySet()) {
            switch (variable.getKey().charAt(0)) {
                case 'M' -> {
                    line = variable.getKey() + " = " + variable.getValue().currentValue + "\n";
                }
                case 'T' -> {
                    line = variable.getKey() + " = " + variable.getValue().currentValue + ", "
                            + variable.getValue().counter + ", " + variable.getValue().maxTimer + ", "
                            + variable.getValue().endTimer + "\n";
                    if (tVariables.size() < 10) {
                        tVariables.add(variable.getValue());
                    }
                }
                case 'C' -> {
                    line = variable.getKey() + " = " + variable.getValue().counter + ", " + variable.getValue().maxTimer
                            + ", " + variable.getValue().endTimer + "\n";
                    if (cVariables.size() < 10) {
                        cVariables.add(variable.getValue());
                    }
                }
            }

            Lista_de_variaveis.setText(Lista_de_variaveis.getText() + line);
        }

        HomePageController.updateTimerLabels(tVariables,
                Arrays.asList(Timer_1, Timer_2, Timer_3, Timer_4, Timer_5, Timer_6, Timer_7, Timer_8, Timer_9,
                        Timer_10),
                Arrays.asList(Temp_atual_1, Temp_atual_2, Temp_atual_3, Temp_atual_4, Temp_atual_5, Temp_atual_6,
                        Temp_atual_7, Temp_atual_8, Temp_atual_9, Temp_atual_10),
                Arrays.asList(Temp_parada_1, Temp_parada_2, Temp_parada_3, Temp_parada_4, Temp_parada_5, Temp_parada_6,
                        Temp_parada_7, Temp_parada_8, Temp_parada_9, Temp_parada_10));

        HomePageController.updateCounterLabels(cVariables,
                Arrays.asList(Contador_1, Contador_2, Contador_3, Contador_4, Contador_5, Contador_6, Contador_7,
                        Contador_8, Contador_9, Contador_10),
                Arrays.asList(Contagem_atual_1, Contagem_atual_2, Contagem_atual_3, Contagem_atual_4, Contagem_atual_5,
                        Contagem_atual_6, Contagem_atual_7, Contagem_atual_8, Contagem_atual_9, Contagem_atual_10),
                Arrays.asList(Contagem_parada_1, Contagem_parada_2, Contagem_parada_3, Contagem_parada_4,
                        Contagem_parada_5, Contagem_parada_6, Contagem_parada_7, Contagem_parada_8, Contagem_parada_9,
                        Contagem_parada_10));
    }

    public static void showErrorMessage(String message) {
        HomePageModel.setMode(ExecutionMode.IDLE);
        JOptionPane.showMessageDialog(null, message);
    }

    public List<String> saveLines(List<String> lineList) {
        int quant = Codigo_Camp.getLineCount();

        for (int i = 0; i < quant; i++) {
            try {
                Integer start = Codigo_Camp.getLineStartOffset(i);
                Integer end = Codigo_Camp.getLineEndOffset(i);
                String line = Codigo_Camp.getText(start, end - start);
                lineList.add(line);
            } catch (BadLocationException ex) {
                Logger.getLogger(HomePg.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        System.out.println("Lista de linhas: " + lineList);
        return lineList;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenu1 = new javax.swing.JMenu();
        Arquivar_BT = new javax.swing.JComboBox<>();
        Editar_BT = new javax.swing.JComboBox<>();
        Sobre_BT = new javax.swing.JButton();
        sceneContainer = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        Temp_parada_1 = new javax.swing.JLabel();
        Temp_parada_2 = new javax.swing.JLabel();
        Temp_parada_3 = new javax.swing.JLabel();
        Temp_parada_4 = new javax.swing.JLabel();
        Temp_parada_5 = new javax.swing.JLabel();
        Temp_parada_6 = new javax.swing.JLabel();
        Temp_parada_7 = new javax.swing.JLabel();
        Temp_parada_8 = new javax.swing.JLabel();
        Temp_parada_9 = new javax.swing.JLabel();
        Temp_parada_10 = new javax.swing.JLabel();
        Contagem_parada_1 = new javax.swing.JLabel();
        Contagem_parada_2 = new javax.swing.JLabel();
        Contagem_parada_3 = new javax.swing.JLabel();
        Contagem_parada_4 = new javax.swing.JLabel();
        Contagem_parada_5 = new javax.swing.JLabel();
        Contagem_parada_6 = new javax.swing.JLabel();
        Contagem_parada_7 = new javax.swing.JLabel();
        Contagem_parada_8 = new javax.swing.JLabel();
        Contagem_parada_9 = new javax.swing.JLabel();
        Contagem_parada_10 = new javax.swing.JLabel();
        Temp_atual_1 = new javax.swing.JLabel();
        Temp_atual_2 = new javax.swing.JLabel();
        Temp_atual_3 = new javax.swing.JLabel();
        Temp_atual_4 = new javax.swing.JLabel();
        Temp_atual_5 = new javax.swing.JLabel();
        Temp_atual_6 = new javax.swing.JLabel();
        Temp_atual_7 = new javax.swing.JLabel();
        Temp_atual_8 = new javax.swing.JLabel();
        Temp_atual_9 = new javax.swing.JLabel();
        Temp_atual_10 = new javax.swing.JLabel();
        Contagem_atual_1 = new javax.swing.JLabel();
        Contagem_atual_2 = new javax.swing.JLabel();
        Contagem_atual_3 = new javax.swing.JLabel();
        Contagem_atual_4 = new javax.swing.JLabel();
        Contagem_atual_5 = new javax.swing.JLabel();
        Contagem_atual_6 = new javax.swing.JLabel();
        Contagem_atual_7 = new javax.swing.JLabel();
        Contagem_atual_8 = new javax.swing.JLabel();
        Contagem_atual_9 = new javax.swing.JLabel();
        Contagem_atual_10 = new javax.swing.JLabel();
        Timer_1 = new javax.swing.JLabel();
        Timer_2 = new javax.swing.JLabel();
        Timer_3 = new javax.swing.JLabel();
        Timer_4 = new javax.swing.JLabel();
        Timer_5 = new javax.swing.JLabel();
        Timer_6 = new javax.swing.JLabel();
        Timer_7 = new javax.swing.JLabel();
        Timer_8 = new javax.swing.JLabel();
        Timer_9 = new javax.swing.JLabel();
        Timer_10 = new javax.swing.JLabel();
        Contador_1 = new javax.swing.JLabel();
        Contador_2 = new javax.swing.JLabel();
        Contador_3 = new javax.swing.JLabel();
        Contador_4 = new javax.swing.JLabel();
        Contador_5 = new javax.swing.JLabel();
        Contador_6 = new javax.swing.JLabel();
        Contador_7 = new javax.swing.JLabel();
        Contador_8 = new javax.swing.JLabel();
        Contador_9 = new javax.swing.JLabel();
        Contador_10 = new javax.swing.JLabel();
        label_1 = new javax.swing.JLabel();
        label_2 = new javax.swing.JLabel();
        label_3 = new javax.swing.JLabel();
        label_4 = new javax.swing.JLabel();
        label_5 = new javax.swing.JLabel();
        label_6 = new javax.swing.JLabel();
        label_7 = new javax.swing.JLabel();
        label_8 = new javax.swing.JLabel();
        label_9 = new javax.swing.JLabel();
        label_10 = new javax.swing.JLabel();
        label_11 = new javax.swing.JLabel();
        label_12 = new javax.swing.JLabel();
        label_13 = new javax.swing.JLabel();
        label_14 = new javax.swing.JLabel();
        label_15 = new javax.swing.JLabel();
        label_16 = new javax.swing.JLabel();
        label_17 = new javax.swing.JLabel();
        label_18 = new javax.swing.JLabel();
        label_19 = new javax.swing.JLabel();
        label_20 = new javax.swing.JLabel();
        Color_Camp = new javax.swing.JPanel();
        Codigo_Camp = new javax.swing.JTextArea();
        Image_Camp = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        simulationsComboBox = new javax.swing.JComboBox<>();
        startBt = new javax.swing.JButton();
        pauseBt = new javax.swing.JButton();
        refreshBt = new javax.swing.JButton();
        dataTableBt = new javax.swing.JButton();
        delay_panel = new javax.swing.JPanel();
        delayLabel = new javax.swing.JLabel();
        delaySpinner = new javax.swing.JSpinner();

        jMenu1.setText("jMenu1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Lista de Instruçoes CLP");
        setBackground(new java.awt.Color(255, 255, 255));

        Arquivar_BT.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Arquivar", "Salvar", "Carregar" }));
        Arquivar_BT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Arquivar_BTActionPerformed(evt);
            }
        });

        Editar_BT.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Editar", "Tema", "Idioma" }));
        Editar_BT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Editar_BTActionPerformed(evt);
            }
        });

        Sobre_BT.setText("Sobre");
        Sobre_BT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Sobre_BTActionPerformed(evt);
            }
        });

        sceneContainer.setBackground(new java.awt.Color(142, 177, 199));
        sceneContainer.setMaximumSize(new java.awt.Dimension(624, 394));
        sceneContainer.setMinimumSize(new java.awt.Dimension(624, 394));

        javax.swing.GroupLayout sceneContainerLayout = new javax.swing.GroupLayout(sceneContainer);
        sceneContainer.setLayout(sceneContainerLayout);
        sceneContainerLayout.setHorizontalGroup(
                sceneContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 624, Short.MAX_VALUE));
        sceneContainerLayout.setVerticalGroup(
                sceneContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 394, Short.MAX_VALUE));

        jPanel2.setBackground(new java.awt.Color(8, 94, 131));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Temp_parada_1.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.add(Temp_parada_1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 30, 50, 20));

        Temp_parada_2.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.add(Temp_parada_2, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 100, 50, 20));

        Temp_parada_3.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.add(Temp_parada_3, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 170, 50, 20));

        Temp_parada_4.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.add(Temp_parada_4, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 240, 50, 20));

        Temp_parada_5.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.add(Temp_parada_5, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 310, 50, 20));

        Temp_parada_6.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.add(Temp_parada_6, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 30, 50, 20));

        Temp_parada_7.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.add(Temp_parada_7, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 100, 50, 20));

        Temp_parada_8.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.add(Temp_parada_8, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 170, 50, 20));

        Temp_parada_9.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.add(Temp_parada_9, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 240, 50, 20));

        Temp_parada_10.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.add(Temp_parada_10, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 310, 50, 20));

        Contagem_parada_1.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.add(Contagem_parada_1, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 30, 50, 20));

        Contagem_parada_2.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.add(Contagem_parada_2, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 100, 50, 20));

        Contagem_parada_3.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.add(Contagem_parada_3, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 170, 50, 20));

        Contagem_parada_4.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.add(Contagem_parada_4, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 240, 50, 20));

        Contagem_parada_5.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.add(Contagem_parada_5, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 310, 50, 20));

        Contagem_parada_6.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.add(Contagem_parada_6, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 30, 50, 20));

        Contagem_parada_7.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.add(Contagem_parada_7, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 100, 50, 20));

        Contagem_parada_8.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.add(Contagem_parada_8, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 170, 50, 20));

        Contagem_parada_9.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.add(Contagem_parada_9, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 240, 50, 20));

        Contagem_parada_10.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.add(Contagem_parada_10, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 310, 50, 20));

        Temp_atual_1.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.add(Temp_atual_1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 10, 50, 20));

        Temp_atual_2.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.add(Temp_atual_2, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 80, 50, 20));

        Temp_atual_3.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.add(Temp_atual_3, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 150, 50, 20));

        Temp_atual_4.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.add(Temp_atual_4, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 220, 50, 20));

        Temp_atual_5.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.add(Temp_atual_5, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 290, 50, 20));

        Temp_atual_6.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.add(Temp_atual_6, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 10, 50, 20));

        Temp_atual_7.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.add(Temp_atual_7, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 80, 50, 20));

        Temp_atual_8.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.add(Temp_atual_8, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 150, 50, 20));

        Temp_atual_9.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.add(Temp_atual_9, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 220, 50, 20));

        Temp_atual_10.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.add(Temp_atual_10, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 290, 50, 20));

        Contagem_atual_1.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.add(Contagem_atual_1, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 10, 50, 20));

        Contagem_atual_2.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.add(Contagem_atual_2, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 80, 50, 20));

        Contagem_atual_3.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.add(Contagem_atual_3, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 150, 50, 20));

        Contagem_atual_4.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.add(Contagem_atual_4, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 220, 50, 20));

        Contagem_atual_5.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.add(Contagem_atual_5, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 290, 50, 20));

        Contagem_atual_6.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.add(Contagem_atual_6, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 10, 50, 20));

        Contagem_atual_7.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.add(Contagem_atual_7, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 80, 50, 20));

        Contagem_atual_8.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.add(Contagem_atual_8, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 150, 50, 20));

        Contagem_atual_9.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.add(Contagem_atual_9, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 220, 50, 20));

        Contagem_atual_10.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.add(Contagem_atual_10, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 290, 50, 20));

        Timer_1.setText("jLabel1");
        jPanel2.add(Timer_1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 130, 60));

        Timer_2.setText("jLabel1");
        jPanel2.add(Timer_2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 130, 60));

        Timer_3.setText("jLabel1");
        jPanel2.add(Timer_3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 150, 130, 60));

        Timer_4.setText("jLabel1");
        jPanel2.add(Timer_4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 220, 130, 60));

        Timer_5.setText("jLabel1");
        jPanel2.add(Timer_5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 290, 130, 60));

        Timer_6.setText("jLabel1");
        jPanel2.add(Timer_6, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 290, 130, 60));

        Timer_7.setText("jLabel1");
        jPanel2.add(Timer_7, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 220, 130, 60));

        Timer_8.setText("jLabel1");
        jPanel2.add(Timer_8, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 150, 130, 60));

        Timer_9.setText("jLabel1");
        jPanel2.add(Timer_9, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 80, 130, 60));

        Timer_10.setText("jLabel1");
        jPanel2.add(Timer_10, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 10, 130, 60));

        Contador_1.setText("jLabel1");
        jPanel2.add(Contador_1, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 10, 130, 60));

        Contador_2.setText("jLabel1");
        jPanel2.add(Contador_2, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 80, 130, 60));

        Contador_3.setText("jLabel1");
        jPanel2.add(Contador_3, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 150, 130, 60));

        Contador_4.setText("jLabel1");
        jPanel2.add(Contador_4, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 220, 130, 60));

        Contador_5.setText("jLabel1");
        jPanel2.add(Contador_5, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 290, 130, 60));

        Contador_6.setText("jLabel1");
        jPanel2.add(Contador_6, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 290, 130, 60));

        Contador_7.setText("jLabel1");
        jPanel2.add(Contador_7, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 220, 130, 60));

        Contador_8.setText("jLabel1");
        jPanel2.add(Contador_8, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 150, 130, 60));

        Contador_9.setText("jLabel1");
        jPanel2.add(Contador_9, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 80, 130, 60));

        Contador_10.setText("jLabel1");
        jPanel2.add(Contador_10, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 10, 130, 60));

        label_1.setForeground(new java.awt.Color(255, 255, 255));
        label_1.setText(" 1");
        jPanel2.add(label_1, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 40, 30, 30));

        label_2.setForeground(new java.awt.Color(255, 255, 255));
        label_2.setText(" 2");
        jPanel2.add(label_2, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 110, 30, 30));

        label_3.setForeground(new java.awt.Color(255, 255, 255));
        label_3.setText(" 3");
        jPanel2.add(label_3, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 180, 30, 30));

        label_4.setForeground(new java.awt.Color(255, 255, 255));
        label_4.setText(" 4");
        jPanel2.add(label_4, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 250, 30, 30));

        label_5.setForeground(new java.awt.Color(255, 255, 255));
        label_5.setText(" 5");
        jPanel2.add(label_5, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 320, 30, 30));

        label_6.setForeground(new java.awt.Color(255, 255, 255));
        label_6.setText(" 6");
        jPanel2.add(label_6, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 40, 30, 30));

        label_7.setForeground(new java.awt.Color(255, 255, 255));
        label_7.setText(" 7");
        jPanel2.add(label_7, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 110, 30, 30));

        label_8.setForeground(new java.awt.Color(255, 255, 255));
        label_8.setText(" 8");
        jPanel2.add(label_8, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 180, 30, 30));

        label_9.setForeground(new java.awt.Color(255, 255, 255));
        label_9.setText(" 9");
        jPanel2.add(label_9, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 250, 30, 30));

        label_10.setForeground(new java.awt.Color(255, 255, 255));
        label_10.setText("10");
        jPanel2.add(label_10, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 320, 30, 30));

        label_11.setForeground(new java.awt.Color(255, 255, 255));
        label_11.setText(" 1");
        jPanel2.add(label_11, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 40, 30, 30));

        label_12.setForeground(new java.awt.Color(255, 255, 255));
        label_12.setText(" 2");
        jPanel2.add(label_12, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 110, 30, 30));

        label_13.setForeground(new java.awt.Color(255, 255, 255));
        label_13.setText(" 3");
        jPanel2.add(label_13, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 180, 30, 30));

        label_14.setForeground(new java.awt.Color(255, 255, 255));
        label_14.setText(" 4");
        jPanel2.add(label_14, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 250, 30, 30));

        label_15.setForeground(new java.awt.Color(255, 255, 255));
        label_15.setText(" 5");
        jPanel2.add(label_15, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 320, 30, 30));

        label_16.setForeground(new java.awt.Color(255, 255, 255));
        label_16.setText(" 6");
        jPanel2.add(label_16, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 40, 30, 30));

        label_17.setForeground(new java.awt.Color(255, 255, 255));
        label_17.setText(" 7");
        jPanel2.add(label_17, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 110, 30, 30));

        label_18.setForeground(new java.awt.Color(255, 255, 255));
        label_18.setText(" 8");
        jPanel2.add(label_18, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 180, 30, 30));

        label_19.setForeground(new java.awt.Color(255, 255, 255));
        label_19.setText(" 9");
        jPanel2.add(label_19, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 250, 30, 30));

        label_20.setForeground(new java.awt.Color(255, 255, 255));
        label_20.setText("10");
        jPanel2.add(label_20, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 320, 30, 30));

        Color_Camp.setBackground(new java.awt.Color(0, 102, 204));
        Color_Camp.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Codigo_Camp.setColumns(20);
        Codigo_Camp.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        Codigo_Camp.setForeground(new java.awt.Color(255, 255, 255));
        Codigo_Camp.setRows(5);
        Codigo_Camp.setDisabledTextColor(new java.awt.Color(255, 255, 255));
        Codigo_Camp.setDragEnabled(true);
        Codigo_Camp.setSelectedTextColor(new java.awt.Color(0, 0, 0));
        Codigo_Camp.setSelectionColor(new java.awt.Color(204, 204, 204));
        Color_Camp.add(Codigo_Camp, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 350, 750));
        Color_Camp.add(Image_Camp, new org.netbeans.lib.awtextra.AbsoluteConstraints(-3, 6, 370, 750));

        simulationsComboBox.setBackground(new java.awt.Color(8, 94, 131));
        simulationsComboBox.setModel(new DefaultComboBoxModel<>(ScenesEnum.values()));
        simulationsComboBox.setSelectedIndex(0);
        simulationsComboBox.setSelectedItem(simulationsComboBox);
        simulationsComboBox.setMaximumSize(new java.awt.Dimension(150, 50));
        simulationsComboBox.setMinimumSize(new java.awt.Dimension(150, 50));
        simulationsComboBox.setName("simulations_combo_box"); // NOI18N
        simulationsComboBox.setOpaque(true);
        simulationsComboBox.setPreferredSize(new java.awt.Dimension(150, 50));
        simulationsComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                simulationsComboBoxActionPerformed(evt);
            }
        });
        jPanel3.add(simulationsComboBox);

        startBt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Assets/start.png"))); // NOI18N
        startBt.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        startBt.setMaximumSize(new java.awt.Dimension(50, 50));
        startBt.setMinimumSize(new java.awt.Dimension(50, 50));
        startBt.setName("start_bt"); // NOI18N
        startBt.setPreferredSize(new java.awt.Dimension(50, 50));
        startBt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startBtActionPerformed(evt);
            }
        });
        jPanel3.add(startBt);

        pauseBt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Assets/pause.png"))); // NOI18N
        pauseBt.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        pauseBt.setMaximumSize(new java.awt.Dimension(50, 50));
        pauseBt.setMinimumSize(new java.awt.Dimension(50, 50));
        pauseBt.setName("pause_br"); // NOI18N
        pauseBt.setPreferredSize(new java.awt.Dimension(50, 50));
        pauseBt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pauseBtActionPerformed(evt);
            }
        });
        jPanel3.add(pauseBt);

        refreshBt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Assets/refresh.png"))); // NOI18N
        refreshBt.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        refreshBt.setMaximumSize(new java.awt.Dimension(50, 50));
        refreshBt.setMinimumSize(new java.awt.Dimension(50, 50));
        refreshBt.setName("refresh_bt"); // NOI18N
        refreshBt.setPreferredSize(new java.awt.Dimension(50, 50));
        refreshBt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshBtActionPerformed(evt);
            }
        });
        jPanel3.add(refreshBt);

        dataTableBt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Assets/menu.png"))); // NOI18N
        dataTableBt.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        dataTableBt.setMaximumSize(new java.awt.Dimension(50, 50));
        dataTableBt.setMinimumSize(new java.awt.Dimension(50, 50));
        dataTableBt.setName("refresh_bt"); // NOI18N
        dataTableBt.setPreferredSize(new java.awt.Dimension(50, 50));
        dataTableBt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dataTableBtActionPerformed(evt);
            }
        });
        jPanel3.add(dataTableBt);

        delay_panel.setMinimumSize(new java.awt.Dimension(200, 35));
        delay_panel.setOpaque(false);
        delay_panel.setPreferredSize(new java.awt.Dimension(200, 35));

        delayLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        delayLabel.setText("Tempo de Delay em ms:");
        delay_panel.add(delayLabel);

        delaySpinner.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        delaySpinner.setMinimumSize(new java.awt.Dimension(50, 25));
        delaySpinner.setPreferredSize(new java.awt.Dimension(50, 25));
        delaySpinner.setRequestFocusEnabled(false);
        delay_panel.add(delaySpinner);

        jPanel3.add(delay_panel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(41, 41, 41)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout
                                                .createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 2,
                                                        Short.MAX_VALUE)
                                                .addGroup(layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING,
                                                                false)
                                                        .addComponent(sceneContainer,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addGap(18, 18, 18)
                                                .addComponent(Color_Camp, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(33, 33, 33))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(Arquivar_BT, javax.swing.GroupLayout.PREFERRED_SIZE, 122,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(Editar_BT, javax.swing.GroupLayout.PREFERRED_SIZE, 94,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(Sobre_BT)
                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        Short.MAX_VALUE))))
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 645,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(Sobre_BT)
                                        .addComponent(Editar_BT, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(Arquivar_BT, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(5, 5, 5)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(sceneContainer, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 358,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(Color_Camp, javax.swing.GroupLayout.PREFERRED_SIZE, 764,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(35, 35, 35)));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void Sobre_BTActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_Sobre_BTActionPerformed
        SobrePopup.mostrarSobre();
    }// GEN-LAST:event_Sobre_BTActionPerformed

    private void Editar_BTActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_Editar_BTActionPerformed
        if (Editar_BT.getItemAt(1) == Editar_BT.getSelectedItem().toString()) {
            Editar_BT.setSelectedIndex(0);
            HomePageModel.setColor(HomePageModel.getColor() + 1);
            if (HomePageModel.getColor() >= 5) {
                HomePageModel.setColor(1);
            }
            setaCores();
        }
        if (Editar_BT.getItemAt(2) == Editar_BT.getSelectedItem().toString()) {
            Editar_BT.setSelectedIndex(0);
            Language.setLingua();
            setaLanguage();
        }

    }// GEN-LAST:event_Editar_BTActionPerformed

    private void Arquivar_BTActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_Arquivar_BTActionPerformed
        HomePageController.handleFileArchiveAction(Arquivar_BT, Codigo_Camp, this.updating, this);
    }// GEN-LAST:event_Arquivar_BTActionPerformed

    private void refreshBtActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_refreshBtActionPerformed
        controller.handleRefreshAction();
    }// GEN-LAST:event_refreshBtActionPerformed

    private void simulationsComboBoxActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_simulationsComboBoxActionPerformed
        ScenesEnum selectedScene = (ScenesEnum) simulationsComboBox.getSelectedItem();

        setCurrentScene(selectedScene);
    }// GEN-LAST:event_simulationsComboBoxActionPerformed

    private void startBtActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_startBtActionPerformed
        if (!HomePageModel.isRunning()) {
            System.out.println("\nBotão run clicado!");
            HomePageModel.setMode(ExecutionMode.RUNNING);

            Integer time = controller.parseDelay(delaySpinner.getValue().toString());
            if (time == null) {
                HomePageModel.setMode(ExecutionMode.IDLE);
                updateMode();
                showErrorMessage("Tempo de delay inválido! Insira um número inteiro.");
                return;
            }

            System.out.println("Tempo de delay: " + time + "\n");

            Timer timer = new Timer(time, e -> controller.runCycle(e));
            timer.setInitialDelay(0);
            timer.start();

        } else {
            System.out.println("\nBotão stop clicado!");
            controller.stopTimers();
            HomePageModel.setMode(ExecutionMode.STOPPED);
            updateMemoryVariables();
            updateMode();
        }
    }// GEN-LAST:event_startBtActionPerformed

    private void pauseBtActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_pauseBtActionPerformed
        HomePageModel.setMode(ExecutionMode.IDLE);
        for (Map.Entry<String, MemoryVariable> variable : HomePageModel.getMemoryVariables().entrySet()) {
            if (variable.getKey().charAt(0) == 'T') {
                variable.getValue().counter = 0;
                variable.getValue().timer.stop();
            }
        }
        updateMemoryVariables();
        updateMode();
    }// GEN-LAST:event_pauseBtActionPerformed

    private void dataTableBtActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_dataTableBtActionPerformed
        tela2.setVisible(true);
        tela2.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        tela2.setLocation(1100, 0);
    }// GEN-LAST:event_dataTableBtActionPerformed

    private void setaCores() {
        simulationsComboBox.setBackground(Colors.firstColor(HomePageModel.getColor()));
        jPanel2.setBackground(Colors.firstColor(HomePageModel.getColor()));
        sceneContainer.setBackground(Colors.secondColor(HomePageModel.getColor()));
        Color_Camp.setBackground(Colors.thirdColor(HomePageModel.getColor()));
    }

    private void setaLanguage() {
        JComboBox aux = Language.getArquivar();
        updating = true;
        Arquivar_BT.removeItemAt(0);
        Arquivar_BT.removeItemAt(0);
        Arquivar_BT.removeItemAt(0);
        Arquivar_BT.insertItemAt(aux.getItemAt(0).toString(), 0);
        Arquivar_BT.insertItemAt(aux.getItemAt(1).toString(), 1);
        Arquivar_BT.insertItemAt(aux.getItemAt(2).toString(), 2);
        Arquivar_BT.setSelectedIndex(0);
        updating = false;

        aux = Language.getEditar();
        Editar_BT.removeItemAt(0);
        Editar_BT.removeItemAt(0);
        Editar_BT.removeItemAt(0);
        Editar_BT.insertItemAt(aux.getItemAt(0).toString(), 0);
        Editar_BT.insertItemAt(aux.getItemAt(1).toString(), 1);
        Editar_BT.insertItemAt(aux.getItemAt(2).toString(), 2);
        Editar_BT.setSelectedIndex(0);

        Sobre_BT.setText(Language.getSobre());
        delayLabel.setText(Language.getDelay());
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            new HomePg().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> Arquivar_BT;
    private javax.swing.JTextArea Codigo_Camp;
    private javax.swing.JPanel Color_Camp;
    private javax.swing.JLabel Contador_1;
    private javax.swing.JLabel Contador_10;
    private javax.swing.JLabel Contador_2;
    private javax.swing.JLabel Contador_3;
    private javax.swing.JLabel Contador_4;
    private javax.swing.JLabel Contador_5;
    private javax.swing.JLabel Contador_6;
    private javax.swing.JLabel Contador_7;
    private javax.swing.JLabel Contador_8;
    private javax.swing.JLabel Contador_9;
    private javax.swing.JLabel Contagem_atual_1;
    private javax.swing.JLabel Contagem_atual_10;
    private javax.swing.JLabel Contagem_atual_2;
    private javax.swing.JLabel Contagem_atual_3;
    private javax.swing.JLabel Contagem_atual_4;
    private javax.swing.JLabel Contagem_atual_5;
    private javax.swing.JLabel Contagem_atual_6;
    private javax.swing.JLabel Contagem_atual_7;
    private javax.swing.JLabel Contagem_atual_8;
    private javax.swing.JLabel Contagem_atual_9;
    private javax.swing.JLabel Contagem_parada_1;
    private javax.swing.JLabel Contagem_parada_10;
    private javax.swing.JLabel Contagem_parada_2;
    private javax.swing.JLabel Contagem_parada_3;
    private javax.swing.JLabel Contagem_parada_4;
    private javax.swing.JLabel Contagem_parada_5;
    private javax.swing.JLabel Contagem_parada_6;
    private javax.swing.JLabel Contagem_parada_7;
    private javax.swing.JLabel Contagem_parada_8;
    private javax.swing.JLabel Contagem_parada_9;
    private javax.swing.JComboBox<String> Editar_BT;
    private javax.swing.JLabel Image_Camp;
    private javax.swing.JButton Sobre_BT;
    private javax.swing.JLabel Temp_atual_1;
    private javax.swing.JLabel Temp_atual_10;
    private javax.swing.JLabel Temp_atual_2;
    private javax.swing.JLabel Temp_atual_3;
    private javax.swing.JLabel Temp_atual_4;
    private javax.swing.JLabel Temp_atual_5;
    private javax.swing.JLabel Temp_atual_6;
    private javax.swing.JLabel Temp_atual_7;
    private javax.swing.JLabel Temp_atual_8;
    private javax.swing.JLabel Temp_atual_9;
    private javax.swing.JLabel Temp_parada_1;
    private javax.swing.JLabel Temp_parada_10;
    private javax.swing.JLabel Temp_parada_2;
    private javax.swing.JLabel Temp_parada_3;
    private javax.swing.JLabel Temp_parada_4;
    private javax.swing.JLabel Temp_parada_5;
    private javax.swing.JLabel Temp_parada_6;
    private javax.swing.JLabel Temp_parada_7;
    private javax.swing.JLabel Temp_parada_8;
    private javax.swing.JLabel Temp_parada_9;
    private javax.swing.JLabel Timer_1;
    private javax.swing.JLabel Timer_10;
    private javax.swing.JLabel Timer_2;
    private javax.swing.JLabel Timer_3;
    private javax.swing.JLabel Timer_4;
    private javax.swing.JLabel Timer_5;
    private javax.swing.JLabel Timer_6;
    private javax.swing.JLabel Timer_7;
    private javax.swing.JLabel Timer_8;
    private javax.swing.JLabel Timer_9;
    private javax.swing.JButton dataTableBt;
    private javax.swing.JLabel delayLabel;
    private javax.swing.JSpinner delaySpinner;
    private javax.swing.JPanel delay_panel;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel label_1;
    private javax.swing.JLabel label_10;
    private javax.swing.JLabel label_11;
    private javax.swing.JLabel label_12;
    private javax.swing.JLabel label_13;
    private javax.swing.JLabel label_14;
    private javax.swing.JLabel label_15;
    private javax.swing.JLabel label_16;
    private javax.swing.JLabel label_17;
    private javax.swing.JLabel label_18;
    private javax.swing.JLabel label_19;
    private javax.swing.JLabel label_2;
    private javax.swing.JLabel label_20;
    private javax.swing.JLabel label_3;
    private javax.swing.JLabel label_4;
    private javax.swing.JLabel label_5;
    private javax.swing.JLabel label_6;
    private javax.swing.JLabel label_7;
    private javax.swing.JLabel label_8;
    private javax.swing.JLabel label_9;
    private javax.swing.JButton pauseBt;
    private javax.swing.JButton refreshBt;
    private javax.swing.JPanel sceneContainer;
    private javax.swing.JComboBox<ScenesEnum> simulationsComboBox;
    private javax.swing.JButton startBt;
    // End of variables declaration//GEN-END:variables
}
