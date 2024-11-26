public final class GameAction {
    public static final int NORMAL = 0;
    public static final int INITIAL = 1;
    private static final int RELEASED = 0;
    public static final int PRESSED = 1;
    public static final int WAIT = 2;
    public String name;
    public int behavior;
    public int amount;
    public int state;
    public GameAction(String name) {
        this(name, NORMAL);
    }
    public GameAction(String name, int behavior) {
        this.name = name;
        this.behavior = behavior;
        reset();
    }
    public String getName() { 
        return name; 
    }
public void reset() {
        state = RELEASED;
        amount = 0;
    }
    public synchronized void tap() {
        press();
        release();
    }

    public synchronized void press() {
        press(1);
    }

    public synchronized void press(int amt) {
        if (state != WAIT) {
            amount += amt;
            state = PRESSED;
        }
    }
    public synchronized void release() {
        state = RELEASED;
    }

    public synchronized boolean isPressed() {
        return getAmount() != 0;
    }

    public synchronized int getAmount() {
        int currentAmount = amount;
        if (currentAmount != 0) {
            if (state == RELEASED || behavior == INITIAL) {
                state = (behavior == INITIAL) ?WAIT: RELEASED;
                amount = 0;
            }
        }
        return currentAmount;
    }
}
