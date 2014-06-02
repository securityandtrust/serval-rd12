package lu.snt.iot.zephir.bh3.frame;/*
* Author : Gregory Nain (developer.name@uni.lu)
* Date : 21/12/12
* (c) 2012 University of Luxembourg – Interdisciplinary Centre for Security Reliability and Trust (SnT)
* All rights reserved
*/

import lu.snt.iot.zephyr.bh3.api.data.BreathWaveformRecord;
import lu.snt.iot.zephyr.bh3.api.data.EcgWaveformRecord;
import lu.snt.iot.zephyr.bh3.api.periodic.BreathingWaveformDataPacket;
import lu.snt.iot.zephyr.bh3.api.periodic.ECGWaveformDataPacket;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

import javax.swing.*;
import java.util.Calendar;
import java.util.List;

public class Ploter extends JPanel {

    private static final long MILIS_IN_A_DAY = 1000 * 60 * 60 * 24;
    private static final long SEC_IN_A_DAY = 60 * 60 * 24;
    private static final long MIN_IN_A_DAY = 60 * 24;

    TimeSeriesCollection dataset = new TimeSeriesCollection();
    TimeSeries ECG = new TimeSeries("ECG");
    TimeSeries breath = new TimeSeries("Breathing");
    JFreeChart chart;

    
    public Ploter() {
        dataset.addSeries(ECG);
        dataset.addSeries(breath);
        chart = createChart();
        ChartPanel chartPanel = new ChartPanel(chart);
        this.add(chartPanel);
    }

    public void updateECG(ECGWaveformDataPacket ecgPacket) {
        System.out.println("Ploter Update");
        Calendar.getInstance().set(ecgPacket.getYear(), ecgPacket.getMonth(), ecgPacket.getDay(),0,0,0);

        for(EcgWaveformRecord rc : ecgPacket.getRecords()) {
            Calendar.getInstance().roll(Calendar.MILLISECOND,(int)(ecgPacket.getBaseRecordTimeInDay() + rc.getTimeFromBaseTimestamp()));
            ECG.add(
                    new Millisecond(Calendar.getInstance().getTime()),
                    rc.getValue());
        }
    }

    public void updateBreath(BreathingWaveformDataPacket ecgPacket) {
        System.out.println("Ploter Update");
        Calendar.getInstance().set(ecgPacket.getYear(), ecgPacket.getMonth(), ecgPacket.getDay(),0,0,0);

        for(BreathWaveformRecord rc : ecgPacket.getRecords()) {
            Calendar.getInstance().roll(Calendar.MILLISECOND,(int)(ecgPacket.getBaseRecordTimeInDay() + rc.getTimeFromBaseTimestamp()));
            breath.add(
                    new Millisecond(Calendar.getInstance().getTime()),
                    rc.getValue());
        }
    }

    private JFreeChart createChart() {

        final JFreeChart result = ChartFactory.createTimeSeriesChart(
                "Dynamic Data Demo",
                "Time",
                "Value",
                dataset,
                true,
                true,
                false
        );
        final XYPlot plot = result.getXYPlot();
        ValueAxis axis = plot.getDomainAxis();
        axis.setAutoRange(true);
        axis.setFixedAutoRange(60000.0);  // 60 seconds
        axis = plot.getRangeAxis();
        axis.setRange(0.0, 1024.0);
        return result;

    }



}
