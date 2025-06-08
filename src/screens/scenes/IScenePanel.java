package screens.scenes;

import java.util.Map;

public interface IScenePanel {

    public void updateUIState(Map<String, Integer> inputsType, Map<String, Boolean> inputs, Map<String, Boolean> outputs);

    public void setInputListener(ScenePanelInputEventListener listener);
}
