package ui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import javax.sound.sampled.LineUnavailableException;

import jdk.nashorn.internal.ir.Labels;
import modules.Ids;

import org.controlsfx.control.NotificationPane;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.PopOver.ArrowLocation;
import org.controlsfx.control.action.Action;
import org.controlsfx.tools.Borders;

import resources.Strings;
import engine.SynthesizerEngine;

//TODO: Midi-Player Fenster
//TODO: Fix disablen
public class MainApplication extends Application {

	public static final int OVERLAY_MIDI = 0;

	private Stage primaryStage;
	private BorderPane rootLayout;
	private SynthesizerEngine engine;

	private SynthiStatusBar statusBar;
	private NotificationPane notificationPane;

	private MenuController menuController;

	private List<ModuleController> controllers = new ArrayList<ModuleController>();

	@FXML 
	private CheckMenuItem monoMenuItem;
	@FXML
	private CheckMenuItem stereoMenuItem;

	private HBox synthesizerLayout;
	private VBox mainLayout;

	private int currProgram = 0;

	@Override
	public void start(Stage primaryStage)
	{
		try 
		{
			try {
				this.engine = new SynthesizerEngine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
		catch (LineUnavailableException e) 
		{
			e.printStackTrace();
		}
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle(Strings.APPLICATION_NAME + " - " + Strings.VERSION_NUMBER);

		synthesizerLayout = new HBox(5);
		mainLayout = new VBox(5);
		initRootLayout();

		initStatusBarAndOverlay();

		mainLayout.getChildren().add(notificationPane);

		initOscillator();
		initFilter();
		initFilterEnvelope();
		initEnvelope();
		initVolume();
		initPlotter();

		initEventHandlers();

		updateStatusBar();

		setCurrProgram(0);

		URL iconUrl = getClass().getClassLoader().getResource("resources/icon.png");
		primaryStage.getIcons().add(new Image(iconUrl.toString()));

		primaryStage.show();
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.initOwner(primaryStage);
		alert.setTitle(Strings.START_POPUP_TITLE);
		alert.setContentText(Strings.START_POPUP_TEXT);
		alert.setHeaderText(Strings.START_POPUP_HEADER);
		alert.showAndWait();

		showOverlay(OVERLAY_MIDI);

		engine.run();
	}

	public void showOverlay(int type)
	{
		if (type == OVERLAY_MIDI)
		{
			notificationPane.setText(Strings.OVERLAY_MIDI_STRING);
			notificationPane.getActions().add(new Action("MIDI-Ger" + Strings.ae + "t ausw" + Strings.ae + "hlen", ae ->
			{
				menuController.onSelectMidiDeviceAction(ae);	
				notificationPane.hide();
			}));
			notificationPane.getActions().add(new Action("MIDI-Datei laden", ae ->
			{
				menuController.onSelectMidiFileAction(ae);	
				notificationPane.hide();
			}));
		}

		notificationPane.show();
	}

	public void initRootLayout() 
	{
		try 
		{
			menuController = new MenuController(engine, this);
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApplication.class.getResource("fxml/RootLayout.fxml"));

			loader.setController(menuController);
			rootLayout = (BorderPane) loader.load();

			menuController.loadData();

			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);

			rootLayout.setCenter(mainLayout);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	public void initStatusBarAndOverlay()
	{
		statusBar = new SynthiStatusBar(engine, this);
		rootLayout.setBottom(statusBar);
		notificationPane = new NotificationPane(synthesizerLayout);
	}

	public void initOscillator() {
		try 
		{
			OscillatorController controller = new OscillatorController(engine, Ids.ID_OSCILLATOR_TONE_1);

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApplication.class.getResource("fxml/OscillatorLayout.fxml"));

			loader.setController(controller);
			TitledPane oscillatorView = (TitledPane) loader.load();
			controller.setMainPane(loader);

			initMouseHandler(oscillatorView, Strings.OSCILLATOR);

			synthesizerLayout.getChildren().add(oscillatorView);

			controllers.add(controller);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	public void initFilter()
	{
		try 
		{
			LowpassFilterController controller = new LowpassFilterController(engine, Ids.ID_LOWPASS_1);

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApplication.class.getResource("fxml/LowpassFilterLayout.fxml"));

			loader.setController(controller);
			TitledPane filterView = (TitledPane) loader.load();
			controller.init();

			initMouseHandler(filterView, Strings.LOWPASS);

			synthesizerLayout.getChildren().add(filterView);
			controllers.add(controller);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	public void initFilterEnvelope()
	{
		try 
		{
			EnvelopeController controller = new EnvelopeController(engine, Ids.ID_CONSTANT_ATTACK_2, Ids.ID_CONSTANT_DECAY_2, Ids.ID_CONSTANT_SUSTAIN_2, Ids.ID_CONSTANT_RELEASE_2, Ids.ID_ENVELOPE_2);

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApplication.class.getResource("fxml/EnvelopeLayout.fxml"));

			loader.setController(controller);
			TitledPane envelopeView = (TitledPane) loader.load();
			envelopeView.setText("Hüllkurve - Tiefpassfilter");
			controller.init();

			initMouseHandler(envelopeView, Strings.ENVELOPE);

			synthesizerLayout.getChildren().add(envelopeView);
			controllers.add(controller);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	public void initEnvelope()
	{
		try 
		{
			EnvelopeController controller = new EnvelopeController(engine, Ids.ID_CONSTANT_ATTACK_1, Ids.ID_CONSTANT_DECAY_1, Ids.ID_CONSTANT_SUSTAIN_1, Ids.ID_CONSTANT_RELEASE_1, Ids.ID_ENVELOPE_1);

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApplication.class.getResource("fxml/EnvelopeLayout.fxml"));

			loader.setController(controller);
			TitledPane envelopeView = (TitledPane) loader.load();
			controller.init();

			initMouseHandler(envelopeView, Strings.ENVELOPE);

			synthesizerLayout.getChildren().add(envelopeView);

			controllers.add(controller);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	public void initPlotter()
	{
		Plotter plotter = new Plotter(engine);
		mainLayout.getChildren().add(plotter);

		initMouseHandler(plotter, Strings.PLOTTER);

		VBox.setVgrow(plotter, Priority.ALWAYS);
	}

	public void initVolume()
	{
		try 
		{
			VolumeController controller = new VolumeController(engine);

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApplication.class.getResource("fxml/VolumeLayout.fxml"));

			loader.setController(controller);
			TitledPane volumeView = (TitledPane) loader.load();
			controller.init();

			synthesizerLayout.getChildren().add(volumeView);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	public void updateStatusBar()
	{
		statusBar.update();
	}

	private void initEventHandlers()
	{
		primaryStage.setOnCloseRequest((event) -> {
			engine.close();
		});      
	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public void setCurrProgram(int newProgram)
	{
		currProgram = newProgram;
		for (ModuleController controller:controllers)
		{
			controller.setCurrProgram(currProgram);
		}
	}

	public void update()
	{
		for (ModuleController controller:controllers)
		{
			controller.loadData();
		}
	}

	private void initMouseHandler(Parent pane, int module)
	{
		pane.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> 
		{
			if (event.getClickCount() == 2)
			{
				PopOver popOver = new PopOver();
				popOver.setDetachable(true);
				popOver.setDetachedTitle(Strings.MODULE_NAMES[module]);
				popOver.setDetached(true);
				popOver.setArrowLocation(ArrowLocation.TOP_CENTER);

				VBox content = new VBox();
				Label mainInfo = new Label(Strings.TOOLTIPS[module]);
				mainInfo.setMaxWidth(500);
				mainInfo.setWrapText(true);
				Node borderedInfo = Borders.wrap(mainInfo).lineBorder().buildAll();
				content.getChildren().add(borderedInfo);

				//Modul hat Parameter zum Darstellen
				if (Strings.PARAM_NAMES.length > module)
				{
					String[] paramNames = Strings.PARAM_NAMES[module];
					String[] paramDescriptions = Strings.PARAM_DESCRIPTIONS[module];
					for (int i = 0; i < paramNames.length; i++)
					{
						Node label = createTitledPane(paramNames[i], paramDescriptions[i]);
						content.getChildren().add(label);
					}
				}
				
				popOver.setContentNode(content);

				popOver.show(pane);
			}
		});

	}

	private Node createTitledPane(String title, String text) 
	{
		Label label = new Label(text);
		label.setWrapText(true);
		label.setMaxWidth(500);
		Node borderedLabel = Borders.wrap(label).etchedBorder().title(title).buildAll();
		return borderedLabel;
	}



	public int getCurrProgram()
	{
		return currProgram;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
