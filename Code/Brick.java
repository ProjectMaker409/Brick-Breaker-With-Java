import java.awt.*;

public class Brick {
    // brick object variables
    private int x;
    private int y;
    private Color color;
    private int width;
    private int height;
    private boolean hit;
    private static boolean hitable;
    private static int numBricks;
    private static int numHit;

    // initalizes brick object (some values are default and do not need to be variable)
    public Brick(int x, int y, Color color)
    {
        this.x = x;
        this.y = y;
        this.width = 40;
        this.height = 10;
        this.color = color;
        this.hit = false;
        Brick.hitable = true;
        numBricks++;

    }
    // updates player score based off which color ball is hit and updates speed if orange of red bricks hit
    public void updateScoreAndFastestSpeed(Ball  ball)
    {
        int multiplier = 0;
        if (this.color.getRGB() == new Color(163, 30, 10).getRGB()){
            multiplier = 7;
            ball.setSpeed(ball.getDx(), 14);
            BrickBreakerGame.soundBallHit(multiplier-1);
            
        }
        else if (this.color.getRGB() == new Color(194, 133, 10).getRGB()){
            multiplier = 5;
            ball.setSpeed(ball.getDx(), 14);
            BrickBreakerGame.soundBallHit(multiplier-1);
        }
        else if (this.color.getRGB() == new Color(10, 133, 51).getRGB()){
            multiplier = 3;
           BrickBreakerGame.soundBallHit(multiplier-1);
        }
        else if(this.color.getRGB() == new Color(194, 194, 41).getRGB()){
            multiplier = 1;
            BrickBreakerGame.soundBallHit(multiplier-1);
        }


        BrickBreakerGame.updateScore(multiplier);
        
    }

    // if the brick has not been hit it is drawn
    public void draw(Graphics g)
    {
        if (!hit){
        g.setColor(this.color);
        g.fillRect(x, y, width, height);
        }
        
    }
    //accesor methods
    public int getX(){return this.x;}
    public int getY(){return this.y;}
    public int getWidth(){return this.width;}
    public int getHeight(){return this.height;}
    public boolean getHit(){return this.hit;}
    public static boolean getHitable(){return Brick.hitable;}
    public static int getNumHit(){return Brick.numHit;}

    //updates the number of bricks hit
    public void hit(){
        hit = true;
        numHit++;
    }
    // set bricks to be hitable since usely only one brick can be broken per ball hit off padel
    public static void setHitable(boolean hitable){Brick.hitable = hitable;}

    //mutator method for numHit
    public static void setNumHit(int numHit){Brick.numHit = numHit;} 
}