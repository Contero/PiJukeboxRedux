package org.conterosoft.jukebox;

import java.util.List;
import java.util.ArrayList;

public class AlbumButton extends ButtonBase
{
	private Album album;
	@SuppressWarnings("unused")
	private JukeboxPi app;
	
	@SuppressWarnings("unchecked")
	@Override
	public Album getObject() { return album; }

	public AlbumButton(JukeboxPi app, Album album)
	{
		super(album.toString());

		this.album = album;
		this.setPrefSize(250,20);
		this.app = app;

		if (album != null)
		{
			this.setOnAction(event -> {
				List<ButtonBase> songs = new ArrayList<ButtonBase>();
				try 
				{
					app.db.getSongs(this.album.getAlbumId()).stream().forEach(song -> { 
						songs.add(new SongButton(app, song)); 
					});
					app.gridpane.fill(songs, 0);
					app.addAll.setVisible(true);
					app.backButton.setOnAction(eventB -> {
						app.gridpane.fill(app.albums, 0);
						app.addAll.setVisible(false);
						app.backButton.setOnAction(eventc -> {
							app.refreshArtists();
							app.backButton.setVisible(false);
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
