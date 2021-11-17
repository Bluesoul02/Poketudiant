import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.net.SocketTimeoutException;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
	private DatagramSocket socket;
	private DatagramPacket receive;
	private DatagramPacket send;
	private Socket s;
    private InetAddress address;
	private PrintWriter writer;
	private BufferedReader reader;
	private List<InetAddress> listOfInetAddress;
	private List<String> serverOutput;
	private static final String SEARCH_SERVER = "looking for poketudiant servers";
	private static final String ANSWER_SEARCH_SERVER = "i'm a poketudiant server";
    private final static int PORTUDP = 9000;
    private final static int PORTTCP = 9001;
    private final static int SIZE = 500;

    public Client(String hostname) throws IOException {
        socket = new DatagramSocket();
        address = InetAddress.getByName(hostname);
        socket.setBroadcast(true);
		listOfInetAddress = new ArrayList<InetAddress>();
		send = new DatagramPacket(SEARCH_SERVER.getBytes(), SEARCH_SERVER.getBytes().length, address, PORTUDP);
		receive = new DatagramPacket(new byte[SIZE], SIZE, address, PORTUDP);
    }

	public List<InetAddress> searchServer() {
		try {
			listOfInetAddress.clear();
			socket.send(send);
			socket.setSoTimeout(2000);

			while (true) {
				socket.receive(receive);
				byte[] msgServer = receive.getData();
				String response = new String(msgServer, 0, ANSWER_SEARCH_SERVER.length());
				System.out.println(response);
				if (ANSWER_SEARCH_SERVER.equals(response)) listOfInetAddress.add(receive.getAddress());
			}

		} catch (SocketTimeoutException e) {
			return listOfInetAddress;
		} catch (IOException e) {
			if (listOfInetAddress.isEmpty()) {
				System.err.println("Pas de serveur");
			}
			socket.close();
		}
		return Collections.emptyList();
	}

	public void connectServer(String hostname) {
		try {
			if (s != null) s.close();
			serverOutput = new ArrayList<>();
			s = new Socket(InetAddress.getByName(hostname), PORTTCP);
			OutputStream output = s.getOutputStream();
			InputStream input = s.getInputStream();

			writer = new PrintWriter(output, true);
			reader = new BufferedReader(new InputStreamReader(input));

			writer.println("require game list");
			writer.flush();

			String str = reader.readLine();
			System.out.println(str); // Print the number of games
			if(str.contains("number of games")) {
				int gamesNb = Integer.parseInt(str.split(" ")[3]);
				for (int i = 0; i < gamesNb; i++) {
					str = reader.readLine();
					serverOutput.add(str);
				}
			}

			for (int i = 0; i < serverOutput.size(); i++) System.out.println(serverOutput.get(i));

			// int choix;
			// Scanner sc = new Scanner(System.in);
			// System.out.println("Choose an option :");
			// choix = sc.nextInt();
			// String msg = "join game " + serverOutput.get(choix).split(" ",2)[1];
			// msg = msg.substring(0, msg.lastIndexOf(" "));
			// System.out.println(msg);
			// writer.println(msg);
			// writer.flush();

			// str = reader.readLine();
			// while (str != null) {
			// 	serverOutput.add(str);
			// 	System.out.println(str);
			// 	str = reader.readLine();
			// }

			// sc.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean createGame(String gameName) throws IOException {
		writer.println("create game " + gameName);
		return reader.readLine().equals("game created");
	}

	public boolean joinGame(String gameName) throws IOException {
		writer.println("join game " + gameName);
		return reader.readLine().equals("game joined");
	}

	public List<String> getServerOutput() {
		return this.serverOutput;
	}

    public void close() {
        socket.close();
    }
}