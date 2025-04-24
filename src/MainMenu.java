import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainMenu extends JFrame {
    private JButton registerButton;
    private JButton loginButton;
    private JPanel panel;

    public MainMenu() {

        setSize(400, 300);
        setContentPane(panel);
        setVisible(true);
        panel.setBackground(new Color(137, 207, 240));


        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Register();
                dispose();
            }
        });

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new login();
                dispose();
            }
        });
    }
}
