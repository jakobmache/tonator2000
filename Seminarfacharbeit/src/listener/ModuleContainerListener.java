package listener;

import engine.ModuleContainer;

public interface ModuleContainerListener 
{
	/**
	 * Diese Methode wird aufgerufen, wenn ein Container fertig mit dem Abspielen eines Tons ist.
	 * 
	 * @param container der Container, der fertig ist
	 */
	public void onContainerFinished(ModuleContainer container);
}
