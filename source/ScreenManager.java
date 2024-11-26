import java.awt.DisplayMode;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
public class ScreenManager {
    private final GraphicsDevice device;
    public ScreenManager() {
        device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    }
    public DisplayMode[] getCompatibleDisplayModes() {
        return device.getDisplayModes();
    }
    public DisplayMode findFirstCompatibleMode(DisplayMode[] modes) {
        for (DisplayMode mode : modes) {
            for (DisplayMode goodMode : device.getDisplayModes()) {
                if (displayModesMatch(mode, goodMode)) {
                    return mode;
                }
            }
        }
        return null;
    }

    public DisplayMode getCurrentDisplayMode() {
        return device.getDisplayMode();
    }
     boolean displayModesMatch(DisplayMode mode1, DisplayMode mode2) {
        return mode1.getWidth() == mode2.getWidth() && mode1.getHeight() == mode2.getHeight() &&(mode1.getBitDepth() == DisplayMode.BIT_DEPTH_MULTI ||mode2.getBitDepth() == DisplayMode.BIT_DEPTH_MULTI ||mode1.getBitDepth() == mode2.getBitDepth()) && (mode1.getRefreshRate() == DisplayMode.REFRESH_RATE_UNKNOWN || mode2.getRefreshRate() == DisplayMode.REFRESH_RATE_UNKNOWN ||
                mode1.getRefreshRate() == mode2.getRefreshRate());
    }
    public void setFullScreen(DisplayMode displayMode) {
        JFrame frame = createFullScreenFrame();
        device.setFullScreenWindow(frame);
        if (displayMode != null && device.isDisplayChangeSupported()) {
            try {
                device.setDisplayMode(displayMode);
            } catch (IllegalArgumentException ignored) {
            }
            frame.setSize(displayMode.getWidth(), displayMode.getHeight());
        }
        createBufferStrategy(frame);
    }
    public Graphics2D getGraphics() {
        Window window = device.getFullScreenWindow();
        if (window != null) {
            BufferStrategy strategy = window.getBufferStrategy();
            return (Graphics2D) strategy.getDrawGraphics();
        }
        return null;
    }
    public void update() {
        Window window = device.getFullScreenWindow();
        if (window != null) {
            BufferStrategy strategy = window.getBufferStrategy();
            if (!strategy.contentsLost()) {
                strategy.show();
            }
        }
        Toolkit.getDefaultToolkit().sync();
    }
    public void restoreScreen() {
        Window window = device.getFullScreenWindow();
        if (window != null) {
            window.dispose();
        }
        device.setFullScreenWindow(null);
    }
    public JFrame getFullScreenWindow() {
        return (JFrame) device.getFullScreenWindow();
    }
    public int getWidth() {
        Window window = device.getFullScreenWindow();
        return (window != null) ? window.getWidth() : 0;
    }
    public int getHeight() {
        Window window = device.getFullScreenWindow();
        return (window != null) ? window.getHeight() : 0;
    }
    public BufferedImage createCompatibleImage(int width, int height, int transparency) {
        Window window = device.getFullScreenWindow();
        if (window != null) {
            GraphicsConfiguration gc = window.getGraphicsConfiguration();
            return gc.createCompatibleImage(width, height, transparency);
        }
        return null;
    }
    private JFrame createFullScreenFrame() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(true);
        frame.setIgnoreRepaint(true);
        frame.setResizable(false);
        return frame;
    }
    private void createBufferStrategy(JFrame frame) {
        EventQueue.invokeLater(() -> frame.createBufferStrategy(2));
    }
}
