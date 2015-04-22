
package midi;

import java.util.HashMap;
import java.util.Map;

public class MidiData {

	private static Map<Integer, Float> FREQUENCIES = new HashMap<Integer, Float>();
	

	public MidiData()
	{
		FREQUENCIES.put(69, (float) 440.0);
	}
	
	public static float getFrequency(int key)
	{
		return FREQUENCIES.get(key);
	}

}
