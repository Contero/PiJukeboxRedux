package org.conterosoft.jukebox;

import java.util.Queue;
import java.util.LinkedList;

public class Playlist 
{
	private Queue<Song> playlist =  new LinkedList<Song>();
	private boolean empty;
	@SuppressWarnings("unused")
	private JukeboxPi app;
	
	public Playlist()
	{
		empty = true;
	}
	
	public void add(JukeboxPi app, Song song)
	{
		playlist.add(song); 
		this.app = app;
		if (empty && !app.player.getIsPlaying()) 
		{ 
			app.player.start(app);
		}
		else if (empty)
		{
			empty = false;
		}
	}
	
	public void playNext(Song song)
	{
	//	playlist.add(position + 1, song);
	}
	
	public Song getNext()
	{
		Song song = playlist.poll();
		if (playlist.size() == 0) { empty = true; }
		
		return song;
	}
	
	public boolean getIsEmpty() { return empty; }
}


