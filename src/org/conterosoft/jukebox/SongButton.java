package org.conterosoft.jukebox;

public class SongButton extends ButtonBase 
{
	private Song song;

	/*
	 * returns song object
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Song getObject() 
	{ 
		return song; 
	}
	
	/*
	 * Constructor, sets song in button and event handlers
	 */
	public SongButton(JukeboxPi app, Song song)
	{
		super(song.toString());
		this.song = song;
		this.setPrefWidth(250);
		this.setOnAction(event -> {
			app.playListView.getItems().add(song);
			app.playlist.add(app, song);
		});
		
		//clicked styling events don't work on touch screen
		//adding events to change color while pressed
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
