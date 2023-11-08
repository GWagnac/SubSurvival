package bagel;
import java.util.ArrayList;

/**
 *  An Animation displays a sequence of images in succession to create the appearance of change.
 *  Uses a list of Rectangles to specify which part of the image should be drawn.
 *  Similar to a Texture whose region is changing repeatedly.
 */
public class Animation
{
    public Texture texture;
    // store the rectangles corresponding to small images within large image
    public ArrayList<Rectangle> regionList;
    
    // how long each image in the spritesheet should be displayed (in seconds; small number)
    public double imageDuration;
    // how long the animation has been playing;
    //   use to calculate which region to display in texture
    public double elapsedTime;
    
    // should we restart the animation from the first region once it is done?
    public boolean imageLoop;
    
    /**
     * Load an image file into a Texture, and create a list of rectangles
     *  corresponding to the smaller image sizes, calculated from number of rows and columns.
     *
     * @param imageFileName name of the file
     * @param rows number of rows of small images
     * @param columns number of columns of small images
     * @param duration how long each image in spritesheet should appear
     * @param loop should animation restart after last image is displayed
     */
    public Animation(String imageFileName, int rows, int columns,
                     double duration, boolean loop)
    {
        texture = new Texture(imageFileName);
        
        imageDuration = duration;
        imageLoop = loop;
        elapsedTime = 0;
        
        regionList = new ArrayList<Rectangle>();
        // calculate size of smaller images
        double smallWidth  = texture.image.getWidth() / columns;
        double smallHeight = texture.image.getHeight() / rows;
        
        for (int rowNum = 0; rowNum < rows; rowNum++)
        {
            for (int columnNum = 0; columnNum < columns; columnNum++)
            {
                // coordinates of top-left corner of small image
                double smallX = columnNum * smallWidth;
                double smallY = rowNum * smallHeight;
                Rectangle smallRect = new Rectangle(smallX, smallY, smallWidth, smallHeight);
                regionList.add( smallRect );
            }
        }
    }
    
    /**
     *  Recalculate which region of texture should be displayed,
     *    based on how much time has passed.
     *    
     * @param deltaTime amount of time that passed since last update (usually 1/60 second).
     */
    public void update(double deltaTime)
    {
        // track total time that has passed
        elapsedTime += deltaTime;
        
        // calculate which region to use in texture
        
        // index: 0   1   2   3   4   5   6   7
        //        
        // example: if imageDuration = 0.10, and elapsedTime = 0.35, 
        //  then regionIndex = round down to nearest int (elapsedTime / imageDuration)
        
        int regionIndex = (int)Math.floor( elapsedTime / imageDuration );
        
        
         
        if (regionIndex > regionList.size() - 1)
        {
            if (imageLoop == true)
            {
                // restart animation from beginning
                regionIndex = regionIndex % regionList.size();
            }
            else
            { 
                regionIndex = regionList.size() - 1;
            }
        }
                
        // update texture data to use current region, based on elapsed time.
        texture.region = regionList.get( regionIndex );
    }
    
    
    
    
    
    
    
}
