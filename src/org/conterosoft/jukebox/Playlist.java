package org.conterosoft.jukebox;

import java.util.Queue;
import java.util.LinkedList;

public class Playlist 
{
	private Queue<Song> playlist =  new LinkedList<Song>();
	private boolean empty;
	
	/*
	 * Constructor, sets empty playlist filter to true
	 */
	public Playlist()
	{
		empty = true;
	}
	
	/*
	 * Accepts reference to app and a song object
	 * adds song to playlist, starts player if it's empty
	 * sets emtpy flag to false
	 */
	public void add(JukeboxPi app, Song song)
	{
		playlist.add(song); 
		if (empty && !app.player.getIsPlaying()) 
		{ 
			app.player.start(app);
		}
		else if (empty)
		{
			empty = false;
		}
	}
		
	/*
	 * returns the next song on the playlist, removes it and sets empty playlist flag
	 * if its empty
	 */
	public Song getNext()
	{
		Song song = playlist.poll();
		if (playlist.size() == 0) 
		{ 
			empty = true; 
		}
		
		return song;
	}
	
	/*
	 * returns the value of the empty playlist flag
	 */
	public boolean getIsEmpty() 
	{ 
		return empty; 
	}
}


