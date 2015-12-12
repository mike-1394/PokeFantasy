import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;


public class Prop {

	private Image sprite;
	private int type; //Building, Sign, Bush, Tree, etc...
	private boolean destructable;
	private boolean destroyed=false;
	private boolean interactable;
	private boolean solid;
	private boolean itemholder=false;
	private String contents="Test.";
	private int xsize;
	private int ysize;
	private boolean transporter=false;
	private Item containedItem;
	private String spriteURL;

	public Prop(int t, String c){
		type=t;
		contents=c;
		try {
			switch(t){
			case 0:
				//SIGN
				spriteURL="imgs/Sign.png";
				interactable=true;
				destructable=false;
				xsize=1;
				ysize=1;
				//Some kinda loadtext for contents
				break;
			case 1:
				//TEST BUILDING
				if(c.equals("tc")){
					spriteURL="imgs/transportcenter.png";
					xsize=9;
					ysize=4;
				}
				else if(c.equals("p")){
					spriteURL="imgs/pyramid1.png";
					xsize=9;
					ysize=9;
				}
				else if(c.equals("gbb")){
					spriteURL="imgs/genericbuilding1_b.png";
					xsize=5;
					ysize=4;
				}
				else if(c.charAt(0)=='n'){
					xsize=5;
					ysize=4;
					switch(c.charAt(1)){
					case 'b':
						spriteURL="imgs/normal_house_blue.png";
						break;
					case 'g':
						spriteURL="imgs/normal_house_green.png";
						break;
					case 'r':
						spriteURL="imgs/normal_house_red.png";
						break;
					case 't':
						spriteURL="imgs/normal_house_teal.png";
						break;
					case 'w':
						spriteURL="imgs/normal_house_white.png";
						break;
					case 'y':
						spriteURL="imgs/normal_house_yellow.png";
						break;
					default:
						spriteURL="imgs/normal_house_teal.png";
						break;
					}
				}
				else if(c.charAt(0)=='f'){
					switch(c.charAt(1)){
					default:
					case 'l':
						spriteURL="imgs/longbush.png";
						xsize=2;
						ysize=1;
						break;
					case 'b':
						spriteURL="imgs/BiggerBush.png";
						xsize=2;
						ysize=2;
						break;
					case 't':
						spriteURL="imgs/TreeTest.png";
						xsize=1;
						ysize=2;
						break;
					}
				}
				else{
					spriteURL="imgs/BuildingTest.png";
					xsize=5;
					ysize=4;
				}
				interactable=false;
				destructable=false;
				break;
			case 2:
				//BUSH
				spriteURL="imgs/BushSprite.png";
				interactable=false;
				destructable=true;
				xsize=1;
				ysize=1;
				break;
			case 3:
				//1.5 TILE HIGH TREE
				spriteURL="imgs/TreeTest.png";
				interactable=false;
				destructable=false;
				xsize=1;
				ysize=1;
				break;
			case 4:
				//Teleport Pad
				spriteURL="imgs/lock.png";
				interactable=false;
				destructable=false;
				transporter=true;
				xsize=1;
				ysize=1;
				break;
			case 5:
				//Item Holder
				spriteURL="imgs/itemball.png";
				itemholder=true;
				containedItem=null;
				interactable=true;
				xsize=1;
				ysize=1;
				break;
			default:
				interactable=false;
				destructable=false;
				xsize=1;
				ysize=1;
				break;
			}
			sprite=new Image(spriteURL);
		} catch (SlickException e) {
			System.out.println("PROP IMAGE NOT FOUND!");
		}
	}

	public void setDestructable(boolean des) {
		destructable = des;
	}

	public boolean isDestructable() {
		return destructable;
	}

	public boolean isSign(){
		return (type==0);
	}

	public String getText(){
		return contents;
	}

	public void setSprite(Image sprite, String URL) {
		spriteURL=URL;
		this.sprite = sprite;
	}

	public Image getSprite() {
		return sprite;
	}

	public void setInteractable(boolean interactable) {
		this.interactable = interactable;
	}

	public boolean isInteractable() {
		return interactable;
	}

	public void setTransporter(boolean transporter) {
		this.transporter = transporter;
	}

	public boolean isTransporter() {
		return transporter;
	}

	public void setXsize(int xsize) {
		this.xsize = xsize;
	}

	public int getXsize() {
		return xsize;
	}

	public void setYsize(int ysize) {
		this.ysize = ysize;
	}

	public int getYsize() {
		return ysize;
	}

	public void setItemholder(boolean ih) {
		itemholder = ih;
	}

	public boolean isItemholder() {
		return itemholder;
	}

	public void setContainedItem(Item ci) {
		containedItem = ci;
	}

	public Item getContainedItem() {
		return containedItem;
	}

	public void setDestroyed(boolean d) {
		destroyed = d;
	}

	public boolean isDestroyed() {
		return destroyed;
	}
	
	public String getSpriteURL(){
		return spriteURL;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}

}
