package containers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ContainerPreset 
{
	
	public Map<Integer, Float> params = new HashMap<Integer, Float>();
	
	public void setParam(int id, float value)
	{
		params.put(id, value);
	}
	
	public float getParam(int id)
	{
		return params.get(id);
	}
	
	public List<Integer> getIds()
	{
		return new ArrayList<Integer>(params.keySet());
	}

}
