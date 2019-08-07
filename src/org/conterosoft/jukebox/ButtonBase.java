package org.conterosoft.jukebox;

import javafx.scene.control.Button;

public abstract class ButtonBase extends Button
{
	/*
	 * Constructor, passes button text to JavaFX Button constructor
	 */
	ButtonBase(String label)
	{
		super(label);
	}
	
	/*
	 * abstract method to return whatever object is held by button
	 */
	public abstract <T> T getObject();
}
