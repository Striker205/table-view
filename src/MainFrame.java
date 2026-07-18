import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.awt.event.MouseAdapter;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.*;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.time.Year;
import java.time.YearMonth;
import java.util.Timer;
import java.util.TimerTask;

import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;


public class MainFrame {
    String userHome = System.getProperty("user.home");
    File file = null;
    FileChannel lockChannel = null;
    FileLock lock = null;

    private javax.swing.JScrollPane jScrollPane1;
    private static javax.swing.JTable jTable1;

    JFrame frame = new JFrame();
    UpdateTable upWin = new UpdateTable();
    Date date = new Date();
    JButton btnExport = new JButton("Export");
    JButton btnImport = new JButton("Import");


    public void checkWindow() {
        try {
            file = new File(userHome, "my.lock");
            if (file.exists())
                file.delete();
            FileOutputStream lockFileOS = new FileOutputStream(file);
            lockFileOS.close();
            lockChannel = new RandomAccessFile(file,"rw").getChannel();
            lock = lockChannel.tryLock();
            if (lock==null) throw new Exception("Unable to obtain lock");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame,"Program je spustený");
            e.printStackTrace();
            System.exit(0);
        }
    }


    public static void AddRow(Object[] data) { 
        DefaultTableModel model = (DefaultTableModel)jTable1.getModel();
        model.addRow(data);
    }

    public void RemoveRow(int row) {
        ((DefaultTableModel)jTable1.getModel()).removeRow(row);
    }  


    private static class HeaderRenderer implements TableCellRenderer {

        DefaultTableCellRenderer renderer;
    
        public HeaderRenderer(JTable table) {
            renderer = (DefaultTableCellRenderer)table.getTableHeader().getDefaultRenderer();
            renderer.setHorizontalAlignment(JLabel.LEFT);
        }
    
        @Override
        public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int col) {
            return renderer.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, col);
        }
    }

    public void Tablee() {
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "CE- č.p.", "Ulica", "číslo vchodu", "rok vyroby", "mesiac", "EZ-EM", "p.č.", "záruka", ""
            }
        ));
        
        final DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setBorder(null);
        jTable1.getTableHeader().setDefaultRenderer(renderer);
        jTable1.setDefaultRenderer(Object.class, renderer);

        jTable1.setFocusable(false);
        jTable1.setOpaque(false);
        jTable1.getTableHeader().setReorderingAllowed(false);
        jTable1.setForeground(new Color(217, 217, 217));
        jTable1.setBackground(new Color(32, 136, 203));
        jTable1.setRowHeight(30);
        jTable1.setFont(new Font("Sergoe UI", Font.BOLD, 13));

        jTable1.setIntercellSpacing(new Dimension(0, 0));

        jTable1.getTableHeader().setDefaultRenderer(new HeaderRenderer(jTable1));
        jTable1.getTableHeader().setBorder(BorderFactory.createLineBorder(new Color(32, 136, 203)));
    
        JPanel panel = new JPanel();
        panel.setBackground(new Color(32, 136, 203));    
        jScrollPane1.setCorner(JScrollPane.UPPER_RIGHT_CORNER, panel);
        
        jTable1.getTableHeader().setPreferredSize(new Dimension(1, 30));
        jTable1.getTableHeader().setEnabled(false);
        //Columns width
        jTable1.getColumnModel().getColumn(0).setPreferredWidth(30);
        jTable1.getColumnModel().getColumn(1).setPreferredWidth(140);
        jTable1.getColumnModel().getColumn(2).setPreferredWidth(15);
        jTable1.getColumnModel().getColumn(3).setPreferredWidth(10);
        jTable1.getColumnModel().getColumn(4).setPreferredWidth(20);
        jTable1.getColumnModel().getColumn(5).setPreferredWidth(70);
        jTable1.getColumnModel().getColumn(6).setPreferredWidth(20);
        jTable1.getColumnModel().getColumn(7).setPreferredWidth(10);
        jTable1.getColumnModel().getColumn(8).setPreferredWidth(10);
    
        
        //Table row text center
        DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();
        centerRender.setHorizontalAlignment(JLabel.LEFT);
        for(int columnIndex = 0; columnIndex < jTable1.getColumnCount(); columnIndex++) {
            jTable1.getColumnModel().getColumn(columnIndex).setCellRenderer(centerRender);
        }

        jScrollPane1.setViewportView(jTable1); 
        jScrollPane1.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        jScrollPane1.setBackground(new Color(0x3D3D41));
        jScrollPane1.getViewport().setBackground(new Color(60, 60, 60));
    }

    class JComponentTableCellRenderer implements TableCellRenderer {

        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            return (JComponent) value;
        }
    }


    public  void AddExpiredDateToTable(int row) {
        try {
            DefaultTableModel model = (DefaultTableModel)jTable1.getModel(); 

            YearMonth dateNow = YearMonth.now();
            
            String year = model.getValueAt(row, 3).toString();
            String month = model.getValueAt(row, 4).toString();

            int yea = Integer.parseInt(year);
            int mon = Integer.parseInt(month);
                
            YearMonth dateOld = YearMonth.of(yea, mon);
            Year ynow = Year.now();
                

            if(dateOld.plusYears(3).isAfter(dateNow)){
                model.setValueAt("", row, 7);
                model.setValueAt(dateOld.getMonthValue() + "/" + (yea+3), row, 7);
            }else {    
                if(dateOld.getMonthValue() > dateNow.getMonthValue()) {
                    model.setValueAt("", row, 7);
                    model.setValueAt(dateOld.getMonthValue() + "/" + ynow.getValue(), row, 7);    
                } else {
                    model.setValueAt("", row, 7);
                    model.setValueAt(dateOld.getMonthValue() + "/" + ynow.plusYears(1), row, 7);
                }
            }
            
        } catch (Exception e) {
            //TODO: handle exception
        }
    } 


    Timer timer = new Timer();

    public void initialize() {  
        
        /**********************Create Table*******************************/  
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable() {
           
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, 
            int row, int column) 
            {
                Component c = super.prepareRenderer(renderer, row, column);
                
                if (date.CheckYear(jTable1, row) == 3) {
                    c.setBackground(new Color(255, 51, 0));
                    c.setForeground(Color.WHITE);
                }else if(date.CheckYear(jTable1, row) == 1) {
                    c.setBackground(new Color(255, 204, 0));
                    c.setForeground(Color.BLACK);
                }else {
                    c.setBackground(row%2==0 ? new Color(191, 191, 191) : new Color(230, 230, 230));
                    c.setForeground(Color.BLACK);
                }  

                /*
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }*/
               
                return c;
            }
        };
        Tablee();

        

        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                for(int row=0; row<jTable1.getRowCount(); row++){
                    date.CheckYear(jTable1, row);
                }
            }
            
        }, 50, 100000);




        /*************************Update Table******************************/
        DefaultTableModel model = (DefaultTableModel)jTable1.getModel(); 
        jTable1.addMouseListener(new MouseAdapter(){

            @Override
            public void mouseClicked(MouseEvent e) {
                    upWin.win.setVisible(true);
                    int idx = jTable1.getSelectedRow();

                    upWin.txt7.setText(model.getValueAt(idx, 0).toString());
                    upWin.txt1.setText(model.getValueAt(idx, 1).toString());
                    upWin.txt2.setText(model.getValueAt(idx, 2).toString());
                    upWin.txt3.setText(model.getValueAt(idx, 3).toString());
                    upWin.txt4.setText(model.getValueAt(idx, 4).toString());
                    upWin.txt5.setText(model.getValueAt(idx, 5).toString());
                    upWin.txt6.setText(model.getValueAt(idx, 6).toString());  
            }
        });   


        /*********************Create Buttons*************************/
        JButton btnUpdate = new JButton("Upraviť");
        btnUpdate.setPreferredSize(new Dimension(200, 200));
        btnUpdate.setBackground(Color.BLACK);
        btnUpdate.setForeground(new Color(0xa6a6a6));
        ImageIcon iconBtnUpdate = new ImageIcon(new ImageIcon("Obr/edit.png").getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT));
        btnUpdate.setIconTextGap(14);
        btnUpdate.setIcon(iconBtnUpdate);
        btnUpdate.setFocusable(false);
        btnUpdate.setBorder(BorderFactory.createEtchedBorder());
        btnUpdate.setBorderPainted(false);

        btnUpdate.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub        
                int i[] = jTable1.getSelectedRows();
                
                try {      
                    model.setValueAt(upWin.txt7.getText(), i[0], 0); 
                    model.setValueAt(upWin.txt1.getText(), i[0], 1);
                    model.setValueAt(upWin.txt2.getText(), i[0], 2);
                    model.setValueAt(upWin.txt3.getText(), i[0], 3);
                    model.setValueAt(upWin.txt4.getText(), i[0], 4);
                    model.setValueAt(upWin.txt5.getText(), i[0], 5);
                    model.setValueAt(upWin.txt6.getText(), i[0], 6);
                    JOptionPane.showMessageDialog(frame, "Success!");
                    upWin.closeWindow();
                    jTable1.clearSelection();
                    AddExpiredDateToTable(i[0]);
                }catch(Exception ex){
                    JOptionPane.showMessageDialog(frame, "Najprv zvolte riadok!");
                }       
            }
        });

        btnUpdate.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btnUpdate.setBackground(new Color(77, 77, 77));
            }
            
            public void mouseExited(MouseEvent evt) {
                btnUpdate.setBackground(Color.BLACK);
            }
        });


        JButton btnDelete = new JButton("Zmazať");
        btnDelete.setPreferredSize(new Dimension(200, 200));
        btnDelete.setBackground(Color.BLACK);
        btnDelete.setForeground(new Color(0xa6a6a6));
        ImageIcon iconBtnDelete = new ImageIcon(new ImageIcon("Obr/delete.png").getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT));
        btnDelete.setIconTextGap(15);
        btnDelete.setIcon(iconBtnDelete);
        btnDelete.setBorder(BorderFactory.createEtchedBorder());
        btnDelete.setFocusable(false);
        btnDelete.setBorderPainted(false);

        btnDelete.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                upWin.closeWindow();
                int j = jTable1.getSelectedRow();
                if(j >= 0) {
                    Object[] options = {"Áno", "Nie"};
                    int x = JOptionPane.showOptionDialog(frame, "Naozaj chcete vymazať tento riadok?", "Vymazanie riadku", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
                    if(x == 0) {
                        RemoveRow(j);
                        newWindow.count--;
                    }
                }else {
                    JOptionPane.showMessageDialog(frame, "Najprv zvolte riadok!");
                }
                jTable1.clearSelection();
            }
        });

        btnDelete.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btnDelete.setBackground(new Color(77, 77, 77));
            }
            
            public void mouseExited(MouseEvent evt) {
                btnDelete.setBackground(Color.BLACK);
            }
        });


        
        btnImport.setPreferredSize(new Dimension(200, 200));
        btnImport.setBackground(Color.BLACK);
        btnImport.setForeground(new Color(0xa6a6a6));
        ImageIcon iconBtnImport = new ImageIcon(new ImageIcon("Obr/import.png").getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT));
        btnImport.setIconTextGap(25);
        btnImport.setIcon(iconBtnImport);
        btnImport.setBorder(BorderFactory.createEtchedBorder());
        btnImport.setFocusable(false);
        btnImport.setBorderPainted(false);
        
        btnImport.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                if(e.getSource()==btnImport){
                    File file = new File("data.xls");               
                    try {
                        Import(file, jTable1);
                        newWindow.count = jTable1.getRowCount();
                        newWindow.count++;
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        JOptionPane.showMessageDialog(frame, "Error!");
                    }             
                }
            }      
        });

        btnImport.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btnImport.setBackground(new Color(77, 77, 77));
            }
            
            public void mouseExited(MouseEvent evt) {
                btnImport.setBackground(Color.BLACK);
            }
        });
        

        JButton btnAdd = new JButton("Pridať");
        btnAdd.setPreferredSize(new Dimension(200, 200));
        btnAdd.setBackground(Color.BLACK); //purple 0x472976
        btnAdd.setForeground(new Color(0xa6a6a6));
        ImageIcon iconBtnAdd = new ImageIcon(new ImageIcon("Obr/add.png").getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT));
        btnAdd.setIconTextGap(25);
        btnAdd.setIcon(iconBtnAdd);
        btnAdd.setBorder(BorderFactory.createEtchedBorder());
        btnAdd.setFocusable(false);
        btnAdd.setBorderPainted(false);
        
        btnAdd.addActionListener(new ActionListener() {
            
            newWindow addWin = null;

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                if(addWin == null) {
                    addWin = new newWindow();
                    addWin.openWindow();
                }
                addWin.win.setVisible(true);
            }     
        });

        btnAdd.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btnAdd.setBackground(new Color(77, 77, 77));
            }
            
            public void mouseExited(MouseEvent evt) {
                btnAdd.setBackground(Color.BLACK);
            }
        });


        JButton btnSave = new JButton("Uložiť");
        btnSave.setPreferredSize(new Dimension(200, 200));
        btnSave.setBackground(Color.BLACK);
        btnSave.setForeground(new Color(0xa6a6a6));
        ImageIcon iconBtnSave = new ImageIcon(new ImageIcon("Obr/save.png").getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT));
        btnSave.setIconTextGap(25);
        btnSave.setIcon(iconBtnSave);
        btnSave.setBorder(BorderFactory.createEtchedBorder());
        btnSave.setFocusable(false);
        btnSave.setBorderPainted(false);
        
        btnSave.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                if(e.getSource()==btnSave){
                    SaveData(jTable1); 
                    int num = newWindow.count;
                    num-=2;
                    AddExpiredDateToTable(num);  
                }
            }      
        });

        btnSave.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btnSave.setBackground(new Color(77, 77, 77));
            }
            
            public void mouseExited(MouseEvent evt) {
                btnSave.setBackground(Color.BLACK);
            }
        });


        btnExport.setPreferredSize(new Dimension(200, 200));
        btnExport.setBackground(Color.BLACK);
        btnExport.setForeground(new Color(0xa6a6a6));
        ImageIcon iconBtnExport = new ImageIcon(new ImageIcon("Obr/export.png").getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT));
        btnExport.setIconTextGap(25);
        btnExport.setIcon(iconBtnExport);
        btnExport.setBorder(BorderFactory.createEtchedBorder());
        btnExport.setFocusable(false);
        btnExport.setBorderPainted(false);

        btnExport.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                if(e.getSource()==btnExport) {
                    Export();
                }
        }});

        btnExport.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btnExport.setBackground(new Color(77, 77, 77));
            }
                
            public void mouseExited(MouseEvent evt) {
                btnExport.setBackground(Color.BLACK);
            }
        });


        JButton btnNothing = new JButton();
        btnNothing.setPreferredSize(new Dimension(200, 200));
        btnNothing.setBackground(Color.BLACK);
        btnNothing.setBorder(BorderFactory.createEtchedBorder());
        btnNothing.setEnabled(false);


        /**********************Create Filter***********************/
        JTextField txtFilter = new JTextField();
        txtFilter.setPreferredSize(new Dimension(100, 30));
        txtFilter.setLayout(new BorderLayout());
        txtFilter.addKeyListener(new KeyListener() {

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
                String query = txtFilter.getText().toLowerCase();
                filter(query);
            }

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
            
            }});

        JLabel textLabel = new JLabel();
        textLabel.setForeground(Color.WHITE);
        textLabel.setText("Vyhľadavanie: ");
        textLabel.setPreferredSize(new Dimension(100, 30));


        //Buttons panel
        JPanel btnPanel = new JPanel();
        btnPanel.setPreferredSize(new Dimension(180, 350));
        btnPanel.setBackground(Color.BLACK);
        btnPanel.setLayout(new GridLayout(7, 0, 0, 0));

        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnSave);
        btnPanel.add(btnDelete);
        btnPanel.add(btnNothing);
        btnPanel.add(btnImport);
        btnPanel.add(btnExport);
       

        //Down panel
        JPanel DPanel = new JPanel();
        DPanel.setPreferredSize(new Dimension(200, 700));
        DPanel.setBackground(Color.BLACK); 
        DPanel.setLayout(new BorderLayout());
        DPanel.setBorder(BorderFactory.createEmptyBorder(130, 0, 35, 0));
        DPanel.add(btnPanel, BorderLayout.CENTER);



        //Text panel
        JPanel TPanel = new JPanel();
        TPanel.setPreferredSize(new Dimension(200, 50));
        TPanel.setLayout(new GridLayout(0,2,-280,0));
        TPanel.setBackground(new Color(0x3D3D41));
        TPanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 25, 570));
        TPanel.add(textLabel, BorderLayout.CENTER);
        TPanel.add(txtFilter, BorderLayout.CENTER);


        //Filter panel
        JPanel FPanel = new JPanel();
        FPanel.setPreferredSize(new Dimension(100, 50));
        FPanel.setBackground(Color.BLUE); // 0x1c153c
        FPanel.setLayout(new BorderLayout());
        FPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        FPanel.add(jScrollPane1, BorderLayout.CENTER);
        FPanel.add(TPanel, BorderLayout.SOUTH);



        /**************** Create Frame*********************/
        frame.setTitle("TabelView");
        frame.setSize(1300, 700);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setResizable(false);
        frame.getContentPane().setBackground(new Color(77, 77, 77)); //0x25233E
        frame.setLayout(new BorderLayout());
        frame.setVisible(true);
    

        //add comp to the frame
        frame.add(FPanel, BorderLayout.CENTER);
        frame.add(DPanel, BorderLayout.WEST);


        //image
        ImageIcon image = new ImageIcon("Obr/logo.png");
        frame.setIconImage(image.getImage());




        /*******************Closing Main Window***********************/
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                
                    if(SystemTray.isSupported()==true) {
                        frame.setDefaultCloseOperation(JFrame.ICONIFIED);
                    }

                    SystemTray systemTray = SystemTray.getSystemTray();
                    TrayIcon trayIcon = new TrayIcon(Toolkit.getDefaultToolkit().getImage("Obr/logo.png"));
                    trayIcon.setImageAutoSize(true);
                    PopupMenu popMenu = new PopupMenu();

                    MenuItem show = new MenuItem("Open TableView");
                    show.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            // TODO Auto-generated method stub
                            frame.setVisible(true);
                            frame.setExtendedState(JFrame.NORMAL);
                            systemTray.remove(trayIcon);
                        }});
                    
                    MenuItem exit = new MenuItem("Exit TableView");
                    exit.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            // TODO Auto-generated method stub
                            SaveData(jTable1);
                            file.deleteOnExit();
                            System.exit(0);
                        }});

                    popMenu.add(show);
                    popMenu.add(exit);
                    trayIcon.setPopupMenu(popMenu);
                    try {
                        systemTray.add(trayIcon);
                    } catch (AWTException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }                     
            }
        });


        /*************Load Data*****************/
        LoadData();
        newWindow.count = jTable1.getRowCount();
        newWindow.count++;
    }


    public void filter(String query) {
        DefaultTableModel dm = (DefaultTableModel)jTable1.getModel();
        TableRowSorter<DefaultTableModel> tr = new TableRowSorter<DefaultTableModel>(dm);
        
        jTable1.setRowSorter(tr);
        tr.setRowFilter(RowFilter.regexFilter("(?i)" + query));
    }

  
    public void SaveData(JTable table) {
        String path = System.getProperty("user.home") + File.separator + "TableW Data"; 
        File file = new File(path+"/data.txt");
        if(!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        try {
            FileWriter writer = new FileWriter(file.getAbsolutePath());
            BufferedWriter buffer = new BufferedWriter(writer);

            TableModel model = jTable1.getModel();
            for(int row=0; row<model.getRowCount(); row++) {
                for(int col=0; col<model.getColumnCount(); col++) {
                    buffer.write(model.getValueAt(row, col)+"<&>");
                }
                buffer.newLine();
            }
            buffer.close();
            JOptionPane.showMessageDialog(frame, "Uložené");
        } catch (Exception e) {
            //TODO: handle exception
            JOptionPane.showMessageDialog(frame, "Error save!");
        }
        
    }


    public void LoadData() {
        String path = System.getProperty("user.home") + File.separator + "TableW Data"; 
        File file = new File(path+"/data.txt");

        try {
            //Read header
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file.getAbsolutePath()));
            DefaultTableModel model = (DefaultTableModel)jTable1.getModel();

            //Read data      
            Object[] data = bufferedReader.lines().toArray();
            
            for(int i=0; i<data.length; i++) {
                String line = data[i].toString().trim();
                String[] dataRow = line.split("<&>");
                model.addRow(dataRow);
            }
            bufferedReader.close();
        } catch (Exception e) {
            //TODO: handle exception
            JOptionPane.showMessageDialog(frame, "Error load!");
        }
    }


    public void Import(File file, JTable table) throws IOException {     
        File excelFile;
        XSSFWorkbook excelImportToJTable = null;
        JFileChooser excelFileChooser = new JFileChooser(new File(file.getAbsolutePath()));
        excelFileChooser.setDialogTitle("Select Excel File");
        FileNameExtensionFilter fnef = new FileNameExtensionFilter(null, "xls", "xlsx", "xlsm");
        excelFileChooser.setFileFilter(fnef);

        int excelChooser = excelFileChooser.showOpenDialog(null);
        if (excelChooser == JFileChooser.APPROVE_OPTION) {
            try {
                //Detele Tble
                DefaultTableModel oldTable = (DefaultTableModel)jTable1.getModel();
                oldTable.setRowCount(0);
                //Import
                excelFile = excelFileChooser.getSelectedFile();
                FileInputStream excelFIS = new FileInputStream(excelFile);
                BufferedInputStream excelBIS = new BufferedInputStream(excelFIS);
                excelImportToJTable = new XSSFWorkbook(excelBIS);
                XSSFSheet excelSheet = excelImportToJTable.getSheetAt(0);
                DefaultTableModel m = (DefaultTableModel)jTable1.getModel();        

                for(int row = 0; row <= excelSheet.getLastRowNum(); row++) {
                    XSSFRow excelRow = excelSheet.getRow(row);
 
                    XSSFCell exceltxt1 = excelRow.getCell(0);
                    XSSFCell exceltxt2 = excelRow.getCell(1);
                    XSSFCell exceltxt3 = excelRow.getCell(2);
                    XSSFCell exceltxt4 = excelRow.getCell(3);
                    XSSFCell exceltxt5 = excelRow.getCell(4);
                    XSSFCell exceltxt6 = excelRow.getCell(5);
                    XSSFCell exceltxt7 = excelRow.getCell(6);
                    XSSFCell exceltxt8 = excelRow.getCell(7);
                    
                    m.addRow(new Object[]{exceltxt1, exceltxt2, exceltxt3, exceltxt4, exceltxt5, exceltxt6, exceltxt7, exceltxt8});
                    
                    AddExpiredDateToTable(row);
                }
                JOptionPane.showMessageDialog(null, "Úspešne nahratie!");
            } catch (IOException iOException) {
                JOptionPane.showMessageDialog(frame, "Error import!");
            }
        }  
    } 


    public void Export() {
        try {
            DefaultTableModel model = (DefaultTableModel)jTable1.getModel();
            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.showSaveDialog(frame);
            File saveFile = jFileChooser.getSelectedFile();
            if(saveFile != null) {
                saveFile = new File(saveFile.toString()+".xlsx");
                Workbook wb = new XSSFWorkbook();
                Sheet sheet = wb.createSheet();

                Row rowCol = sheet.createRow(0);
                for(int i=0; i<model.getColumnCount()-1;i++) {
                    Cell cell = rowCol.createCell(i);
                    cell.setCellValue(model.getColumnName(i)+"\t");
                }

                for(int j=0; j<model.getRowCount();j++) {
                    Row row = sheet.createRow(j+1);
                    for(int k=0; k<model.getColumnCount()-1;k++) {
                        Cell cell = row.createCell(k);
                        if(model.getValueAt(j, k) != null) {
                            cell.setCellValue(model.getValueAt(j, k).toString());
                        }
                    }
                }
                FileOutputStream out = new FileOutputStream(new File(saveFile.toString()));
                wb.write(out);
                wb.close();
                out.close();
            }else {
                JOptionPane.showMessageDialog(frame, "Error export!");
            }

        } catch (FileNotFoundException e) {
            //TODO: handle exception
            System.out.println(e);
        } catch (IOException io) {
            //TODO: handle exception
            System.out.println(io);
        }
    }

}
