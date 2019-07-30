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
	enum GridState
	{
		Artist,
		Album,
		Song
	}
	
	private GridPane gridpane = new GridPane();
	private Image arrow = new Image(JukeboxPi.class.getResource("resources/arrow.png").toExternalForm()); 
	private ImageView leftArrow = new ImageView(arrow),
			rightArrow = new ImageView(arrow);
	private Region spring = new Region();
	private GridState gridstate;
	Button pageBack = new Button();
	Button pageForward = new Button();
	private JukeboxPi app;
	
	private HBox pageControls = new HBox(pageBack, spring, pageForward);
	private int rows = 12,
				cols;
	int artistPage = 0;
	
	private List<ButtonBase> buttons;
	public List<ButtonBase> getButtons() { return buttons; }
	
	public void setRows(double rows)
	{
		this.rows = (int)rows;
	}
	
	public void setCols(double cols)
	{
		this.cols = (int)cols;
	}
	
	public DisplayGrid(JukeboxPi app)
	{
		//super(5,gridpane,pageControls);
		super(5);
		this.getChildren().add(gridpane);
		this.getChildren().add(pageControls);
		
		HBox.setHgrow(spring, Priority.ALWAYS);
		this.app = app;
	}
	
	
	public void refill()
	{
		fill(buttons, 0);
	}
	
	public void fill(List<ButtonBase> buttons, int pageIndex)
	{
		this.buttons =  (List<ButtonBase>) buttons;
		int size = buttons.size();
		
		int perPage = rows * cols;
		
		int start = pageIndex * perPage;
		int end = (start + perPage < size) ? start + perPage : size ;
		
		if (buttons.get(0).getClass() != null && buttons.get(0).getClass().toString() == "org.conterosoft.jukebox.ArtistButton")
			{
					artistPage = pageIndex;
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
			this.fill(buttons,pageIndex - 1);
		});
		
		pageForward.setOnAction(event -> {
			this.fill(buttons,pageIndex + 1);
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
