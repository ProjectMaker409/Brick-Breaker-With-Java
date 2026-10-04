import java.awt.*;

// ball object class
public class Ball {
    private int x;
    private int y;
    private int dx;
    private int dy;
    private int imageWidth;
    private int imageHeight;
    private Image image;
    private boolean yesImage;
    private boolean multiHits;

    // full initalization
    public Ball(int x, int y, int dx, int dy, int imageWidth,int imageHeight, Image image, boolean yesImage, boolean multiHits)
    {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.imageWidth = imageWidth;
        this.image = image;
        this.yesImage = yesImage;
        this.imageHeight = imageHeight;
        this.multiHits = multiHits;
        

        
        
    }
    //standard setup most values are preset for simplicity
    public Ball(Image image, boolean multiHits)
    {
        // places it off screen
        this.x = 800;
        this.y = 500;
        this.dx = 2;
        this.dy = 5;
        this.imageWidth = 12;
        this.image = image;
        this.yesImage = false;
        this.imageHeight = 8;
        this.multiHits = multiHits;
    }

    // checks to see if ball colided with the paddle (side colisions also handeled)
    public void colidePadel( Padel padel)
    {
        //four if statments each for if ball colideds with paddle variables are updated if colision is detected
        if (dy >= 0 && this.y + this.imageHeight <= padel.getY() && 
           (this.x + imageWidth  >= padel.getX() && this.x  <= (padel.getX() + padel.getWidth())) && 
           (this.y + this.imageHeight + dy) >= (padel.getY())){
            dy = -dy;
            BrickBreakerGame.soundPadel(0);
            y = padel.getY() - imageHeight -1;
            
            updateX(padel);
            Brick.setHitable(true);
        }
        if (dy <= 0 && this.y >= padel.getY() + padel.getHeight() && 
        (this.x + imageWidth  >= padel.getX() && this.x  <= (padel.getX() + padel.getWidth())) && 
        (this.y + dy) - (padel.getY() + padel.getHeight()) <=0){
            dy = -dy;
            BrickBreakerGame.soundPadel(0);
            y = padel.getY() + padel.getHeight() +1;
            Brick.setHitable(true);
        }
        if (this.x + this.imageWidth <= padel.getX() && 
        (this.y + imageHeight >= padel.getY() && this.y <= (padel.getY() + padel.getHeight())) && 
        (this.x + this.imageWidth + dx) >= (padel.getX())){
            if (dx > 0 || padel.getVel() == 0)
                dx = -dx;
            dx = (int) (padel.getVel()*1.5);
            Brick.setHitable(true);
            BrickBreakerGame.soundPadel(0);
        }
        if (this.x + this.imageWidth >= padel.getX() && 
        (this.y + imageHeight >= padel.getY() && this.y <= (padel.getY() + padel.getHeight())) && 
        (this.x + dx) <= (padel.getX() + padel.getWidth())){
            if (dx < 0 || padel.getVel() == 0)
                dx = -dx;
            else
                dx = (int) (padel.getVel() *1.5);
            Brick.setHitable(true);
            BrickBreakerGame.soundPadel(0);
        }

    }
     
        // handles all colisions with the brick left right up down and updates values of ball and brick and score depending on direction
    public void colideBrick( Brick brick)
        {
            //variables updated diffrently depending on if multiHits is active
        if (!brick.getHit()){

          if (dy >= 0 && this.y + this.imageHeight <= brick.getY() && 
          (this.x + imageWidth  >= brick.getX() && this.x  <= (brick.getX() + brick.getWidth())) && 
          (this.y + this.imageHeight + dy) >= (brick.getY())){
            dy = -dy;
            y = brick.getY() -this.imageHeight -1;
            if(Brick.getHitable() == true){
            brick.hit();
            
            if (!multiHits)
            Brick.setHitable(false);
              brick.updateScoreAndFastestSpeed(this);
            }
          
            }
        if (dy <= 0 && this.y >= brick.getY() + brick.getHeight() && 
                (this.x + imageWidth  >= brick.getX() && this.x  <= (brick.getX() + brick.getWidth())) && 
                (this.y + dy) - (brick.getY() + brick.getHeight()) <=0){
                dy = -dy;
                y = brick.getY() + brick.getHeight() +1;
            if(Brick.getHitable() == true){
                brick.hit();
                
                if (!multiHits)
                    Brick.setHitable(false);
                brick.updateScoreAndFastestSpeed(this);
            }
            }
        if (dx >= 0 && this.x + this.imageWidth <= brick.getX() && 
                (this.y + imageHeight >= brick.getY() && this.y <= (brick.getY() + brick.getHeight())) && 
                (this.x + this.imageWidth + dx) >= (brick.getX())){
                dx = -dx;
                x = brick.getX() -this.imageWidth -1;
            if(Brick.getHitable() == true){
                brick.hit();
                
                if (!multiHits)
                    Brick.setHitable(false);
                brick.updateScoreAndFastestSpeed(this);
            }

            }
        if (dx <= 0 && this.x + this.imageWidth >= brick.getX() && 
            (this.y + imageHeight >= brick.getY() && this.y <= (brick.getY() + brick.getHeight())) && 
            (this.x + dx) <= (brick.getX() + brick.getWidth())){
            dx = -dx;
            x = brick.getX() + brick.getWidth() +1;
            if(Brick.getHitable() == true){
                    brick.hit();
                    if (!multiHits)
                        Brick.setHitable(false);
                    brick.updateScoreAndFastestSpeed(this);
                }

            }  
        }
    }
        

    // updates balls position and calculates for colisions with walls adjusts padel if lose is detected
    public void move(Padel padel)
    {
        x  += dx;
        y += dy;

        if(x + dx + imageWidth >= Main.xGameBoundsRight -12)
        {
            dx = -dx;
            x = Main.xGameBoundsRight - imageWidth -13;
            if(BrickBreakerGame.gameOn)
            BrickBreakerGame.soundWall(0);
        }
        if( x + dx <= Main.xGameBoundsLeft + 11)
        {
            dx = -dx;
            x = Main.xGameBoundsLeft +12;
            if(BrickBreakerGame.gameOn)
            BrickBreakerGame.soundWall(0);
        }
        if(y + dy + imageHeight >= Main.yGameBoundsBottom + 5)
        {
            loseSequence(padel);
            
        }
        //padel shrinks once it breaks throught bricks
        if(y + dy <= Main.yGameBoundsTop + 30)
        {
            dy = -dy;
            y = Main.yGameBoundsTop +31;
            Brick.setHitable(true);
            if(padel.getWidth() == 62){// orginial padel width so it only shrinks once after a breakout
            padel.setWidth(padel.getWidth() / 2);
            this.setSpeed(this.getDx(), 13);
            if(BrickBreakerGame.gameOn)
            BrickBreakerGame.soundWall(0);
            }
           //shrink
        }

        // for title and end screen animations
        
        if (this.y + imageHeight > padel.getY() && !BrickBreakerGame.gameOn)
            this.dy = -dy;
    }

    //draws the ball at its current position
    public void draw(Graphics g)
    {
         g.clearRect(x, y, imageWidth,imageHeight);
        if(yesImage){
            g.setColor(new Color(0, 0, 0));
            g.fillRect(x, y, imageWidth,imageHeight);
            g.drawImage(this.image, x , y, imageWidth,imageHeight,null);
        }
         else{
            g.setColor(new Color(210, 210, 210));
            g.fillRect(x, y, imageWidth,imageHeight);

         }
    }
    //adds velocity to ball position
    public void setSpeed(int dx, int dy)
    {
        this.dx = dx;
        this.dy = dy;
    }
    // updates ball horizontal velocity depnding where it hit the padel
    public void updateX(Padel padel)
    {
        int padelQuadrant = padel.getWidth() / 4;
        
        if(this.x + this.imageWidth <= padel.getX() + padelQuadrant)
        { 
            dx = -4 - (int) (Math.abs(dy) * 0.3);
        }
        else if(this.x + this.imageWidth <= padel.getX() + padelQuadrant*2)
        {
            dx = -2 - (int) (Math.abs(dy) * 0.15);
        }
        else if(this.x + this.imageWidth <= padel.getX() + padelQuadrant*3)
        {
           dx = 2 + (int) (Math.abs(dy) * 0.15);
        }
        else
        {
           dx = 4 + (int) ( Math.abs(dy) * 0.3);
        }
    }
    // serves the ball to the player when the previous ball goes off screen
    public void ballServe()
    {
        if (Math.random() > 0.5)
            this.setSpeed(-2, 5);
        else
            this.setSpeed(2, 5);
        this.x = (Main.xGameBoundsRight + Main.xGameBoundsLeft) / 2;  
        this.y = 350; 
         BrickBreakerGame.soundServe(0);
         BrickBreakerGame.soundBallHit(0);
         
    }
    // sets the ball position
    public void position(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
    //adjusts ball behaviour depending on if game is won or lost
    public void loseSequence(Padel padel)
    {
        BrickBreakerGame.lives--;
            if (BrickBreakerGame.lives > 0){
            this.ballServe();
            Brick.setNumHit(0);
            padel.setWidth(62);
            }
            if (BrickBreakerGame.lives == 0){
                padel.setX(Main.xGameBoundsLeft +11);
                padel.setWidth(Main.xGameBoundsRight - 12 - (Main.xGameBoundsLeft +11));
                this.x = 800;
                this.y = 500;
                this.dx = 8;
                this.dy = 7;
                Brick.setHitable(false);
                BrickBreakerGame.gameOn = false;
                BrickBreakerGame.lose = true;
                
            }
    }

//allows the ball to hit multiple bricks at once
public void setMultiHits(boolean multiHits)
{
    this.multiHits = multiHits;
}
// draws ball as specificed image vs a white rectangle
public void setYesImage(boolean yesImage){
    this.yesImage = yesImage;
}
    //accessor methods
    public boolean getMultiHits(){return this.multiHits;}
    public int getDx(){return this.dx;}
    public int getDy(){return this.dy;}
    public int getY() {return this.y;}
}
