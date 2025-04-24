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



        first_name.setText("Welcome " + user.getFirst_name());

        setContentPane(panel);
        setVisible(true);

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
