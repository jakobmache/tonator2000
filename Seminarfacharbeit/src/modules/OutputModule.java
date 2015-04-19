/*
 * 
 */
package modules;

import java.nio.ByteBuffer;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import engine.Module;
import engine.ModuleContainer;

// TODO: Auto-generated Javadoc
/**
 * The Class OutputModule.
 */
public class OutputModule extends Module{
	
	/** The data line. */
	private SourceDataLine dataLine; 
	
	/** The buffer. */
	private ByteBuffer buffer;

	/**
	 * Instantiates a new output module.
	 *
	 * @param parent the parent
	 * @throws LineUnavailableException the line unavailable exception
	 */
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
	
	/* (non-Javadoc)
	 * @see engine.Module#handleSample(float)
	 */
	@Override
	public float handleSample(float sampleValue) 
	{
		buffer.clear();
		buffer.putFloat(sampleValue);
		dataLine.write(buffer.array(), 0, buffer.position());
		return 22;
		
	}

	/* (non-Javadoc)
	 * @see engine.Module#close()
	 */
	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}
	
	
	
	

}
