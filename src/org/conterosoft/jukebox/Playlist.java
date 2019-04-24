package org.conterosoft.jukebox;

import java.util.Queue;
import java.util.LinkedList;

public class Playlist 
{
	private Queue<Song> playlist =  new LinkedList<Song>();
	private boolean empty;
	
	public Playlist()
	{
		empty = true;
	}
	
	public void add(Song song)
	{
		playlist.add(song); 
		if (empty && !JukeboxPi.player.getIsPlaying()) 
		{ 
			JukeboxPi.player.start();
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


