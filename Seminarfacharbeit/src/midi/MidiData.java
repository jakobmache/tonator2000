/*
 * 
 */
package midi;

import java.util.HashMap;
import java.util.Map;

// TODO: Auto-generated Javadoc
/**
 * The Class MidiData.
 */
public class MidiData {
	
	/** The frequencies. */
	private static Map<Integer, Float> FREQUENCIES = new HashMap<Integer, Float>();
	
	/**
	 * Instantiates a new midi data.
	 */
	public MidiData()
	{
		FREQUENCIES.put(69, (float) 440.0);
	}
	
	/**
	 * Gets the frequency.
	 *
	 * @param key the key
	 * @return the frequency
	 */
	public static float getFrequency(int key)
	{
		return FREQUENCIES.get(key);
	}

}
