package com.AtolCD.client.GAT.utils;

import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.DOM;




public class BlindedPopup implements ClickListener
{
    PopupPanel popup;
    PopupPanel glass;
    public BlindedPopup(String message, boolean addDefaultStyles, boolean button)
    {
        /*********************************************
         * A glass panel or 'blinder'
         * to wash out the current screen
         ********************************************/
        glass = new PopupPanel();
        glass.setStyleName("rx-glass");
        /*
         * Set full screen
         */
        DOM.setStyleAttribute(glass.getElement(), "width", "100%");
        DOM.setStyleAttribute(glass.getElement(), "height", "100%");
        /*
         * Add default styles if required
         */
        if (addDefaultStyles)
        {
            DOM.setStyleAttribute(glass.getElement(),"backgroundColor", "#000000");
            DOM.setStyleAttribute(glass.getElement(),"opacity", "0.70");
            DOM.setStyleAttribute(glass.getElement(),"z-index", "70");
            DOM.setStyleAttribute(glass.getElement(),"-moz-opacity", "0.70");
            DOM.setStyleAttribute(glass.getElement(),"filter",  " alpha(opacity=70)");
        }
        /**********************************************
         * A popup
         *********************************************/
        popup = new PopupPanel(false);
        popup.setStyleName("rx-BlindedPopup");
        /*
         * Add default styles if required
         */
        if (addDefaultStyles)
        {
            DOM.setStyleAttribute(popup.getElement(),"backgroundColor", "#ffffff");
            DOM.setStyleAttribute(popup.getElement(), "border", "3px solid #888888");
        }
		
        /*
         * Popups can only hold one widget,
         * so we need something to keep all
         * our bits in
         */
        VerticalPanel popupContents = new VerticalPanel();
        /*
         * The header element
         */
        Label header = new Label("Information");
        header.setStyleName("rx-BlindedPopup-header");
        if (addDefaultStyles)
        {
            DOM.setStyleAttribute(header.getElement(),"backgroundColor", "#ffffff");
            DOM.setStyleAttribute(header.getElement(),"borderBottom", "3px solid #888888");
            DOM.setStyleAttribute(header.getElement(),"fontWeight", "bold");
            DOM.setStyleAttribute(header.getElement(),"fontSize", "90%");
            DOM.setStyleAttribute(header.getElement(),"padding", "5px");
            DOM.setStyleAttribute(header.getElement(),"textAlign", "center");
        }
        /*
         * The main element
         */
		HTML html; 
		
		if(button){
			html = new HTML(message);
		}
		else
		{
			html = new HTML(message+"<br><center><img height=\"32px\" width=\"32px\"  src='loading.gif'></center>");
		}
        html.setStyleName("rx-BlindedPopup-message");
        if (addDefaultStyles)
        {
            DOM.setStyleAttribute(html.getElement(),"fontSize", "80%");
            DOM.setStyleAttribute(html.getElement(),"padding", "15px");
        }
        /*
         * A button to close the popup
         */
        Button closeButton = new Button("Close", this);
        DOM.setStyleAttribute(closeButton.getElement(), "width","80px");
        /*
         * Assemble it all
         */
        popupContents.add(header);
        popupContents.add(html);
		if(button){
			popupContents.add(closeButton);
			popupContents.setCellHorizontalAlignment(closeButton,HasAlignment.ALIGN_CENTER);
		}
        popup.add(popupContents);
        /*
         * Show the glass first, then the popup will be over it
         */
        glass.show();
        popup.center();
    }
    public void onClick(Widget sender)
    {
        popup.hide();
        glass.hide();
    }
	public void fermer()
    {
        popup.hide();
        glass.hide();
    }
	    public void ouvrir()
    {
        glass.show();
        popup.center();
    }
	
	
	
}