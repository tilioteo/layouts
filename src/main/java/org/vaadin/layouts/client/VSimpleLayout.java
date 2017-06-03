/**
 * 
 */
package org.vaadin.layouts.client;

import com.vaadin.client.ui.VVerticalLayout;

/**
 * @author morongk
 *
 */
public class VSimpleLayout extends VVerticalLayout {

	public static final String CLASSNAME = "v-simplelayout";

	/**
	 * Default constructor
	 */
	public VSimpleLayout() {
		setStyleName(CLASSNAME);
	}

	@Override
	public void setStyleName(String style) {
		super.setStyleName(style);
		addStyleName("v-simple");
	}
}
