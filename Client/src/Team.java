import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.List;

public class Team extends JPanel{
    
    public Team(Client client) {
        client.setTeam(this);
        GridLayout gl = new GridLayout();
        this.setLayout(gl);
        setBackground(Color.decode("#1e3d59"));
    }

    public void drawTeam(List<Poketudiant> poketudiants) {
        JButton button;
        for (Poketudiant poketudiant : poketudiants) {
            button = new JButton(poketudiant.getType().concat(
                " ".concat(String.valueOf(poketudiant.getLvl()))));
            button.setSize(75, 100);
            add(button);
            this.add(Box.createRigidArea(new Dimension(10, 10)));
        }
        revalidate();
        repaint();
    }
}
