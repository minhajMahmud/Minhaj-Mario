import java.awt.Image;
import java.util.ArrayList;
public class Animation {
    private ArrayList<AnimFrame> frames = new ArrayList<>();
    private int currFrameIndex = 0;
    private long animTime = 0;
    private long totalDuration = 0;
    public Animation() {
    }
    public Animation clone() {
        Animation clonedAnimation = new Animation();
        clonedAnimation.frames = new ArrayList<>(this.frames);
        clonedAnimation.totalDuration = this.totalDuration;
        return clonedAnimation;
    }
    public synchronized void addFrame(Image image, long duration) {
        totalDuration =  totalDuration +duration;
        frames.add(new AnimFrame(image, totalDuration));
    }
    public synchronized void start() {
        animTime = 0;
        currFrameIndex = 0;
    }
    public synchronized void update(long elapsedTime) {
        if (frames.size() > 1) {
            animTime = (animTime + elapsedTime) % totalDuration;

            while (animTime > frames.get(currFrameIndex).endTime) {
                currFrameIndex++;
            }
        }
    }

    public synchronized Image getImage() {
        return frames.isEmpty() ? null : frames.get(currFrameIndex).image;
    }

    public static class AnimFrame {
        Image image;
        long endTime;

        AnimFrame(Image image, long endTime) {
            this.image = image;
            this.endTime = endTime;
        }
    }
}
