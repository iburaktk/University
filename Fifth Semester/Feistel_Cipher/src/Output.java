import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

public class Output {
	
    public static void output(PrintStream printStream,Boolean[] pieces) throws FileNotFoundException 
    {
        System.setOut(printStream);
        for (Boolean b:pieces) 
        {
            if(b)
                System.out.print("1");
            else 
            	System.out.print("0");
        }
    }
}
