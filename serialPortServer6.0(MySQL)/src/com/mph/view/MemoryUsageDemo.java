package com.mph.view;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleInsets;

public class MemoryUsageDemo extends JPanel
{
	//total--direct    free--pitch
	public static double direct;
	public static double pitch;
	public static int i;
    class DataGenerator extends Timer implements ActionListener
    {

        public void actionPerformed(ActionEvent actionevent)
        {
        	double  l = pitch;
        	double  l1 = direct;
            addTotalObservation(l1);
            addFreeObservation(l);
        }

        DataGenerator(int i)
        {
            super(i, null);
            addActionListener(this);
        }
    }

    public MemoryUsageDemo(int i)
    {
        super(new BorderLayout());
        total = new XYSeries("Direction"); //先产生XYSeries 对象
        free = new XYSeries("Pitch");
        
        //实时收集数据
        XYSeriesCollection xySeriesCollection = new XYSeriesCollection();
        xySeriesCollection.addSeries(total);
        xySeriesCollection.addSeries(free);
        
        NumberAxis numberaxis1 = new NumberAxis("Serial");
        NumberAxis numberaxis = new NumberAxis("Degree");
        numberaxis1.setTickLabelFont(new Font("SansSerif", 0, 12));
        numberaxis.setTickLabelFont(new Font("SansSerif", 0, 12));
        numberaxis1.setLabelFont(new Font("SansSerif", 0, 14));
        numberaxis.setLabelFont(new Font("SansSerif", 0, 14));
        
        XYLineAndShapeRenderer xylineandshaperenderer = new XYLineAndShapeRenderer(true, false);
        xylineandshaperenderer.setSeriesPaint(0, Color.red);
        xylineandshaperenderer.setSeriesPaint(1, Color.green);
        xylineandshaperenderer.setSeriesStroke(0, new BasicStroke(3F, 0, 2));
        xylineandshaperenderer.setSeriesStroke(1, new BasicStroke(3F, 0, 2));
        
        //设置鼠标提示信息内容格式 （格式：xxx.xx度） 
        StandardXYToolTipGenerator tipGenerator = new StandardXYToolTipGenerator("{2} 度", new SimpleDateFormat(), NumberFormat.getNumberInstance()); 
        xylineandshaperenderer.setToolTipGenerator(tipGenerator); 
       
        XYPlot xyplot = new XYPlot(xySeriesCollection, numberaxis1, numberaxis, xylineandshaperenderer);
        xyplot.setBackgroundPaint(Color.lightGray);
        xyplot.setDomainGridlinePaint(Color.white);
        xyplot.setRangeGridlinePaint(Color.white);
        xyplot.setAxisOffset(new RectangleInsets(5D, 5D, 5D, 5D));
        
        numberaxis1.setAutoRange(true);
        numberaxis1.setLowerMargin(0.0D);
        numberaxis1.setUpperMargin(0.0D);
        numberaxis1.setTickLabelsVisible(true);
        numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
       
        JFreeChart jfreechart = new JFreeChart("Direct & Pitch", new Font("SansSerif", 1, 24), xyplot, true);
        jfreechart.setBackgroundPaint(Color.white);
      
        ChartPanel chartpanel = new ChartPanel(jfreechart, true);
        chartpanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4), BorderFactory.createLineBorder(Color.black)));
        add(chartpanel);
    }

    private void addTotalObservation(double d)
    {
        total.add(i, d);
    }

    private void addFreeObservation(double d)
    {
        free.add(i, d);
    }

    private XYSeries total;
    private XYSeries free;
}
