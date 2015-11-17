package ui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.sound.sampled.LineUnavailableException;

import modules.Ids;

import org.controlsfx.control.NotificationPane;
import org.controlsfx.control.action.Action;

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
			oscillatorView.setTooltip(new Tooltip(Strings.OSCILLATOR_TOOLTIP_STRING));
			controller.setMainPane(loader);

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
			filterView.setTooltip(new Tooltip(Strings.LOWPASS_TOOLTIP_STRING));
			controller.init();

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
			envelopeView.setTooltip(new Tooltip(Strings.ENVELOPE_TOOLTIP_STRING));
			envelopeView.setText("Hüllkurve - Tiefpassfilter");
			controller.init();

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
			envelopeView.setTooltip(new Tooltip(Strings.ENVELOPE_TOOLTIP_STRING));
			controller.init();

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
		plotter.setTooltip(new Tooltip(Strings.PLOTTER_TOOLTIP_STRING));
		mainLayout.getChildren().add(plotter);
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
			volumeView.setTooltip(new Tooltip(Strings.VOLUME_TOOLTIP_STRING));
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

	public int getCurrProgram()
	{
		return currProgram;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
