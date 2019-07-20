package org.conterosoft.jukebox;

import java.util.List;
import java.util.ArrayList;

public class AlbumButton extends ButtonBase
{
	private Album album;
	
	@Override
	public Album getObject() { return album; }

	public AlbumButton(Album album)
	{
		super(album.toString());

		this.album = album;
		this.setPrefSize(250,20);

		if (album != null)
		{
			this.setOnAction(event -> {
				List<ButtonBase> songs = new ArrayList<ButtonBase>();
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
