import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Chat extends JPanel{
    private DefaultListModel<String> data;
    private JList<String> chat;
    private Client client;
    
    public Chat(Client client) {
        this.client = client;
        client.setChat(this);
        setBackground(Color.decode("#f5f0e1"));
        data = new DefaultListModel<String>();
        chat = new JList<String>(data);
        JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(200, 30));
        textField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendMessage(textField.getText());
                //data.addElement(textField.getText());
                textField.setText("");
            }
        });
        chat.setPreferredSize(new Dimension(200, Toolkit.getDefaultToolkit().getScreenSize().height - 100));
        add(chat);
        add(textField);
        System.out.println("chatPanel");
    }

    public void receiveMessage(String rival, String msg) {
        data.addElement(rival.concat(": ").concat(msg));
        revalidate();
        repaint();
    }

    public void sendMessage(String msg) {
        client.sendMessage(msg);
    }

}
