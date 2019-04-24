package org.conterosoft.jukebox;

public class Album 
{
	private int albumId;
	private String albumName;
	
	public Album(String albumName, int albumId)
	{
		this.albumName = albumName;
		this.albumId=albumId;
	}
	
	public String getAlbumName()
	{
		return albumName;
	}
	
	public int getAlbumId()
	{
		return albumId;
	}
	
	@Override
	public String toString()
	{
		return albumName;
	}
}
