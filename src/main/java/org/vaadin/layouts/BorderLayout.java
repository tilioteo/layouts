/**
 * 
 */
package org.vaadin.layouts;

import java.util.EnumMap;
import java.util.Map.Entry;
import java.util.stream.Stream;

import com.vaadin.server.SizeWithUnit;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Layout;

/**
 * @author Kamil Morong, Tilioteo Ltd
 *
 */
@SuppressWarnings("serial")
public class BorderLayout extends TinyVerticalLayout {

	public enum Position {
		NORTH, WEST, CENTER, EAST, SOUTH;
	}

	private TinyHorizontalLayout innerLayout;

	private EnumMap<Position, Layout> layoutMap = new EnumMap<>(Position.class);
	private EnumMap<Position, SizeWithUnit> sizeMap = new EnumMap<>(Position.class);

	/**
	 * Create a layout structure that mimics the traditional
	 * {@link java.awt.BorderLayout}.
	 */
	public BorderLayout() {
		initLayouts();

		setSizeFull();
		super.setMargin(false);
		super.setSpacing(false);
	}

	private void initLayouts() {
		initLayoutMap();

		innerLayout = new TinyHorizontalLayout();
		innerLayout.setSpacing(false);
		innerLayout.setMargin(false);
		innerLayout.addComponent(getLayout(Position.WEST));
		innerLayout.addComponent(getLayout(Position.CENTER));
		innerLayout.addComponent(getLayout(Position.EAST));
		innerLayout.setSizeFull();
		innerLayout.setExpandRatio(getLayout(Position.CENTER), 1);

		super.addComponent(getLayout(Position.NORTH));
		super.addComponent(innerLayout);
		super.addComponent(getLayout(Position.SOUTH));
		super.setExpandRatio(innerLayout, 1);
	}

	private void initLayoutMap() {
		Stream.of(Position.values()).forEach(e -> {
			CssLayout layout = new CssLayout();
			layout.setSizeFull();
			layoutMap.put(e, layout);
		});
	}

	private void addComponent(Layout layout, Component component) {
		if (layout != null) {
			layout.removeAllComponents();
			if (component != null) {
				component.setSizeFull();
				layout.addComponent(component);
			}
		}
	}

	private Component getComponent(Layout layout) {
		if (layout != null && layout.iterator().hasNext()) {
			return layout.iterator().next();
		}
		return null;
	}

	private Layout getLayout(Position position) {
		return layoutMap.get(position);
	}

	@Override
	public void setMargin(boolean margin) {
		if (innerLayout == null) {
			return;
		}
		super.setMargin(margin);

		innerLayout.setMargin(margin);
		markAsDirty();
	}

	@Override
	public void setSpacing(boolean spacing) {
		if (innerLayout == null) {
			return;
		}
		super.setSpacing(spacing);

		innerLayout.setSpacing(spacing);
		markAsDirty();
	}

	@Override
	public void removeComponent(Component c) {
		if (c != null) {
			replaceComponent(c, null);
		}
	}

	public void removeComponent(Position position) {
		Component c = getComponent(position);
		if (c != null) {
			replaceComponent(c, null);
		}
	}

	/**
	 * Add component into borderlayout
	 * 
	 * @param component
	 *            component to be added into layout
	 * @param position
	 *            place of the component (have to be on of Position.NORTH,
	 *            Position.WEST, Position.CENTER, Position.EAST, or
	 *            Position.SOUTH
	 */
	public void addComponent(Component component, Position position) {
		Layout layout = getLayout(position);
		if (layout != null) {
			addComponent(layout, component);
			markAsDirty();
		} else {
			throw new IllegalArgumentException("Invalid BorderLayout position.");
		}
	}

	/**
	 * Adds components into layout in the default order: 1. NORTH, 2. WEST, 3.
	 * CENTER, 4. EAST, 5. SOUTH until all slots are filled
	 * 
	 * @throws IllegalArgumentException
	 *             if layout is "full"
	 */
	@Override
	public void addComponent(Component component) {
		for (Position position : Position.values()) {
			if (null == getComponent(position)) {
				addComponent(component, position);
				return;
			}
		}
		throw new IllegalArgumentException(
				"All layout places are filled, please use addComponent(Component c, Position position) for force fill given place");
	}

	@Override
	public void replaceComponent(Component oldComponent, Component newComponent) {
		addComponent(newComponent, getPosition(oldComponent));
	}

	/**
	 * Return component from specific position
	 * 
	 * @param position
	 * @return
	 */
	public Component getComponent(Position position) {
		Layout layout = getLayout(position);
		if (layout != null) {
			return getComponent(layout);
		} else {
			throw new IllegalArgumentException("Invalid BorderLayout position.");
		}
	}

	/**
	 * Returns position of given component or null if the layout doesn't contain
	 * it
	 * 
	 * @param component
	 * @return
	 */
	public Position getPosition(Component component) {
		if (component != null) {
			for (Entry<Position, Layout> entry : layoutMap.entrySet()) {
				if (getComponent(entry.getValue()) == component) {
					return entry.getKey();
				}
			}
		}
		return null;
	}

	private void setSize(Position position, SizeWithUnit size) {
		if (position != null && position != Position.CENTER) {
			if (null == size) {
				sizeMap.remove(position);
			} else {
				sizeMap.put(position, size);
				Layout layout = getLayout(position);
				AbstractTinyOrderedLayout parentLayout = (AbstractTinyOrderedLayout) layout.getParent();

				if (size.getUnit() == Unit.PERCENTAGE) {
					parentLayout.setExpandRatio(layout, size.getSize() / 100);
				} else {
					parentLayout.setExpandRatio(layout, 0.0f);
					if (parentLayout == innerLayout) {
						layout.setWidth(size.getSize(), size.getUnit());
					} else {
						layout.setHeight(size.getSize(), size.getUnit());
					}
				}
			}
		}
	}

	private SizeWithUnit getSizeWithUnit(Position position) {
		if (position != Position.CENTER) {
			return sizeMap.get(position);
		}
		return null;
	}

	private float getSizeOrDefault(SizeWithUnit size) {
		if (size != null) {
			return size.getSize();
		}
		return 0f;
	}

	private Unit getUnitOrDefault(SizeWithUnit size) {
		if (size != null) {
			return size.getUnit();
		}
		return null;
	}

	public float getSize(Position position) {
		return getSizeOrDefault(getSizeWithUnit(position));
	}

	public Unit getSizeUnit(Position position) {
		return getUnitOrDefault(getSizeWithUnit(position));
	}

	public void setSize(Position position, String size) {
		if (position != null) {
			setSize(position, SizeWithUnit.parseStringSize(size));
		}
	}

	public void setSize(Position position, float size, Unit unit) {
		if (position != null) {
			setSize(position, new SizeWithUnit(size, unit));
		}
	}

}
