import org.newdawn.slick.*;

public class Cell {
	//A 24px*24px area is a cell. Motion and object placement grid.
	private int xloc; //In grid
	private int yloc; //In grid
	private boolean solid;
	private boolean occupied;
	private NPCPerson occupier;
	private boolean playerloc;
	private Image tex;
	private boolean spawnable;
	private boolean variable;
	private boolean transporter=false;
	private int txloc;
	private int tyloc;
	private int destid;
	private boolean locked=false;
	private String lockdata;
	private Prop prop;
	private char symbol;
	private int transtype;

	public Cell(char s, int x, int y, boolean sol, boolean v, boolean sp){
		symbol=s;
		solid=sol;
		setVariable(v);
		setXloc(x);
		setYloc(y);
		spawnable=sp;
	}

	public boolean hasProp(){
		return (prop!=null);
	}

	public boolean canInteract(){
		return (hasProp() && prop.isInteractable());
	}

	public Prop getProp(){
		return prop;
	}

	public void destroyProp(){
		prop.setDestroyed(true);
		//setDestroyed(true) b/c need to remove from CellGrid.PropList
		if(prop.isDestructable()){
			//prop=null;
			solid=false;
		}
		else if(prop.isItemholder()){
			//prop=null;
			solid=false;
		}
		else if(this.isLocked()){
			solid=false;
		}
		else{
			System.out.println("Nothing to destroy. Why was Cell.destroyProp() called?");
		}
		//Add any other changes to conditionals here too
	}

	public void setProp(Prop p){
		prop=p;
	}

	public Image getPropImage(){
		return (prop.getSprite());
	}

	public void makeTransporter(int did, int tx, int ty, int type, Image pi){
		transtype=type;
		setProp(new Prop(4,""));
		if(pi!=null){
			getProp().setSprite(pi, "imgs/TransportSprites.png");
		}
		setVariable(false);
		setSolid(false);
		setTransporter(true);
		setDestid(did);
		setTxloc(tx);
		setTyloc(ty);
	}

	public void setOccupied(){
		occupied=true;
	}

	public void unsetOccupied(){
		occupied=false;
	}

	public boolean isOccupied(){
		return occupied;
	}

	public void setTex(Image t) {
		tex = t;
	}

	public Image getTex() {
		return tex;
	}

	public void setVariable(boolean v) {
		variable = v;
	}

	public boolean isVariable() {
		return variable;
	}

	public void setXloc(int x) {
		xloc = x;
	}

	public int getXloc() {
		return xloc;
	}

	public void setYloc(int y) {
		yloc = y;
	}

	public int getYloc() {
		return yloc;
	}

	public void setPlayerLoc(){
		playerloc=true;
	}

	public boolean isPlayerLoc(){
		return playerloc;
	}

	public void unsetPlayerLoc(){
		playerloc=false;
	}

	public void markSpawnable(){
		if(!solid && !occupied && !playerloc){
			spawnable=true;
		}
		else{
			spawnable=false;
		}
	}

	public void setSpawnable(boolean s){
		spawnable=s;
	}

	public boolean isAvailable(){
		return !(solid||occupied||playerloc);
	}

	public boolean isSpawnable(){
		if(spawnable && !playerloc && !transporter && !solid){
			return true;
		}
		else{
			return false;
		}
	}

	public void setOccupier(NPCPerson occ) {
		occupier = occ;
	}

	public NPCPerson getOccupier() {
		return occupier;
	}

	public void setTransporter(boolean transporter) {
		this.transporter = transporter;
	}

	public boolean isTransporter() {
		return transporter;
	}

	public boolean isSolid(){
		return solid;
	}

	public void setSolid(boolean s){
		solid=s;
	}

	public void setTxloc(int txloc) {
		this.txloc = txloc;
	}

	public int getTxloc() {
		return txloc;
	}

	public void setTyloc(int tyloc) {
		this.tyloc = tyloc;
	}

	public int getTyloc() {
		return tyloc;
	}

	public void setDestid(int destid) {
		this.destid = destid;
	}

	public int getDestid() {
		return destid;
	}

	public void setSymbol(char s) {
		symbol = s;
	}

	public char getSymbol() {
		return symbol;
	}

	public void putItemHere(Item i){
		setProp(new Prop(5,"Item"));
		setSolid(true);
		getProp().setContainedItem(i);
	}

	public int getTransType(){
		return transtype;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public boolean isLocked() {
		return locked;
	}

	public String getLockdata() {
		return lockdata;
	}

	public void lock(String ld){
		if(!locked){
			lockdata=ld;
			locked=true;
			solid=true; //SO YOU CAN'T MOVE ONTO IT
		}
		else{
			System.out.println("Error: transporter cell is already locked with data: "+lockdata);
		}
	}

	public void unlock(String ld){
		if(ld.equals(lockdata)){
			locked=false;
			System.out.println("Unlock successful.");
			
		}
		else{
			System.out.println("Incorrect lockdata: "+ld+" != "+lockdata);
		}

	}

}
