import javax.swing.JButton;

public class PokeButton extends JButton{
    private int pos;
    private Poketudiant poketudiant;

    public PokeButton(int pos, Poketudiant poketudiant) {
        super(poketudiant.getType().concat(
            " ".concat(String.valueOf(poketudiant.getLvl()))));
        this.pos = pos;
        this.poketudiant = poketudiant;
    }

    public int getPos() {
        return pos;
    }

    public Poketudiant getPoketudiant() {
        return poketudiant;
    }
}
