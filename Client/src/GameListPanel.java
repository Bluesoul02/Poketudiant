import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;
import java.util.List;

public class GameListPanel extends JPanel {
    
    public GameListPanel(Client client) {
        setBackground(Color.decode("#f5f0e1"));
        BoxLayout bl = new BoxLayout(this, BoxLayout.PAGE_AXIS);
        this.setLayout(bl);
        // showGameList(client);
    }

    public void showGameList(Client client) {
        removeAll();
        List<String> serverOutput = client.getServerOutput();
        JLabel test = new JLabel(serverOutput.toString());
        add(test);
        if (serverOutput.size() == 0) return;
        JLabel jlabel = new JLabel(serverOutput.get(0));
        add(jlabel);
        for (int i = 0; i < serverOutput.size(); i++) {
            System.out.println(serverOutput.get(i));
        }
        revalidate();
        repaint();
    }

}
