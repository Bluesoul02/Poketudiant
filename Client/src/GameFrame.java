import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class GameFrame extends JFrame {

    public GameFrame() {
        super("Poketudiant");
    
        Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(0, 0, screensize.width, screensize.height);
        setResizable(false); // might change
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setContentPane(new Menu());
        setVisible(true);
    }

}