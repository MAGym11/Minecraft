package lib.scripts;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard implements KeyListener {
    public boolean[] keys = new boolean[88];

    public void keyPressed(KeyEvent e) {
        // System.out.println(e.getKeyCode());
        if (e.getKeyCode() > keys.length - 1) {
            keys = new boolean[e.getKeyCode() + 1];
            keys[e.getKeyCode()] = true;
        } else {
            keys[e.getKeyCode()] = true;
        }
        
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() > keys.length - 1) {
            keys = new boolean[e.getKeyCode() + 1];
        } else {
            keys[e.getKeyCode()] = false;
        }
    }

    public void keyTyped(KeyEvent e) {
        
    }
}
