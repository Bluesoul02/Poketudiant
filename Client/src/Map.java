import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Map extends JPanel{
    private ImageIcon grass;
    private ImageIcon tallGrass;
    private ImageIcon pokmnCenter;
    private ImageIcon player;
    private Client client;
    private InputListener l;
    private GridLayout gl;
    
    public Map(Client client) {
        this.addMouseListener(new MouseInputListener() {

            @Override
            public void mouseClicked(MouseEvent me) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void mouseEntered(MouseEvent me) {
                requestFocus();
            }

            @Override
            public void mouseExited(MouseEvent me) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void mousePressed(MouseEvent me) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void mouseReleased(MouseEvent me) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void mouseDragged(MouseEvent me) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void mouseMoved(MouseEvent me) {
                // TODO Auto-generated method stub
                
            }
        });
        gl = new GridLayout();
        this.setLayout(gl);
        this.client = client;
        client.setMap(this);
        try {
            grass = new ImageIcon(ImageIO.read(new File("assets/grass.png")));
            Image image = grass.getImage();
            Image rescaled = image.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            grass = new ImageIcon(rescaled);
                        
            tallGrass = new ImageIcon(ImageIO.read(new File("assets/tallGrass.png")));
            image = tallGrass.getImage();
            rescaled = image.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            tallGrass = new ImageIcon(rescaled);

            pokmnCenter = new ImageIcon(ImageIO.read(new File("assets/pokmnCenter.png")));
            image = pokmnCenter.getImage();
            rescaled = image.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            pokmnCenter = new ImageIcon(rescaled);

            player = new ImageIcon(ImageIO.read(new File("assets/player.png")));
            image = player.getImage();
            rescaled = image.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            player = new ImageIcon(rescaled);

        } catch (IOException e) {
            e.printStackTrace();
        }
        l = new InputListener(client);
    }

    public void drawMap(int width) {
        // is getting called too many times i think
        removeAll();
        if (getKeyListeners().length > 0) removeKeyListener(l);
        this.addKeyListener(l);
        this.requestFocus();
        System.out.println("drawMap");
        if (width > 15) width = 15;
        int c = 0;
        gl.setColumns(width);
        List<String> map = client.getServerOutput();
        gl.setRows(map.size() > 15 ? 15 : map.size());
        JLabel picLabel;
        for (String mapLine : map) {
            for(int i = 0; i < width; i++) {
                if ((mapLine.charAt(i)) == ' ') picLabel = new JLabel(grass);
                else if ((mapLine.charAt(i)) == '*') picLabel = new JLabel(tallGrass);
                else if ((mapLine.charAt(i)) == '+') picLabel = new JLabel(pokmnCenter);
                else picLabel = new JLabel(player);
                add(picLabel);
            }
            if (++c == 15) break;
        }
        revalidate();
        repaint();
    }
}
