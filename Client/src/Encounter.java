import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridLayout;

public class Encounter extends JPanel {
    private JButton escape;
    private JButton capture;
    private JButton attack1;
    private JButton attack2;
    private JLabel nbPokmn;
    private JLabel nbPokmnRival;
    private JLabel lvlRival;
    private JLabel lvl;
    private JLabel hpRival;
    private JLabel hp;
    private JLabel imageRival;
    private JLabel image;
    private boolean rival;

    public Encounter(Client client) {
        this.setLayout(new GridLayout(6,2));

        attack1 = new JButton("attack1 \n type");
        attack2 = new JButton("attack2 \n type");
        nbPokmn = new JLabel("nbPokmn");
        nbPokmnRival = new JLabel("nbPokmnRival");
        lvl = new JLabel("lvl");
        lvlRival = new JLabel("lvlRival");
        hp = new JLabel("XX %");
        hpRival = new JLabel("XX %");
        image = new JLabel("image");
        imageRival = new JLabel("imageRival");


        escape = new JButton("Escape");
        escape.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                client.escape();
            }
        });

        capture = new JButton("Catch");
        capture.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                client.capture();
            }
        });

        add(nbPokmnRival);
        add(image);
        add(imageRival);
        add(lvl);
        add(lvlRival);
        add(hp);
        add(hpRival);
        add(nbPokmn);
        add(attack1);
        add(attack2);
        add(capture);
        add(escape);
    }

    public void startFight(int nbPokmnRival, boolean rival) {
        this.rival = rival;
        if (isRival()) {
            this.escape.setEnabled(false);
            this.capture.setEnabled(false);
        }
        this.nbPokmnRival.setText(Integer.toString(nbPokmnRival));
    }

    public void setInfo(String variety, String lvl, String hp, String attack1Name, String attack1Type, String attack2Name, String attack2Type) {
        image.setText(variety);
        this.lvl.setText(lvl);
        this.hp.setText(hp);
        attack1.setText(attack1Name + "\n" + attack1Type);
        attack2.setText(attack2Name + "\n" + attack2Type);
    }

    public void setInfo(String variety, String lvl, String hp) {
        imageRival.setText(variety);
        lvlRival.setText(lvl);
        hpRival.setText(hp);
    }

    public void endEncounter() {
        this.setVisible(false);
        this.escape.setEnabled(true);
        this.capture.setEnabled(true);
    }

    public boolean isRival() {
        return rival;
    }
}
