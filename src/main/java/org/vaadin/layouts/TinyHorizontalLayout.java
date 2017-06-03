/**
 * 
 */
package org.vaadin.layouts;

import com.vaadin.shared.ui.orderedlayout.HorizontalLayoutState;
import com.vaadin.ui.Component;

/**
 * @author Kamil Morong, Tilioteo Ltd
 *
 */
@SuppressWarnings("serial")
public class TinyHorizontalLayout extends AbstractTinyOrderedLayout {

	/**
	 * Constructs an empty TinyHorizontalLayout.
	 */
	public TinyHorizontalLayout() {
		setSpacing(true);
	}

	/**
	 * Constructs a TinyHorizontalLayout with the given components. The
	 * components are added in the given order.
	 *
	 * @see AbstractTinyOrderedLayout#addComponents(Component...)
	 *
	 * @param children
	 *            The components to add.
	 */
	public TinyHorizontalLayout(Component... children) {
		this();
		addComponents(children);
	}

	@Override
	protected HorizontalLayoutState getState() {
		return (HorizontalLayoutState) super.getState();
	}

	@Override
	protected HorizontalLayoutState getState(boolean markAsDirty) {
		return (HorizontalLayoutState) super.getState(markAsDirty);
	}

	/**
	 * Adds the given components to this layout and sets them as expanded. The
	 * width of all added child components are set to 100% so that the expansion
	 * will be effective. The width of this layout is also set to 100% if it is
	 * currently undefined.
	 * <p>
	 * The components are added in the provided order to the end of this layout.
	 * Any components that are already children of this layout will be moved to
	 * new positions.
	 *
	 * @param components
	 *            the components to set, not <code>null</code>
	 * @since 8.0
	 */
	public void addComponentsAndExpand(Component... components) {
		addComponents(components);

		if (getWidth() < 0) {
			setWidth(100, Unit.PERCENTAGE);
		}

		for (Component child : components) {
			child.setWidth(100, Unit.PERCENTAGE);
			setExpandRatio(child, 1);
		}
	}

}
