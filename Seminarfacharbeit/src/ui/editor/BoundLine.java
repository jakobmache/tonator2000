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
		
		setStartX(input.getParent().getParent().getParent().getParent().localToParent(
				input.getParent().getParent().getParent().localToParent(
				input.getParent().getParent().localToParent(
				input.getParent().localToParent(
				input.localToParent(point))))).getX());
		setStartY(input.getParent().getParent().getParent().getParent().localToParent(
				input.getParent().getParent().getParent().localToParent(
				input.getParent().getParent().localToParent(
				input.getParent().localToParent(
				input.localToParent(point))))).getY());
		
		input.setConnectedLine(this);
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
		
		setEndX(output.getParent().getParent().getParent().getParent().localToParent(
				output.getParent().getParent().getParent().localToParent(
				output.getParent().getParent().localToParent(
				output.getParent().localToParent(
				output.localToParent(point))))).getX());
		setEndY(output.getParent().getParent().getParent().getParent().localToParent(
				output.getParent().getParent().getParent().localToParent(
				output.getParent().getParent().localToParent(
				output.getParent().localToParent(
				output.localToParent(point))))).getY());
		
		output.setConnectedLine(this);
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
		
		setStartX(input.getParent().getParent().getParent().getParent().localToParent(
				input.getParent().getParent().getParent().localToParent(
				input.getParent().getParent().localToParent(
				input.getParent().localToParent(
				input.localToParent(point))))).getX());
		setStartY(input.getParent().getParent().getParent().getParent().localToParent(
				input.getParent().getParent().getParent().localToParent(
				input.getParent().getParent().localToParent(
				input.getParent().localToParent(
				input.localToParent(point))))).getY());

		point = new Point2D((float) output.getCenterX(), (float) output.getCenterY());
		
		setEndX(output.getParent().getParent().getParent().getParent().localToParent(
				output.getParent().getParent().getParent().localToParent(
				output.getParent().getParent().localToParent(
				output.getParent().localToParent(
				output.localToParent(point))))).getX());
		setEndY(output.getParent().getParent().getParent().getParent().localToParent(
				output.getParent().getParent().getParent().localToParent(
				output.getParent().getParent().localToParent(
				output.getParent().localToParent(
				output.localToParent(point))))).getY());
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