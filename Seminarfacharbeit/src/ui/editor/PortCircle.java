package ui.editor;

import javafx.scene.Cursor;
import javafx.scene.input.MouseButton;
import javafx.scene.shape.Circle;

public class PortCircle extends Circle
{
	private SynthesizerEditor editor;
	private ModuleGuiBackend owner;
	
	private BoundLine connectedLine;
	
	private int index;
	private PortCircleType type;

	public PortCircle(SynthesizerEditor editor, ModuleGuiBackend owner, double radius, int index, PortCircleType type)
	{
		super(radius);
		this.editor = editor;
		this.owner = owner;
		this.type = type;
		initBindings();
	}
	
	private void initBindings()
	{
		setOnMousePressed((event) -> 
		{
			//Neue Linie zeichnen
			if (event.getButton() == MouseButton.PRIMARY && connectedLine == null && editor.getBoundLine() == null)
			{
				BoundLine line = new BoundLine(editor);
				line.setStartCircle(this);
				line.setEnd(event.getSceneX(), event.getSceneY());
				
				editor.getPane().getChildren().add(line);
				editor.setBoundLine(line);
				
				connectedLine = line;
			}
			
			//Linie beenden
			else if (event.getButton() == MouseButton.PRIMARY && editor.getBoundLine() != null && connectedLine == null)
			{
				editor.getBoundLine().setEndCircle(this);
				connectedLine = editor.getBoundLine();
				
				editor.setBoundLine(null);
			}
		});
		
		setOnMouseEntered((event) ->
		{
			if (connectedLine == null)
				getScene().setCursor(Cursor.HAND);
		});
		
		setOnMouseExited((event) ->
		{
			getScene().setCursor(Cursor.DEFAULT);
		});
	};
	
	public void onLayoutXChanged(double newX)
	{
		if (connectedLine != null)
			connectedLine.update();
	}
	
	public void onLayoutYChanged(double newY)
	{
		if (connectedLine != null)
			connectedLine.update();
	}
	
	public void deleteConnectedLine()
	{
		connectedLine = null;
	}
	
	public BoundLine getBoundLine()
	{
		return connectedLine;
	}
	
	public ModuleGuiBackend getOwner()
	{
		return owner;
	}
	
	public int getIndex()
	{
		return index;
	}
	
	public PortCircleType getType()
	{
		return type;
	}
}
