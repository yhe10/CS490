
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

public class Module3
{
    RealTimeChart rtc = null;
    
    public void start(String target, String oid, int type)
    {
        String[] types = {"Data Received", "Data Send"}; 
	JFrame frame = new JFrame("Real Time Data Usage"); 
	RealTimeChart rtcp = new RealTimeChart("Data", types[type], "bytes",target,oid); 
        frame.getContentPane().add(rtcp, new BorderLayout().CENTER); 
	frame.pack(); 
	frame.setVisible(true); 
	(new Thread(rtcp)).start();
        
	frame.addWindowListener(new WindowAdapter()
        { 
            public void windowClosing(WindowEvent windowevent)
            { 
                rtcp.Stop();
            }    
        });
    }
}
