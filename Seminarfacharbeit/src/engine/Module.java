package engine;

public abstract class Module 
{
	//Die Typen aller Module. Damit kann man, wenn man Daten Modulen zuordnet, zum Beispiel in Arrays
	//auf diese zugreifen (Index = Module.MODULNAME)
	public static final int OSCILLATOR = 0;
	public static final int LOWPASS = 1;
	public static final int ENVELOPE = 2;
	public static final int AMPLIFIER = 3;
	public static final int MIXER = 4;
	public static final int BALANCED_MIXER = 5;
	public static final int HIGHPASS = 6;
	public static final int CONSTANT = 7;
	public static final int PLOTTER = 8;
	public static final int VOLUME = 9;
	public static final int OUTPUT_MODULE = 10;
	
	//Alle Module, die im Editor vom Benutzer erstellt werden k�nnen
	public static final int[] CREATABLE_MODULES = new int[]{
		OSCILLATOR, LOWPASS, ENVELOPE, AMPLIFIER, MIXER, BALANCED_MIXER, CONSTANT
	};
	
	protected SynthesizerEngine parent;

	protected Wire[] inputWires;
	protected Wire[] outputWires;

	protected String[] inputNames;
	protected String[] outputNames;

	protected int moduleId;

	protected boolean enabled = true;
	
	protected String name = "Modul";
	
	public int type;
	
	/**
	 * Instanziert ein neues Objekt der Klasse Module.
	 * Dieses repr�sentiert dabei ein Modul des Synthesizers.
	 *
	 * @param parent die Engine, von der das Modul die Daten liest
	 * @param numInputWires die Anzahl an Inputs
	 * @param numOutputWires die Anzahl an Outputs
	 * @param id ID des Modules, einzigartiger Identifizierer
	 * @param moduleName der Name des Moduls
	 */
	public Module(SynthesizerEngine parent, int numInputWires, int numOutputWires, int id, String moduleName)
	{
		this.parent = parent;
		this.inputWires = new Wire[numInputWires];
		this.outputWires = new Wire[numOutputWires];
		this.inputNames = new String[numInputWires];
		this.outputNames = new String[numOutputWires];
		this.moduleId = id;
		this.name = moduleName;
	}
	
	/**
	 * Gibt das n�chste berechnete Sample zur�ck. 
	 *
	 * @param index Output, von dem das Sample abgefragt werden soll
	 * @return neu berechnetes Sample
	 */
	public float requestNextSample(int index)
	{
		if (enabled)
		{
			float value =  calcNextSample(index);
			if (value > Short.MAX_VALUE || value < Short.MIN_VALUE)
				System.out.println("Value too high at " + moduleId);
			return value;
		}
		else 
			return calcNextDisabledSample(index);
	}
	
	/**
	 * Berechnet das n�chste Sample bei aktiviertem Modul.
	 * Hier muss die Logik des Moduls implementiert werden, das hei�t, wie es seine Werte berechnet.
	 *
	 * @param index Output, von dem das Sample berechnet werden soll
	 * @return neu berechnetes Sample
	 */
	public abstract float calcNextSample(int index);
	
	/**
	 * Berechnet das n�chste Sample bei deaktiviertem Modul.
	 *
	 * @param index Output, von dem das Sample berechnet werden soll
	 * @return neu berechnetes Sample
	 */
	public abstract float calcNextDisabledSample(int index);
	
	/**
	 * Verbindet ein Kabel mit einem Input des Moduls.
	 *
	 * @param index Input, an dem das Kabel angeschlossen werden soll
	 * @param wire Kabel, das mit dem Modul verbunden werden soll
	 */
	public void connectInputWire(int index, Wire wire)
	{
		inputWires[index] = wire;
	}
	
	/**
	 * Verbindet ein Kabel mit einem Output des Moduls.
	 *
	 * @param index Output, an dem das Kabel angeschlossen werden soll
	 * @param wire Kabel, das mit dem Modul verbunden werden soll
	 */
	public void connectOutputWire(int index, Wire wire)
	{
		outputWires[index] = wire;
	}
	
	/**
	 * Gibt die ID des Moduls zur�ck.
	 *
	 * @return ID
	 */
	public int getId()
	{
		return moduleId;
	}
	
	/**
	 * Aktiviert oder deaktiviert das Modul.
	 *
	 * @param value true wenn aktiviert, sonst false
	 */
	public void setEnabled(boolean value)
	{
		enabled = value;
	}
	
	/**
	 * Gibt zur�ck, ob das Modul aktiviert ist.
	 *
	 * @return true, wenn das Modul aktiviert ist
	 */
	public boolean isEnabled()
	{
		return enabled;
	}
	
	/**
	 * Gibt den Namen des Moduls zur�ck.
	 *
	 * @return Name
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Gibt die Namen der Inputs zur�ck.
	 *
	 * @return Namen der Inputs
	 */
	public String[] getInputNames()
	{
		return inputNames;
	}
	
	/**
	 * Gibt die Namen der Outputs zur�ck.
	 *
	 * @return Namen der Outputs
	 */
	public String[] getOutputNames()
	{
		return outputNames;
	}
}
