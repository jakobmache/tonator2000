package midi;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;

import listener.MidiLoggerListener;

public class MidiLogger 
{	
	public static final String SEPARATOR = " : ";
	private List<MidiLoggerListener> listeners = new ArrayList<MidiLoggerListener>();

	/**
	 * Wird aufgerufen, wenn ein Event eingeht.
	 * 
	 * @param event das Event
	 * @param timeStamp der Zeitstempel
	 */
	public void receiveEvent(MidiMessage event, long timeStamp)
	{
		for (MidiLoggerListener listener:listeners)
		{
			listener.eventReceived(eventToDescription(event, timeStamp));
		}
	}
	
	/**
	 * Fügt einen Listener zum Logger hinzu, der über neue Einträge benachrichtigt wird.
	 * @param listener der hinzzufügender Listener
	 */
	public void addListener(MidiLoggerListener listener)
	{
		listeners.add(listener);
	}

	/**
	 * Wandelt ein MIDI-Event in seine textuelle Repräsentation um.
	 * 
	 * @param event das Event
	 * @param timeStamp Zeitstempel des Events
	 * @return die textuelle Repräsentation
	 */
	public String eventToDescription(MidiMessage event, long timeStamp)
	{
		StringBuilder builder = new StringBuilder();
		builder.append(timeStamp);
		builder.append(SEPARATOR);
		if (event instanceof ShortMessage)
		{
			ShortMessage message = ((ShortMessage) event);
			if (message.getCommand() == ShortMessage.NOTE_ON)
			{

				builder.append("Note on -  channel " + message.getChannel());
				builder.append(" - key " + message.getData1());
				builder.append(" - velocity " + message.getData2());
			}
			else if (message.getCommand() == ShortMessage.NOTE_OFF)
			{
				builder.append("Note off -  channel " + message.getChannel());
				builder.append(" - key " + message.getData1());
				builder.append(" - velocity " + message.getData2());
			}
			else if (message.getCommand() == ShortMessage.PROGRAM_CHANGE)
			{
				builder.append("New program on channel " + message.getChannel());
				builder.append(" - program number " + message.getData1());
			}
			else if (message.getCommand() == ShortMessage.CONTROL_CHANGE)
			{
				builder.append("Control change on channel " + message.getChannel());
				builder.append(" with data: " + message.getData1() + " | ");
				builder.append(message.getData2());
			}
			else
			{
				builder.append("ShortMessage " + message.getCommand() + " with Data: ");
				builder.append(message.getData1() + " | ");
				builder.append(message.getData2());
			}

		}
		else
		{
			builder.append("Midi-Event!");
		}

		return builder.toString();
	}

}
