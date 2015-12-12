import java.util.LinkedList;

public class Inventory {
	private LinkedList<Item> inventory = new LinkedList<Item>();
	private LinkedList<Item> enumed = new LinkedList<Item>();
	private boolean visible=false;

	public Inventory(){

	}

	public String[] getItemList(int index){
		String[] temp = {"","","",""};
		if(index==0 && enumed.isEmpty()){
			temp[0]="Inventory is empty.";
			return temp;
		}
		int k=index*4;
		int j=0;
		for(k=index*4;k<enumed.size();k++){
			temp[j]=enumed.get(k).getQuantity()+" x "+enumed.get(k).getName();
			j++;
			if(j==4){
				break;
			}
		}
		return temp;
	}
	
	public Item[] getUIList(int index){
		Item[] temp = new Item[16];
		int k=index*16;
		int j=0;
		for(k=index*4;k<enumed.size();k++){
			temp[j]=enumed.get(k);
			j++;
			if(j==16){ //16 because j++ will make 15th into 16.
				break;
			}
		}		
		return temp;
	}

	public void addToInventory(Item i){
		boolean exists=false;
		inventory.add(i);
		for(int j=0;j<enumed.size();j++){
			if(i.getName().equals(enumed.get(j).getName())){
				enumed.get(j).setQuantity((enumed.get(j).getQuantity()+i.getQuantity()));
				exists=true;
			}
		}
		if(!exists){
			enumed.add(i);
		}
	}

	public void sortAlphabetical(){

	}

	public void sortByType(){

	}

	public void listAllItems(){
		System.out.println("Inventory:");
		if(inventory.size()==0){
			System.out.println("[EMPTY]");
		}
		for(int i=0;i<inventory.size();i++){
			if(i!=inventory.size()-1){
				System.out.print(inventory.get(i).getName()+", ");
			}
			else{
				System.out.println(inventory.get(i).getName());
			}
		}
		System.out.println("Enumerated:");
		if(enumed.size()==0){
			System.out.println("[EMPTY]");
		}
		for(int i=0;i<enumed.size();i++){
			if(i!=enumed.size()-1){
				System.out.print(enumed.get(i).getName()+" x"+enumed.get(i).getQuantity()+", ");
			}
			else{
				System.out.println(enumed.get(i).getName()+" x"+enumed.get(i).getQuantity());
			}
		}

	}

	public void triggerInventoryUI(int m){
		if(m==1){
			visible=true;
		}
		else{
			visible=false;
		}
	}

	public boolean isVisible(){
		return visible;
	}

	public Item checkForDataMatch(String dat){
		for(int k=0;k<inventory.size();k++){
			if(inventory.get(k).getData().equals(dat)){
				return inventory.get(k);
			}
		}
		return null;
	}

	public boolean checkForItemPresence(int itemID){
		boolean exists=false;
		for(int k=0;k<inventory.size();k++){
			if(inventory.get(k).getType()==itemID){
				exists=true;
				System.out.println("Item "+itemID+" exists in inventory.");
				break;
			}
		}
		return exists;
	}

	public boolean checkForItemQuantity(int itemID, int quant){
		boolean exists=false;
		for(int k=0;k<enumed.size();k++){
			if(enumed.get(k).getType()==itemID && enumed.get(k).getQuantity()==quant){
				exists=true;
				System.out.println(quant+" of item "+itemID+" does exist in inventory.");
				break;
			}
		}
		return exists;
	}

	public boolean useItem(int itemID){
		//NOTE THAT THIS METHOD CHECKS *AND* REMOVES ITEMS FROM INVENTORY. RETURNS FALSE IF NOT CONTAINED.
		boolean removed1=false;
		boolean removed2=false;
		for(int k=0;k<inventory.size();k++){
			if(inventory.get(k).getType()==itemID){
				inventory.remove(k);
				removed1=true;
				System.out.println("Removed item "+itemID+" from inventory.");
				break;
			}
		}
		for(int e=0;e<enumed.size();e++){
			if(enumed.get(e).getType()==itemID){
				if(enumed.get(e).getQuantity()==0){
					System.out.println("For some reason, enumerated inventory contained item with quantity 0.");
					enumed.remove(e);
					removed2=false;
					break;
				}
				else if(enumed.get(e).getQuantity()==1){
					System.out.println("Removed item "+itemID+" from enumerated inventory.");
					enumed.remove(e);
					removed2=true;
					break;
				}
				else{
					System.out.println("Removed 1 item "+itemID+" from enumerated inventory.");
					enumed.get(e).setQuantity(enumed.get(e).getQuantity()-1);
					removed2=true;
					break;
				}
			}
		}
		if(!(removed1 && removed2)){
			System.out.println("enumed and inventory have errors!");
			return false;
		}
		return (removed1 && removed2);
	}
	
	public int getTotalItemNumber(){
		return inventory.size();
	}
	
	public int getDifferentItemNumber(){
		return enumed.size();
	}
	
	public LinkedList<Integer> getAllItemTypes(){
		LinkedList<Integer> temp = new LinkedList<Integer>();
		for(int t=0;t<enumed.size();t++){
			temp.add(enumed.get(t).getType());
		}
		return temp;
	}
	
	public Item getItemFromEnumed(int index){
		return enumed.get(index);
	}
	
	public boolean isEmpty(){ //only checks enumed
		return(enumed.isEmpty());
	}

}
