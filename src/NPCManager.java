import java.io.*;
import java.util.*;
import org.newdawn.slick.*;

//FIGURE OUT WHY YOU'RE USING noNPCs instead of npcs.size()!


public class NPCManager {
	private FileReader namelist;
	private FileReader speechlist;
	private LinkedList<NPCPerson> npcs=new LinkedList<NPCPerson>();
	private LinkedList<String> texts=new LinkedList<String>();
	private String line;
	private String[] names;
	private boolean dynamic;
	private int noNPCs;

	public NPCManager(int type){
		switch(type){
		case 0:
			break;
		case 1:
			break;
		default:
			break;
		}
		dynamic=false;
		noNPCs=0;
		try{
			speechlist = new FileReader("strings/TutorialSpeech.txt");
			BufferedReader bufRead = new BufferedReader(speechlist);
			while ((line = bufRead.readLine()) != null){
				texts.add(line);
			}
			bufRead.close();
		}
		catch(Exception e){
			System.out.println("Speech list read error.");
		}

	}

	public void despawnNPCs(CellGrid cg){
		for(int i=0;i<cg.getHeight();i++){
			for(int j=0;j<cg.getLength();j++){
				cg.getCell(j, i).setOccupier(null);
				cg.getCell(j, i).unsetOccupied();
			}
		}
		npcs.clear();
		noNPCs=0;
		//System.out.println(npcs.size()+"=0:Cleared?");

	}

	public void spawnNPCs(CellGrid cg){ //Do after player spawn
		for(int k=0;k<npcs.size();k++){
			//Sets NPCs in the manager to THIS cellgrid
			//Ah, this is why Ben isn't giving null pointers.
			//Good.
			npcs.get(k).setCellGrid(cg);
		}
		LinkedList<Cell> spawnables= new LinkedList<Cell>();
		Random r = new Random();
		//System.out.print("Spawn locations: ");
		for(int i=0;i<cg.grid.size();i++){
			if(cg.grid.get(i).isSpawnable()){
				spawnables.add(cg.grid.get(i));
				//System.out.print(cg.grid.get(i).getXloc()+","+cg.grid.get(i).getYloc()+" ");
			}
		}
		if(spawnables.size()<=noNPCs){
			System.out.println("Only spawning first "+spawnables.size()+" NPC's due to spawn cell limit. (or even)");
			for(int i=0;i<spawnables.size();i++){
				npcs.get(i).setLoc(spawnables.get(i));
				spawnables.get(i).setOccupied(); //WILL NEED TO UNSET FOR MOVING!
				spawnables.get(i).setOccupier(npcs.get(i)); //THIS TOO!
				System.out.println("Spawned "+npcs.get(i).getName()+" at "+spawnables.get(i).getXloc()+","+spawnables.get(i).getYloc());
			}
		}
		if(spawnables.size()>noNPCs){
			for(int i=0;i<noNPCs;i++){
				int randomloc=r.nextInt(spawnables.size());
				npcs.get(i).setLoc(spawnables.get(randomloc));
				spawnables.get(randomloc).setOccupied(); //WILL NEED TO UNSET FOR MOVING!
				spawnables.get(randomloc).setOccupier(npcs.get(i)); //THIS TOO!
				spawnables.remove(randomloc);
				//The line below threw a null for some reason...
				//System.out.println("i: "+i+" Null?");
				//System.out.println("Spawned "+npcs.get(i).getName()+" at "+spawnables.get(randomloc).getXloc()+","+spawnables.get(randomloc).getYloc());
			}
		}
	}

	public void drawNPCs(Graphics g, float x, float y){ //x and y are offsets NULL POINTER FOR NPCS
		for(int j=0; j<npcs.size(); j++){
			if(npcs.get(j).getLoc().getXloc()*24+x+npcs.get(j).xoffset<640 && npcs.get(j).getLoc().getXloc()*24+x+npcs.get(j).xoffset>-24 && npcs.get(j).getLoc().getYloc()*24+y-4+npcs.get(j).yoffset<480 && npcs.get(j).getLoc().getYloc()*24+y-4+npcs.get(j).yoffset>-28){
				g.drawImage(npcs.get(j).getFrame(), npcs.get(j).getLoc().getXloc()*24+x+npcs.get(j).xoffset, npcs.get(j).getLoc().getYloc()*24+y-4+npcs.get(j).yoffset); //CELL LOCATIONS NEED *24!
			}
		}
	}

	public void randomMove(){
		if(!npcs.isEmpty()){
			Random r=new Random();
			int npcno=r.nextInt(noNPCs);
			if(!npcs.get(npcno).isBen()){
				npcs.get(npcno).rMove();
			}
		}
	}

	public void moveNPCs(){
		for(int i=0;i<npcs.size();i++){
			if(npcs.get(i).isBen()){
				if(!npcs.get(i).isInteracting()){
					npcs.get(i).spookPlayer();
				}
				npcs.get(i).progressBen();
			}
			else if(npcs.get(i).isMoving()){
				npcs.get(i).progressMove();
			}

		}
	}

	public void toggleDynamic(){
		if(!dynamic){
			dynamic=true;
		}
		else{
			dynamic=false;
		}
	}


	public void generateRandoms(int number, int gender){
		//Generates random NPCs
		Random r = new Random();
		try{
			if(gender==1){
				namelist = new FileReader("strings/MaleNames.txt");
			}
			else{
				namelist = new FileReader("strings/FemaleNames.txt");
			}
			BufferedReader bufRead = new BufferedReader(namelist);
			line=bufRead.readLine();
			names=line.split(" ");
			System.out.println("Parsed an array of "+names.length+" names!");
			bufRead.close();
			line=null;
		}
		catch(Exception e){
			System.out.println("File Read Error.");
			return;
		}
		for(int i=0;i<number;i++){
			try{
				if(gender==1){
					npcs.add(new NPCPerson(names[(r.nextInt(names.length))],-1));
				}
				else{
					npcs.add(new NPCPerson(names[(r.nextInt(names.length))],-2));
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		noNPCs+=number;
		//System.out.println(noNPCs);
		names=null;
		dynamic=true;
	}

	public void addBen(){
		//Generates Ben NPC
		try{
			System.out.println("BOO!");
			npcs.add(new NPCPerson("Ben",0));
			noNPCs++;
		}
		catch(Exception e){
			System.out.println("For some reason, Ben sprite data wasn't found.");
		}
	}

	public void removeBen(){
		for(int j=0;j<npcs.size();j++){
			if(npcs.get(j).getName().equals("Ben")){
				npcs.get(j).getLoc().setOccupier(null);
				npcs.get(j).getLoc().unsetOccupied(); //Why is this even its own method? Should be implied based on if(occupier!=null).
				npcs.remove(j);
				noNPCs--;
			}
		}
	}

	public void addPersistantNPC(int npcID){
		System.out.print("Adding Persistant NPC #"+npcID+": ");
		try{
			switch(npcID){
			case 1:
				//Kat
				npcs.add(new NPCPerson("Katarina",1));
				break;
			}
			noNPCs++;
		}
		catch(Exception e){
			System.out.println(" SPRITE(S) NOT FOUND!");
		}

	}

	public LinkedList<String> getStrings(){
		return texts;
	}

	public int getNumNPCs(){
		return noNPCs;
	}
}

