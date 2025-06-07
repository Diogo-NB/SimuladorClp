package ilcompiler.input;

import java.util.Map;

// Classe para as ações relacionadas com as entradas
public class InputActions {

    public static Map create(Map inputs) {
        for (int i = 1; i <= 16; i++) {
            String id = "I" + i;
            Input input = new Input(id, false);
            inputs.put(input.id, input.currentValue);
        }
        return inputs;
    }

    public static Map createType(Map inputsType) {
        for (int i = 1; i <= 16; i++) {
            String id = "I" + i;
            inputsType.put(id, 0);
        }
        return inputsType;
    }

    public static Map read(Map inputs) {

        return inputs;
    }

    /* Não está sendo usado, verificar possibilidade de remoção*/
//    // "Simula" leitura
//    public static Map<String, Boolean> dummyRead(Map<String, Boolean> inputs) {
//        int simulatedValue = 0x1234; // exemplo com 16 bits
//        boolean[] arrayBoolean = convertValueRead(simulatedValue);
//        System.out.println("[Dummy] Valor lido do módulo: " + simulatedValue);
//
//        for (int i = 0; i < 16; i++) {
//            inputs.put("I" + (i + 1), arrayBoolean[15 - i]); // MSB em I1
//        }
//
//        return inputs;
//    }
//
//    // Converte para boolean
//    public static boolean[] convertValueRead(int value) {
//        boolean[] bits = new boolean[16];
//        for (int i = 0; i < 16; i++) {
//            bits[i] = ((value >> i) & 1) == 1;
//        }
//        return bits;
//    }
}
