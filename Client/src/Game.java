import javax.swing.JPanel;

public class Game extends JPanel {
    
    public Game(Client client) {
        client.emptyList(); // clear the list
        Map map = new Map(client); // add the map on the panel
        Chat chat = new Chat(client);
        add(map);
        add(chat);
    }

}
