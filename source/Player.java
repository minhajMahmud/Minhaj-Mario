public class Player extends Creature {
    private static final float jump = -0.95f;
    private static final float highspeed = 0.5f;   
    private boolean onGround; 
    public Player(Animation left, Animation right, Animation deadLeft, Animation deadRight) {
        super(left, right, deadLeft, deadRight);
    }
    // public Player(Animation left, Animation right) {
    //     super(left, right);
    // }
    public void collideHorizontal() {
        setVelocityX(0);
    }
    public void collideVertical() {
        if (getVelocityY() > 0) onGround = true; 
        setVelocityY(0);
    }
    public void setY(float y) {
        onGround = Math.round(y) > Math.round(getY()) ? false : onGround;
        super.setY(y);
    }
    @Override
    public void wakeUp() {
    }
    public void jump(boolean forceJump) {
        if (onGround || forceJump) {
            onGround = false;
            setVelocityY(jump);
        }
    }
    @Override
    public float getMaxSpeed() {
        return highspeed;
    }
}
