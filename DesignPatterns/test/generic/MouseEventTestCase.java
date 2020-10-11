package generic;
import java.awt.Container;
import java.awt.event.MouseEvent;

public class MouseEventTestCase {
	
	/**
	 * Creates a new MouseEvent from existing one with SHIFT key down.
	 * 
	 * @since draw.1
	 * @param me       Existing MouseEvent
	 * @return         new MouseEvent that duplicates me and adds SHIFT key.
	 */
	public MouseEvent addShift(MouseEvent me) {
		
		MouseEvent nme = new MouseEvent(me.getComponent(), me.getID(), me.getWhen(),
				me.getModifiers() | MouseEvent.SHIFT_MASK, me.getX(), me.getY(), me.getClickCount(), me.isPopupTrigger());
		return nme;
	}
	
	public MouseEvent createMoved (Container view, int x, int y) {
		MouseEvent me = new MouseEvent(view, MouseEvent.MOUSE_MOVED, 
				System.currentTimeMillis(), 0, x, y, 0, false);
		return me;
	}
	
	public MouseEvent createPressed (Container view, int x, int y) {
		MouseEvent me = new MouseEvent(view, MouseEvent.MOUSE_PRESSED, 
				System.currentTimeMillis(), 0, x, y, 0, false);
		return me;
	}
	
	public MouseEvent createDragged (Container view, int x, int y) {
		MouseEvent me = new MouseEvent(view, MouseEvent.MOUSE_DRAGGED, 
				System.currentTimeMillis(), 0, x, y, 0, false);
		return me;
	}
	
	public MouseEvent createRightClick  (Container view, int x, int y) {
		MouseEvent me = new MouseEvent(view, MouseEvent.MOUSE_PRESSED, 
				System.currentTimeMillis(), 0, x, y, 0, true);
		return me;
	}
	
	public MouseEvent createReleased  (Container view, int x, int y) {
		MouseEvent me = new MouseEvent(view, MouseEvent.MOUSE_RELEASED, 
				System.currentTimeMillis(), 0, x, y, 0, false);
		return me;
	}
	
	
	public MouseEvent createClicked  (Container view, int x, int y) {
		MouseEvent me = new MouseEvent(view, MouseEvent.MOUSE_CLICKED, 
				System.currentTimeMillis(), 0, x, y, 0, false);
		return me;
	}
	
	
	public MouseEvent createDoubleClicked  (Container view, int x, int y) {
		MouseEvent me = new MouseEvent(view, MouseEvent.MOUSE_CLICKED, 
				System.currentTimeMillis(), 0, x, y, 0, false);
		return me;
	}
}
