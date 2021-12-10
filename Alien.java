/**
 * @(#)Alien.java
 *
 * Sets up an alien; contains methods relating to the alien (eg. making the alien change direction when it reaches the end)
 * Calls the AlienBullet class so that each alien has their own AlienBullet; also contains methods relating to the AlienBullet (eg. making the bullet move)
 *
 * @author 
 * @version 1.00 2021/11/22
 */
 
import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.*;
import javax.imageio.ImageIO;

public class Alien extends Common{
	public int aW,aH;					//height and width
	private BufferedImage img1, img2;	//2 images per alien (for animation)
	private String img1Name, img2Name;
	private AlienBullet b = new AlienBullet(); 	//init bullets

/********************** Constructor **********************/	
	public Alien(int x, int y, int s){
		setX(x);
		setY(y);
		setSpeed(1);
		
		//images
		try {
			//*names of diff. alien types are set as numbers, with either an 'a' or no 'a' (eg. "2.png", "2a.png")
    		img1 = ImageIO.read(new File(Integer.toString(s)+".png"));
    		img2 = ImageIO.read(new File(Integer.toString(s)+"a.png"));
		} 
		catch (IOException e) {
			System.out.println(e);
		}
		
		aW = img1.getWidth(); 	//img1 and img2 are same dimensions
    	aH = img1.getHeight();
  		
  		//get file names (minus the .png)
    	img1Name = Integer.toString(s);
    	img2Name = "" + Integer.toString(s) + "a";
	}

/********************** Methods **********************/	
	
	//move alien
	public void aMove(){
		setX(getX() + getSpeed());
	}
	
	//check if aliens reached the end
	public boolean reachedEnd(int screenW){
		if(getX() >= screenW-aW-10 && getSpeed() >0 || getX()<10 && getSpeed()<0){
			return true;
		}
		return false;
	}
	
	//make alien move down and switch direction
	public void newLine(){
		setSpeed(-getSpeed());
		setY(getY() + 30);
	}
	
	//speed up alien
	public void changeSpeed(){
		if(getSpeed() > 0){
			setSpeed(getSpeed() + 1); 		//change to <-
		}
		else{
			setSpeed(getSpeed() - 1); 		//change to ->
		}

	}
	
	//alien shoots
	public void shoot(){
		b = new AlienBullet(getX()+aW/2+b.bW,getY()+aH);
		b.setVisible(true); 		//set bullet to visible so it's drawn
	}
	
	//get the desired sprite as Image (so that it can be drawn)
	public Image toImage(int m, int time){
		if(m<time){
			return (Image)img1;
		}
		return (Image)img2;
	}
		
	//move alien bullet
	public void abMove(){
		b.bMove(); 	
	}	

/***************** Getters *****************/
	
	public String getName(){ 	//for score keeping, so only the file names w/ only numbers are used (eg. "1" instead of "1a")
		return img1Name;
	}

//getters for the alien bullet 
	public boolean isBVis(){
		return b.isVisible();
	}
	
	public int getBX(){
		return b.getX();
	}
	public int getBY(){
		return b.getY();
	}
	public int getBW(){
		return b.getW();
	}
	public int getBH(){
		return b.getH();
	}
	
/***************** Setters (for alien bullet) *****************/	
	public void setBY(int y){
		b.setX(y);
	}
	
	public void setBVis(boolean v){
		b.setVisible(v);
	}
}
