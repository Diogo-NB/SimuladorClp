package ilcompiler.input;

import java.util.*;

public class InputActions {

    private static final List<String> INPUT_IDS = new ArrayList<>();

    static {
        for (int i = 0; i <= 7; i++) {
            INPUT_IDS.add("I0." + i);
        }
        for (int i = 0; i <= 7; i++) {
            INPUT_IDS.add("I1." + i);
        }
    }

    public static Map<String, Boolean> create(Map<String, Boolean> inputs) {
        for (String id : INPUT_IDS) {
            Input input = new Input(id, false);
            inputs.put(input.id, input.currentValue);
        }
        return inputs;
    }

    public static Map<String, Integer> createType(Map<String, Integer> inputsType) {
        for (String id : INPUT_IDS) {
            inputsType.put(id, 0);
        }
        return inputsType;
    }

    public static Map<String, Boolean> read(Map<String, Boolean> inputs) {
        return inputs;
    }
}
