/**
 * @(#)Common.java
 *
 *
 * Contains variables common to every element/sprite (so that there's no need to set seperated variables for everything)
 * Except for those in main.java and Sounds.java, all other classes extend to this
 * Includes x & y vals of the element's position, the element's speed, and whether or not the element is visible
 *
 * @author 
 * @version 1.00 2021/11/24
 */

public class Common {

	private int x, y, speed; 	//x val, y val, movement speed (how much x increases/decreases)
	private boolean visible; 	//if the element is to be drawn or not; true = visible (alive, existing), false = not visible (dead, non-existing)
								//also ensures for some elements that there is only one on the screen at a time (eg. mystery ship/ufo)

/********************** Constructor **********************/	
    public Common() {
    	visible = true; 	//default set elements to visible
    }

/********************** Setters **********************/	 
    public void setX(int x){
    	this.x = x;
    }
    public void setY(int y){
    	this.y = y;
    }
    public void setSpeed(int s){ 
    	speed = s;
    }
    public void setVisible(boolean v){
    	visible = v;
    }

/********************** Getters **********************/	
    public int getX(){
    	return x;
    }
    public int getY(){
    	return y;
    }
    public int getSpeed(){ 
    	return speed;
    }
    public boolean isVisible(){
    	return visible;
    }
    
}