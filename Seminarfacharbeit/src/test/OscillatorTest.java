package test;

import java.nio.ByteBuffer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class OscillatorTest 
{
	private static float frequency = 440;
	private static float amplitude = 10000;
	private static int samplingRate = 44100;
	private static double cyclePosition = 0;
	private final static int SAMPLE_SIZE = 2;
	
	private static SourceDataLine dataLine;
	private static ByteBuffer buffer;
	private static double bufferDuration = 0.01;
	private final static int PACKET_SIZE = (int) (bufferDuration * samplingRate * SAMPLE_SIZE);
	
	private static int samplesThisPass;
	private static int sampleCounter = 0;

	
	public static void main(String[] args) throws InterruptedException, LineUnavailableException
	{
		long start = System.currentTimeMillis();
		OscillatorTest testOsci = new OscillatorTest();
		for (int i = 0; i < 5 * samplingRate; i++)
		{
			System.out.println(i);
			float sample = testOsci.getNextValue();
			System.out.println(sample);
			testOsci.play(sample);
		}
		long end = System.currentTimeMillis();
		System.out.println(end - start);

	}
	
	private static int getLineSampleCount() {
        return dataLine.getBufferSize() - dataLine.available();
     }
	
	private static float getNextValue()
	{
		double cycleIncrease = frequency / samplingRate;
		float value = (float) (amplitude * Math.sin(2 * Math.PI * cyclePosition));
		cyclePosition += cycleIncrease;
		if (cyclePosition > 1)
			cyclePosition -= 1;	
		return value;
	}
	
	private static void play(float sample) throws InterruptedException
	{
		if (sampleCounter < samplesThisPass)
		{
			buffer.putShort((short)sample);
			sampleCounter++;
		}
		else 
		{
			sampleCounter = 0;
			dataLine.write(buffer.array(), 0, buffer.position());  

			while (getLineSampleCount() > PACKET_SIZE)  
			{
				
				Thread.sleep(1);  
			}
			
			buffer.clear();
		}
	}
	public OscillatorTest() throws LineUnavailableException
	{
		AudioFormat format = new AudioFormat(samplingRate, 16, 1, true, true);
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

		if (!AudioSystem.isLineSupported(info))
		{
			throw new LineUnavailableException();
		}
		
		dataLine = (SourceDataLine) AudioSystem.getLine(info);
		dataLine.open(format);
		dataLine.start();
		
		samplesThisPass = PACKET_SIZE / SAMPLE_SIZE;

		buffer = ByteBuffer.allocate(PACKET_SIZE);
	}

}
