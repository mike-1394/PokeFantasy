import org.lwjgl.Sys;
import org.newdawn.slick.*;
import java.util.*;

public class NPCPerson {
	private String name;
	private SpriteSheet sprites;
	private Image frame;
	private boolean visible;
	private boolean persistence=false;
	private boolean moving;
	private boolean interacting;
	private int direction;
	private int transdir;
	private Cell loc;
	private CellGrid cg;
	private long animstart;
	private boolean interactedWith;
	float xoffset;
	float yoffset;
	private int npcID;
	//UGH you're gonna have to change the rendering method for these to make up for offsets.
	//Otherwise it will immediately draw them in the next cell.
	//Oh! WAIT! MAKE THE OFFSET NEGATIVE! ON CELL TRANSITION, SET TO -1 CELL OFFSET THEN ADD TIL ZERO!
	//Programming. Genius.

	public NPCPerson(String n, int id){
		name=n;
		npcID=id;
		//-1: randoMale, -2: randoFemale
		if(npcID>=0){
			persistence=true;
		}
		//CG gets assigned in spawn method
		direction=2;
		transdir=0;//Down
		animstart=0;
		xoffset=0;
		yoffset=0;
		interactedWith=false;
		try{
			if(persistence){
				switch(npcID){
				case 0://Ben
					sprites=new SpriteSheet("imgs/ben.png",24,28);
					break;
				case 1://Kat
					sprites=new SpriteSheet("imgs/gerbil.png",24,28);
					break;
				}
			}
			else{
				switch(npcID){//These will have a random gen for multiple SS
				case -2://Female Random
					sprites=new SpriteSheet("imgs/kristy.png",24,28);
					break;
				case -1://Male Random
					sprites=new SpriteSheet("imgs/terry.png",24,28);
					break;
				}
			}
		}
		catch(Exception e){
			System.out.println("NPC Sprites ("+npcID+") not found!");
		}
		frame=sprites.getSubImage(0, 0);
	}

	public void parseStrings(String url){

	}

	public void spookPlayer(){
		//Could easily eliminate this using
		//Child class Ben extends NPCPerson
		//with overriding move() method
		if(!moving){
			//U0 R1 D2 L3
			if(cg.findPlayer().getXloc()>loc.getXloc()){
				//If player is to right of Ben, go right.
				if(getRight()!=null && getRight().isAvailable()){
					move(1);
				}
				else{
					if(cg.findPlayer().getYloc()>loc.getYloc()){
						//If player is below Ben, go down.
						move(2);
					}
					else{
						//Otherwise, go up.
						move(0);
					}
				}
			}
			else if(cg.findPlayer().getXloc()==loc.getXloc()){
				//If X locations are equal...
				if(cg.findPlayer().getYloc()>loc.getYloc()){
					//If player is below Ben, go down.
					move(2);
				}
				else{
					//Otherwise, go up.
					move(0);
				}
			}
			else{
				//Otherwise, go left.
				if(getLeft()!=null && getLeft().isAvailable()){
					move(3);
				}
				else{
					if(cg.findPlayer().getYloc()>loc.getYloc()){
						//If player is below Ben, go down.
						move(2);
					}
					else{
						//Otherwise, go up.
						move(0);
					}
				}
			}
		}
	}

	public void move(int dir){
		if(!moving){
			if(getDir(dir)!=null){
				if(getDir(dir).isAvailable() &&!interacting){
					direction=dir; //TURN ONLY
					turnNPC(direction);
					animstart=Sys.getTime();
					moving=true;
					loc.unsetOccupied();
					loc.setOccupier(null);
					loc=getDir(direction);
					loc.setOccupied();
					loc.setOccupier(this);
					switch(direction){
					case 0:
						yoffset=24;
						break;
					case 1:
						xoffset=-24;
						break;
					case 2:
						yoffset=-24;
						break;
					case 3:
						xoffset=24;
						break;
					}
					//SEND TO PROGRESS MOVE
					//Mark Start Time w/ Sys.getTime()
					//Unmark Current Cell
					//Mark Destination Cell
					//System.out.println(name+" was triggered to move to " + getDir(direction).getXloc()+","+getDir(direction).getYloc());
				}
			}
		}
	}


	public void rMove(){
		if(!moving){
			Random d=new Random();
			int newd=d.nextInt(4);
			if(getDir(newd)!=null){
				if(getDir(newd).isAvailable() && !getDir(newd).isTransporter() &&!interacting){
					direction=newd; //TURN ONLY
					turnNPC(direction);
					animstart=Sys.getTime();
					moving=true;
					loc.unsetOccupied();
					loc.setOccupier(null);
					loc=getDir(direction);
					loc.setOccupied();
					loc.setOccupier(this);
					switch(direction){
					case 0:
						yoffset=24;
						break;
					case 1:
						xoffset=-24;
						break;
					case 2:
						yoffset=-24;
						break;
					case 3:
						xoffset=24;
						break;
					}
					//moving=true; //SEND TO PROGRESS MOVE
					//Mark Start Time w/ Sys.getTime()
					//Unmark Current Cell
					//Mark Destination Cell
					//System.out.println(name+" was triggered to move to " + getDir(direction).getXloc()+","+getDir(direction).getYloc());
				}
			}
		}
	}

	public void progressMove(){
		//Perhaps should set time at start
		//just in case there is a significant change
		//before the correct line of code is executed
		if(Sys.getTime()>animstart+200){
			setFrame(0);
			switch(direction){
			case 0:
				yoffset=0;
				break;
			case 1:
				xoffset=0;
				break;
			case 2:
				yoffset=0;
				break;
			case 3:
				xoffset=0;
				break;
			}
			moving=false;
		}
		else if(Sys.getTime()>animstart+175){
			switch(direction){
			case 0:
				yoffset=3;
				break;
			case 1:
				xoffset=-3;
				break;
			case 2:
				yoffset=-3;
				break;
			case 3:
				xoffset=3;
				break;
			}
		}
		else if(Sys.getTime()>animstart+150){
			switch(direction){
			case 0:
				yoffset=6;
				break;
			case 1:
				xoffset=-6;
				break;
			case 2:
				yoffset=-6;
				break;
			case 3:
				xoffset=6;
				break;
			}
			setFrame(2);
		}
		else if(Sys.getTime()>animstart+125){
			switch(direction){
			case 0:
				yoffset=9;
				break;
			case 1:
				xoffset=-9;
				break;
			case 2:
				yoffset=-9;
				break;
			case 3:
				xoffset=9;
				break;
			}
		}
		else if(Sys.getTime()>animstart+100){
			switch(direction){
			case 0:
				yoffset=12;
				break;
			case 1:
				xoffset=-12;
				break;
			case 2:
				yoffset=-12;
				break;
			case 3:
				xoffset=12;
				break;
			}
			setFrame(0);
		}
		else if(Sys.getTime()>animstart+75){
			switch(direction){
			case 0:
				yoffset=15;
				break;
			case 1:
				xoffset=-15;
				break;
			case 2:
				yoffset=-15;
				break;
			case 3:
				xoffset=15;
				break;
			}
		}
		else if(Sys.getTime()>animstart+50){
			switch(direction){
			case 0:
				yoffset=18;
				break;
			case 1:
				xoffset=-18;
				break;
			case 2:
				yoffset=-18;
				break;
			case 3:
				xoffset=18;
				break;
			}
			setFrame(1);
		}
		else if(Sys.getTime()>animstart+25){
			switch(direction){
			case 0:
				yoffset=21;
				break;
			case 1:
				xoffset=-21;
				break;
			case 2:
				yoffset=-21;
				break;
			case 3:
				xoffset=21;
				break;
			}
		}

		//Progressively change offset for time diff
		//Change animation at each offset (may need to add variable)
		//Almost identical to player
		//Mark moving false when done
		//Reset timer
	}

	public void progressBen(){
		//Could be eliminated using inheretance
		//Ben moves more slowly
		if(Sys.getTime()>animstart+400){
			setFrame(0);
			switch(direction){
			case 0:
				yoffset=0;
				break;
			case 1:
				xoffset=0;
				break;
			case 2:
				yoffset=0;
				break;
			case 3:
				xoffset=0;
				break;
			}
			moving=false;
		}
		else if(Sys.getTime()>animstart+350){
			switch(direction){
			case 0:
				yoffset=3;
				break;
			case 1:
				xoffset=-3;
				break;
			case 2:
				yoffset=-3;
				break;
			case 3:
				xoffset=3;
				break;
			}
		}
		else if(Sys.getTime()>animstart+300){
			switch(direction){
			case 0:
				yoffset=6;
				break;
			case 1:
				xoffset=-6;
				break;
			case 2:
				yoffset=-6;
				break;
			case 3:
				xoffset=6;
				break;
			}
			setFrame(2);
		}
		else if(Sys.getTime()>animstart+250){
			switch(direction){
			case 0:
				yoffset=9;
				break;
			case 1:
				xoffset=-9;
				break;
			case 2:
				yoffset=-9;
				break;
			case 3:
				xoffset=9;
				break;
			}
		}
		else if(Sys.getTime()>animstart+200){
			switch(direction){
			case 0:
				yoffset=12;
				break;
			case 1:
				xoffset=-12;
				break;
			case 2:
				yoffset=-12;
				break;
			case 3:
				xoffset=12;
				break;
			}
			setFrame(0);
		}
		else if(Sys.getTime()>animstart+150){
			switch(direction){
			case 0:
				yoffset=15;
				break;
			case 1:
				xoffset=-15;
				break;
			case 2:
				yoffset=-15;
				break;
			case 3:
				xoffset=15;
				break;
			}
		}
		else if(Sys.getTime()>animstart+100){
			switch(direction){
			case 0:
				yoffset=18;
				break;
			case 1:
				xoffset=-18;
				break;
			case 2:
				yoffset=-18;
				break;
			case 3:
				xoffset=18;
				break;
			}
			setFrame(1);
		}
		else if(Sys.getTime()>animstart+50){
			switch(direction){
			case 0:
				yoffset=21;
				break;
			case 1:
				xoffset=-21;
				break;
			case 2:
				yoffset=-21;
				break;
			case 3:
				xoffset=21;
				break;
			}
		}

		//Progressively change offset for time diff
		//Change animation at each offset (may need to add variable)
		//Almost identical to player
		//Mark moving false when done
		//Reset timer
	}



	public boolean isPersistant(){
		return persistence;
		//return npcID!=-1;
	}

	public void toggleVisibility(){
		if(visible){
			visible=false;	
		}
		else{
			visible=true;
		}
	}

	public void setName(String n){
		if(!persistence){
			name=n;
		}
		else{
			System.out.println("Persistant Ref. CANNOT CHANGE NAME.");
		}
	}

	public void setSprites(SpriteSheet s){
		sprites=s;
	}

	public String getName(){
		return name;
	}

	public SpriteSheet getSprites(){
		return sprites;
	}

	public Image getFrame(){
		return frame;
	}

	public void turnNPC(int dir){
		switch(dir){
		//U0 R1 D2 L3
		case 0:
			frame=sprites.getSubImage(3, 0);
			transdir=3;
			break;
		case 1:
			frame=sprites.getSubImage(1, 0);
			transdir=1;
			break;
		case 2:
			frame=sprites.getSubImage(0, 0);
			transdir=0;
			break;
		case 3:
			frame=sprites.getSubImage(2, 0);
			transdir=2;
			break;
		}
	}

	public void setFrame(int f){
		switch(f){
		case 0:
			frame=sprites.getSubImage(transdir, 0);
			break;
		case 1:
			frame=sprites.getSubImage(transdir, 1);
			break;
		case 2:
			frame=sprites.getSubImage(transdir, 2);
			break;
		}
	}

	public void turnToPlayer(Player p){
		//U0 R1 D2 L3
		switch(p.getDirection()){
		case 0:
			direction=2;
			transdir=0;
			break;
		case 1:
			direction=3;
			transdir=2;
			break;
		case 2:
			direction=0;
			transdir=3;
			break;
		case 3:
			direction=1;
			transdir=1;
			break;
		}
		turnNPC(direction);
	}

	//Test Methods

	public void testSprites(){
		System.out.println("SpriteSheet Dimms: "+sprites.getHorizontalCount()+","+sprites.getVerticalCount());
	}

	public void setLoc(Cell l) {
		loc = l;
	}

	public Cell getLoc() {
		return loc;
	}

	public void setCellGrid(CellGrid c) {
		cg = c;
	}

	public CellGrid getCellGrid() {
		return cg;
	}

	public Cell getUp(){
		return cg.getCell(loc.getXloc(), loc.getYloc()-1);
	}
	public Cell getDown(){
		return cg.getCell(loc.getXloc(), loc.getYloc()+1);
	}
	public Cell getLeft(){
		return cg.getCell(loc.getXloc()-1, loc.getYloc());
	}
	public Cell getRight(){
		return cg.getCell(loc.getXloc()+1, loc.getYloc());
	}
	public Cell getDir(int dir){
		Cell c=null;
		try{
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
		}
		catch(Exception e){
			System.out.println("NPC "+name+" getDir() returned null.");
		}
		return c;
	}



	public void setInteracting(boolean in) {
		interacting = in;
	}

	public boolean isInteracting() {
		return interacting;
	}

	public void setMoving(boolean m) {
		moving = m;
	}

	public boolean isMoving() {
		return moving;
	}

	public void setInteractedWith(boolean interactedWith) {
		this.interactedWith = interactedWith;
	}

	public boolean wasInteractedWith() {
		return interactedWith;
	}
	public boolean isBen(){
		return name.equals("Ben");
	}

	public int getNpcID() {
		return npcID;
	}
}

