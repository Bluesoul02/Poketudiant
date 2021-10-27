import java.io.IOException;

public class Main {
        public static void main(String[] args) throws IOException {
        Client c = new Client();
        c.send("looking for poketudiant servers");
        String recv = c.receive();
        System.out.println(recv);
    }
}
