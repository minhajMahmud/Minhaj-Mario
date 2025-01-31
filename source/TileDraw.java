import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.Iterator;
public class TileDraw {
    public static final int tile = 64;
    public static final int tileSize = 6;
  private Image background;
    public static int pixelsToTiles(float pixels) {
        return pixelsToTiles(Math.round(pixels));
    } 
    public static int pixelsToTiles(int pixels) {
        return pixels >> tileSize;
    }
    public static int tilesToPixels(int numTiles) {
        return numTiles << tileSize;
    }
    public void setBackground(Image background) {
        this.background = background;
    }
    public void draw(Graphics2D g, TileMap map, int screenWidth, int screenHeight){
        Sprite player = map.getPlayer();
        int mapWidth = tilesToPixels(map.getWidth());
        int offsetX = screenWidth / 2 - Math.round(player.getX()) - tile;
        offsetX = Math.min(offsetX, 0);
        offsetX = Math.max(offsetX, screenWidth - mapWidth);
        int offsetY = screenHeight - tilesToPixels(map.getHeight());
        if (background == null || screenHeight > background.getHeight(null)) {
            g.setColor(Color.black);
            g.fillRect(0, 0, screenWidth, screenHeight);
        }
        if (background != null) {
    int x = offsetX *(screenWidth - background.getWidth(null)) /(screenWidth - mapWidth);
    int y = screenHeight - background.getHeight(null);
            g.drawImage(background, x, y, null);  
        }
        int firstTileX = pixelsToTiles(-offsetX);
        int lastTileX = firstTileX +
            pixelsToTiles(screenWidth) + 1;
        for (int y=0; y<map.getHeight(); y++) {
            for (int x=firstTileX; x <= lastTileX; x++) {
                Image image = map.getTile(x, y);
                if (image != null) {
                    g.drawImage(image,
                        tilesToPixels(x) + offsetX,
                        tilesToPixels(y) + offsetY,
                        null);
                }
            }
        }
        g.drawImage(player.getImage(),
            Math.round(player.getX()) + offsetX,
            Math.round(player.getY()) + offsetY,
            null);
        @SuppressWarnings("rawtypes")
        Iterator i = map.getSprites();
        while (i.hasNext()) {
            Sprite sprite = (Sprite)i.next();
            int x = Math.round(sprite.getX()) + offsetX;
            int y = Math.round(sprite.getY()) + offsetY;
            g.drawImage(sprite.getImage(), x, y, null);
            if (sprite instanceof Creature &&
                x >= 0 && x < screenWidth)
            {
                ((Creature)sprite).wakeUp();
            }
        }
    }

}
