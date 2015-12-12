import java.io.BufferedReader;
import java.io.FileReader;
import java.util.LinkedList;
import org.lwjgl.Sys;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class Quest {

	private LinkedList<Objective> objectives = new LinkedList<Objective>();
	private String shortName;
	private String fullName;
	private boolean timeSensitive;
	private boolean complete;
	private boolean active;
	private int guid;
	private int currentObjNum=0;
	private long updateTime;

	public Quest(int id){
		updateTime=Sys.getTime();
		guid=id;
		switch(id){
		case 0:
			parseQFile("quests/TestQuest.txt");
			break;
		case 1:
			break;
		}
		//Parse/create Objective objects
	}

	public LinkedList<Objective> getObjectives() {
		return objectives;
	}

	public void addObjective(Objective O){
		objectives.add(O);
		System.out.println("Objective Added to Quest.");
	}

	public Objective getCurrentObjective(){
		return objectives.get(currentObjNum);
	}

	public void completeCurrentObjective(){
		if(objectives.get(currentObjNum).equals(objectives.getLast())){
			objectives.get(currentObjNum).setComplete(true);
			System.out.println("Quest: "+fullName+" complete!");
			complete=true;
			updateTime=Sys.getTime();
		}
		else{
			objectives.get(currentObjNum).setComplete(true);
			currentObjNum++;
			System.out.println("Quest: "+fullName+" progresses to stage "+currentObjNum+".");
			updateTime=Sys.getTime();
		}
	}

	public void updateQuest(){
		updateTime=Sys.getTime();
	}

	public void setTimeSensitive(boolean ts) {
		timeSensitive = ts;
	}

	public void parseQFile(String url){
		String line="";
		int parsemode=0;
		int step=0;
		BufferedReader br = null;
		FileReader parsable = null;
		try{
			parsable = new FileReader(url);
			br = new BufferedReader(parsable);
			while((line=br.readLine())!=null){
				if(line.charAt(0)!='#'){//Ignore comment lines
					if(line.charAt(0)=='@'){
						parsemode++;
					}
					else{
						switch(parsemode){
						case 0:
							fullName=line;
							break;
						case 1:
							String[] data = line.split("::");
							objectives.add(new Objective(step, data[0], data[1], Integer.parseInt(data[2]), Integer.parseInt(data[3])));
							System.out.println("Step "+step+": "+data[1]);
							step++;
							break;
						case 2:
							String[] txtData = line.split("::");
							objectives.get(Integer.parseInt(txtData[0])).setiText(txtData[1]);
							break;
						}

					}
				}
			}
		}
		catch(Exception e){
			System.out.println("Quest File Parse Error.");
		}
	}

	public boolean isTimeSensitive() {
		return timeSensitive;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}

	public boolean isComplete() {
		return complete;
	}

	public int getGuid() {
		return guid;
	}
	
	public void drawQuestUpdates(Graphics g){
		if(Sys.getTime()<updateTime+2000){
			g.setColor(Color.blue);
			g.fillRect(0, 0, 360, 48);
			g.setColor(Color.white);
			if(!complete){
				g.drawString("Quest: "+fullName+" objective update!\n"+getCurrentObjective().getDescText(), 2, 2);
			}
			else{
				g.drawString("Quest: "+fullName+" Complete!", 2, 2);
			}
		}
	}
	
	public String getFullName(){
		return fullName;
	}
}
