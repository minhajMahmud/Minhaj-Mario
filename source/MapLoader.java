
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
public class MapLoader 
{
    public ArrayList tiles;
    public int currentMap;
    public GraphicsConfiguration gc;
    public Sprite player;
    public Sprite bonus;
    public Sprite level;
    public Sprite grubIt;
    public Sprite flyIt;
    public MapLoader(GraphicsConfiguration gc) 
    {
        this.gc = gc;
        loadTileImages();
        loadCreatureSprites();
        loadPowerUpSprites();
    }
    public Image loadImage(String name) 
    {
        String filename = "images/" + name;
        return new ImageIcon(filename).getImage();
    }


    public Image getMirrorImage(Image image) 
    {
        return getScaledImage(image, -1, 1);
    }
    public Image getScaledImage(Image image, float x, float y) 
    {
        AffineTransform transform = new AffineTransform();
        transform.scale(x, y);
transform.translate((x-1) * image.getWidth(null) / 2,(y-1) * image.getHeight(null) / 2);
Image newImage = gc.createCompatibleImage(image.getWidth(null),image.getHeight(null),
Transparency.BITMASK);
        Graphics2D g = (Graphics2D)newImage.getGraphics();
        g.drawImage(image, transform, null);
        g.dispose();
        return newImage;
    }


    public TileMap loadNextMap() 
    {
        TileMap map = null;
        while (map == null) 
        {
            currentMap++;
            try {
                map = loadMap( "maps/map" + currentMap + ".txt");
            }
            catch (IOException ex) 
            {
                if (currentMap == 2) 
                {
                    return null;
                }
                  currentMap = 0;
                map = null;
            }
        }

        return map;
    }


    public TileMap reloadMap() 
    {
        try { return loadMap(
                "maps/map" + currentMap + ".txt");
        }
        catch (IOException e) {
            return null;
        }
    }
    public TileMap loadMap(String filename) throws IOException{
        ArrayList lines = new ArrayList();
        int width = 0;
        int height ;
        BufferedReader reader = new BufferedReader( new FileReader(filename));
        while (true) {
            String line = reader.readLine();
            if (line == null) {
                reader.close();
                break;
            }
            if (!line.startsWith("#")) {
                lines.add(line);
                width = Math.max(width, line.length());
            }
        }
        height = lines.size();
        TileMap newMap = new TileMap(width, height);
        for (int y=0; y<height; y++) {
            String line = (String)lines.get(y);
            for (int x=0; x<line.length(); x++) {
                char ch = line.charAt(x);
                int tile = ch - 'A';
                if (tile >= 0 && tile < tiles.size()) {
                    newMap.setTile(x, y, (Image)tiles.get(tile));
                }
                else if (ch == 'o') {
                    addSprite(newMap,bonus, x, y);
                }
                else if (ch == '*') {
                    addSprite(newMap, level, x, y);
                }
                else if (ch == '1') {
                    addSprite(newMap, grubIt, x, y);
                }
                else if (ch == '2') {
                    addSprite(newMap, flyIt, x, y);
                }
            }
        }
        Sprite player1 = (Sprite)player.clone();
        player1.setX(TileDraw.tilesToPixels(3));
        player1.setY(lines.size());
        newMap.setPlayer(player1);

        return newMap;
    }


    private void addSprite(TileMap map,
        Sprite hostSprite, int tileX, int tileY)
    {
        if (hostSprite != null) {
            Sprite sprite = (Sprite)hostSprite.clone();
 sprite.setX( TileDraw.tilesToPixels(tileX) + (TileDraw.tilesToPixels(1) -sprite.getWidth()) / 2);
sprite.setY(TileDraw.tilesToPixels(tileY + 1) -sprite.getHeight());
 map.addSprite(sprite);
        }
    } public void loadTileImages() {
        tiles = new ArrayList();
        char ch = 'A'; 
        while (true) 
        {String name = ch + ".png";
            File file = new File("images/" + name);
            if (!file.exists()) 
                break; 
            tiles.add(loadImage(name));
            ch++;
        }
    }
    public void loadCreatureSprites() 
    {

        Image[][] images = new Image[4][];
        images[0] = new Image[] {
            loadImage("minhaj.png"),         
            loadImage("enemy1.png"),
            loadImage("enemy1.png"),
            loadImage("enemy1.png"),
            loadImage("grub1.png"),
            loadImage("grub2.png"),
        };

        images[1] = new Image[images[0].length];
        images[2] = new Image[images[0].length];
        images[3] = new Image[images[0].length];
        
        for (int i=0; i<images[0].length; i++) 
        {
            images[1][i] = getMirrorImage(images[0][i]);
        }
        Animation[] playerAnim = new Animation[4];
        Animation[] flyAnim = new Animation[4];
        Animation[] grubAnim = new Animation[4];
        
        for (int i=0; i<4; i++) 
        {
            playerAnim[i] = MinhajPlayer (images[i][0]);
            flyAnim[i] = MinhajAnim (images[i][1], images[i][1], images[i][3]);
            grubAnim[i] = MinhajGrab (images[i][4], images[i][5]);
        }
        player = new Player (playerAnim[0], playerAnim[1],playerAnim[2], playerAnim[3]);
        flyIt = new Fly (flyAnim[0], flyAnim[1],flyAnim[2], flyAnim[3]);
        grubIt = new Grub (grubAnim[0], grubAnim[1],grubAnim[2], grubAnim[3]);
    }    public Animation MinhajPlayer(Image player)
    {
        Animation anim = new Animation();
        anim.addFrame(player, 250);
     
        return anim;
    }    private Animation MinhajAnim(Image img1, Image img2, Image img3)
    {
        Animation anim = new Animation();
        anim.addFrame(img1, 50);
        anim.addFrame(img2, 50);
        anim.addFrame(img3, 50);
        anim.addFrame(img2, 50);
        return anim;
    }
    private Animation MinhajGrab(Image img1, Image img2)
    {
        Animation anim = new Animation();
        anim.addFrame(img1, 250);
        anim.addFrame(img2, 250);
        return anim;
    }
    private void loadPowerUpSprites() 
    {
        Animation anim = new Animation();
        anim.addFrame(loadImage("heart.png"), 150);
        level= new PowerUp.Goal(anim);
        anim = new Animation();
        anim.addFrame(loadImage("bonus.png"),250 ) ;  
        
       bonus= new PowerUp.Star(anim);
        anim = new Animation();
    }

}
