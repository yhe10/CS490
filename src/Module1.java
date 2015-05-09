import com.adventnet.snmp.snmp2.*;
import com.adventnet.snmp.beans.SnmpTarget;
import java.util.*;
import java.io.*;

public class Module1
{
    ArrayList<String> devices;
    ArrayList<String[]> details;
    SnmpTarget target;
    
    public Module1()
    {
        devices = new ArrayList();
        details = new ArrayList();
        target = new SnmpTarget();
        target.setCommunity("Public");
        target.setSnmpVersion(SnmpAPI.SNMP_VERSION_2C);
    }
    
    public void updateDevices()
    {
        devices.clear();
        details.clear();
        String[] IPs = {"192.168.0.2","192.168.0.3","192.168.0.4","192.168.0.5"};
        

        for(String IP : IPs)
        {
            String temp = SnmpGet(IP,".1.3.6.1.2.1.1.5.0");

            try
            {
                if(!(temp.equals("null")))
                {
                    devices.add(temp);
                    String[] results = new String[6];
                    results[0] = IP;
                    details.add(results);
                }
            }
            catch(NullPointerException e){}
        }
        
        if(devices.isEmpty())
        {
            devices.add("No device");
        }
    }
    
    public String SnmpGet(String IP, String OID)
    {
        target.setTargetHost(IP);
        target.setObjectID(OID);
        return target.snmpGet();
    }
    
    public String SnmpGetNext(String IP, String OID)
    {
        target.setTargetHost(IP);
        target.setObjectID(OID);
        return target.snmpGetNext();
    }
    
    public void updateDetails(String target)
    {
        int index = devices.indexOf(target);
        String IP = (details.get(index))[0];
        String descriptions[] = SnmpGet(IP,".1.3.6.1.2.1.1.1.0").split("Software: ");
        details.get(index)[1]=descriptions[0].substring(10);
        details.get(index)[2]=descriptions[1];
        details.get(index)[3]=SnmpGet(IP,".1.3.6.1.2.1.1.3.0");
        details.get(index)[4]=String.valueOf(Double.parseDouble(SnmpGet(IP,".1.3.6.1.2.1.25.2.2.0"))/1024/1024).substring(0, 4)+"GB";
        details.get(index)[5]=SnmpGetNext(IP,".1.3.6.1.2.1.2.2.1.5")+"bps";
    }
    
    public String[] getDevices()
    {
        String[] devices = new String[this.devices.size()];
        Iterator<String> itr = this.devices.iterator();
        int i=0;
        
        while(itr.hasNext())
        {
            devices[i++] = itr.next();
        }
        
        return devices;
    }
    
    public String[] getDetails(String target)
    {
        int i = devices.indexOf(target);
        return details.get(i);
    }
}