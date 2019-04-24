package org.conterosoft.jukebox;

import javafx.scene.control.Button;
import javafx.scene.paint.Color;

public class SongButton extends Button 
{
	private Song song;
	
	public Song getSong() 
	{ 
		return song; 
	}
	
	public SongButton(Song song)
	{
		super(song.toString());
		this.song = song;
		this.setPrefWidth(250);
		this.setOnAction(event -> {
			JukeboxPi.playListView.getItems().add(song);
			JukeboxPi.playlist.add(song);
		});
		
		this.setOnTouchPressed(event -> {
			this.setStyle("-fx-background-color: lightgrey; ");
		});
		
		this.setOnTouchReleased(event -> {
			try 
			{
				Thread.sleep(500);
				this.setStyle(null);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		});
	}
}
