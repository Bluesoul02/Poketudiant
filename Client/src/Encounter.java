import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Encounter extends JPanel {
    public Encounter(Client client) {
        add(new JLabel("test"));
        JButton escape = new JButton("Escape");
        escape.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                client.escape();
            }
        });
        add(escape);
    }
}
