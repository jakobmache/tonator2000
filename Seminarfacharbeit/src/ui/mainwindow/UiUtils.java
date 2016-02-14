package ui.mainwindow;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.controlsfx.tools.Borders;

import engine.SynthesizerEngine;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import modules.ModuleType;
import resources.Strings;

public class UiUtils 
{
	
	static double radius = 5;
	static double thickness = 1;

	public static final String[] layoutPaths = new String[] {
		"fxml/OscillatorLayout.fxml", "fxml/LowpassFilterLayout.fxml",
		"fxml/EnvelopeLayout.fxml", "", "", 
		"fxml/BalancedMixerLayout.fxml", "fxml/LowpassFilterLayout.fxml", "fxml/ConstantLayout.fxml", "", 
		"fxml/SimpleSliderLayout.fxml"
	};
	
	public static final String WAVEFORM_PATH = "fxml/WaveformLayout.fxml";

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

	public static Node generateModuleGui(SynthesizerEngine engine, MainApplication application, ModuleType type, float[] ids) throws IOException
	{
		//Ids ist ein Array mit den Ids: Sie müssen folgende Form haben:
		//	-Oszillator: 	0: Oszillator-ID; 1: Oszillatortyp-ID
		//  -Lowpass/Highpass: 		0: Lowpass-ID; 1: Cutoffkonstanten-ID; 2: Resonanzkonstanten-ID
		//	-Envelope:		0: Envelope-ID; 1: Attack-ID; 2: Decay-ID; 3: Sustain-ID; 4: Release-ID, 5:Steilheit-ID
		//	-BalancedMixer:	0: Mixer-ID; 1: Modul1-ID; 2:Module2-ID; : Balancekonstanten-ID
		//	-Konstante:		0: Konstanten-ID, 1: Minimum, 2: Maximum, 3: Standard


		String title;
		if (ids != null && ids.length > 0)
			title = Strings.getStandardModuleName(Math.round(ids[0]));
		else
			title = Strings.MODULE_NAMES[type.getIndex()];
		return generateModuleGui(engine, title, application, type, ids);
	}
	
	public static Node generateModuleGui(SynthesizerEngine engine, String title, MainApplication application, ModuleType type, float[] ids) throws IOException
	{
		Node pane = null;

		ModuleController controller = null;
		
		thickness = 1;

		if (type == ModuleType.PLOTTER)
		{
			pane = new Plotter(engine);
			thickness = 2;
		}

		else if (type == ModuleType.OSCILLATOR)
		{
			controller = new OscillatorController(engine, Math.round(ids[0]), Math.round(ids[1]));
		}
		
		else if (type ==  ModuleType.LOWPASS || type == ModuleType.HIGHPASS)
		{
			controller = new LowpassFilterController(engine, Math.round(ids[0]), Math.round(ids[1]), Math.round(ids[2]));
		}
		
		else if (type ==  ModuleType.ENVELOPE)
		{
			controller = new EnvelopeController(engine, Math.round(ids[0]), Math.round(ids[1]), Math.round(ids[2]), Math.round(ids[3]), Math.round(ids[4]), Math.round(ids[5]));
		}
		
		else if (type ==  ModuleType.VOLUME)
		{
			controller = new VolumeController(engine);
		}
		
		else if (type ==  ModuleType.BALANCED_MIXER)
		{
			controller = new BalanceController(engine, Math.round(ids[0]), Math.round(ids[1]), Math.round(ids[2]), Math.round(ids[3]));
		}
		
		else if (type ==  ModuleType.CONSTANT)
		{
			controller = new ConstantController(engine, Math.round(ids[0]), ids[1], ids[2], ids[3]);
		}

		if (type !=  ModuleType.PLOTTER )
		{	
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(UiUtils.class.getResource(layoutPaths[type.getIndex()]));
			loader.setController(controller);
			
			pane = loader.load();
			
			controller.init();

			application.getModuleControllers().add(controller);
		}

		pane = (Pane) Borders.wrap(pane).lineBorder().title(title)
				.radius(radius).thickness(thickness).buildAll();
		
		VBox layout = new VBox();
//		layout.setStyle("-fx-border-color: red;");
		layout.getChildren().add(pane);
		VBox.setVgrow(pane, Priority.ALWAYS);

		application.initMouseHandler(pane, type);
	
		return (Node) layout;
	}
	
	public static Node generateWaveformSelector(SynthesizerEngine engine, String title, MainApplication application, int waveformId) throws IOException
	{
		WaveformController controller = new WaveformController(engine, 0, waveformId);
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(UiUtils.class.getResource(WAVEFORM_PATH));
		loader.setController(controller);
		
		Pane pane = loader.load();
		
		controller.init();
		
		pane = (Pane) Borders.wrap(pane).lineBorder().title(title)
				.radius(radius).thickness(thickness).buildAll();

		application.getModuleControllers().add(controller);
		
		return pane;
	}
}
