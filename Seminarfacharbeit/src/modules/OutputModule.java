package modules;

import java.nio.ByteBuffer;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import engine.Module;
import engine.ModuleContainer;

//Grundlage: http://www.wolinlabs.com/blog/java.sine.wave.html
public class OutputModule extends Module
{
	private SourceDataLine dataLine; 
	private ByteBuffer buffer;

	private int samplesPerPacket;

	private int packetSize;

	private boolean stopPlaying = false;

	public OutputModule(ModuleContainer parent) throws LineUnavailableException 
	{
		super(parent, 1, 0);
		//Größe eines Paketes: Anzahl der Samples in der BufferZeit * Samplegröße in Bytes
		packetSize = (int) (getEngine().getBufferTime() * getEngine().getSamplingRate() * getEngine().getSampleSizeInBytes());

		//Buffergröße der SourceDataLine = 2 * Paketgröße
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, getEngine().getAudioFormat(), packetSize * 2);

		if (!AudioSystem.isLineSupported(info))
		{
			throw new LineUnavailableException();
		}

		dataLine = (SourceDataLine) AudioSystem.getLine(info);
		dataLine.open(getEngine().getAudioFormat());

		samplesPerPacket = packetSize / getEngine().getSampleSizeInBytes();
		buffer = ByteBuffer.allocate(dataLine.getBufferSize());
	}

	public void startPlaying() throws InterruptedException
	{
		dataLine.start();
		stopPlaying = false;

		while (!stopPlaying)
		{
			buffer.clear();

			//Wir berechnen soviele Samples, dass ein Paket voll ist --> Hälfte des Buffers der SourceDataLine
			for (int i = 0; i < samplesPerPacket; i++)
			{
				buffer.putShort(requestNextSample());
			}

			dataLine.write(buffer.array(), 0, buffer.position());

			//Solange der Buffer der SourceDataLine mehr als halbvoll ist, warten wir
			while (getLineSampleCount() > packetSize)
			{
				Thread.sleep(1);
			}

		}

		dataLine.drain();
		dataLine.close();
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
