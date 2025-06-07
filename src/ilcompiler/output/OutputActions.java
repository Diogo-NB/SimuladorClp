package ilcompiler.output;

import java.util.Map;

// Classe para as ações relacionadas com as saídas
public class OutputActions {

    public static Map create(Map outputs) {
        for (int i = 1; i <= 16; i++) {
            String id = "Q" + i;
            Output output = new Output(id, false);
            outputs.put(output.id, output.currentValue);
        }
        return outputs;
    }

    // "Limpa" values do hash de saída
    public static Map resetOutputs(Map outputs) {
        for (int i = 1; i <= 16; i++) {
            String id = "Q" + i;
            outputs.put(id, false);
        }
        return outputs;
    }

    // Converte para inteiro
    public static int convertValueWrite(Map<String, Boolean> outputs) {
        StringBuilder binaryString = new StringBuilder();

        for (int i = 16; i >= 1; i--) {
            binaryString.append(outputs.getOrDefault("Q" + i, false) ? '1' : '0');
        }

        // Converte a string binária para inteiro
        return Integer.parseInt(binaryString.toString(), 2);
    }
}
