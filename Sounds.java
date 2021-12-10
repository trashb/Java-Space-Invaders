/**
 * @(#)Sounds.java
 *
 * Sets up and gets the game's sound effects
 *
 * @author 
 * @version 1.00 2021/12/7
 */
import java.applet.*;
import java.io.*;
 
public class Sounds {
	private File mystery = new File("ufo_lowpitch.wav"); 	//mystery ship/ufo appearing
	private File shoot = new File("shoot.wav"); 			//player shoot
	private File aDeath = new File("invaderkilled.wav"); 	//alien dies
	private File pDeath = new File("explosion.wav"); 		//player dies
	 
  	private AudioClip m,s,a,p; 								//mystery ship, shot, alien, and player sounds respectively

/********************** Constructor **********************/
    public Sounds() {
    	
    	try{
    		m = Applet.newAudioClip(mystery.toURL());
    		s = Applet.newAudioClip(shoot.toURL());
    		a = Applet.newAudioClip(aDeath.toURL());
    		p = Applet.newAudioClip(pDeath.toURL());
   	 	}
   	 	catch(Exception e){
    		e.printStackTrace();
    	}
    }

/********************** Methods **********************/
    //play for each sound
    public void playM(){
    	m.play();
    }
    public void playS(){
    	s.play();
    }
    public void playA(){
    	a.play();
    }
    public void playP(){
    	p.play();
    }
    
    //stop for each sound
     public void stopM(){
    	m.stop();
    }
    public void stopS(){
    	s.stop();
    }
    public void stopA(){
    	a.stop();
    }
    public void stopP(){
    	p.play();
    }
   
}