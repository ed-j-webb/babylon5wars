package aog.b5w.gui.utils;

import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;

import javax.swing.JPopupMenu;

public class SwingUtilities {

    /**
     * Returns the root Frame of the component. Also works for JMenuItems
     * by finding the invoker of the popup menu.
     * @param c the component to find the root frame of. 
     */
    public static Component getRoot(Component c) {
        Component root = javax.swing.SwingUtilities.getRoot(c);
        if (root == null) {
            while (null != c.getParent()) {
                c = c.getParent();
                if (c instanceof JPopupMenu) {
                    JPopupMenu popup = (JPopupMenu) c;
                    c = popup.getInvoker();
                    root = SwingUtilities.getRoot(c);
                    break;
                }
            }            
        }
        return root;
    }
    
    public static Frame getRootFrame(Component c) {
    	Component root = getRoot(c);
    	if (root instanceof Frame) {
    		return (Frame)root;
    	}
    	if (root instanceof Container) {
    		Container frame = (Container)root;
    		while (frame != null) {
    			frame = frame.getParent();
    			if (frame instanceof Frame) {
    				return (Frame)frame;
    			}
    		}
    		return null;
    	} else {
    		return null;
    	}
    }
}
