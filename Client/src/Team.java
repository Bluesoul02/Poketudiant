import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class Team extends JPanel{

    private class MyActionListener implements ActionListener{
        private Direction direction;

        public MyActionListener(Direction direction) {
            this.direction = direction;
        }

        public void actionPerformed(ActionEvent e) {
            client.movePoketudiant(selectedButton.getPos(), direction);
        }
    }

    private JLabel select;
    private PokeButton selectedButton;
    private Client client;
    private List<PokeButton> pokebuttons;

    public Team(Client client) {
        this.client = client;
        client.setTeam(this);
        GridLayout gl = new GridLayout();
        gl.setColumns(2);
        gl.setRows(4);
        gl.setHgap(10);
        gl.setVgap(10);
        this.setLayout(gl);
        select = new JLabel();
        pokebuttons = new ArrayList<PokeButton>();
        select.setForeground(Color.WHITE);
        setBackground(Color.decode("#1e3d59"));
        JButton up = new JButton("\u25B2");
        JButton right = new JButton("\u25B6");
        JButton left = new JButton("\u25C0");
        JButton down = new JButton("\u25BC");
        up.addActionListener(new MyActionListener(Direction.UP));
        down.addActionListener(new MyActionListener(Direction.DOWN));
        right.addActionListener(new MyActionListener(Direction.RIGHT));
        left.addActionListener(new MyActionListener(Direction.LEFT));
        add(up);
        add(right);
        add(left);
        add(down);
        add(select);
    }

    public void drawTeam(List<Poketudiant> poketudiants) {
        for (PokeButton pokebutton : pokebuttons) {
            remove(pokebutton);
        }
        PokeButton button;
        int c = 0;
        for (Poketudiant poketudiant : poketudiants) {
            button = new PokeButton(c++, poketudiant);
            button.setSize(100, 150);
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    select.setText(poketudiant.getType().concat(
                    " ".concat(String.valueOf(poketudiant.getLvl()))));
                    selectedButton = (PokeButton) e.getSource();
                    revalidate();
                    repaint();
                }
            });
            add(button);
            pokebuttons.add(button);
        }
        revalidate();
        repaint();
    }
}
