/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package screens;

import ilcompiler.edit.Colors;
import ilcompiler.edit.Language;
import javax.swing.ImageIcon;
import ilcompiler.interpreter.Interpreter;
import ilcompiler.input.InputActions;
import ilcompiler.memoryvariable.MemoryVariable;
import ilcompiler.output.OutputActions;
import ilcompiler.uppercasedocumentfilter.UpperCaseDocumentFilter;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import save.Save;

public final class HomePg extends javax.swing.JFrame {

    /**
     * Creates new form HomePg
     */
    // Cria variáveis
    static Map<String, Integer> inputsType;
    static Map<String, Boolean> inputs;
    static Map<String, Boolean> outputs;
    static Map<String, MemoryVariable> memoryVariables = new HashMap<>();
    static Integer mode = 1;
    static Integer color = 1;
    ListaDeVariaveisPg tela2 = new ListaDeVariaveisPg();
    private JTextArea Lista_de_variaveis = null;
    private boolean updating = false;
    
    private ScenePanel currentScenePanel = new ScenePanel();

    @SuppressWarnings("unchecked")
    public HomePg() {
        initComponents();
        Lista_de_variaveis = tela2.getListaDeVariaveis();

        ImageIcon iconCampo = new ImageIcon(getClass().getResource("/Assets/bloco_notas.png"));
        iconCampo.setImage(iconCampo.getImage().getScaledInstance(Codigo_Camp.getWidth(), Codigo_Camp.getHeight(), 1));
        Image_Camp.setIcon(iconCampo);

        Codigo_Camp.setOpaque(false);

        //adicionando icones de contador e timer
        ImageIcon icontimer = new ImageIcon(getClass().getResource("/Assets/temporizador.png"));
        icontimer.setImage(icontimer.getImage().getScaledInstance(Timer_1.getWidth(), Timer_1.getHeight(), 1));
        Timer_1.setIcon(icontimer);
        Timer_2.setIcon(icontimer);
        Timer_3.setIcon(icontimer);
        Timer_4.setIcon(icontimer);
        Timer_5.setIcon(icontimer);
        Timer_6.setIcon(icontimer);
        Timer_7.setIcon(icontimer);
        Timer_8.setIcon(icontimer);
        Timer_9.setIcon(icontimer);
        Timer_10.setIcon(icontimer);
        ImageIcon iconCont = new ImageIcon(getClass().getResource("/Assets/contador.png"));
        iconCont.setImage(iconCont.getImage().getScaledInstance(Contador_1.getWidth(), Contador_1.getHeight(), 1));
        Contador_1.setIcon(iconCont);
        Contador_2.setIcon(iconCont);
        Contador_3.setIcon(iconCont);
        Contador_4.setIcon(iconCont);
        Contador_5.setIcon(iconCont);
        Contador_6.setIcon(iconCont);
        Contador_7.setIcon(iconCont);
        Contador_8.setIcon(iconCont);
        Contador_9.setIcon(iconCont);
        Contador_10.setIcon(iconCont);

        AbstractDocument doc = (AbstractDocument) Codigo_Camp.getDocument();
        doc.setDocumentFilter(new UpperCaseDocumentFilter());
        //Inicializa entradas e saídas
        inputsType = new HashMap<>();
        inputs = new HashMap<>();
        outputs = new HashMap<>();
        inputsType = InputActions.createType(inputsType);
        inputs = InputActions.create(inputs);
        System.out.println("HashMap de entradas criado:" + inputs);
        outputs = OutputActions.create(outputs);
        System.out.println("HashMap de saídas criado:" + outputs);
        
        currentScenePanel.setInputListener(new ScenePanelInputEventListener() {
            @Override
            public void onPressed(String inputKey, MouseEvent evt) {
                handleInputButtonPressed(inputKey, evt);
            }

            @Override
            public void onReleased(String inputKey, MouseEvent evt) {
                handleInputButtonReleased(inputKey, evt);
            }
        });
        sceneContainer.setLayout(new BorderLayout());
        sceneContainer.add(currentScenePanel, BorderLayout.CENTER);
        sceneContainer.revalidate();
        sceneContainer.repaint();
        
        this.setResizable(false);
        
        pack();

        // Atualiza entradas e saídas na tela
        updateSceneUI();
    }
    
    private void handleInputButtonPressed(String inputKey, java.awt.event.MouseEvent evt) {
        if (evt.getButton() == java.awt.event.MouseEvent.BUTTON1) {
            switch (inputsType.get(inputKey)) {
                case 0 ->
                    inputs.put(inputKey, !inputs.get(inputKey));
                case 1 ->
                    inputs.put(inputKey, true);
                case 2 ->
                    inputs.put(inputKey, false);
            }
            updateSceneUI();
        } else if (evt.getButton() == java.awt.event.MouseEvent.BUTTON3) {
            int val = inputsType.get(inputKey) + 1;
            if (val >= 3) {
                val = 0;
            }
            inputsType.put(inputKey, val);
            inputs.put(inputKey, (val == 2));
            updateSceneUI();
        }
    }

    private void handleInputButtonReleased(String key, java.awt.event.MouseEvent evt) {
        if (evt.getButton() == java.awt.event.MouseEvent.BUTTON1) {
            int type = inputsType.get(key);
            if (type == 1) {
                inputs.put(key, false);
            } else if (type == 2) {
                inputs.put(key, true);
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

    // Atualiza entradas e saídas na tela
    public void updateSceneUI() {
        currentScenePanel.updateUIState(inputsType, inputs, outputs);
    }

    // Atualiza o modo atual na tela
    public void updateMode() {
        System.out.println("Modo atual: " + mode);
        
        boolean isRunningMode = mode == 3;
        refreshBt.setEnabled(!isRunningMode);
        simulationsComboBox.setEnabled(!isRunningMode);
        
        if (mode == null) {
            Codigo_Camp.setEditable(false);
            ImageIcon icon1 = new ImageIcon(getClass().getResource("/Assets/start_green.png"));
            startBt.setIcon(icon1);
        } else {
            switch (mode) {
                case 1 -> {
                    Codigo_Camp.setEditable(true);
                    ImageIcon icon1 = new ImageIcon(getClass().getResource("/Assets/start.png"));
                    startBt.setIcon(icon1);
                }
                case 2 -> {
                    Codigo_Camp.setEditable(false);
                    ImageIcon icon1 = new ImageIcon(getClass().getResource("/Assets/start.png"));
                    startBt.setIcon(icon1);
                }
                default -> {
                    Codigo_Camp.setEditable(false);
                    ImageIcon icon1 = new ImageIcon(getClass().getResource("/Assets/start_green.png"));
                    startBt.setIcon(icon1);
                }
            }
        }
    }

    // Atualiza as variáveis de memória na tela
    public void updateMemoryVariables() {
        Lista_de_variaveis.setText("");

        String line = "";
        List<MemoryVariable> tVariables = new ArrayList<>();
        List<MemoryVariable> cVariables = new ArrayList<>();
        int contC = 1;
        int contT = 1;

        for (Map.Entry<String, MemoryVariable> variable : memoryVariables.entrySet()) {
            switch (variable.getKey().charAt(0)) {
                case 'M' -> {
                    line = variable.getKey() + " = " + variable.getValue().currentValue + "\n";
                }
                case 'T' -> {
                    line = variable.getKey() + " = " + variable.getValue().currentValue + ", " + variable.getValue().counter + ", " + variable.getValue().maxTimer + ", " + variable.getValue().endTimer + "\n";
                    if (tVariables.size() < 10) {
                        tVariables.add(variable.getValue());
                    }
                }
                case 'C' -> {
                    line = variable.getKey() + " = " + variable.getValue().counter + ", " + variable.getValue().maxTimer + ", " + variable.getValue().endTimer + "\n";
                    if (cVariables.size() < 10) {
                        cVariables.add(variable.getValue());
                    }
                }
                default -> {
                }
            }

            Lista_de_variaveis.setText(Lista_de_variaveis.getText() + line);
        }

        // Exemplo de como você pode usar as listas tVariables e cVariables
        for (MemoryVariable tVariable : tVariables) {
            // System.out.println("ID: " + tVariable.id + ", Counter: " + tVariable.counter + ", MaxTimer: " + tVariable.maxTimer);
            switch (contT) {
                case 1 -> {
                    Timer_1.setText(String.valueOf(tVariable.id));
                    Timer_1.setHorizontalTextPosition(JLabel.CENTER);
                    Timer_1.setVerticalTextPosition(JLabel.CENTER);
                    Timer_1.setForeground(Color.WHITE);

                    Temp_atual_1.setText(String.valueOf(tVariable.counter));
                    Temp_atual_1.setHorizontalAlignment(SwingConstants.CENTER);

                    Temp_parada_1.setText(String.valueOf(tVariable.maxTimer));
                    Temp_parada_1.setHorizontalAlignment(SwingConstants.CENTER);
                }
                case 2 -> {
                    Timer_2.setText(String.valueOf(tVariable.id));
                    Timer_2.setHorizontalTextPosition(JLabel.CENTER);
                    Timer_2.setVerticalTextPosition(JLabel.CENTER);
                    Timer_2.setForeground(Color.WHITE);

                    Temp_atual_2.setText(String.valueOf(tVariable.counter));
                    Temp_atual_2.setHorizontalAlignment(SwingConstants.CENTER);

                    Temp_parada_2.setText(String.valueOf(tVariable.maxTimer));
                    Temp_parada_2.setHorizontalAlignment(SwingConstants.CENTER);
                }
                case 3 -> {
                    Timer_3.setText(String.valueOf(tVariable.id));
                    Timer_3.setHorizontalTextPosition(JLabel.CENTER);
                    Timer_3.setVerticalTextPosition(JLabel.CENTER);
                    Timer_3.setForeground(Color.WHITE);

                    Temp_atual_3.setText(String.valueOf(tVariable.counter));
                    Temp_atual_3.setHorizontalAlignment(SwingConstants.CENTER);

                    Temp_parada_3.setText(String.valueOf(tVariable.maxTimer));
                    Temp_parada_3.setHorizontalAlignment(SwingConstants.CENTER);
                }
                case 4 -> {
                    Timer_4.setText(String.valueOf(tVariable.id));
                    Timer_4.setHorizontalTextPosition(JLabel.CENTER);
                    Timer_4.setVerticalTextPosition(JLabel.CENTER);
                    Timer_4.setForeground(Color.WHITE);

                    Temp_atual_4.setText(String.valueOf(tVariable.counter));
                    Temp_atual_4.setHorizontalAlignment(SwingConstants.CENTER);

                    Temp_parada_4.setText(String.valueOf(tVariable.maxTimer));
                    Temp_parada_4.setHorizontalAlignment(SwingConstants.CENTER);
                }
                case 5 -> {
                    Timer_5.setText(String.valueOf(tVariable.id));
                    Timer_5.setHorizontalTextPosition(JLabel.CENTER);
                    Timer_5.setVerticalTextPosition(JLabel.CENTER);
                    Timer_5.setForeground(Color.WHITE);

                    Temp_atual_5.setText(String.valueOf(tVariable.counter));
                    Temp_atual_5.setHorizontalAlignment(SwingConstants.CENTER);

                    Temp_parada_5.setText(String.valueOf(tVariable.maxTimer));
                    Temp_parada_5.setHorizontalAlignment(SwingConstants.CENTER);
                }
                case 6 -> {
                    Timer_6.setText(String.valueOf(tVariable.id));
                    Timer_6.setHorizontalTextPosition(JLabel.CENTER);
                    Timer_6.setVerticalTextPosition(JLabel.CENTER);
                    Timer_6.setForeground(Color.WHITE);

                    Temp_atual_6.setText(String.valueOf(tVariable.counter));
                    Temp_atual_6.setHorizontalAlignment(SwingConstants.CENTER);

                    Temp_parada_6.setText(String.valueOf(tVariable.maxTimer));
                    Temp_parada_6.setHorizontalAlignment(SwingConstants.CENTER);
                }
                case 7 -> {
                    Timer_7.setText(String.valueOf(tVariable.id));
                    Timer_7.setHorizontalTextPosition(JLabel.CENTER);
                    Timer_7.setVerticalTextPosition(JLabel.CENTER);
                    Timer_7.setForeground(Color.WHITE);

                    Temp_atual_7.setText(String.valueOf(tVariable.counter));
                    Temp_atual_7.setHorizontalAlignment(SwingConstants.CENTER);

                    Temp_parada_7.setText(String.valueOf(tVariable.maxTimer));
                    Temp_parada_7.setHorizontalAlignment(SwingConstants.CENTER);
                }
                case 8 -> {
                    Timer_8.setText(String.valueOf(tVariable.id));
                    Timer_8.setHorizontalTextPosition(JLabel.CENTER);
                    Timer_8.setVerticalTextPosition(JLabel.CENTER);
                    Timer_8.setForeground(Color.WHITE);

                    Temp_atual_8.setText(String.valueOf(tVariable.counter));
                    Temp_atual_8.setHorizontalAlignment(SwingConstants.CENTER);

                    Temp_parada_8.setText(String.valueOf(tVariable.maxTimer));
                    Temp_parada_8.setHorizontalAlignment(SwingConstants.CENTER);
                }
                case 9 -> {
                    Timer_9.setText(String.valueOf(tVariable.id));
                    Timer_9.setHorizontalTextPosition(JLabel.CENTER);
                    Timer_9.setVerticalTextPosition(JLabel.CENTER);
                    Timer_9.setForeground(Color.WHITE);

                    Temp_atual_9.setText(String.valueOf(tVariable.counter));
                    Temp_atual_9.setHorizontalAlignment(SwingConstants.CENTER);

                    Temp_parada_9.setText(String.valueOf(tVariable.maxTimer));
                    Temp_parada_9.setHorizontalAlignment(SwingConstants.CENTER);
                }
                case 10 -> {
                    Timer_10.setText(String.valueOf(tVariable.id));
                    Timer_10.setHorizontalTextPosition(JLabel.CENTER);
                    Timer_10.setVerticalTextPosition(JLabel.CENTER);
                    Timer_10.setForeground(Color.WHITE);

                    Temp_atual_10.setText(String.valueOf(tVariable.counter));
                    Temp_atual_10.setHorizontalAlignment(SwingConstants.CENTER);

                    Temp_parada_10.setText(String.valueOf(tVariable.maxTimer));
                    Temp_parada_10.setHorizontalAlignment(SwingConstants.CENTER);
                }
            }
            contT++;
        }
        for (MemoryVariable cVariable : cVariables) {
            // System.out.println("ID: " + cVariable.id + ", Counter: " + cVariable.counter + ", MaxTimer: " + cVariable.maxTimer);
            switch (contC) {
                case 1 -> {
                    Contador_1.setText(String.valueOf(cVariable.id));
                    Contador_1.setHorizontalTextPosition(JLabel.CENTER);
                    Contador_1.setVerticalTextPosition(JLabel.CENTER);
                    Contador_1.setForeground(Color.WHITE);

                    Contagem_atual_1.setText(String.valueOf(cVariable.counter));
                    Contagem_atual_1.setHorizontalAlignment(SwingConstants.CENTER);

                    Contagem_parada_1.setText(String.valueOf(cVariable.maxTimer));
                    Contagem_parada_1.setHorizontalAlignment(SwingConstants.CENTER);
                }
                case 2 -> {
                    Contador_2.setText(String.valueOf(cVariable.id));
                    Contador_2.setHorizontalTextPosition(JLabel.CENTER);
                    Contador_2.setVerticalTextPosition(JLabel.CENTER);
                    Contador_2.setForeground(Color.WHITE);

                    Contagem_atual_2.setText(String.valueOf(cVariable.counter));
                    Contagem_atual_2.setHorizontalAlignment(SwingConstants.CENTER);

                    Contagem_parada_2.setText(String.valueOf(cVariable.maxTimer));
                    Contagem_parada_2.setHorizontalAlignment(SwingConstants.CENTER);
                }
                case 3 -> {
                    Contador_3.setText(String.valueOf(cVariable.id));
                    Contador_3.setHorizontalTextPosition(JLabel.CENTER);
                    Contador_3.setVerticalTextPosition(JLabel.CENTER);
                    Contador_3.setForeground(Color.WHITE);

                    Contagem_atual_3.setText(String.valueOf(cVariable.counter));
                    Contagem_atual_3.setHorizontalAlignment(SwingConstants.CENTER);

                    Contagem_parada_3.setText(String.valueOf(cVariable.maxTimer));
                    Contagem_parada_3.setHorizontalAlignment(SwingConstants.CENTER);
                }
                case 4 -> {
                    Contador_4.setText(String.valueOf(cVariable.id));
                    Contador_4.setHorizontalTextPosition(JLabel.CENTER);
                    Contador_4.setVerticalTextPosition(JLabel.CENTER);
                    Contador_4.setForeground(Color.WHITE);

                    Contagem_atual_4.setText(String.valueOf(cVariable.counter));
                    Contagem_atual_4.setHorizontalAlignment(SwingConstants.CENTER);

                    Contagem_parada_4.setText(String.valueOf(cVariable.maxTimer));
                    Contagem_parada_4.setHorizontalAlignment(SwingConstants.CENTER);
                }
                case 5 -> {
                    Contador_5.setText(String.valueOf(cVariable.id));
                    Contador_5.setHorizontalTextPosition(JLabel.CENTER);
                    Contador_5.setVerticalTextPosition(JLabel.CENTER);
                    Contador_5.setForeground(Color.WHITE);

                    Contagem_atual_5.setText(String.valueOf(cVariable.counter));
                    Contagem_atual_5.setHorizontalAlignment(SwingConstants.CENTER);

                    Contagem_parada_5.setText(String.valueOf(cVariable.maxTimer));
                    Contagem_parada_5.setHorizontalAlignment(SwingConstants.CENTER);
                }
                case 6 -> {
                    Contador_6.setText(String.valueOf(cVariable.id));
                    Contador_6.setHorizontalTextPosition(JLabel.CENTER);
                    Contador_6.setVerticalTextPosition(JLabel.CENTER);
                    Contador_6.setForeground(Color.WHITE);

                    Contagem_atual_6.setText(String.valueOf(cVariable.counter));
                    Contagem_atual_6.setHorizontalAlignment(SwingConstants.CENTER);

                    Contagem_parada_6.setText(String.valueOf(cVariable.maxTimer));
                    Contagem_parada_6.setHorizontalAlignment(SwingConstants.CENTER);
                }
                case 7 -> {
                    Contador_7.setText(String.valueOf(cVariable.id));
                    Contador_7.setHorizontalTextPosition(JLabel.CENTER);
                    Contador_7.setVerticalTextPosition(JLabel.CENTER);
                    Contador_7.setForeground(Color.WHITE);

                    Contagem_atual_7.setText(String.valueOf(cVariable.counter));
                    Contagem_atual_7.setHorizontalAlignment(SwingConstants.CENTER);

                    Contagem_parada_7.setText(String.valueOf(cVariable.maxTimer));
                    Contagem_parada_7.setHorizontalAlignment(SwingConstants.CENTER);
                }
                case 8 -> {
                    Contador_8.setText(String.valueOf(cVariable.id));
                    Contador_8.setHorizontalTextPosition(JLabel.CENTER);
                    Contador_8.setVerticalTextPosition(JLabel.CENTER);
                    Contador_8.setForeground(Color.WHITE);

                    Contagem_atual_8.setText(String.valueOf(cVariable.counter));
                    Contagem_atual_8.setHorizontalAlignment(SwingConstants.CENTER);

                    Contagem_parada_8.setText(String.valueOf(cVariable.maxTimer));
                    Contagem_parada_8.setHorizontalAlignment(SwingConstants.CENTER);
                }
                case 9 -> {
                    Contador_9.setText(String.valueOf(cVariable.id));
                    Contador_9.setHorizontalTextPosition(JLabel.CENTER);
                    Contador_9.setVerticalTextPosition(JLabel.CENTER);
                    Contador_9.setForeground(Color.WHITE);

                    Contagem_atual_9.setText(String.valueOf(cVariable.counter));
                    Contagem_atual_9.setHorizontalAlignment(SwingConstants.CENTER);

                    Contagem_parada_9.setText(String.valueOf(cVariable.maxTimer));
                    Contagem_parada_9.setHorizontalAlignment(SwingConstants.CENTER);
                }
                case 10 -> {
                    Contador_10.setText(String.valueOf(cVariable.id));
                    Contador_10.setHorizontalTextPosition(JLabel.CENTER);
                    Contador_10.setVerticalTextPosition(JLabel.CENTER);
                    Contador_10.setForeground(Color.WHITE);

                    Contagem_atual_10.setText(String.valueOf(cVariable.counter));
                    Contagem_atual_10.setHorizontalAlignment(SwingConstants.CENTER);

                    Contagem_parada_10.setText(String.valueOf(cVariable.maxTimer));
                    Contagem_parada_10.setHorizontalAlignment(SwingConstants.CENTER);
                }
            }
            contC++;
        }
    }

    // Mostra mensagem de erro na tela
    public static void showErrorMessage(String message) {
        mode = 1;
        JOptionPane.showMessageDialog(null, message);
    }

    private List<String> saveLines(List<String> lineList) {
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

    @SuppressWarnings("rawtypes")
    public Map setaBit(Map<String, Boolean> inputs) {
        Boolean input = inputs.get("I1");
        inputs.clear();
        inputs.put("I1", !input);
        inputs.put("I2", !input);
        inputs.put("I3", !input);
        inputs.put("I4", !input);
        inputs.put("I5", !input);
        inputs.put("I6", !input);
        inputs.put("I7", !input);
        inputs.put("I8", !input);
        return inputs;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenu1 = new javax.swing.JMenu();
        Arquivar_BT = new javax.swing.JComboBox<>();
        Editar_BT = new javax.swing.JComboBox<>();
        Help_BT = new javax.swing.JButton();
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

        Help_BT.setText("Help");

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
            .addGap(0, 624, Short.MAX_VALUE)
        );
        sceneContainerLayout.setVerticalGroup(
            sceneContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 394, Short.MAX_VALUE)
        );

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
        simulationsComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Painel (padrão)", "Simulação Batch" }));
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
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 2, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(sceneContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addComponent(Color_Camp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(Arquivar_BT, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Editar_BT, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Help_BT)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Sobre_BT)
                        .addContainerGap(658, Short.MAX_VALUE))))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 645, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Sobre_BT)
                    .addComponent(Help_BT)
                    .addComponent(Editar_BT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Arquivar_BT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(sceneContainer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 358, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(Color_Camp, javax.swing.GroupLayout.PREFERRED_SIZE, 764, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void Sobre_BTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Sobre_BTActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Sobre_BTActionPerformed

    private void Editar_BTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Editar_BTActionPerformed
        if (Editar_BT.getItemAt(1) == Editar_BT.getSelectedItem().toString()) {
            Editar_BT.setSelectedIndex(0);
            color++;
            if (color >= 5) {
                color = 1;
            }
            setaCores();
        }
        if (Editar_BT.getItemAt(2) == Editar_BT.getSelectedItem().toString()) {
            Editar_BT.setSelectedIndex(0);
            Language.setLingua();
            setaLanguage();
        }

    }//GEN-LAST:event_Editar_BTActionPerformed

    private void Arquivar_BTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Arquivar_BTActionPerformed
        if (updating) {
            return;
        }

        if (Language.getArquivar().getItemAt(2) == (Arquivar_BT.getSelectedItem().toString())) {
            JFileChooser c = new JFileChooser();
            String filename = "";
            String dir = "";
            // Demonstrate "Open" dialog:
            int rVal = c.showOpenDialog(HomePg.this);
            if (rVal == JFileChooser.APPROVE_OPTION) {
                filename = (c.getSelectedFile().getName());
                dir = (c.getCurrentDirectory().toString());
            }
            List<String> memory = new ArrayList<>();
            try {
                memory = Save.load(dir + "\\" + filename);
                Codigo_Camp.setText("");
                for (int i = 0; i < memory.size(); i++) {
                    Codigo_Camp.append(memory.get(i));
                    Codigo_Camp.append("\n");
                }
            } catch (IOException ex) {
                Logger.getLogger(HomePg.class.getName()).log(Level.SEVERE, null, ex);
            }
            Arquivar_BT.setSelectedIndex(0);
        }

        if (Arquivar_BT.getItemAt(1) == (Arquivar_BT.getSelectedItem())) {

            Arquivar_BT.setSelectedIndex(0);

            JFileChooser c = new JFileChooser();
            String filename = "";
            String dir = "";
            // Demonstrate "Save" dialog:
            int rVal = c.showSaveDialog(HomePg.this);
            if (rVal == JFileChooser.APPROVE_OPTION) {
                filename = (c.getSelectedFile().getName());
                dir = (c.getCurrentDirectory().toString());
            }

            List<String> memory = new ArrayList<>();
            memory = saveLines(memory);
            try {
                Save.save(dir + "\\" + filename, memory);
            } catch (IOException ex) {
                Logger.getLogger(HomePg.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_Arquivar_BTActionPerformed

    private void refreshBtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshBtActionPerformed
        if (mode == 3) {
            return;
        }

        outputs = OutputActions.resetOutputs(outputs);

        for (Map.Entry<String, MemoryVariable> entry : memoryVariables.entrySet()) {
            MemoryVariable variable = entry.getValue();
            variable.timer.stop();
            variable.counter = 0;
            variable.currentValue = false;
        }

        updateMemoryVariables();
        updateSceneUI();
    }//GEN-LAST:event_refreshBtActionPerformed

    private void simulationsComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_simulationsComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_simulationsComboBoxActionPerformed

    private void startBtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startBtActionPerformed
        if (mode != 3) {
            System.out.println("\nBotão run clicado!");
            mode = 3;
            // Verificando tempo de delay
            String stringTime = delaySpinner.getValue().toString();
            Integer time = 0;

            if (!stringTime.equals("")) {
                try {
                    time = Integer.valueOf(stringTime);
                } catch (NumberFormatException e) {
                    mode = 1;
                    updateMode();
                    showErrorMessage("Tempo de delay inválido! Insira um número inteiro.");
                }

                System.out.println("Tempo de delay: " + time + "\n");
            }

            // Executa o laço corretamente sem travar a tela 
            @SuppressWarnings("unchecked")
            Timer timer = new Timer(time, (ActionEvent evt1) -> {
                // Salva linhas da área de texto
                List<String> lineList = new ArrayList<>();
                lineList = saveLines(lineList);
                if (mode == 3) {
                    inputs = InputActions.read(inputs);
                    outputs = OutputActions.resetOutputs(outputs);
                    outputs = Interpreter.receiveLines(lineList, inputs, outputs, memoryVariables);
                    for (Map.Entry<String, MemoryVariable> variable : memoryVariables.entrySet()) {
                        if (variable.getKey().charAt(0) == 'T' && variable.getValue().timerType.equals("ON") && variable.getValue().currentValue == true) {
                            variable.getValue().timer.start();
                        } else if (variable.getKey().charAt(0) == 'T' && variable.getValue().timerType.equals("ON") && variable.getValue().currentValue == false) {
                            variable.getValue().timer.stop();
                            variable.getValue().counter = 0;
                            variable.getValue().endTimer = false;
                        }
                        if (variable.getKey().charAt(0) == 'T' && variable.getValue().timerType.equals("OFF") && variable.getValue().currentValue == true) {
                            variable.getValue().timer.stop();
                            variable.getValue().counter = 0;
                            variable.getValue().endTimer = true;
                        } else if (variable.getKey().charAt(0) == 'T' && variable.getValue().timerType.equals("OFF") && variable.getValue().currentValue == false) {
                            variable.getValue().timer.start();
                        }
                    }
                    updateMode();
                    updateSceneUI();
                    updateMemoryVariables();
                } else {
                    ((Timer) evt1.getSource()).stop();
                }
            });

            timer.setInitialDelay(0); // começa sem atraso
            timer.start();
        } else {
            System.out.println("\nBotão stop clicado!");
            mode = 2;
            for (Map.Entry<String, MemoryVariable> variable : memoryVariables.entrySet()) {
                if (variable.getKey().charAt(0) == 'T') {
                    variable.getValue().timer.stop();
                }
            }
            updateMemoryVariables();
            updateMode();
        }
    }//GEN-LAST:event_startBtActionPerformed

    private void pauseBtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pauseBtActionPerformed
        mode = 1;
        for (Map.Entry<String, MemoryVariable> variable : memoryVariables.entrySet()) {
            if (variable.getKey().charAt(0) == 'T') {
                variable.getValue().counter = 0;
                variable.getValue().timer.stop();
            }
        }
        updateMemoryVariables();
        updateMode();
    }//GEN-LAST:event_pauseBtActionPerformed

    private void dataTableBtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dataTableBtActionPerformed
        tela2.setVisible(true);
        tela2.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        tela2.setLocation(1100, 0);
    }//GEN-LAST:event_dataTableBtActionPerformed

    private void setaCores() {
        simulationsComboBox.setBackground(Colors.firstColor(color));
        jPanel2.setBackground(Colors.firstColor(color));
        sceneContainer.setBackground(Colors.secondColor(color));
        Color_Camp.setBackground(Colors.thirdColor(color));
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

        aux = Language.getSimulação();
        simulationsComboBox.removeItemAt(0);
        simulationsComboBox.removeItemAt(0);
        simulationsComboBox.insertItemAt(aux.getItemAt(0).toString(), 0);
        simulationsComboBox.insertItemAt(aux.getItemAt(1).toString(), 1);
        simulationsComboBox.setSelectedIndex(0);

        Help_BT.setText(Language.getAjudar());
        Sobre_BT.setText(Language.getSobre());
        currentScenePanel.setLabels(Language.getEntradas(), Language.getSaidas());
        delayLabel.setText(Language.getDelay());
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HomePg.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        //</editor-fold>
        /* Create and display the form */
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
    private javax.swing.JButton Help_BT;
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
    private javax.swing.JComboBox<String> simulationsComboBox;
    private javax.swing.JButton startBt;
    // End of variables declaration//GEN-END:variables
}
