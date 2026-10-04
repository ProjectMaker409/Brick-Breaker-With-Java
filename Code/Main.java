import javax.swing.JFrame;
import java.awt.Toolkit;

public class Main
{
    //gets screen length and width (stack overflow was used to figure out how to do this)
    //https://coderanch.com/t/341780/java/set-Jframe-full-screen
    //https://stackoverflow.com/questions/13734069/how-can-i-set-in-the-midst/13734319#13734319

      static Toolkit tk = Toolkit.getDefaultToolkit();
      static int width = ((int) tk.getScreenSize().getWidth());
      static int height = ((int) tk.getScreenSize().getHeight());
      // gets mid point of screen
      static int xCenter = width / 2;
      static int yCenter = height / 2;
      //variables of play area size
      static int gameWidth = 648;
      static int xGameBoundsLeft = 444;
      static int xGameBoundsRight = xGameBoundsLeft + gameWidth;
      static int yGameBoundsTop = 0;
      static int yGameBoundsBottom = height;
      //sets up Jframe
      public static void main(String[] args)
    {
        JFrame frame = new JFrame();
        frame.setResizable(true);
        // makes the full screen the window with no added buttons or side pannels
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 

        frame.setUndecorated(true);
        // starts brick breaker game
        BrickBreakerGame game = new BrickBreakerGame();
        frame.add(game);
        frame.setVisible(true);
    }
}