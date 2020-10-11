package generic;
import java.awt.Container;
import java.awt.event.MouseEvent;

public class MouseEventTestCase {
	
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
