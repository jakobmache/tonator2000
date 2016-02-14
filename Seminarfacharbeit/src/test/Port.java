package test;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Port extends Circle
{
	
	private Wire connLine;
	
	public Port()
	{
		super(10, Color.ALICEBLUE);
		setStroke(Color.BLACK);
	}
	
	public void connectWire(Wire connLine)
	{
		this.connLine = connLine;
	}
	
	public void update()
	{
		if (connLine != null)
			connLine.update();
	}
}
