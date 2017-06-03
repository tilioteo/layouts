/**
 * 
 */
package org.vaadin.layouts;

import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Logger;
import java.util.stream.Stream;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.vaadin.layouts.shared.simplelayout.SimpleLayoutState;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.orderedlayout.AbstractOrderedLayoutState.ChildComponentData;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.Layout;
import com.vaadin.ui.declarative.DesignAttributeHandler;
import com.vaadin.ui.declarative.DesignContext;

/**
 * @author Kamil Morong, Tilioteo Ltd
 *
 */
@SuppressWarnings("serial")
public class SimpleLayout extends AbstractLayout implements Layout.AlignmentHandler, Layout.MarginHandler {

	public static final Alignment ALIGNMENT_DEFAULT = Alignment.TOP_LEFT;

	/**
	 * Custom layout slots containing the components.
	 */
	protected Component component;

	private Alignment defaultComponentAlignment = Alignment.MIDDLE_CENTER;

	/* Child component alignments */

	/**
	 * Constructs an empty SimpleLayout.
	 */
	public SimpleLayout() {
		setMargin(false);
	}

	@Override
	protected SimpleLayoutState getState() {
		return (SimpleLayoutState) super.getState();
	}

	@Override
	protected SimpleLayoutState getState(boolean markAsDirty) {
		return (SimpleLayoutState) super.getState(markAsDirty);
	}

	/**
	 * Add a component into this container.
	 *
	 * @param c
	 *            the component to be added.
	 */
	@Override
	public void addComponent(Component c) {
		// Add to components before calling super.addComponent
		// so that it is available to AttachListeners
		component = c;
		try {
			super.addComponent(c);
		} catch (IllegalArgumentException e) {
			component = null;
			throw e;
		}
		componentAdded(c);
	}

	private void componentRemoved(Component c) {
		getState().childData.remove(c);
	}

	private void componentAdded(Component c) {
		ChildComponentData ccd = new ChildComponentData();
		ccd.alignmentBitmask = getDefaultComponentAlignment().getBitMask();
		getState().childData.put(c, ccd);
	}

	/**
	 * Removes the component from this container.
	 *
	 * @param c
	 *            the component to be removed.
	 */
	@Override
	public void removeComponent(Component c) {
		component = null;
		super.removeComponent(c);
		componentRemoved(c);
	}

	/**
	 * Gets the component container iterator for going trough all the components
	 * in the container.
	 *
	 * @return the Iterator of the components inside the container.
	 */
	@Override
	public Iterator<Component> iterator() {
		return Stream.of(component).iterator();
	}

	/**
	 * Gets the number of contained components. Consistent with the iterator
	 * returned by {@link #getComponentIterator()}.
	 *
	 * @return the number of contained components
	 */
	@Override
	public int getComponentCount() {
		return component != null ? 1 : 0;
	}

	/* Documented in superclass */
	@Override
	public void replaceComponent(Component oldComponent, Component newComponent) {

		Alignment alignment = getComponentAlignment(component);

		removeComponent(component);
		addComponent(newComponent);
		applyLayoutSettings(newComponent, alignment);

		// markAsDirty();
	}

	@Override
	public void setComponentAlignment(Component childComponent, Alignment alignment) {
		ChildComponentData childData = getState().childData.get(component);
		if (childData != null) {
			// Alignments are bit masks
			childData.alignmentBitmask = alignment.getBitMask();
		} else {
			throw new IllegalArgumentException(
					"Component must be added to layout before using setComponentAlignment()");
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.vaadin.ui.Layout.AlignmentHandler#getComponentAlignment(com
	 * .vaadin.ui.Component)
	 */
	@Override
	public Alignment getComponentAlignment(Component childComponent) {
		ChildComponentData childData = getState().childData.get(childComponent);
		if (childData == null) {
			throw new IllegalArgumentException("The given component is not a child of this layout");
		}

		return new Alignment(childData.alignmentBitmask);
	}

	/**
	 * Returns the component.
	 *
	 * @return The component.
	 */
	public Component getComponent() {
		return component;
	}

	@Override
	public void setMargin(boolean enabled) {
		setMargin(new MarginInfo(enabled));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.vaadin.ui.Layout.MarginHandler#getMargin()
	 */
	@Override
	public MarginInfo getMargin() {
		return new MarginInfo(getState(false).marginsBitmask);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.vaadin.ui.Layout.MarginHandler#setMargin(MarginInfo)
	 */
	@Override
	public void setMargin(MarginInfo marginInfo) {
		getState().marginsBitmask = marginInfo.getBitMask();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.vaadin.ui.Layout.AlignmentHandler#getDefaultComponentAlignment()
	 */
	@Override
	public Alignment getDefaultComponentAlignment() {
		return defaultComponentAlignment;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.vaadin.ui.Layout.AlignmentHandler#setDefaultComponentAlignment(com
	 * .vaadin.ui.Alignment)
	 */
	@Override
	public void setDefaultComponentAlignment(Alignment defaultAlignment) {
		defaultComponentAlignment = defaultAlignment;
	}

	private void applyLayoutSettings(Component target, Alignment alignment) {
		setComponentAlignment(target, alignment);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.vaadin.ui.AbstractComponent#readDesign(org.jsoup.nodes .Element,
	 * com.vaadin.ui.declarative.DesignContext)
	 */
	@Override
	public void readDesign(Element design, DesignContext designContext) {
		// process default attributes
		super.readDesign(design, designContext);

		setMargin(readMargin(design, getMargin(), designContext));

		// handle children
		for (Element childComponent : design.children()) {
			Attributes attr = childComponent.attributes();
			Component newChild = designContext.readDesign(childComponent);
			addComponent(newChild);
			// handle alignment
			setComponentAlignment(newChild, DesignAttributeHandler.readAlignment(attr));
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.vaadin.ui.AbstractComponent#writeDesign(org.jsoup.nodes.Element
	 * , com.vaadin.ui.declarative.DesignContext)
	 */
	@Override
	public void writeDesign(Element design, DesignContext designContext) {
		// write default attributes
		super.writeDesign(design, designContext);

		AbstractOrderedLayout def = designContext.getDefaultInstance(this);

		writeMargin(design, getMargin(), def.getMargin(), designContext);

		// handle children
		if (!designContext.shouldWriteChildren(this, def)) {
			return;
		}

		for (Component child : this) {
			Element childElement = designContext.createElement(child);
			design.appendChild(childElement);
			// handle alignment
			Alignment alignment = getComponentAlignment(child);
			if (alignment.isMiddle()) {
				childElement.attr(":middle", true);
			} else if (alignment.isBottom()) {
				childElement.attr(":bottom", true);
			}
			if (alignment.isCenter()) {
				childElement.attr(":center", true);
			} else if (alignment.isRight()) {
				childElement.attr(":right", true);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.vaadin.ui.AbstractComponent#getCustomAttributes()
	 */
	@Override
	protected Collection<String> getCustomAttributes() {
		Collection<String> customAttributes = super.getCustomAttributes();
		customAttributes.add("margin");
		customAttributes.add("margin-left");
		customAttributes.add("margin-right");
		customAttributes.add("margin-top");
		customAttributes.add("margin-bottom");
		return customAttributes;
	}

	private static Logger getLogger() {
		return Logger.getLogger(SimpleLayout.class.getName());
	}
}
