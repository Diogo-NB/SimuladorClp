package ilcompiler.output;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// Classe para as ações relacionadas com as saídas
public class OutputActions {

    private static final List<String> OUTPUT_IDS = new ArrayList<>();

    static {
        for (int i = 0; i <= 7; i++) {
            OUTPUT_IDS.add("O1." + i);
        }
        for (int i = 0; i <= 7; i++) {
            OUTPUT_IDS.add("O0." + i);
        }
    }

    public static Map<String, Boolean> create(Map<String, Boolean> outputs) {
        for (String id : OUTPUT_IDS) {
            Output output = new Output(id, false);
            outputs.put(output.id, output.currentValue);
        }
        return outputs;
    }

    // "Limpa" values do hash de saída
    public static Map<String, Boolean> resetOutputs(Map<String, Boolean> outputs) {
        for (String id : OUTPUT_IDS) {
            outputs.put(id, false);
        }
        return outputs;
    }

    // Converte para inteiro considerando O1.7 ... O1.0 e O0.7 ... O0.0 como bits 15..0
    public static int convertValueWrite(Map<String, Boolean> outputs) {
        StringBuilder binaryString = new StringBuilder();

        for (String id : OUTPUT_IDS) {
            boolean bit = outputs.getOrDefault(id, false);
            binaryString.append(bit ? '1' : '0');
        }

        return Integer.parseInt(binaryString.toString(), 2);
    }
}
