package screens.scenes;

import java.util.Map;

import ilcompiler.input.Input.InputType;

public interface IScenePanel {

    public void updateUIState(Map<String, InputType> inputsType, Map<String, Boolean> inputs,
            Map<String, Boolean> outputs);

    public void setInputListener(InputEventListener listener);
}
