/**
 * Do what you want 'cause a pirate is free, You are a pirate!
 * This work is licensed under the Creative Commons Attribution 4.0 International
 * License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by/4.0/ or send a letter to Creative
 * Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
 */

package Engine;

import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author Red Hara (rtc6fg4.fejg2@gmail.com)
 */
public class RedPolygon extends RedObject {
    
    public RedPoint[] points;
    public int color = 0;
    public double zoom = 0;
    public RedPoint scrollFactor = new RedPoint(1, 1);
    public RedPoint offset = new RedPoint(0, 0);
    public RedPoint canvasDelta = new RedPoint(0, 0);
    public boolean fill = true;
    public double displayZoom = 1;
    
    public RedPolygon(double X, double Y, double Width, double Height, RedPoint[] Points) {
        super(X, Y, Width, Height);
        points = Points;
    }
    
    public RedPolygon(double X, double Y, double Width, double Height, RedPoint[] Points, int Color) {
        super(X, Y, Width, Height);
        points = Points;
        color = Color;
    }
    
    @Override
    public void draw(RedCanvas Canvas) {
        if (visible) {
            Graphics graphics = Canvas.getGraphics();
            graphics.setColor(new Color(color, true));
            RedPoint[] rotatedPoints = new RedPoint[points.length];
            if (angle != 0) {
                for (int i = 0; i < points.length; i++) {
                    rotatedPoints[i] = rotatePoint(points[i], new RedPoint(width / 2 + offset.x, height / 2 + offset.y), angle);
                }
            } else {
                for (int i = 0; i < points.length; i++) {
                    rotatedPoints[i] = points[i].getCopy();
                }
            }
            
            RedPoint deltaCenter;
            for (RedPoint rotatedPoint : rotatedPoints) {
                deltaCenter = new RedPoint(width / 2 + offset.x - rotatedPoint.x, height / 2 + offset.y - rotatedPoint.y);
                rotatedPoint.x = width / 2 - deltaCenter.x * displayZoom;
                rotatedPoint.y = height / 2 - deltaCenter.y * displayZoom;
                
            }
            int pointsX[] = new int[points.length];
            int pointsY[] = new int[points.length];
            int helperArray[];
            
            int j = 0;
            for (RedPoint rotatedPoint : rotatedPoints) {
                if (rotatedPoint != null) {
                    pointsX[j] = (int) (Math.floor((x + rotatedPoint.x - RedG.screen.x * scrollFactor.x + canvasDelta.x + Canvas.offset.x) * ((zoom == 0) ? Canvas.zoom : zoom)));
                    pointsY[j++] = (int) (Math.floor((y + rotatedPoint.y - RedG.screen.y * scrollFactor.y + canvasDelta.y + Canvas.offset.y) * ((zoom == 0) ? Canvas.zoom : zoom)));
                }
            }
            
            helperArray = pointsX;
            System.arraycopy(helperArray, 0, pointsX, 0, j);
            helperArray = pointsY;
            pointsY = new int[j];
            System.arraycopy(helperArray, 0, pointsY, 0, j);
            
            if (fill) {
                graphics.fillPolygon(pointsX, pointsY, j);
            } else {
                graphics.drawPolygon(pointsX, pointsY, j);
            }
            
        }
    }
    
    public static RedPoint rotatePoint(RedPoint Point, RedPoint Center, double Angle) {
        double cosAngle = Math.cos(Angle);
        double sinAngle = Math.sin(Angle);
        double dx = (Point.x - Center.x);
        double dy = (Point.y - Center.y);
        
        return new RedPoint(Center.x + (dx * cosAngle - dy * sinAngle), Center.y + (dx * sinAngle + dy * cosAngle));
    }
}
