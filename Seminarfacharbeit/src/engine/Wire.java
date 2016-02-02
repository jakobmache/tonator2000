package engine;

public class Wire 
{
	private Module moduleDataIsSentTo;
	private Module moduleDataIsGrabbedFrom;
	
	private int indexDataIsGrabbedFrom;
	private int indexDataIsSentTo;
	
	/**
	 * Erzeugt ein neues Kabel / Wire. Dieses verbindet zwei Module miteinander.
	 * 
	 * @param moduleDataIsSentTo Modul, zu dem die Daten gesendet werden und mit dessen Eingang das Kabel verbunden wird
	 * @param moduleDataIsGrabbedFrom Modul, von dessen Ausgang die Daten gelesen werden
	 * @param indexDataIsGrabbedFrom Index des Ausgangs
	 * @param indexDataIsSentTo Index des Eingangs
	 */
	public Wire (Module moduleDataIsSentTo, Module moduleDataIsGrabbedFrom, int indexDataIsGrabbedFrom, int indexDataIsSentTo)
	{
		this.moduleDataIsSentTo = moduleDataIsSentTo;
		this.moduleDataIsGrabbedFrom = moduleDataIsGrabbedFrom; 
		
		moduleDataIsSentTo.connectInputWire(indexDataIsSentTo, this);
		moduleDataIsGrabbedFrom.connectOutputWire(indexDataIsGrabbedFrom, this);
		
		this.indexDataIsGrabbedFrom = indexDataIsGrabbedFrom;
		this.indexDataIsSentTo = indexDataIsSentTo;
	}
	
	/**
	 * Ruft den nächsten Wert des Kabels ab.
	 * 
	 * @return den nächsten Wert des Eingangsmodules.
	 */
	public float getNextSample()
	{
		float value = moduleDataIsGrabbedFrom.requestNextSample(indexDataIsGrabbedFrom);
//		System.out.println(this + ":" + value);
		return value;
	}
	
	public Module getModuleDataIsGrabbedFrom()
	{
		return moduleDataIsGrabbedFrom;
	}
	
	public Module getModuleDataIsSentTo()
	{
		return moduleDataIsSentTo;
	}
	
	public int getIndexDataIsGrabbedFrom() 
	{
		return indexDataIsGrabbedFrom;
	}

	public int getIndexDataIsSentTo() 
	{
		return indexDataIsSentTo;
	}

	@Override
	public String toString()
	{
		return "Kabel von " + moduleDataIsGrabbedFrom + "(" + indexDataIsGrabbedFrom + ") zu " + moduleDataIsSentTo + "(" + indexDataIsSentTo + "), id: " + this.hashCode();
	}

}
