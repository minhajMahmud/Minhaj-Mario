import java.awt.Image;
import java.util.Iterator;
import java.util.LinkedList;
public class TileMap {
    public Image[][] tiles;
    public  LinkedList sprites;
   public  Sprite player;
    public TileMap(int width, int height) {
        tiles = new Image[width][height];
        sprites = new LinkedList();
    }
    public int getWidth() {
        return tiles.length;
    } 
    public int getHeight() {
        return tiles[0].length;
    }
      public Image getTile(int x, int y) {
        if (x < 0 || x >= getWidth() || y < 0 || y >= getHeight()){
            return null;
        }else {
            return tiles[x][y];
        }
    }
    public void setTile(int x, int y, Image tile) {
        tiles[x][y] = tile;
    }
    public Sprite getPlayer() {
        return player;
    }
    public void setPlayer(Sprite player) {
        this.player = player;
    }
 @SuppressWarnings("unchecked")
public void addSprite(Sprite sprite) {
        sprites.add(sprite);
    } public void removeSprite(Sprite sprite) {
        sprites.remove(sprite);
    }

    @SuppressWarnings("rawtypes")
    public Iterator getSprites() {
        return sprites.iterator();
    }

}
