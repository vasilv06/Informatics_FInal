import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.IOException;

public class User {
    private String actor_id;
    private String first_name;
    private InputStream img;

    public User(String id, String first_name, InputStream img) {
        this.actor_id = id;
        this.first_name = first_name;
        this.img = img;
    }

    public String getFirst_name() {
        return first_name;
    }

    public ImageIcon getImageIcon() {
        try {
            BufferedImage image = ImageIO.read(img);
            return new ImageIcon(image);
        } catch (IOException e) {
            System.out.println("Error loading image: " + e.getMessage());
            return null;
        }
    }
}
