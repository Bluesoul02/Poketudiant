import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridLayout;

public class Encounter extends JPanel {
    private JButton attack1;
    private JButton attack2;
    private JLabel nbPokmn;
    private JLabel nbPokmnRival;
    private JLabel hpRival;
    private JLabel hp;
    private JLabel imageRival;
    private JLabel image;
    private boolean rival;

    public Encounter(Client client) {
        this.setLayout(new GridLayout(5,2));

        attack1 = new JButton("attack1");
        attack2 = new JButton("attack2");
        nbPokmn = new JLabel("nbPokmn");
        nbPokmnRival = new JLabel("nbPokmnRival");
        hp = new JLabel("XX / XX");
        hpRival = new JLabel("XX / XX");
        image = new JLabel("image");
        imageRival = new JLabel("imageRival");


        JButton escape = new JButton("Escape");
        escape.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                client.escape();
            }
        });

        JButton capture = new JButton("Catch");
        capture.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                client.capture();
            }
        });

        add(nbPokmnRival);
        add(image);
        add(imageRival);
        add(hp);
        add(hpRival);
        add(nbPokmn);
        add(attack1);
        add(attack2);
        add(capture);
        add(escape);
    }

    public void fight(int nbPokmnRival, boolean rival) {
        this.rival = rival;
    }

    public boolean isRival() {
        return rival;
    }
}
