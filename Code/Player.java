import java.util.ArrayList;
//each player has name and score they are saved in an array list and repersent the top 10 higest scores
public class Player {
    // player object variables
    private String name;
    private int score;

    //constructor method
    public Player(String name, int score)
    {
        this.name = name;
        this.score = score;
    }
    // mutator method
    public void setScore(int score){this.score = score;}

    public void setName(String name){this.name = name;}

    //accessor methods
    public int getScore(){return score;}

    public String getName(){return name;}
    
    //sorts an array list of players to display high score (uses selection sort)
    public static void sortPlayers(ArrayList<Player> list)
    {
    
        for (int i = 0; i < list.size() - 1; i++)
        {
            int x = findMin(list, i);
            swap(list, i, x);
        }
    }
    // helper method for sorting array list
     private static void swap(ArrayList<Player> list, int x, int y)
    {
         Player temp = list.get(x);
        list.set(x, list.get(y));
        list.set(y, temp);
    }

    // helper method for sorting array list
     private static int findMin(ArrayList<Player> list, int start)
    {
        int minValue = list.get(start).getScore();
        int minIndex = start;

        for (int i = start + 1; i < list.size(); i++)
        {
            if (list.get(i).getScore() < minValue)
            {
                minValue = list.get(i).getScore() ;
                minIndex = i;
            }
        }

        return minIndex;
    }
    // returns a string of only the players with the top ten socres to be wrriten to the text file
    public static String copyPlayers(ArrayList<Player> list)
    {
        String str = "";
        if(list.size() < 10){
         for (int i = 0; i < list.size(); i++ )
         {
            str += list.get(i).getName() + " " + list.get(i).getScore();
            str += "\n";
         }
        }
        else{
            for (int i = 0; i < 10; i++ )
         {
            str += list.get(i).getName() + " " + list.get(i).getScore();
            str += "\n";
         }
        }
         return str;
    }
}
