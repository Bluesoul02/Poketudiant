import java.io.IOException;
import java.net.InetAddress;
import java.util.List;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Main {
	public static void main(String[] args) throws IOException {
		try {
			Client client = new Client("255.255.255.255");
			List<InetAddress> list = client.searchServer(); // List of servers
			System.out.println(list);
			if (list.size() != 0)
				client.connectServer(list.get(0).getHostAddress()); // Connection to the first server
		} catch (SocketException | UnknownHostException e) {
			e.printStackTrace();
		}
    }
}
