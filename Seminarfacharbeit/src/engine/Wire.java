package engine;

public class Wire 
{
	private Module moduleDataIsSentTo;
	private Module moduleDataIsGrabbedFrom;
	
	private int indexDataIsGrabbedFrom;
	
	public Wire (Module moduleDataIsSentTo, Module moduleDataIsGrabbedFrom, int indexDataIsGrabbedFrom, int indexDataIsSentTo)
	{
		this.moduleDataIsSentTo = moduleDataIsSentTo;
		this.moduleDataIsGrabbedFrom = moduleDataIsGrabbedFrom; 
		
		moduleDataIsSentTo.connectInputWire(indexDataIsSentTo, this);
		moduleDataIsGrabbedFrom.connectOutputWire(indexDataIsGrabbedFrom, this);
		
		this.indexDataIsGrabbedFrom = indexDataIsGrabbedFrom;
	}
	
	public float getNextSample()
	{
		return moduleDataIsGrabbedFrom.requestNextSample(indexDataIsGrabbedFrom);
	}
	
	public String toString()
	{
		return "Kabel von " + moduleDataIsGrabbedFrom + " zu " + moduleDataIsSentTo + ", id: " + this.hashCode();
	}

}
