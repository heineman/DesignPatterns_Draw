/**
 * Available tools for application.
 * <p>
 * Default tools are:
 *
 * <ol>
 * <li> {@link draw.tools.RectangleElt} -- Draws Rectangle Elements.</li>
 * <li> {@link draw.tools.OvalElt} -- Draws Oval Elements.</li>
 * </ol>
 * <p>
 * All tools are registered in the {@link draw.tools.Tools} class. As new tools
 * are implemented, be sure to update in the {@link draw.tools.Tools} repository.
 * 
 * Tools class implements the Singleton Design Pattern.
 * 
 * ToolTemplate implements a Template method, used whenever a new tool is registered with the system.
 * @since 1.0
 */
package draw.tools;
