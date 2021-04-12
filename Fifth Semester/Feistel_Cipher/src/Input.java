import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class Input
{
    private String[] args;
    public Input(String[] argsInput){
        this.args = argsInput;
    }
    public void execute() throws Exception {
        HashMap<String,String> parameters = new HashMap<>();
//        HashMap<String, HashMap<String,String>> substitutionBox = new HashMap<>();
//        String[][] subsBox = new String[4][16];

        parameters.put("encordec",args[0]);
        for(int i =1;i<args.length;i=i+2){
            parameters.put(args[i],args[i+1]);
        }
        boolean[] inputFiles = new boolean[96];
        //System.out.println(bitToNum("1110")+" " +bitToBool("1100")[0] +" " + boolToBit(new boolean[]{true,false,true,false}));
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(parameters.get("-I")), StandardCharsets.UTF_8));
        int character =0;
        int numberOfCharIsRead = 0;
        while((character =bufferedReader.read() )!= -1){
            if(numberOfCharIsRead % 96 ==95){
                feistel(inputFiles);
                numberOfCharIsRead = 0;
                continue;
            }
            inputFiles[numberOfCharIsRead] = character == '1';
            numberOfCharIsRead++;

        }
        if(numberOfCharIsRead % 96 !=95){
            for(int a =numberOfCharIsRead+1;a<96;a++){
                inputFiles[a] = false;
            }
            feistel(inputFiles);
            //numberOfCharIsRead = 0;
        }


        // write your code here
    }
}
