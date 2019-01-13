//Racer.java

//The Racer class is sub to the Player class and contains its respective
//attributes, constructor, get&set methods, and the move method -
//which allows us to move the racer's positions

public class Racer extends Player{
  
  //Attributes
  private int steps;
  private int direction;
  private int character;
  
  //Constructor
  public Racer(String n, int c, int m){
    super(n, m); //Draw from super class (Player)
    character = c;
  }
  
  //Get
  public int getSteps(){
    return steps;
  }
  public int getDirection(){
    return direction;
  }
  public int getCharacter(){
    return character;
  }
  
  //Set
  public void setSteps(int s){
    steps = s;
  }
  public void setDirection(int d){
    direction = d;
  }
  public void setCharacter(int c){
    character = c;
  }
  
  //move
  //This overrides the Player class move method
  public void move(int d, int s){
    if (getCharacter() == 1){ //If the racer is slow and steady
      d = 1; //then direction must be only forward
      if (s > 2){ //Keep input within rules
        s = 2;
      }
      else if (s < 1){ //Keep input within rules
        s = 1;
      }
      if ((getPosition() + s) > (getMaxPosition())){ //Keep input within rules
        setPosition(getPosition());
      }
      else if ((getPosition() + s) <= (getMaxPosition())){
        setPosition(getPosition() + s);
      }
    }
    else if (getCharacter() == 2){ //If the racer is fast and curious
      if (d == 0){ //it can go backwards
        if (s > 4){ //Keep input within rules
          s = 4;
        }
        else if (s < 1){ //Keep input within rules
          s = 1;
        }
        if ((getPosition() - s) < 1){ //Keep input within rules
          setPosition(1);
        }
        else if ((getPosition() - s) >= 1){
          setPosition(getPosition() - s);
        }
      }
      else if (d == 1){ //but it can also go forwards
        if (s > 4){ //Keep input within rules
          s = 4;
        }
        else if (s < 1){ //Keep input within rules
          s = 1;
        }
        if ((getPosition() + s) > (getMaxPosition())){ //Keep input within rules
          setPosition(getPosition());
        }
        else if ((getPosition() + s) <= (getMaxPosition())){
          setPosition(getPosition() + s);
        }
      }
    }
  }
}