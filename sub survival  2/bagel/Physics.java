package bagel;

/**
 *   Do calculations involving position, velocity, and acceleration
 *     for smoother Sprite movement.
 */
public class Physics
{
    public Vector position;
    public Vector velocity;
    public Vector acceleration;

    // how quickly speed increases
    public double accelerationValue;
    // maximum possible speed of this object
    public double maximumSpeed;
    // how quickly speed decreases when not accelerating
    public double decelerationValue;
    
    /**
     * Physics Constructor
     */
    public Physics(double accValue, double maxSpeed, double decValue)
    {
        position = new Vector();
        velocity = new Vector();
        acceleration = new Vector();
        
        accelerationValue = accValue;
        maximumSpeed      = maxSpeed;
        decelerationValue = decValue;
    }
    
    /**
     * Change the speed of this object.
     * @param speed the new speed of this object
     */
    public void setSpeed(double speed)
    {
        velocity.setLength(speed);
    }
    
    /**
     * Get the speed of this object.
     * @return the speed
     */
    public double getSpeed()
    {
        return velocity.getLength();
    }
    
    /**
     * Set the angle that the object is moving in.
     * @param angleDegrees the new angle of motion, in degrees
     */
    public void setMotionAngle(double angleDegrees)
    {
        velocity.setAngle(angleDegrees);
    }
    
    /**
     * Get the angle of motion of this object.
     * @return the angle of motion (in degrees)
     */
    public double getMotionAngle()
    {
        return velocity.getAngle();
    }
    
    /**
     * Accelerate this object, by the value stored in accelerating value,
     *   in the direction indicated by angleDegrees
     *
     * @param angleDegrees the direction of acceleration
     */
    public void accelerateAtAngle(double angleDegrees)
    {
        Vector a = new Vector();
        a.setLength( accelerationValue );
        a.setAngle( angleDegrees );
        
        // combine with current acceleration, 
        //  for moving in multiple directions at once
        acceleration.addValues( a.x, a.y );
    }
    
    
    
    
    /**
     * Update the position of this object,
     *   based on the current velocity, and the time that has passed
     *   since the last update.
     * @param deltaTime change in time since last update (usually 1/60 second)
     */
    public void update(double deltaTime)
    {
        // acceleration is change in velocity over time
        velocity.addValues( acceleration.x * deltaTime, acceleration.y * deltaTime);
        
        double speed = getSpeed();
        
        // if speed is greater than maximum allowed, decrease it to the maximum
        if (speed > maximumSpeed)
            speed = maximumSpeed;
            
        // if not accelerating, then subtract deceleration value from speed
        if ( acceleration.getLength() < 0.0001 )
            speed -= decelerationValue * deltaTime;

        // speed should never be negative
        if (speed < 0)
            speed = 0;
    
        // save changes to speed in velocity vector
        setSpeed( speed );
    
        // velocity is change in position over time
        position.addValues( velocity.x * deltaTime, velocity.y * deltaTime );
        
        // acceleration is "used up"; must be re-applied to continue accelerating
        acceleration.setValues(0, 0);
    }
    
    /**
     * Change the angle of motion of this object, assuming it has collided
     *  with a solid object, the solid object is at an angle of surfaceAngleDegrees.
     *
     * @param surfaceAngleDegrees angle of solid object
     */
    public void bounceAgainst(double surfaceAngleDegrees)
    {
        double collisionAngle = this.getMotionAngle() - surfaceAngleDegrees; // A
        double newMotionAngle = surfaceAngleDegrees - collisionAngle;
        this.setMotionAngle( newMotionAngle );
    }
    
    
    
    
    
    
    
    
    
    
}
