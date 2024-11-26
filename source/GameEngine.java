import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.Iterator;
public class GameEngine extends GameCore 
{    public static void main(String[] args)  {
        new GameEngine().run(); }
    public static final float GRAVITY = 0.002f;
    public Point pointCache = new Point();
    public TileMap map;
    public MapLoader mapLoader;
    public InputManager inputManager;
    public TileDraw drawer;
    public GameAction moveLeft;
    GameAction moveRight;
    public GameAction jump;
    public GameAction exit;
     int collectedStars=0;
     int numLives=6;
     
@Override
    public void init()
    {
        super.init();
        initInput();
        mapLoader = new MapLoader(screen.getFullScreenWindow().getGraphicsConfiguration());
        drawer = new TileDraw();
        drawer.setBackground(mapLoader.loadImage("bg.png"));
        map = mapLoader.loadNextMap();
    }
    
@Override
    public void stop() {
        super.stop();    
    }
    private void initInput() {
        moveLeft = new GameAction("L");
        moveRight = new GameAction("R");
        jump = new GameAction("lafh", GameAction.INITIAL);
        exit = new GameAction("exit",GameAction.INITIAL);   
        inputManager = new InputManager(screen.getFullScreenWindow());
        inputManager.mapToKey(moveLeft, KeyEvent.VK_LEFT);
        inputManager.mapToKey(moveRight, KeyEvent.VK_RIGHT);
        inputManager.mapToKey(jump, KeyEvent.VK_SPACE);
        inputManager.mapToKey(exit, KeyEvent.VK_ESCAPE);
    } 
    private void checkInput() 
    { if (exit.isPressed()) {
            stop();
        }
        Player player = (Player)map.getPlayer();
        if (player.isAlive()) 
        {
            float velocityX = 0;
            if (moveLeft.isPressed()) 
            {
                velocityX-=player.getMaxSpeed();
            }
            if (moveRight.isPressed()) {
                velocityX+=player.getMaxSpeed();
            }
            if (jump.isPressed()) {
                player.jump(false);
            }
            player.setVelocityX(velocityX);
        }  
    }  
@Override
    public void draw(Graphics2D g) {
        drawer.draw(g, map, screen.getWidth(), screen.getHeight());
        g.setColor(Color.WHITE);
        g.drawString("Press ESC for EXIT.",10.0f,20.0f);
        g.setColor(Color.GREEN);
        g.drawString("Bonus: "+collectedStars,300.0f,20.0f);
        g.setColor(Color.YELLOW);
        g.drawString("Lives: "+(numLives),500.0f,20.0f );
        g.setColor(Color.WHITE);
        g.drawString("Home: "+mapLoader.currentMap,700.0f,20.0f);     
    }
    public TileMap getMap() {
        return map;
    }
    public Point tileebadha(Sprite sprite, float newX, float newY) 
    {
        float fromX = Math.min(sprite.getX(), newX);
        float fromY = Math.min(sprite.getY(), newY);
        float toX = Math.max(sprite.getX(), newX);
        float toY = Math.max(sprite.getY(), newY);
        int fromTileX = TileDraw.pixelsToTiles(fromX);
        int fromTileY = TileDraw.pixelsToTiles(fromY);
        int toTileX = TileDraw.pixelsToTiles(toX + sprite.getWidth() - 1);
        int toTileY = TileDraw.pixelsToTiles(toY + sprite.getHeight() - 1);
        for (int x=fromTileX; x<=toTileX; x++) {
            for (int y=fromTileY; y<=toTileY; y++) {
                if (x < 0 || x >= map.getWidth() ||map.getTile(x, y) != null) {
                    pointCache.setLocation(x, y);
                    return pointCache;
                }
            }
        }
        return null;
    }
    public boolean isCollision(Sprite s1, Sprite s2) {
        if (s1 == s2) {
            return false;
        }
        if (s1 instanceof Creature && !((Creature)s1).isAlive()) {
            return false; }
        if (s2 instanceof Creature && !((Creature)s2).isAlive()) {
            return false;
        }
        int s1x = Math.round(s1.getX());
        int s1y = Math.round(s1.getY());
        int s2x = Math.round(s2.getX());
        int s2y = Math.round(s2.getY());
        return (s1x < s2x + s2.getWidth() && s2x < s1x + s1.getWidth() && s1y < s2y + s2.getHeight() &&
                s2y < s1y + s1.getHeight());
    }
    @SuppressWarnings("rawtypes")
    public Sprite getSpriteCollision(Sprite sprite) {
        Iterator i = map.getSprites();
        while (i.hasNext()) {
            Sprite otherSprite = (Sprite)i.next();
            if (isCollision(sprite, otherSprite)) {
                return otherSprite;
            }
        }
        return null;
    }
@Override
    public void update(long elapsedTime) {
        Creature player = (Creature)map.getPlayer();
        if (player.getState() == Creature.finalmara) {
            map = mapLoader.reloadMap();
            return;
        }
        checkInput();
        updateCreature(player, elapsedTime);
        player.update(elapsedTime);
        @SuppressWarnings("rawtypes")
        Iterator i = map.getSprites();
        while (i.hasNext()) {
            Sprite sprite = (Sprite)i.next();
            if (sprite instanceof Creature) {
                Creature creature = (Creature)sprite;
                if (creature.getState() == Creature.firstmara) {
                    i.remove();
                } else {
                    updateCreature(creature, elapsedTime);
                }
            }
            sprite.update(elapsedTime);
        }
    }
    private void updateCreature(Creature creature,
            long elapsedTime) {
        if (!creature.isFlying()) {
            creature.setVelocityY(creature.getVelocityY() + GRAVITY * elapsedTime);
        }

        float dx = creature.getVelocityX();
        float oldX = creature.getX();
        float newX = oldX + dx * elapsedTime;
        Point tile =
        tileebadha(creature, newX, creature.getY());
        if (tile == null) {
            creature.setX(newX);
        } else {
            if (dx > 0) {
                creature.setX(TileDraw.tilesToPixels(tile.x) -creature.getWidth());
            } else if (dx < 0) {
                creature.setX( TileDraw.tilesToPixels(tile.x + 1));
            }
            creature.collideHorizontal();
        }
        if (creature instanceof Player) {
      checkPlayerCollision((Player)creature, false);
        }
        float dy = creature.getVelocityY();
        float oldY = creature.getY();
        float newY = oldY + dy * elapsedTime;
        tile = tileebadha(creature, creature.getX(), newY);
        if (tile == null) {
            creature.setY(newY);
        } else {
            if (dy > 0) {
                creature.setY( TileDraw.tilesToPixels(tile.y) - creature.getHeight());
            } else if (dy < 0) {
                creature.setY(TileDraw.tilesToPixels(tile.y + 1));
            }
            creature.collideVertical();
        }
        if (creature instanceof Player player) {
            boolean canKill = (oldY < creature.getY());
            checkPlayerCollision(player, canKill);
        }
        
    }
    
    public void checkPlayerCollision(Player player,
            boolean canKill) {
        if (!player.isAlive()) {
            return;
        }
        Sprite collisionSprite = getSpriteCollision(player);
        if (collisionSprite instanceof PowerUp) {
            acquirePowerUp((PowerUp)collisionSprite);
        } else if (collisionSprite instanceof Creature) {
            Creature badguy = (Creature)collisionSprite;
            if (canKill) {
                badguy.setState(Creature.firstmara);
                player.setY(badguy.getY() - player.getHeight());
                player.jump(true);
            } else {
                player.setState(Creature.firstmara);
                numLives--;
                if(numLives==0) {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    stop();
                }
            }
        }
    }
     public void acquirePowerUp(PowerUp powerUp) {
        map.removeSprite(powerUp); 
        if (powerUp instanceof PowerUp.Star) {
            collectedStars++;
            if(collectedStars==100) 
            {
                numLives++;
                collectedStars=0;
            }
   
        } else if (powerUp instanceof PowerUp.Goal) {     
      
            map = mapLoader.loadNextMap();
            
        }
    }
    
      
}