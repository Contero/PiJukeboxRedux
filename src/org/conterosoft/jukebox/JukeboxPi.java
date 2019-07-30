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

	DbController db;
	Settings settingsObj = new Settings();
	SettingsUI adminStage;
	Playlist playlist = new Playlist();
	ListView<Song> playListView = new ListView<Song>();
	Player player = new Player();
	List<ButtonBase> albums;
	DisplayGrid gridpane = new DisplayGrid(this);
	HBox lists;
	Button backButton,
					addAll;
	final double BUTTON_WIDTH = 250,
			BUTTON_HEIGHT = 31;

	public static void main(String[] args) 
	{
		launch(args);
	}

	@Override
	public void start(Stage mainStage) throws Exception {
	
		final String MENU_IMAGE = JukeboxPi.class.getResource("resources/note.png").toExternalForm(),
				FONT_FILE = JukeboxPi.class.getResource("resources/aqua.ttf").toExternalForm(),
				BACK_ARROW_IMAGE = JukeboxPi.class.getResource("resources/backArrow.png").toExternalForm(),
				PLUS_IMAGE = JukeboxPi.class.getResource("resources/plus.jpg").toExternalForm();

		//connect to database
		try 
		{
			db = new DbController(this);
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
		adminStage = new SettingsUI(this);

		ImageView note = new ImageView(new Image(MENU_IMAGE));
		note.setFitWidth(25);
		note.setPreserveRatio(true);
		note.setEffect(new DropShadow());

		Button menuBtn = new Button();
		menuBtn.setGraphic(note);
		menuBtn.getStyleClass().add("iconButton");

		menuBtn.setOnAction(event -> {
			adminStage.showAndWait();
			this.makeLists();

			refreshArtists();

			addAll.setVisible(false);
			backButton.setVisible(false);

		});

		addAll.setOnAction(event ->{
			List<ButtonBase> l = gridpane.getButtons();

			for (ButtonBase b: l)
			{
				playListView.getItems().add(b.getObject());
				playlist.add(this, b.getObject());
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

				int fill = 145;
				double screenX = mainStage.getWidth(),
						screenY = mainStage.getHeight(),
						padX,
						padY,
						rows = (screenY - fill) / BUTTON_HEIGHT,
						cols = (screenX / (BUTTON_WIDTH + 5));

				padY = (screenY - ((int)(rows) * BUTTON_HEIGHT + fill))/2; 
				padX = (screenX - (int)(cols) * BUTTON_WIDTH)/2;

				vbox.setPadding(new Insets(padY, padX, padY, padX));

				playListView.setPrefHeight(rows * BUTTON_HEIGHT);

				gridpane.setRows(rows);

				if (settingsObj.getShowPlaylist()) 
				{ cols--; }
				gridpane.setCols(cols);
				gridpane.refill();
			}
		}

		ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) ->
		{
			int fill = 145;
			double screenX = mainStage.getWidth(),
					screenY = mainStage.getHeight(),
					padX,
					padY,
					rows = (screenY - fill) / (BUTTON_HEIGHT),
					cols = (screenX / (BUTTON_WIDTH + 5));

			padY = (screenY - ((int)(rows) * BUTTON_HEIGHT + fill))/2; 
			padX = (screenX - (int)(cols) * BUTTON_WIDTH)/2;
			vbox.setPadding(new Insets(padY, padX, padY, padX));

			playListView.setPrefHeight(rows * BUTTON_HEIGHT);

			gridpane.setRows(rows);

			if (settingsObj.getShowPlaylist()) 
			{ cols--; }
			gridpane.setCols(cols);
			gridpane.refill();
		};

		mainStage.widthProperty().addListener(stageSizeListener);
		mainStage.heightProperty().addListener(stageSizeListener); 
		adminStage.addEventHandler(WindowEvent.WINDOW_HIDDEN, new resize());

		scene.getStylesheets().add(JukeboxPi.class.getResource("resources/jukebox.css").toExternalForm()); 
		mainStage.setMinHeight(200);
		mainStage.setMinWidth(400);
		mainStage.setFullScreen(true);
		mainStage.show();
	}
	public void refreshArtists()
	{
		try
		{			
			List<ButtonBase> artistButtonList = new ArrayList<ButtonBase>();

			db.getArtists().stream().forEach(a -> {
				artistButtonList.add(new ArtistButton(this, a));
			});
			gridpane.fill(artistButtonList,0);

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void refreshArtists(int page)
	{
		try
		{			
			List<ButtonBase> artistButtonList = new ArrayList<ButtonBase>();

			this.db.getArtists().stream().forEach(a -> {
				artistButtonList.add(new ArtistButton(this, a));
			});
			gridpane.fill(artistButtonList,page);

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void makeLists()
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
