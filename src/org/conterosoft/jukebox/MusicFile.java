package org.conterosoft.jukebox;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

public class MusicFile 
{
	private boolean isMusic;
	private Song song;
	private String songName,
					fileLocation,
					album,
					artist;
	private int trackNo;
	
	static {

        //Disable loggers               
        Logger[] pin = new Logger[]{ Logger.getLogger("org.jaudiotagger") };

        for (Logger l : pin)
            l.setLevel(Level.OFF);
    }
	public boolean getIsMusic() { return isMusic; }
	public Song getSong() { return song; }
	
	public MusicFile(File file) throws IOException
	{
		String fileName = file.getName();
				
		if (fileName.lastIndexOf('.') < 0 ||
			(!fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase().equals("mp3")
			&& !fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase().equals("flac")))
		{
			isMusic = false;
		}
		else
		{
			isMusic = true;
			//fileLocation = file.getPath();
			fileLocation = file.getCanonicalPath();
			try
			{
				AudioFile audioFile= AudioFileIO.read(file);
				
				Tag tag = audioFile.getTag();
				songName = tag.getFirst(FieldKey.TITLE);
				album = tag.getFirst(FieldKey.ALBUM);
				artist = tag.getFirst(FieldKey.ALBUM_ARTIST);
				trackNo = Integer.parseInt(tag.getFirst(FieldKey.TRACK));
					 	
			}
			catch (Exception e)
			{
				songName = fileName;
				album = "unknown";
				artist = "unknown";
				trackNo = 0;	
			}
			song = new Song(songName, fileLocation, album, artist, trackNo);
		}
	}	
}
