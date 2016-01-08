package ui.editor;

import javafx.scene.Cursor;
import javafx.scene.input.MouseButton;
import javafx.scene.shape.Circle;

public class PortCircle extends Circle
{
	private SynthesizerEditor owner;
	
	private BoundLine connectedLine;

	public PortCircle(SynthesizerEditor owner, double radius)
	{
		super(radius);
		this.owner = owner;
		initBindings();
	}
	
	private void initBindings()
	{
		setOnMousePressed((event) -> 
		{
			//Neue Linie zeichnen
			if (event.getButton() == MouseButton.PRIMARY && connectedLine == null && owner.getBoundLine() == null)
			{
				BoundLine line = new BoundLine();
				line.setStartCircle(this);
				line.setEnd(event.getSceneX(), event.getSceneY());
				
				owner.getPane().getChildren().add(line);
				owner.setBoundLine(line);
				
				connectedLine = line;
			}
			
			//Linie beenden
			else if (event.getButton() == MouseButton.PRIMARY && owner.getBoundLine() != null && connectedLine == null)
			{
				owner.getBoundLine().setEndCircle(this);
				connectedLine = owner.getBoundLine();
				
				owner.setBoundLine(null);
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
}
