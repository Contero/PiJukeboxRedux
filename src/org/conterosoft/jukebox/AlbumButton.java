package org.conterosoft.jukebox;

import javafx.scene.control.Button;
import java.util.List;
import java.util.ArrayList;

public class AlbumButton extends Button 
{
	private Album album;

	public Album getAlbum() { return album; }

	public AlbumButton(Album album)
	{
		super(album.toString());

		this.album = album;
		//this.setMinWidth(250);
		this.setPrefWidth(250);

		if (album != null)
		{

			this.setOnAction(event -> {
				List<SongButton> songs = new ArrayList<SongButton>();
				try 
				{
					JukeboxPi.db.getSongs(this.album.getAlbumId()).stream().forEach(song -> { 
						songs.add(new SongButton(song)); 
					});
					JukeboxPi.gridpane.fill(songs, 0);
					JukeboxPi.addAll.setVisible(true);
					JukeboxPi.backButton.setOnAction(eventB -> {
						JukeboxPi.gridpane.fill(JukeboxPi.albums, 0);
						JukeboxPi.addAll.setVisible(false);
						JukeboxPi.backButton.setOnAction(eventc -> {
							JukeboxPi.refreshArtists();
							JukeboxPi.backButton.setVisible(false);
						});
					});
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			});
			this.setOnTouchPressed(event -> {
				this.setStyle("-fx-background-color: lightgrey; ");
			});
			
			this.setOnTouchReleased(event -> {
				try 
				{
					Thread.sleep(500);
					this.setStyle(null);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			});
		}

	}
}