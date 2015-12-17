package engine;

import java.util.ArrayList;
import java.util.List;

import listener.ModuleContainerListener;
import modules.Constant;
import containers.ContainerPreset;

public abstract class ModuleContainer extends Module
{
	public static final int SAMPLE_INPUT = 0;
	public static final int SAMPLE_OUTPUT = 0;
	
	protected List<Module> modules;
	
	protected ContainerPreset preset;
	
	private List<ModuleContainerListener> listeners = new ArrayList<ModuleContainerListener>();
	
	/**
	 * Instanziert einen neuen ModuleContainer, der verschiedene Module enthalten kann. Das letzte Modul muss mit dem 
	 * Sample-Input des Containers verbunden werden.
	 * 
	 * @param parent die Engine, zu der der Container gehört
	 * @param numInputWires Anzahl der Eingänge
	 * @param numOutputWires Anzahl der Ausgänge
	 * @param id ID des Containers
	 * @param name Name des Containers
	 */
	public ModuleContainer(SynthesizerEngine parent, int numInputWires, int numOutputWires, int id, String name)
	{
		super(parent, numInputWires, numOutputWires, id, name);
		modules = new ArrayList<Module>();
	}
	
	/**
	 * Fügt dem Container ein Modul hinzu.
	 * 
	 * @param module das Modul, welches hinzugefügt werden soll
	 */
	public void addModule(Module module)
	{
		modules.add(module);
	}
	
	/**
	 * Legt eine Verbindung zwischen zwei Modulen mittels eines Kabels bzw. Wires.
	 * 
	 * @param moduleDataIsGrabbedFrom Modul, dessen Ausgang mit dem Kabel verbunden werden soll
	 * @param moduleDataIsSentTo Modul, dessen Eingang mit dem Kabel verbunden werden soll
	 * @param indexDataIsGrabbedFrom Output-Index, an dem das Kabel verbunden werden soll
	 * @param indexDataIsSentTo Input Index, an dem das Kabel verbunden werden soll
	 */
	public void addConnection(Module moduleDataIsGrabbedFrom, Module moduleDataIsSentTo, int indexDataIsGrabbedFrom, int indexDataIsSentTo)
	{
		new Wire(moduleDataIsSentTo, moduleDataIsGrabbedFrom, indexDataIsGrabbedFrom, indexDataIsSentTo);
	}
	
	/**
	 * Findet alle Module mit der gesuchten ID.
	 * 
	 * @param id die ID, die die gesuchten Module haben
	 * @return Liste der gefundenen Module
	 */
	public List<Module> findModulesById(int id)
	{
		List<Module> result = new ArrayList<Module>();
		for (Module module:modules)
		{
			if (module.getId() == id)
				result.add(module);
		}
		return result;
	}
	
	/**
	 * Findet das erste vorkommende Modul mit der gesuchten ID.
	 * 
	 * @param id ID des zu suchenden Moduls
	 * @return das erste Modul im Container mit dieser ID
	 */
	public Module findModuleById(int id)
	{
		for (Module module:modules)
		{
			if (module.getId() == id)
				return module;
		}
		return null;
	}
	
	/**
	 * Wendet ein Preset auf den Container an. Das bedeutet, dass alle Konstanten / Module
	 * die im Preset gespeicherten Werte erhalten. Der Container muss alle im Preset enthalten Module
	 * auch enthalten.
	 * 
	 * @param preset Preset, welches angewandt werden soll
	 */
	public void applyContainerPreset(ContainerPreset preset)
	{
		this.preset = preset;
		for (Integer id:preset.getIds())
		{
			Constant module = (Constant) findModuleById(id);
			module.setValue(preset.getParam(id));
		}
	}
	
	/**
	 * Gibt das aktuell angewandete Preset zurück.
	 * 
	 * @return das aktuell angewandte Preset
	 */
	public ContainerPreset getPreset()
	{
		return preset;
	}
	
	/**
	 * Aktualisiert eine Konstante bzw. ein Parameter im aktuell angewandten Preset. 
	 * 
	 * @param id die ID des zu verändernden Parameters
	 * @param value der neue Wert des Parametes
	 */
	public void updatePreset(Integer id, float value)
	{
		preset.setParam(id, value);
		Constant module = (Constant) findModuleById(id);
		module.setValue(value);
	}
	
	/**
	 * Fügt einen ModuleContainerListener hinzu. Dieser wird benachrichtigt, wenn
	 * der Container fertig mit dem Abspielen des aktuellen Tons ist.
	 * 
	 * @param listener der Listener, der hinzugefügt werden soll
	 */
	public void addListener(ModuleContainerListener listener)
	{
		listeners.add(listener);
	}
	
	/**
	 * Benachrichtigt alle Listener, dass der Container fertig mit Abspielen des Tones ist.
	 */
	public void onFinished() 
	{
		for (ModuleContainerListener listener:listeners)
		{
			listener.onContainerFinished(this);
		}
	}
	
	/**
	 * Gibt das Kabel am Sample-Ausgang zurück.
	 * 
	 * @return das Kabel am Sample-Ausgang des Containers
	 */
	public Wire getOutputWire()
	{
		return outputWires[SAMPLE_OUTPUT];
	}
}
