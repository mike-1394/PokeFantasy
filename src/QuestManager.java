import java.util.LinkedList;
import org.newdawn.slick.Graphics;

public class QuestManager {

	//This will become part of world or universe... Preferably world
	private LinkedList<Quest> quests = new LinkedList<Quest>();
	private LinkedList<Quest> completed = new LinkedList<Quest>();
	private Inventory inv;


	public QuestManager(Inventory in){
		inv=in;
	}

	public Quest getSpecificQuest(int i){
		Quest temp=null;
		try{
			temp = quests.get(i);
		}
		catch(Exception e){
			System.out.println("QM returns null. Quest index OOB.");
			temp=null;
		}
		return temp;
	}

	public void addQuest(int id){
		boolean exists=false;
		for(int k=0;k<quests.size();k++){
			if(quests.get(k).getGuid()==id){
				exists=true;
			}
		}
		if(!exists){
			quests.add(new Quest(id));
			System.out.println("Added Quest "+id+" to QM.");
		}
	}

	public void itterateQuest(int guid){

	}

	public void triggerLocationQuests(int cgGUID){
		for(int i=0;i<quests.size();i++){
			if(quests.get(i).getCurrentObjective().getType()==1 && !quests.get(i).isComplete()){
				if(quests.get(i).getCurrentObjective().getData()==cgGUID){
					quests.get(i).completeCurrentObjective();
				}
			}
		}
	}

	public void triggerQuest(Interaction i){
		for(int j=0;j<quests.size();j++){ //Nested...
			//if(quests.get(j).getObjectives().)
		}
	}

	public void generateRandomQuest(){
		//world.getNPCS()...
	}

	public void updateQMLoc(){

	}

	public Quest getRelatedNPCQuest(int npcGUID){
		if(npcGUID==-1){ //ANY RANDOM NPC
			for(int i=0;i<quests.size();i++){
				if(quests.get(i).getCurrentObjective().getType()==2){
					if(quests.get(i).getCurrentObjective().getData()==-1){
						return quests.get(i);
					}
				}
			}
		}
		else{
			//same thing
		}
		return null;
	}

	public void triggerNPCQuests(int npcGUID){
		if(npcGUID==-1){ //ANY RANDOM NPC

		}
	}

	public void triggerItemQuests(int itemTYPE){
		for(int k=0;k<quests.size();k++){
			if(!quests.get(k).isComplete()){
				if(quests.get(k).getCurrentObjective().getType()==0){
					if(quests.get(k).getCurrentObjective().getData()==itemTYPE){
						quests.get(k).completeCurrentObjective();
					}
				}
			}
		}
	}

	public void drawQuestUpdates(Graphics g){
		for(int k=0;k<quests.size();k++){
			quests.get(k).drawQuestUpdates(g);
		}
	}

	public void removeCompleted(){
		for(int c=0;c<quests.size();c++){
			if(quests.get(c).isComplete()){
				completed.add(quests.get(c));
				quests.remove(c);
				System.out.println("Removed quest.");
				break; //IDK if I should do c--; or just stop.
			}
		}
	}

	public boolean checkForCompleted(int id){
		for(int i=0;i<completed.size();i++){
			if(completed.get(0).getGuid()==id){
				return true;
			}
		}
		return false;
	}
	
	public boolean hasQuests(){
		return !quests.isEmpty();
	}
	
	public int countQuests(){
		return quests.size();
	}
	
	public int countCompletedQuests(){
		return completed.size();
	}

}
