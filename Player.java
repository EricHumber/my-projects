//Player.java

//The Player class is super to the Racer class and we draw from some of it,
//most notably the attributes and the win method

public class Player{
  
  //Attributes
  private String name;
  private int position;
  private int maxPosition;
  
  //Player constructor
  public Player(String n, int m){
    name = n;
    position = 1; //Set position to 1
    maxPosition = m;
  }
  
  //Get
  public String getName(){
    return name;
  }
  public int getPosition(){
    return position;
  }
  public int getMaxPosition(){
    return maxPosition;
  }
  
  //Set
  public void setName(String n){
    name = n;
  }
  public void setPosition(int pos){
    position = pos;
  }
  public void setMaxPosition(int m){
    maxPosition = m;
  }
  
  //move
  //This gets overridden by the Racer class move method
  public void move(int pos){
    if (pos >= getMaxPosition()){ //Keep input within rules
      setPosition(getMaxPosition());
    }
    else if (pos < getMaxPosition() && pos >= 1){
      setPosition(pos);
    }
    else if (pos < 1){ //Keep input within rules
      setPosition(1);
    }
  }
  
  //win
  public boolean win(){
    if (getPosition() == getMaxPosition()){
      return true;
    }
    else{
      return false;
    }
  }
}