package ui;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import modules.listener.EngineListener;

import org.controlsfx.control.StatusBar;

import resources.Strings;
import engine.SynthesizerEngine;

public class SynthiStatusBar extends StatusBar implements EngineListener
{
	
	public static final int SAMPLINGRATE = 0;
	public static final int LATENCY = 1;
	public static final int ENGINE_STATUS = 2;
	public static final int MAX_POLYPHONY = 3;
	public static final int MIDI_DEVICE = 4;
	public static final int CURR_INSTRUMENT = 5;

	private Button sampleRateLabel;
	private Button latencyLabel;
	private Button midiDeviceLabel;
	private Button currProgramLabel;
	private Button engineRunningLabel;
	private Button maxPolyphonyLabel;

	private SynthesizerEngine engine;

	private MainApplication parent;

	public SynthiStatusBar(SynthesizerEngine engine, MainApplication parent)
	{
		super();
		this.engine = engine;
		this.parent = parent;
		engine.addListener(this);
		initElements();
		update();
	}

	public void update()
	{
		sampleRateLabel.setText(Float.toString(engine.getSamplingRate()));
		latencyLabel.setText(Double.toString(engine.getBufferTime()) + "s");
		maxPolyphonyLabel.setText(Integer.toString(engine.getMaxPolyphony()));

		if (engine.getConnectedMidiDevice() != null)
		{
			midiDeviceLabel.setText(engine.getConnectedMidiDevice().getDeviceInfo().getName());
			midiDeviceLabel.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, new CornerRadii(2), new Insets(4))));
		}
		else 
		{
			midiDeviceLabel.setText(Strings.STATUSBAR_NO_DEVICE_STRING);
			midiDeviceLabel.setBackground(new Background(new BackgroundFill(Color.ORANGE, new CornerRadii(2), new Insets(4))));
		}

		currProgramLabel.setText(engine.getProgramManager().getInstrumentName(parent.getCurrProgram()));

		if (engine.isRunning())
		{
			engineRunningLabel.setText(Strings.STATUSBAR_RUNNING_STRING);
			engineRunningLabel.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, new CornerRadii(2), new Insets(4))));
		}
		else 
		{
			engineRunningLabel.setText(Strings.STATUSBAR_NOT_RUNNING_STRING);
			engineRunningLabel.setBackground(new Background(new BackgroundFill(Color.ORANGE, new CornerRadii(2), new Insets(4))));
		}
	}

	private void initElements()
	{
		sampleRateLabel = new Button();
		sampleRateLabel.setTooltip(new Tooltip(Strings.STATUSBAR_TOOLTIPS[SAMPLINGRATE]));
		engineRunningLabel = new Button();
		engineRunningLabel.setTooltip(new Tooltip(Strings.STATUSBAR_TOOLTIPS[ENGINE_STATUS]));
		latencyLabel = new Button();
		latencyLabel.setTooltip(new Tooltip(Strings.STATUSBAR_TOOLTIPS[LATENCY]));
		midiDeviceLabel = new Button();
		midiDeviceLabel.setTooltip(new Tooltip(Strings.STATUSBAR_TOOLTIPS[MIDI_DEVICE]));
		currProgramLabel = new Button();
		currProgramLabel.setTooltip(new Tooltip(Strings.STATUSBAR_TOOLTIPS[CURR_INSTRUMENT]));
		maxPolyphonyLabel = new Button();
		maxPolyphonyLabel.setTooltip(new Tooltip(Strings.STATUSBAR_TOOLTIPS[MAX_POLYPHONY]));

		sampleRateLabel.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, new CornerRadii(2), new Insets(4))));
		latencyLabel.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, new CornerRadii(2), new Insets(4))));
		midiDeviceLabel.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, new CornerRadii(2), new Insets(4))));
		currProgramLabel.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, new CornerRadii(2), new Insets(4))));
		engineRunningLabel.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, new CornerRadii(2), new Insets(4))));
		maxPolyphonyLabel.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, new CornerRadii(2), new Insets(4))));
		
		getLeftItems().add(sampleRateLabel);
		getLeftItems().add(new Separator(Orientation.VERTICAL));
		getLeftItems().add(latencyLabel);
		getLeftItems().add(new Separator(Orientation.VERTICAL));
		getLeftItems().add(engineRunningLabel);
		getLeftItems().add(new Separator(Orientation.VERTICAL));
		getLeftItems().add(maxPolyphonyLabel);
		getLeftItems().add(new Separator(Orientation.VERTICAL));

		getRightItems().add(new Separator(Orientation.VERTICAL));
		getRightItems().add(midiDeviceLabel);
		getRightItems().add(new Separator(Orientation.VERTICAL));
		getRightItems().add(currProgramLabel);
		
		setText("");
	}

	@Override
	public void onValueChanged() 
	{
		update();
	}

}
