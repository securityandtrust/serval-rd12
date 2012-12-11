package lu.snt.serval.cloud.kevoree.sandbox;

import org.kevoree.annotation.*;
import org.kevoree.context.CounterHistoryMetric;
import org.kevoree.context.DurationHistoryMetric;
import org.kevoree.context.Metric;
import org.kevoree.context.PutHelper;
import org.kevoree.framework.AbstractComponentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
@Provides({
        @ProvidedPort(name = "trigger", type = PortType.MESSAGE)
})
public class LatencyComponent extends AbstractComponentType implements Runnable {

    private ScheduledExecutorService p = null;
    private LinkedList<Integer> fifo = new LinkedList<Integer>();
    private Metric met = null;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Start
    public void start() throws Exception {
        p = Executors.newScheduledThreadPool(1);
        met = PutHelper.getMetric(getModelService().getContextModel(), getDictionary().get("Context").toString(), PutHelper.getParam().setMetricTypeClazzName(DurationHistoryMetric.class.getName()).setNumber(1000));
        p.scheduleAtFixedRate(this, 0, Integer.parseInt(getDictionary().get("period").toString()), TimeUnit.MILLISECONDS);
    }

    @Stop
    public void stop() throws Exception {
        p.shutdownNow();
        p = null;
    }

    @Port(name = "trigger")
    public void triggerCalled() {
        fillStack();
    }

    public void fillStack() {
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
        } catch (Exception e) {
            logger.error("error while putting context in KevC model", e);
        }
    }

}

