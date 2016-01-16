package ui.editor;

import javafx.scene.Cursor;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import modules.Ids;
import modules.ModuleType;

import org.controlsfx.tools.Borders;

import resources.Strings;

public class ModuleGuiBackend
{
	private ModuleType type;

	protected int xOffset = 10;
	protected int yOffset = 10;
	protected int radius = 10;
	
	private int id;

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
	
	private boolean destroyable = true;

	/**
	 * Creates a new ModuleGuiBackend which contains data related to this module.
	 * 
	 * @param owner SynthesizerEditor in which the module is created
	 * @param type Type of module
	 * @param name Name of module
	 */
	public ModuleGuiBackend(SynthesizerEditor owner, ModuleType type, String name)
	{
		super();
		this.owner = owner;
		this.type = type;
		this.name = name;
		gui = new Pane();
		
		id = Ids.getNextId();

		initSize();
		initMenu();

		drawInputs();
		drawLine();
		drawOutputs();
	}
	
	/**
	 * Creates a new ModuleGuiBackend which contains data related to this module with custom height.
	 * 
	 * @param owner SynthesizerEditor in which the module is created
	 * @param type Type of module
	 * @param name Name of module
	 * @param height Height of the GUI in pixels
	 */
	public ModuleGuiBackend(SynthesizerEditor owner, ModuleType type, String name, int height)
	{
		super();
		
		this.height = height;
		
		id = Ids.getNextId();

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

	/**
	 * Calculates the width and height of a module.
	 */
	private void initSize()
	{
		int max = Math.max(Strings.INPUT_NAMES[type.getIndex()].length, Strings.OUTPUT_NAMES[type.getIndex()].length);
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
		MenuItem deleteMenuItem = new MenuItem("Modul hinzuf�gen");
		menu.getItems().add(deleteMenuItem);
	}

	private void initBindings()
	{	
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
			if (event.getButton() == MouseButton.PRIMARY && owner.getBoundLine() == null)
			{
				returnPane.getScene().setCursor(Cursor.CLOSED_HAND);
				oldX = returnPane.getLayoutX() - event.getSceneX();
				oldY = returnPane.getLayoutY() - event.getSceneY();
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
				double newX = event.getSceneX() + oldX;
				if (newX > 0 && newX < returnPane.getScene().getWidth()) 
				{
					returnPane.setLayoutX(newX);
				}  
				double newY = event.getSceneY() + oldY;
				if (newY > 0 && newY < returnPane.getScene().getHeight()) 
				{
					returnPane.setLayoutY(newY);
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
		inputs = new PortCircle[Strings.INPUT_NAMES[type.getIndex()].length];
		for (int i = 0; i < Strings.INPUT_NAMES[type.getIndex()].length; i++)
		{
			PortCircle circle = new PortCircle(owner, this, radius, i, PortCircleType.INPUT);
			circle.setStroke(Color.BLACK);
			circle.setStrokeWidth(1);
			circle.setFill(Color.ALICEBLUE);

			inputs[i] = circle;

			double x = (i + 1) * (width / (Strings.INPUT_NAMES[type.getIndex()].length + 1));
			double y = height / 4;

			circle.setCenterX(x);
			circle.setCenterY(y);
			Tooltip tooltip = new Tooltip(Strings.INPUT_NAMES[type.getIndex()][i]);
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
		outputs = new PortCircle[Strings.OUTPUT_NAMES[type.getIndex()].length];
		for (int i = 0; i < Strings.OUTPUT_NAMES[type.getIndex()].length; i++)
		{
			PortCircle circle = new PortCircle(owner, this, radius, i, PortCircleType.OUTPUT);
			circle.setStroke(Color.BLACK);
			circle.setStrokeWidth(1);
			circle.setFill(Color.ALICEBLUE);

			outputs[i] = circle;

			double x = (i + 1) * (width / (Strings.OUTPUT_NAMES[type.getIndex()].length + 1));
			double y = 0.75 * height;

			circle.setCenterX(x);
			circle.setCenterY(y);
			Tooltip tooltip = new Tooltip(Strings.OUTPUT_NAMES[type.getIndex()][i]);
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
	
	public PortCircle[] getInputs()
	{
		return inputs;
	}
	
	public PortCircle[] getOutputs()
	{
		return outputs;
	}
	
	public boolean isDestroyable()
	{
		return destroyable;
	}
	
	public void setDestroyable(boolean newValue)
	{
		destroyable = newValue;
	}
	
	public ModuleType getModuleType()
	{
		return type;
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getId()
	{
		return id;
	}
}