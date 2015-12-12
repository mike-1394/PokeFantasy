import org.lwjgl.Sys;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;


public class UserInterface {

	private boolean active;

	private Inventory playerinv;
	private QuestManager qman;
	private int type;
	private Image iBackground;
	private Image dBackground;
	private Image qBackground;
	private int selected=0;
	private int page=0;
	private long timeCreated;
	private String descText;
	Item[] temp;

	public UserInterface(Inventory i, QuestManager q){
		active=true;
		timeCreated=Sys.getTime();
		//This constructor requires 1 OR the other.
		if(i!=null){
			System.out.println("UI is Inventory Type.");
			playerinv=i;
			temp=playerinv.getUIList(0);
			type=0;
			try{
				iBackground = new Image("imgs/InventoryBackground.png");
				dBackground = new Image("imgs/DescBox.png");
			}
			catch(Exception e){
				System.out.println("Image (i/d)Background not found.");
			}

		}
		else if(q!=null){
			System.out.println("UI is Quest Type.");
			qman=q;
			type=1;
			try{
				qBackground = new Image("imgs/QuestUI.png");
			}
			catch(Exception e){
				System.out.println("Image qBackground not found.");
			}
		}

	}

	public void moveSelection(int dir){
		if(type==0){
			//Inventory
			System.out.print(selected+" -> ");
			if(playerinv.getDifferentItemNumber()>0){
				if(dir==2){
					if(selected==playerinv.getDifferentItemNumber()-1){
						selected=0;
					}
					else{
						selected++;
					}
				}
				else if(dir==0){
					if(selected==0){
						selected=playerinv.getDifferentItemNumber()-1;
					}
					else{
						selected--;
					}
				}
			}
			page=selected/16;
			temp = playerinv.getUIList(page);
			System.out.println(selected);
		}
		else if(type==1){
			//Quests
			if(qman.countQuests()>0){
				if(dir==1){
					if(selected==qman.countQuests()-1){
						selected=0;
					}
					else{
						selected++;
					}
				}
				else if (dir==3){
					if(selected==0){
						selected=qman.countQuests()-1;
					}
					else{
						selected--;
					}
				}
			}
		}
		
	}

	public void populateItemList(Inventory i){

	}

	public void drawTruncatedItemList(Graphics g){
		for(int i=0;i<temp.length;i++){
			g.setColor(Color.black);
			if(temp[i]!=null){
				if(i==selected-(page*16)){
					g.setColor(Color.blue);
				}
				g.drawString(temp[i].getQuantity()+" x "+temp[i].getName(), 330, 10+(440*i/17));
			}
		}
	}

	public void drawUserInterface(Graphics g){
		switch(type){
		case 0:
			g.setColor(Color.black);
			g.drawImage(iBackground,320,0);
			g.drawImage(dBackground,0,440);
			if(playerinv.isEmpty()){
				g.drawString("Your inventory is empty.",8,450);
			}
			else{
				drawTruncatedItemList(g);
				g.drawString(playerinv.getItemFromEnumed(selected).getDesc(),8,450);
			}
			break;
		case 1:
			g.setColor(Color.black);
			g.drawImage(qBackground,0,240);
			if(!qman.hasQuests()){
				g.drawString("No active quests.", 8, 250);
			}
			else{
				g.drawString(qman.getSpecificQuest(selected).getFullName(), 8, 250);
				g.drawString(qman.getSpecificQuest(selected).getCurrentObjective().getTitleText()+"\n"+qman.getSpecificQuest(selected).getCurrentObjective().getDescText(), 8, 284);
			}
			break;
		}
	}
	
}
