package draw.controller;

import java.awt.Graphics;
import java.awt.Point;
import java.util.Optional;

import draw.model.Element;

/**
 * This interface describes a number of potential behaviors.
 * 
 * <ol>
 * <li> Move -- when the user moves the mouse, potentially with key modifiers down but no button pressed.</li>
 * <li>Anchor actions
 *   <ol type="a">
 *      <li>startAnchor    -- press mouse over an anchor for an element</li>
 *      <li>dragAnchor     -- drag mouse down to continue action</li>
 *      <li>completeAnchor -- release mouse</li>
 *   </ol></li>
 * <li>Empty actions
 *   <ol type="a">
 *      <li>start          -- press mouse over a possible element</li>
 *      <li>drag           -- drag mouse down to continue action</li>
 *      <li>complete       -- release mouse</li>
 *   </ol></li>
 * </ol>
 * 
 * <p>Each selected element in the GUI will have four "anchor" squares at the four corners
 * of the bounding box that defines the element. For example:</p>
 * 
 * <img src="doc-files/anchors.png" alt="Anchors for a shape">
 * 
 * In handling events, there may be a need to paint directly into the Graphics object being controlled, which 
 * is why the {@link #paint(Graphics)} is provided.
 */
public interface IActionInterface {
	// values are identical to those in java.awt.Cursor, for convenience
	public static final int NorthWestAnchor = 6;
	public static final int NorthEastAnchor = 7;
	public static final int SouthEastAnchor = 5;
	public static final int SouthWestAnchor = 4;
	public static final int NoAnchor        = 0;
	
	/** 
	 * Movement of cursor detected without the mouse button being pressed.
	 * 
	 * <p>Element is the optional element over which the point is moving. If 
	 * it is present then there are two options:</p>
	 * <ol>
	 * <li>anchor is one of these values, as it passes over the anchor area.
	 * 
	 *   <ul><li>Cursor.NW_RESIZE_CURSOR (6)</li>
	 *       <li>Cursor.NE_RESIZE_CURSOR (7)</li>
 	 *       <li>Cursor.SE_RESIZE_CURSOR (5)</li>
	 *       <li>Cursor.SW_RESIZE_CURSOR (4)</li></ul>
	 *     </li>
	 *   
	 * <li>anchor is zero (0 or NoAnchor) which means it points over an element.</li>
	 * </ol>
	 * 
	 * <p>When element is not present, then cursor is moving over some place 
	 * in which no element intersects.</p>
	 * <p>In all cases, modifiers reflects whether shift or control is down.</p>
	 *
	 * @param pt          mouse location
	 * @param modifiers   keyboard modifiers in effect
	 * @param element     optional element in play
	 * @param anchor      which anchor is affected
	 * @return whether mouse action was processed
	 */
	boolean move(Point pt, int modifiers, Optional<Element> element, int anchor);
	
	/** 
	 * Initiate action by pressing mouse at point with, potentially, a selected element. 
	 * 
	 * @param pt           starting an element action at this mouse location
	 * @param modifiers    keyboard modifiers in effect
	 * @param element      optional element in play
	 * @return whether mouse action was processed
	 */
	boolean start(Point pt, int modifiers, Optional<Element> element);
	
	/** 
	 * Act on the mouse drag. Pass along modifiers (SHIFT, CONTROL, etc...).
	 * 
	 * @param start        original starting mouse location  of this drag action
	 * @param modifiers    keyboard modifiers in effect
	 * @param element      optional element in play
	 * @param current      current mouse location
	 * @return whether mouse action was processed
	 */
	boolean drag(Point start, int modifiers, Optional<Element> element, Point current);
	
	/** 
	 * Complete the mouse interaction.
	 * 
	 * @param pt           ending mouse location of this mouse action
	 * @param modifiers    keyboard modifiers in effect
	 * @param element      optional element in play
	 * @return whether mouse action was processed
	 */
	boolean complete(Point pt, int modifiers, Optional<Element> element);
	
	/** 
	 * Start the mouse interaction as started by dragType.
	 *
	 * @param pt           start an anchor at this mouse location
	 * @param modifiers    keyboard modifiers in effect
	 * @param element      affected element
	 * @param anchor       initiating anchor index position
	 * @return whether mouse action was processed
	 */
	boolean startAnchor(Point pt, int modifiers, Element element, int anchor);

	/** 
	 * Act on the anchor with mouse down. Pass along modifiers (SHIFT, CONTROL, etc...)
	 *
	 * @param start       mouse location where action started
	 * @param modifiers   keyboard modifiers in effect
	 * @param element     affected element
	 * @param current     current mouse location
	 * @return whether mouse action was processed
	 */
	boolean dragAnchor(Point start, int modifiers, Element element, Point current);
		
	/** 
	 * Complete the mouse interaction as started by dragType.
	 *
	 * @param pt           ending an anchor action at this mouse location
	 * @param modifiers    keyboard modifiers in effect
	 * @param element      affected element
	 * @return whether mouse action was processed
	 */
	boolean completeAnchor(Point pt, int modifiers, Element element);
	

	/**
	 * Provide the capability for any of these controllers to paint into the Graphics object they are controlling.
	 * @param g  Graphics object being controlled.
	 */
	void paint(Graphics g);
}
