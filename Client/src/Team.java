import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.List;

public class Team extends JPanel{
    private JLabel select;
    private PokeButton selectedButton;
    
    public Team(Client client) {
        client.setTeam(this);
        GridLayout gl = new GridLayout();
        this.setLayout(gl);
        select = new JLabel();
        select.setForeground(Color.WHITE);
        add(select);
        setBackground(Color.decode("#1e3d59"));
        // TO DO ajouter les boutons de directions ici
    }

    public void drawTeam(List<Poketudiant> poketudiants) {
        PokeButton button;
        int c = 0;
        for (Poketudiant poketudiant : poketudiants) {
            button = new PokeButton(c++, poketudiant);
            button.setSize(75, 100);
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
            this.add(Box.createRigidArea(new Dimension(10, 10)));
        }
        revalidate();
        repaint();
    }
}
