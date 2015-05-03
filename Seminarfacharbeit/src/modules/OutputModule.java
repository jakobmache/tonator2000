package modules;

import java.nio.ByteBuffer;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import engine.Module;
import engine.ModuleContainer;
import engine.Wire;

public class OutputModule extends Module
{
	private SourceDataLine dataLine; 
	private ByteBuffer buffer;

	private int sampleCounter = 0;
	private int samplesPerPacket;

	private int packetSize;

	private boolean stopPlaying = false;

	public OutputModule(ModuleContainer parent) throws LineUnavailableException 
	{
		super(parent, 1, 0);
		packetSize = (int) (getEngine().getBufferTime() * getEngine().getSamplingRate() * getEngine().getSampleSizeInBytes());

		DataLine.Info info = new DataLine.Info(SourceDataLine.class, getEngine().getAudioFormat(), packetSize * 2);

		if (!AudioSystem.isLineSupported(info))
		{
			throw new LineUnavailableException();
		}

		dataLine = (SourceDataLine) AudioSystem.getLine(info);
		dataLine.open(getEngine().getAudioFormat());
		//dataLine.start();

		samplesPerPacket = packetSize / getEngine().getSampleSizeInBytes();
		buffer = ByteBuffer.allocate(dataLine.getBufferSize());
	}

	public void startPlaying()
	{
		dataLine.start();
		stopPlaying = false;
		
		long startTime = System.currentTimeMillis();
		while (!stopPlaying)
		{
			buffer.clear();

			for (int i = 0; i < samplesPerPacket; i++)
			{
				buffer.putShort(requestNextSample());
			}

			dataLine.write(buffer.array(), 0, buffer.position());

			try 
			{
				while (getLineSampleCount() > packetSize)
				{
					Thread.sleep(1);
				}
			} 
			catch (InterruptedException e) 
			{	
			}
		}
		
		dataLine.drain();
		dataLine.close();
		long endTime = System.currentTimeMillis();
		System.out.println(endTime - startTime);
	}


	@Override
	public short requestNextSample() 
	{
		short sampleValue = inputWires[0].getNextSample();
		return sampleValue;
	}

	private int getLineSampleCount() 
	{
		return dataLine.getBufferSize() - dataLine.available();
	}

	public void stopPlaying()
	{
		stopPlaying = true;
	}

}
