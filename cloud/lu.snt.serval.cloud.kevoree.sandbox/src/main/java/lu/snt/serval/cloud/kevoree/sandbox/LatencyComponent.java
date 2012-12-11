package lu.snt.serval.cloud.kevoree.sandbox;

import org.kevoree.annotation.*;
import org.kevoree.context.CounterHistoryMetric;
import org.kevoree.context.DurationHistoryMetric;
import org.kevoree.context.Metric;
import org.kevoree.context.PutHelper;
import org.kevoree.framework.AbstractComponentType;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: jmeira
 * Date: 11/12/12
 * Time: 09:04
 * To change this template use File | Settings | File Templates.
 */

@Library(name = "JavaSE")
@ComponentType
@DictionaryType({
        @DictionaryAttribute(name = "latencyValue", defaultValue = "10", optional = true),
        @DictionaryAttribute(name = "latencyPeakWindow", defaultValue = "10", optional = true),
        @DictionaryAttribute(name = "latencyPeak", defaultValue = "90", optional = true),
        @DictionaryAttribute(name = "latencyPlus", defaultValue = "10", optional = true),
        @DictionaryAttribute(name = "period", defaultValue = "2000", optional = true),
        @DictionaryAttribute(name = "Context")
})
public class LatencyComponent extends AbstractComponentType implements Runnable {

    private ScheduledExecutorService p = null;
    private LinkedList<Integer> fifo = new LinkedList<Integer>();
    private Frame frame;


    private Metric met = null;


    @Start
    public void start() throws Exception {
        frame = new MyFrame("on");
        frame.setVisible(true);
        p = Executors.newScheduledThreadPool(1);

        met = PutHelper.getMetric(getModelService().getContextModel(),getDictionary().get("Context").toString(),PutHelper.getParam().setMetricTypeClazzName(DurationHistoryMetric.class.getName()).setNumber(1000));

        p.scheduleAtFixedRate(this, 0, Integer.parseInt(getDictionary().get("period").toString()), TimeUnit.MILLISECONDS);
    }

    @Stop
    public void stop() throws Exception {
        p.shutdownNow();
        p = null;
    }


    public void fillStack(){
        int i, j = 0;

        fifo.clear();

        for (i = 0; i < 100; i += Integer.parseInt(getDictionary().get("latencyPlus").toString())) {
            fifo.offer(i);
            if (i >= Integer.parseInt(getDictionary().get("latencyPeak").toString())) {
                for (j = 0; j < Integer.parseInt(getDictionary().get("latencyPeakWindow").toString()); j++) {
                    fifo.offer(Integer.parseInt(getDictionary().get("latencyPeak").toString()));
                }
            }
        }


        for (i = Integer.parseInt(getDictionary().get("latencyPeak").toString()); i > Integer.parseInt(getDictionary().get("latencyValue").toString()); i -= Integer.parseInt(getDictionary().get("latencyPlus").toString())) {
            fifo.offer(i);
        }


    }


    @Override
    public void run() {
        try {
            if (fifo.isEmpty()) {
                PutHelper.addValue(met, String.valueOf(0));
            } else {
                System.out.println(fifo.peek());
                PutHelper.addValue(met, String.valueOf(fifo.poll()));
            }

        } catch (
                Exception e
                )

        {
            e.printStackTrace();
        }
    }

    private class MyFrame extends JFrame {

        private JButton on;

        private String onText;

        public MyFrame(final String onText) {

            this.onText = onText;
            on = new JButton(onText);
            on.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {

                    fillStack();
                }

            });

            ButtonGroup bg = new ButtonGroup();
            bg.add(on);


            setLayout(new FlowLayout());
            add(on);


            this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

            pack();
            setVisible(true);
        }


        @Override


        public void repaint() {
            on.setText(onText);
            super.repaint();
        }


        public final void setOnText(String onText) {
            this.onText = onText;
        }
    }

    public static void main(String[] args) throws Exception, InterruptedException {
        LatencyComponent l = new LatencyComponent();
        l.start();
    }
}

