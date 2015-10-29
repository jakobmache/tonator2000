package modules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.ShortMessage;

import presets.OscillatorContainerPreset;
import midi.MidiUtils;
import containers.ContainerPreset;
import containers.ModuleContainerListener;
import containers.OscillatorContainer;
import engine.ModuleContainer;
import engine.SynthesizerEngine;
import engine.Wire;

public class InputController implements ModuleContainerListener{

	private List<ModuleContainer> allContainers;
	private Map<Integer, ModuleContainer> currentNotes;
	private ContainerPreset preset;
	private SynthesizerEngine parent;

	private final int NOTE_ON_START = 144;
	private final int NOTE_OFF_START = 128;
	private final int NUM_CHANNELS = 16;

	public InputController(SynthesizerEngine parent)
	{
		this.parent = parent;
		currentNotes = new HashMap<Integer, ModuleContainer>();
		allContainers = new ArrayList<ModuleContainer>();
		preset = new OscillatorContainerPreset();
	}

	public void handleMessage(ShortMessage message)
	{
		if (message.getChannel() == 9)
			return;
		if ((NOTE_ON_START <= message.getCommand()) && (message.getCommand() < NOTE_ON_START + NUM_CHANNELS))
		{
			if (message.getData2() == 0)
				playNoteOff(message);
			else 
				playNoteOn(message);
		}


		else if ((NOTE_OFF_START <= message.getCommand()) && (message.getCommand() < NOTE_OFF_START + NUM_CHANNELS))
		{
			playNoteOff(message);
		}
	}

	private void playNoteOn(ShortMessage message)
	{
		int key = message.getData1();
		int velocity = message.getData2();
		float frequency = MidiUtils.midiNoteNumberToFrequency(key);
		
		System.out.println("Note on! - " + key + " - " + velocity + " - " + frequency + "Hz | Channel " + message.getChannel());

		//Wird die Note schon abgespielt?
		if (currentNotes.keySet().contains(key))
		{
			System.out.println("Note wird schon abgespielt - Fehler!");
			return;
		}

		//Sie wird nicht abgespielt --> Wir erzeugen einen neuen Container, der sie spielt
		OscillatorContainer container;
		container = new OscillatorContainer(parent);
		container.applyContainerPreset(preset);
		container.addListener(this);
	
		new Wire(parent.getOutputMixer(), container, ModuleContainer.SAMPLE_OUTPUT, Mixer.NEXT_FREE_INPUT);

		//Wir müssen uns den Container merken
		allContainers.add(container);
		currentNotes.put(key, container);

		container.startPlaying(frequency, (velocity / 127.0F) * Short.MAX_VALUE);
	}

	private void playNoteOff(ShortMessage message)
	{
		int key = message.getData1();
		System.out.println("Note off! - " + key);

		//Wenn der Ton nicht abgespielt wird, können wir es ignorieren
		if (!currentNotes.keySet().contains(key))
		{
			System.out.println("Note wird nicht abgespielt - Fehler!");
			return;
		}
		
		//Sie wird abgespielt --> Wir stoppen das Abspielen (bzw bei ASDR Release)
		OscillatorContainer container = (OscillatorContainer) currentNotes.get(key);
		container.stopPlaying();
		
		//Die entsprechende Note wird nicht mehr abgespielt
		currentNotes.remove(key);
	}

	public void resetMidi() throws InvalidMidiDataException
	{
		for (ModuleContainer container:allContainers)
		{
			((OscillatorContainer) container).stopPlaying();
		}
	}

	public List<ModuleContainer> getAllContainers()
	{
		return allContainers;
	}

	public ContainerPreset getPreset() {
		return preset;
	}

	public void setPreset(ContainerPreset preset) 
	{
		this.preset = preset;
		for (ModuleContainer container:allContainers)
		{
			container.applyContainerPreset(preset);
		}
	}

	public void updatePresetValue(int id, float value)
	{
		for (ModuleContainer container:allContainers)
		{
			container.updatePreset(id, value);
		}
		preset.setParam(id, value);
	}

	@Override
	public void onContainerFinished(ModuleContainer container) 
	{
		parent.getOutputMixer().disconnectInputWire(container.getOutputWire());
		allContainers.remove(container);
	}

}
