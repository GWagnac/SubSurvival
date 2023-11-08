package bagel;

// create list of spripackage bagel;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import javafx.scene.Scene;
import java.util.ArrayList;
import javafx.scene.paint.Color;

import javafx.scene.canvas.*;
import javafx.animation.*;

// abstract class: contains abstract method(s)
// can not create an instance of this class;
//  must write extending class that defines abstract methods.
public abstract class Game extends Application
{
    public int screenWidth;
    public int screenHeight;

    public Canvas canvas;

    public Stage stage;

    //Input object to store keyboard inputs
    public Input input;

    public Game()
    {

    }

    // game-specific code to initialize sprite objects
    //  abstract method: code MUST be added by an extending class
    public abstract void initialize();

    public abstract void update();

    public void start(Stage mainStage)
    {
        // create a Scene, using a Pane to organize
        Pane root = new Pane();
        Scene mainScene = new Scene(root, 800, 600); 
        // attach Scene to Stage
        mainStage.setScene( mainScene );
        mainStage.setResizable(false);
        stage = mainStage;
        screenWidth = 640;
        screenHeight = 400;

        input = new Input(mainScene);

        // create a list to store groups
        groupList = new ArrayList<Group>();

        // canvas displays images
        canvas = new Canvas(screenWidth, screenHeight);
        // attach canvas to scene
        root.getChildren().add( canvas );
        // context has methods for drawing images
        GraphicsContext context = 
            canvas.getGraphicsContext2D();

        // create the loop that runs 60 times per second
        AnimationTimer gameloop = new AnimationTimer()
            {
                // code to run
                public void handle(long nanoseconds)
                {
                    // refresh canvas by making all pixels
                    //   a solid color (black)
                    context.fillRect(0,0, screenWidth,screenHeight);
                    // update input key lists                    
                    input.update();
                    update();

                    // draw all sprites, in all groups, in the group list.
                    for (Group g : groupList)
                    {
                        g.update( 1.0 / 60.0 );
                        for ( Sprite s : g.getSpriteList() )
                        {
                            // if destroy function was called,
                            //  remove sprite from the group that contains it.
                            if (s.destroySignal == true)
                                g.removeSprite(s);
                                
                            s.draw(context);
                        }
                    }
                }
            };

        // initialize game sprites
        initialize();

        // start the game loop
        gameloop.start();

        // make stage visible
        mainStage.show();
    }

    public void setScreenSize(int newScreenWidth, int newScreenHeight)
    {
        screenWidth = newScreenWidth;
        screenHeight = newScreenHeight;

        canvas.setWidth(newScreenWidth);
        canvas.setHeight(newScreenHeight);

        stage.setWidth(newScreenWidth);
        stage.setHeight(newScreenHeight);

    }

    public void setTitle(String title){
        stage.setTitle(title);
    }

    // list of groups, each of which contains a list of sprites 
    //   (to stay organized)
    public ArrayList<Group> groupList;
    
    public void createGroup(String groupName)
    {
        Group g = new Group(groupName);
        groupList.add(g);
    }

    /**
     * Get the group with the given name from the list of all groups.
     *
     * @param groupName the name of the group
     * @return the group with the given name
     */
    public Group getGroup(String groupName)
    {
        for (Group g : groupList)
        {
            if ( g.name.equals(groupName) )
                return g;
        }
        
        // if this line is reached, there is no group with that name
        
        // option 1: print message in Java console
        // System.out.println("There is no group with the name: " + groupName);

        // option 2: print message as an error (red font)
        // System.err.println("There is no group with the name: " + groupName);

        // option 3: print error and stop program (throw Exception)
        throw new RuntimeException("There is no group with the name: " + groupName);
    }
    
    /**
     * Add a sprite to the group with the given name.
     *
     * @param sprite sprite to be added
     * @param groupName name of the group to add the sprite to
     */
    public void addSpriteToGroup(Sprite sprite, String groupName)
    {
        // Group g = getGroup( groupName );
        // g.addSprite( sprite );
        
        // or, more efficiently:
        getGroup( groupName ).addSprite( sprite );
    }
    
    /**
     * Remove a sprite from the group with the given name
     *
     * @param sprite the sprite to be removed
     * @param groupName name of the group that the sprite is in
     */
    public void removeSpriteFromGroup(Sprite sprite, String groupName)
    {
        getGroup( groupName ).removeSprite( sprite );
    }
    
    /**
     * Get the list of sprites in the group with the given name;
     *   useful in for-loops, when interacting with a specific type of sprite.
     *
     * @param groupName the name of the group
     * @return the list of sprites in that group
     */
    public ArrayList<Sprite> getGroupSpriteList(String groupName)
    {
        return getGroup( groupName ).getSpriteList();
    }
    
    /**
     * Return the number of sprites in the group with the given name
     *
     * @param groupName the name of the group
     * @return the number of sprites in the group's sprite list
     */
    public int getGroupSpriteCount(String groupName)
    {
        return getGroup( groupName ).getSpriteCount();
    }

}

