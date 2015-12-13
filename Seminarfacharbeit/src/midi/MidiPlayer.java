package midi;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;
import javax.sound.midi.Transmitter;

import engine.SynthesizerEngine;

public class MidiPlayer 
{

	private Sequencer sequencer;
	private Sequence sequence;
	private SynthesizerEngine engine;
	
	private File midiFile;

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

		for (Transmitter trans:sequencer.getTransmitters())
		{
			Receiver receiver = trans.getReceiver();
			if ((receiver != null) && !(receiver.equals(engine)))
			{
				trans.close();
			}
		}

		this.midiFile = midiFile;
		
		System.out.println("All programs: " + getAllPrograms());
	}

	public void startPlayingMidiFile()
	{
		if (sequencer != null)
		{
			if (sequencer.getTickPosition() == sequencer.getTickLength())
				sequencer.setTickPosition(0);
			sequencer.start();
		}
	}

	public void pausePlayingMidiFile()
	{
		if (sequencer != null)
		{
			if (sequencer.isOpen())
				sequencer.stop();
		}
	}

	public void stopPlayingMidiFile()
	{
		if (sequencer != null)
		{
			pausePlayingMidiFile();
			sequencer.setTickPosition(0);
		}
	}

	public List<Integer> getAllPrograms()
	{
		List<Integer> programs = new ArrayList<Integer>();
		for (Track track :  sequence.getTracks()) 
		{
			for (int i = 0; i < track.size(); i++) 
			{ 
				MidiMessage message = track.get(i).getMessage();
				if (message instanceof ShortMessage) 
				{
					if (((ShortMessage) message).getCommand() == ShortMessage.PROGRAM_CHANGE)
					{
						if (!programs.contains(((ShortMessage) message).getData1()))
							programs.add(((ShortMessage) message).getData1());
					}
				}
			}
		}
		return programs;
	}
	
	public List<MetaMessage> getMetaMessages()
	{
		List<MetaMessage> messages = new ArrayList<MetaMessage>();
		for (Track track :  sequence.getTracks()) 
		{
			for (int i = 0; i < track.size(); i++) 
			{ 
				MidiMessage message = track.get(i).getMessage();
				if (message instanceof MetaMessage) 
				{
					messages.add((MetaMessage) message);
				}
			}
		}
		return messages;
	}
	
	public File getMidiFile()
	{
		if (sequencer.getSequence() == null)
			midiFile = null;
		return midiFile;
	}
}
