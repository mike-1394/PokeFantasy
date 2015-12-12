import java.io.BufferedReader;
import java.io.FileReader;



public class Item {

	private int type;
	private String name;
	private String data;
	private String desc;
	private int guid; //Will be used to prevent item recreation
	private int quantity;
	private boolean fieldUsable;
	private boolean battleUsable;

	public Item(int t, int g){
		quantity=1; //Default 1
		type=t;
		guid=g;
		String line="";
		BufferedReader br = null;
		FileReader fr = null;
		try{
			fr = new FileReader("strings/itemList.txt");
			br = new BufferedReader(fr);
			while((line=br.readLine())!=null){
				String[] chunks=line.split("::");
				if(type==Integer.parseInt(chunks[0])){
					name=chunks[1];
					desc=chunks[2];
					data=chunks[3];
					break;
				}
			}
		}
		catch(Exception e){
			System.out.println("Item IO Error!");
		}
	}

	public void setName(String n) {
		name = n;
	}

	public String getName() {
		return name;
	}

	public void setType(int t) {
		type = t;
	}

	public int getType() {
		return type;
	}

	public void setDesc(String des) {
		desc = des;
	}

	public String getDesc() {
		return desc;
	}

	public String getData(){
		return data;
	}

	public void setGUID(int guid) {
		this.guid = guid;
	}

	public int getGUID() {
		//REMEMBER! GUID IS A UNIQUE VALUE FOR EACH ITEM.
		//This is NOT the same as TYPE.
		return guid;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getQuantity() {
		return quantity;
	}

}
