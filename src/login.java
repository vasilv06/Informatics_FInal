import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class login extends JFrame {
    private JTextField textField1;
    private JTextField textField2;
    private JButton loginButton;
    private JPanel panel;

    public login() {

        setSize(500, 500);
        setContentPane(panel);
        setVisible(true);
        panel.setBackground(new Color(137, 207, 240));


        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                User user = connect.login(textField1.getText(), textField2.getText());
                if (user != null) {
                    JOptionPane.showMessageDialog(null, "Successfully logged in as " + user.getFirst_name(), "Login Success", JOptionPane.INFORMATION_MESSAGE);
                    new Welcome(user);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Login failed. Invalid credentials.", "Login Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
