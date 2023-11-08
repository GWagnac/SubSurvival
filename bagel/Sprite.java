package bagel;

import javafx.scene.canvas.*;
import java.util.ArrayList;

public class Sprite{

    public Vector position;

    public Rectangle size;

    public Texture texture;

    public boolean visible;

    public double opacity;

    public double angle;

    public boolean destroySignal;

    public boolean mirrored;

    public boolean flipped;

    public Animation animation;

    public Physics physics;

    public ArrayList<Action> actionList;

    public Sprite(){
        position = new Vector(0,0);

        size = new Rectangle();
        size.position = position;
        visible = true;

        opacity = 1;

        angle = 0;

        flipped = false;
        mirrored = false;

        destroySignal = false;
        actionList = new ArrayList<Action>();

    }

    public Sprite(double x, double y){
        position = new Vector(x,y);

        size = new Rectangle();
        size.position = position;
        visible = true;

        opacity = 1;

        angle = 0;

        destroySignal = false;

        actionList = new ArrayList<Action>();

    }

    public void setPosition(double x, double y){
        position.setValues(x, y);
    }

    public void setSize(double width, double height){
        size.setSize(width,height);
    }

    public void setTexture(Texture tex){
        texture = tex;
        size.setSize(tex.region.width, tex.region.height );
    }

    ////////////////////////////////////

    public void draw(GraphicsContext context) {
        if (visible) {
            // we store angle in degrees; Math class functions require radians.
            double A = Math.toRadians(angle);

            double scaleX = 1;
            if (mirrored){
                scaleX = -1;
            }

            double scaleY = 1;
            if (flipped){
                scaleY = -1;
            }

            // rotates objects around the center point of the sprite;
            //  also renders objects at center of sprite.
            context.setTransform( scaleX * Math.cos(A), scaleX * Math.sin(A),
                scaleY * (-Math.sin(A)), scaleY * Math.cos(A),
                position.x + size.width/2, position.y + size.height/2 );

            // set transparency level ("alpha") used when drawing image
            context.setGlobalAlpha(opacity);

            //                   [region of image]      [position on canvas]
            // drawImage( image, x, y, width, height,  x, y, width, height );
            context.drawImage( texture.image, 
                texture.region.position.x, texture.region.position.y,
                texture.region.width,      texture.region.height,
                -size.width/2, -size.height/2, size.width, size.height  );
        }
    }

    public void update(double deltaTime)
    {
        // update physics, if present
        //  if physics is null, physics variable has not been assigned.
        if ( physics != null )
        { 
            physics.update(deltaTime);
        }
        // update animation if present
        if ( animation != null )
        {
            animation.update(deltaTime);
            texture = animation.texture;
        }

        // update actions from list, if any are present
        // when an action is finished, remove it from the list

        // copy actionList
        ArrayList<Action> actionListCopy = new ArrayList<Action>(actionList);
        for (Action action : actionListCopy)
        {
            // apply action to this Sprite, and check if finished
            boolean finished = action.apply( this, deltaTime );
            // if finished, remove Action from original actionList.
            if ( finished )
                actionList.remove(action);
        }
    }

    /**
     * Assign a Physics object to this Sprite.
     * When physics object updates position, Sprite position will be updated too.
     * @param p Physics object
     */
    public void setPhysics(Physics p)
    {
        physics = p;

        // link physics object to sprite position vector
        physics.position = this.position;
    }

    public void setAngle(double angleDegrees)
    {
        angle = angleDegrees;    
    }

    /**
     * If the sprite moves past the edge of the screen,
     *   adjust its position to the opposite side of the screen.
     *
     * @param screenWidth width of the game window (800 by default)
     * @param screenHeight height of the game window (600 by default)
     */
    public void wrap(double screenWidth, double screenHeight)
    {
        // check if sprite has moved completely past left screen edge
        if (position.x + size.width < 0)
            position.x = screenWidth;
        // check if sprite has moved completely past right screen edge
        if (position.x > screenWidth)
            position.x = -size.width;
        // check if sprite has moved completely past top screen edge
        if (position.y + size.height < 0)
            position.y = screenHeight;
        // check if sprite has moved completely past bottom screen edge
        if (position.y > screenHeight)
            position.y = -size.height;
    }

    public void moveBy(double xAmount, double yAmount)
    {
        position.addValues(xAmount, yAmount);
    }

    public boolean overlap(Sprite other)
    {
        return this.size.overlap( other.size );
    }

    /**
     * Move this sprite by the minimum amount so that it no longer overlaps other sprite.
     *
     * @param other Represents the solid sprite.
     */
    public void preventOverlap(Sprite other)
    {
        // double check this sprite does overlap other
        if ( this.overlap(other) )
        {
            // get minimum translation vector
            Vector mtv = this.size.getMinimumTranslationVector( other.size );

            this.moveBy( mtv.x, mtv.y );
        }
    }

    /**
     * Assign an Animation object to this Sprite.
     */
    public void setAnimation(Animation a)
    {
        animation = a;

        // link animation texture to sprite texture
        texture = animation.texture;

        // also, set default size of this sprite based on texture data
        setSize( texture.region.width, texture.region.height );
    }

    /**
     * Set visibility of this sprite, which determines whether it appears on the screen.
     *
     * @param vis should this sprite be visible?
     */
    public void setVisible(boolean vis)
    {
        visible = vis;
    }

    public void rotateBy(double angleAmount)
    {
        angle += angleAmount;
    }

    public void addAction(Action a)
    {
        actionList.add(a);
    }

    /**
     * Set destroy signal to true;
     *  the next time the game loops over all Sprites,
     *  any Sprite with destroySignal = true will be removed from its list.
     */
    public void destroy()
    {
        destroySignal = true;    
    }

    public void alignToSprite(Sprite otherSprite){
        setPosition(otherSprite.position.x + otherSprite.size.width/2 - this.size.width/2,
            otherSprite.position.y + otherSprite.size.height/2 - this.size.height/2);

        setAngle(otherSprite.angle);

    }

    public void bounceAgainst(Sprite other)
    {
        // bouncing only happens if objects overlap
        if ( this.overlap(other) )
        {
            Vector mtv = this.size.getMinimumTranslationVector( other.size );
            // move this object so there is no longer overlap
            this.moveBy( mtv.x, mtv.y );

            // the angle of the solid surface is 90 degrees from mtv vector
            double surfaceAngle = mtv.getAngle() + 90;

            // adjust motion angle
            this.physics.bounceAgainst(surfaceAngle);
        }
    }
}
 