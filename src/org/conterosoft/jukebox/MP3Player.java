package org.conterosoft.jukebox;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

import javazoom.jl.player.Player;

public class MP3Player 
{

	public MP3Player(File file)
	{
		Player player;
		try {
            FileInputStream fis = new FileInputStream(file);
        
            BufferedInputStream bis = new BufferedInputStream(fis);
            player = new Player(bis);
            player.play();
        }
        catch (Exception e) {
            System.out.println("Problem playing file " + file);
            System.out.println(e);
        }
	}
	
}
