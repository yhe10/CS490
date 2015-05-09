import org.jfree.chart.*;
import org.jfree.chart.axis.ValueAxis; 
import org.jfree.chart.plot.XYPlot; 
import org.jfree.data.time.*;
import java.io.*;

public class RealTimeChart extends ChartPanel implements Runnable
{ 
    private static final long serialVersionUID = 1L; 
    private static TimeSeries timeSeries; 
    private final String target;
    private final String oid;
    private int lastValue;
    Module1 get = new Module1();
    private boolean valid = true;

    public RealTimeChart(String chartContent, String title, String yaxisName, String target, String oid)
    { 
	super(createChart(chartContent, title, yaxisName)); 
        this.target = target;
        this.oid = oid;
    } 

    @SuppressWarnings("deprecation") 
    private static JFreeChart createChart(String chartContent, String title, String yaxisName)
    { 
        timeSeries = new TimeSeries(chartContent); 
	TimeSeriesCollection timeseriescollection = new TimeSeriesCollection(timeSeries); 
	JFreeChart jfreechart = ChartFactory.createTimeSeriesChart(title, "time", yaxisName, timeseriescollection, true, true, false); 
	XYPlot xyplot = jfreechart.getXYPlot(); 
        ValueAxis valueaxis = xyplot.getDomainAxis(); 
        valueaxis.setAutoRange(true); 
	valueaxis.setFixedAutoRange(30000D);    

	return jfreechart; 
    } 

    @Override
    public void run()
    { 
        lastValue = Value();

	while (valid)
        { 
            try
            {
                Thread.sleep(2000);
                int temp = Value();
                int delta = temp - lastValue;
                timeSeries.add(new Second(), delta);
                lastValue = temp;
                
                try
                {
                    FileWriter fw = new FileWriter("data.txt",true);
                    BufferedWriter bw = new BufferedWriter(fw);
                    bw.append(Integer.toString(delta));
                    bw.newLine();
                    bw.close();
                    fw.close();
                }
                catch(IOException e){}
            }
            catch (InterruptedException e){}
        }
    } 

    public void Stop()
    {
        valid = false;
    }
    
    private int Value()
    { 
        try
        {
            return Integer.parseInt(get.SnmpGetNext(target, oid));
        }
        catch(NumberFormatException e)
        {
            return -1;
        }
    }
}