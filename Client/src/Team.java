import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.List;

public class Team extends JPanel{
    private JLabel select;
    private PokeButton selectedButton;
    
    public Team(Client client) {
        client.setTeam(this);
        GridLayout gl = new GridLayout();
        gl.setColumns(2);
        gl.setRows(4);
        gl.setHgap(10);
        gl.setVgap(10);
        this.setLayout(gl);
        select = new JLabel();
        select.setForeground(Color.WHITE);
        setBackground(Color.decode("#1e3d59"));
        JButton haut = new JButton("\u25B2");
        JButton droite = new JButton("\u25B6");
        JButton gauche = new JButton("\u25C0");
        JButton bas = new JButton("\u25BC");
        add(haut);
        add(droite);
        add(gauche);
        add(bas);
        add(select);
        // TO DO gérer les boutons de directions
    }

    public void drawTeam(List<Poketudiant> poketudiants) {
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
        }
        revalidate();
        repaint();
    }
}
