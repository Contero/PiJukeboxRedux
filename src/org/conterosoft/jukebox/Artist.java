package org.conterosoft.jukebox;

import java.util.ArrayList;
import java.util.List;

public class Artist 
{
	private String artist;
	private int artistId;
	private List<Album> albums;
	
	public String getArtist() {
		return artist;
	}

	public int getArtistId() {
		return artistId;
	}

	public List<Album> getAlbums() {
		return albums;
	}


	Artist(String artist, int artistId) 
	{
		this.artist = artist;
		this.artistId = artistId;
		albums = new ArrayList<Album>();
	}
	
	@Override
	public String toString()
	{
		return artist;
	}
}
