package ui.editor;

import javafx.scene.Cursor;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import org.controlsfx.tools.Borders;

import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;

import resources.Strings;

public class ModuleGui
{
	private int type;


	private int xOffset = 10;
	private int yOffset = 10;
	private int radius = 10;

	private int width;
	private int height;
	
	private double dragX;
	private double dragY;

	private Pane gui;

	private PortCircle[] inputs;
	private PortCircle[] outputs;

	private SynthesizerEditor owner;

	public ModuleGui(SynthesizerEditor owner, int type)
	{
		super();
		this.owner = owner;
		this.type = type;
		gui = new Pane();

		initSize();

		drawInputs();
		drawLine();
		drawOutputs();

		gui = (Pane) Borders.wrap(gui).lineBorder().title(Strings.MODULE_NAMES[type]).radius(3).buildAll();

		initBindings();
	}

	private void initSize()
	{
		int max = Math.max(Strings.INPUT_NAMES[type].length, Strings.OUTPUT_NAMES[type].length);
		width = 2 * radius * max + (max + 1) * xOffset;
		height = 4 * radius + 4 * yOffset;
		gui.setMinWidth(width);
		gui.setMinHeight(height);
	}

	private void initBindings()
	{	
		gui.setOnMouseEntered((event) ->
		{
			if (owner.getBoundLine() == null)
				owner.setSelectedNode(gui);
		});

		gui.setOnMouseExited((event) ->
		{
			owner.setSelectedNode(null);
		});

		gui.setOnMousePressed((event) ->
		{
			if (event.getButton() == MouseButton.PRIMARY)
			{
				gui.getScene().setCursor(Cursor.CLOSED_HAND);
				dragX = gui.getLayoutX() - event.getX();
				dragY = gui.getLayoutY() - event.getY();
			}
		});

		gui.setOnMouseReleased((event) ->
		{
			gui.getScene().setCursor(Cursor.DEFAULT);
		});

		gui.setOnMouseDragged((event) -> 
		{
			if (event.getButton() == MouseButton.PRIMARY && owner.getBoundLine() == null)
			{
				double newX = event.getX() + dragX;
				if (newX > 0 && newX < gui.getScene().getWidth()) 
				{
					gui.setLayoutX(newX);
					dragX = newX;
				}  
				double newY = event.getY() + dragY;
				if (newY > 0 && newY < gui.getScene().getHeight()) 
				{
					gui.setLayoutY(newY);
					dragY = newY;
				}  
			}
		});

		gui.layoutXProperty().addListener((observable, oldValue, newValue) ->
		{
			for (PortCircle circle:inputs)
				circle.onLayoutXChanged(newValue.doubleValue());
			
			for (PortCircle circle:outputs)
				circle.onLayoutXChanged(newValue.doubleValue());
		});
		
		gui.layoutYProperty().addListener((observable, oldValue, newValue) ->
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

private void drawLine()
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
	return gui;
}
}

