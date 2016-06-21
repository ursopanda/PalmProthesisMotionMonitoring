package app.edi.palmprothesismotionmonitoring;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import java.util.List;
import java.util.Random;

public class GraphActivity extends AppCompatActivity {
        // Getting a refference to a DB to get Flexion's values to the table

        private static Random random = new Random();

        private static TimeSeries timeSeries;
        private static XYMultipleSeriesDataset dataset;
        private static XYMultipleSeriesRenderer renderer;
        private static XYSeriesRenderer rendererSeries;
        private static GraphicalView view;
        private static Thread mThread;


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            dataset = new XYMultipleSeriesDataset();

            renderer = new XYMultipleSeriesRenderer();
            renderer.setAxesColor(Color.BLUE);
            renderer.setAxisTitleTextSize(16);
            renderer.setChartTitle("Flexion Value Chart");
            renderer.setChartTitleTextSize(15);
            renderer.setFitLegend(true);
            renderer.setGridColor(Color.LTGRAY);
            renderer.setPanEnabled(true, true);
            renderer.setPointSize(10);
            renderer.setXTitle("");
            renderer.setYTitle("Flexion Angle");
            renderer.setMargins( new int []{20, 30, 15, 0});
            renderer.setZoomButtonsVisible(true);
            renderer.setBarSpacing(10);
            renderer.setShowGrid(true);


            rendererSeries = new XYSeriesRenderer();
            rendererSeries.setColor(Color.RED);
            renderer.addSeriesRenderer(rendererSeries);
            rendererSeries.setFillPoints(true);
            rendererSeries.setPointStyle(PointStyle.CIRCLE);

            timeSeries = new TimeSeries("Flexion");
            // Use it to clean Database
            //db.deleteFlexionStats();
//            final List<FlexionStats> flexionStats = db.getAllFlexionStats();
            mThread = new Thread(){
                public void run(){
                    while(true){
                        try {
                            Thread.sleep(2000L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //implement getFlexionValue() method in here
//                        List<FlexionStats> flexionStatsList = db.getAllFlexionStats();
//                        for (int i=0; i < flexionStatsList.size(); i++) {
//                            timeSeries.add(i,flexionStatsList.get(i).getFlexion_value());
//                        }
                        //timeSeries.add(new Date(), random.nextInt(10));
                        for (int i = 0; i < 10; i++) {
                            timeSeries.add(i, i * 10);
                        }
                        view.repaint();
                    }
                }
            };
            mThread.start();
        }

        @Override
        protected void onStart() {
            super.onStart();
            dataset.addSeries(timeSeries);
            view = ChartFactory.getTimeChartView(this, dataset, renderer, "Test");
            view.refreshDrawableState();
            view.repaint();
            setContentView(view);
        }
    }
