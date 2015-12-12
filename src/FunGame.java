import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.*;

//WELCOME BACK! Good job fixing that prop io error.
//Add interaction confirmation dialog.
//and have update itterate the interaction method, then render draws the interaction data.
//DECIDE WHETHER PROPS SHOULD BE RENDERED DURING CELL GRID DRAW OR AFTER (BUT STILL BEFORE PLAYER AND NPCS)
//FIX BUILDING PROP STUFF (size, choice, etc..)
//CLEAN UP CELL CODE AND CONSTRUCTOR.
//May be able to remove cell images and certain prop images.
//Add isPath to Cell class- maybe for better ai and for battles off-path if CG vars permit
//Check for lw call frequency in nested get..() statements. Would using a ref pointer be more efficient?
//CLEAN UP PROP CODE (remove teleporter data- that is handled by Cells and CellGrid) (Prop subtypes?)
//Add 1*1 props to their own spritesheet. (See if that's more efficient. Fewer files at least.)
//ABSTRACTIFY NPC GENERATION AND CLEAN UP THE CODE
//FIX BEN FOLLOW AI
//FIX WORLD MOVE ANIMATION DELAY
//FIX JILL ANIMATION PNG!
//FIX BEN ANIMATION PNG!
//CLEAN UP CODE!
//Oh, also, I'd feel safer if stuff was blocked by a null cell so I could put those right on the edge of the map...
//UMM... SEE IF THERE'S ANYTHING YOU CAN/NEED TO DO ABOUT CLOCK OVERFLOW? RE-CENTER ON PLAYER? SNAP ALL?
//TRY TO RECREATE OFFSET GLITCH AND FIGURE OUT HOW TO FIX IT! I REFUSE TO CALL CENTERONPLAYER() EVERY NON-MOTION CYCLE!

public class FunGame extends BasicGame
{
    boolean declared=false;
    Input input;
    Universe uni;

    public FunGame(){
        super("The Jill Game!");
    }
    @Override
    public void init(GameContainer gc) throws SlickException
    {
        System.out.println("Welcome to the Jill Game!");
        uni=new Universe();
    }
    @Override
    public void update(GameContainer gc, int delta) throws SlickException
    {
        input=gc.getInput();
        if(input.isKeyPressed(Keyboard.KEY_RETURN)){
            declared=true;
        }
        if(uni.needsMouse()){
            if(input.isKeyPressed(Keyboard.KEY_UP)){
                uni.navigateUI(0);
            }
            else if(input.isKeyPressed(Keyboard.KEY_DOWN)){
                uni.navigateUI(2);
            }
            else if(input.isKeyPressed(Keyboard.KEY_RIGHT)){
                uni.navigateUI(1);
            }
            else if(input.isKeyPressed(Keyboard.KEY_LEFT)){
                uni.navigateUI(3);
            }
            uni.setMouse(input.getMouseX(), input.getMouseY());
            if(input.isMousePressed(Input.MOUSE_LEFT_BUTTON)){
                uni.clickMouse();
                input.clearKeyPressedRecord();
            }
        }
        if(declared){
            if(!uni.isMoving() || !uni.directionDown()){
                if(input.isKeyPressed(Keyboard.KEY_R)){
                    uni.repopulate();
                    return;
                }
                if(input.isKeyPressed(Keyboard.KEY_T)){
                    uni.triggerTestQuest();
                    //uni.triggerBattle(0);
                    return;
                }
                if(input.isKeyPressed(Keyboard.KEY_Q)){
                    uni.openUI(1);
                    //uni.printTestQuestInfo();
                    return;
                }
                if(input.isKeyPressed(Keyboard.KEY_B)){
                    uni.printDebugInfo();
                    return;
                }
                if(input.isKeyPressed(Keyboard.KEY_I)){
                    //uni.printInventory();
                    uni.openUI(0);
                    input.clearKeyPressedRecord();
                    return;
                }
                if(input.isKeyPressed(Keyboard.KEY_S)){
                    return;
                }
                if(input.isKeyPressed(Keyboard.KEY_SPACE)){
                    uni.interact();
                    return;
                }
            }
            if(input.isKeyPressed(Keyboard.KEY_M)){
                System.out.println("Player ID change triggered.");
                uni.manipulate();
            }
            //Maybe stop movings should be first... then starts... with returns.
            //Cell offset snapping randomly occurs in house cell grid when walking N/S...
            if(input.isKeyDown(Keyboard.KEY_UP)){
                uni.startMovingUp();
            }
            if(input.isKeyDown(Keyboard.KEY_RIGHT)){
                uni.startMovingRight();
            }
            if(input.isKeyDown(Keyboard.KEY_DOWN)){
                uni.startMovingDown();
            }
            if(input.isKeyDown(Keyboard.KEY_LEFT)){
                uni.startMovingLeft();
            }
            if(!input.isKeyDown(Keyboard.KEY_UP)){
                uni.stopMovingUp();
            }
            if(!input.isKeyDown(Keyboard.KEY_RIGHT)){
                uni.stopMovingRight();
            }
            if(!input.isKeyDown(Keyboard.KEY_DOWN)){
                uni.stopMovingDown();
            }
            if(!input.isKeyDown(Keyboard.KEY_LEFT)){
                uni.stopMovingLeft();
            }
            uni.moveTheUniverse();
            uni.simulate();
        }
    }

    public void render(GameContainer gc, Graphics g) throws SlickException
    {
        //Draw stuff below this.
        g.setColor(Color.black);
        uni.drawTheUniverse(g);
        g.setColor(Color.white);
        g.drawString("MEM T(U):" + (Runtime.getRuntime().totalMemory()/1000000) + "(" + ((Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory())/1000000) + ") MB", 5, 120);
        //For initial text box:
        if(!declared){
            g.setColor(Color.white);
            g.fillRect(0, 0, 640, 480);
            g.setColor(Color.black);
            g.drawString("Welcome to Codename: The Jill Game", 20, 20);
            g.drawString("There is no point to the game yet.", 20, 100);
            g.drawString("Arrow keys move character. Space to interact.", 20, 260);
            g.drawString("R spawns new NPCs. M cycles the character's appearance.",20,280);
            g.drawString("I opens your inventory.", 20, 300);
            g.drawString("T triggers test quest, Q shows objective. (Quest sys still a WIP)",20,320);
            g.drawString("Press enter to continue.", 20, 400);
        }

    }

    public static void main(String[] args) throws SlickException
    {
        AppGameContainer app = new AppGameContainer(new FunGame());
        app.setDisplayMode(640, 480, false);
        app.setTargetFrameRate(60);
        app.setShowFPS(false);
        app.start();
    }
}