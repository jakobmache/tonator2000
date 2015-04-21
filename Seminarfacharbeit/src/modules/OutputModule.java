
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

	public OutputModule(ModuleContainer parent) throws LineUnavailableException
	{
		super(parent);
		
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, parent.getAudioFormat());
	
		System.out.println(parent.getAudioFormat().toString());
		
		if (!AudioSystem.isLineSupported(info))
		{
			throw new LineUnavailableException();
		}
		
		dataLine = (SourceDataLine) AudioSystem.getLine(info);
		dataLine.open(parent.getAudioFormat());
		dataLine.start();
		
		buffer = ByteBuffer.allocate(dataLine.getBufferSize());
	}

	@Override
	public float handleSample(float sampleValue) throws InterruptedException 
	{
		int samplesThisPass = dataLine.available() / parent.getAudioFormat().getSampleSizeInBits();
		System.out.println(samplesThisPass);
		System.out.println(sampleCounter);
		if (sampleCounter < samplesThisPass)
		{
			buffer.putFloat(sampleValue);
			sampleCounter++;
		}
		else {
			sampleCounter = 0;
			buffer.putFloat(sampleValue);
			dataLine.write(buffer.array(), 0, buffer.position());
			System.out.println("AV:" + dataLine.available());
			buffer.clear();
			
			while (dataLine.getBufferSize() / 2 < dataLine.available())
			{
				System.out.println(dataLine.available());
				Thread.sleep(1);
			}
		}
		return (float) 0.0;
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
