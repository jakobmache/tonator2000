package engine;

import java.util.ArrayList;

import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.SysexMessage;

public class SynthesizerEngine implements Receiver{

	private ArrayList<Module> modules;
	
	public SynthesizerEngine()
	{
	}

	public void run()
	{
	}

	@Override
	public void close() 
	{
	}

	@Override
	public void send(MidiMessage message, long timeStamp) 
	{
		//TODO: Handle all types of messages
		
		if (message instanceof ShortMessage)
		{
				printMessage((ShortMessage) message);
		}

		else if (message instanceof SysexMessage)
		{

		}

		else if (message instanceof MetaMessage)
		{

		}

		else
		{

		}


	}

	private void printMessage(ShortMessage message)
	{	
		String stringMessage = null;
		
		switch (message.getCommand())
		{
		case ShortMessage.NOTE_ON:
			stringMessage = "Note on: " + message.getData1() + " - " + message.getData2(); break;
		
		case ShortMessage.NOTE_OFF:
			stringMessage = "Note off: " + message.getData1() + " - " + message.getData2(); break;
		}
		
		System.out.println(stringMessage);
	}


}
