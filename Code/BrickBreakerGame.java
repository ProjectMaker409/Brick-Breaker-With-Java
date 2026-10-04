import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.imageio.*;
import java.io.*;
import javax.sound.sampled.*;

import java.util.ArrayList;
import java.util.Scanner;


public class BrickBreakerGame extends JPanel implements MouseListener, ActionListener, KeyListener, MouseMotionListener
{
    //backgrounds
    static Image image1;
    static Image image2;
    static Image image3;
    static Image image4;
    static Image image5;
    static Image image6;

    // pacman easter egg image and images of numbers for socre board
    static Image ballImage;
    static Image nine;
    static Image eight;
    static Image seven;
    static Image six;
    static Image five;
    static Image four;
    static Image three;
    static Image two;
    static Image one;
    static Image zero;
    //list of number image for ease of access
    static ArrayList<Image> numberImages;

    //list of players for 3 ball mode high score aswell as the file and scaner used to save them
    static ArrayList<Player> player3Ball = new ArrayList<>();
    static File ball3File;
    static Scanner scan3;

    //list of players for 5 ball mode high score aswell as the file and scaner used to save them
    static ArrayList<Player> player5Ball = new ArrayList<>();
    static File ball5File;
    static Scanner scan5;

    //feild so user can enter name
    JTextField nameInput;
    String playerName = "";


    // sets up keys padel bricks and 
    static boolean[] keys = new boolean[200];
    static Ball ball;
    static Padel padel;
    static Timer timer;
    static Brick[][] bricks = new Brick[14][8];

    //game variables
    static int score = 0;
    static int frameNum = 0;
    static int lives = 0;
    static int imageNum = 0;//for backgrounds

    //boolean values allowing diffrent methods to be called based on state of the game
    static boolean gameOn = false;
    static boolean fiveBall = false;
    static boolean lose = false;
    static boolean fileWrite = true;
    static boolean nameEntered = false;

    public BrickBreakerGame()
    {
        addMouseListener(this);
        addKeyListener(this);
        addMouseMotionListener(this);
        //loads images
        try{
            ballImage = ImageIO.read(new File("Backgrounds\\Ball.png"));
            image1 = ImageIO.read(new File("Backgrounds\\Generic.png"));
            image2 = ImageIO.read(new File("Backgrounds\\Generic2.png"));
            image3 = ImageIO.read(new File("Backgrounds\\PacMan.png"));
            image4 = ImageIO.read(new File("Backgrounds\\PacMan4.png"));
            image5 = ImageIO.read(new File("Backgrounds\\image5.jpg"));
            image6 = ImageIO.read(new File("Backgrounds\\image6.jpg"));
            nine = ImageIO.read(new File("Numbers\\Nine.png"));
            eight = ImageIO.read(new File("Numbers\\Eight.png"));
            seven = ImageIO.read(new File("Numbers\\Seven.png"));
            six = ImageIO.read(new File("Numbers\\Six.png"));
            five = ImageIO.read(new File("Numbers\\Five.png"));
            four = ImageIO.read(new File("Numbers\\Four.png"));
            three = ImageIO.read(new File("Numbers\\Three.png"));
            two = ImageIO.read(new File("Numbers\\Two.png"));
            one = ImageIO.read(new File("Numbers\\One.png"));
            zero = ImageIO.read(new File("Numbers\\Zero.png"));

             numberImages = new ArrayList<>();
                numberImages.add(zero);
                numberImages.add(one);
                numberImages.add(two);
                numberImages.add(three);
                numberImages.add(four);
                numberImages.add(five);
                numberImages.add(six);
                numberImages.add(seven);
                numberImages.add(eight);
                numberImages.add(nine);

            //initalizes file readers and writers
            ball5File = new File("Ball5.txt");
            scan5 = new Scanner(ball5File);

            ball3File = new File("Ball3.txt");
            scan3 = new Scanner(ball3File);

            //reads the top socrers into player array list
            readFile(scan5, player5Ball);
            readFile(scan3, player3Ball);
               
        }
        catch(IOException e)
        {
                
        }
        // sets up timer object and other objects
        ball = new Ball(ballImage,false); 
        timer = new Timer(10, this); // calls the action performed method every 10ms
        padel = new Padel();
        Color brickColor; 

       //creates 2d array of brick objects of diffrent colors depending on row.
       for(int i = 0; i < bricks.length; i++)
       {
        for(int j = 0; j < bricks[0].length; j++){
            if(j <= 1)
               brickColor = new Color(163, 30, 10);
            else if(j >= 2 && j <= 3)
                brickColor = new Color(194, 133, 10);
            else if (j >= 4 && j <= 5)
                brickColor = new Color(10, 133, 51);
            else 
                brickColor = new Color(194, 194, 41);

            bricks[i][j] = new Brick(Main.xGameBoundsLeft + 11 + (i*(40+5)),200 + (j*14),brickColor);
        }
       }

       setFocusable(true);
       setFocusTraversalKeysEnabled(false);
       timer.start();

        // Setup Name Input Box
        nameInput = new JTextField(8);
        nameInput.setText("");
        nameInput.setBounds((Main.xGameBoundsRight + Main.xGameBoundsLeft) /2  -40, 420, 70, 30);
        //nameInput.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        nameInput.setHorizontalAlignment(JTextField.CENTER);
        nameInput.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
        nameInput.setForeground(new Color(210, 210, 0));
        nameInput.setOpaque(false);
        nameInput.setVisible(false);
        this.add(nameInput);

    }
    // performs actions every clock tick
    public void actionPerformed(ActionEvent e)
    {
      // draws game
       repaint();
      // updates variables
       update();
       frameNum++;
       

    }

    //updates game variables every clock tick including user input from keys
    public void update()
    {
        updateFileAccess();
        
        if(!gameOn) 
           updateTitleScreen();

    // updates padel speed based on key press      
    else if(gameOn){
            if(keys[KeyEvent.VK_RIGHT] || keys[KeyEvent.VK_D])
            {
                padel.increaseVel(padel.getAcel());
            }
            
            if(keys[KeyEvent.VK_LEFT] || keys[KeyEvent.VK_A])
            {
                padel.increaseVel(-padel.getAcel());
            }

            
            // updates game objects position and colisions with each other
            padel.move();
            
            ball.colidePadel(padel);

            ball.move(padel);

            brickColisions();
            
        // increases the balls speed as more bricks are hit t
        // the fastest speed when red or orange blocks are hit is handeled in the brick class by updateScoreAndFastestSpeed()
            if (ball.getDy() > 0 && ball.getDy() <= 12){
        if(Brick.getNumHit()>= 12)
            ball.setSpeed(ball.getDx(), 12);
        else if(Brick.getNumHit()>= 4)
            ball.setSpeed(ball.getDx(), 8);
            }
    }

}
// writes to file after a name is inputed once a top score is achived, file is only written to once
public void updateFileAccess()
{
    if(lose){
            if (playerName.length() == 5)
                nameEntered = true;
            
            if (!fiveBall && score < player3Ball.get(0).getScore() && player3Ball.size() >= 10 || score == 0)
                nameEntered = true;
            else if(fiveBall && score < player5Ball.get(0).getScore() && player5Ball.size() >= 10 || score == 0)
                nameEntered = true;
        
            playerName = nameInput.getText();
        if(fileWrite && lose && nameEntered){
                try {
                    if(fiveBall){
                    readFile(scan5, player5Ball);
                    writeFile("Ball5.txt", player5Ball);
                    }
                    else{
                        readFile(scan3, player3Ball);
                        writeFile("Ball3.txt",player3Ball);
                    }
                } catch (Exception e) {
                    
                }
                fileWrite = false;
            }
            }
}
// updates the screen the player can see
public void updateTitleScreen()
    {
         // restarts game variables and set game to 3 ball mode
        if((keys[KeyEvent.VK_3] && !lose) || (lose && keys[KeyEvent.VK_3] && nameEntered ))
            {
                gameOn = true;
                fileWrite = true;
                lives = 3;
                padel.setWidth(62);
                padel.setX((Main.xGameBoundsRight - 12 + (Main.xGameBoundsLeft +11)) / 2);
                fiveBall = false;
                score = 0;
                lose = false;
                BrickBreakerGame.resetBricks();
                 Brick.setNumHit(0);
                ball.ballServe();
                nameEntered = false;
                playerName = "";
                nameInput.setText("");
               
            }
            // restarts game variables and set game to 5 ball mode
            if ((keys[KeyEvent.VK_5] && !lose) || (lose && keys[KeyEvent.VK_5] && nameEntered ))
            {
                gameOn = true;
                fileWrite = true;
                lives = 5;
                padel.setWidth(62);
                padel.setX((Main.xGameBoundsRight - 12 + (Main.xGameBoundsLeft +11)) / 2);
                fiveBall = true;
                score = 0;
                lose = false;
                BrickBreakerGame.resetBricks();
                Brick.setNumHit(0);
                ball.ballServe();
                nameEntered = false;
                playerName = "";
                 nameInput.setText("");
                
            }
            
            
            if(!gameOn){
            padel.setX(Main.xGameBoundsLeft +11);
            padel.setWidth(Main.xGameBoundsRight - 12 - (Main.xGameBoundsLeft +11));
            Brick.setHitable(false);
            ball.move(padel);
            brickColisions();

            }
             
    }
    // loops throught 2d array of bricks to call colision method which checks for colisions
     public void brickColisions()
    {
        //depending on where the ball is so closer bricks check for colisions first to guard against "phase thru" (my term for it, not technical)
        if (ball.getY() >= bricks[0][0].getHeight())
            {
                for (int i = 0 ; i <  bricks.length; i++)
                {
                    for (int j = 0; j < bricks[0].length; j++){
                        if(!bricks[i][j].getHit())
                        ball.colideBrick(bricks[i][j]);
                    }
                }
            }
            else{
                for(int i = bricks.length -1; i >= 0; i--)
            {
                    for(int j = bricks[0].length -1; j >= 0; j--){
                        if(!bricks[i][j].getHit())
                        ball.colideBrick(bricks[i][j]);
                    }
            }
        }
    }

//updates score
 public static void updateScore(int n )
    {
        score += n;
    }

    //resets bricks to start a new game
    public static void resetBricks()
    {
        Color brickColor; 

       for(int i = 0; i < bricks.length; i++)
       {
        for(int j = 0; j < bricks[0].length; j++){
            if(j <= 1)
               brickColor = new Color(163, 30, 10);
            else if(j >= 2 && j <= 3)
                brickColor = new Color(194, 133, 10);
            else if (j >= 4 && j <= 5)
                brickColor = new Color(10, 133, 51);
            else 
                brickColor = new Color(194, 194, 41);

            bricks[i][j] = new Brick(Main.xGameBoundsLeft + 11 + (i*(40+5)),200 + (j*14),brickColor);
        }
    }
}
    
    //redraws objects every game cycles
    public void paintComponent(Graphics g)
    {
        drawBackgrounds(g);

        if(!gameOn)
        {
            
            drawNumberScore(score, g,numberImages);
            drawLives(lives,g,numberImages);
            drawTitleScreen(g);
        }

        if (gameOn){
        if(frameNum % 16 >= 8)
        drawNumberScore(score, g,numberImages);

        drawLives(lives,g,numberImages);

        if(fiveBall)
                drawHighScoreDuringGame(g, player5Ball, numberImages);
            else
                drawHighScoreDuringGame(g, player3Ball, numberImages);

        }

        ball.draw(g);
        padel.draw(g);

        for(int i = 0; i < bricks.length; i++)
       {
        for(int j = 0; j < bricks[0].length; j++){
        bricks[i][j].draw(g);
        }
      }
      
    }
//draws title screen when game is not on
    public void drawTitleScreen(Graphics g)
    {
        ball.draw(g);

        if (lose){

            drawLoseScreen(g);
        }
        if (ball.getMultiHits())
        {
            Font font = new Font(Font.MONOSPACED, Font.BOLD, 20);
            g.setFont(font);
            g.setColor(Color.yellow);
            String text = "Multi Hits active";
            g.drawString(text, (Main.xGameBoundsRight + Main.xGameBoundsLeft) /2  -90, 90);
        }
    }

// draws screen whe game is lost and alows for game to be replayed
    public void drawLoseScreen(Graphics g)
    {   
        Font font = new Font(Font.MONOSPACED, Font.BOLD, 20);
        g.setFont(font);
        g.setColor(Color.yellow);
        ball.draw(g);
        String text = "NEW TOP SCORE ENTER 5 LETTERS NAME!";
        // if player did not put in their name but has a high score draws the text feild too put it in
        if(!nameEntered){
            nameInput.setVisible(true);
            g.setColor(new Color(230,230,0));
            g.drawString(text, (Main.xGameBoundsRight + Main.xGameBoundsLeft) /2  -210, 390);

        }
        //draws high score list
        else{
        g.setColor(Color.white);
        text = "HIGH SCORES";
        g.drawString(text, (Main.xGameBoundsRight + Main.xGameBoundsLeft) /2  -70, 390);
        if(fiveBall && nameEntered)
            drawHighScore(g, player5Ball);
        else if (nameEntered)
            drawHighScore(g, player3Ball);
        }
    }

    // draws the top 10 scores of players
    public void drawHighScore(Graphics g, ArrayList<Player> list)
    {
        if (list.size() > 10){
        for(int i = list.size() -1; i >= list.size() - 10 ; i--)
        g.drawString(list.get(i).getName() + " " + list.get(i).getScore(), (Main.xGameBoundsRight + Main.xGameBoundsLeft) /2  -70, 390 + 40 *((list.size() - i)));
        }
        else{
            for (int i = list.size() -1; i >= 0 ; i--){
            g.drawString(list.get(i).getName() + " " + list.get(i).getScore(), (Main.xGameBoundsRight + Main.xGameBoundsLeft) /2  -70, 390 + 40 *((list.size() - i)));
            }
        }
        
    }
    
    //reads file and copys it to 
    public void readFile(Scanner scan, ArrayList<Player> list)
    {
        while(scan.hasNext()){
            list.add(new Player(scan.next(), scan.nextInt()));

        }
        Player.sortPlayers(list);
    }
    // copies file to array list and sorts it then writes to file and adds user who just played a game if their score is high 
    public void writeFile(String fileName, ArrayList<Player> list) throws IOException 
    { 
        FileWriter fw = new FileWriter(fileName);
        PrintWriter pw = new PrintWriter(fw);
        if (list.size() < 10  && score != 0)
            list.add(new Player(playerName, score));
        else if (score >= list.get(0).getScore() && score != 0)
            list.add(new Player(playerName, score));
        Player.sortPlayers(list);
        pw.print(Player.copyPlayers(list));
        pw.close();
        nameInput.setVisible(false);
        nameEntered = true;

    }

    // draws side images depending on which one is chosen as well as other backgrounds
    public void drawBackgrounds (Graphics g)
    {
        if (imageNum % 6 == 0){
            g.drawImage(image1, 0, 0, Main.width, Main.height, null);
            ball.setYesImage(false);
        }
        else if (imageNum % 6 == 1){
             g.drawImage(image2, 0, 0, Main.width, Main.height, null);
             ball.setYesImage(false);
        }
        else if (imageNum % 6 == 2){
             g.drawImage(image3, 0, 0, Main.width, Main.height, null);
             ball.setYesImage(false);
        }
        else if (imageNum % 6 == 3){
            g.drawImage(image4, 0, 0, Main.width, Main.height, null);
            ball.setYesImage(true);
        }
        else if (imageNum % 6 == 4){
            g.drawImage(image5, 0, 0, Main.width, Main.height, null);
            ball.setYesImage(false);
        }
        else if (imageNum % 6 == 5){
            g.drawImage(image6, 0, 0, Main.width, Main.height, null);
            ball.setYesImage(false);
        }
        
        
        g.setColor(new Color(0,0,0));
        g.fillRect(Main.xGameBoundsLeft, Main.yGameBoundsTop, Main.gameWidth, Main.yGameBoundsBottom);
        g.setColor(new Color(210, 210, 210));
        g.fillRect(Main.xGameBoundsLeft, Main.yGameBoundsTop, 11, Main.yGameBoundsBottom);
        g.fillRect(Main.xGameBoundsLeft, Main.yGameBoundsTop, Main.gameWidth, 30);
        g.fillRect(Main.xGameBoundsRight - 12, Main.yGameBoundsTop, 12, Main.yGameBoundsBottom);
    }
    // draws players current score
    public void drawNumberScore(int score, Graphics g,ArrayList<Image> images)
    {
        int xPos = Main.xGameBoundsLeft +132;
        int yPos = 120;
        int height = 6*14;
        int width = 4*10;
        //fun fact you dont need to re declare the type for the new ArrayList part

        g.drawImage(images.get(score % 10), xPos+100, yPos, width , height, null);
        g.drawImage(images.get((score / 10) % 10), xPos+50, yPos, width , height, null);
        g.drawImage(images.get((score / 100) % 10), xPos, yPos, width, height, null);
        

    }

    //displays the highest score for the game mode during play
    public void drawHighScoreDuringGame(Graphics g, ArrayList<Player> list, ArrayList<Image> images)
    {
        int xPos = Main.xGameBoundsRight -132 - (100);
        int yPos = 120;
        int height = 6*14;
        int width = 4*10;
        //fun fact you dont need to re declare the type for the new ArrayList part
        int number = list.get(list.size()-1).getScore();
        g.drawImage(images.get( number % 10), xPos+100, yPos, width , height, null);
        g.drawImage(images.get((number / 10) % 10), xPos+50, yPos, width , height, null);
        g.drawImage(images.get((number / 100) % 10), xPos, yPos, width, height, null);
        

    }

    // draws lives left
    public void drawLives(int lives, Graphics g,ArrayList<Image> images)
    {
        int xPos = Main.xGameBoundsLeft +80;
        int yPos = 35;
        int height = 6*14;
        int width = 4*10;
        int gameMode;
        if (fiveBall)
            gameMode = 5;
        else
            gameMode = 3;

        if (gameOn)
        g.drawImage(images.get(lives), xPos, yPos, width , height, null);
        if (lose || gameOn)
        g.drawImage(images.get(gameMode), xPos+500, yPos, width , height, null);
        

    }


    //sound methods from "Bro Code" https://www.youtube.com/watch?v=SyZQVJiARTQ

    //play sound of ball hitting brick played n times depnding how much points the brick is worth
    public static void soundBallHit(int n){
        try {
            File file = new File("Sounds\\brick.wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            
            clip.loop(n);


            
        } catch (Exception e) {
            
        }
    }

    //play sound of brick hitting padel
    public static void soundPadel(int n){
        try {
            File file = new File("Sounds\\padel.wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            
            clip.loop(n);
        } catch (Exception e) {
            
        }
    }

    //sound of ball hitting wall
    public static void soundWall(int n){
        try {
            File file = new File("Sounds\\wall.wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            
            clip.loop(n);
        } catch (Exception e) {
            
        }
    }
    // plays sound when ball is served
    public static void soundServe(int n){
        try {
            File file = new File("Sounds\\ballServe.wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            
            clip.loop(n);
        } catch (Exception e) {
            
        }
    }

    public void mousePressed(MouseEvent e) {}
   
    public void mouseClicked(MouseEvent e){}  

    public void mouseEntered(MouseEvent e){}

    public void mouseExited(MouseEvent e){}

    public void mouseReleased(MouseEvent e){}

    // updates key variables when key pressed
    public void keyPressed(KeyEvent e)
    {

        keys[e.getKeyCode()] = true;
        
    }
    public void keyReleased(KeyEvent e){
         keys[e.getKeyCode()] = false;
         padel.setVel(0.0f);
        
    }
    // toggle keys space and c are used to update game images and multihits mode
    public void keyTyped(KeyEvent e){
        if(keys[KeyEvent.VK_SPACE] && !gameOn)
            {
                if(!ball.getMultiHits())
                ball.setMultiHits(true);
                else
                ball.setMultiHits(false);
            }
        
        if(keys[KeyEvent.VK_C] && !gameOn)
        {
            imageNum++;
        }
    }

    public void mouseDragged(MouseEvent e)
    {
        mouseMoved(e);
    }
    // if the game is on the padel is moved the where the mouse is
    public void mouseMoved(MouseEvent e)
    {
         if(gameOn){
        if(e.getX() < Main.xGameBoundsLeft + 11)
            padel.setX(Main.xGameBoundsLeft + 11);
        else if (e.getX() > Main.xGameBoundsRight - padel.getWidth() - 11)
            padel.setX(Main.xGameBoundsRight - padel.getWidth() - 11);
        else
        padel.setX(e.getX());
         }

    }
    
        
}
    