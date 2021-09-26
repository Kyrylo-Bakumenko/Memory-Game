import javax.swing.*;
import java.awt.*;

public class WelcomeScreen extends JPanel{

    private final Color almostBlack = GameWindow.almostBlack; // welcome screen background color
    private final Color textColor = GameWindow.textColor; // welcome screen text color
    private final Color altTextColor = GameWindow.two; // welcome screen alternative text color
    private final int offsetti = Main.sidePad*2; //random offset amount for visual benefit of text and images

    public WelcomeScreen() {
        super();
        setBackground(almostBlack);
        repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(almostBlack);
        addWelcomeText(g);
        repaint();
    }

    public void addWelcomeText(Graphics g){
        int bigGap = 50;
        int smallGap = 30;
        int startText = Main.buttonPad/2;
        Font myFont = new Font("Serif", Font.BOLD, 36);
        g.setFont(myFont);
        g.setColor(textColor);
        String text = "Welcome to my Memory Game!";
        g.drawString(text, (Main.width-g.getFontMetrics().stringWidth(text))/2, startText);
        myFont = new Font("Serif", Font.PLAIN, 24);
        g.setFont(myFont);
        text="\n Goal: Click Every Number Card";
        g.drawString(text, offsetti, startText+g.getFontMetrics().getAscent() + bigGap);
        text="\n Rules: ";
        g.drawString(text, offsetti, startText+g.getFontMetrics().getAscent() + 2*bigGap);
        g.setColor(altTextColor);
        text="\n\t -Press Play to Reveal Cards for 3 Seconds";
        g.drawString(text, offsetti + smallGap, startText+g.getFontMetrics().getAscent() + 3*bigGap);
        text="\n\t -Click all Cards in Increasing Order";
        g.drawString(text, offsetti + smallGap, startText+g.getFontMetrics().getAscent() + 3*bigGap + 1*smallGap);
        text="\n\t -Start at 0";
        g.drawString(text, offsetti + smallGap, startText+g.getFontMetrics().getAscent() + 3*bigGap + 2*smallGap);
        text="\n\t -Fail, and you Restart!";
        g.drawString(text, offsetti + smallGap, startText+g.getFontMetrics().getAscent() + 3*bigGap + 3*smallGap);
        text="\n\t -Each Completed Round Difficulty Increases";
        g.drawString(text, offsetti + smallGap, startText+g.getFontMetrics().getAscent() + 3*bigGap + 4*smallGap);
        text="\n\t -Press 'Randomize!' to Play Again";
        g.drawString(text, offsetti + smallGap, startText+g.getFontMetrics().getAscent() + 3*bigGap + 5*smallGap);
        g.setColor(textColor);
        text="\n Fun! Exit this window to begin!";
        myFont = new Font("TNR", Font.ITALIC, 24);
        g.setFont(myFont);
        g.drawString(text, offsetti, startText+g.getFontMetrics().getAscent() + 4*bigGap + 5*smallGap);

    }
}