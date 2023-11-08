import bagel.*;

/**
 * Write a description of class SubSurvival here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class SubSurvival extends Game
{
    // instance variables - replace the example below with your own
    Sprite water;
    Sprite core;
    Label scoreLabel;
    Label coreLabel;
    public int score;
    public int coreHP; 
    Sprite explosion;
    Sprite player;
    public Sprite bullet;
    public Texture enemyTexture;
    public Sprite loseMessage;
    
    // keep track of how much time has passed since last enemy created
    public double enemyTimer;
    // keep track of current enemy speed
    public double enemySpeed;
    // keep track of how quickly enemy planes appear
    public double enemySpawnRate;

    
    public void initialize()
    {
          setScreenSize(640,480);
        
          createGroup("main");
          createGroup("bullets");
          createGroup("enemy");
          createGroup("item");
          
          water = new Sprite(); 
          water.setTexture(new Texture("images/water.png"));
          water.setPosition(0,0);
          water.setSize(640, 480);
          addSpriteToGroup( water, "main" );
          
          player = new Sprite();
          player.setTexture( new Texture("images/player.png") );
          player.setPosition( 400,300 );
          player.setSize(64, 64);
          // need to set physics: acceleration value, max speed, deceleration value
          player.setPhysics( new Physics(400, 200, 400) );
          addSpriteToGroup( player, "main" );
          
          enemyTexture = new Texture("images/enemy.png");
          enemyTimer = 0;
          enemySpeed = 100;
          enemySpawnRate = 3;
          
          core = new Sprite();
          core.setTexture( new Texture("images/core.png"));
          core.setPosition(5,20);
          core.setSize(150,450);
          addSpriteToGroup(core, "main");
          
          createGroup("labels");
          score = 0;
          coreHP = 100; 
          
          scoreLabel = new Label();
          scoreLabel.setText("Score: " + score);
          scoreLabel.setPosition( 20, 50 );
          scoreLabel.setFont("Impact", 48 );
          // draw text in yellow
          scoreLabel.setColor(1.00, 1.00, 0.00);
          addSpriteToGroup( scoreLabel, "labels" );
          
          
          coreLabel = new Label();
          coreLabel.setText("Core HP: " + coreHP);
          coreLabel.setPosition( 350, 50 );
          coreLabel.setFont("Impact", 48 );
          // draw text in yellow
          coreLabel.setColor(1.00, 1.00, 0.00);
          addSpriteToGroup( coreLabel, "labels" );
    
        
          loseMessage = new Sprite();
          loseMessage.setTexture( new Texture("images/message-lose.png") );
          loseMessage.setPosition(160, 140);
          loseMessage.visible = false;
          addSpriteToGroup( loseMessage, "labels");
    }
    
    public void update()
    {
        if ( loseMessage.visible == true)
            return;
        
        if (input.isKeyPressing("W"))
            player.physics.accelerateAtAngle( 270 );
        if (input.isKeyPressing("A"))
            player.physics.accelerateAtAngle( 180 );
        if (input.isKeyPressing("S"))
            player.physics.accelerateAtAngle( 90 );
        if (input.isKeyPressing("D"))
            player.physics.accelerateAtAngle( 0 );
            
        if (input.isKeyPressed("SPACE"))
        {
          bullet = new Sprite();
          bullet.setTexture(new Texture ("images/bullet.png"));
          bullet.setSize(16, 16);
          bullet.alignToSprite(player);
          bullet.setPhysics(new Physics(0,640,0));
          bullet.physics.setSpeed(640);
          bullet.physics.setMotionAngle(player.angle);
          bullet.addAction(Action.wrap(640, 480));
          
          Action[] actionArray = {Action.delay(0.5), Action.destroy()};
          bullet.addAction( Action.sequence(actionArray) );
          addSpriteToGroup(bullet, "bullets");
          
        }
        
        
        enemyTimer += 1.0 / 60.0;
         if (enemyTimer >= enemySpawnRate)
        {
            // create a new enemy at a random position
            Sprite enemy = new Sprite();
            enemy.setTexture( enemyTexture );
            // appear at random y coordinate
            double y = Math.random() * 600;
            enemy.setPosition( 600, y );
            enemy.setPhysics( new Physics(0, 600, 0) );
            // increase the base speed for new enemies
            enemySpeed += 6;
            enemy.physics.setSpeed(enemySpeed);
            enemy.physics.setMotionAngle(180);
            Action[] actionArray2 = { Action.delay(6), Action.destroy() };
            enemy.addAction( Action.sequence(actionArray2) );
            
            
            addSpriteToGroup(enemy, "enemy");
            // reset the timer
            enemyTimer = 0;
            // make enemies spawn more quickly;
            // time until next enemy spawns
            enemySpawnRate -= 0.01;
            // do not let spawn rate get too fast
            if (enemySpawnRate < 0.25)
                enemySpawnRate = 0.25;
        }
        
        for (Sprite enemy : getGroupSpriteList("enemy"))
        {
                if (bullet.overlap(enemy))
                {
                    enemy.destroy();
                    
                    // explosion
                    Sprite explosion = new Sprite();
                    Animation explosionAnim = new Animation("images/explosion.png", 8, 8, 0.01,false);
                    explosion.setAnimation(explosionAnim);
                    explosion.setSize(60,60);
                    explosion.alignToSprite(enemy);
                    addSpriteToGroup(explosion, "main");
                    
                    score += 100;
                    scoreLabel.setText("Score: " + score);
                    
                    if (Math.random() < 0.10)
                    {
                        Sprite bonus = new Sprite();
                        bonus.setTexture(new Texture ("images/bonus.png"));
                        bonus.setSize(50,50);
                        bonus.alignToSprite(enemy);
                        bonus.angle = 0;
                        addSpriteToGroup(bonus, "item");
                        
                        
                    }
                }
        }
        
        for (Sprite bonus : getGroupSpriteList("item"))
        {
            if (player.overlap(bonus))
            {
                bonus.destroy();
                coreHP += 10;
                coreLabel.setText("Core HP: " + coreHP);
            }
        }
        
        for (Sprite enemy : getGroupSpriteList("enemy")){
            if (enemy.overlap(core))
            {
                enemy.destroy();
                coreHP -= 25;
                coreLabel.setText("Core HP: " + coreHP);
            
            }
        }
        
        for (Sprite enemy : getGroupSpriteList("enemy"))
        {
            if (player.overlap(enemy))
            {
                player.destroy();
                loseMessage.visible = true;
            }
        }
        
        if (coreHP == 0)
        {
            player.destroy();
            loseMessage.visible = true;
        
        }
    }
    public static void main(String[] args)
    {
        try
        {
            launch(args);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            System.exit(0);
        }
    }
}
