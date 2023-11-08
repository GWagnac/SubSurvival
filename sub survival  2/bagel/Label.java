package bagel;

import javafx.scene.canvas.*;
import javafx.scene.text.*;
import javafx.scene.paint.Color;

/**
 * Display text in a game.
 * Extending the Sprite class so Labels can be added to Groups,
 *   and automatically drawn by the Game class.
 */
public class Label extends Sprite
{
    public String text;

    public Font font;

    public Color fontColor;
    public Color borderColor;

    public Label()
    {
        text = "";
        font = new Font("Arial Bold", 36);
        fontColor = Color.BLACK;
        borderColor = Color.BLACK;
    }

    /**
     * Change the text which is displayed by this label.
     *
     * @param t the text to be displayed.
     */
    public void setText(String t)
    {
        text = t;
    }

    /**
     * Change the font used to draw the text
     *
     * @param fontName name of the font to use 
     *         (ex: "Arial", "Times New Roman", "Courier New", ....)
     * @param fontSize base size of font (approximately height in pixels)
     */
    public void setFont(String fontName, int fontSize)
    {
        font = new Font(fontName, fontSize);
    }

    /**
     * Set the color of the text being drawn. Colors use RGB (red, green, blue) percents;
     *   mix together as colors of light (not colors of paint).
     *
     * @param redPercent the amount of red light used
     * @param greenPercent the amount of green light used
     * @param bluePercent the amount of blue light used
     */
    public void setColor(double redPercent, double greenPercent, double bluePercent)
    {
        fontColor = Color.color(redPercent, greenPercent, bluePercent);
    }

    /**
     * Set the color of the text border being drawn. Colors use RGB (red, green, blue) percents;
     *   mix together as colors of light (not colors of paint).
     *
     * @param redPercent the amount of red light used
     * @param greenPercent the amount of green light used
     * @param bluePercent the amount of blue light used
     */
    public void setBorderColor(double redPercent, double greenPercent, double bluePercent)
    {
        borderColor = Color.color(redPercent, greenPercent, bluePercent);
    }

    /**
     * Draw the text on the screen.
     *
     * @param context the context attached to the canvas where images are drawn
     */
    public void draw(GraphicsContext context)
    {
        if (visible)
        {
            // undo any transforms set by drawing previous sprites.
            context.setTransform( 1,0, 0,1, 0,0 );
            
            // set transparency level ("alpha") used when drawing image
            context.setGlobalAlpha(opacity);
            
            // tell context which font to use when drawing text
            context.setFont( font );

            // tell context which color to use when drawing text
            context.setFill( fontColor );

            context.fillText(text, position.x, position.y);

            // tell context color to use around edges
            context.setStroke( borderColor );

            context.strokeText(text, position.x, position.y);
        }
    }
}