public class Fly extends Creature {
    public Fly(Animation leftAnim, Animation rightAnim, Animation deadLeftAnim, Animation deadRightAnim) {
        super(leftAnim, rightAnim, deadLeftAnim, deadRightAnim);
    }
    @Override
    public float getMaxSpeed() {
        return 0.2f;
    }
    @Override
    public boolean isFlying() {
        return isAlive();
    }
}
