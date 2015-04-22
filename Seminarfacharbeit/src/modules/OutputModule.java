
package modules;

import java.nio.ByteBuffer;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import engine.Module;
import engine.ModuleContainer;

public class OutputModule extends Module{

	private SourceDataLine dataLine; 
	private ByteBuffer buffer;

	private int sampleCounter = 0;
	private int samplesPerPacket;
	
	private int packetSize;

	public OutputModule(ModuleContainer parent) throws LineUnavailableException
	{
		super(parent);

		packetSize = (int) (getEngine().getBufferTime() * getEngine().getSamplingRate() * getEngine().getSampleSizeInBytes());
		
		//Der Buffer soll zwei Mal so groß wie die Paketgröße sein
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, getEngine().getAudioFormat(), packetSize * 2);

		if (!AudioSystem.isLineSupported(info))
		{
			throw new LineUnavailableException();
		}
		
		dataLine = (SourceDataLine) AudioSystem.getLine(info);
		dataLine.open(getEngine().getAudioFormat());
		dataLine.start();
		
		samplesPerPacket = packetSize / getEngine().getSampleSizeInBytes();
		buffer = ByteBuffer.allocate(dataLine.getBufferSize());
	}

	@Override
	public short handleSample(short sampleValue) throws InterruptedException 
	{
		if (sampleCounter < samplesPerPacket)
		{
			buffer.putShort((short) sampleValue);
			sampleCounter++;
		}
		else 
		{
			sampleCounter = 0;
			dataLine.write(buffer.array(), 0, buffer.position());  
			
			
			while (getLineSampleCount() > packetSize)  
			{
				Thread.sleep(1);  
			}
			
			buffer.clear();
			
		}

		
		return 1;
	}
	
	private int getLineSampleCount()
	{
		return dataLine.getBufferSize() - dataLine.available();
	}

	@Override
	public void close() {
		dataLine.close();	
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}
}
