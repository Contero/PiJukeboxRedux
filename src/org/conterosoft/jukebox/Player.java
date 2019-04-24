package org.conterosoft.jukebox;

import javafx.application.*;//.Platform;
import javafx.scene.media.*;
import java.io.File;

public class Player
{
	private Media media;
	private Song song;
	private MediaPlayer player;
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
		
		if (!isFlac)
		{
			media = new Media(file.toURI().toString());
			player = new MediaPlayer(media);
			
			player.play();
			player.setOnEndOfMedia(new Runnable() {
				@Override public void run()
				{

					player.stop();
					if (!JukeboxPi.playlist.getIsEmpty())
					{
						JukeboxPi.playListView.getItems().remove(0);
						play(JukeboxPi.playlist.getNext());
					}
					else
					{
						isPlaying = false;
					}
				}
			});
		}
		else
		{
			try
			{
				Thread thread = new Thread(() -> {
					FlacMediaPlayer flacPlayer = new FlacMediaPlayer(file);
					flacPlayer.play();

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
	}
	
	public boolean getIsPlaying() { return isPlaying; }

}
