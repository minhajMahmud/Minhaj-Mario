
public class Grub extends Creature {
    public Grub(Animation left, Animation right,
        Animation deadLeft, Animation deadRight)
    {
        super(left, right, deadLeft, deadRight);
    }
    // public Grub(Animation left, Animation right)
    // {
    //     super(left, right);
    // }
    public float getMaxSpeed() {
        return 0.05f;
    }

}
