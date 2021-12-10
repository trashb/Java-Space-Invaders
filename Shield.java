/**
 * @(#)Shield.java
 *
 * Set up a shield; contains methods relating to the shield (eg. changing pixel colours when hit from green -> invis)
 *
 * @author 
 * @version 1.00 2021/11/26
 */
import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.*;
import javax.imageio.ImageIO;

public class Shield extends Common{
	private BufferedImage img;
	public int sW, sH;	//height and width
	
/********************** Constructor **********************/
    public Shield(int x){
    	//get sprite
    	try {
    		img = ImageIO.read(new File("shield.png"));
		} 
		catch (IOException e) {
			System.out.println(e);
		}
    	sW = img.getWidth();
    	sH = img.getHeight();
   		setX(x);
   		setY(400);
    }

/********************** Methods **********************/    
    //set pxs to invis if alien bullet hits shield
    public void alienHit(int offx, int offy, int bulletY, int bulletH){ 	//offx & offy = vals of pixels that have been hit
    
    	for(int g=-4; g<8; g++){ 			//left-right offset (want the bullet to destroy more pixels)
			for(int h=-20; h<10; h++){ 			//^ but for top-bottom

				if(bulletY+bulletH>=getY()){ 		//make sure the bullet doesnt go beyond the shield
					if(offx+g>=0 && offy+h<sH){ 		//make sure pixels are in the shield

					 	int chY = offy+h >= 0 ? offy+h : 0; 	//to avoid coordinate out of bounds error
					 
						if(offx+g>=sW){ 						//if offx+g too far right
					 		setCol(sW-1, chY, 0x00000000);			//prevent coordinate out of bounds error
					 	}
					 	else if(offx+g<=0){						//if too far left
					 		setCol(0, chY, 0x00000000);				//prevent out of bounds error
					 	}
					 	else{
					 		setCol(offx+g, chY, 0x00000000);	
						}
					}		
				}	
			}
		}
    }
    
    //set pxs to invis if player bullet hits shield (same as ^, but with some minor changes bc player's bullet move upwards instead of downwards):
	public void playerHit(int offx, int offy, int bulletY){
		for(int i=-2; i<6; i++){
			for(int j=-4; j<10; j++){
				if(bulletY+j>=getY()){
					if(offx+i>=0 && offy+j<sH){
				 		if(offx+i>=sW){ 
							setCol(sW-1, offy+j, 0x00000000);
				 		}
				 		else if(offx+i<=0){
				 			setCol(0, offy+j, 0x00000000);
				 		}
				 		else{
				 			setCol(offx+i, offy+j, 0x00000000);	
				 		}
				 	}				
				} 				
			}
		}
	}
   
   //get pixel colour 
    public int getCol(int x, int y){
    	return img.getRGB(x,y);
    }
    
    //set pixel colour
    public void setCol(int x, int y, int col){
    	img.setRGB(x,y,col);
    }
    
    //to Image so that it can be drawn
    public Image toImage(){
    	return (Image)img;
    }
    
}