import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import org.lwjgl.Sys;
import org.newdawn.slick.*;


public class CellGrid {
	//private int type; //Indoors? Outdoors? City? Route? Cave?
	private boolean interior=false;
	private int wallType=0;//0=noWalls,1=commercial,2=pyramid,3=home1,4=home2
	//Ground/Fence/Themes?
	private int guid;
	private int length;
	private int height;
	private float xoffset=0;
	private float yoffset=0;
	private boolean initialized=false;
	private FileReader parsable;
	LinkedList<Cell> grid=new LinkedList<Cell>();
	LinkedList<Cell> propList=new LinkedList<Cell>();
	LinkedList<Integer> itemGUIDs; 
	private SpriteSheet texts;
	private SpriteSheet trans;
	//private SpriteSheet props;
	private int parsemode=0;
	private boolean mpstarted=false;
	private float xoprev;
	private float yoprev;
	String line;
	BufferedImage background;
	private Image fullMap;
	private boolean reloaded=true;
	private String outputFN;
	private LinkedList<String> spawnData=new LinkedList<String>();
	private boolean modified=false;

	
	public CellGrid(){
		modified=true;
		itemGUIDs=new LinkedList<Integer>();
		String name = JOptionPane.showInputDialog("Enter Grid File Name");
		if(!name.toLowerCase().contains(".txt")){
			name=name+".txt";
		}
		System.out.println("Drawing "+name);
		outputFN=JOptionPane.showInputDialog("Enter Output Image File Name");
		parseFile("strings/"+name);
		//pukeOutGridImageFile();
	}
	
	public CellGrid(int l, int h, int id, LinkedList<Integer> iG){
		setLength(l); //USELESS
		setHeight(h); //USELESS
		itemGUIDs=iG;
		guid=id;
		switch(id){
		case 0:
			outputFN="grid0";
			parseFile("strings/FullCellGrid0.txt");
			break;
		case 1:
			outputFN="grid1";
			parseFile("strings/FullCellGrid1.txt");
			break;
		case 2:
			outputFN="gridint0";
			parseFile("strings/transportcenter1.txt");
			break;
		case 3:
			outputFN="grid2";
			parseFile("strings/FullCellGrid2.txt");
			break;
		case 4:
			outputFN="gridint1";
			parseFile("strings/pyramidInterior.txt");
			break;
		case 5:
			outputFN="grid3";
			parseFile("strings/FullCellGrid3.txt");
			break;
		case 6:
			outputFN="grid4";
			parseFile("strings/FullCellGrid4.txt");
			break;
		default:
			outputFN="Error";
			break;
		}
	}

	public float getXOffset(){
		return xoffset;
	}
	public float getYOffset(){
		return yoffset;
	}

	//COMMENTED OUT PRINT STATEMENTS
	public void moveProgressive(int direction, long startTime){
		if(!mpstarted){
			//System.out.println("Started progressive move.");
			xoprev=xoffset;
			yoprev=yoffset;
			mpstarted=true;
		}
		//U0 R1 D2 L3
		if(Sys.getTime()>startTime+160){
			switch(direction){
			case 0:
				yoffset=yoprev+24;
				break;
			case 1:
				xoffset=xoprev-24;
				break;
			case 2:
				yoffset=yoprev-24;
				break;
			case 3:
				xoffset=xoprev+24;
				break;
			}
			mpstarted=false;
		}
		else if(Sys.getTime()>startTime+140){
			switch(direction){
			case 0:
				yoffset=yoprev+21;
				break;
			case 1:
				xoffset=xoprev-21;
				break;
			case 2:
				yoffset=yoprev-21;
				break;
			case 3:
				xoffset=xoprev+21;
				break;
			}
		}
		else if(Sys.getTime()>startTime+120){
			switch(direction){
			case 0:
				yoffset=yoprev+18;
				break;
			case 1:
				xoffset=xoprev-18;
				break;
			case 2:
				yoffset=yoprev-18;
				break;
			case 3:
				xoffset=xoprev+18;
				break;
			}
		}
		else if(Sys.getTime()>startTime+100){
			switch(direction){
			case 0:
				yoffset=yoprev+15;
				break;
			case 1:
				xoffset=xoprev-15;
				break;
			case 2:
				yoffset=yoprev-15;
				break;
			case 3:
				xoffset=xoprev+15;
				break;
			}
		}
		else if(Sys.getTime()>startTime+80){
			switch(direction){
			case 0:
				yoffset=yoprev+12;
				break;
			case 1:
				xoffset=xoprev-12;
				break;
			case 2:
				yoffset=yoprev-12;
				break;
			case 3:
				xoffset=xoprev+12;
				break;
			}
		}
		else if(Sys.getTime()>startTime+60){
			switch(direction){
			case 0:
				yoffset=yoprev+9;
				break;
			case 1:
				xoffset=xoprev-9;
				break;
			case 2:
				yoffset=yoprev-9;
				break;
			case 3:
				xoffset=xoprev+9;
				break;
			}
		}
		else if(Sys.getTime()>startTime+40){
			switch(direction){
			case 0:
				yoffset=yoprev+6;
				break;
			case 1:
				xoffset=xoprev-6;
				break;
			case 2:
				yoffset=yoprev-6;
				break;
			case 3:
				xoffset=xoprev+6;
				break;
			}
		}
		else if(Sys.getTime()>startTime+20){
			switch(direction){
			case 0:
				yoffset=yoprev+3;
				break;
			case 1:
				xoffset=xoprev-3;
				break;
			case 2:
				yoffset=yoprev-3;
				break;
			case 3:
				xoffset=xoprev+3;
				break;
			}
		}
	}

	public void move(int direction){
		//U0 R1 D2 L3
		//This will become useless with motion.
		switch(direction){
		case 0:
			yoffset+=24;
			break;
		case 1:
			xoffset-=24;
			break;
		case 2:
			yoffset-=24;
			break;
		case 3:
			xoffset+=24;
			break;
		default:
			System.out.println("Invalid direction value. You should NOT be seeing this.");
			break;
		}
	}

	//NEW!
	public void parseFile(String url){
		int pcount=0;
		int tcount=0;
		int icount=0;
		parsemode=0; //Standby
		try{ //YOU MIGHT BE ABLE TO REMOVE THIS!
			trans = new SpriteSheet("imgs/TransportSprites.png", 24, 24);
			//ADD PROPS SPRITESHEET HERE!
		}
		catch(Exception e){
			e.printStackTrace();
			System.out.println("SpriteSheet Fail.");
			return;
		}
		BufferedReader br = null;
		try{
			parsable = new FileReader(url);
			br = new BufferedReader(parsable);
			while((line=br.readLine())!=null){
				if(line.charAt(0)!='#'){
					if(line.charAt(0)=='@'){
						parsemode++;
					}
					else{
						if(parsemode==0){
							if(line.charAt(0)=='1'){
								modified=true;
							}
							if(line.charAt(1)=='1'){
								interior=true;
								wallType=Character.getNumericValue(line.charAt(2));
							}
						}
						if(parsemode==1){
							if(!initialized){
								setLength(line.length());
								setHeight(1);
								initialized=true;
							}
							else{
								if(line.length()!=length){
									System.out.println("BOUND ERROR!");
									return;
								}
								height++;

							}
							for(int i=0;i<length;i++){
								switch(line.charAt(i)){
								case '0':
									grid.add(new Cell(line.charAt(i), i, height-1, true, false, false));
									break;
								case 'G':
									grid.add(new Cell(line.charAt(i), i, height-1, true, (10*Math.random())>8.77, false));
									break;
								case 'g':
									grid.add(new Cell(line.charAt(i), i, height-1, false, ((10*Math.random())>9.46), true));
									break;
								case 's':
									grid.add(new Cell(line.charAt(i), i, height-1, false, false, true));
									break;
								case 'S':
									grid.add(new Cell(line.charAt(i), i, height-1, true, false, false));
									break;
								case 'd':
									grid.add(new Cell(line.charAt(i), i, height-1, false, false, true));
									break;
								case 'B':
									grid.add(new Cell(line.charAt(i), i, height-1, true, false, false));
									break;
								case 't':
									grid.add(new Cell(line.charAt(i), i, height-1, false, false, true));
									break;
								case 'T':
									grid.add(new Cell(line.charAt(i), i, height-1, true, false, false));
									break;
								case 'w':
									grid.add(new Cell(line.charAt(i), i, height-1, false, false, true));
									break;
								case 'p':
									grid.add(new Cell(line.charAt(i), i, height-1, false, false, true));
									break;
								case 'c':
									grid.add(new Cell(line.charAt(i), i, height-1, false, false, true));
									break;
								case 'i':
									grid.add(new Cell(line.charAt(i), i, height-1, false, false, true));
									break;
								case 'k':
									grid.add(new Cell(line.charAt(i), i, height-1, false, false, true));
									break;
								case 'f':
									grid.add(new Cell(line.charAt(i), i, height-1, false, false, true));
									break;
								case 'r':
									grid.add(new Cell(line.charAt(i), i, height-1, false, false, true));
									break;
								case 'D':
									grid.add(new Cell(line.charAt(i), i, height-1, true, false, false));
									break;
								case 'P':
									grid.add(new Cell(line.charAt(i), i, height-1, true, false, false));
									break;
								case 'F':
									grid.add(new Cell(line.charAt(i), i, height-1, true, false, false));
									break;
								case 'R':
									grid.add(new Cell(line.charAt(i), i, height-1, false, false, true));
								    break;
								default:
									System.out.println("Map matrix contained invalid character.");
									break;						
								}

							}
						}
						if(parsemode==2){
							if(line.charAt(0)!='#'){
								String[] dater=line.split("::");
								String content="";
								try{
									content=dater[3];
								}
								catch(Exception e){
									content="";
								}
								//FOR TEST 1x1 SOLIDIFICATION! MAY REMOVE OR ENHANCE!
								getCell(Integer.parseInt(dater[0]),Integer.parseInt(dater[1])).setSolid(true);
								getCell(Integer.parseInt(dater[0]),Integer.parseInt(dater[1])).setVariable(false);
								getCell(Integer.parseInt(dater[0]),Integer.parseInt(dater[1])).setSpawnable(false);
								//ADDS PROP
								getCell(Integer.parseInt(dater[0]),Integer.parseInt(dater[1])).setProp(new Prop(Integer.parseInt(dater[2]),content));
								//ADD TO PROP CELL LIST (IF NOT DRAWN ONTO BACKGROUND LIKE BUILDINGS)
								if(getCell(Integer.parseInt(dater[0]),Integer.parseInt(dater[1])).getProp().getType()!=1){
									propList.add(getCell(Integer.parseInt(dater[0]),Integer.parseInt(dater[1])));
								}
								pcount++;
							}
						}
						if(parsemode==3){
							String[] dater=line.split("::");
							switch(Integer.parseInt(dater[2])){
							//U0 R1 D2 L3
							case 0:
								//YOU DID MAKE THIS A FUNCTION IN THE CELL CLASS!
								getCell(Integer.parseInt(dater[0]),Integer.parseInt(dater[1])).makeTransporter(Integer.parseInt(dater[3]), Integer.parseInt(dater[4]), Integer.parseInt(dater[5]), Integer.parseInt(dater[2]), trans.getSubImage(1, 0));
								break;
							case 1:
								getCell(Integer.parseInt(dater[0]),Integer.parseInt(dater[1])).makeTransporter(Integer.parseInt(dater[3]), Integer.parseInt(dater[4]), Integer.parseInt(dater[5]), Integer.parseInt(dater[2]), trans.getSubImage(1, 1));
								break;
							case 2:
								getCell(Integer.parseInt(dater[0]),Integer.parseInt(dater[1])).makeTransporter(Integer.parseInt(dater[3]), Integer.parseInt(dater[4]), Integer.parseInt(dater[5]), Integer.parseInt(dater[2]), trans.getSubImage(0, 1));
								break;
							case 3:
								getCell(Integer.parseInt(dater[0]),Integer.parseInt(dater[1])).makeTransporter(Integer.parseInt(dater[3]), Integer.parseInt(dater[4]), Integer.parseInt(dater[5]), Integer.parseInt(dater[2]), trans.getSubImage(0, 0));
								break;
							case 4:
								getCell(Integer.parseInt(dater[0]),Integer.parseInt(dater[1])).makeTransporter(Integer.parseInt(dater[3]), Integer.parseInt(dater[4]), Integer.parseInt(dater[5]), Integer.parseInt(dater[2]), trans.getSubImage(2, 0));
								break;
							case 5:
								getCell(Integer.parseInt(dater[0]),Integer.parseInt(dater[1])).makeTransporter(Integer.parseInt(dater[3]), Integer.parseInt(dater[4]), Integer.parseInt(dater[5]), Integer.parseInt(dater[2]), trans.getSubImage(2, 1));
								break;
							case 6:
								getCell(Integer.parseInt(dater[0]),Integer.parseInt(dater[1])).makeTransporter(Integer.parseInt(dater[3]), Integer.parseInt(dater[4]), Integer.parseInt(dater[5]), Integer.parseInt(dater[2]), trans.getSubImage(3, 0));
								break;
							default:
								break;
							}
							try{
								String ld=dater[6];
								System.out.println("This door is locked with data: "+ld);
								getCell(Integer.parseInt(dater[0]),Integer.parseInt(dater[1])).getProp().setSprite(new Image("imgs/lock.png"), "imgs/lock.png");
								getCell(Integer.parseInt(dater[0]),Integer.parseInt(dater[1])).lock(ld);
								propList.add(getCell(Integer.parseInt(dater[0]),Integer.parseInt(dater[1]))); //BECAUSE IT HAS TO DRAW LOCK
								//MUST MODIFY INTERACTION FOR UNLOCKING!
							}
							catch(Exception e){

							}
							tcount++;
							//I think I add this to the propList... Yes, BUT now that trasporters are drawn onto the map, you don't.
							//propList.add(getCell(Integer.parseInt(dater[0]),Integer.parseInt(dater[1])));
						}
						if(parsemode==4){
							//Item Pass
							String[] items=line.split("::");
							//Conditional for spawning only once
							if(!itemGUIDs.contains(Integer.parseInt(items[3]))){
								//If item isn't already taken. (See Item and World classes)
								getCell(Integer.parseInt(items[0]),Integer.parseInt(items[1])).putItemHere(new Item(Integer.parseInt(items[2]),Integer.parseInt(items[3])));
								propList.add(getCell(Integer.parseInt(items[0]),Integer.parseInt(items[1])));
							}
							icount++;
						}
						if(parsemode==5){
							//This is to get NPC Spawn data from cell grid parse file.
							//CellGrid does NOT handle NPC spawning
							spawnData.add(line);
						}
					}
				}
			}
			System.out.println("Map is "+length+"x"+height);
			br.close();
			if(modified){
				pukeOutGridImageFile();
			}
			//Set as fullMap
			reloaded=true;
			fullMap=new Image("imgs/"+outputFN+".png");
		}

		catch(Exception e){
			System.out.println("MAP PARSE ERROR! @"+parsemode);
		}
	}


	public void pukeOutGridImageFile(){
		int total=0;
		BufferedImage bi;
		try{
			System.out.println("Initializing tile file.");
			BufferedImage walls = ImageIO.read(new File("imgs/walls.png"));
			BufferedImage tiles = ImageIO.read(new File("imgs/tiles.png"));
			BufferedImage fences = ImageIO.read(new File("imgs/fence2.png"));
			BufferedImage transporters = ImageIO.read(new File("imgs/TransportSprites.png"));
			System.out.println("Tile file read. Initializing output.");
			BufferedImage output = new BufferedImage(length*24,height*24,BufferedImage.TYPE_INT_ARGB);
			System.out.println("Output initialized.");
			System.out.println("Attempting to initialize java.awt.Graphics.");
			java.awt.Graphics g2=output.getGraphics();
			for(int k=0;k<height;k++){
				for(int j=0;j<length;j++){
					switch(grid.get(total).getSymbol()){
					case '0':
						bi = tiles.getSubimage(0, 0, 24, 24);
						g2.drawImage(bi,j*24,k*24,null);
						if(interior && checkWallable(total)){
							g2.drawImage(walls.getSubimage(wallType*24,0,24,24), j*24, k*24, null);
						}
						break;
					case 'G':
						bi = tiles.getSubimage(24, 0, 24, 24);
						g2.drawImage(bi,j*24,k*24,null);
						if(grid.get(total).isVariable()){
							if(total!=grid.size()-1 && !grid.get(total+1).hasProp()){
								//^This is to prevent it from drawing flowers under tree trunks specifically
								g2.drawImage(tiles.getSubimage(72,72,24,24),j*24,k*24,null);
							}
						}
						break;
					case 'g':
						g2.drawImage(tiles.getSubimage(24, 0, 24, 24),j*24,k*24,null);
						if(grid.get(total).isVariable()){
							g2.drawImage(tiles.getSubimage(96,72,24,24),j*24,k*24,null);
						}
						break;
					case 's':
						g2.drawImage(tiles.getSubimage(72, 0, 24, 24),j*24,k*24,null);
						break;
					case 'S':
						g2.drawImage(tiles.getSubimage(72, 0, 24, 24),j*24,k*24,null);
						break;
					case 'd':
						g2.drawImage(tiles.getSubimage(48, 0, 24, 24),j*24,k*24,null);
						break;
					case 'B':
						if(total+length<grid.size() && grid.get(total+length).getSymbol()=='B'){
							g2.drawImage(tiles.getSubimage(0, 72, 24, 24),j*24,k*24,null);
						}
						else if(total+length>=grid.size()){
							g2.drawImage(tiles.getSubimage(0, 72, 24, 24),j*24,k*24,null);
						}
						else{
							g2.drawImage(ImageIO.read(new File("imgs/blocktile.png")),j*24,k*24,null);
						}
						break;
					case 't':
						g2.drawImage(tiles.getSubimage(72, 48, 24, 24),j*24,k*24,null);
						break;
					case 'T':
						g2.drawImage(tiles.getSubimage(72, 48, 24, 24),j*24,k*24,null);
						break;
					case 'w':
						g2.drawImage(tiles.getSubimage(24, 48, 24, 24),j*24,k*24,null);
						break;
					case 'p':
						g2.drawImage(tiles.getSubimage(24, 72, 24, 24),j*24,k*24,null);
						break;
					case 'c':
						g2.drawImage(tiles.getSubimage(72, 24, 24, 24),j*24,k*24,null);
						break;
					case 'i':
						g2.drawImage(tiles.getSubimage(48, 72, 24, 24),j*24,k*24,null);
						break;
					case 'k':
						g2.drawImage(tiles.getSubimage(24, 24, 24, 24),j*24,k*24,null);
						break;
					case 'f':
						g2.drawImage(tiles.getSubimage(48, 24, 24, 24),j*24,k*24,null);
						break;
					case 'r':
						g2.drawImage(tiles.getSubimage(48, 48, 24, 24),j*24,k*24,null);
						break;
					case 'D':
						g2.drawImage(tiles.getSubimage(96, 24, 24, 24),j*24,k*24,null);
						break;
					case 'P':
						g2.drawImage(tiles.getSubimage(96, 48, 24, 24),j*24,k*24,null);
						break;
					case 'F':
						g2.drawImage(tiles.getSubimage(24, 0, 24, 24),j*24,k*24,null);
						g2.drawImage(selectSpecShapeTile(getSurroundData('F',total),fences),j*24,k*24,null);
						break;
					case 'R':
						g2.drawImage(tiles.getSubimage(0, 48, 24, 24),j*24,k*24,null);
						break;
					default:
						break;	
					}
					if(grid.get(total).isTransporter()){
						switch(grid.get(total).getTransType()){
						case 0:
							g2.drawImage(transporters.getSubimage(24, 0, 24, 24), j*24, k*24, null);
							break;
						case 1:
							g2.drawImage(transporters.getSubimage(24, 24, 24, 24), j*24, k*24, null);
							break;
						case 2:
							g2.drawImage(transporters.getSubimage(0, 24, 24, 24), j*24, k*24, null);
							break;
						case 3:
							g2.drawImage(transporters.getSubimage(0, 0, 24, 24), j*24, k*24, null);
							break;
						case 4:
							g2.drawImage(transporters.getSubimage(48, 0, 24, 24), j*24, k*24, null);
							break;
						case 5:
							g2.drawImage(transporters.getSubimage(48, 24, 24, 24), j*24, k*24, null);
							break;
						case 6:
							g2.drawImage(transporters.getSubimage(72, 0, 24, 24), j*24, k*24, null);
							break;
						default:
							System.out.println("YOU NEED TO UPDATE PUKEOUTGRIDIMAGE() TRANSPORTER WRITER.");
						}
					}
					total++;
				}
			}
			//BEGIN BUILDING SOLIDIFY TEST!
			total=0;
			for(int k=0;k<height;k++){
				for(int j=0;j<length;j++){
					if(grid.get(total).hasProp() && grid.get(total).getProp().getType()==1){
						g2.drawImage(ImageIO.read(new File(grid.get(total).getProp().getSpriteURL())), j*24-grid.get(total).getPropImage().getWidth()+24, k*24-grid.get(total).getPropImage().getHeight()+24, null);
					}
					total++;
				}
			}
			//END BUILDING SOLIDIFY TEST!

			//File make
			ImageIO.write(output,"PNG",new File("imgs",(outputFN+".png")));
			System.out.println("Puked out background image!");
		}
		catch(Exception e){
			e.printStackTrace();
			System.out.println("Failed to make image.");
		}
	}
	
	public boolean checkWallable(int here){
		if(here+length>grid.size()-1){
			return false;
		}
		if(grid.get(here+length).getSymbol()!='0'){
			return true;
		}
		return false;
	}
	
	public int getSurroundData(char sur, int here){
		int retval=-1; //No surroundings by default
		boolean isTopRow=false;
		boolean isBtmRow=false;
		boolean isLeftSide=false;
		boolean isRightSide=false;
		boolean onUp=false;
		boolean onDown=false;
		boolean onLeft=false;
		boolean onRight=false;
		//Check if it's on an edge
		if(here-length<0){
			isTopRow=true;
		}
		else if(here+length>grid.size()-1){
			isBtmRow=true;
		}
		if(here%length==0){
			isLeftSide=true;
		}
		else if((here+1)%length==0){
			isRightSide=true;
		}
		//Check surrounding cells
		if(!isTopRow){
			if(grid.get(here-length).getSymbol()==sur){
				onUp=true;
			}
		}
		if(!isBtmRow){
			if(grid.get(here+length).getSymbol()==sur){
				onDown=true;
			}
		}
		if(!isLeftSide && here!=0){
			if(grid.get(here-1).getSymbol()==sur){
				onLeft=true;
			}
		}
		if(!isRightSide && here+1<grid.size()){
			if(grid.get(here+1).getSymbol()==sur){
				onRight=true;
			}
		}
		//Now we have sides
		//-1=none
		//0=up
		//1=right
		//2=down
		//3=left
		//4=UpDown
		//5=LeftRight
		//6=UR
		//7=RD
		//8=DL
		//9=LU
		//10=URD
		//11=RDL
		//12=DLU
		//13=LUR
		//14=X(All)
		if(onUp && onDown && onLeft && onRight){
			retval=14;
		}
		else if(onUp && onDown){
			if(onLeft){
				retval=12;
			}
			else if(onRight){
				retval=10;
			}
			else{
				retval=4;
			}
		}
		else if(onLeft && onRight){
			if(onUp){
				retval=13;
			}
			else if(onDown){
				retval=11;
			}
			else{
				retval=5;
			}
		}
		else if(onUp){
			if(onLeft){
				retval=9;
			}
			else if(onRight){
				retval=6;
			}
			else{
				retval=0;
			}
		}
		else if(onDown){
			if(onLeft){
				retval=8;
			}
			else if(onRight){
				retval=7;
			}
			else{
				retval=2;
			}
		}
		else{
			if(onRight){
				retval=1;
			}
			else if(onLeft){
				retval=3;
			}
		}
		return retval;
	}
	
	public BufferedImage selectSpecShapeTile(int s, BufferedImage sheet){
		int subx=0;
		int suby=0;
		switch(s){
		case -1: //none
			subx=0;
			suby=0;
			break;
		case 0: //up
			subx=1;
			suby=0;
			break;
		case 1: //right
			subx=2;
			suby=0;
			break;
		case 2: //down
			subx=3;
			suby=0;
			break;
		case 3: //left
			subx=0;
			suby=1;
			break;
		case 4: //UD
			subx=1;
			suby=1;
			break;
		case 5: //LR
			subx=2;
			suby=1;
			break;
		case 6: //UR
			subx=3;
			suby=1;
			break;
		case 7: //RD
			subx=0;
			suby=2;
			break;
		case 8: //DL
			subx=1;
			suby=2;
			break;
		case 9: //LU
			subx=2;
			suby=2;
			break;
		case 10: //URD
			subx=3;
			suby=2;
			break;
		case 11: //RDL
			subx=0;
			suby=3;
			break;
		case 12: //DLU
			subx=1;
			suby=3;
			break;
		case 13: //LUR
			subx=2;
			suby=3;
			break;
		case 14: //ALL
			subx=3;
			suby=3;
			break;
		default:
			break;
		}
		return sheet.getSubimage(subx*24, suby*24, 24, 24);
	}

	public void drawBackgroundEtAl(Graphics g){
		if(reloaded){
			g.flush();
			reloaded=false;
		}
		g.drawImage(fullMap,xoffset,yoffset);
		//}
		//else{
		//g.drawImage(fullMap2,xoffset,yoffset);
		//}
		for(int i=0;i<propList.size();i++){
			Cell cref=propList.get(i);
			if(cref.getProp().isDestroyed()){
				cref.setProp(null);
				propList.remove(i);
			}
			else{
				int j=cref.getXloc();
				int k=cref.getYloc();
				Prop pref=cref.getProp();
				if(j*24+xoffset-pref.getXsize()*24<640 && k*24+yoffset-pref.getYsize()*24<480 && j*24+xoffset>-24 && k*24+yoffset>-24){
					g.drawImage(cref.getPropImage(), j*24+xoffset-cref.getPropImage().getWidth()+24,k*24+yoffset-cref.getPropImage().getHeight()+24);
				}

			}
		}
		//Load a prop cell storage object/data structure to draw them all efficiently
		//Draw Background
		//Draw props/transporters
	}

	public void drawGrid(Graphics g){
		//System.out.println(xoffset+","+yoffset);
		int total=0;
		for(int k=0;k<height;k++){
			for(int j=0;j<length;j++){
				if(j*24+xoffset<640 && k*24+yoffset<480 && j*24+xoffset>-24 && k*24+yoffset>-24){
					g.drawImage(grid.get(total).getTex(), j*24+xoffset,k*24+yoffset);
					if(grid.get(total).isVariable()){
						if(grid.get(total).isSolid()){
							g.drawImage(texts.getSubImage(3, 3), j*24+xoffset,k*24+yoffset); //Draws random red flowers
						}
						else{
							g.drawImage(texts.getSubImage(4, 3), j*24+xoffset,k*24+yoffset); //Draws random blue flowers
						}
					}
				}

				//TEST
				if(grid.get(total).hasProp()){
					Prop ref=grid.get(total).getProp();
					if(j*24+xoffset-ref.getXsize()*24<640 && k*24+yoffset-ref.getYsize()*24<480 && j*24+xoffset>-24 && k*24+yoffset>-24){
						g.drawImage(grid.get(total).getPropImage(), j*24+xoffset-grid.get(total).getPropImage().getWidth()+24,k*24+yoffset-grid.get(total).getPropImage().getHeight()+24);
					}
				}
				//END TEST

				total++;
			}
		}
		/*
		//SEPARATE PROP DRAW TEST! (2nd pass)
		for(int k=0;k<height;k++){
			for(int j=0;j<length;j++){
				if(grid.get(k*length+j).hasProp()){
					g.drawImage(grid.get(k*length+j).getProp().getSprite(),j*24+xoffset,k*24+yoffset); //EDIT DRAW LOCATION!!!
				}
			}
		}
		//END NESTED TEST
		 */

	}

	public Cell getCell(int x, int y){
		if(x>length-1 || y>height-1){
			System.out.println("!GETCELL BOUND ERROR!");
		}
		return grid.get(y*length+x);
	}


	public void setLength(int l) {
		length = l;
	}

	public int getLength() {
		return length;
	}

	public void setHeight(int h) {
		height = h;
	}

	public int getHeight() {
		return height;
	}

	public void centerOnPlayer(){
		for(int i=0;i<grid.size();i++){
			if(grid.get(i).isPlayerLoc()){
				xoffset=312-grid.get(i).getXloc()*24;
				yoffset=216-grid.get(i).getYloc()*24;
				System.out.println("Found player at: "+grid.get(i).getXloc()+","+grid.get(i).getYloc());
				break;
				//312*216 is center where player is drawn.
			}
		}
	}

	public Cell findPlayer(){
		for(int i=0;i<grid.size();i++){
			if(grid.get(i).isPlayerLoc()){
				return grid.get(i);
			}
		}
		return null;
	}

	public int getGUID(){
		return guid;
	}

	public LinkedList<String> getSpawnData(){
		return spawnData;
	}

	public LinkedList<Integer> getItemGUIDs(){
		return itemGUIDs;
	}
}
