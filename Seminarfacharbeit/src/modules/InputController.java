package modules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.ShortMessage;

import midi.MidiUtils;
import containers.OscillatorContainer;
import engine.ModuleContainer;
import engine.SynthesizerEngine;
import engine.Wire;

public class InputController{

	private List<Integer> currentNotes;
	private Map<Integer, ModuleContainer> allModules;
	private SynthesizerEngine parent;
	
	private final int NOTE_ON_START = 144;
	private final int NOTE_OFF_START = 128;
	private final int NUM_CHANNELS = 16;

	public InputController(SynthesizerEngine parent)
	{
		this.parent = parent;
		currentNotes = new ArrayList<Integer>();
		allModules = new HashMap<Integer, ModuleContainer>();
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

		System.out.println("Note on! - " + key + " - " + velocity + " | Channel " + message.getChannel());
		float frequency = MidiUtils.midiNoteNumberToFrequency(key);

		if (currentNotes.contains(key))
		{
			System.out.println("Note wird schon abgespielt - Fehler!");
			return;
		}

		OscillatorContainer container;

		if (allModules.get(key) == null)
		{
			container = new OscillatorContainer(parent);
			allModules.put(key, container);
			new Wire(parent.getOutputMixer(), container, 0, parent.getOutputMixer().getNumModules());
		}
		else 
		{
			container = (OscillatorContainer) allModules.get(key);
		}


		Oscillator oscillator = container.getOscillator();
		oscillator.setType(parent.getOscillatorType());
		container.startPlaying(frequency, (float) (velocity / 127.0) * Short.MAX_VALUE);;

		currentNotes.add(key);
	}
	
	private void playNoteOff(ShortMessage message)
	{
		int key = message.getData1();
		System.out.println("Note off! - " + key);

		if (!currentNotes.contains(key))
		{
			System.out.println("Note wird nicht abgespielt - Fehler!");
			return;
		}


		OscillatorContainer container = (OscillatorContainer) allModules.get(key);

		container.stopPlaying();
		currentNotes.remove((Integer) key);
	}
	
	public void resetMidi() throws InvalidMidiDataException
	{
		for (int key:new ArrayList<Integer>(currentNotes))
		{
			playNoteOff(new ShortMessage(ShortMessage.NOTE_OFF, key, 0));
		}
	}
	
	
	public List<Integer> getCurrentNotes()
	{
		return currentNotes;
	}

	public Map<Integer, ModuleContainer> getAllModules()
	{
		return allModules;
	}

}
