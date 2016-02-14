package test;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class Wire extends Line
{

	private Port input;
	private Port output;
	
	public Wire()
	{
		super();
		setStroke(Color.GREEN);
		setStrokeWidth(3);
	}

	public void setInput(Port input)
	{
		this.input = input;
		input.connectWire(this);
		update();
	}

	public void setOutput(Port output)
	{
		this.output = output;
		output.connectWire(this);
		update();
	}

	public void update()
	{
		if (input != null)
		{
			Point2D centerInput = input.localToScene(input.getCenterX(), input.getCenterY());
			System.out.println(centerInput);
			setStartX(centerInput.getX());
			setStartY(centerInput.getY());
		}

		if (output != null)
		{
			Point2D centerOutput = output.localToScene(output.getCenterX(), output.getCenterY());
			setEndX(centerOutput.getX());
			setEndY(centerOutput.getY());
		}
	}

}
