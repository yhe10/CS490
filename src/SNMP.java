import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;

public class SNMP
{
    Module1 module1;
    Module2 module2;
    Module3 module3;
    JLabel label_devices, label_monitor, label_data, label_trap;
    JFrame f;
    JList devicesList, deviceDetail;
    DefaultListModel dlm;
    String[] devices;
    String[] details = {"IP: ","System Description: ","System Up Time: ","Memory: ","Speed: "};
    JButton refresh, monitor;
    JComboBox monitorMode, statusMode;
    ArpAttack detect;
    
    private void getDevices()
    {
        module1.updateDevices();
        devices = module1.getDevices();
        
        if(devices.length >= dlm.size())
        {
            int delta = devices.length - dlm.size();
            for(int i=0;i<delta;i++)
            {
                dlm.addElement("");
            }
        }
        else
        {
            dlm.removeRange(devices.length, dlm.size()-1);
        }
        
        int i=0;
        for(String s:devices)
        {
            dlm.set(i++,s);
        }
        
        devicesList.setEnabled(false);
        devicesList.setEnabled(true);
    }
    
    private void getDetail(String target)
    {
        details = new String[]{"IP: ","Hardware: ","Software: ","System Up Time: ","Memory: ","Speed: "};
        
        module1.updateDetails(target);
        String[] result = module1.getDetails(target);
        details[0] += result[0];
        details[1] += result[1];
        details[2] += result[2];
        details[3] += result[3];
        details[4] += result[4];
        details[5] += result[5];
    }
    
    public void init()
    {
        f.setExtendedState(JFrame.MAXIMIZED_BOTH);
        f.setLayout(null);
        f.addWindowListener(new WindowAdapter()
        { 
            @Override
            public void windowClosing(WindowEvent windowevent)
            { 
                System.exit(0); 
            }    
        });
        
        devicesList.setVisible(true);
        devicesList.setBounds(50, 90, 200, 400);
        devicesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        devicesList.addListSelectionListener((ListSelectionEvent e) -> {
            devicesList.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            getDetail(devices[devicesList.getSelectedIndex()]);
            deviceDetail.setListData(details);
            showComponents();
            devicesList.setCursor(Cursor.getDefaultCursor());
        });
        
        deviceDetail.setBounds(260, 90, 800, 400);
        deviceDetail.setVisible(false);
        
        label_devices.setVisible(true);
        label_devices.setBounds(50, 70, 200, 15);
        
        label_monitor.setBounds(1070, 70, 220, 15);

        label_data.setBounds(1070,100,80,20);
        
        label_trap.setBounds(1070,160,80,20);
              
        refresh.setVisible(true);
        refresh.setBounds(50, 500, 80, 20);
        refresh.addActionListener((ActionEvent e) -> {
            refresh.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            getDevices();
            if(devices[0].equals("No device"))
            {
                devicesList.setEnabled(false);
                hideComponents();
            }
            else
            {
                devicesList.setEnabled(true);
            }
            refresh.setCursor(Cursor.getDefaultCursor());
        });
        
        monitor.setBounds(1070,240,80,20);
        monitor.addActionListener((ActionEvent e) -> {
            switch(monitorMode.getSelectedIndex())
            {
                case 0:module3.start(details[0].substring(4),".1.3.6.1.2.1.7.1",0);break;
                case 1:module3.start(details[0].substring(4),".1.3.6.1.2.1.7.4",1);break;
                default:break;
            }
            
            if(statusMode.getSelectedIndex() == 0)
            {
                module2 = new Module2();
                module2.start();
            }
        });
        
        monitorMode.addItem("Received");
        monitorMode.addItem("Send");
        monitorMode.addItem("None");
        monitorMode.setBounds(1070, 120, 100, 20);
        
        statusMode.addItem("Enable");
        statusMode.addItem("Disable");
        statusMode.setBounds(1070, 180, 100, 20);
        
        f.setVisible(true);
        f.add(devicesList);
        f.add(deviceDetail);
        f.add(label_devices);
        f.add(label_monitor);
        f.add(label_data);
        f.add(label_trap);
        f.add(refresh);
        f.add(monitor);
        f.add(monitorMode);
        f.add(statusMode);
        (new Thread(detect)).start();
    }
    
    public SNMP()
    {
        module1 = new Module1();
        module3 = new Module3();
        f = new JFrame("Network Management System");
        dlm = new DefaultListModel<String>();
        dlm.addElement("No device");
        devicesList = new JList(dlm);
        devicesList.setEnabled(false);
        deviceDetail = new JList(details);
        label_devices = new JLabel("Currently connected devices:");
        label_monitor = new JLabel("Device Monitor:");
        label_data = new JLabel("Data Usage");
        label_trap = new JLabel("Device Status");
        refresh = new JButton("Refresh");
        monitor = new JButton("Monitor");
        monitorMode = new JComboBox();
        statusMode = new JComboBox();
        detect = new ArpAttack();
        
        hideComponents();
    }
    
    private void hideComponents()
    {
        deviceDetail.setVisible(false);
        label_monitor.setVisible(false);
        label_data.setVisible(false);
        label_trap.setVisible(false);
        monitor.setVisible(false);
        monitorMode.setVisible(false);
        statusMode.setVisible(false);
    }
    
    private void showComponents()
    {
        deviceDetail.setVisible(true);
        label_monitor.setVisible(true);
        label_data.setVisible(true);
        label_trap.setVisible(true);
        monitor.setVisible(true);
        monitorMode.setVisible(true);
        statusMode.setVisible(true);
    }
    
    public static void main(String[] args)
    {
        SNMP program = new SNMP();
        program.init();
    }
}