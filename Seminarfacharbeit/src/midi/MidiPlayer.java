package midi;

import java.io.File;
import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Transmitter;

import engine.SynthesizerEngine;

public class MidiPlayer 
{
	
	private Sequencer sequencer;
	private Sequence sequence;
	private SynthesizerEngine engine;
	
	public MidiPlayer(SynthesizerEngine engine)
	{
		this.engine = engine;
	}
	
	public void loadMidiFile(File midiFile) throws MidiUnavailableException, InvalidMidiDataException, IOException
	{
		sequencer = MidiSystem.getSequencer();
		sequence = MidiSystem.getSequence(midiFile);
		sequencer.setSequence(sequence);
		engine.connectMidiDevice(sequencer);
		
		sequencer.open();
		
		System.out.println(sequencer.getTransmitters());
		for (Transmitter trans:sequencer.getTransmitters())
		{
			Receiver receiver = trans.getReceiver();
			if ((receiver != null) && !(receiver.equals(engine)))
			{
				trans.close();
			}
		}
	}
	
	public void startPlayingMidiFile()
	{
		if (sequencer != null)
		{
			sequencer.start();
			System.out.println(sequencer.getTransmitters());
		}
	}
	
	public void stopPlayingMidiFile()
	{
		if (sequencer != null)
		{
			sequencer.stop();
		}
	}

}
