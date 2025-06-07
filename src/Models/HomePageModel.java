package Models;

import ilcompiler.memoryvariable.MemoryVariable;
import java.util.HashMap;
import java.util.Map;

public class HomePageModel {

    private static Map<String, Integer> inputsType = new HashMap<>();
    private static Map<String, Boolean> inputs = new HashMap<>();
    private static Map<String, Boolean> outputs = new HashMap<>();
    private static Map<String, MemoryVariable> memoryVariables = new HashMap<>();
    private static Integer mode = 1;
    private static Integer color = 1;

    public static Map<String, Integer> getInputsType() {
        return inputsType;
    }

    public static void setInputsType(Map<String, Integer> map) {
        inputsType = map;
    }

    public static Map<String, Boolean> getInputs() {
        return inputs;
    }

    public static void setInputs(Map<String, Boolean> map) {
        inputs = map;
    }

    public static Map<String, Boolean> getOutputs() {
        return outputs;
    }

    public static void setOutputs(Map<String, Boolean> map) {
        outputs = map;
    }

    public static Map<String, MemoryVariable> getMemoryVariables() {
        return memoryVariables;
    }

    public static void setMemoryVariables(Map<String, MemoryVariable> map) {
        memoryVariables = map;
    }

    public static Integer getMode() {
        return mode;
    }

    public static void setMode(Integer newMode) {
        mode = newMode;
    }

    public static Integer getColor() {
        return color;
    }

    public static void setColor(Integer newColor) {
        color = newColor;
    }
}
