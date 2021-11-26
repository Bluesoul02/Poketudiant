import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputListener implements KeyListener{
    private Client client;
    private final long threshold = 500;
    private long last = System.currentTimeMillis();

    public InputListener(Client client) {
        this.client = client;
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        long now = System.currentTimeMillis();
        if (now - last > threshold) {
            switch(e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    client.move(Direction.UP);
                    break;
                case KeyEvent.VK_DOWN:
                    client.move(Direction.DOWN);
                    break;
                case KeyEvent.VK_LEFT:
                    client.move(Direction.LEFT);
                    break;
                case KeyEvent.VK_RIGHT:
                    client.move(Direction.RIGHT);
                    break;
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    
}
