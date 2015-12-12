
public class Objective {

	private boolean complete;
	private int step;
	private String titleText;
	private String descText;
	private int type;
	//Type: 0=Find item, 1=goto place, 2=talk to NPC
	private int data;
	/*
	private NPCPerson NPCtarget;
	private Prop Ptarget;
	private Item Itarget;
	*/
	private String iText="Empty interaction text.";
	

	public Objective(int no, String title, String desc, int typ, int dat){
		complete=false;
		step=no;
		titleText=title;
		descText=desc;
		type=typ;
		data=dat;
	}


	public void setComplete(boolean c) {
		complete = c;
	}


	public boolean isComplete() {
		return complete;
	}


	public void setDescText(String dt) {
		descText = dt;
	}


	public String getDescText() {
		return descText;
	}


	public void setStep(int s) {
		step = s;
	}


	public int getStep() {
		return step;
	}

	/*
	public void assignNPCtarget(NPCPerson n) {
		NPCtarget = n;
	}


	public NPCPerson getNPCtarget() {
		return NPCtarget;
	}


	public void assignPtarget(Prop p) {
		Ptarget = p;
	}


	public Prop getPtarget() {
		return Ptarget;
	}


	public void assignItarget(Item i) {
		Itarget = i;
	}


	public Item getItarget() {
		return Itarget;
	}
	
	public boolean hasNPCTarget(){
		return NPCtarget!=null;
	}
	
	public boolean hasPropTarget(){
		return Ptarget!=null;
	}
	
	public boolean hasItemTarget(){
		return Itarget!=null;
	}
	*/
	
	public void setiText(String it){
		iText=it;
	}
	
	public String getiText(){
		return iText;
	}
	
	public int getType(){
		return type;
	}
	
	public int getData(){
		return data;
	}


	public void setTitleText(String titleText) {
		this.titleText = titleText;
	}


	public String getTitleText() {
		return titleText;
	}
}
