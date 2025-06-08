package ilcompiler.input;

import java.util.Map;
import java.util.HashMap;
import Controllers.IOController;

public class InputActions {

    private static IOController ioController = new IOController();

    // Cria Map<String, Boolean> com as 16 entradas, inicializadas a partir do IOController
    public static Map<String, Boolean> create(Map<String, Boolean> inputs) {
        for (int i = 1; i <= 16; i++) {
            int word = (i - 1) / 8 + 1; // word 1 ou 2
            int bit = (i - 1) % 8;
            boolean value = ioController.getInputBit(word, bit);
            inputs.put("I" + i, value);
        }
        return inputs;
    }

    public static Map<String, Integer> createType(Map<String, Integer> inputsType) {
        for (int i = 1; i <= 16; i++) {
            inputsType.put("I" + i, 0);
        }
        return inputsType;
    }

    // Atualiza o Map inputs com o estado atual das palavras do IOController
    public static Map<String, Boolean> updateInputsMap(Map<String, Boolean> inputs) {
        for (int i = 1; i <= 16; i++) {
            int word = (i - 1) / 8 + 1;
            int bit = (i - 1) % 8;
            inputs.put("I" + i, ioController.getInputBit(word, bit));
        }
        return inputs;
    }

    // Atualiza o IOController a partir do Map recebido
    public static void updateWordsFromMap(Map<String, Boolean> inputs) {
        for (int i = 1; i <= 16; i++) {
            String key = "I" + i;
            Boolean value = inputs.get(key);
            if (value != null) {
                int word = (i - 1) / 8 + 1;
                int bit = (i - 1) % 8;
                ioController.setInputBit(word, bit, value);
            }
        }
    }

    // Leitura simples que retorna o Map atualizado com o estado das entradas
    public static Map<String, Boolean> read(Map<String, Boolean> inputs) {
        return updateInputsMap(inputs);
    }

    // Método para acessar o IOController de fora, se precisar
    public static IOController getIOController() {
        return ioController;
    }
}
