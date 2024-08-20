import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class FlyShinchan extends JPanel implements ActionListener, KeyListener{
    int boardwidth = 1280;
    int boardheight = 720;

    //IMAGES
    Image bgImg;
    Image shinchanImg;
    Image topPipe;
    Image bottomPipe;

    //SHINCHAN CLASS
    int shinchanX = boardwidth/9;
    int shinchanY = boardheight/2;
    int shinchanWidth = 55;
    int shinchanHeight = 44;


        class Player{
        int x = shinchanX;
        int y = shinchanY;
        int width = shinchanWidth;
        int height = shinchanHeight;
        Image img;

        Player(Image img){
            this.img = img;    
            }
            }

            //PIPE CLASS
            int pipex = boardwidth;
            int pipey = 0;
            int pipeWidth = 54;  //SCALED BY 1/6
            int pipeHeight = 512;

            class Pipe{
                int x = pipex;
                int y = pipey;
                int width = pipeWidth;
                int height = pipeHeight;
                Image img;
                boolean passed = false;

                Pipe(Image img){
                    this.img = img;
                }

            }

        

            //GAME LOGIC
            Player s;
            int velocityx = -7;//MOVES PIPES TO THE LEFT SPEED(STIMULATES SHINCHAN MOVING RIGHT)
            int velocityy = 0;//MOVES BIRD UP/DOWN SPEED.
            int gravity =1;

            ArrayList<Pipe> pipes;
            Random random = new Random();

            Timer gameLoop;
            Timer placePipeTimer;
            boolean gameOver = false;
            double score =0;

        


        FlyShinchan() {
        setPreferredSize(new Dimension(boardwidth,boardheight));
       // setBackground(Color.yellow);
        setFocusable(true);
        addKeyListener(this);

        // LOAD IMAGES
        bgImg = new ImageIcon(getClass().getResource("./bg2.jpg")).getImage();
        shinchanImg = new ImageIcon(getClass().getResource("./shinchanImg.png")).getImage();
        topPipe = new ImageIcon(getClass().getResource("./topPipe.jpg")).getImage();
        bottomPipe = new ImageIcon(getClass().getResource("./bottomPipe.jpg")).getImage();

        
        //SHINCHAN
        s = new Player(shinchanImg);
        pipes = new ArrayList<Pipe>();

        //PLACE PIPES TIMER
        placePipeTimer = new Timer(1600,new ActionListener() {
           // @Override
            public void actionPerformed(ActionEvent e){
                //CODE TO BE EXECUTED...
                placePipes();
            }
        });
        placePipeTimer.start();

                //GAME TIMER
                gameLoop = new Timer(1000/60,this);//HOW LONG IT TAKES TO START TIMER, MILLISECONDS GONE BETWEEN FRAMES
            gameLoop.start();

    }


    void placePipes(){
        //(0-1) * pipeheight/4.
        // 0 -> - -138 (pipeheight/6)
        // 1 -> - -138(pipeheight/6-pipeheight/4) = -3/4 pipeheight
        int randomPipey = (int) (pipey - pipeHeight/6 - Math.random()*(pipeHeight/4));
        int openingSpace = boardheight/6;

        Pipe tPipe = new Pipe(topPipe);
        tPipe.y = randomPipey;
        pipes.add(tPipe);

        Pipe bPipe = new Pipe(bottomPipe);
        bPipe.y = tPipe.y + pipeHeight + openingSpace;
        pipes.add(bPipe);
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        //DRAW BACKGROUND
        g.drawImage(bgImg,0,0,this.boardwidth,this.boardheight,null);

        //SHINCHAN
        g.drawImage(shinchanImg,s.x,s.y,s.width,s.height,null);

        //PIPES
        for(int i = 0;i < pipes.size(); i++){
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.img,pipe.x,pipe.y,pipe.width,pipe.height,null);
        }

            //SCORE
            g.setColor(Color.white);

            g.setFont(new Font("Arial", Font.PLAIN,52));
            if(gameOver){
                g.drawString("GAME OVER :-  "+ String.valueOf((int) score),10,35);
            }
            else{
                g.drawString(String.valueOf((int) score),10,35);
            }
    }
    public void move(){
        //SHINCHAN
        velocityy += gravity;
        s.y += velocityy;
        s.y = Math.max(s.y,0);//APPLY GRAVITY TO shinchan.y,LIMIT THE  shinchan TO THE TOP

        //PIPES
        for( int i = 0; i < pipes.size(); i++){
            Pipe pipe = pipes.get(i);
            pipe.x += velocityx;

            if(!pipe.passed && s.x > pipe.x + pipe.width){
                score += 0.5; //0.5 BCOZ THERE ARE 2 PIPES SO, 0.5*2 = 1 ,1 FOR SET OF PIPES
                pipe.passed = true;
            }

            if (collision(s, pipe)){
                gameOver = true;
            }
        }

        if(s.y > boardheight){
            gameOver = true ;
        }
    }

    boolean collision(Player a, Pipe b){
        return a.x < b.x + b.width && //a's TOP LEFT CORNER DOESN'T RACH b's TOP RIGHT CORNER
               a.x + a.width > b.x && //a's TOP RIGHT CORNER PASSED b's TOP LEFT CORNER
                a.y < b.y + b.height && //a's TOP LEFT CORNER DOESN'T REACH b's BOTTEM LEFT CORNER
                a.y + a.height > b.y;
    }

    public void actionPerformed(ActionEvent e){
        move();
        repaint();
        if (gameOver){
            placePipeTimer.stop();
            gameLoop.stop();
        }
    }

    public void keyPressed(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            //System.out.println("JUMP...!");
            velocityy = -9;

            if(gameOver){
                //RESTART GAME BY RESETTING CONDITIONS
                s.y = shinchanY;
                velocityy = 0;
                pipes.clear();
                gameOver = false;
                score = 0;
                gameLoop.start();
                placePipeTimer.start();
            }
        }
    }
    /*public void keyTyped(KeyEvent e){}

    @Override
    public void KeyReleased(KeyEvent e){}*/

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'keyTyped'");
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'keyReleased'");
    }

    
}
