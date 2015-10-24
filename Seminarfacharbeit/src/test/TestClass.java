package test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.animation.AnimationTimer;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class TestClass extends Application
{
	
    private static final int MAX_DATA_POINTS = 2000;

    private Series series;
    private int xSeriesData = 0;
    private ConcurrentLinkedQueue<Number> dataQ = new ConcurrentLinkedQueue<Number>();
    private ExecutorService executor;
    private AddToQueue addToQueue;
    private Timeline timeline2;
    private NumberAxis xAxis;

	private void init(Stage primaryStage) {
		xAxis = new NumberAxis(0,MAX_DATA_POINTS,MAX_DATA_POINTS/10);
		xAxis.setForceZeroInRange(false);
		xAxis.setAutoRanging(false);

		NumberAxis yAxis = new NumberAxis();
		yAxis.setAutoRanging(false);

		//-- Chart
		final AreaChart<Number, Number> sc = new AreaChart<Number, Number>(xAxis, yAxis) {
			// Override to remove symbols on each data point
			@Override protected void dataItemAdded(Series<Number, Number> series, int itemIndex, Data<Number, Number> item) {}
		};
		sc.setAnimated(false);
		sc.setId("liveAreaChart");
		sc.setTitle("Animated Area Chart");

		//-- Chart Series
		series = new AreaChart.Series<Number, Number>();
		series.setName("Area Chart Series");
		sc.getData().add(series);

		primaryStage.setScene(new Scene(sc));
	}

	@Override public void start(Stage primaryStage) throws Exception {
		init(primaryStage);
		primaryStage.show();

		//-- Prepare Executor Services
		executor = Executors.newCachedThreadPool();
		addToQueue = new AddToQueue();
		executor.execute(addToQueue);
		//-- Prepare Timeline
		prepareTimeline();
	}

	public static void main(String[] args) {
		launch(args);
	}

	private class AddToQueue implements Runnable {
		public void run() {
			try {
				// add a item of random data to queue
				for (int i = 0; i < 200; i++)
					dataQ.add(Math.random());
				Thread.sleep(50);
				executor.execute(this);
			} catch (InterruptedException ex) {
				Logger.getLogger(TestClass.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	//-- Timeline gets called in the JavaFX Main thread
	private void prepareTimeline() {
		// Every frame to take any data from queue and add to chart
		new AnimationTimer() {
			@Override public void handle(long now) {
				addDataToSeries();
			}
		}.start();
	}

	private void addDataToSeries() {
		for (int i = 0; i < 1000; i++) { //-- add 20 numbers to the plot+
			if (dataQ.isEmpty()) break;
			series.getData().add(new LineChart.Data<Number, Number>(xSeriesData++, dataQ.remove()));
		}
		// remove points to keep us at no more than MAX_DATA_POINTS
		if (series.getData().size() > MAX_DATA_POINTS) {
			series.getData().remove(0, series.getData().size() - MAX_DATA_POINTS);
		}
		// update 
		xAxis.setLowerBound(xSeriesData-MAX_DATA_POINTS);
		xAxis.setUpperBound(xSeriesData-1);
	}
}



class Plotter extends Pane
{
	private int maxDataPoints = 1000;

	private int xSeriesData = 0;
	private XYChart.Series<Number, Number> series = new XYChart.Series<>();
	private ConcurrentLinkedQueue<Number> dataQ = new ConcurrentLinkedQueue<>();

	private NumberAxis xAxis;

	public Plotter()
	{
		xAxis = new NumberAxis(0, maxDataPoints, 1000);
		xAxis.setForceZeroInRange(false);
		xAxis.setAutoRanging(false);
		xAxis.setTickLabelsVisible(false);
		xAxis.setTickMarkVisible(false);
		xAxis.setMinorTickVisible(false);

		NumberAxis yAxis = new NumberAxis();
		yAxis.setAutoRanging(false);
		yAxis.setUpperBound(Short.MAX_VALUE);
		yAxis.setLowerBound(Short.MIN_VALUE);
		yAxis.setTickUnit(10000);

		LineChart<Number, Number> lineChart = new LineChart<Number, Number>(xAxis, yAxis);
		lineChart.setCreateSymbols(false);

		lineChart.setAnimated(false);
		lineChart.setHorizontalGridLinesVisible(true);
		lineChart.setLegendVisible(false);

		lineChart.getData().add(series);

		prepareTimeline();

		getChildren().add(lineChart);
	}

	private void prepareTimeline() 
	{
		new AnimationTimer() 
		{
			@Override
			public void handle(long now) 
			{
				AddToQueue task = new AddToQueue();
				Thread thread = new Thread(task);
				thread.setDaemon(true);
				thread.start();
				PlotUpdater updater = new PlotUpdater();
				Thread updateThread = new Thread(updater);
				updateThread.setDaemon(true);
				updateThread.start();
			}
		}.start();
	}

	private class AddToQueue extends Task<Void>
	{
		@Override
		protected Void call() throws Exception 
		{
			//Receive data here
			Random random = new Random();
			List<Short> dataList = new ArrayList<Short>();
			for (int i = 0; i < 1000; i++)
			{
				dataList.add((short) random.nextInt(Short.MAX_VALUE));
			}

			for (short sample:dataList)
				dataQ.add(sample);

			return null;
		}	
	}

	private class PlotUpdater extends Task<Void>
	{
		@Override
		protected Void call() throws Exception 
		{
			for (int i = 0; i < dataQ.size(); i++) 
			{
				if (dataQ.isEmpty()) 
					break;

				Platform.runLater(new AddToPlot());
			}
			return null;
		}	
	}

	private class AddToPlot implements Runnable
	{
		public void run() 
		{
			if (!dataQ.isEmpty())
			{
				XYChart.Data<Number, Number> data = new XYChart.Data<Number, Number>(xSeriesData++, dataQ.remove());
				series.getData().add(data);
			}

			if (series.getData().size() > maxDataPoints) 
			{
				series.getData().remove(0, series.getData().size() - maxDataPoints);
			}

			xAxis.setLowerBound(xSeriesData - maxDataPoints);
			xAxis.setUpperBound(xSeriesData - 1);
		}
	}
}
