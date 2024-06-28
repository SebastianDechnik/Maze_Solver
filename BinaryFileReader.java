import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.print.StreamPrintService;
import java.io.RandomAccessFile;
import java.io.FileNotFoundException;


public class BinaryFileReader {
    public static void binRead(String fileName) {
        Fileread f = new Fileread();
        try (DataInputStream inputStream = new DataInputStream(new FileInputStream(fileName))) {

            RandomAccessFile randomAccessFile = new RandomAccessFile(fileName, "r");
            int fileID = Integer.reverseBytes(randomAccessFile.readInt());
            int escape = randomAccessFile.read();
            int columns = Short.reverseBytes(randomAccessFile.readShort());
            int lines = Short.reverseBytes(randomAccessFile.readShort());
            int entryX = Short.reverseBytes(randomAccessFile.readShort());
            int entryY = Short.reverseBytes(randomAccessFile.readShort());
            int exitX = Short.reverseBytes(randomAccessFile.readShort());
            int exitY = Short.reverseBytes(randomAccessFile.readShort());
            randomAccessFile.skipBytes(12);
            int counter = Integer.reverseBytes(randomAccessFile.readInt());
            randomAccessFile.readInt();
            char separator = (char) randomAccessFile.read();
            char wall = (char) randomAccessFile.read();
            char path = (char) randomAccessFile.read();

            int newline=0;
            int newcol = 0;
            int stepx=3;
            int stepy=0;
            FileOutputStream decodedMaze = new FileOutputStream("decoded_maze.txt");
            for (int i = 0; i < counter; i++) {
                separator = (char) randomAccessFile.read();
                char value = (char) randomAccessFile.read();
                int count = randomAccessFile.read();
                
                for (int j = 0; j <= count; j++) {
                    if(stepx == entryX && stepy+1 == entryY){
                        decodedMaze.write('P');
                        newline++;
                        stepx++;
                        
                    }
                    else if(stepx == exitX && stepy+1 == exitY){
                        decodedMaze.write('K');
                        newline++;
                        stepx++;
                    }
     
                    else if (value == wall) {
                        decodedMaze.write('X');
                        newline++;
                        stepx++;
                    } else if (value == path) {
                        decodedMaze.write(' ');
                        newline++;
                        stepx++;
                     
                    }
                    if (newline==columns) {
                        stepy++;
                        if (stepy < lines-1)
                        decodedMaze.write('\n');
                        newline=0;
                        stepx=1;
                    }
                }
            }
            
            
            decodedMaze.close();
            System.out.println("Labirynt został zdekodowany i zapisany do pliku decoded_maze.txt");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}