/**
 * 
 */
package org.vaadin.layouts;

import com.vaadin.shared.ui.orderedlayout.VerticalLayoutState;
import com.vaadin.ui.Component;

/**
 * @author Kamil Morong, Tilioteo Ltd
 *
 */
@SuppressWarnings("serial")
public class TinyVerticalLayout extends AbstractTinyOrderedLayout {

	/**
	 * Constructs an empty TinyVerticalLayout.
	 */
	public TinyVerticalLayout() {
		setWidth("100%");
		setSpacing(true);
		setMargin(true);
	}

	/**
	 * Constructs a TinyVerticalLayout with the given components. The components
	 * are added in the given order.
	 *
	 * @see AbstractTinyOrderedLayout#addComponents(Component...)
	 *
	 * @param children
	 *            The components to add.
	 */
	public TinyVerticalLayout(Component... children) {
		this();
		addComponents(children);
	}

	@Override
	protected VerticalLayoutState getState() {
		return (VerticalLayoutState) super.getState();
	}

	@Override
	protected VerticalLayoutState getState(boolean markAsDirty) {
		return (VerticalLayoutState) super.getState(markAsDirty);
	}

	/**
	 * Adds the given components to this layout and sets them as expanded. The
	 * height of all added child components are set to 100% so that the
	 * expansion will be effective. The height of this layout is also set to
	 * 100% if it is currently undefined.
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

		if (getHeight() < 0) {
			setHeight(100, Unit.PERCENTAGE);
		}

		for (Component child : components) {
			child.setHeight(100, Unit.PERCENTAGE);
			setExpandRatio(child, 1);
		}
	}
}
