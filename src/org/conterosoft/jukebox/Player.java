package org.conterosoft.jukebox;

import javafx.application.*;
import java.io.File;

public class Player
{

	private Song song;
	private File file;
	private boolean isPlaying = false,
					isFlac;
	
	public void start()
	{
		song = JukeboxPi.playlist.getNext();
		JukeboxPi.playListView.getItems().remove(0);
		isPlaying = true;
		play(song);
	}
	
	private void play(Song song) 
	{
		file = new File(song.getFileLocation());
		
		isFlac = ((song.getFileLocation().substring(song.getFileLocation().lastIndexOf('.') + 1).toLowerCase().equals("flac")))? true : false;

			try
			{
				Thread thread = new Thread(() -> {
					
					if (isFlac)
					{
						new FlacMediaPlayer(file);
					}
					else
					{
						new MP3Player(file);
					}
					
					Platform.runLater(() -> {
						if (!JukeboxPi.playlist.getIsEmpty())
						{
							JukeboxPi.playListView.getItems().remove(0);
							play(JukeboxPi.playlist.getNext());
						}
						else
						{
							isPlaying = false;
						}
					});
				});
				thread.setDaemon(true);
				thread.start();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
	}
	
	public boolean getIsPlaying() { return isPlaying; }

}
