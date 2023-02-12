import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class GameWindow extends JPanel implements MouseListener, MouseMotionListener {

    private int numCardsMin = 5; // the number of cards cannot go below this value
    private int numCards = numCardsMin; // number of cards --> represents difficulty level
    private final int cardDim = 32;
    private Card[] cards = makeCardSet(); // card dim: 32x32
    static final Color black = new Color(16, 16, 16); // black background color
    static final Color almostBlack = new Color(30, 30, 30); // screen background color
    static final Color grayEdge = new Color(59, 59, 59); // screen background color
    static final Color textColor = new Color(170, 170, 170); // screen background color
    private final int buttonHeight = Main.buttonPad; //height of button
    private final int buttonWidth = 128; // 32x4
    private final int buttonEdgeWidth = 2;
    private final int hitboxPadding = 4; // to aid people smacking the button
    private final int scoreBoxWidth = 48;
    private int widthBoxesNum=Main.width/cardDim -1; // num spaces for cards horizontally
    private int heightBoxesNum=(Main.height-buttonHeight-Main.sidePad)/cardDim -1; //num spaces for cards vertically
    static Color one = new Color(82, 234, 34);
    static Color two = new Color(253, 213, 8);
    static Color three = new Color(255, 22, 14);
    private Color[] colors = new Color[]{one, two, three};
    private final int fontSize = 24;
    private final Font myFont = new Font("Serif", Font.BOLD, fontSize);
    private final Font bigFont = new Font("Serif", Font.BOLD, fontSize*2);
    private final Font smallFont = new Font("Serif", Font.PLAIN, (int)(fontSize/1.2));
    private boolean countdown = false; // should the countdown be shown?
    private boolean revealed = false; // have the values been shown?
    private int count = 0; // countdown counter
    private int score = 0; // score
    private int totalScore = 0; // max score
    private int pickNumber = 0; // pick number representing the order of selection of card
    private int levelEndText = -1; //-1 for no message, 0 for lose message, 1 for win message
    private boolean randomized = true;

    public GameWindow() {
        super();
        setBackground(almostBlack);
        placeCards(cards);
        addMouseListener(this);
        addMouseMotionListener(this);
        repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for(Card card: cards) {
            card.paint(g);
        }
        createButton(g);
        createScoreBoxes(g);
//        madeDivider(g);
        if(countdown) {
            threeStepCountdown(g);

        }else{
            createButtonMessageDefault(g);
        }
        levelEndTextMessage(g, levelEndText);
        repaint();
    }

    public Card[] makeCardSet(){
        Card[] temp = new Card[numCards];
        for(int i = 0; i < numCards; i++){
            temp[i] = new Card(i);
        }
        return temp;
    }

    public void placeCards(Card[] toPlace){
        ArrayList<Integer> takenVertical = new ArrayList();
        ArrayList<Integer> takenHorizontal = new ArrayList();
        for(Card c: toPlace){
            int choiceHeight = (int)(Math.random()*heightBoxesNum);
            while(takenVertical.contains(choiceHeight)){
                choiceHeight = (int)(Math.random()*heightBoxesNum);
            }
            int choiceWidth = (int)(Math.random()*widthBoxesNum);
            while(takenHorizontal.contains(choiceWidth)){
                choiceWidth = (int)(Math.random()*widthBoxesNum);
            }
            takenVertical.add(choiceHeight);
            takenHorizontal.add(choiceWidth);
            c.setX(Main.sidePad+choiceWidth*cardDim);
            c.setY(buttonHeight+choiceHeight*cardDim);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
//        if(insideButton(e)){
//            if(isAllSelected()){
//                level++;
//                unselectAll();
//            }
//        }
//        for (Card card : cards) {
//            if(card.contains(e.getX(), e.getY())){
//                if(!card.getSelect()){
//                    card.toggleSelect();
//                }
//            }
//        }
//        this.repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
//        System.out.println(e.getX()+", "+e.getY());
        if(insideButton(e)&&revealed&&cards[0].canClick()){
            if(isAllSelected()){
                reset();
            }
        }else if(insideButton(e)&&!revealed){
            countdown=true;
            revealed=true;
            randomized=false;
        }
        for (Card card : cards) {
            if(card.contains(e.getX(), e.getY()) && !card.getSelect() && card.canClick()){
                if(!card.getSelect()){
                    card.toggleSelect();
                }
                addScore(card);
            }
        }
        this.repaint();
    }

    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
        this.repaint();
        //end repaint loop
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    public void customDelay(long delay){
        try{
            Thread.sleep(delay);
        }catch(Exception ignored){}
    }

    public boolean isAllSelected(){
        boolean result = true;
        for(Card c: cards){
            if(!c.getSelect()){
                result=false;
                break;
            }
        }
        return result;
    }

    public void unselectAll(){
        for(Card c: cards){
            c.setSelect(false);
        }
    }

    public void selectAll(){
        for(Card c: cards){
            c.setSelect(true);
        }
    }

    public void setAllUnclickable(){
        for(Card c: cards){
            c.setCanClick(false);
        }
    }

    public void setAllClickable(){
        for(Card c: cards){
            c.setCanClick(true);
        }
    }

    public void reset(){
        cards=makeCardSet();
        revealed=false;
        unselectAll();
        placeCards(cards);
        pickNumber=0;
        setAllUnclickable();
        levelEndText=-1;
        randomized=true;
    }

    public void addScore(Card card){
        if(card.getValue() == pickNumber){
//            System.out.println("Pick: "+pickNumber+", Value:"+card.getValue());
            score++;
            if(pickNumber==cards.length-1){
                levelEndText=1;
                numCards=Math.min(10, numCards+1);
//                System.out.println("Level Up:"+numCards);
            }
        }else{
            selectAll();
            levelEndText=0;
            if(score > totalScore){
                totalScore=score;
            }
            score=0;
            numCards=numCardsMin;
        }
        pickNumber=(pickNumber+1)%10;
    }

    public void createButton(Graphics g){
        double sidePadCof = 3.0;
        g.setColor(textColor); // outer edge
        g.fillRoundRect(Main.width/2-buttonWidth/2-buttonEdgeWidth-Main.sidePad, (int)sidePadCof*Main.sidePad-buttonEdgeWidth-6, buttonWidth+2*buttonEdgeWidth, buttonHeight-(int)sidePadCof*2*Main.sidePad+2*buttonEdgeWidth, 30, 30);
        g.setColor(grayEdge); // grey edge
        g.fillRoundRect(Main.width/2-buttonWidth/2-Main.sidePad, (int)sidePadCof*Main.sidePad-6, buttonWidth, buttonHeight-(int)sidePadCof*2*Main.sidePad, 30, 30);
        g.setColor(black); // inside of button
        g.fillRoundRect(Main.width/2-buttonWidth/2+buttonEdgeWidth-Main.sidePad, (int)sidePadCof*Main.sidePad+buttonEdgeWidth-6, buttonWidth-2*buttonEdgeWidth, buttonHeight-(int)sidePadCof*2*Main.sidePad-2*buttonEdgeWidth, 30, 30);
    }

    public boolean insideButton(MouseEvent e){
        int leftEdge = Main.width/2-buttonWidth/2-buttonEdgeWidth-Main.sidePad;
        int rightEdge = leftEdge + buttonWidth+2*buttonEdgeWidth;
        int topEdge = 3*Main.sidePad-buttonEdgeWidth-6;
        int bottomEdge = topEdge+buttonHeight-6*Main.sidePad+2*buttonEdgeWidth;
        return (e.getX() > leftEdge-hitboxPadding && e.getX() < rightEdge+hitboxPadding && e.getY() > topEdge-hitboxPadding && e.getY() < bottomEdge+hitboxPadding);
    }

    public void createButtonMessageDefault(Graphics g) {
        int fontSize = 24;
        Font myFont = new Font("Serif", Font.BOLD, fontSize);
        g.setFont(myFont);
        g.setColor(textColor);
        String text="";
        if(randomized) {
            text = "Play!";
        }else{
            text = "Randomize!";
        }
        g.drawString(text, (Main.width-g.getFontMetrics().stringWidth(text))/2-11, (buttonWidth+Main.sidePad)/2-14);
    }

    public void threeStepCountdown(Graphics g){
        if(count==0) {
            selectAll();
        }else if(count<4){
            g.setFont(myFont);
            g.setColor(colors[count-1]);
            g.drawString("" + (count), Main.width / 2 - Main.sidePad -4, (buttonWidth + Main.sidePad) / 2 - 14);
            System.out.println("COUNTDOWN");
            customDelay(1000);
        }else{
            g.setColor(colors[2]);
            g.drawString("3", Main.width / 2 - Main.sidePad -4, (buttonWidth + Main.sidePad) / 2 - 14);
            System.out.println("COUNTDOWN");
            customDelay(1000);
        }
        if (count>=4) {
            unselectAll();
            setAllClickable();
            count=-1;
            revealed = true;
            countdown = false;
        }
        count++;
    }

    public void createScoreBoxes(Graphics g){
        createScoreBox(g);
        createScoreText(g);
        createtotalScoreBox(g);
        createtotalScoreText(g);
    }

    public void createScoreText(Graphics g) {
        g.setFont(myFont);
        g.setColor(textColor);
        if (score < 10) {
            g.drawString("" + score, Main.width / 2 - buttonWidth / 2 - buttonEdgeWidth - 2 * Main.sidePad - scoreBoxWidth / 2 - 6, (buttonWidth + Main.sidePad) / 2 - 14);
        }else{
            g.drawString("" + score, Main.width / 2 - buttonWidth / 2 - buttonEdgeWidth - 2 * Main.sidePad - scoreBoxWidth / 2 - 11, (buttonWidth + Main.sidePad) / 2 - 14);
        }
    }
    public void createtotalScoreText(Graphics g){
        g.setFont(myFont);
        g.setColor(textColor);
        if(totalScore < 10) {
            g.drawString("" + totalScore, Main.width / 2 + buttonWidth / 2 + buttonEdgeWidth + 18, (buttonWidth + Main.sidePad) / 2 - 14);
        }else {
            g.drawString("" + totalScore, Main.width / 2 + buttonWidth / 2 + buttonEdgeWidth + 13, (buttonWidth + Main.sidePad) / 2 - 14);
        }
    }

    public void createScoreBox(Graphics g){
        int leftBoxLeftEdge = Main.width/2-buttonWidth/2-buttonEdgeWidth-2*Main.sidePad-scoreBoxWidth;
        int topEdge = 3*Main.sidePad-buttonEdgeWidth-6;
        g.setColor(textColor); // outer edge
        g.fillRoundRect(leftBoxLeftEdge, topEdge, scoreBoxWidth, buttonHeight-6*Main.sidePad+2*buttonEdgeWidth, 30, 30);
        g.setColor(grayEdge); // grey edge
        g.fillRoundRect(leftBoxLeftEdge+buttonEdgeWidth, topEdge+buttonEdgeWidth, scoreBoxWidth-2*buttonEdgeWidth, buttonHeight-6*Main.sidePad, 30, 30);
        g.setColor(black); // inside of button
        g.fillRoundRect(leftBoxLeftEdge+2*buttonEdgeWidth, topEdge+2*buttonEdgeWidth, scoreBoxWidth-4*buttonEdgeWidth, buttonHeight-6*Main.sidePad-2*buttonEdgeWidth, 30, 30);
    }

    public void createtotalScoreBox(Graphics g){
        int leftBoxLeftEdge = Main.width/2+buttonWidth/2+buttonEdgeWidth;
        int topEdge = 3*Main.sidePad-buttonEdgeWidth-6;
        g.setColor(textColor); // outer edge
        g.fillRoundRect(leftBoxLeftEdge, topEdge, scoreBoxWidth, buttonHeight-6*Main.sidePad+2*buttonEdgeWidth, 30, 30);
        g.setColor(grayEdge); // grey edge
        g.fillRoundRect(leftBoxLeftEdge+buttonEdgeWidth, topEdge+buttonEdgeWidth, scoreBoxWidth-2*buttonEdgeWidth, buttonHeight-6*Main.sidePad, 30, 30);
        g.setColor(black); // inside of button
        g.fillRoundRect(leftBoxLeftEdge+2*buttonEdgeWidth, topEdge+2*buttonEdgeWidth, scoreBoxWidth-4*buttonEdgeWidth, buttonHeight-6*Main.sidePad-2*buttonEdgeWidth, 30, 30);
    }

    public void madeDivider(Graphics g){
        g.setColor(black);
        g.fillRect(0, Main.buttonPad-Main.sidePad, Main.width, 2*buttonEdgeWidth);
    }

    public void levelEndTextMessage(Graphics g, int levelEndText){
        if(levelEndText==0){
            g.setFont(bigFont);
            g.setColor(two);
            String text = "Better Luck Next Time!";
            g.drawString(text, (Main.width-g.getFontMetrics().stringWidth(text))/2, (Main.height-g.getFontMetrics().getAscent())/2);
            int ascent = g.getFontMetrics().getAscent();
            g.setFont(smallFont);
            g.setColor(three);
            text = "(Click 'Randomize!' to Reset!)";
            g.drawString(text, (Main.width-g.getFontMetrics().stringWidth(text))/2, (Main.height-g.getFontMetrics().getAscent()+ascent)/2);
        }else if(levelEndText==1){
            g.setFont(bigFont);
            g.setColor(one);
            String text = "Nice Work!";
            g.drawString(text, (Main.width-g.getFontMetrics().stringWidth(text))/2, (Main.height-g.getFontMetrics().getAscent())/2);
            int ascent = g.getFontMetrics().getAscent();
            g.setFont(smallFont);
            g.setColor(one);
            text = "(Click 'Randomize!' to Reset!)";
            g.drawString(text, (Main.width-g.getFontMetrics().stringWidth(text))/2, (Main.height-g.getFontMetrics().getAscent()+ascent)/2);
        }
    }
}
