package ilcompiler.output;

import java.util.Map;

// Classe para as ações relacionadas com as saídas
public class OutputActions {

    public static Map create(Map outputs) {
        // Cria as 8 saídas
        Output Q1 = new Output("Q1", false);
        Output Q2 = new Output("Q2", false);
        Output Q3 = new Output("Q3", false);
        Output Q4 = new Output("Q4", false);
        Output Q5 = new Output("Q5", false);
        Output Q6 = new Output("Q6", false);
        Output Q7 = new Output("Q7", false);
        Output Q8 = new Output("Q8", false);

        // Adiciona no hash
        outputs.put(Q1.id, Q1.currentValue);
        outputs.put(Q2.id, Q2.currentValue);
        outputs.put(Q3.id, Q3.currentValue);
        outputs.put(Q4.id, Q4.currentValue);
        outputs.put(Q5.id, Q5.currentValue);
        outputs.put(Q6.id, Q6.currentValue);
        outputs.put(Q7.id, Q7.currentValue);
        outputs.put(Q8.id, Q8.currentValue);

        return outputs;
    }

    // "Limpa" values do hash de saída
    public static Map resetOutputs(Map outputs) {
        outputs.put("Q1", false);
        outputs.put("Q2", false);
        outputs.put("Q3", false);
        outputs.put("Q4", false);
        outputs.put("Q5", false);
        outputs.put("Q6", false);
        outputs.put("Q7", false);
        outputs.put("Q8", false);

        return outputs;
    }

    // Converte para inteiro
    public static int convertValueWrite(Map<String, Boolean> outputs) {
        StringBuilder binaryString = new StringBuilder();

        for (int i = 8; i >= 1; i--) {
            binaryString.append(outputs.getOrDefault("Q" + i, false) ? '1' : '0');
        }

        // Converte a string binária para inteiro
        return Integer.parseInt(binaryString.toString(), 2);
    }
}
