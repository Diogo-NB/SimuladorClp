package screens.scenes;

import java.awt.event.MouseEvent;

public interface ScenePanelInputEventListener {
    void onPressed(String inputKey, MouseEvent evt);
    void onReleased(String inputKey, MouseEvent evt); 
}
