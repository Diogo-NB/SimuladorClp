package ilcompiler.output;

import java.util.Map;
import java.util.HashMap;
import Controllers.IOController;
import ilcompiler.input.InputActions;

public class OutputActions {

    private static IOController ioController = InputActions.getIOController(); // Usa a mesma instância do IOController

    // Cria Map<String, Boolean> com as 16 saídas, inicializadas a partir do IOController
    public static Map<String, Boolean> create(Map<String, Boolean> outputs) {
        for (int i = 1; i <= 16; i++) {
            int word = (i - 1) / 8 + 1; // word 1 ou 2
            int bit = (i - 1) % 8;
            boolean value = ioController.getOutputBit(word, bit);
            outputs.put("Q" + i, value);
        }
        return outputs;
    }

    // "Limpa" os valores das saídas no Map e no IOController
    public static Map<String, Boolean> resetOutputs(Map<String, Boolean> outputs) {
        for (int i = 1; i <= 16; i++) {
            String id = "Q" + i;
            outputs.put(id, false);
            int word = (i - 1) / 8 + 1;
            int bit = (i - 1) % 8;
            ioController.setOutputBit(word, bit, false);
        }
        return outputs;
    }

    // Atualiza o Map outputs com o estado atual das palavras do IOController
    public static Map<String, Boolean> updateOutputsMap(Map<String, Boolean> outputs) {
        for (int i = 1; i <= 16; i++) {
            int word = (i - 1) / 8 + 1;
            int bit = (i - 1) % 8;
            outputs.put("Q" + i, ioController.getOutputBit(word, bit));
        }
        return outputs;
    }

    // Atualiza o IOController a partir do Map recebido
    public static void updateWordsFromMap(Map<String, Boolean> outputs) {
        for (int i = 1; i <= 16; i++) {
            String key = "Q" + i;
            Boolean value = outputs.get(key);
            if (value != null) {
                int word = (i - 1) / 8 + 1;
                int bit = (i - 1) % 8;
                ioController.setOutputBit(word, bit, value);
            }
        }
    }

    // Mantém método de conversão para inteiro, se quiser usar
    public static int convertValueWrite(Map<String, Boolean> outputs) {
        StringBuilder binaryString = new StringBuilder();

        for (int i = 16; i >= 1; i--) {
            binaryString.append(outputs.getOrDefault("Q" + i, false) ? '1' : '0');
        }

        return Integer.parseInt(binaryString.toString(), 2);
    }
}
