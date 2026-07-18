import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.awt.event.*;


public class newWindow {
    public static int count = 1;

    MainFrame main = new MainFrame();
    JFrame win = new JFrame();
    JTextField txt1 = new JTextField();
    JTextField txt2 = new JTextField();
    JTextField txt3 = new JTextField();
    JTextField txt4 = new JTextField();
    JTextField txt5 = new JTextField();
    JTextField txt6 = new JTextField();

    public void closeWindow() {
        win.dispose();
    }

    public void openWindow() {

        /******************Create Text Label********************/
        JLabel textLabel1 = new JLabel();
        textLabel1.setForeground(Color.WHITE);
        textLabel1.setText("Ulica: ");
        textLabel1.setBounds(100,100,200,40);

        JLabel textLabel2 = new JLabel();
        textLabel2.setForeground(Color.WHITE);
        textLabel2.setText("Číslo vchodu: ");

        JLabel textLabel3 = new JLabel();
        textLabel3.setForeground(Color.WHITE);
        textLabel3.setText("Rok Výroby: ");

        JLabel textLabel4 = new JLabel();
        textLabel4.setForeground(Color.WHITE);
        textLabel4.setText("Mesiac: ");

        JLabel textLabel5 = new JLabel();
        textLabel5.setForeground(Color.WHITE);
        textLabel5.setText("EZ-EM: ");

        JLabel textLabel6 = new JLabel();
        textLabel6.setForeground(Color.WHITE); 
        textLabel6.setText("Poradové číslo: ");


        //Text fields
        JPanel textPanel = new JPanel();
        textPanel.setPreferredSize(new Dimension(50, 40));
        textPanel.setBackground(new Color(0x3D3D41));
        textPanel.setLayout(new GridLayout(6, 2, 0, 25));
        textPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 40, 40));

        //Add textfield comp
        textPanel.add(textLabel1);
        textPanel.add(txt1);
        textPanel.add(textLabel2);
        textPanel.add(txt2);
        textPanel.add(textLabel3);
        textPanel.add(txt3);
        textPanel.add(textLabel4);
        textPanel.add(txt4);
        textPanel.add(textLabel5);
        textPanel.add(txt5);
        textPanel.add(textLabel6);
        textPanel.add(txt6);


        /******************Create Buttons***********************/
        JButton btnOk = new JButton("OK");
        btnOk.setPreferredSize(new Dimension(200, 200));
        btnOk.setBackground(Color.BLACK);
        btnOk.setForeground(new Color(0xa6a6a6)); 
        btnOk.setFocusable(false);
        btnOk.setBorder(BorderFactory.createEtchedBorder());
        btnOk.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                if(e.getSource()==btnOk) {
                    if(txt1.getText().equals("") || txt2.getText().equals("") || txt3.getText().equals("") 
                    || txt4.getText().equals("") || txt5.getText().equals("") || txt6.getText().equals("")) {
                        JOptionPane.showMessageDialog(win, "Je potrebné vyplniť všetky polia!");
                    }else {
                        MainFrame.AddRow(new Object[] {
                            count++,
                            txt1.getText(), txt2.getText(), txt3.getText(), txt4.getText(), txt5.getText(), txt6.getText(),
                        });
                        win.dispose();
                        txt1.setText(""); txt2.setText(""); txt3.setText(""); txt4.setText(""); txt5.setText(""); txt6.setText("");
                    }
                }
            }     
        });

        btnOk.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btnOk.setBackground(new Color(77, 77, 77));
            }
            
            public void mouseExited(MouseEvent evt) {
                btnOk.setBackground(Color.BLACK);
            }
        });


        JButton btnCancel = new JButton("Zrušiť");
        btnCancel.setPreferredSize(new Dimension(200, 200));
        btnCancel.setBackground(Color.BLACK);
        btnCancel.setForeground(new Color(0xa6a6a6));
        btnCancel.setBorder(BorderFactory.createEtchedBorder());
        btnCancel.setFocusable(false);
        btnCancel.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                win.dispose();
            }     
        });

        btnCancel.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btnCancel.setBackground(new Color(77, 77, 77));
            }
            
            public void mouseExited(MouseEvent evt) {
                btnCancel.setBackground(Color.BLACK);
            }
        });


        //Buttons panel
        JPanel btnPanel = new JPanel();
        btnPanel.setPreferredSize(new Dimension(350, 40));
        btnPanel.setBackground(Color.BLACK);
        btnPanel.setLayout(new GridLayout(2, 0, 0, 0));

        btnPanel.add(btnOk);
        btnPanel.add(btnCancel);


        //Down panel
        JPanel DPanel = new JPanel();
        DPanel.setPreferredSize(new Dimension(150, 80));
        DPanel.setBackground(Color.BLACK);
        DPanel.setLayout(new BorderLayout());
        DPanel.setBorder(BorderFactory.createEmptyBorder(200, 0, 50, 0));
        DPanel.add(btnPanel, BorderLayout.CENTER);



        /**************** Create Frame*********************/
        win.setSize(500, 400);
        win.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        win.getContentPane().setBackground(Color.BLACK);
        win.setResizable(false);
        win.setTitle("Add");
        win.setLayout(new BorderLayout());

        //add comp
        win.add(DPanel, BorderLayout.EAST);
        win.add(textPanel, BorderLayout.CENTER);


        ImageIcon imageTitle = new ImageIcon("Obr/logo.png");
        win.setIconImage(imageTitle.getImage()); 
    }
}
