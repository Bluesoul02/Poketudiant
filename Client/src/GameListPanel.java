import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.util.List;

public class GameListPanel extends JPanel {

    private class MyActionListener implements ActionListener {

        private int counter;

        public MyActionListener(int i) {
            counter = i;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                if (client.joinGame(serverOutput.get(counter).split(" ")[1])) {
                    // TODO launch game
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    private Client client;
    private List<String> serverOutput;
    
    public GameListPanel(Client client) {
        this.client = client;
        setBackground(Color.decode("#f5f0e1"));
        BoxLayout bl = new BoxLayout(this, BoxLayout.PAGE_AXIS);
        this.setLayout(bl);
        // showGameList(client);
    }

    public void showGameList() {
        removeAll();
        serverOutput = client.getServerOutput();
        JLabel jlabel = new JLabel("Number of games : " + serverOutput.size());
        add(jlabel);
        JButton button;
        for (int i = 0; i < serverOutput.size(); i++) {
            add(Box.createRigidArea(new Dimension(0, 15)));
            button = new JButton();
            button.addActionListener(new MyActionListener(i));
            add(button);
        }
        this.add(Box.createRigidArea(new Dimension(0, 15)));
        JButton createGame = new JButton("Create a game");
        createGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String gameName = JOptionPane.showInputDialog("Nom de la partie : ");
                try {
                    if (client.createGame(gameName)) {
                        // TODO launch game
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }     
            }
        });
        add(createGame);
        revalidate();
        repaint();
    }

}
