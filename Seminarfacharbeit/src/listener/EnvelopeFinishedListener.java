package listener;

import modules.Envelope;

public interface EnvelopeFinishedListener 
{
	/**
	 * Wird aufgerufen, wenn eine H�llkurve fertig mit Abspielen ist.
	 * @param envelope die fertige H�llkurve
	 */
	public void onEnvelopeFinished(Envelope envelope);
}
