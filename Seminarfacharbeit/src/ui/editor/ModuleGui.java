package ui.editor;

import javafx.scene.Cursor;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import org.controlsfx.tools.Borders;

import resources.Strings;

public class ModuleGui
{
	private int type;


	protected int xOffset = 10;
	protected int yOffset = 10;
	protected int radius = 10;

	protected int width;
	protected int height;

	private double oldX;
	private double oldY;

	protected Pane gui;
	protected Pane returnPane;
	
	private String name;

	private PortCircle[] inputs;
	private PortCircle[] outputs;

	private SynthesizerEditor owner;
	
	private ContextMenu menu;

	public ModuleGui(SynthesizerEditor owner, int type, String name)
	{
		super();
		this.owner = owner;
		this.type = type;
		this.name = name;
		gui = new Pane();

		initSize();
		initMenu();

		drawInputs();
		drawLine();
		drawOutputs();
	}
	
	public ModuleGui(SynthesizerEditor owner, int type, String name, int height)
	{
		super();
		
		this.height = height;

		this.owner = owner;
		this.type = type;
		this.name = name;
		gui = new Pane();

		initSize();
		initMenu();
		
		drawInputs();
		drawLine();
		drawOutputs();
	}

	private void initSize()
	{
		int max = Math.max(Strings.INPUT_NAMES[type].length, Strings.OUTPUT_NAMES[type].length);
		if (width == 0)
			width = 2 * radius * max + (max + 1) * xOffset;
		if (height == 0)
			height = 4 * radius + 4 * yOffset;
		gui.setMinWidth(width);
		gui.setMinHeight(height);
	}
	
	private void initMenu()
	{
		menu = new ContextMenu();
		MenuItem deleteMenuItem = new MenuItem("Modul hinzufügen");
		menu.getItems().add(deleteMenuItem);
	}

	private void initBindings()
	{	
		returnPane.setOnMouseClicked((event) ->
		{
			//Rechtsklick -> Menü
			if (event.getClickCount() == 1 && event.getButton() == MouseButton.SECONDARY)
			{
				menu.show(returnPane, event.getScreenX(), event.getScreenY());
			}
		});
		
		returnPane.setOnMouseEntered((event) ->
		{
			if (owner.getBoundLine() == null)
				owner.setSelectedNode(returnPane);
		});

		returnPane.setOnMouseExited((event) ->
		{
			owner.setSelectedNode(null);
		});

		returnPane.setOnMousePressed((event) ->
		{
			if (event.getButton() == MouseButton.PRIMARY)
			{
				returnPane.getScene().setCursor(Cursor.CLOSED_HAND);
				oldX = returnPane.getLayoutX();
				oldY = returnPane.getLayoutY();
			}
		});

		returnPane.setOnMouseReleased((event) ->
		{
			returnPane.getScene().setCursor(Cursor.DEFAULT);
		});

		returnPane.setOnMouseDragged((event) -> 
		{
			if (event.getButton() == MouseButton.PRIMARY && owner.getBoundLine() == null)
			{
				double newX = event.getX() + oldX;
				if (newX > 0 && newX < returnPane.getScene().getWidth()) 
				{
					returnPane.setLayoutX(newX);
					oldX = newX;
				}  
				double newY = event.getY() + oldY;
				if (newY > 0 && newY < returnPane.getScene().getHeight()) 
				{
					returnPane.setLayoutY(newY);
					oldY = newY;
				}  
			}
		});

		returnPane.layoutXProperty().addListener((observable, oldValue, newValue) ->
		{
			for (PortCircle circle:inputs)
				circle.onLayoutXChanged(newValue.doubleValue());

			for (PortCircle circle:outputs)
				circle.onLayoutXChanged(newValue.doubleValue());
		});

		returnPane.layoutYProperty().addListener((observable, oldValue, newValue) ->
		{
			for (PortCircle circle:inputs)
				circle.onLayoutYChanged(newValue.doubleValue());

			for (PortCircle circle:outputs)
				circle.onLayoutXChanged(newValue.doubleValue());
		});	
	}

	private void drawInputs()
	{
		inputs = new PortCircle[Strings.INPUT_NAMES[type].length];
		for (int i = 0; i < Strings.INPUT_NAMES[type].length; i++)
		{
			PortCircle circle = new PortCircle(owner, radius);
			circle.setStroke(Color.BLACK);
			circle.setStrokeWidth(1);
			circle.setFill(Color.ALICEBLUE);

			inputs[i] = circle;

			double x = (i + 1) * (width / (Strings.INPUT_NAMES[type].length + 1));
			double y = height / 4;

			circle.setCenterX(x);
			circle.setCenterY(y);
			Tooltip tooltip = new Tooltip(Strings.INPUT_NAMES[type][i]);
			Tooltip.install(circle, tooltip);

			gui.getChildren().add(circle);
		}
	}

	protected void drawLine()
	{
		Line line = new Line();
		line.setStartX(2);
		line.setStartY(height / 2);
		line.setEndX(width + 2);
		line.setEndY(height / 2);
		line.relocate(0, height / 2);
		gui.getChildren().add(line);
	}

	private void drawOutputs()
	{
		outputs = new PortCircle[Strings.OUTPUT_NAMES[type].length];
		for (int i = 0; i < Strings.OUTPUT_NAMES[type].length; i++)
		{
			PortCircle circle = new PortCircle(owner, radius);
			circle.setStroke(Color.BLACK);
			circle.setStrokeWidth(1);
			circle.setFill(Color.ALICEBLUE);

			outputs[i] = circle;

			double x = (i + 1) * (width / (Strings.OUTPUT_NAMES[type].length + 1));
			double y = 0.75 * height;

			circle.setCenterX(x);
			circle.setCenterY(y);
			Tooltip tooltip = new Tooltip(Strings.OUTPUT_NAMES[type][i]);
			Tooltip.install(circle, tooltip);

			gui.getChildren().add(circle);
		}
	}

	public Pane getGui()
	{
		returnPane = (Pane) Borders.wrap(gui).lineBorder().title(name).radius(3).buildAll();
		initBindings();
		return returnPane;
	}
}
