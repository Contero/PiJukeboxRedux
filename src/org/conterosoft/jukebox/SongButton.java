package org.conterosoft.jukebox;

public class SongButton extends ButtonBase 
{
	private Song song;
	
	@Override
	public Song getObject() 
	{ 
		return song; 
	}
	
	public SongButton(Song song)
	{
		super(song.toString());
		this.song = song;
		this.setPrefWidth(250);
		this.setOnAction(event -> {
			JukeboxPi.playListView.getItems().add(song);
			JukeboxPi.playlist.add(song);
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
