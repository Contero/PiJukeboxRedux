package org.conterosoft.jukebox;

public class Song {
	private String songName,
					fileLocation,
					album,
					artist;
	private int trackNo;
	
	public String getSongName() { return songName; }
	public String getAlbum() { return album; }
	public String getArtist() { return artist; }
	public int getTrackNo() { return trackNo; }
	
	public void setSongName(String songName) {
		this.songName = songName;
	}
	public String getFileLocation() {
		return fileLocation;
	}
	public void setFileLocation(String fileLocation) {
		this.fileLocation = fileLocation;
	}
	
	public Song(String songName, String fileLocation, String album, String artist, int trackNo)
	{
		this.songName = songName;
		this.fileLocation = fileLocation;
		this.album = album;
		this.artist = artist;
		this.trackNo = trackNo;
		
	}
	
	public Song(String songName, String fileLocation, int trackNo)
	{
		this.songName = songName;
		this.fileLocation = fileLocation;
		this.trackNo = trackNo;
	}
	
	@Override
	public String toString()
	{
		return songName;
	}
}
