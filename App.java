import javax.swing.*;
public class App{
public static void main(String[] args)throws Exception{
        int boardwidth = 1280;
        int boardheight = 720;

        JFrame frame = new JFrame("FLYING SHINCHAN");
        frame.setSize(boardwidth,boardheight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        FlyShinchan shinchan = new FlyShinchan();
        frame.add(shinchan);
        frame.pack();
        frame.setVisible(true);
}

}      