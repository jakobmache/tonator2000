package engine;

public class Event{
	
	public static int STOP = 0; 
	public static int START = 1;
	
	private int type;
	
	private float[] data;

	public Event(int type, float... data)
	{
		this.type = type;
		this.data = data;
	}

	public int getType() {
		return type;
	}
	
	public float[] getData()
	{
		return data;
	}
}
