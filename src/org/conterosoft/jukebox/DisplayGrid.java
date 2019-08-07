package org.conterosoft.jukebox;

import java.util.List;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class DisplayGrid extends VBox
{
	
	private GridPane gridpane = new GridPane();
	private Image arrow = new Image(JukeboxPi.class.getResource("resources/arrow.png").toExternalForm()); 
	private ImageView leftArrow = new ImageView(arrow),
			rightArrow = new ImageView(arrow);
	private Region spring = new Region();
	Button pageBack = new Button();
	Button pageForward = new Button();
	private JukeboxPi app;
	
	private HBox pageControls = new HBox(pageBack, spring, pageForward);
	private int rows = 12,
				cols,
				artistPage = 0,
				albumPage = 0;
	
	private List<ButtonBase> buttons;
	private ButtonType buttonType;
	/*
	 * returns buttons containing objects (artist, album, song)
	 */
	public List<ButtonBase> getButtons() { return buttons; }
	
	/*
	 * sets rows and columns for grid
	 */
	public void setRows(double rows)
	{
		this.rows = (int)rows;
	}
	
	public void setCols(double cols)
	{
		this.cols = (int)cols;
	}
	
	/*
	 * Clears page numbers, for resizing so we don;t go to a non existent page
	 */
	public void clearPages()
	{
		artistPage = 0;
		albumPage = 0;
	}
	
	/*
	 * Accepts: reference to app
	 * Constructor for Grid component
	 * adds grid and page controls
	 */
	public DisplayGrid(JukeboxPi app)
	{
		super(5);
		this.getChildren().add(gridpane);
		this.getChildren().add(pageControls);
		
		HBox.setHgrow(spring, Priority.ALWAYS);
		this.app = app;
	}
	
	/*
	 * Refills grid with current list - for size changes
	 */
	public void refill()
	{
		fill(buttons, buttonType, 0);
	}
	
	/*
	 * Accepts list of buttons (artist, album, song)
	 * and the type of button
	 * calls other fill method and provides last page visited
	 */
	public void fill(List<ButtonBase> buttons, ButtonType buttonType)
	{
		int page;
		
		if (buttonType == ButtonType.ARTIST)
		{
			page = artistPage;
		}
		else if (buttonType == ButtonType.ALBUM)
		{
			page = albumPage;
		}
		else
		{
			page = 0; //shouldn't get here, but just in case
		}
		
		fill(buttons, buttonType, page);
	}
	
	/*
	 * Accepts list of buttons (artist, album, or song),
	 * the type of button in the list,
	 * and a specific page number to display in the grid
	 * Displays the buttons in the grid on the page specified
	 */
	public void fill(List<ButtonBase> buttons, ButtonType buttonType, int pageIndex)
	{
		this.buttons =  (List<ButtonBase>) buttons;
		this.buttonType = buttonType;
		int size = buttons.size();
		
		int perPage = rows * cols;
		
		int start = pageIndex * perPage;
		int end = (start + perPage < size) ? start + perPage : size ;
		
		//set state of grid, i.e. type of buttons
		if (buttonType == ButtonType.ARTIST)
		{
			artistPage = pageIndex;
		}
		else if (buttonType == ButtonType.ALBUM)
		{
			albumPage = pageIndex;
		}
	
		gridpane.getChildren().clear();
		
		int buttonIter = start;

		for (int col = 0; col < cols; col++)
		{
			for (int row = 0; row < rows; row++)
			{
				if (buttonIter < size) 
				{
					gridpane.add((Node) buttons.get(buttonIter), col, row);
				}
				else
				{
					//add empty buttons to fill space of partial page
					gridpane.add(new ArtistButton(app, null), col, row);
				}
				buttonIter++;
			}	
		}
		
		//buttons
		leftArrow.setRotate(-90);
		rightArrow.setRotate(90);
		
		leftArrow.setFitWidth(25);
		leftArrow.setPreserveRatio(true);
		
		rightArrow.setFitWidth(25);
		rightArrow.setPreserveRatio(true);
				
		pageBack.setGraphic(leftArrow);
		pageForward.setGraphic(rightArrow);
		
		pageBack.getStyleClass().add("iconButton");
		pageForward.getStyleClass().add("iconButton");
		
		pageBack.setOnAction(event -> {
			this.fill(buttons, this.buttonType, pageIndex - 1);
		});
		
		pageForward.setOnAction(event -> {
			this.fill(buttons, this.buttonType,pageIndex + 1);
		});
		
		if (pageIndex > 0)
		{
			pageBack.setDisable(false);
		}
		else
		{
			pageBack.setDisable(true);
		}
		
		if (size > end)
		{
			pageForward.setDisable(false);
		}
		else
		{
			pageForward.setDisable(true);
		}
	}
}
