package org.conterosoft.jukebox;

import javafx.scene.control.Button;

public class Btn<T> extends Button
{

		public T t;
	
		public Btn(T t)
		{
			this.t=t;
		}
		
		public T get() {return t;}
		
		@Override
		public String toString() { return "dfdf"; };
}
