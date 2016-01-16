package ui.editor;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

class BoundLine extends Line 
{
	
	private SynthesizerEditor owner;
	
	private PortCircle input;
	private PortCircle output;
	
	private Color defaultColor = Color.BLACK;
	private Color highlightColor = Color.RED;
	
	public BoundLine(SynthesizerEditor owner)
	{
		this.owner = owner;
		setStroke(defaultColor);
		setStrokeWidth(4);
		initBindings();
	}
	
	public void setStartCircle(PortCircle circle)
	{
		input = circle;
		
		Point2D point = new Point2D((float) input.getCenterX(), (float) input.getCenterY());
		setStartX(input.localToScene(point).getX());
		setStartY(input.localToScene(point).getY());
	}
	
	public void setEnd(double endX, double endY)
	{
		setEndX(endX);
		setEndY(endY);
	}
	
	public void setEndCircle(PortCircle circle)
	{
		output = circle;
		
		Point2D point = new Point2D((float) output.getCenterX(), (float) output.getCenterY());
		setEndX(output.localToScene(point).getX());
		setEndY(output.localToScene(point).getY());
	}

	public PortCircle getStartCircle()
	{
		return input;
	}
	
	public PortCircle getEndCircle()
	{
		return output;
	}
	
	public void update()
	{
		Point2D point = new Point2D((float) input.getCenterX(), (float) input.getCenterY());
		setStartX(input.localToScene(point).getX());
		setStartY(input.localToScene(point).getY());

		point = new Point2D((float) output.getCenterX(), (float) output.getCenterY());
		setEndX(output.localToScene(point).getX());
		setEndY(output.localToScene(point).getY());
	}
	
	private void initBindings()
	{
		setOnMouseEntered((event) -> 
		{
			if (owner.getBoundLine() == null)
			{
				setStroke(highlightColor);
				owner.setHighlightedLine(this);
			}
		});
		setOnMouseExited((event) ->
		{
			setStroke(defaultColor);
		});
	}
}