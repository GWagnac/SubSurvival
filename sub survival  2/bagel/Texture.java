package bagel;

import javafx.scene.image.Image;

public class Texture{
    
    public Image image;
    
    public Rectangle region;
    
    public Texture(String imageFileName){
        image = new Image(imageFileName);
        region = new Rectangle(0,0, image.getWidth(), image.getHeight());
    }    
}
