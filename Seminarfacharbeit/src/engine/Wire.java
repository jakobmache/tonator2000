package engine;

public class Wire 
{
	private Module moduleDataIsSentTo;
	private Module moduleDataIsGrabbedFrom;
	
	private int indexDataIsGrabbedFrom;
	
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
	}
	
	/**
	 * Ruft den nächsten Wert des Kabels ab.
	 * 
	 * @return den nächsten Wert des Eingangsmodules.
	 */
	public float getNextSample()
	{
		return moduleDataIsGrabbedFrom.requestNextSample(indexDataIsGrabbedFrom);
	}
	
	@Override
	public String toString()
	{
		return "Kabel von " + moduleDataIsGrabbedFrom + " zu " + moduleDataIsSentTo + ", id: " + this.hashCode();
	}

}
