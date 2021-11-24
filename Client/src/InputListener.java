import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Timer;
import java.util.TimerTask;

public class InputListener implements KeyListener{
    private Client client;
    private Timer timer;
    private TimerTask timertask;

    public InputListener(Client client) {
        super();
        this.client = client;
        timer = new Timer();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (timertask != null) return;

        timertask = new TimerTask() {
            @Override
            public void run() {
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
        };
        timer.scheduleAtFixedRate(timertask, 0, 600);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        timertask.cancel();
        timertask = null;
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    
}
