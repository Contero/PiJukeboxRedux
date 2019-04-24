package org.conterosoft.jukebox;

import java.io.*;
import java.util.Scanner;

public class Settings {
	private String rootFolder;
	private boolean hasRoot;
	private final File settings = new File("settings.dat");
	private boolean showPlaylist = false;
	
	public Settings()
	{
		//check for file
		try {
			Scanner settingsFile = new Scanner(settings);
			rootFolder = settingsFile.nextLine();
			showPlaylist = Boolean.parseBoolean(settingsFile.nextLine());
			settingsFile.close();
			hasRoot = true;
		}
		catch (Exception e)
		{
			hasRoot = false;
		}
	}
	
	public void setRoot(String rootFolder)
	{
		this.rootFolder = rootFolder;
		hasRoot = true;
		save();
	}
	
	public String getRoot() { return rootFolder; }
	
	public boolean getHasRoot() { return hasRoot; }
	
	public void setShowPlaylist(boolean showPlaylist)
	{
		this.showPlaylist = showPlaylist;
		save();
	}
	
	private void save()
	{
		try
		{
			FileWriter writer = new FileWriter(settings);
			writer.write(rootFolder);
			writer.write("\n");
			writer.write(String.valueOf(showPlaylist));
			writer.close();
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
	}

	public boolean getShowPlaylist() {
		// TODO Auto-generated method stub
		return showPlaylist;
	}
}