/**
 * Do what you want 'cause a pirate is free, You are a pirate!
 * This work is licensed under the Creative Commons Attribution 4.0 International
 * License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by/4.0/ or send a letter to Creative
 * Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
 */

package Engine;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.io.IOException;

/**
 *
 * @author hara
 */
public class RedText extends RedObject {

    public String text;
    public Font font = new Font(null, Font.PLAIN, 8);
    public int color = 0xffffffff;
    public int shadow = 0;
    public RedPoint offset;
    public RedPoint scrollFactor;
    public double zoom = 0;
    public double widhInPieces = 8;
    public boolean centered = false;

    public RedText(double X, double Y, double Width, String Text) {
        super(X, Y, Width, 0);

        try {
            font = Font.createFont(Font.PLAIN, getClass().getResourceAsStream("/Engine/system/data/pixie.ttf"));
        } catch (FontFormatException | IOException e) {
            System.err.println(e);
        }
        font = font.deriveFont(8f);

        text = Text;
        height = font.getSize();

        offset = new RedPoint(0, 0);
        scrollFactor = new RedPoint(1, 1);
    }

    public RedText(double X, double Y, String Text) {
        super(X, Y, 0, 0);

        try {
            font = Font.createFont(Font.PLAIN, getClass().getResourceAsStream("/Engine/system/data/pixie.ttf"));
        } catch (FontFormatException | IOException e) {
            System.err.println(e);
        }
        font = font.deriveFont(8f);

        text = Text;
        height = font.getSize();

        offset = new RedPoint(0, 0);
        scrollFactor = new RedPoint(1, 1);
    }

    /**
     * FIX IT. Later.
     *
     * @param Canvas
     */
    @Override
    public void draw(RedCanvas Canvas) {
        if (visible) {
            Graphics graphics = Canvas.getGraphics();
            Font helperFont = font.deriveFont(font.getSize2D() * (int) (((zoom == 0) ? Canvas.zoom : zoom)));
            graphics.setFont(helperFont);
            if (font.getMaxCharBounds(new FontRenderContext(new AffineTransform(), true, true)).getWidth() <= width || width <= 0) {
                int lineY = 0;
                String result = fitInWidth(text, font, width);
                int centeredOffset = 0;
                for (String line : result.split("\n")) {

                    if (centered) {
                        centeredOffset = getLineLength(line, font) / 2;
                    }

                    lineY += (graphics.getFontMetrics().getHeight() + offset.y * ((zoom == 0) ? Canvas.zoom : zoom));
                    graphics.setColor(new Color(shadow, true));
                    graphics.drawString(line,
                            (int) Math.floor(((x - centeredOffset + Canvas.offset.x - RedG.screen.x * scrollFactor.x) * ((zoom == 0) ? Canvas.zoom : zoom) - font.getSize() * (int) ((zoom == 0) ? Canvas.zoom : zoom) / widhInPieces)),
                            (int) Math.floor((((y - RedG.screen.y * scrollFactor.y + Canvas.offset.y) * ((zoom == 0) ? Canvas.zoom : zoom)) + font.getSize() * (int) ((zoom == 0) ? Canvas.zoom : zoom) / widhInPieces)) + lineY);
                    graphics.setColor(new Color(color, true));
                    graphics.drawString(line,
                            (int) Math.floor((x - centeredOffset + Canvas.offset.x - RedG.screen.x * scrollFactor.x) * ((zoom == 0) ? Canvas.zoom : zoom)),
                            (int) Math.floor((((y - RedG.screen.y * scrollFactor.y + Canvas.offset.y) * ((zoom == 0) ? Canvas.zoom : zoom))) + lineY));
                }
            }
        }
    }

    public static String fitInWidth(String Input, Font FontOfString, double Width) {
        double lineWidth = 0;
        double wordWidth = 0;
        int lastSpace = 0;
        char character;
        double characterWidth;
        FontRenderContext frc = new FontRenderContext(new AffineTransform(), true, true);

        if (Width > 0 && Input != null) {
            for (int i = 0; i < Input.length(); i++) {
                character = Input.charAt(i);
                if (!(character == ' ' || character == '\n')) {
                    characterWidth = FontOfString.getStringBounds(String.valueOf(character), frc).getWidth();
                    wordWidth += characterWidth;
                    lineWidth += characterWidth;
                    if (lineWidth > Width) {
                        if (wordWidth >= lineWidth) {
                            Input = Input.substring(0, i) + "\n" + Input.substring(i);
                            lastSpace = i;
                            wordWidth -= lineWidth;
                            lineWidth = 0;
                        } else {
                            Input = Input.substring(0, lastSpace) + "\n" + Input.substring(lastSpace + 1);
                            lineWidth = wordWidth;
                            i++;
                        }
                        wordWidth = 0;
                    }
                } else if (character == '\n') {
                    lastSpace = i;
                    lineWidth = 0;
                    wordWidth = 0;
                } else if (character == ' ') {
                    lastSpace = i;
                    wordWidth = 0;
                    lineWidth += FontOfString.getStringBounds(" ", frc).getWidth();
                }
            }
        }
        return Input;
    }

    public static int getLineLength(String Line, Font Font) {
        int result = 0;
        FontRenderContext frc = new FontRenderContext(new AffineTransform(), true, true);
        for (int i = 0; i < Line.length(); i++) {
            result += Font.getStringBounds(String.valueOf(Line.charAt(i)), frc).getWidth();
        }
        return result;
    }

    public void setFont(String FontPath, float Size) {
        if (FontPath != null) {
            try {
                font = Font.createFont(Font.PLAIN, getClass().getResourceAsStream(FontPath));
            } catch (FontFormatException | IOException e) {
                System.err.println(e);
            }
        }
        font = font.deriveFont(Size);
    }
}
