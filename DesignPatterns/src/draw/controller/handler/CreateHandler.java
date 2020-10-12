package draw.controller.handler;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Optional;

import draw.model.Element;
import draw.model.Model;
import draw.palette.Action;
import draw.palette.Create;
import draw.tools.Tools;
import draw.view.DrawingPalette;

public class CreateHandler extends Handler {

	/** New element being created, if present. */
	Optional<Element> createdElement = Optional.empty();
	
	public CreateHandler(Model model, DrawingPalette view) {
		super(model, view);
	}

	@Override
	protected boolean interested(Action action) {
		return action.equals(Action.Create);
	}

	/**
	 * Initiate the beginning of a drag action.
	 * 
	 * Since draw.3 creates element with applied style from view.
	 * 
	 * Might be over an existing element -- might be over empty space.
	 */
	@Override
	public boolean start(Point start, int modifiers, Optional<Element> elt) {
		// protect to only continue if current action is creational
		if (isInterested()) {
			// unselect everything
			for (Element e : model) {
				e.setSelected(false);
			}

			// Safe to cast because we are protected by 'isInterested' above. Once we know the
			// active tool, then we know when Element to create.
			Create action = (Create) Tools.getInstance().getActiveTool();   
			createdElement = Optional.of(action.prototype.clone());
			createdElement.get().setBoundingBox(new Rectangle(start));
			createdElement.get().setStyle(view.getStyle());  

			view.repaint();
		}

		return super.start(start, modifiers, elt); 
	}

	/** Add the given object to the actual model. */
	@Override
	public boolean complete(Point pt, int modifiers, Optional<Element> elt) {
		if (isInterested()) {

			if (createdElement.isPresent()) {
				Element newOne = createdElement.get();

				// be careful not to add "EMPTY" elements. And be sure it is
				// selected upon completion
				if (!newOne.getBoundingBox().isEmpty()) {
					newOne.setSelected(true);
					model.add(newOne);

					// reset to select tool
					view.chooseTool(Tools.getInstance().getSelectTool());
					view.repaint();
				}
			}
		}

		return super.complete(pt, modifiers, elt); 
	}

	@Override
	public boolean drag(Point starting, int modifiers, Optional<Element> elt, Point current) {
		if (isInterested()) {
			if (!createdElement.isPresent()) {
				return false;
			}

			Rectangle r = new Rectangle(starting);
			r.add(current);

			// this newly created elements needs be drawn separately (last)
			createdElement.get().setBoundingBox(r);
			view.repaint();
		}

		return super.drag(starting, modifiers, elt, current); 
	}

	@Override
	public void paint(Graphics g) {
		if (isInterested()) {
			if (createdElement.isPresent()) {
				createdElement.get().drawElement(g);
			}

			super.paint(g);
		}
	}
}
