package ui;

import java.io.PrintWriter;
import java.io.StringWriter;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import resources.Strings;

public class UiUtils 
{
	
	public static Alert generateAlert(AlertType type, String title, String header, String text)
	{
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(text);
		return alert;
	}
	
	public static Alert generateAlert(Stage owner, AlertType type, String title, String header, String text)
	{
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(text);
		alert.initOwner(owner);
		return alert;
	}
	
	public static Alert generateExceptionDialog(Stage owner, Exception ex, String title, String header, String text)
	{
		Alert alert = generateExceptionDialog(ex, title, header, text);
		alert.initOwner(owner);
		return alert;
	}
	
	public static Alert generateExceptionDialog(Exception ex, String title, String header, String text)
	{
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(text);

		// Create expandable Exception.
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		ex.printStackTrace(pw);
		String exceptionText = sw.toString();

		Label label = new Label("Erl" + Strings.ae + "uterung (f" + Strings.ue + "r Entwickler):");

		TextArea textArea = new TextArea(exceptionText);
		textArea.setEditable(false);
		textArea.setWrapText(true);

		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(label, 0, 0);
		expContent.add(textArea, 0, 1);

		// Set expandable Exception into the dialog pane.
		alert.getDialogPane().setExpandableContent(expContent);

		return alert;
	}
}
