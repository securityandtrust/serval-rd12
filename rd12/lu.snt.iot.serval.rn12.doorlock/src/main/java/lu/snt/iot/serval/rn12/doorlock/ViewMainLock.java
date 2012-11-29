package lu.snt.iot.serval.rn12.doorlock;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JOptionPane;

/**
 *
 * @author Assaad
 */
public class ViewMainLock extends JPanel{

    private DoorClass doorClass;
    private JPasswordField displayField;
    private JButton[] buttons;
    private JPanel buttonsPanel;
    public static final int NB_LINES = 4;
    public static final int NB_COLS = 3;
    public static final int BUTTON_SPACE = 5;
    public static final String CLEAN="C";
    public static final String ENTER="E";
    
    public ViewMainLock( DoorClass doorClass ){
        super(new BorderLayout());
        displayField = new JPasswordField("");
        displayField.setHorizontalAlignment(JPasswordField.RIGHT);
        add(displayField, BorderLayout.NORTH);
        buttonsPanel = new JPanel(new GridLayout(NB_LINES, NB_COLS, BUTTON_SPACE, BUTTON_SPACE));
        buttons = new JButton[NB_LINES * NB_COLS];
        
        for (int i = 0; i < buttons.length-3; i++) {
            buttons[i] = new JButton(""+Integer.toString(i+1));
            buttonsPanel.add(buttons[i]);
            buttonsPanel.add (buttons[i], BorderLayout.CENTER);
        }

        buttons[buttons.length-3] = new JButton(CLEAN);
        buttonsPanel.add(buttons[buttons.length-3]);
        buttonsPanel.add (buttons[buttons.length-3], BorderLayout.CENTER);

        buttons[buttons.length-2] = new JButton("0");
        buttonsPanel.add(buttons[buttons.length-2]);
        buttonsPanel.add (buttons[buttons.length-2], BorderLayout.CENTER);


        buttons[buttons.length-1] = new JButton(ENTER);
        buttonsPanel.add(buttons[buttons.length-1]);
        buttonsPanel.add (buttons[buttons.length-1], BorderLayout.CENTER);
        add (buttonsPanel, BorderLayout.CENTER);

        for (int i = 0; i < buttons.length; i++) {

            buttons[i].addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton1ActionPerformed(evt);
                }
            });
        }

        this.doorClass=doorClass;
       // JOptionPane.showMessageDialog(null,args[0]+" " + args[1], "Info", JOptionPane.INFORMATION_MESSAGE);


    }

    public void changePassword()
    {
        doorClass.ChangePassword();
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        String temp = evt.getActionCommand();

        if(temp==CLEAN)
        {
            displayField.setText("");
        }
        else if(temp==ENTER)
        {
            String pass = new String(displayField.getPassword());
            if(doorClass.OpenDoor(pass))
            {
                JOptionPane.showMessageDialog(null,"You Opened the door!", "Congratulations! ", JOptionPane.INFORMATION_MESSAGE);
            }
            else
            {
                JOptionPane.showMessageDialog(null,"Wrong Password!", "Try again ", JOptionPane.INFORMATION_MESSAGE);
            }
            displayField.setText("");

        }
        else
        {
            String pass = new String(displayField.getPassword());
            displayField.setText(pass +temp);

        }
    }
}
