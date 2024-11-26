import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Window;

import javax.swing.ImageIcon;
public abstract class GameCore {
    private static final int FONT_SIZE = 18;
    private static final DisplayMode[] MODES = {
        new DisplayMode(800, 600, 32, 0),
        new DisplayMode(800, 600, 16, 0),
        new DisplayMode(800, 600, 24, 0),
        new DisplayMode(1024, 768, 16, 0),
        new DisplayMode(1024, 768, 32, 0),
        new DisplayMode(1024, 768, 24, 0)
    };
    public boolean running;
    protected ScreenManager screen;
    public void stop() {
        running = false;
    }

    public void run() {
        try {
            init();
            gameLoop();
        } finally {
            screen.restoreScreen();
            exitAfterDelay();
        }
    }

    private void exitAfterDelay() {
        new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ignored) {}
            System.exit(0);
        }).start();
    }
    public void init() {
        screen = new ScreenManager();
        DisplayMode mode = screen.findFirstCompatibleMode(MODES);
        screen.setFullScreen(mode);
        Window window = screen.getFullScreenWindow();
        window.setFont(new Font("Minhaj!Super Mario", Font.PLAIN, FONT_SIZE));
        window.setBackground(Color.BLACK);
        window.setForeground(Color.WHITE);
        running = true;
    }
    public Image loadImage(String fileName) {
        return new ImageIcon(fileName).getImage();
    }

    private void gameLoop() {
        long currTime = System.currentTimeMillis();

        while (running) {
            long elapsedTime = System.currentTimeMillis() - currTime;
            currTime = currTime +elapsedTime;
            update(elapsedTime);
            Graphics2D g = screen.getGraphics();
            draw(g);
            g.dispose();
            screen.update();
        }
    }public void update(long elapsedTime) {
    }
 public abstract void draw(Graphics2D g);
}
