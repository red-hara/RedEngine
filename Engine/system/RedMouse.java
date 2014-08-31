/**
 * Do what you want 'cause a pirate is free, You are a pirate!
 * This work is licensed under the Creative Commons Attribution 4.0 International
 * License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by/4.0/ or send a letter to Creative
 * Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
 */

package Engine.system;

import Engine.RedG;
import Engine.RedGame;
import Engine.RedPoint;
import java.awt.MouseInfo;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 
 * @author hara
 */

//  ______
// // \^\ \
// ||1|2|3| 
// || /v/ |
// ||     | 
// \\_____/ 
//    \V
//    ||
public class RedMouse extends MouseAdapter {

    public static double x;
    public static double y;
    public static RedPoint onScreen = new RedPoint(0, 0);
    private RedPoint lastCoord = new RedPoint(0, 0);
    public static RedPoint velocity = new RedPoint(0, 0);
    private boolean current[] = new boolean[4];
    private boolean last[] = new boolean[4];

    @Override
    public void mousePressed(MouseEvent Event) {
        current[ Event.getButton()] = true;
    }

    @Override
    public void mouseReleased(MouseEvent Event) {
        current[ Event.getButton()] = false;
    }

    public void update() {
        System.arraycopy(current, 0, last, 0, current.length);

        if (RedGame.getWindow().isUndecorated()) {
            if (RedGame.scaleType == RedGame.NOSCALE) {
                onScreen.x = (MouseInfo.getPointerInfo().getLocation().getX() - RedGame.getWindow().getLocation().getX()) / RedGame.getWindow().canvas.zoom;
                onScreen.y = (MouseInfo.getPointerInfo().getLocation().getY() - RedGame.getWindow().getLocation().getY()) / RedGame.getWindow().canvas.zoom;
            } else if (RedGame.scaleType == RedGame.FILL) {
                onScreen.x = (MouseInfo.getPointerInfo().getLocation().getX() - RedGame.getWindow().getLocation().getX()) / (RedGame.getWindow().getContentPane().getWidth() / RedGame.width);
                onScreen.y = (MouseInfo.getPointerInfo().getLocation().getY() - RedGame.getWindow().getLocation().getY()) / (RedGame.getWindow().getContentPane().getHeight() / RedGame.height);
            } else if (RedGame.scaleType == RedGame.PROPOTION) {
                double propX = (RedGame.getWindow().getContentPane().getWidth()) / RedGame.width;
                double propY = (RedGame.getWindow().getContentPane().getHeight()) / RedGame.height;
                if( propX <= propY ) {
                    onScreen.x = (MouseInfo.getPointerInfo().getLocation().getX() - RedGame.getWindow().getLocation().getX()) / propX;
                    onScreen.y = (MouseInfo.getPointerInfo().getLocation().getY() - RedGame.getWindow().getLocation().getY()- (RedGame.getWindow().getContentPane().getHeight()/ 2 - RedGame.height / 2 * propX)) / propX;
                } else {
                    onScreen.x = (MouseInfo.getPointerInfo().getLocation().getX() - RedGame.getWindow().getLocation().getX() - (RedGame.getWindow().getContentPane().getWidth() / 2 - RedGame.width / 2 * propY)) / propY;
                    onScreen.y = (MouseInfo.getPointerInfo().getLocation().getY() - RedGame.getWindow().getLocation().getY()) / propY;
                }
            }
        } else {
            if( RedGame.scaleType == RedGame.NOSCALE ) {
                onScreen.x = (MouseInfo.getPointerInfo().getLocation().getX() - RedGame.getWindow().getLocation().getX() - RedGame.getWindow().getInsets().left) / RedGame.getWindow().canvas.zoom / RedGame.getWindow().canvas.zoom;
                onScreen.y = (MouseInfo.getPointerInfo().getLocation().getY() - RedGame.getWindow().getLocation().getY() - RedGame.getWindow().getInsets().top) / RedGame.getWindow().canvas.zoom / RedGame.getWindow().canvas.zoom;
            } else if( RedGame.scaleType == RedGame.FILL) {
                onScreen.x = (MouseInfo.getPointerInfo().getLocation().getX() - RedGame.getWindow().getLocation().getX() -RedGame.getWindow().getInsets().left ) / (RedGame.getWindow().getContentPane().getWidth() / RedGame.width);
                onScreen.y = (MouseInfo.getPointerInfo().getLocation().getY() - RedGame.getWindow().getLocation().getY() - RedGame.getWindow().getInsets().top) / (RedGame.getWindow().getContentPane().getHeight() / RedGame.height);
            } else if( RedGame.scaleType == RedGame.PROPOTION ) {
                                double propX = (RedGame.getWindow().getContentPane().getWidth()) / RedGame.width;
                double propY = (RedGame.getWindow().getContentPane().getHeight()) / RedGame.height;
                if( propX <= propY ) {
                    onScreen.x = (MouseInfo.getPointerInfo().getLocation().getX() - RedGame.getWindow().getLocation().getX() - RedGame.getWindow().getInsets().left) / propX;
                    onScreen.y = (MouseInfo.getPointerInfo().getLocation().getY() - RedGame.getWindow().getLocation().getY()- (RedGame.getWindow().getContentPane().getHeight()/ 2 - RedGame.height / 2 * propX) - RedGame.getWindow().getInsets().top) / propX;
                } else {
                    onScreen.x = (MouseInfo.getPointerInfo().getLocation().getX() - RedGame.getWindow().getLocation().getX() - (RedGame.getWindow().getContentPane().getWidth() / 2 - RedGame.width / 2 * propY) - RedGame.getWindow().getInsets().left) / propY;
                    onScreen.y = (MouseInfo.getPointerInfo().getLocation().getY() - RedGame.getWindow().getLocation().getY() - RedGame.getWindow().getInsets().top) / propY;
                }
            }
        }

        x = onScreen.x + RedG.screen.x;
        y = onScreen.y + RedG.screen.y;

        velocity.x = (x - lastCoord.x) / RedG.elapsed;
        velocity.y = (y - lastCoord.y) / RedG.elapsed;
        lastCoord.x = x;
        lastCoord.y = y;
    }

    public boolean isPressed(int ButtonCode) {
        return (current[ ButtonCode]);
    }

    public boolean justPressed(int ButtonCode) {
        return (current[ButtonCode] && !last[ButtonCode]);
    }

    public boolean justReleased(int ButtonCode) {
        return (!current[ButtonCode] && last[ButtonCode]);
    }
}