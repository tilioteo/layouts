/**
 * 
 */
package org.vaadin.layouts.client.simplelayout;

import org.vaadin.layouts.SimpleLayout;
import org.vaadin.layouts.client.VSimpleLayout;
import org.vaadin.layouts.client.orderedlayout.TinyVerticalLayoutConnector;
import org.vaadin.layouts.shared.simplelayout.SimpleLayoutState;

import com.vaadin.shared.ui.Connect;
import com.vaadin.shared.ui.Connect.LoadStyle;

/**
 * @author Kamil Morong, Tilioteo Ltd
 *
 */
/**
 * Connects the client widget {@link VSimpleLayout} with the Vaadin server side
 * counterpart {@link SimpleLayout}
 */
@SuppressWarnings("serial")
@Connect(value = SimpleLayout.class, loadStyle = LoadStyle.EAGER)
public class SimpleLayoutConnector extends TinyVerticalLayoutConnector {

	/*
	 * (non-Javadoc)
	 *
	 * @see com.vaadin.client.ui.AbstractComponentConnector#getWidget()
	 */
	@Override
	public VSimpleLayout getWidget() {
		return (VSimpleLayout) super.getWidget();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.vaadin.client.ui.AbstractLayoutConnector#getState()
	 */
	@Override
	public SimpleLayoutState getState() {
		return (SimpleLayoutState) super.getState();
	}
}
