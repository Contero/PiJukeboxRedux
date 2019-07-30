package org.conterosoft.jukebox;

public class SongButton extends ButtonBase 
{
	private Song song;
	private JukeboxPi app;
	
	@Override
	public Song getObject() 
	{ 
		return song; 
	}
	
	public SongButton(JukeboxPi app, Song song)
	{
		super(song.toString());
		this.song = song;
		this.setPrefWidth(250);
		this.app = app;
		this.setOnAction(event -> {
			app.playListView.getItems().add(song);
			app.playlist.add(app, song);
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
