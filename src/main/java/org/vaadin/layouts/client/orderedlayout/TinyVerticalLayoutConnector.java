/**
 * 
 */
package org.vaadin.layouts.client.orderedlayout;

import org.vaadin.layouts.TinyVerticalLayout;

import com.vaadin.client.ui.VVerticalLayout;
import com.vaadin.shared.ui.Connect;
import com.vaadin.shared.ui.Connect.LoadStyle;
import com.vaadin.shared.ui.orderedlayout.VerticalLayoutState;

/**
 * @author Kamil Morong, Tilioteo Ltd
 *
 */
/**
 * Connects the client widget {@link VVerticalLayout} with the Vaadin server
 * side counterpart {@link TinyVerticalLayout}
 */
@SuppressWarnings("serial")
@Connect(value = TinyVerticalLayout.class, loadStyle = LoadStyle.EAGER)
public class TinyVerticalLayoutConnector extends AbstractTinyOrderedLayoutConnector {

	/*
	 * (non-Javadoc)
	 *
	 * @see com.example.ui.AbstractTinyOrderedLayoutConnector# getWidget ()
	 */
	@Override
	public VVerticalLayout getWidget() {
		return (VVerticalLayout) super.getWidget();
	}

	@Override
	public VerticalLayoutState getState() {
		return (VerticalLayoutState) super.getState();
	}
}
