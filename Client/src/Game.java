import java.io.IOException;
import javax.swing.JPanel;

public class Game extends JPanel {
    private Chat chat;
    private Map map;
    
    public Game(Client client) {
        client.emptyList(); // clear the list
        map = new Map(client); 
        chat = new Chat(client);
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
    }

    /**
     * @return the chat
     */
    public Chat getChat() {
        return chat;
    }

    /**
     * @return the map
     */
    public Map getMap() {
        return map;
    }
}
