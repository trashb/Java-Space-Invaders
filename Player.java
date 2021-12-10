/**
 * @(#)Player.java
 *
 * Set up player; contains methods relating to the player (eg. making the player move)
 *
 * @author 
 * @version 1.00 2021/11/22
 */
import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.*;
import javax.imageio.ImageIO;

public class Player extends Common{
	public int pW,pH;	//height and width
	private BufferedImage img;

/********************** Constructor **********************/		
	public Player(int x){
		//get sprite
		try {
    		img = ImageIO.read(new File("player.png"));
		} 
		catch (IOException e) {
			System.out.println(e);
		}
		setX(x);
		setY(510);
		pW = img.getWidth();
    	pH = img.getHeight();
		setSpeed(6);
	}

/********************** Methods **********************/
	//move
	public void pMove(int dir,int w, int h){
		setX(getX() + getSpeed() * dir);
	}
	
	//to Image for drawing
	public Image toImage(){
		return (Image)img;
	}
	
	//kill the player
	public void die(int initX){
		setVisible(false); 
		setX(initX);
	}
	
}