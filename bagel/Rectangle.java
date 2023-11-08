package bagel;

 import java.util.Arrays;

/**
 * Rectangles store position and size; used to check overlap between sprites.
 */
public class Rectangle
{
    public double width;
    public double height;
    public Vector position;
    
    /**
     * Rectangle Constructor: sets default values to a 1x1 rectangle,
     *   top-left corner at (0,0).
     *
     */
    public Rectangle()
    {
        width = 1;
        height = 1;
        position = new Vector(0,0);
    }
    
    /**
     * Rectangle Constructor; sets position of top-left corner to (x,y)
     *   and the size to w by h.
     *
     * @param x coordinate for left side
     * @param y coordinate for top side
     * @param w width
     * @param h height
     */
    public Rectangle(double x, double y, double w, double h)
    {
        position = new Vector(x,y);
        width = w;
        height = h;
    }
    
    /**
     * Update the position of this rectangle
     *
     * @param x new coordinate of left side
     * @param y new coordinate of top side
     */
    public void setPosition(double x, double y)
    {
        position.setValues(x, y);
    }
    
    /**
     * Set size of this rectangle
     *
     * @param w new width of rectangle
     * @param h new height of rectangle
     */
    public void setSize(double w, double h)
    {
        width = w;
        height = h;
    }
    
    
    /**
     * Check if this rectangle overlaps another rectangle.
     *
     * @param other other rectangle to check for overlap
     * @return true if rectangles overlap
     */
    public boolean overlap(Rectangle other)
    {
        boolean notOverlap = 
          (position.x + width < other.position.x) || 
          (position.y + height < other.position.y) ||
          (other.position.x + other.width < position.x) || 
          (other.position.y + other.height < position.y);
          
        return !(notOverlap);
    }
    
    /**
     * Assuming that this rectangle overlaps other rectangle,
     * calculate the four vectors that could be used to translate this rectangle
     * so that this is no more overlap.
     * Return the shortest (minimum) of these four vectors.
     *
     * @param other Represents the "solid" rectangle
     * @return the minimum direction to move this rectangle by
     */
    public Vector getMinimumTranslationVector(Rectangle other)
    {
        Vector[] directions = {
            new Vector(other.position.x + other.width - this.position.x, 0), // right
            new Vector(-this.position.x  - this.width + other.position.x, 0),  // left
            new Vector(0, other.position.y + other.height - this.position.y ), // down
            new Vector(0, -this.position.y - this.height + other.position.y)    // up
        };   
        // sorts the array of vectors, using the Vector.compareTo method.
        Arrays.sort( directions );
            
        // return the smallest vector in the sorted array.
        return directions[0];
    }
    
    
    
    
}