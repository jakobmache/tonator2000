package containers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import engine.Module;

public class ContainerConfiguration 
{
	
	private List<Module> idTypes = new HashMap<Integer, Integer>();
	private List<Integer[]> wires = new ArrayList<Integer[]>();
	
	public ContainerConfiguration()
	{
		
	}
	
	public void addModule(int type, int id)
	{
		idTypes.put(id, type);
	}
	
	public void addWire(int from, int to, int fromIndex, int toIndex)
	{
		wires.add(new Integer[]{from, to, fromIndex, toIndex});
	}
	
	public Map<Integer, Integer> getModules()
	{
		return idTypes;
	}
	
	public List<Integer[]> getWires()
	{
		return wires;
	}
}
