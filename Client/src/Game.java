import java.io.IOException;
import javax.swing.JPanel;

public class Game extends JPanel {
    
    public Game(Client client) {
        client.emptyList(); // clear the list
        Map map = new Map(client); 
        Chat chat = new Chat(client);
        Team team = new Team(client);
        add(team); // add the team on the panel
        add(map); // add the map on the panel
        add(chat); // add the chat on the panel
        startGame(client);
    }

    public void startGame(Client client) {
        Thread daemon = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    client.listenToServer();
                } catch (IOException e) {
                    e.printStackTrace();
                }     
            }
        }, "Demon");
        daemon.setDaemon(true);
        daemon.start();
        this.addKeyListener(new InputListener(client));
    }
}
