package modules;

import java.nio.ByteBuffer;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import engine.Module;
import engine.SynthesizerEngine;

//Grundlage: http://www.wolinlabs.com/blog/java.sine.wave.html
public class OutputModule extends Module
{
	private SourceDataLine dataLine; 
	private ByteBuffer buffer;

	private int samplesPerPacket;

	private int packetSize;

	private boolean stopPlaying = false;

	public OutputModule(SynthesizerEngine parent) throws LineUnavailableException 
	{
		super(parent, 1, 0);
		//Größe eines Paketes: Anzahl der Samples in der BufferZeit * Samplegröße in Bytes
		packetSize = (int) (parent.getBufferTime() * parent.getSamplingRate() * parent.getSampleSizeInBytes());

		//Buffergröße der SourceDataLine = 2 * Paketgröße
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, parent.getAudioFormat(), packetSize * 2);

		if (!AudioSystem.isLineSupported(info))
		{
			throw new LineUnavailableException();
		}

		dataLine = (SourceDataLine) AudioSystem.getLine(info);
		dataLine.open(parent.getAudioFormat());
		dataLine.start();
		
		samplesPerPacket = packetSize / parent.getSampleSizeInBytes();
		buffer = ByteBuffer.allocate(dataLine.getBufferSize());
	}

	public void startPlaying() throws InterruptedException
	{
		stopPlaying = false;

		while (!stopPlaying)
		{
			buffer.clear();

			//Wir berechnen soviele Samples, dass ein Paket voll ist --> Hälfte des Buffers der SourceDataLine
			for (int i = 0; i < samplesPerPacket; i++)
			{
				short value = requestNextSample(0);		
				buffer.putShort(value);
			}

			dataLine.write(buffer.array(), 0, buffer.position());

			//Solange der Buffer der SourceDataLine mehr als halbvoll ist, warten wir
			while (getLineSampleCount() > packetSize)
			{
				Thread.sleep(1);
			}

		}

		dataLine.drain();
	}


	@Override
	public short requestNextSample(int outputWireIndex) 
	{
		short sampleValue = inputWires[0].getNextSample();
		return sampleValue;
	}
	
	public void setVolume(float newValue)
	{
	      if (dataLine.isControlSupported(FloatControl.Type.MASTER_GAIN)) 
	      {
	            FloatControl volume = (FloatControl) dataLine.getControl(FloatControl.Type.MASTER_GAIN);
	            volume.setValue(newValue);
	      }
	}
	
	
	private int getLineSampleCount() 
	{
		return dataLine.getBufferSize() - dataLine.available();
	}

	public void stopPlaying()
	{
		stopPlaying = true;
	}
	
	public void close()
	{
		dataLine.close();
	}
	
	public SourceDataLine getAudioLine()
	{
		return dataLine;
	}
	
	public void updateFormat() throws LineUnavailableException
	{
		stopPlaying();
		
		//Größe eines Paketes: Anzahl der Samples in der BufferZeit * Samplegröße in Bytes
		packetSize = (int) (parent.getBufferTime() * parent.getSamplingRate() * parent.getSampleSizeInBytes());

		//Buffergröße der SourceDataLine = 2 * Paketgröße
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, parent.getAudioFormat(), packetSize * 2);

		if (!AudioSystem.isLineSupported(info))
		{
			throw new LineUnavailableException();
		}

		dataLine = (SourceDataLine) AudioSystem.getLine(info);
		dataLine.open(parent.getAudioFormat());
		dataLine.start();
		
		samplesPerPacket = packetSize / parent.getSampleSizeInBytes();
		buffer = ByteBuffer.allocate(dataLine.getBufferSize());
	}

}
