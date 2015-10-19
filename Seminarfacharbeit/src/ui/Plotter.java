package ui;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import javafx.animation.AnimationTimer;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TitledPane;
import modules.SampleFilter;
import containers.StandardModuleContainer;
import engine.SynthesizerEngine;

public class Plotter extends TitledPane
{

	private static final int MAX_DATA_POINTS = 1000;

	private SampleFilter sampleFilter;

	private int xSeriesData = 0;
	private XYChart.Series<Number, Number> series = new XYChart.Series<>();

	private ExecutorService executor;
	private ConcurrentLinkedQueue<Number> dataQ = new ConcurrentLinkedQueue<>();

	private NumberAxis xAxis;

	public Plotter(SynthesizerEngine engine)
	{
		this.sampleFilter = ((StandardModuleContainer) engine.getAllContainer()).getSampleFilter();

		xAxis = new NumberAxis(0, MAX_DATA_POINTS, 1000);
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

		setContent(lineChart);

		executor = Executors.newCachedThreadPool(new ThreadFactory() 
		{
			@Override
			public Thread newThread(Runnable r) {
				Thread thread = new Thread(r);
				thread.setDaemon(true);
				return thread;
			}
		});

		AddToQueue addToQueue = new AddToQueue();
		executor.execute(addToQueue);

		prepareTimeline();

		setText("Oszilloskop");
		setCollapsible(false);

	}


	private class AddToQueue implements Runnable {
		public void run() 
		{
			List<Short> dataList = sampleFilter.getBufferList();
			for (short sample:dataList)
				dataQ.add(sample);

//			try {
//				Thread.sleep(10);
//			} 
//			catch (InterruptedException e) 
//			{
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			executor.execute(this);
		}
	}

	//-- Timeline gets called in the JavaFX Main thread
	private void prepareTimeline() {
		// Every frame to take any data from queue and add to chart
		new AnimationTimer() {
			@Override
			public void handle(long now) {
				addDataToSeries();
			}
		}.start();
	}

	private void addDataToSeries() {
		for (int i = 0; i < 1000; i++) { //-- add 20 numbers to the plot+
			if (dataQ.isEmpty()) break;
			XYChart.Data<Number, Number> data = new XYChart.Data<Number, Number>(xSeriesData++, dataQ.remove());
			series.getData().add(data);

		}
		// remove points to keep us at no more than MAX_DATA_POINTS
		if (series.getData().size() > MAX_DATA_POINTS) {
			series.getData().remove(0, series.getData().size() - MAX_DATA_POINTS);
		}

		// update
		xAxis.setLowerBound(xSeriesData - MAX_DATA_POINTS);
		xAxis.setUpperBound(xSeriesData - 1);
	}


}
