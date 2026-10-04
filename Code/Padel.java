import java.awt.*;

public class Padel {//I realized to late thats not how you spell padel oh well
    // padel object variables
    private int x;
    private int y;
    private Color color;
    private float vel;
    private int padelWidth;
    private int padelHeight;
    private float acel;
    private float maxVel;

    // only one padle so parameters are standard
    public Padel()
    {
        this.y = 800 - (this.padelHeight / 2);
        this.padelHeight = 10;
        this.padelWidth = 62;
        this.x = Main.xCenter - (this.padelWidth / 2);
        this.color = new Color(10, 133, 194);
        this.vel = 0.0f;
        this.acel = 1.25f;
        maxVel = 21.0f;
    }

    // draws paddle at its position
    public void draw(Graphics g)
    {
        g.setColor(this.color);
        g.fillRect(this.x,this.y,this.padelWidth,this.padelHeight);
    }
    // sets velocity to specific value
    public void setVel(float vel)
    {
        if (vel < maxVel && vel > -maxVel)
        this.vel = vel;
    }
    //acelerates padle to a top speed
    public void increaseVel(float vel)
    {
        if (this.vel + vel < maxVel && this.vel + vel > -maxVel )
        this.vel += vel;
    }
    // updates padle position and keeps it within screen
    public void move()
    {
        if( vel > 0.0f && this.x + padelWidth + vel >= Main.xGameBoundsRight -12)
        {
            vel = 0.0f;
            x = Main.xGameBoundsRight - 12 - padelWidth;
        }
        else if( vel < 0.0f && this.x + vel <= Main.xGameBoundsLeft +11)
        {
            vel = 0.0f;
            x = Main.xGameBoundsLeft +11;
        }
        else
            x += vel;

    }
    //mutator methods
    public void setX(int x){this.x = x;}
    public void setWidth(int padelWidth){this.padelWidth = padelWidth;}
    //acessor methods.
    public float getAcel(){return acel;}
    public int getX() {return this.x;}
    public int getY() {return this.y;}
    public int getHeight() {return this.padelHeight;}
    public int getWidth() {return this.padelWidth;}
    public float getVel() {return this.vel;}

}
