package modules;

public enum ModuleType 
{
	
	OSCILLATOR(0, true),
	LOWPASS(1, true),
	ENVELOPE(2, true),
	AMPLIFIER(3, true),
	MIXER(4, true),
	BALANCED_MIXER(5, true),
	HIGHPASS(6, true),
	CONSTANT(7, true),
	PLOTTER(8, false),
	VOLUME(9, false),
	OUTPUT_MODULE(10, false);
	
	private int index;
	private boolean creatable;
	
	private ModuleType(int index, boolean creatable)
	{
		this.index = index;
		this.creatable = creatable;
	}
	
	public int getIndex()
	{
		return index;
	}
	
	public boolean isCreatable()
	{
		return creatable;
	}
}
