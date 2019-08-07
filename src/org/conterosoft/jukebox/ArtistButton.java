package org.conterosoft.jukebox;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class ArtistButton extends ButtonBase 
{

	private Artist artist;
	@SuppressWarnings("unused")
	private JukeboxPi app;
	
	@Override
	public Artist getObject() { return artist; }

	public ArtistButton(JukeboxPi app, Artist artist)
	{
		super((artist == null) ? "" : artist.toString());

		this.artist = artist;
		this.setPrefSize(250,25);
		this.app = app;

		if (artist != null)
		{
			this.setOnAction(event -> {
				try {
					List<ButtonBase> albums = new ArrayList<ButtonBase>();
					
					app.db.getAlbums(this.artist.getArtistId()).stream().forEach(a -> albums.add(new AlbumButton(app, a)));
					
					app.gridpane.fill(albums, ButtonType.ALBUM, 0);
					app.albums = albums;
					
					app.backButton.setOnAction(eventB -> {
						app.refreshArtists(false);
						app.backButton.setVisible(false);
					});
					app.backButton.setVisible(true);
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