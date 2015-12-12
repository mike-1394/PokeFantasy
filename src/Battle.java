import org.lwjgl.Sys;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;


public class Battle {

	private int stage;
	private long startAnimTimer;

	public Battle(){
		
	}

	public void startBattle(){
		stage=0;
		startAnimTimer=Sys.getTime();
	}
	
	public void progressBattle(){
		if(stage==0){
			if(Sys.getTime()>startAnimTimer+400){
				stage=1;
			}
		}
	}




	public void drawBattle(Graphics g){
		if(stage==0){
			g.setColor(Color.white);
			g.fillRect(0,0,640*(Sys.getTime()-startAnimTimer)/400,480);
		}
		else{
			g.setColor(Color.white);
			g.fillRect(0, 0, 640, 480); //Background
		}
		g.setColor(Color.black);
		g.drawString("Battle stage:"+stage, 0, 0);
	}

}
