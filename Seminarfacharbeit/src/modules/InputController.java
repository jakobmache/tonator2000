package modules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sound.midi.ShortMessage;
import javax.sound.sampled.LineUnavailableException;

import midi.MidiUtils;
import containers.OscillatorContainer;
import containers.StandardModuleContainer;
import engine.Module;
import engine.ModuleContainer;
import engine.SynthesizerEngine;
import engine.Wire;

public class InputController{

	private List<Integer> currentNotes;
	private Map<Integer, ModuleContainer> allModules;
	private SynthesizerEngine parent;

	public InputController(SynthesizerEngine parent)
	{
		this.parent = parent;
		currentNotes = new ArrayList<Integer>();
		allModules = new HashMap<Integer, ModuleContainer>();
	}

	public void handleMessage(ShortMessage message)
	{

		if (message.getCommand() == ShortMessage.NOTE_ON)
		{
			int key = message.getData1();
			int velocity = message.getData2();

			System.out.println("Note on! - " + key + " - " + velocity);
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
			container.startPlaying(frequency, Short.MAX_VALUE);
			System.out.println(container + " is playing with " + oscillator.getFrequency() + "Hz, Ampl:" + oscillator.getAmplitude());
			//container.getOscillator().setAmplitude((velocity / 127.0) * Short.MAX_VALUE);

			currentNotes.add(key);
		}

		else if (message.getCommand() == ShortMessage.NOTE_OFF)
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
