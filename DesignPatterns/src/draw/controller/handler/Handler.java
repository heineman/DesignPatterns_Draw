package draw.controller.handler;

import java.awt.Graphics;
import java.awt.Point;
import java.util.Optional;

import draw.controller.IActionInterface;
import draw.model.Element;
import draw.model.Model;
import draw.palette.Action;
import draw.tools.Tools;
import draw.view.DrawingPalette;

/**
 * Handler for Chain of Responsibilities.
 * 
 * This class provides a default terminal implementation, that is, invocations on any of the methods
 * in IActionInterface are ignored, and false is returned.
 * 
 * <img src="doc-files/chain-of-responsibility.png" alt="Chain of Responsibility Pattern">
 */
public abstract class Handler implements IActionInterface {
	protected Model model;
	protected DrawingPalette view;
	
	/** Next handler in the chain of responsibility. */
	Optional<Handler> next = Optional.empty();
	
	public Handler(Model model, DrawingPalette view) {
		this.model = model;
		this.view = view;
	}
	
	/** 
	 * Return next one in chain, to allow external configuration.
	 * 
	 * @return  Return next one in the chain.
	 */
	public Optional<Handler> getNext() {
		return next;
	}
	
	/**
	 * Uses currently selected action to determine if this handler is interested in processing events.
	 * Each handler subclass overrides the {@link #interested(Action)} method to declare actions of interest. 
	 * @return    whether a given handler is interested in processing events
	 */
	public boolean isInterested() {
		return interested(Tools.getInstance().getActiveTool().action);
	}
	
	/** Determines whether a handler is interested in current tool action. Override for restrictions. */
	protected abstract boolean interested(Action action);
	
	/**
	 * Add next to be the next in the chain.
	 * 
	 * In draw.2, fixed to return next instead of self to chain properly. 
	 * @param next   Next handler to append
	 * @return   next, so programmer can chain together
	 */
	public Handler setNext(Handler next) {
		this.next = Optional.of(next);
		return next;
	}
	
	@Override
	public boolean move(Point pt, int modifiers, Optional<Element> elt, int anchor) { 
		if (next.isPresent()) {
			return next.get().move(pt, modifiers, elt, anchor);
		}
		
		return false;    // end of the line
	}

	@Override
	public boolean start(Point pt, int modifiers, Optional<Element> elt) {
		if (next.isPresent()) {
			return next.get().start(pt, modifiers, elt);
		}

		return false;    // end of the line
	}
	
	@Override
	public boolean drag(Point start, int modifiers, Optional<Element> elt, Point current) {
		if (next.isPresent()) {
			return next.get().drag(start, modifiers, elt, current);
		}

		return false;    // end of the line
	}
	
	@Override
	public boolean complete(Point pt, int modifiers, Optional<Element> elt) {
		if (next.isPresent()) {
			return next.get().complete(pt, modifiers, elt);
		}

		return false;    // end of the line
	}

	@Override
	public boolean startAnchor(Point pt, int modifiers, Element elt, int anchor) {
		if (next.isPresent()) {
			return next.get().startAnchor(pt,  modifiers, elt, anchor);
		}

		return false;    // end of the line
	}
	
	@Override
	public boolean dragAnchor(Point start, int modifiers, Element elt, Point current) {
		if (next.isPresent()) {
			return next.get().dragAnchor(start, modifiers, elt, current);
		}

		return false;    // end of the line
	}

	@Override
	public boolean completeAnchor(Point pt, int modifiers, Element elt) {
		if (next.isPresent()) {
			return next.get().completeAnchor(pt, modifiers, elt);
		}

		return false;    // end of the line
	}
	
	/**
	 * Each handler gets the chance to override and provide specific paint capabilities after elements are drawn. 
	 * @param g  Graphics context into which to draw whatever is necessary based on the tool
	 */
	public void paint(Graphics g) {
		if (next.isPresent()) {
			next.get().paint(g);
		}
	}
}
