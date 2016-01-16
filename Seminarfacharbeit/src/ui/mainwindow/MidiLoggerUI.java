package ui.mainwindow;

import resources.Strings;
import javafx.application.Platform;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import listener.MidiLoggerListener;
import midi.MidiLogger;

public class MidiLoggerUI extends ListView<String> implements MidiLoggerListener
{
	public MidiLoggerUI(MidiLogger logger) 
	{
		logger.addListener(this);
		setEditable(false);
		
		MenuItem clearItem = new MenuItem();
		clearItem.setOnAction((event) ->
		{
			getItems().clear();
		});
		clearItem.setText(Strings.CLEAR_MIDI_LOGGER_MENU_ITEM);		
		
		ContextMenu menu = new ContextMenu();
		menu.getItems().add(clearItem);
		
		setContextMenu(menu);
	}

	@Override
	public void eventReceived(String description) 
	{
		Platform.runLater(() -> 
		{
			getItems().add(description);
		});
	}
}
