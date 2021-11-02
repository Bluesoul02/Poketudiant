import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class Main {
	public static void main(String[] args) throws IOException {
		try {
			Client client = new Client("255.255.255.255");
			List<InetAddress> list = client.searchServer();
			System.out.println(list);
			client.connectServer();
		} catch (SocketException | UnknownHostException e) {
			e.printStackTrace();
		}
    }
}
