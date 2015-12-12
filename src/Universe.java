import org.lwjgl.Sys;
import org.newdawn.slick.*;
import java.util.*;

public class Universe {

	private boolean movingUp;
	private boolean movingRight;
	private boolean movingDown;
	private boolean movingLeft;
	private boolean moving;
	private boolean movini;
	private boolean tbind;
	private World gworld;
	private int dir;
	private long timer;
	private long ttime;
	private boolean timeMarked=false;
	private Image tbox;
	private Image ibox;
	private Image dbox;
	private Interaction interact;
	private boolean interacting;
	private boolean battle;
	private Battle bat;
	private boolean uiOpen=false;

	public Universe(){
		System.out.println("Creating Universe...");
		gworld=new World();
		System.out.println("World added to Universe.");
		movingUp=false;
		movingRight=false;
		movingDown=false;
		movingLeft=false;
		moving=false;
		movini=false;
		try{
			tbox=new Image("imgs/TextBox.png");
			ibox=new Image("imgs/InventoryBackground.png");
			dbox=new Image("imgs/DescBox.png");
		}
		catch(Exception e){
			System.out.println("One or more UI images not found.");
		}
		System.out.println("Done.");
	}

	public void manipulate(){
		if(!gworld.isTalking()){
			gworld.getPlayer().reID();
		}
	}

	public void triggerBattle(int m){
		gworld.setSuspended(true);
		gworld.setTalking(true);//To prevent player move
		bat=new Battle();
		bat.startBattle();
		battle=true;
	}

	public void repopulate(){
		if(!gworld.isTalking()){
			System.out.println("Warning! Deleting current NPCs!");
			gworld.removeNPCs();
			System.out.println("Generating new NPCs!");
			gworld.populate();
		}
	}

	public void simulate(){
		/*
		if(uiOpen){
			gworld.updateUI();
		}
		 */
		if(!battle){
			gworld.simulateNPCs();
			gworld.progressNPCs();
		}
		else if(battle){
			bat.progressBattle();
		}
	}

	public void moveTheUniverse(){
		gworld.turnPlayer(dir);
		if(gworld.getPlayer().getCell().isTransporter()){
			if(!tbind){
				tbind=true;
				ttime=Sys.getTime();
			}
			if(tbind){
				if(Sys.getTime()>ttime+250){
					gworld.transport();
					tbind=false;
					//STOP ALL MOVE
					movingUp=false;
					movingRight=false;
					movingDown=false;
					movingLeft=false;
					moving=false;
					movini=false;
					//YISS
					return;
				}
			}
		}
		if(!moving && !timeMarked && !movini && !gworld.isTalking() && !tbind){
			if(movingUp){
				dir=0;
				moving=true;
				movini=true;
			}
			else if(movingRight){
				dir=1;
				moving=true;
				movini=true;

			}
			else if(movingDown){
				dir=2;
				moving=true;
				movini=true;

			}
			else if(movingLeft){
				dir=3;
				moving=true;
				movini=true;

			}
		}

		//------------------------
		//STUDY BELOW FOR JUMP!
		//-----------------------

		if(movini){
			//TIMER=SYS was here ORIGINALLY
			//MOVE IN WORLD.
			gworld.moveInWorld(dir);
			//INVALID MOVE MUST RETURN VALIDITY HERE TO PREVENT scroll&walk
			if(gworld.isLastMoveValid()){
				timeMarked=true;
			}
			else{
				moving=false;
			}
			movini=false;
			timer=Sys.getTime();
			//Remove if, else, and else contents.
		}
		if(timeMarked){
			if(timer+160<Sys.getTime()){//TIME DELAY IS HERE! (200)
				timeMarked=false;
				moving=false;
				//System.out.println("Delay Over.");
			}
			if(gworld.isLastMoveValid()){
				gworld.playerWalk(timer); //DELTA TIME DELAY HERE!
				gworld.getCellGrid().moveProgressive(dir, timer); //DELTA TIME DELAY HERE TOO!
			}
		}
	}


	//--------------------------
	//STUDY ABOVE FOR JUMP!
	//--------------------------


	public void drawTheUniverse(Graphics g){
		gworld.renderWorld(g);
		gworld.drawUI(g, tbox);
		if(battle){
			bat.drawBattle(g);
		}
		if(interact!=null && interact.isValid()){
			g.drawImage(tbox,0,380);
			interact.drawInteraction(g);
		}
	}

	public void openUI(int m){
		if(!moving && !timeMarked && !movini && interact==null){
			if(m==0){
				//Inventory
				if(!gworld.isSuspended()){
					uiOpen=true;
					gworld.openUI(0);
					/*
					gworld.setSuspended(true);
					gworld.setTalking(true); //To disable player move
					gworld.triggerQMItemUpdate();
					gworld.getPlayerInventory().triggerInventoryUI(1);
					 */

				}
				else{
					uiOpen=false;
					gworld.closeUI();
					/*
					gworld.getPlayerInventory().triggerInventoryUI(0);
					gworld.setSuspended(false);
					gworld.setTalking(false); //To enabled player move
					 */
				}
			}
			else if(m==1){
				if(!gworld.isSuspended()){
					uiOpen=true;
					gworld.openUI(1);
				}
				else{
					uiOpen=false;
					gworld.closeUI();
				}
			}
		}
	}

	public void interact(){
		if(!moving && !timeMarked && !movini && !gworld.isSuspended()){
			//So now you'd create a new interaction object...
			//That does whatever the shit below this does.
			if(interact==null || !interact.isValid()){
				interact=new Interaction(gworld, gworld.getPlayer().getFacing(), gworld.getQuestmgr());
				interact.startInteraction();
			}
			else if(interact.hasMultipleLines()){
				interact.nextLine();
			}
			else{
				interact.endInteraction();
				interact=null;
			}
			if(interact!=null && !interact.isValid()){
				interact=null;
			}
		}
	}

	//START USELESS?
	public void startInteracting(){
		if(!moving && !timeMarked && !movini){
			interact=new Interaction(gworld, gworld.getPlayer().getFacing(), gworld.getQuestmgr());
			if(interact.isValid()){
				interacting=true;
				gworld.setTalking(true); //STILL NECESSARY?
			}
			else{
				interact=null; //if even necessary.
				System.out.println("I went to the ELAB, but nobody was there.");
				gworld.setTalking(false); //STILL NECESSARY?
			}
		}
	}

	public void endInteracting(){
		if(interact.isDone()){
			interact.endInteraction();
			gworld.setTalking(false); //STILL NECESSARY?
			interact=null;
		}
	}
	//END USELESS?


	public void startMovingUp() {
		movingUp = true;
	}

	public void stopMovingUp(){
		movingUp = false;
	}

	public boolean isMovingUp() {
		return movingUp;
	}

	public void startMovingRight() {
		movingRight = true;
	}

	public void stopMovingRight(){
		movingRight = false;
	}

	public boolean isMovingRight() {
		return movingRight;
	}
	public void startMovingDown() {
		movingDown = true;
	}

	public void stopMovingDown(){
		movingDown = false;
	}

	public boolean isMovingDown() {
		return movingDown;
	}
	public void startMovingLeft() {
		movingLeft = true;
	}

	public void stopMovingLeft(){
		movingLeft = false;
	}

	public boolean isMovingLeft() {
		return movingLeft;
	}

	public boolean isMoving(){
		return moving;
	}

	public boolean directionDown(){
		return (movingLeft || movingRight || movingUp || movingDown);
	}

	public void printDebugInfo(){
		System.out.println("Player loc: CG "+gworld.getCellGrid().getGUID()+" @ "+gworld.getPlayer().getCell().getXloc()+","+gworld.getPlayer().getCell().getYloc());
		System.out.println("No NPCs: "+gworld.getNPCs().getNumNPCs()+".");
	}

	public void printInventory(){
		gworld.getPlayerInventory().listAllItems();
	}

	public void setInteracting(boolean interacting) {
		this.interacting = interacting;
	}

	public boolean isInteracting() {
		return interacting;
	}

	public boolean isInBattleMode(){
		return battle;
	}

	public void triggerTestQuest(){
		gworld.getQuestmgr().addQuest(0);
	}

	public void printTestQuestInfo(){
		if(gworld.getQuestmgr().getSpecificQuest(0)!=null){
			gworld.getQuestmgr().getSpecificQuest(0).updateQuest(); //REMEMBER THIS JUST UPDATES TIME!
			System.out.println(gworld.getQuestmgr().getSpecificQuest(0).getCurrentObjective().getTitleText());
		}
	}

	public boolean needsMouse(){
		return(battle || uiOpen);
	}

	public void setMouse(int x, int y){
		gworld.updateMouseData(x, y);
	}

	public void clickMouse(){
		gworld.testMouse();
	}

	public void navigateUI(int dir){
		gworld.navigateUI(dir);
	}

}
