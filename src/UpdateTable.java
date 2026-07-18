import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.awt.event.*;


public class UpdateTable {
    
    JFrame win = new JFrame();
    JTextField txt1 = new JTextField();
    JTextField txt2 = new JTextField();
    JTextField txt3 = new JTextField();
    JTextField txt4 = new JTextField();
    JTextField txt5 = new JTextField();
    JTextField txt6 = new JTextField();
    JTextField txt7 = new JTextField();

    public JButton btnReset = new JButton("Reset");

    public void closeWindow() {
        win.dispose();
    }

    UpdateTable() {
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

        JLabel textLabel7 = new JLabel();
        textLabel7.setForeground(Color.WHITE); 
        textLabel7.setText("CE poradové číslo: ");

        //Reset Button
        btnReset.setPreferredSize(new Dimension(50, 40));
        btnReset.setFocusable(false);
        btnReset.setBackground(Color.BLACK);
        btnReset.setForeground(Color.WHITE);
        btnReset.setBorderPainted(false);
        btnReset.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                Date.clicked = true;
                win.dispose();
            }});

        btnReset.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btnReset.setBackground(new Color(77, 77, 77));
            }
            
            public void mouseExited(MouseEvent evt) {
                btnReset.setBackground(Color.BLACK);
            }
        });


        //Text fields
        JPanel textPanel = new JPanel();
        textPanel.setPreferredSize(new Dimension(50, 40));
        textPanel.setBackground(new Color(0x3D3D41));
        textPanel.setLayout(new GridLayout(7, 2, 0, 20));
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
        textPanel.add(textLabel7);
        textPanel.add(txt7);


        /**************** Create Frame*********************/
        win.setSize(350, 400);
        win.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        win.getContentPane().setBackground(new Color(0x25233E));
        win.setResizable(false);
        win.setTitle("Úprava riadku");
        win.setLayout(new BorderLayout());

        ImageIcon imageTitle = new ImageIcon("Obr/logo.png");
        win.setIconImage(imageTitle.getImage());

        //add comp
        win.add(textPanel, BorderLayout.CENTER);
        win.add(btnReset, BorderLayout.SOUTH);
    }
}
