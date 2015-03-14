package engine;

import java.util.ArrayList;
import java.util.List;

import events.PlayEvent;

public class ModuleContainer {
	
	private List<Module> modules = new ArrayList<Module>();
	
	public List<Module> getModules()
	{
		return modules;
	}
	
	public void setModules(List<Module> modules)
	{
		this.modules = modules;
	}
	
	public void addModule(Module module)
	{
		modules.add(module);
	}
	
	public void sendEvent(Event event)
	{
		if (event instanceof PlayEvent)
		{
			handleEvent((PlayEvent) event);
		}
	}
	
	private void handleEvent(PlayEvent event)
	{
		//TODO: Handle event
	}

}
