import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;

public class Main {
	public static void main(String[] args) {
		try {
			Client client = new Client("255.255.255.255");
			new GameFrame();
			List<InetAddress> list = client.searchServer(); // List of servers
			System.out.println(list);
			if (list.size() != 0)
				client.connectServer(list.get(0).getHostAddress()); // Connection to a server
		} catch (SocketException | UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
