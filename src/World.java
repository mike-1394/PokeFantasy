import java.util.LinkedList;
import java.util.Random;
import org.newdawn.slick.*;
import org.lwjgl.*;

public class World {

	//LinkedList<CellGrid> linked=new LinkedList<CellGrid>();
	private NPCManager npcm;
	private Player jillian;
	private CellGrid cells;
	private boolean isTalking;
	private int worldMode;
	private boolean suspended;
	private boolean moving;
	private boolean lastMoveValid=false;
	private LinkedList<Integer> iG = new LinkedList<Integer>();

	//THIS MAY NOT BE THE BEST PLACE FOR THE INVENTORY.
	private Inventory playerItems;
	//Or QM
	private QuestManager questmgr;
	//Or UI
	private UserInterface usint;

	//TEST
	Image ibox;
	Image dbox;
	private int mouseX=0;
	private int mouseY=0;
	private boolean clicked;
	int savedX=0;
	int savedY=0;
	//END TEST

	public World(){
		cells=new CellGrid(0,0,0, iG);
		//cells.getItemGUIDs()
		System.out.println("Initialized Test Cell Grid.");
		System.out.println("Successfully Parsed Test Grid.");
		questmgr=new QuestManager(playerItems);
		npcm=new NPCManager(0);
		addPlayer(new Player(), getCellGrid().getCell(5, 8));
		//addNPCs(null);
		populate();
		playerItems=new Inventory();
		System.out.println("Player added to World.");
		isTalking=false;
		suspended=false;
	}

	public void renderWorld(Graphics g){
		//REDCELL TEST BELOW THIS LINE
		//cells.findPlayer().setTex(n);//I AM FOR TEST
		//cells.drawGrid(g);
		cells.drawBackgroundEtAl(g);
		npcm.drawNPCs(g, cells.getXOffset(), cells.getYOffset());
		jillian.drawPlayer(g);
		questmgr.drawQuestUpdates(g);
	}
	
	public void openUI(int type){
		suspended=true;
		isTalking=true;
		if(type==0){
			triggerQMItemUpdate();
			usint=new UserInterface(playerItems, null);
		}
		else if(type==1){
			usint=new UserInterface(null, questmgr);
		}
	}
	
	public void closeUI(){
		suspended=false;
		isTalking=false;
		usint=null;
	}
	
	public void drawUI(Graphics g, Image b){
		//Start NEW TEST
		if(usint!=null){
			usint.drawUserInterface(g);
			g.setColor(Color.red);
			g.drawString(mouseX+","+mouseY, 10, 6);
			if(clicked){ //Draw clicked debug info
				g.drawRect(savedX, savedY, mouseX-savedX, mouseY-savedY);
				g.drawString(savedX+","+savedY,10,20);
				g.drawString("Dimms: "+(mouseX-savedX)+","+(mouseY-savedY),100, 6);
			}
			
			
		}
		//End NEW TEST
		/*
		if(playerItems.isVisible()){
			g.setColor(Color.black);
			g.drawImage(b,0,380);
			g.drawString(playerItems.getItemList(0)[0]+"\n"+playerItems.getItemList(0)[1]+"\n"+playerItems.getItemList(0)[2]+"\n"+playerItems.getItemList(0)[3],8,390);
			g.drawString(playerItems.getItemList(1)[0]+"\n"+playerItems.getItemList(1)[1]+"\n"+playerItems.getItemList(1)[2]+"\n"+playerItems.getItemList(1)[3],320,390);
			
		}
		*/
	}
	
	public void testMouse(){
		if(!clicked){
			savedX=mouseX;
			savedY=mouseY;
			clicked=true;
		}
		else{
			clicked=false;
		}
	}
	
	public void updateMouseData(int x, int y){
		mouseX=x;
		mouseY=y;
	}


	public void transport(){
		//SPECIFIC FOR TEST CASE. MUST BE MADE MORE ABSTRACT.
		int gdest=jillian.getCell().getDestid();
		int gx=jillian.getCell().getTxloc();
		int gy=jillian.getCell().getTyloc();
		removeNPCs();
		cells=new CellGrid(0,0,gdest,iG);
		jillian.setCGloc(cells);
		jillian.spawnPlayer(cells.getCell(gx, gy));
		cells.centerOnPlayer();
		questmgr.triggerLocationQuests(gdest); //TEST FOR QM!
		populate();
	}

	public void addPlayer(Player p, Cell c){
		jillian=p;
		jillian.setCGloc(cells);
		jillian.spawnPlayer(c);
		cells.centerOnPlayer();
	}

	public void removeNPCs(){
		npcm.despawnNPCs(cells);
	}

	public void addNPCs(NPCManager n){
		if(n!=null){
			n=npcm;
		}
		else{
			npcm.generateRandoms(7, 1);
		}
		npcm.spawnNPCs(cells);
	}

	public void populate(){
		//For populating world with NPC data from CG file.
		if(cells.getSpawnData().isEmpty()){
			System.out.println("No spawn data, defaulting to 7 M randoms.");
			npcm.generateRandoms(7, 1);
		}
		else if(cells.getSpawnData().get(0).equals("x")){
			System.out.println("x: no NPC generation.");
			return;
		}
		else for(int q=0;q<cells.getSpawnData().size();q++){
			//Generate NPC data
			String[] temp = cells.getSpawnData().get(q).split(" ");
			if(temp[0].charAt(0)=='%'){
				npcm.addBen();
			}
			else if(temp[0].charAt(0)=='p'){
				npcm.addPersistantNPC(Integer.parseInt(temp[1]));
			}
			else if(temp[0].charAt(0)=='~'){
				if(temp[2].charAt(0)=='m'){
					npcm.generateRandoms(Integer.parseInt(temp[1]), 1);
				}
				else{
					npcm.generateRandoms(Integer.parseInt(temp[1]), 0);
				}
			}
		}
		npcm.spawnNPCs(cells);
	}

	public void addProps(){

	}

	public void simulateNPCs(){
		if(!suspended){
			Random n= new Random();
			//Line below adjusts NPC move frequency
			if(n.nextInt(100)>92){
				npcm.randomMove();
			}
		}
	}

	public void progressNPCs(){
		if(!suspended){
			npcm.moveNPCs();
		}
	}

	public void turnPlayer(int direction){
		if(!isTalking){ // && !moving ??
			jillian.turnPlayer(direction);
			jillian.setFrame(0);
		}
	}

	public boolean isTalking() {
		return isTalking;
	}

	public void setTalking(boolean it) {
		isTalking = it;
	}

	public void playerWalk(long startTime){
		//Time-based. Frames can drop. Delta can change system-to-system.
		if(Sys.getTime()>startTime+160){
			jillian.setFrame(0);
			return;
		}
		else if(Sys.getTime()>startTime+120){
			jillian.setFrame(2);
		}
		else if(Sys.getTime()>startTime+80){
			jillian.setFrame(0);
		}
		else if(Sys.getTime()>startTime+40){
			jillian.setFrame(1);
		}
	}

	//COMMENTED OUT PRINT STATEMENTS
	public void moveInWorld(int direction){//This moves cellgrid references
		if(!isTalking){
			jillian.turnPlayer(direction);
			jillian.getCell().unsetPlayerLoc();
			//U0 R1 D2 L3
			//System.out.println("Trying to move in direction "+direction+".");
			if(jillian.getDir(direction).isAvailable()){
				//System.out.println("Direction "+direction+" is available.\nMoving cells.");
				//cells.move(direction); //REMOVED FOR TESTING. MOVES CELLS SHARPLY.
				jillian.getDir(direction).setPlayerLoc();
				jillian.setCloc(jillian.getDir(direction));
				setLastMoveValid(true);
			}
			else{
				//System.out.println("Direction "+direction+" unavailable.");
				jillian.getCell().setPlayerLoc();
				setLastMoveValid(false);
			}
			//Annoying yet useful location printout
			//System.out.println("Location: "+jillian.getCell().getXloc()+","+jillian.getCell().getYloc());
		}
	}

	public Player getPlayer(){
		return jillian;
	}

	public void stopMoving() {
		moving = false;
	}

	public void startMoving(){
		moving=true;
	}

	public boolean isMoving() {
		return moving;
	}

	public CellGrid getCellGrid(){
		return cells;
	}

	public void setLastMoveValid(boolean lmv) {
		lastMoveValid = lmv;
	}

	public boolean isLastMoveValid() {
		return lastMoveValid;
	}

	public NPCManager getNPCs(){
		return npcm;
	}

	public Inventory getPlayerInventory(){
		return playerItems;
	}

	public LinkedList<Integer> getTakenItemList(){
		return iG;
	}

	public void setSuspended(boolean s){ 
		suspended = s;
		System.out.println("World suspended? "+suspended);
	}

	public boolean isSuspended() {
		return suspended;
	}

	public QuestManager getQuestmgr() {
		return questmgr;
	}
	
	public void triggerQMItemUpdate(){
		System.out.println("Triggered QM Item Update.");
		LinkedList<Integer> temp = playerItems.getAllItemTypes();
		for(int t=0;t<temp.size();t++){
			questmgr.triggerItemQuests(temp.get(t));
		}
	}
	
	public void navigateUI(int dir){
		if(usint!=null){
			usint.moveSelection(dir);
		}
	}
	
	public void updateUI(){
		
	}

}
