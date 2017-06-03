/**
 * 
 */
package org.vaadin.layouts.client.orderedlayout;

import org.vaadin.layouts.TinyHorizontalLayout;

import com.vaadin.client.ui.VHorizontalLayout;
import com.vaadin.shared.ui.Connect;
import com.vaadin.shared.ui.Connect.LoadStyle;
import com.vaadin.shared.ui.orderedlayout.HorizontalLayoutState;

/**
 * @author Kamil Morong, Tilioteo Ltd
 *
 */
/**
 * Connects the client widget {@link VHorizontalLayout} with the Vaadin server
 * side counterpart {@link TinyHorizontalLayout}
 */
@SuppressWarnings("serial")
@Connect(value = TinyHorizontalLayout.class, loadStyle = LoadStyle.EAGER)
public class TinyHorizontalLayoutConnector extends AbstractTinyOrderedLayoutConnector {

	/*
	 * (non-Javadoc)
	 *
	 * @see com.example.ui.AbstractTinyOrderedLayoutConnector# getWidget ()
	 */
	@Override
	public VHorizontalLayout getWidget() {
		return (VHorizontalLayout) super.getWidget();
	}

	@Override
	public HorizontalLayoutState getState() {
		return (HorizontalLayoutState) super.getState();
	}
}
