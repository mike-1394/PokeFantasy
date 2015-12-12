import org.lwjgl.Sys;
import org.newdawn.slick.*;

public class Player {

	private SpriteSheet anims;
	private int identity;
	private int xloc;
	private int yloc;
	private int direction;//U0 R1 D2 L3
	private CellGrid cgloc;
	private Cell cloc;
	private boolean animating=false;
	private long animstart;
	final long ANIMDELAY=200;
	private Image currentTexture;
	private int currentFrame=0;

	public Player(){
		identity=0;
		try{
			anims=new SpriteSheet("imgs/jill.png",24,28);
		}
		catch(Exception e){
			System.out.println("Sprites not found.");
		}
		direction=2;//DOWN
		currentTexture=anims.getSubImage(0, 0);
	}

	public int getDirection(){
		return direction;
	}

	public void setFrame(int frame){
		if(frame>2){
			System.out.println("INVALID SETFRAME VALUE");
			frame=0;
		}
		//0 to 2!
		int x=0;
		switch(direction){
		case 0:
			x=3;
			break;
		case 1:
			x=1;
			break;
		case 2:
			x=0;
			break;
		case 3:
			x=2;
			break;
		}
		currentTexture=anims.getSubImage(x, frame);
		currentFrame=frame;
	}

	public void nextFrame(){
		int x=0;
		switch(direction){
		case 0:
			x=3;
			break;
		case 1:
			x=1;
			break;
		case 2:
			x=0;
			break;
		case 3:
			x=2;
			break;
		}
		if(currentFrame==2){
			currentTexture=anims.getSubImage(x, 0);
			currentFrame=0;
		}
		else{
			currentFrame++;
			currentTexture=anims.getSubImage(x,currentFrame);
		}
	}

	public void animateWalk(int dir){
		System.out.println(Sys.getTime()+" !> "+animstart+ANIMDELAY);
		if(Sys.getTime()>animstart+ANIMDELAY){
			System.out.println("Stopped Anim");
			stopAnimating();
		}
		//Will have to be part of DRAWPLAYER metherd.
	}

	public void reID(){
		switch(identity){
		case 0:
			identity++;
			try {
				anims=new SpriteSheet("imgs/terry.png",24,28);
			} catch (SlickException e) {
				System.out.println("Terry Sprites Not Found!");
			}
			System.out.println("Player is now Terry.");
			break;
		case 1:
			identity++;
			try {
				anims=new SpriteSheet("imgs/ben.png",24,28);
			} catch (SlickException e) {
				System.out.println("Ben Sprites Not Found!");
			}
			System.out.println("Player is now Ben.");
			break;
		case 2:
			identity++;
			try {
				anims=new SpriteSheet("imgs/kristy.png",24,28);
			} catch (SlickException e) {
				System.out.println("Kristy Sprites Not Found!");
			}
			System.out.println("Player is now Kristy.");
			break;
		case 3:
			identity++;
			try {
				anims=new SpriteSheet("imgs/gerbil.png",24,28);
			} catch (SlickException e) {
				System.out.println("Gerbil Sprites Not Found!");
			}
			System.out.println("Player is now Gerbil.");
			break;
		case 4:
			identity=0;
			try {
				anims=new SpriteSheet("imgs/jill.png",24,28);
			} catch (SlickException e) {
				System.out.println("Jill Sprites Lost!");
			}
			System.out.println("Player is now Jill.");
			break;
		}
	}

	public void spawnPlayer(Cell dest){
		cloc=dest;
		dest.setPlayerLoc();
		xloc=dest.getXloc();
		yloc=dest.getYloc();
	}

	public void drawPlayer(Graphics g){
		g.drawImage(currentTexture, 312, 216-4); //center of image (Player stays here for now).
	}

	public void turnPlayer(int d){
		direction=d;
	}

	public Cell getUp(){
		return cgloc.getCell(xloc, yloc-1);
	}
	public Cell getDown(){
		return cgloc.getCell(xloc, yloc+1);
	}
	public Cell getLeft(){
		return cgloc.getCell(xloc-1, yloc);
	}
	public Cell getRight(){
		return cgloc.getCell(xloc+1, yloc);
	}

	public void setCloc(Cell c) {
		cloc = c;
		xloc=c.getXloc();
		yloc=c.getYloc();
	}

	public void setCGloc(CellGrid cg){
		cgloc= cg;
	}

	public Cell getCell() {
		return cloc;
	}

	public boolean isAnimating(){
		return animating;
	}

	public void startAnimating(){
		animstart=Sys.getTime();
		System.out.println("Started anim at: "+animstart);
		animating=true;
	}

	public void stopAnimating(){
		animating=false;
		animstart=0;
	}

	public void toggleAnimating(){
		if(animating){
			animating=false;
		}
		else{
			animating=true;
		}
	}

	public long getAnimStart(){
		return animstart;
	}

	public long getAnimDelay(){
		return ANIMDELAY;
	}

	public Cell getDir(int dir){
		Cell c=null;
		switch(dir){
		case 0:
			c=getUp();
			break;
		case 1:
			c=getRight();
			break;
		case 2:
			c=getDown();
			break;
		case 3:
			c=getLeft();
			break;
		}
		return c;
	}

	public Cell getFacing(){
		Cell c=null;
		switch(direction){
		case 0:
			c=getUp();
			break;
		case 1:
			c=getRight();
			break;
		case 2:
			c=getDown();
			break;
		case 3:
			c=getLeft();
			break;
		}
		return c;
	}
}