/**
 * @(#)main.java
 *
 * Main file. Contains both the GamePanel class where game logic is found, and the Game4 class where the game is run
 *
 * @author Ashley
 * @version 1.00 2021/11/22
 */

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.Random;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;


/******************************** MAIN ********************************/
class Game4 extends JFrame{
	GamePanel game= new GamePanel();
		
    public Game4() {
		super("Space Invaders");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(game); 	//GamePanel
		pack(); 
		setVisible(true);
		setResizable(false);
    }
    
    public static void main(String[] arguments) {
		Game4 frame = new Game4();	//wooo 	:)
    }
}


/******************************** GAMEPANEL ********************************/
class GamePanel extends JPanel implements KeyListener, ActionListener{
//init variables & classes

	//constants
	public static final int INTRO=0, GAME=1, END=2; 	//for screen status
	public static final int W=800, H=600; 				//screen width and height
	
	//classes that I made
	private Player p;
	private Bullet b;
	private Mystery m;
	private ArrayList<Alien> aliens;
	private Shield[] shields;
	private Sounds sounds;
	
	//classes that were imported
	private Random rand;
	private Timer timer;
	
	//variables for gameplay
	private int screen = INTRO;
	private int level=0; 				//as level increases, aliens start lower
	private int score=0;
	private int hiscore=0;
	private int lives=4; 				//player lives
	
	//variables used for logic
	private int img; 					//which type of alien (one of 3)
	private int whichImg; 				//animation for alien (open or closed)
	private final int ANIMTIME = 30; 	//time between calls for animation and sounds
	private int lowest; 				//lowest alien
	private int invincTime; 			//time player is invincible (lower = invincible, higher = not invincible)
	private boolean isInvincible;  		//is the player invincible?
			
	//initial positions
	private int pInitX; 	//player's x val
	private int aInitX; 	//top-left alien's x val
	private int aInitY; 	//top-left alien's y val
	private int sInitX; 	//left-most alien's x val
	
	//font
	Font f;
	
	//images
	Image menu;
	Image gameover;
	
	//keys (from keylistener)
	private boolean []keys;
	
/********************** CONSTRUCTOR **********************/
	public GamePanel(){
		keys = new boolean[KeyEvent.KEY_LAST+1]; 	//for checking key press
		setPreferredSize(new Dimension(W, H)); 
		timer = new Timer(20, this);
		timer.start();
		setFocusable(true);
		requestFocus();
		addKeyListener(this);
		
		gameInit(level,lives,score);	//method below	
	}
	
/********************** METHODS **********************/
	//sets up initial "things" (init font, images, variables, classes)
	private void gameInit(int lev, int liv, int s){ //lev = level; 0,1,2,... 	liv = lives 	s = score (if level+, score should carry over)
		
		//custom font (from StackOverflow)
		try {
		    f = Font.createFont(Font.TRUETYPE_FONT, new File("space_invaders.ttf")).deriveFont(18f);
		    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		    //register the font
		    ge.registerFont(f);
		} catch (IOException e) {
		    e.printStackTrace();
		} catch(FontFormatException e) {
		    e.printStackTrace();
		}
		
		//set variables
		score = s;
		lives = liv;
		pInitX = 10;
		aInitX = 10;
		aInitY = 100 + lev*50;
		sInitX = 50;
		lowest = 0;
		invincTime = 0;
		isInvincible = true;
		whichImg = 0;
		
		//set images
		try {
	    	menu = (Image) ImageIO.read(new File("menu.png"));
	    	gameover = (Image) ImageIO.read(new File("gameover.png"));
		} 
		catch (IOException e) {
			System.out.println(e);
		}
		
		//init classes
		rand = new Random();
		
		sounds = new Sounds();
		p = new Player(pInitX);
		b = new Bullet(); 		//not "created", just init
		m = new Mystery(); 		//not "created", just init
		
		//get 4 shields
		shields = new Shield[4];
		for(int i=0; i<shields.length; i++){
			shields[i] = new Shield(sInitX + i*200);
		}
		
		//get 5*7 aliens (5 rows, 7 columns)
		aliens = new ArrayList<>(); 
		for(int yy=0; yy<5*40; yy+=40){
			for(int xx=0; xx<7*60; xx+=60){
				
				//5 rows but 3 sprites, so v caluculates which sprite to use
				//(file names are numbers (0,1,or 2))
				if((yy/40)%2==1){
					img = yy/40 /3 + 1; 	//1 -> 1, 3 -> 2
				}
				else{
					img = yy/40/2; 			//0 -> 0, 2 -> 1, 4 -> 2,
				}	
				Alien a = new Alien(aInitX+xx,aInitY+yy, img); 	//create alien
				aliens.add(a); 									//add alien to the list of aliens
			}
		}
	}

/********************** collision check **********************/
	//taken from stack overflow because it's a lot more condensed than what I would've made
	private boolean collides(
		int ax, int ay, int aw, int ah,
		int bx, int by, int bw, int bh){
		if(ax + aw > bx && 		//a's left edge > b's left edge
		   ay + ah > by && 		//a's top edge  > b's top edge
		   bx + bw > ax &&		//b's left edge > a's left edge
		   by + bh > ay){ 		//b's top edge  > a's top edge
		   	return true;
		   }
		return false;
	}
	
/********************** player action **********************/
	public void playerAction(){
		//move character	
		if(keys[KeyEvent.VK_LEFT] && p.getX()-5>=10){ 			//if <- is pressed and the player won't go off-screen by moving
			p.pMove(-1,W,H);
		}		
		if(keys[KeyEvent.VK_RIGHT] && p.getX()+5<=W-10-p.pW){	//if -> is pressed and the player won't go off-screen by moving
			p.pMove(1,W,H);
		}
		//shoot bullet
		if(keys[KeyEvent.VK_SPACE]){
			if(!b.isVisible()){ 			//if there is not existing bullet:
				b = new Bullet(p.getX(),p.getY()+b.bH); 	//create bullet
				b.setVisible(true);							//set it as visible (existing)
				//sfx
				sounds.playS();
			}
		}
	}
	
/********************** alien methods (used for shooting) **********************/
	//get the x vals for all the alien columns 
	public ArrayList<Integer> getCols(){ 
		boolean isInCols = false; 	//flag
		ArrayList<Integer> cols = new ArrayList<>(); 	//contains x vals
		
		//get the columns using a flag
		for(Alien a:aliens){
			if(a.isVisible()){ 		//if the alien is visible (alive)
				for(int i=0; i<cols.size(); i++){ 	//cols.size() will be 0,1,...,# of columns
					if(a.getX() == cols.get(i).intValue()){ 	//if the x val of alien is in the arraylist of columns
						isInCols = true; 						//don't add its x val to the arraylist
						break;
					}
					else{
						isInCols = false; 						//say that its x val is not in the arraylist (for now)
					}
				}
				if(!isInCols){ 					//if its x val is not in the arraylist of columns
					cols.add(a.getX());			//add it to the arraylist
				}
			}
		}
		return cols;
	}
	
	//get array containing the bottom aliens (ones that can shoot)
	public int[] bottomAliens(){
		int lowest; 						//lowest alien (highest y val) per column
		ArrayList<Integer> al = getCols(); 	//previous method
		int[] res = new int[al.size()]; 	//contains y val
		
		//find lowest per column and add to array
		for(int i=0; i<al.size(); i++){
			lowest = 0;
			for(Alien a:aliens){
				if(a.getX()==al.get(i)){
					lowest = a.getY()>lowest ? a.getY():lowest; 	//set current y val as the lowest if it is indeed so
				}
			}
			res[i] = lowest;
		}
		return res;
	}

/******************************** GAME LOOP ********************************/
	@Override
	public void actionPerformed(ActionEvent e){

/********************** INTRO SCREEN **********************/
		if(screen == INTRO){
			if(keys[KeyEvent.VK_SPACE]){
				screen = GAME;
			}
		}
/********************** GAMESCREEN **********************/		
		if(screen == GAME){
			
/********************** player **********************/	
			invincTime++; 			//invincible between 0 & 60
			
			if(invincTime>60){ 			//player is no longer invincible
				isInvincible = false;
				invincTime = 0;
			}	 
			else if(invincTime>40){ 	//player visible, but still invincible
				p.setVisible(true);
			}

			
			//if player isn't dead, allow player to act
			if(p.isVisible()){
				playerAction();
			}
	
			
/********************** mystery **********************/
			if(rand.nextInt(1300)==0 && !m.isVisible()){ 	//arbitrary number; don't want the mystery ship to be drawn too often
				m = new Mystery(-m.mW-50); 					//create ship off screen
				m.setVisible(true);
				//sfx
				sounds.playM();
			}
			//move mystery ship
			if(m.isVisible()){
				m.mMove(W);

			}
	
	
/********************** alien **********************/
			int aCount = 0; 				//number of aliens alive
			boolean reachedWall = false; 	//have they reached an edge of the screen?
			lowest = 0; 					//set lowest as "highest" val (visually the highest)
			whichImg++; 					
			
			if(whichImg>ANIMTIME *2){ 		//if 1 animation cycle passed (eg. went from 2.png -> 2a.png, and it's time to go back to 2.png)
				whichImg = 0; 				//reset the cycle
			}	
			
			//alien move (and check some variables while at it)
			for(Alien a: aliens){
				if(a.isVisible()){ 					//if alien is alive
					a.aMove(); 										//move the alien
					aCount++; 										//increase counter
					lowest = a.getY()>lowest ? a.getY():lowest; 	//set the alien as lowest if it is indeed the lowest
					
				}
			}
			
			//shoot & wall check
			for(Alien aal:aliens){
						
				//alien shoot
				for(int y:bottomAliens()){ 						//for all the bottom-most aliens (of their respective columns)
					if(aal.getY() == y && aal.isVisible() && !aal.isBVis()){
						int r = rand.nextInt(1000); 			//1000 is arbitrary number for "cooldown" between shots
						if(r==0){ 								// 1/1000 chance
							aal.shoot(); 							//alien shoot
						}
	
					}
				}
							
				//check if aliens reached the edge of the screen
				if(aal.reachedEnd(W)){
					reachedWall = true;
				}
			}
			
			//make aliens move down if one reached the edge of the screen
			if(reachedWall){ 			
				for(Alien ali:aliens){
					ali.newLine(); 			//move alien down
					
					//make aliens speed up
					if((ali.getSpeed()==1||ali.getSpeed()==-1)&&aCount<15){ 	//when there are less than 11 aliens and speed hasn't been changed yet
						ali.changeSpeed(); 	//make aliens move faster
					}	
				}
			}
	
		
/********************** alien bullet **********************/	
			for(Alien a : aliens){
				if(a.isBVis()){
					a.abMove(); 	//move bullet if it "exists"
					
					//bullet -> player collision
					if(collides(a.getBX(),a.getBY(),a.getBW(),a.getBH(),p.getX(),p.getY(),p.pW,p.pH) && p.isVisible() && !isInvincible){
						p.die(pInitX); 		//kill player
						lives--; 			//lose a life
						invincTime = 0; 		//player is now invincible (until invincTime > 60)
						isInvincible = true; 	//^
						a.setBVis(false); 		//make the bullet not visible
						//sfx
						sounds.playP();
						
						break;
					}
					
					//bullet -> player bullet collision
					if(collides(a.getBX(),a.getBY(),a.getBW(),a.getBH(),b.getX(),b.getY(),b.bW,b.bH)){
						a.setBVis(false); 		//destroy alien's bullet
						b.setVisible(false); 	//destroy player's bullet
					}
					
					//bullet -> shield collision
					for(Shield s:shields){
						int offx = a.getBX()-s.getX();				//get the x pos of hit area in terms of the shield (0 = shield left edge)
						int offy = a.getBY()+a.getBH()-s.getY(); 	//get the y pos of hit area in terms of the shield (0 = shield top edge)
						if(offx<0){ 	//prevent it from being too far left
							offx = 0;
						}
						if(collides(a.getBX(),a.getBY(),a.getBW(),a.getBH(),s.getX(),s.getY(),s.sW,s.sH)){
							int colour;
							
							//avoid coordinate out of bounds error due to invis pixels
							try{
					    		colour=s.getCol(offx,offy);
					   	 	}
					   	 	catch(Exception q){
					    		continue;
					    	}
						    
						    //check if pixel colour is green (ie hits the shield)	
							if(colour==-16712704){ 
								s.alienHit(offx,offy,a.getBY(),a.getBH()); 	//setting the colour of shield pixels to invis							
					 			a.setBVis(false);	
							}
						}
					}
					
					//bullet -> green line at the bottom of the screen collision
					if(a.getBY() > H-35-a.getBH()){
						a.setBVis(false); 		//set bullet to not viisble
					}
				}
			}
		
			
/********************** player bullet **********************/		
			//bullet move
			if(b.isVisible()){
				b.bMove();
				
				//bullet -> alien collision
				for(Alien a:aliens){
					if(a.isVisible() && collides(b.getX(), b.getY(), b.bW, b.bH, a.getX(), a.getY(), a.aW, a.aH)){
						b.setVisible(false); 	//set bullet to not visible ("destroyed")
						a.setVisible(false);	//set bullet to not visible (dead)
						score += (3-Integer.parseInt(a.getName()))*10; 	//get score according to which alien was killed
						//sfx
						sounds.playA();
					}
				}
				
				//bullet -> mystery collision
				if(m.isVisible() && collides(b.getX(), b.getY(), b.bW, b.bH, m.getX(), m.getY(), m.mW, m.mH)){
					m.setVisible(false); 
					b.setVisible(false);
					score += (rand.nextInt(4)+1)*50;	//score + either 50, 100, 150, or 250
					//stop the mystery sfx (if it's playing)
					sounds.stopM();
				}
				
				//bullet -> shield collision
				//basically same as in the alien bullet section (ln 375)
				for(Shield s:shields){
					int offx = b.getX() - s.getX();
					int offy = b.getY() - s.getY();
					if(offx<0){
						offx = 0;
					}
					if(collides(b.getX(),b.getY(),b.bW,b.bH,s.getX(),s.getY(),s.sW,s.sH) &&
					   s.getCol(offx,offy)==-16712704){ 	//-16812804 = image colour
						
				 		s.playerHit(offx, offy, b.getY());
						b.setVisible(false);
					}
				}
			}
			
/********************** game end (or start next level) **********************/
			//all aliens die
			if(aCount==0){
				level++; 	//increase level (aliens start lower)
				gameInit(level,lives,score); 	//start new "game" (level)
			}

			//no lives
			if(lives == 0){
				hiscore = score>hiscore ? score:hiscore; 	//set current score as high score if it is indeed
				screen = END;
			}
			
			//aliens reach end
			for(Alien a:aliens){
				if(a.isVisible() && a.getY() >= H - 200){
					hiscore = score>hiscore ? score:hiscore; 	//set current score as high score if it is indeed
					screen=END;
					break;
				}
			}
			
		}
		
/********************** ENDSCREEN **********************/
		if(screen==END){
			if(keys[KeyEvent.VK_SPACE]){
				gameInit(0,4,0); 	//level, lives, score
				screen = GAME;
			}
			else if(keys[KeyEvent.VK_ESCAPE]){
				System.exit(0);
			}
		}
		repaint();
	}

/********************** KEY EVENTS **********************/
	@Override
	public void keyReleased(KeyEvent e){
		int key = e.getKeyCode();
		keys[key] = false;
	}	
	@Override
	public void keyPressed(KeyEvent e){
		int key = e.getKeyCode();
		keys[key] = true;
	}
	@Override
	public void keyTyped(KeyEvent ke){}
	
/******************************** PAINT ********************************/
	@Override
	public void paint(Graphics g){
		g.setFont(f); 		//set font

/********************** introscreen **********************/
		if(screen == INTRO){
			//draw menu image (fill black too because I didn't size the image properly oops)
			g.setColor(Color.BLACK);
			g.fillRect(0,0,getWidth(), getHeight());
			g.drawImage(menu, 0, 0,null);					
		}
		
/********************** game **********************/
		else if(screen == GAME){
			//bg
			g.setColor(Color.BLACK);
			g.fillRect(0,0,getWidth(), getHeight());
			
			//shields
			for(Shield s: shields){
				g.drawImage(s.toImage(), s.getX(),s.getY(),null);
			}	
						
			//player
			if(p.isVisible()){
				g.drawImage(p.toImage(), p.getX(),p.getY(),null);
			}
			
			//mystery
			if(m.isVisible()){
				g.drawImage(m.toImage(), m.getX(),m.getY(),null);
			}
			
			//alien
			for(Alien a:aliens){
				if(a.isVisible()){
					g.drawImage(a.toImage(whichImg,ANIMTIME), a.getX(),a.getY(),null);
					
					//alien bullet
					if(a.isBVis()){
						g.setColor(Color.WHITE);
						g.fillRect(a.getBX(),a.getBY(),a.getBW(),a.getBH());
					}
				}
			}
			
			//bullet
			if(b.isVisible() && p.isVisible()){
				g.setColor(Color.WHITE);
				g.fillRect(b.getX(),b.getY(),b.bW,b.bH);
			}
			
			//lives
			for(int i=60; i<lives*60; i+=60){ 			//spaced 60 px apart; also draw 1 less life than there actually is
				g.drawImage(p.toImage(), i, H-25,null);
			}
			
			//line at the bottom of the screen
			g.setColor(Color.GREEN);
			g.drawLine(0, H-35, W, H-35);
			
			//text (lives, score, hi-score)
			g.setColor(Color.WHITE);
			g.drawString(Integer.toString(lives), 12, H-4);
			g.drawString("score <1>", 12, 30);
			g.drawString(Integer.toString(score), 50, 60);
			g.drawString("hi-score", W/2, 30);
			g.drawString(Integer.toString(hiscore), W/2, 60);
		}

/********************** endscreen **********************/
		else if(screen == END){
			//draw end image (fill black too because I didn't size the image properly oops)
			g.setColor(Color.BLACK);
			g.fillRect(0,0,getWidth(), getHeight());
			g.drawImage(gameover, 0, 30,null);
			//draw score & hi-score
			g.setColor(Color.WHITE);
			g.drawString("score <1>", 12, 30);
			g.drawString(Integer.toString(score), 50, 60);
			g.drawString("hi-score", W/2, 30);
			g.drawString(Integer.toString(hiscore), W/2, 60);
		}
    }
}
