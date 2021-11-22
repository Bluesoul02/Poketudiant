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
	private Chat chat;
	private Map map;
	private boolean inGame;
	//public Game game;
	private static final String SEARCH_SERVER = "looking for poketudiant servers";
	private static final String ANSWER_SEARCH_SERVER = "i'm a poketudiant server";
    private final static int PORTUDP = 9000;
    private final static int PORTTCP = 9001;
    private final static int SIZE = 500;

    public Client(String hostname) throws IOException {
		serverOutput = new ArrayList<>();
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
			serverOutput.clear();
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

	public void listenToServer() throws IOException {
		inGame = true;
		while (inGame) {
			System.out.println("listening");
			String str = reader.readLine();
			System.out.println(str);
			if (str.contains("map")) readMap(str);
			else if (str.contains("rival message")) {
				String[] lStrings = str.split(" "); 
				String rival = lStrings[2].concat(" ".concat(lStrings[3]));
				String msg = str.split(" ", 6)[5];
				chat.receiveMessage(rival, msg);
			}
			else if (str.contains("team")) receiveTeam();
			else System.out.println("x"+str+"x");
		}
	}

	public boolean createGame(String gameName) throws IOException {
		writer.println("create game ".concat(gameName));
		writer.flush();
		String str = reader.readLine();
		System.out.println(str);
		return str.equals("game created");
	}

	public boolean joinGame(String gameName) throws IOException {
		writer.println("join game ".concat(gameName));
		writer.flush();
		return reader.readLine().equals("game joined");
	}

	public void readMap(String str) throws IOException {
		emptyList();
		System.out.println(str);
		int width = Integer.parseInt(str.split(" ")[1]);
		int height = Integer.parseInt(str.split(" ")[2]);
		System.out.println(height);
		for (int i = 0; i < height; i++) {
			serverOutput.add(reader.readLine());
			System.out.println(serverOutput.get(i));
		}
		map.drawMap(width);
	}

	public void sendMessage(String msg) {
		writer.println("send message ".concat(msg));
		writer.flush();
	}

	public void receiveTeam() throws IOException {
		emptyList();
		String str = reader.readLine();
		for (int i = 0; i < Integer.parseInt(str.split(" ")[2]); i++) {

		}
	}

	public void managePoketudiant(int pos, String direction, boolean move) {
		writer.println("poketudiant " + pos + (move ? " move ".concat(direction) : " free"));
	}

	public void move(Direction direction) {
		writer.println("map move ".concat(direction.label));
	}

	public void emptyList() {
		serverOutput.clear();
	}

	public List<String> getServerOutput() {
		return this.serverOutput;
	}

	public void setChat(Chat chat) {
		this.chat = chat;
	}

	public void setMap(Map map) {
		this.map = map;
	}

    public void close() {
        socket.close();
    }
}