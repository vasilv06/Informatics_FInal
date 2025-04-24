import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Welcome extends JFrame {
    private JLabel img;
    private JLabel first_name;
    private JButton inventoryButton;
    private JPanel panel;

    public Welcome(User user) {
        setSize(500, 500);



        first_name.setText("                                            Welcome " + user.getFirst_name() + ":");

        setContentPane(panel);
        setVisible(true);
        panel.setBackground(new Color(137, 207, 240));


        ImageIcon icon = user.getImageIcon();
        img.setIcon(icon);

        inventoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new InventoryManagement();
                dispose();
            }
        });
    }
}
