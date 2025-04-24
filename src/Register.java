import org.mindrot.jbcrypt.BCrypt;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class Register extends JFrame {
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JButton registerButton;
    private JButton selectImageButton;
    private JButton loginInsteadButton;
    private JLabel imageLabel;
    private JPanel panel;

    public Register() {

        setSize(500, 500);
        panel.setBackground(new Color(137, 207, 240));

        setContentPane(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        selectImageButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                int result = fc.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    try {
                        Image img = ImageIO.read(file);
                        ImageIcon icon = new ImageIcon(img.getScaledInstance(100, 100, Image.SCALE_SMOOTH));
                        imageLabel.setIcon(icon);
                        imageLabel.setText("");
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Error loading image.");
                    }
                }
            }
        });

        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String fName = textField1.getText();
                String lName = textField2.getText();
                String password = textField3.getText();
                String hashedPass = BCrypt.hashpw(password, BCrypt.gensalt());

                ImageIcon icon = (ImageIcon) imageLabel.getIcon();
                InputStream imgStream = null;
                if (icon != null) {
                    BufferedImage bimg = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
                    Graphics g = bimg.createGraphics();
                    icon.paintIcon(null, g, 0, 0);
                    g.dispose();
                    try {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ImageIO.write(bimg, "png", baos);
                        imgStream = new ByteArrayInputStream(baos.toByteArray());
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Error converting image.");
                        return;
                    }
                }

                if (!fName.isEmpty() && !lName.isEmpty() && !password.isEmpty() && imgStream != null) {
                    connect.addUser(fName, lName, imgStream, hashedPass);
                    JOptionPane.showMessageDialog(null, "Registration successful!");
                    new Welcome(new User("0", fName, imgStream));
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Fill all the fields!");
                }
            }
        });

        loginInsteadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new login();
                dispose();
            }
        });
    }
}
