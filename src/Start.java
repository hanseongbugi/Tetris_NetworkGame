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
        setTitle("TETRIS");
        setSize(500, 500);
        setContentPane(new MyPanel());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container c = getContentPane();
        c.setLayout(new FlowLayout());
        JLabel id = new JLabel("ID");
        id.setBounds(1600, 1500, 100, 80);
        c.add(id);
        tf.setLocation(1600, 1600);
        c.add(tf);

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

        //bt.setBounds(1200, 500, 100, 80); //앞이 좌표 뒤가 크기
        setLayout(new FlowLayout(FlowLayout.CENTER));
        c.add(bt);
        setLocationRelativeTo(null);
        setVisible(true);

    }

    class MyPanel extends JPanel {
        private ImageIcon icon = new ImageIcon("Tetris.PNG");
        private Image img = icon.getImage();
        private Vector<Point> sv = new Vector<Point>();


        public MyPanel() {
            this.addComponentListener(new ComponentAdapter() {
                public void componentResized(ComponentEvent e) {

                    MyPanel.this.removeComponentListener(this);
                }
            });
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            g.setFont(new Font("Jokerman", Font.ITALIC, 55));
            g.setColor(Color.YELLOW);
            g.drawString("TETRIS", 50, 150);
        }
    }

    public static void main(String[] args) throws IOException {
//		new ImageSource();
//		new GameFrame();

        new Start();
        System.out.println("goodies");

    }
}
	

