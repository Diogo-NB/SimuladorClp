package Controllers;

import ilcompiler.edit.Language;
import ilcompiler.memoryvariable.MemoryVariable;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.ArrayList;

import java.util.List;
import save.Save;
import screens.HomePg;

public class HomePageController {

    public static void updateTimerLabels(List<MemoryVariable> tVariables, List<JLabel> timerLabels, List<JLabel> currentLabels, List<JLabel> stopLabels) {
        for (int i = 0; i < tVariables.size(); i++) {
            MemoryVariable variable = tVariables.get(i);

            JLabel timer = timerLabels.get(i);
            JLabel current = currentLabels.get(i);
            JLabel stop = stopLabels.get(i);

            timer.setText(String.valueOf(variable.id));
            timer.setHorizontalTextPosition(JLabel.CENTER);
            timer.setVerticalTextPosition(JLabel.CENTER);
            timer.setForeground(Color.WHITE);

            current.setText(String.valueOf(variable.counter));
            current.setHorizontalAlignment(SwingConstants.CENTER);

            stop.setText(String.valueOf(variable.maxTimer));
            stop.setHorizontalAlignment(SwingConstants.CENTER);
        }
    }

    public static void updateCounterLabels(List<MemoryVariable> cVariables, List<JLabel> counterLabels, List<JLabel> currentLabels, List<JLabel> stopLabels) {
        for (int i = 0; i < cVariables.size(); i++) {
            MemoryVariable variable = cVariables.get(i);

            JLabel counter = counterLabels.get(i);
            JLabel current = currentLabels.get(i);
            JLabel stop = stopLabels.get(i);

            counter.setText(String.valueOf(variable.id));
            counter.setHorizontalTextPosition(JLabel.CENTER);
            counter.setVerticalTextPosition(JLabel.CENTER);
            counter.setForeground(Color.WHITE);

            current.setText(String.valueOf(variable.counter));
            current.setHorizontalAlignment(SwingConstants.CENTER);

            stop.setText(String.valueOf(variable.maxTimer));
            stop.setHorizontalAlignment(SwingConstants.CENTER);
        }
    }

    public static void handleFileArchiveAction(
            JComboBox<String> arquivarComboBox,
            JTextArea codigoCampTextArea,
            boolean updating,
            HomePg homePageInstance) {

        if (updating) {
            return;
        }

        if (Language.getArquivar().getItemAt(2).equals(arquivarComboBox.getSelectedItem().toString())) {
            JFileChooser c = new JFileChooser();
            String filename = "";
            String dir = "";
            int rVal = c.showOpenDialog(homePageInstance);
            if (rVal == JFileChooser.APPROVE_OPTION) {
                filename = c.getSelectedFile().getName();
                dir = c.getCurrentDirectory().toString();
            }
            List<String> memory = new ArrayList<>();
            try {
                memory = Save.load(dir + "\\" + filename);
                codigoCampTextArea.setText("");
                for (String line : memory) {
                    codigoCampTextArea.append(line);
                    codigoCampTextArea.append("\n");
                }
            } catch (IOException ex) {
                System.out.println("Erro ao carregar/salvar arquivo: " + ex.getMessage());
                ex.printStackTrace();  // Opcional: mostra o stack trace completo no console
            }
            arquivarComboBox.setSelectedIndex(0);
        }

        if (arquivarComboBox.getItemAt(1).equals(arquivarComboBox.getSelectedItem())) {
            arquivarComboBox.setSelectedIndex(0);

            JFileChooser c = new JFileChooser();
            String filename = "";
            String dir = "";
            int rVal = c.showSaveDialog(homePageInstance);
            if (rVal == JFileChooser.APPROVE_OPTION) {
                filename = c.getSelectedFile().getName();
                dir = c.getCurrentDirectory().toString();
            }

            List<String> memory = new ArrayList<>();
            memory = homePageInstance.saveLines(memory);  // supondo que saveLines está na HomePg

            try {
                Save.save(dir + "\\" + filename, memory);
            } catch (IOException ex) {
                System.out.println("Erro: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }
}
