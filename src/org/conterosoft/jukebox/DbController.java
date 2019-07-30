package org.conterosoft.jukebox;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.sqlite.JDBC;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.io.File;
import java.io.IOException;

public class DbController 
{
	
	private Connection db = null;
	
	private int artistCount,
				albumCount,
				songCount;
	private Artist artist;
	private Album album;
	private List<Artist> artists = new ArrayList<Artist>();
	
	private String lastArtist = "",
					lastAlbum = "";
	private int lastArtistId = 0,
				lastAlbumId = 0;
	private JukeboxPi app;

	
	public int getArtistCount() { return artistCount; }
	public int getAlbumCount() { return albumCount; }
	public int getSongCount() { return songCount; }
	/**
	 * Constructor: creates database connection and creates tables if the do not exist
	 * @throws SQLException
	 */
	public DbController(JukeboxPi app) throws SQLException
	{
		final String dbfile = "jdbc:sqlite:juke.db";
		DriverManager.registerDriver(new JDBC());
		db = DriverManager.getConnection(dbfile);
		this.app = app;
		
		DatabaseMetaData md = db.getMetaData();
		ResultSet rs = md.getTables(null, null, "artists", null);
		
		if (!rs.next())
		{
			makeTables();
		}
		
		rs.close();
	}	
	
	/**
	 * finalize, closes the database connection
	 */
	@Override
	public void finalize() throws SQLException
	{
		db.close();
	}
	
	/**
	 * Returns an ArrayList of Artist objects
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<Artist> getArtists() throws SQLException
	{
		ArrayList<Artist> artists = new ArrayList<Artist>();
		
		Statement stmt = db.createStatement();
		stmt.execute("select artist_id, artist_name from artists order by artist_name;");
		ResultSet rs = stmt.getResultSet();
		
		while (rs.next())
		{
			artists.add(new Artist(rs.getString(2), rs.getInt(1)));
		}
		
		rs.close();
		
		return  artists;
	}
	
	/**
	 * Returns an ArrayList of album objects for a given artistId
	 * @param artistId
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<Album> getAlbums(int artistId) throws SQLException
	{
		ArrayList<Album> albums = new ArrayList<Album>();
		
		PreparedStatement pstmt = db.prepareStatement("select album_id, album_name from albums where artist_id = ? order by album_name");
		pstmt.setInt(1, artistId);
		ResultSet rs = pstmt.executeQuery();
		
		while (rs.next())
		{
			albums.add(new Album(rs.getString(2), rs.getInt(1)));
		}
		
		rs.close();
		return albums;
	}
	
	
	public List<Song> getSongs(int albumId) throws SQLException
	{
		PreparedStatement pstmt = db.prepareStatement("Select song_name, song_location, track_no from songs where album_id = ? order by track_no");
		pstmt.setInt(1, albumId);
		
		ResultSet rs = pstmt.executeQuery();
		ArrayList<Song> songs = new ArrayList<Song>();
		
		while (rs.next())
		{
			songs.add(new Song(rs.getString(1), rs.getString(2), rs.getInt(3)));
		}
		
		rs.close();
		
		return songs;
	}
	
	public void libraryScan() throws SQLException, IOException
	{
		//reset counters
		artistCount = albumCount = songCount = 0;
		
		//Do I have a settings
		if(app.settingsObj.getHasRoot())
		{
			makeTables();
			folderscan(new File(app.settingsObj.getRoot()));
		}
		else
		{
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setContentText("No root folder");
			alert.showAndWait();
		}
	}
	
	private void makeTables()
	{
		String[] tables = {"songs", "artists", "albums"};
		String sqlDrop = "drop table if exists ",
				sqlCreateSongs = "Create table songs (song_id integer primary key, album_id varchar(255), song_name varchar(255), song_location varchar(255), track_no integer);",
				sqlCreateAlbums = "create table albums(album_id integer primary key, artist_id integer, album_name varchar(255));",
				sqlCreateArtists = "create table artists(artist_id integer primary key, artist_name varchar(255));";
		try
		{
			Statement statement = db.createStatement();
			for (String table : tables)
			{
				statement.execute(sqlDrop + table + ";");
			}
			
			statement.execute(sqlCreateSongs);
			statement.execute(sqlCreateAlbums);
			statement.execute(sqlCreateArtists);
			
		}
		catch (Exception e)
		{
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setContentText("Error: " + e.getMessage());
			alert.showAndWait();
		}
	}
	
	//recursive search method to scan drive
	private void folderscan(File baseFolder) throws SQLException, IOException
	{
		File[] files = baseFolder.listFiles();
		
		for(File file : files)
		{
			if(file.isDirectory()) 
			{ 
				folderscan(file); 
			}
			else if (file.isFile()) { processFile(file); }
			else System.out.println("sup with " + file + "?");
		}
	}
	
	private void processFile(File file) throws SQLException, IOException 
	{
		
		String thisArtist;
		String thisAlbum;
		PreparedStatement insertArtist = db.prepareStatement("Insert into artists (artist_name) values (?);");
		PreparedStatement insertAlbum = db.prepareStatement("Insert into albums (album_name, artist_id) values (?,?);");
		PreparedStatement insertSong = db.prepareStatement("Insert into songs (song_name, album_id, song_location, track_no) values (?,?,?,?);");
		PreparedStatement getID= db.prepareStatement("Select last_insert_rowid();");
		
		MusicFile musicFile = new MusicFile(file);
		try
		{
			if (musicFile.getIsMusic())
			{
				Song song = musicFile.getSong();
				thisArtist = song.getArtist();
				thisAlbum = song.getAlbum();
				
				if (!thisArtist.equals(lastArtist))
				{
					if (!artists.stream().filter(obj -> obj.getArtist().equals(thisArtist)).findFirst().isPresent())
					{
						insertArtist.setString(1, thisArtist);
						insertArtist.execute();
						artistCount++;
						
						lastArtist = thisArtist;
						lastArtistId = getID.executeQuery().getInt(1);
						
						artists.add(new Artist(thisArtist, lastArtistId));
						
						artist = artists.get(artists.size()-1);
					}
					else
					{
						artist = artists.stream().filter(obj -> obj.getArtist().equals(thisArtist)).findFirst().get();
						
						lastArtist = thisArtist;
						lastArtistId = artist.getArtistId();
					}
				}
				
				//if album no exist, add
				if(!thisAlbum.equals(lastAlbum)) 
				{
					if (!artist.getAlbums().stream().filter(a -> a.getAlbumName().equals(thisAlbum)).findFirst().isPresent())
					{
						insertAlbum.setString(1, thisAlbum);
						insertAlbum.setInt(2, lastArtistId);
						insertAlbum.execute();
						albumCount++;
						
						lastAlbum=thisAlbum;
						lastAlbumId = getID.executeQuery().getInt(1);
				
						artist.getAlbums().add(new Album(thisAlbum, lastAlbumId));
					
					}
					else
					{
						album = artist.getAlbums().stream().filter(a -> a.getAlbumName().equals(thisAlbum)).findFirst().get();
						
						lastAlbum=thisAlbum;
						lastAlbumId = album.getAlbumId();
					}
				}
				
				//add song n'shit
				insertSong.setString(1, song.getSongName());
				insertSong.setInt(2, lastAlbumId);
				insertSong.setString(3,file.getPath());
				insertSong.setInt(4, song.getTrackNo());
				insertSong.execute();
				songCount++;
				
				app.adminStage.setMessage(artistCount, albumCount, songCount);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
