import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class Menu extends JPanel {
    
    public Menu() {

        BoxLayout bl = new BoxLayout(this, BoxLayout.PAGE_AXIS);
        this.setLayout(bl);
        JButton serverList = new JButton("Server list");
        serverList.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ServerPanel servPane;
                try {
                    servPane = new ServerPanel(new Client("255.255.255.255"));
                    GameFrame.getInstance().setContentPane(servPane);                    
                    servPane.revalidate();
                    servPane.repaint();
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
}