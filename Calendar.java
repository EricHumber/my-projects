/* CSCI 1110
 * Eric Humber
 * B00 713 687
 * 12 Oct 2018
 * 
 * Assignment 3 Problem 5 (Part 2)
 * This program adds appointments for specific days and
 * prints the updated calendar
 */

import java.util.Scanner;
public class Calendar{
  
  //Main method
  public static void main(String[] args){
  Scanner kb = new Scanner(System.in);
  
  System.out.println("Pleae enter the month you wish to schedule for (numeric value 1-12): ");
  int mNum = kb.nextInt();
      
    //Initialize 2D array
    String[][] date = new String[5][7];
    int counter = 1;
    for (int i=0; i < 5; i++){ //Rows
        for (int j=0; j < 7; j++){ //Columns
            if (mNum == 1 || mNum == 3 || mNum == 5 || mNum == 7 || mNum == 8 || mNum == 10 || mNum == 12){
               if (counter > 31){
               date[i][j] = " "; //Fill in blank spaces of calendar after day 30
               }
               else{
               date[i][j] = counter + "-"; //Add number of counter and dash
               }
            }
            else if (mNum == 4 || mNum == 6 || mNum == 9 || mNum == 11){
               if (counter > 30){
               date[i][j] = " "; //Fill in blank spaces of calendar after day 30
               }
               else{
               date[i][j] = counter + "-"; //Add number of counter and dash
               }
            }
            else if (mNum ==2){
               if (counter > 28){
               date[i][j] = " "; //Fill in blank spaces of calendar after day 30
               }
               else{
               date[i][j] = counter + "-"; //Add number of counter and dash
               }
            }
        counter++;
        }
    }
    
    //Initialize variables for adding appointments
    String[][] apptAdd = new String[5][7]; //New array with appts
    String apptDate = "";
    String apptInit = "";
    
    //Read in appointments
    do{ //So it does it at least once, in case it's -1 right away
      System.out.print("Enter date for appointment(-1 to end): ");
        apptDate = kb.next() + "-"; //Ensure it recognizes what's already in array
      if (apptDate.equals("-1-")){
        break;
      }
      System.out.print("Enter initials of person you're meeting: ");
        apptInit = kb.next();
      
        for(int i=0; i < 5; i++){ //Rows
          for (int j=0; j < 7; j++){ //Columns
            if (apptDate.equals(date[i][j])){ //Find date
              if (apptAdd[i][j] == null){ //Only allow appt adding if no existing appt
                apptAdd[i][j] = apptInit; //Add initials for person user is meeting
              }
              else if (apptAdd[i][j] != null){
               System.out.println("Date already booked. Please pick another date.");
              }
            }
          }
        }
    }while (!apptDate.equals("-1-")); //Exit condition
            
    //Print days of the week
    System.out.printf("%-10s", "Sun");
    System.out.printf("%-10s", "Mon");
    System.out.printf("%-10s", "Tues");
    System.out.printf("%-10s", "Wed");
    System.out.printf("%-10s", "Thurs");
    System.out.printf("%-10s", "Fri");
    System.out.printf("%-10s\n", "Sat");
    
    //Print calendar
    for (int i = 0; i < 5; i++){ //Rows
      for (int j = 0; j < 7; j++){ //Columns
        if (apptAdd[i][j] == null){ //If the user didn't add anything
          System.out.printf("%-10s", date[i][j]);
        }
        else{
          System.out.printf("%-10s", date[i][j] + apptAdd[i][j]); //If the user added appts
        }
      }
      System.out.println();
    }
  }
}