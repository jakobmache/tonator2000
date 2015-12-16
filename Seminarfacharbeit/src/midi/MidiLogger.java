package midi;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;

import modules.listener.MidiLoggerListener;

public class MidiLogger 
{	
	public static final String SEPARATOR = " : ";
	private List<MidiLoggerListener> listeners = new ArrayList<MidiLoggerListener>();
	
	public void receiveEvent(MidiMessage event, long timeStamp)
	{
		for (MidiLoggerListener listener:listeners)
		{
			listener.eventReceived(eventToDescription(event, timeStamp));
		}
	}

	public void addListener(MidiLoggerListener listener)
	{
		listeners.add(listener);
	}

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
