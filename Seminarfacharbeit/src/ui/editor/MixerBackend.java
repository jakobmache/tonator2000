package ui.editor;

import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import modules.ModuleGenerator;
import modules.ModuleType;
import resources.Strings;

public class MixerBackend extends ModuleGuiBackend
{
	
	private int numInputs;

	public MixerBackend(SynthesizerEditor owner, ModuleType type, String name, int id, int numInputs) 
	{
		super(owner, type, name);
		
		this.numInputs = numInputs;
		
		module = ModuleGenerator.createMixer(owner.getEngine(), name, id, numInputs);
		gui.getChildren().clear();
		setId(id);
		
		initSize();
		
		drawInputs();
		drawLine();
		drawOutputs();
	}
	
	@Override
	protected void initSize()
	{
		int max = numInputs;
		width = 2 * radius * max + (max + 1) * xOffset;
		height = 4 * radius + 4 * yOffset;
		gui.setMinWidth(width);
		gui.setMinHeight(height);
	}
	
	protected void drawInputs()
	{
		inputs = new PortCircle[numInputs];
		for (int i = 0; i < numInputs; i++)
		{
			PortCircle circle = new PortCircle(owner, this, radius, i, PortCircleType.INPUT);
			circle.setStroke(Color.BLACK);
			circle.setStrokeWidth(1);
			circle.setFill(Color.ALICEBLUE);

			inputs[i] = circle;

			double x = (i + 1) * (width / (numInputs + 1));
			double y = height / 4;

			circle.setCenterX(x);
			circle.setCenterY(y);
			Tooltip tooltip = new Tooltip(Strings.INPUT_NAMES[ModuleType.MIXER.getIndex()][0] + " " + Integer.toString(i + 1));
			Tooltip.install(circle, tooltip);

			gui.getChildren().add(circle);
		}
	}
}
