import javax.swing.*;

/**
 * Created by derkhumblet on 12/12/15.
 */
public class Printer extends JFrame {
    private JLabel label = new JLabel("Hello from Java Code Geeks!");

    public Printer() {
        super("Jave Web Start Example");
        this.setSize(350, 200);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
    }

    public void addButtons() {
        label.setSize(200, 30);
        label.setLocation(80, 50);
        this.getContentPane().add(label);
    }

    public static void main(String[] args) {
        Printer exp = new Printer();
        exp.addButtons();
        exp.setVisible(true);
    }

}
