/**
 * <p>Classes that model desired actions by user initiated with the mouse.</p>
 * 
 * <p>Currently, two actions initiated by the mouse are:</p>
 * 
 * <ol>
 * <li> {@link draw.palette.Create} -- Create a new element, based on the active tool.</li>
 * <li> {@link draw.palette.Select} -- Select an existing element.</li>
 * </ol>
 * 
 * <p>Each {@link draw.palette.PaletteEntry} has a visible presence with two icons -- a normal one and
 * a selected one. These {@link draw.palette.PaletteEntry} objects are meant to model the actions. 
 * The real behaviors are encoded within the {@link draw.controller.handler} package.</p>
 * 
 * <p>The mouse behavior supports press, followed by a drag, with a final release to signal the 
 * completion of an activity. Move events are supported to provide, for example, the ability to change
 * the mouse cursor when the mouse moves over a specific region.</p>
 * 
 * @since 1.0
 */
package draw.palette;
