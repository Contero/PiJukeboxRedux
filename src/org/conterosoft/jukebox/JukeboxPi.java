package org.conterosoft.jukebox;

import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.effect.DropShadow;


public class JukeboxPi extends Application 
{

	static DbController db;
	static Settings settingsObj = new Settings();
	static SettingsUI adminStage;
	static Playlist playlist = new Playlist();
	static ListView<Song> playListView = new ListView<Song>();
	static Player player = new Player();
	static List<AlbumButton> albums;
	static DisplayGrid gridpane = new DisplayGrid();
	static HBox lists;
	static Button backButton,
					addAll;

	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		launch(args);
	}

	@Override
	public void start(Stage mainStage) throws Exception {
		// TODO Auto-generated method stub
		final String MENU_IMAGE = "file:stuff/note.png",
				FONT_FILE = "file:stuff.aqua.ttf",
				BACK_ARROW_IMAGE = "file:stuff/backArrow.png",
				PLUS_IMAGE = "file:stuff/plus.jpg";

		//connect to database
		try 
		{
			db = new DbController();
		}
		catch (Exception e)
		{
			Popup error = new Popup();
			error.getContent().add(new Label(e.getMessage()));
			error.show(mainStage);
		}

		Font.loadFont(FONT_FILE, 10);

		//build header
		ImageView backArrow = new ImageView(new Image(BACK_ARROW_IMAGE));
		backArrow.setFitWidth(15);
		backArrow.setPreserveRatio(true);
		backButton = new Button("Back");
		backButton.setGraphic(backArrow);

		ImageView plusIcon = new ImageView(new Image(PLUS_IMAGE));
		plusIcon.setFitWidth(15);
		plusIcon.setPreserveRatio(true);	

		addAll = new Button("Add All");
		addAll.setGraphic(plusIcon);

		Region filler = new Region();
		adminStage = new SettingsUI();

		ImageView note = new ImageView(new Image(MENU_IMAGE));
		note.setFitWidth(25);
		note.setPreserveRatio(true);
		note.setEffect(new DropShadow());

		Button menuBtn = new Button();
		menuBtn.setGraphic(note);
		menuBtn.getStyleClass().add("iconButton");

		menuBtn.setOnAction(event -> {
			adminStage.showAndWait();
			JukeboxPi.makeLists();

			refreshArtists();

			addAll.setVisible(false);
			backButton.setVisible(false);

		});

		addAll.setOnAction(event ->{
			List<?> l = gridpane.getButtons();

			for (SongButton b: (List<SongButton>)l)
			{
				JukeboxPi.playListView.getItems().add(b.getSong());
				JukeboxPi.playlist.add(b.getSong());
			}
		});

		HBox header = new HBox(10, backButton, addAll, filler, menuBtn);
		HBox.setHgrow(filler, Priority.ALWAYS);

		//initialize data
		refreshArtists();
		addAll.setVisible(false);
		backButton.setVisible(false);

		if (settingsObj.getShowPlaylist())
		{	
			lists = new HBox(10, gridpane, playListView);
		}
		else
		{
			lists = new HBox(10, gridpane);	
		}

		VBox vbox = new VBox(10,header, lists);
		Scene scene = new Scene(vbox);
		mainStage.setScene(scene);

		class resize implements EventHandler<WindowEvent>
		{
			@Override
			public void handle(WindowEvent arg0) 
			{

				int fill = 125;
				double screenX = mainStage.getWidth(),
						screenY = mainStage.getHeight(),
						padX,
						padY,
						rows = (screenY - fill) / 25.6,
						cols = (screenX / 255);

				padY = (screenY - ((int)(rows) * 25.6 + fill))/2; 
				padX = (screenX - (int)(cols) * 250)/2;

				vbox.setPadding(new Insets(padY, padX, padY, padX));

				playListView.setPrefHeight(rows * 25.6);

				gridpane.setRows(rows);

				if (settingsObj.getShowPlaylist()) 
				{ cols = cols - 1; }
				gridpane.setCols(cols);
				gridpane.refill();
			}
		}

		ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) ->
		{
			int fill = 125;
			double screenX = mainStage.getWidth(),
					screenY = mainStage.getHeight(),
					padX,
					padY,
					rows = (screenY - fill) / 25.6,
					cols = (screenX / 255);

			padY = (screenY - ((int)(rows) * 25.6 + fill))/2; 
			padX = (screenX - (int)(cols) * 250)/2;
			vbox.setPadding(new Insets(padY, padX, padY, padX));

			playListView.setPrefHeight(rows * 25.6);

			gridpane.setRows(rows);

			if (settingsObj.getShowPlaylist()) 
			{ cols = cols - 1; }
			gridpane.setCols(cols);
			gridpane.refill();

		};

		mainStage.widthProperty().addListener(stageSizeListener);
		mainStage.heightProperty().addListener(stageSizeListener); 
		adminStage.addEventHandler(WindowEvent.WINDOW_HIDDEN, new resize());

		scene.getStylesheets().add("file:stuff/jukebox.css"); 
		mainStage.setMinHeight(200);
		mainStage.setMinWidth(400);
		mainStage.setFullScreen(true);
		mainStage.show();
	}
	public static void refreshArtists()
	{
		try
		{			
			List<ArtistButton> artistButtonList = new ArrayList<ArtistButton>();

			db.getArtists().stream().forEach(a -> {
				artistButtonList.add(new ArtistButton(a));
			});
			gridpane.fill(artistButtonList,0);

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void makeLists()
	{
		if (settingsObj.getShowPlaylist() && !lists.getChildren().contains(playListView))
		{	
			lists.getChildren().add(playListView);
		}
		else if (!settingsObj.getShowPlaylist() && lists.getChildren().contains(playListView))
		{
			lists.getChildren().remove(playListView);	
		}
	}
}
