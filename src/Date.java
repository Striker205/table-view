import java.time.Year;
import java.time.YearMonth;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.TrayIcon.MessageType;
import java.net.MalformedURLException;

import java.util.ArrayList;


public class Date extends JTable {

    public static boolean clicked = false;
    ArrayList<Integer> line = new ArrayList<Integer>();

    public int CheckYear(JTable table, int row) { 
        
        if((table.getValueAt(row, 7)==null) || (table.getValueAt(row, 7)=="")){
            return -1;
        }
        
        try {
            DefaultTableModel model = (DefaultTableModel)table.getModel();
            String date = table.getValueAt(row, 7).toString();

            String y = model.getValueAt(row, 3).toString();
            int year = Integer.parseInt(y);
    
            Year yearo = Year.of(year);
            Year yearn = Year.now();


            String[] ss = date.split("/");
            int yea = Integer.parseInt(ss[1]);
            int mon = Integer.parseInt(ss[0]);


            YearMonth dateOld = YearMonth.of(yea, mon);
            YearMonth dateNow = YearMonth.now();
            
            if(dateOld.isBefore(dateNow)) {   
                
                if(yearn.minusYears(3).equals(yearo)) {

                    if(clicked && table.getSelectedRow() == row) {
                        model.setValueAt("", row, 8);
                        model.setValueAt("", row, 7);
                        model.setValueAt(dateOld.getMonthValue() + "/" + (yea+1), row, 7);
                        clicked = false;    
                        return -1;
                    }
                   
                    /*******Notification*******/
                    if(!line.contains(row)) {
                        line.add(row);
                        if (SystemTray.isSupported()) {
                            try{
                                displayTray(table, row);
                            }catch(AWTException ex) { }
                            catch(MalformedURLException ex) { }
                        }
                    }

                    model.setValueAt("3-roč", row, 8);
                            
                    return  3;

                }else {

                    if(clicked && table.getSelectedRow() == row) {
                        model.setValueAt("", row, 8);
                        model.setValueAt("", row, 7);
                        model.setValueAt(dateOld.getMonthValue() + "/" + (yea+1), row, 7);
                        clicked = false;
                        return -1;
                    }

                    model.setValueAt("1-roč", row, 8);

                    return  1;

                }                
            }else {
                model.setValueAt("", row, 8);
            }
        } catch (Exception e) {
            //TODO: handle exception
        } 
       
        return -1;
    }

    public void displayTray(JTable table, int row) throws AWTException, MalformedURLException {
        //Obtain only one instance of the SystemTray object
        SystemTray tray = SystemTray.getSystemTray();

        //If the icon is a file
        Image image = Toolkit.getDefaultToolkit().createImage("icon.png");

        TrayIcon trayIcon = new TrayIcon(image, "Java AWT Tray Demo");
        //Let the system resize the image if needed
        trayIcon.setImageAutoSize(true);
        //Set tooltip text for the tray icon
        trayIcon.setToolTip("System tray icon demo");
        tray.add(trayIcon);

        trayIcon.displayMessage(table.getValueAt(row, 1).toString() + " " + table.getValueAt(row, 2), "3. ročná záruka", MessageType.INFO);
    }
}
