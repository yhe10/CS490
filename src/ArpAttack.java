
import java.util.logging.Level;
import java.util.logging.Logger;



public class ArpAttack implements Runnable
{
    Module1 data = new Module1();
    int lastvalue;
    
    @Override
    public void run()
    {
        lastvalue = value();
        
        while(true)
        {
            try
            {
                Thread.sleep(400);
            }
            catch (InterruptedException ex)
            {
                Logger.getLogger(ArpAttack.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            int temp = value();
            if((temp-lastvalue) > 40)
            {
                System.out.println("Potential ARP Spoofing!");
            }
            lastvalue = temp;
        }
    }
    
    private int value()
    {
        return Integer.parseInt(data.SnmpGetNext("192.168.0.2", ".1.3.6.1.2.1.7.1"));
    }
}
