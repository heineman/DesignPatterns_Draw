/**
 * Adapts low-level mouse events to events relevant for the application-domain.
 * 
 * We want to be able to support a number of expected user experiences that are common
 * in applications. These include reacting to mouse events intuitively and changing the
 * cursor in response to potential events.  Instead of wallowing in complex mouse handlers,
 * the classes in this package provide a consistent API to allow new behaviors to easily 
 * be added to the initially defined ones.
 * 
 * {@link draw.controller.IActionInterface} provides the application-domain events of interest, including:
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
 * To be able to produce these events, the {@link ActiveToolHandler} class takes the responsibility of
 * <b>adapting</b> low-level mouse events into application-domain events. 
 * 
 * @since 1.0
 */
package draw.controller.handler;
