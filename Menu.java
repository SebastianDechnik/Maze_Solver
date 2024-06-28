import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Menu {
    private static boolean b = false;
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            createAndShowGUI();
        });
    }
    
    private static void createAndShowGUI() {
        
        JFrame mainFrame = new JFrame("Maze Solver");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int) screenSize.getWidth();
        int screenHeight = (int) screenSize.getHeight();

        int width = screenWidth / 2;
        int height = (int) ((double) 80 / 100 * screenHeight);
        mainFrame.setSize(width, height);
        mainFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(173, 216, 230));

        JButton button1 = new JButton("Znajdź najkrótszą ścieżkę");
        button1.setPreferredSize(new Dimension(200, 50));
        JButton button2 = new JButton("Wczytaj plik binarny");
        button2.setPreferredSize(new Dimension(200, 50));

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fileName = JOptionPane.showInputDialog(mainFrame, "Podaj nazwę pliku:", "Wczytaj plik", JOptionPane.PLAIN_MESSAGE);
                if (fileName != null && !fileName.isEmpty()) {
                    findShortestPath(fileName);
                }
            }
        });

        button2.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
              if(b==false){
                b = true;
              }
                String fileName = JOptionPane.showInputDialog(mainFrame, "Podaj nazwę pliku:", "Wczytaj plik", JOptionPane.PLAIN_MESSAGE);
                if (fileName != null && !fileName.isEmpty()) {
                    BinaryFileReader.binRead(fileName);
                    findShortestPath("decoded_maze.txt");
                }
            }
        });

        panel.add(button1);
        panel.add(button2);
        mainFrame.add(panel);
        mainFrame.setVisible(true);
    }

    private static void findShortestPath(String fileName) {
        Fileread f = new Fileread();
        /*if(b==false){
            int temp = f.width;
            f.width = f.height;
            f.height = temp;
        }*/
        try {
            //BufferedReader reader = new BufferedReader(new FileReader(fileName));
            f.read(fileName);
            System.out.println("x0: " + f.getStartX() + ", y0: " + f.getStartY() + ", xk: " + f.getEndX() + ", yk: " +f.getEndY() + ", width: " + f.getWidth()+ ", height: " + f.getHeight());
         //   reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        char [][] lab = Fileread.wczytajLabirynt(fileName);
        //Fileread.print(lab);

        GUI g = new GUI(lab,f);
        g.createMaze();
        //System.err.printf("x0=%d,y0=%d\n",f.getStartX(),f.getStartY());
        g.mainFrame.add(g.maze);
        g.colorStart(g.maze,f.getStartX(),f.getStartY(),f.getWidth());
        g.colorEnd(g.maze,f.getEndX(),f.getEndY(),f.getWidth());
        g.mainFrame.setVisible(true);
        g.start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                BFS bfs = new BFS(g.updatelab(), f.getStartX(), f.getStartY(), f.getEndX(), f.getEndY(), f.getWidth(), f.getHeight(), g);
                Thread t = new Thread(bfs);
                t.start();
            }
        });

       
    }
}
