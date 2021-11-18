import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Map extends JPanel{
    
    public Map(Client client) {
        GridLayout gl = new GridLayout();
        this.setLayout(gl);
        try {
            int width = client.readMap();
            gl.setColumns(width);
            List<String> map = client.getServerOutput();
            final BufferedImage grass = ImageIO.read(new File("assets/grass.png"));
            JLabel picLabel;
            for (String mapLine : map) {
                for(int i = 0; i < width; i++) {
                    System.out.println(mapLine.charAt(i));
                    picLabel = new JLabel(new ImageIcon(grass));
                    add(picLabel);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
