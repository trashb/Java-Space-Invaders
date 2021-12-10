/**
 * @(#)Mystery.java
 *
 * Sets up the mystery ship/ufo; contains methods relating to it (eg. making the ship move)
 *
 * @author 
 * @version 1.00 2021/11/22
 */
import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.*;
import javax.imageio.ImageIO;

public class Mystery extends Common{
	public int mW,mH;	//height and width
	private BufferedImage img;

/********************** Constructors **********************/	
	public Mystery(){
		setVisible(false); 	//set invisible by default
	}
	
	public Mystery(int x){
		setX(x);
		setY(30);
		setSpeed(3);
		setVisible(false);
		
		//get sprite
		try {
    		img = ImageIO.read(new File("mystery.png"));
		} 
		catch (IOException e) {
			System.out.println(e);
		}
		//get width and height from image
		mW = img.getWidth();
    	mH = img.getHeight();
	}

/********************** Methods **********************/
	//move	
	public void mMove(int screenW){
		setX(getX() + getSpeed());
		reachedEnd(screenW);
	}
	
	//set visibility to false if it reaches edge of screen
	public void reachedEnd(int screenW){
		if(getX() >= screenW){
			setVisible(false);
		}
	}
	
	//get Image so it can be drawn
	public Image toImage(){
		return (Image)img;
	}
}
