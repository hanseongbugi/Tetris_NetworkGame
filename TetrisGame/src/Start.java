import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.util.Vector;

public class Start extends JFrame {

    private JTextField tf = new JTextField(20);

    public Start() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setTitle("TETRIS");
        setSize(500, 500);

        JLabel logo = new JLabel("Tetris");
        logo.setFont(new Font("Jokerman", Font.ITALIC, 55));
        logo.setForeground(Color.YELLOW);

        JLabel id = new JLabel("ID");
        JButton bt = new JButton("START");

        bt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("CLICK START!!");
                String temp = tf.getText();
                if (temp.equals("")) {
                    JOptionPane.showMessageDialog(null, "ID가 입력되지 않았습니다.");
                } else {
                    try {
                        new ImageSource();
                        new GameFrame();

                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
                System.out.println(temp);
            }
        });

        logo.setSize(500,100);
        logo.setHorizontalAlignment(JLabel.CENTER);
        id.setSize(100, 80);
        tf.setSize(10, 10);
        bt.setSize(100, 80);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.add(id);
        panel.add(tf);
        panel.add(bt);
        add(logo);
        add(panel);

        setVisible(true);

    }

    public static void main(String[] args) throws IOException {
        new Start();
        System.out.println("goodies");

    }
}
	

