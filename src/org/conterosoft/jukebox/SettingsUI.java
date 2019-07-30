package org.conterosoft.jukebox;

import java.io.File;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SettingsUI extends Stage
{
	boolean dirty = false;
	private Label message = new Label();
	@SuppressWarnings("unused")
	private JukeboxPi app;
	
	public void setMessage(int artists, int albums, int songs) {
		Platform.runLater(new Runnable() {
			@Override
			public void run()
			{
				message.setText("Scanning...\nArtists: " + artists +
						"\nAlbums: " + albums + 
						"\nSongs: " + songs);
			}
		});
	}
	
	
	public SettingsUI(JukeboxPi app)
	{ 		
		Label rootLabel = new Label("Music root folder:");
		this.app = app;
		TextField root = new TextField(app.settingsObj.getRoot());
		Button browse = new Button("Browse"),
				close = new Button("Close Settings"),
				exit = new Button("Exit Program"),
				powerOff = new Button("Power off Pi");
		HBox rootHbox = new HBox(10,rootLabel,root,browse);
		CheckBox showPlaylist = new CheckBox("Show Playlist");
		showPlaylist.setSelected(app.settingsObj.getShowPlaylist());

		showPlaylist.setOnAction(event -> {
			app.settingsObj.setShowPlaylist(showPlaylist.isSelected());
		});

		root.textProperty().addListener((obs,old,newt) ->{ dirty = true; });

		DirectoryChooser folderBrowser = new DirectoryChooser();
		folderBrowser.setTitle("Root Music Directory");
		browse.setOnAction(event -> {

			File rootFolder = folderBrowser.showDialog(null);
			if(rootFolder != null)
			{
				root.setText(rootFolder.toString());
			};
		});

		exit.setOnAction(event -> {
			this.close();
			Platform.exit();
		});
		
		powerOff.setOnAction(event -> {
			try
			{
					if (System.getProperty("os.name").equals("Linux"))
					{
						Runtime.getRuntime().exec("shutdown -h now");
						this.close();
						Platform.exit();
					}
					else
					{
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setContentText("This ain't Linux, bro");
						alert.showAndWait();
					}
			}
			catch (Exception e)
			{
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setContentText("Something went horribly awry\n" + e.getMessage());
				alert.showAndWait();
			}
		});
		
		Button scan = new Button("Scan Music");
		scan.setOnAction(event -> { 
			if (dirty)
			{
				app.settingsObj.setRoot(root.getText());
				dirty = false;
			}

			if (!app.settingsObj.getHasRoot())
			{
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setContentText("Need root directory, bro");
				alert.showAndWait();
			}
			else
			{		
				Thread scanThread = new Thread(() -> {
					
						
						try 
						{
							app.db.libraryScan();
						} 
						catch (Exception e) 
						{
							e.printStackTrace();
						}
						Platform.runLater(() -> {
						message.setText("Complete!\nArtists: " + app.db.getArtistCount() +
								"\nAlbums: " + app.db.getAlbumCount() + 
								"\nSongs: " + app.db.getSongCount());
						});
				});
						
				scanThread.start();
				
			}
		});


		VBox vbox = new VBox(10,rootHbox,showPlaylist, scan, message, exit, powerOff, close);

		message.setMinHeight(65);

		close.setOnAction(event -> {
			this.close();
		});

		Scene scene = new Scene(vbox);
		scene.getStylesheets().add(JukeboxPi.class.getResource("resources/jukebox.css").toExternalForm());
		vbox.getStyleClass().add("settingsLayout");
		
		vbox.setPadding(new Insets(10));

		this.setScene(scene);
		this.initModality(Modality.APPLICATION_MODAL);
		this.setOnCloseRequest(event -> {
			if (dirty) { app.settingsObj.setRoot(root.getText()); }
		});
		this.initStyle(StageStyle.UNDECORATED);
		
	}
}
