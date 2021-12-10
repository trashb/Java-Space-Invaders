/**
 * @(#)Bullet.java
 *
 * Sets up the Player's bullet; contains methods relating to the player's bullet (eg. making it move)
 *
 * @author 
 * @version 1.00 2021/11/22
 */

public class Bullet extends Common{
	public int bW,bH;	//height and width

/********************** Constructors **********************/
	public Bullet(){
	//isn't created until player shoots so there's no need to set it as invisible
	}
	
	public Bullet(int x, int y){
		setX(x+18);
		setY(y);
		bW = 5;
		bH = 10;
		setVisible(false);
		setSpeed(10);
	}

/********************** Method **********************/
	//move
	public void bMove(){
		if(getY()>-bH && isVisible()){
			setY(getY() - getSpeed());
		}
		else{
			setVisible(false); 	 //stop drawing the bullet when it goes off-screen
		}

		
	}
}
