import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;

public class Card {
    private Image face;
    private Image back;
    private final int value; // represents digit to display on card
    private boolean Select;
    private boolean canClick;
    private int x, y;
    public Card(int value){
        this.x = 0;
        this.y = 0;
        this.Select=false;
        this.canClick=false; //can be clicked
        this.value = value;
        String filePath="digits/"; // all digits and images used were hand drawn by me in MS Paint.
        filePath += value;
        File faceUpNumber = new File(filePath + ".png");
        File blankBack = new File("digits/Blank.png");
//        System.out.println(filePath + ".png");
        try{
            face = ImageIO.read(faceUpNumber);
            back = ImageIO.read(blankBack);
        }catch(Exception e){
            e.printStackTrace();
            System.err.println("file error");
        }
    }
    public boolean contains(int x, int y) {
        return this.x <= x && x <= this.x + face.getWidth(null) && this.y <= y && y <= this.y + face.getHeight(null);
    }

    public boolean toggleSelect() {
        this.Select = !this.Select;
        return this.Select;
    }

    public void paint(Graphics g){
        Image display;
        if(Select){
            display=face;
        }else{
            display=back;
        }
        g.drawImage(display, x, y, null);
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean getSelect(){
        return this.Select;
    }

    public void setSelect(boolean a){
        this.Select = a;
    }

    public void setCanClick(boolean canClick){
        this.canClick=canClick;
    }

    public boolean canClick(){
        return canClick;
    }

    public int getValue(){
        return value;
    }
}
