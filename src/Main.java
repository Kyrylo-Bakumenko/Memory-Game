import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

public class Main {
    static int sidePad = 12; // pad for left of screen
    static int buttonPad = 96 + sidePad; // 32*3 pad for button
    static int width = 576 + 2*sidePad; //32x18
    static int height = 384 + buttonPad + sidePad; //32x12 + pad for button + bottom pad

    // UIManager keys list found on https://gist.github.com/itzg/5938035

    public static void main(String[] args) throws IOException{
        UIDefaults uiDefaults = UIManager.getDefaults();
        uiDefaults.put("activeCaption", new javax.swing.plaf.ColorUIResource(GameWindow.almostBlack));
        uiDefaults.put("OptionPane.background",new ColorUIResource(GameWindow.almostBlack));
        uiDefaults.put("Panel.background",new ColorUIResource(GameWindow.almostBlack));
//        JFrame.setDefaultLookAndFeelDecorated(true);

        JFrame window = new JFrame();
        window.setSize(width, height);
        window.setLocationRelativeTo(null);
        window.setFocusable(false);
        window.setResizable(false);
        window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        window.setIconImage(ImageIO.read(new File("digits/1.png")));
        window.add(new GameWindow());
        window.setVisible(false);

        JFrame welcome = new JFrame();
        welcome.add(new WelcomeScreen());
        welcome.setIconImage(ImageIO.read(new File("digits/1.png")));
        WindowAdapter myWelcome = new WindowAdapter() {
            public void windowOpened(WindowEvent e) {
                welcome.setSize(width, height);
                welcome.setLocationRelativeTo(window);
                welcome.setFocusable(false);
                welcome.setResizable(false);
                welcome.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            }

            public void windowClosing(WindowEvent e) {
                JLabel label = new JLabel("<html><center>'Yes'  to go to Game<br>'No'  to Continue Reading");
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setFont(new Font("Serif", Font.BOLD, 18));
                label.setForeground(GameWindow.two);
                int answer=JOptionPane.showConfirmDialog(welcome, label, "Continue?", JOptionPane.YES_NO_OPTION, JOptionPane.DEFAULT_OPTION);
                window.setLocationRelativeTo(welcome);
                if (answer == JOptionPane.YES_OPTION) {
                    welcome.setVisible(false);
                    window.setVisible(true);
                } else if (answer == JOptionPane.NO_OPTION) {
                    welcome.setVisible(true);
                }
            }
        };
        welcome.addWindowListener(myWelcome);
        welcome.setVisible(true);

        WindowAdapter myWindow = new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                JLabel label = new JLabel("<html><center>'Yes'  to Quit<br>'No'  to Stay");
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setFont(new Font("Serif", Font.BOLD, 18));
                label.setForeground(GameWindow.two);
                int answer = JOptionPane.showConfirmDialog(window, label, "Exit Game?", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (answer==JOptionPane.YES_OPTION) {
                    welcome.setVisible(false);
                    window.setVisible(false);
                    welcome.dispose();
                    window.dispose();
                }else{
                    window.setVisible(true);
                }
            }
        };
        window.addWindowListener(myWindow);
    }
}

