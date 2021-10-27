import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Client{
    private DatagramSocket socket;
    private InetAddress address;

    private final static int PORT = 9000;

    public Client() throws IOException {
        socket = new DatagramSocket();
        address = InetAddress.getByName("255.255.255.255");
        socket.setBroadcast(true);

    }

	public String receive() {
		byte[] receiveData = new byte[1024];
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		try {
			socket.receive(receivePacket);
			String serverAnswer = new String(receivePacket.getData());
			System.out.println("message reçu : " + serverAnswer);
			return serverAnswer;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void send(String msg) {
		DatagramPacket toSend = new DatagramPacket(msg.getBytes(), msg.getBytes().length, address, PORT);
		try {
			socket.send(toSend);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

    public void close() {
        socket.close();
    }

}