import java.awt.event.ActionListener;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import java.net.InetAddress;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ServerListPanel extends JPanel{
    
    public ServerListPanel(Client client) {
        BoxLayout bl = new BoxLayout(this, BoxLayout.PAGE_AXIS);
        this.setLayout(bl);

        JLabel jlabel = new JLabel("Liste des serveurs disponibles :");
        jlabel.setAlignmentX(CENTER_ALIGNMENT);
        add(jlabel);
        List<InetAddress> list = client.searchServer(); // List of servers
        for (InetAddress inetAddress : list) {
            this.add(Box.createRigidArea(new Dimension(0, 15)));
            JButton button = new JButton(inetAddress.toString());
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    client.connectServer(inetAddress.getHostAddress());
                }
            });
            button.setAlignmentX(CENTER_ALIGNMENT);
            add(button);
        }
    }
}
