/**
 * Entity classes that represent the application domain.
 * <p>
 * A number of different elements can be added by the user. Each has a fixed bounding box, and
 * appropriate subclasses can be found in {@link draw.tools} package.
 * 
 * In draw.1 we incorporate the Composite design pattern:
 * 
 * <img src="doc-files/composite.png" alt="Composite Pattern">
 * 
 * Each {@link Group} is treated as a single {@link Element} because {@link Group} extends
 * {@link Element}.
 * 
 * @since 1.0
 */
package draw.model;
