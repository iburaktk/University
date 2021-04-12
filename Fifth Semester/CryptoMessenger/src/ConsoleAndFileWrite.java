import java.io.PrintStream;

public class ConsoleAndFileWrite {
    public static void write(PrintStream fileOut, PrintStream defaultOut,String string){
        System.setOut(fileOut); // writing to log.txt
        System.out.println(string);
        System.out.flush();
        System.setOut(defaultOut); // writing to the console
        System.out.println(string);
        System.out.flush();
    }
}