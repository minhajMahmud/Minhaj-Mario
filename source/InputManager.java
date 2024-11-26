import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
public class InputManager implements KeyListener {
    public static Object cursior;
    public final GameAction[] keyActions = new GameAction[600];
    public final Component comp; 
    public InputManager(Component comp) {
        this.comp = comp;
        initializeComponentListeners();
    }
    public void initializeComponentListeners() {
        comp.addKeyListener(this);
        comp.setFocusTraversalKeysEnabled(false);
    }
    public void mapToKey(GameAction action, int keyCode) {
        keyActions[keyCode] = action;
    }
    public void clearMap(GameAction action) {
        for (int i = 0; i < keyActions.length; i++) {
            if (keyActions[i] == action) keyActions[i] = null;
        }
        action.reset();
    }
    public List<String> getMaps(GameAction action) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < keyActions.length; i++) {
            if (keyActions[i] == action) {
                list.add(KeyEvent.getKeyText(i));
            }
        }
        return list;
    }
    public void resetgame() {
        for (GameAction action : keyActions) {
            if (action != null) action.reset();
        }
    }
    @Override
    public void keyPressed(KeyEvent e) {
        handleKeyEvent(e, true);
    }
    @Override
    public void keyReleased(KeyEvent e) {
        handleKeyEvent(e, false);
    }
    @Override
    public void keyTyped(KeyEvent e) {
    }
    private void handleKeyEvent(KeyEvent e, boolean pressed) {
        int keyCode = e.getKeyCode();
        if (keyCode < keyActions.length) {
            GameAction action = keyActions[keyCode];
            if (action != null) {
                if (pressed) {
                    action.press();
                } else {
                    action.release();
                }
            }
        }
        e.consume();
    }
}
