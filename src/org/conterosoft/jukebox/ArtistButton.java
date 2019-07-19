package org.conterosoft.jukebox;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.Button;

public class ArtistButton extends Button 
{

	private Artist artist;

	public Artist getArtist() { return artist; }

	public ArtistButton(Artist artist)
	{
		super((artist == null) ? "" : artist.toString());

		this.artist = artist;
		this.setPrefSize(250,25);

		if (artist != null)
		{
			this.setOnAction(event -> {
				try {
					List<AlbumButton> albums = new ArrayList<AlbumButton>();

					JukeboxPi.db.getAlbums(this.artist.getArtistId()).stream().forEach(a -> albums.add(new AlbumButton(a)));

					JukeboxPi.gridpane.fill(albums,0);
					JukeboxPi.albums = albums;
					
					JukeboxPi.backButton.setOnAction(eventB -> {
						JukeboxPi.refreshArtists();
						JukeboxPi.backButton.setVisible(false);
					});
					JukeboxPi.backButton.setVisible(true);
				} catch (SQLException e) {
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