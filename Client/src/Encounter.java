import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridLayout;

public class Encounter extends JPanel {

    private JButton escape;
    private JButton capture;
    private JButton swap;
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
        this.setLayout(new GridLayout(7,2));

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
        rival = false;

        escape = new JButton("Escape");
        escape.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                waitNextTurn();
                client.escape();
            }
        });

        capture = new JButton("Catch");
        capture.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                waitNextTurn();
                client.capture();
            }
        });

        swap = new JButton("Switch");
        swap.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                waitNextTurn();
                client.swap();
            }
        });

        attack1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                waitNextTurn();
                client.attack("attack1");
            }
        });

        attack2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                waitNextTurn();
                client.attack("attack2");
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
        add(swap);
        add(capture);
        add(escape);

        attack1.setEnabled(false);
        attack2.setEnabled(false);
        this.escape.setEnabled(false);
        this.capture.setEnabled(false);
        swap.setEnabled(false);
    }

    public void waitNextTurn() {
        System.out.println("waitNextTurn");
        attack1.setEnabled(false);
        attack2.setEnabled(false);
        escape.setEnabled(false);
        capture.setEnabled(false);
        swap.setEnabled(false);
    }

    public void startFight(int nbPokmnRival, boolean rival) {
        this.setVisible(true);
        this.rival = rival;
        this.nbPokmnRival.setText(Integer.toString(nbPokmnRival));
    }

    public void setNbPokmn(int nbPokmn) {
        this.nbPokmn.setText(Integer.toString(nbPokmn));
    }

    public void setInfo(String variety, String lvl, String hp, String attack1Name, String attack1Type, String attack2Name, String attack2Type) {
        image.setText(variety);
        this.lvl.setText(lvl);
        this.hp.setText(hp);
        attack1.setText(attack1Name.concat(" \n").concat(attack1Type));
        attack2.setText(attack2Name.concat(" \n").concat(attack2Type));
    }

    public void setInfo(String variety, String lvl, String hp) {
        imageRival.setText(variety);
        lvlRival.setText(lvl);
        hpRival.setText(hp);
    }

    public void waitAction() {
        System.out.println("waitAction");
        this.attack2.setEnabled(true);
        this.attack1.setEnabled(true);
        this.swap.setEnabled(true);
        if (isRival()) {
            System.out.println("rival");
            this.escape.setEnabled(false);
            this.capture.setEnabled(false);
        } else {
            System.out.println("wild");
            this.escape.setEnabled(true);
            this.capture.setEnabled(true);
        }
    }

    public void endEncounter() {
        this.setVisible(false);
    }

    public boolean isRival() {
        return rival;
    }
}
