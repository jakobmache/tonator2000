package ui.editor;

import javafx.geometry.Point2D;
import javafx.scene.shape.Line;

class BoundLine extends Line 
{
	private PortCircle input;
	private PortCircle output;
	
	public BoundLine()
	{
		setStrokeWidth(2);
	}
	
	public void setStart(PortCircle circle)
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
	
	public void setEnd(PortCircle circle)
	{
		output = circle;
		
		Point2D point = new Point2D((float) output.getCenterX(), (float) output.getCenterY());
		setEndX(output.localToScene(point).getX());
		setEndY(output.localToScene(point).getY());
	}
	
	public void setEndCircle(PortCircle circle)
	{
		output = circle;
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

}