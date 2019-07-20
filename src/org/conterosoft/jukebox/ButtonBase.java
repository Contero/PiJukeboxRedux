package org.conterosoft.jukebox;

import javafx.scene.control.Button;

public abstract class ButtonBase extends Button
{
	ButtonBase(String label)
	{
		super(label);
	}
	
	public abstract <T> T getObject();
}
