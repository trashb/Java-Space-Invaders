/**
 * @(#)AlienBullet.java
 *
 * Sets up an Alien's bullet. Each Alien has their own AlienBullet. Contains methods relating to an Alien's bullet (eg. making the it move)
 * As this class is called in the Alien class instead of the main program, methods in the Alien class are used to call the methods here
 *
 * @author 
 * @version 1.00 2021/11/22
 */

public class AlienBullet extends Common{
	public int bW,bH; 		//bullet height and width

/********************** Constructors **********************/	
	public AlienBullet(){
		setVisible(false); 	//shoudn't be visible until it's shot
	}
	
	public AlienBullet(int x, int y){
		setX(x);
		setY(y);
		bW = 5;
		bH = 15;
		setVisible(false);	//shoudn't be visible until it's shot
		setSpeed(6);
	}

/********************** Methods **********************/	
	//move the bullet	
	public void bMove(){
		setY(getY() + getSpeed());		
	}
	
/********************** Getters **********************/	
	public int getW(){
		return bW;
	}
	public int getH(){
		return bH;
	}
}
