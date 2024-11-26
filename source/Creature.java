public abstract class Creature extends Sprite {
    private static final int die = 1000;
    public static final int norm = 0;
    public static final int firstmara = 1;//inital mara
    public static final int finalmara = 2; //2nd time mara jowa
    public Animation left, right, deadLeft, deadRight;
    //public Animation left, right;
    public int state = norm;
    public long stateTime = 0;
    public Creature(Animation left, Animation right, Animation deadLeft, Animation deadRight) {
     super(right);
      this.left = left;
        this.right = right;
        this.deadLeft = deadLeft;
        this.deadRight = deadRight;
    }
    // public Creature(Animation left, Animation right) {
    //     super(right);
    //      this.left = left;
    //        this.right = right;
    //    }
    public Object clone() {
        try {
   return getClass().getConstructor(Animation.class, Animation.class,Animation.class, Animation.class).newInstance(left.clone(), right.clone(), deadLeft.clone(), deadRight.clone());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public abstract float getMaxSpeed();

    public void wakeUp() {
        if (state == norm && getVelocityX() == 0) {
            setVelocityX(-getMaxSpeed());
        }
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        if (this.state != state) {
            this.state = state;
            stateTime = 0;
            if (state == firstmara ) {
                setVelocityX(0);
                setVelocityY(0);
            }
        }
    }
    public boolean isAlive() {
        return state == norm;
    }

    public boolean isFlying() {
        return false;
    }
    public void collideHorizontal() {
        setVelocityX(-getVelocityX());
    }

    public void collideVertical() {
        setVelocityY(0);
    }

    @Override
    public void update(long elapsedTime) {
        Animation newAnim = getVelocityX() < 0 ? left : getVelocityX() > 0 ? right : anim;
        if (anim != newAnim) {
            anim = newAnim;
            anim.start();
        } else {
            anim.update(elapsedTime);
        }

        stateTime += elapsedTime;

        if (state == firstmara && stateTime >= die) {
            setState(finalmara);
        }
    }
}
