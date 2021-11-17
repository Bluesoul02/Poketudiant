import javax.swing.JPanel;
import java.awt.Graphics;
import java.io.IOException;
import java.util.List;

public class Map extends JPanel{
    private Client client;
    
    public Map(Client client) {
        this.client = client;


    }

    public void paintComponent(Graphics g) {
        try {
            int width = client.readMap();
            List<String> map = client.getServerOutput();
            for (String mapLine : map) {
                for(int i = 0; i < width; i++) {
                    mapLine.charAt(i);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
