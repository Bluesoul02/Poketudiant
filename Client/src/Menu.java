import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JFrame;

public class Menu extends JPanel {
    
    public Menu() {

        BoxLayout bl = new BoxLayout(this, BoxLayout.PAGE_AXIS);
        this.setLayout(bl);
        JButton serverList = new JButton("Server list");
        serverList.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ServerListPanel servListPane;
                try {
                    servListPane = new ServerListPanel(new Client("255.255.255.255"));
                    ((JFrame) getParent().getParent().getParent()).setContentPane(servListPane);
                    servListPane.revalidate();
                    servListPane.repaint();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        JButton quit = new JButton("Quit");
        quit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        quit.setAlignmentX(CENTER_ALIGNMENT);
        serverList.setAlignmentX(CENTER_ALIGNMENT);
        // set buttons size even though it's not its true purpose
        serverList.add(Box.createRigidArea(new Dimension(200, 50)));
        quit.add(Box.createRigidArea(new Dimension(200, 50)));
        this.add(serverList);
        // create a gap between the buttons
        this.add(Box.createRigidArea(new Dimension(0, 15)));
        this.add(quit);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        //g.drawImage(Toolkit.getDefaultToolkit().createImage("img/MenuBg"), 0, 0, null);
    }
}