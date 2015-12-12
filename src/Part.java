import org.newdawn.slick.*;

public class Part {
	//Parts are typically 1-cell objects, textures, or images.
	//At least, they are for now.
	private int type;
	private SpriteSheet textures;
	private boolean solid;
	private int indexx=0;
	private int indexy=0;

	public Part(int t, SpriteSheet tex, boolean s){
		type=t;
		textures=tex;
		solid=s;
	}

	public boolean isSolid(){
		return solid;
	}

	public String typeIs(){
		switch(type){
		case 0:
			return "floor";
		case 1:
			return "wall";
		case 2:
			return "furniture";
		default:
			return "misc";
		}
	}

	public void drawPart(Graphics g, float x, float y){
		g.drawImage(textures.getSprite(indexx, indexy), x, y);
	}

	public void nextSprite(){
		if(textures.getHorizontalCount()>1 && indexx!=textures.getHorizontalCount()-1){
			indexx++;
		}
		else{
			indexx=0;
			if(textures.getVerticalCount()>1 && indexy!=textures.getVerticalCount()-1){
				indexy++;
			}
			else{
				indexy=0;
			}

		}
	}
}