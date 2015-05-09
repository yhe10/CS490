import com.adventnet.snmp.beans.*;
import com.adventnet.snmp.snmp2.SnmpException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;

public class Module2 implements TrapListener
{
    SnmpTrapReceiver receiver;
    JFrame f;
    JTextArea notifications;
    
    public Module2()
    {
        receiver = new SnmpTrapReceiver();
        f = new JFrame("Device Status");
        f.addWindowListener(new WindowAdapter()
        { 
            public void windowClosing(WindowEvent windowevent)
            { 
                stop();
            }    
        });
        f.setBounds(200,80,400,600);
        notifications = new JTextArea();
        notifications.setLineWrap(true);
        notifications.setEditable(false);
        f.add(notifications);
        f.setVisible(true);
    }
    
    public void start()
    {
        receiver.addTrapListener(this);
        receiver.setPort(162);
        receiver.setCommunity("Public");
        notifications.append("Waiting to receive traps .......\n");
    }
    
    public void stop()
    {
        receiver.releaseResources();
        receiver.stop();
    }
    
    @Override
    public void receivedTrap(TrapEvent e)
    {
        SimpleDateFormat date = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String time = date.format(new Date());
        String port = e.getVariable(0);
        
        switch(e.getTrapType())
        {
            case 0: notifications.append(time + "" + e.getRemoteHost() + " cold start\n");break;
            case 1: notifications.append(time + "" +e.getRemoteHost() + " warm start\n");break;
            case 2: notifications.append(time + "" + e.getRemoteHost() + "" + port + " down\n");break;
            case 3: notifications.append(time + "" + e.getRemoteHost() + "" + port + " link up\n");break;
            case 4: notifications.append(time + "" + e.getRemoteHost() + " authentication failure\n");break;
            case 5: notifications.append(time + "" + e.getRemoteHost() + " lose neighbor\n");break;
            default: break;
        }
    }
}
