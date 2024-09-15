package lib.scripts;

import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;


public class Mouse implements MouseListener, MouseMotionListener {
    public static boolean leftClick = false;
    public static boolean rightClick = false;

    public static int x;
    public static int y;

    public void mouseClicked(MouseEvent e) {
        
    }

    public void mousePressed(MouseEvent e) {
        if (e.getButton() == 1) {
            leftClick = true;
        }
        if (e.getButton() == 3) {
            rightClick = true;
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == 1) {
            leftClick = false;
        } else if (e.getButton() == 3) {
            rightClick = false;
        }
    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }

    public void mouseDragged(MouseEvent e) {
        x = e.getX();
        y = e.getY();
    }

    public void mouseMoved(MouseEvent e) {
        x = e.getX();
        y = e.getY();
    }
}
