import java.util.Random;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;


public class Interaction {

	private int type;
	//-1=Invalid
	//0=NPC
	//1=Sign
	//2=Destructable
	//3=PNPC
	//4=Ben
	//5=Item
	//6=Unlock
	private String unlockText="";
	private String questText="";
	private Item key;
	private boolean active;
	private boolean different=false;
	private boolean necessaryItem=false;
	private String text;
	private Cell source;
	private long timeout=500;
	private long timeoutstart;
	private boolean delayed;
	private boolean done;
	private int textlines;
	private int textboxes;
	private World w;
	private int intrand=0;
	private QuestManager quests;
	private Quest actQ;

	public Interaction(World world, Cell s, QuestManager qm){
		done=false;
		quests=qm;
		source=s;
		w=world;
		type=-1;
		textlines=1;
		if(source==null){
			type=-1;
		}
		else{
			if(source.getOccupier()!=null){
				type=0;
				if(source.getOccupier().isPersistant()){
					type=3;
					//Test VAR
				}
				if(source.getOccupier().isBen()){
					type=4;
					//BOO
				}
			}
			if(source.hasProp()){
				if(source.getProp().isSign()){
					type=1;
				}
				else if(source.getProp().isDestructable()){
					type=2;
				}
				else if (source.getProp().isItemholder()){
					type=5;
				}
				else if (source.isLocked()){
					type=6;
				}
				else{
					type=-1;
				}
			}
		}
	}

	public void startInteraction(){
		System.out.print("Started interaction type "+type);
		if(isValid()){
			System.out.println(": Valid.");
			active=true;
			w.setTalking(true);
			switch(type){
			case 0:
				System.out.println("Started talking to NPC");
				w.getPlayer().getFacing().getOccupier().setInteracting(true);
				Random r = new Random();
				intrand=r.nextInt(w.getNPCs().getStrings().size());
				w.getPlayer().getFacing().getOccupier().turnToPlayer(w.getPlayer());
				actQ=quests.getRelatedNPCQuest(-1);
				if(actQ!=null){
					questText=actQ.getCurrentObjective().getiText();
					actQ.completeCurrentObjective();
				}
				break;
			case 1:
				System.out.println("Read sign.");
				break;
			case 2:
				if(w.getPlayerInventory().checkForItemPresence(7)){
					necessaryItem=true;
					System.out.println("DESTROY!");
				}
				break;
			case 3:
				w.getPlayer().getFacing().getOccupier().setInteracting(true);
				w.getPlayer().getFacing().getOccupier().turnToPlayer(w.getPlayer());
				//Get persistant NPC stuff
				
				//Something with the QM
				break;
			case 4:
				w.getPlayer().getFacing().getOccupier().setInteracting(true);
				w.getPlayer().getFacing().getOccupier().turnToPlayer(w.getPlayer());
				key=w.getPlayerInventory().checkForDataMatch("BenAway");
				if(key!=null){
					different=true;
				}
				break;
			case 5:
				System.out.println("GAHT ITEM!");
				w.getPlayerInventory().addToInventory(source.getProp().getContainedItem());
				//Add to inventory
				//Remove prop
				//Prevent from regenerating?
				break;
			case 6:
				key=w.getPlayerInventory().checkForDataMatch(source.getLockdata());
				if(key!=null){
					System.out.println("Key match found!");
					unlockText="Unlocked with "+key.getName()+"!";
				}
				else{
					unlockText="This is locked.";
					System.out.println("No matching key.");
				}
				break;
			}
		}
		else{
			System.out.println(": Invalid");
			System.out.println("I went to the ELab, but nobody was there!");
		}

	}

	public void endInteraction(){
		active=false;
		//Find some way to make this null?
		//So it doesn't leak memory?
		//Perhaps on interaction close in universe class?
		//Which will be triggered by space bar?
		switch(type){
		case 0:
			w.getPlayer().getFacing().getOccupier().setInteractedWith(true);
			w.getPlayer().getFacing().getOccupier().setInteracting(false);
			if(actQ!=null){ //Totally making assumptions here.
				quests.removeCompleted();
			}
			break;
		case 2:
			if(necessaryItem){
				w.getPlayerInventory().useItem(7);
				source.destroyProp();
			}
			break;
		case 3:
			w.getPlayer().getFacing().getOccupier().setInteractedWith(true);
			w.getPlayer().getFacing().getOccupier().setInteracting(false);
			break;			
		case 4:
			w.getPlayer().getFacing().getOccupier().setInteractedWith(true);
			w.getPlayer().getFacing().getOccupier().setInteracting(false);
			if(different){
				w.getNPCs().removeBen();
			}
			break;
		case 5:
			quests.triggerItemQuests(source.getProp().getContainedItem().getType()); //TYPE != GUID!
			if(source.getProp().getContainedItem().getGUID()!=-1){ //GUID -1 == Respawnable.
				w.getTakenItemList().add(source.getProp().getContainedItem().getGUID());
			}
			source.destroyProp();
			break;
		case 6:
			unlockText="";
			if(key!=null){
				source.destroyProp();//See if it works like this.
			}
			break;
		}
		w.setTalking(false);
	}

	public void drawInteraction(Graphics g){
		g.setColor(Color.black);
		switch(type){
		case 0:
			//USE THIS TO WORK ON MULTI-FRAME TEXT!
			if(actQ!=null){
				g.drawString(w.getPlayer().getFacing().getOccupier().getName()+":\n"+questText, 8, 390);
			}
			else if(!w.getPlayer().getFacing().getOccupier().wasInteractedWith()){
				g.drawString("Hello! My name is "+w.getPlayer().getFacing().getOccupier().getName()+".", 8, 390);
			}
			else{
				g.drawString(w.getPlayer().getFacing().getOccupier().getName()+":\n"+w.getNPCs().getStrings().get(intrand), 8, 390);
			}			
			break;
		case 1:
			g.drawString(w.getPlayer().getFacing().getProp().getText(), 8, 400);
			break;
		case 2:
			if(necessaryItem){
				g.drawString("You used an HM01 to destroy this.", 8, 400);
			}
			else{
				g.drawString("You might be able to destroy this... Maybe if you get HM01.", 8, 400);
			}
			break;
		case 3:
			g.drawString("I'm depressed. Can I have a coke?", 8, 400);
			//Persistant NPC. Get PNPC Data.
			break;
		case 4:
			if(different){
				g.drawString("Ben copies your completed hardware homework and leaves you alone.", 8, 400);
			}
			else{
				g.drawString("This is the Ben NPC. He is going to mummify you! Run away!", 8, 400);
			}
			break;
		case 5:
			g.drawString("Player found 1 "+w.getPlayer().getFacing().getProp().getContainedItem().getName()+"!", 8, 400);
			break;
		case 6:
			g.drawString(unlockText, 8, 400);
			break;
		}
	}

	public boolean isActive(){
		return active;
	}

	public int getType(){
		return type;
	}

	public String getText(){
		return text;
	}

	public boolean isDelayed(){
		return delayed;
	}

	public boolean isValid(){
		return (type!=-1);
	}

	public void setDone(boolean d) {
		done = d;
	}

	public boolean isDone() {
		return done;
	}

	public int getRandomValue(){
		return intrand;
	}

	public boolean hasMultipleLines(){
		return textlines>1;
	}

	public void nextLine(){

	}
}
