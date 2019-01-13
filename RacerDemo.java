/*
 * This program creates two racer objects from user input that race against each other
 * 
 * The RacerDemo class keeps track of user input for moving the racers,
 * then prints who wins (or if there is a tie) when they reach the "max position"
 */

//RacerDemo.java

import java.util.Scanner;
public class RacerDemo{
  public static void main(String[] args){
    Scanner kb = new Scanner(System.in);
    
    //Explain rules to user
    System.out.println("Welcome! For today's race there will be two players.");
    System.out.println("Players can either be slow & steady or fast & furious.");
    System.out.println("If your player is slow & steady, they will only move forward but can only move up to 2 spots per turn!");
    System.out.println("If you players is fast & furious, they can move backward or forward but can move up to 4 spots per turn!");
    System.out.println("Keep in mind that each player will start at Position 1 on the track.");
    System.out.println("If a player wants to win, they cannot overshoot it! They must land right on their maximum position!");
    System.out.println("May the best player win!");
    System.out.println();
    
    //First racer object input
    System.out.println("Please enter the first Player's name: ");
    String n1 = kb.next(); //name
    System.out.println("Please enter the first Player's type (1 for slow & steady, 2 for fast and furious): ");
    int c1 = kb.nextInt(); //character type
    System.out.println("Please enter the maximum position for " + n1 + ": ");
    int m1 = kb.nextInt(); //max position
    
    //Second racer object input
    System.out.println("Please enter the second Player's name: ");
    String n2 = kb.next();
    System.out.println("Please enter the second Player's type (1 for slow & steady, 2 for fast and furious): ");
    int c2 = kb.nextInt();
    System.out.println("Please enter the maximum position for " + n2 + ": ");
    int m2 = kb.nextInt(); //max position
    
    //Create racer objects
    Racer r1, r2;
    r1 = new Racer(n1, c1, m1);
    r2 = new Racer(n2, c2, m2);
    
    System.out.println();
    System.out.println("And they're off!");
    System.out.println();
    
    //Grab direction and steps, and update racers
    int cntr = 0;
    while (r1.win() != true && r2.win() != true){ //keeps race going until a win or a tie
      System.out.println("Please enter direction for " + n1 + " to move (0 for backwards, 1 for forwards): ");  
      int d1 = kb.nextInt();
      if (c1 == 1){ //Keep direction for first player within rules
         d1 = 1;
      }
        
      System.out.println("Please enter number of steps for " + n1 + " to move (max 2 for s&s, max 4 for f&f): ");
      int s1 = kb.nextInt();
      if (c1 == 1){ //Keep moves for first player within rules
         if (s1 > 2){
            s1 = 2;
         }
         if (s1 < 0){
            s1 = 0;
         }
      }
      if (c1 == 2){
         if (s1 > 4){
            s1 = 4;
         }
         if (s1 < 0){
            s1 = 0;
         }
      }
      
      r1.move(d1, s1); //call move method for first racer
      if (d1 == 0){
         System.out.println(n1 + " moved backward " + s1 + " steps");
         System.out.println();
      }
      else if (d1 == 1){
         System.out.println(n1 + " moved forward " + s1 + " steps");
         System.out.println();
      }
      
      System.out.println("Please enter direction for " + n2 + " to move (0 for backwards, 1 for forwards): ");
      int d2 = kb.nextInt();
      if (c2 == 1){ //Keep direction for second player within rules
         d1 = 1;
      }
      
      System.out.println("Please enter number of steps for " + n2 + " to move (max 2 for s&s, max 4 for f&f): ");
      int s2 = kb.nextInt();
      if (c2 == 1){ //Keep moves within rules
         if (s2 > 2){
            s2 = 2;
         }
         if (s2 < 0){
            s2 = 0;
         }
      }
      if (c2 == 2){
         if (s2 > 4){
            s2 = 4;
         }
         if (s2 < 0){
            s2 = 0;
         }
      }
      
      r2.move(d2, s2); //call move method for second racer
      if (d2 == 0){
         System.out.println(n2 + " moved backward " + s2 + " steps");
         System.out.println();
      }
      else if (d2 == 1){
         System.out.println(n2 + " moved forward " + s2 + " steps");
         System.out.println();
      }
      
      cntr++;
    }
    
    //Print win condition
    if (r1.win() == true && r2.win() != true){ //first racer wins
        System.out.println("");
        System.out.print(r1.getName() + " wins in " + cntr + " rounds!");
    }
    else if (r1.win() != true && r2.win() == true){ //second racer wins
        System.out.println("");
        System.out.print(r2.getName() + " wins in " + cntr + " rounds!");
    }
    else if (r1.win() == true && r2.win() == true){ //a tie
        System.out.println("");
        System.out.print("Tie! in " + cntr + " rounds!");
    }
  }
}