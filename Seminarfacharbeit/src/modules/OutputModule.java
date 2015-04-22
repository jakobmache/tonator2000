
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
		System.out.println("OutputModule: " + buffer.position());
	}

	@Override
	public float handleSample(float sampleValue) throws InterruptedException 
	{
		buffer.putFloat(sampleValue);
		System.out.println("Handles sample: " + buffer.position());
		System.out.println(buffer.remaining());
		
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
