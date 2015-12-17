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

	/**
	 * Erstellt einen Midi-Player, der MIDI-Dateien wiedergeben kann.
	 * 
	 * @param engine die Engine, in der der Player wiedergeben soll
	 */
	public MidiPlayer(SynthesizerEngine engine)
	{
		this.engine = engine;
	}

	/**
	 * Lädt ein MIDI-File in den Player.
	 * 
	 * @param midiFile die MIDI-Datei
	 * @throws MidiUnavailableException wenn das Verbinden mit der Engine scheitert
	 * @throws InvalidMidiDataException wenn die MIDI-Datei ungültige Daten enthält
	 * @throws IOException wenn die MIDI-Datei nicht gelesen werden kann
	 */
	public void loadMidiFile(File midiFile) throws MidiUnavailableException, InvalidMidiDataException, IOException
	{
		sequencer = MidiSystem.getSequencer();
		sequence = MidiSystem.getSequence(midiFile);
		sequencer.setSequence(sequence);

		engine.connectMidiDevice(sequencer);

		sequencer.open();

		//Wir müssen alle anderen Transmitter und Receiver vom Sequencer (dem Wiedergabegerät) entfernen
		//Sonst werden die Daten nicht an die Engine weitergegegeben
		for (Transmitter trans:sequencer.getTransmitters())
		{
			Receiver receiver = trans.getReceiver();
			if ((receiver != null) && !(receiver.equals(engine)))
			{
				trans.close();
			}
		}

		this.midiFile = midiFile;
	}

	/**
	 * Startet das Abspielen der geladenen MIDI-Datei.
	 */
	public void startPlayingMidiFile()
	{
		if (sequencer != null)
		{
			//Wenn wir am Ende sind, von vorne weitermachen
			if (sequencer.getTickPosition() == sequencer.getTickLength())
				sequencer.setTickPosition(0);
			sequencer.start();
		}
	}

	/**
	 * Pausiert das Abspielen der geladenen Datei.
	 */
	public void pausePlayingMidiFile()
	{
		if (sequencer != null)
		{
			if (sequencer.isOpen())
				sequencer.stop();
		}
	}

	/**
	 * Stoppt das Abspielen der geladenen Datei. Das heißt, beim nächsten Abspielen beginnt es von vorne.
	 */
	public void stopPlayingMidiFile()
	{
		if (sequencer != null)
		{
			pausePlayingMidiFile();
			sequencer.setTickPosition(0);
		}
	}

	/**
	 * Liest alle Instrumente aus, die in der Datei vorkommen.
	 * 
	 * @return Liste alle vorkommenden Instrumente
	 */
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
	
	/**
	 * Liest alle MetaMessages in der geladenen Datei aus.
	 * 
	 * @return Liste aller MetaMessages in der Datei
	 */
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
	
	/**
	 * Gibt die aktuell geladenen Datei zurück.
	 * 
	 * @return die aktuell geladene Datei
	 */
	public File getMidiFile()
	{
		if (sequencer.getSequence() == null)
			midiFile = null;
		return midiFile;
	}
}
