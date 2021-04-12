import java.io.BufferedReader;
import java.io.File;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Scanner;

public class Operations {
	
    public static Boolean[] bitToBool(String key)				// String bits to boolean array
    {
        Boolean[] booleans = new Boolean[96];
        for(int i =0;i<key.length();i++)
        {
            booleans[i] =key.charAt(i) == '1';
        }
        return booleans;
    }
                   
    public static void ecb(BufferedReader bufferedReader,File outputFile, String keyString, int mode) throws Exception
    {
    	// ECB mode
        Boolean[] block = new Boolean[96];
        Boolean[] key = bitToBool(keyString);
        Boolean[] tempkey = bitToBool(keyString);
        int character =0;
        PrintStream fileWrite = new PrintStream(outputFile);
        int numberOfCharIsRead = 0;
        while((character =bufferedReader.read() )!= -1)	// getting bits up to EOF
        {
            block[numberOfCharIsRead] = character == '1';
            if(numberOfCharIsRead == 95)				// every 96 bit is a block. Process it.
            {
                feistel(block,key,mode);
                key = bitToBool(keyString);				// refreshing key for next process
                numberOfCharIsRead = 0;
                Output.output(fileWrite,block);
                continue;
            }
            numberOfCharIsRead++;
        }
        if(numberOfCharIsRead != 0){
            for(int a =numberOfCharIsRead;a<96;a++)
                block[a] = false;						// completing bits with zeros up to 96 bits
            feistel(block,key,mode);
            Output.output(fileWrite,block);
        }
    }

    public static void cbc(BufferedReader bufferedReader,File outputFile, String keyString, int mode) throws Exception
    {
    	// CBC mode
        Boolean[] block = new Boolean[96];
        Boolean[] vector = new Boolean[96];
        Boolean[] key = bitToBool(keyString);
        Arrays.fill(vector, true);
        int character =0;
        int numberOfCharIsRead = 0;
        PrintStream fileWrite = new PrintStream(outputFile);
        while((character =bufferedReader.read() )!= -1)	// getting bits up to EOF
        {	
            block[numberOfCharIsRead] = character == '1';
            if(numberOfCharIsRead == 95)				// every 96 bit is a block. Process it.
            {
                numberOfCharIsRead = 0;
                if (mode == 1)							// encryption mode
                {
                	for (int i=0;i<96;i++)
                        block[i] = block[i] ^ vector[i];	// XORing block and vector
                    feistel(block, key, mode);
                    for (int i=0;i<96;i++)
                        vector[i] = block[i];				// generating next vector
                }	
                if (mode == 0)							// decryption mode
                {
                	Boolean[] nextVector = new Boolean[96];
                	for (int i=0;i<96;i++)
                		nextVector[i] = block[i];		// generating next vector
                	feistel(block, key, mode);
                	for (int i=0;i<96;i++)
                	{
                        block[i] = block[i] ^ vector[i];	// XORing block and vector
                        vector[i] = nextVector[i];
                	}
                }
                Output.output(fileWrite,block);
                key = bitToBool(keyString);				// refreshing key for next process
                continue;
            }
            numberOfCharIsRead++;
        }
        if(numberOfCharIsRead != 0)
        {
            for(int a =numberOfCharIsRead+1;a<96;a++)
                block[a] = false;						// completing bits with zeros up to 96 bits
            if (mode == 1)								// encryption mode
            {
            	for (int i=0;i<96;i++)
                    block[i] = block[i] ^ vector[i];
                feistel(block, key, mode);
                for (int i=0;i<96;i++)
                    vector[i] = block[i];
            }
            if (mode == 0)								// decryption mode
            {
            	Boolean[] nextVector = new Boolean[96];
            	for (int i=0;i<96;i++)
            		nextVector[i] = block[i];
            	feistel(block, key, mode);
            	for (int i=0;i<96;i++)
            	{
                    block[i] = block[i] ^ vector[i];
                    vector[i] = nextVector[i];
            	}
            }
            Output.output(fileWrite,block);
        }
    }

    public static void ofb(BufferedReader bufferedReader, File outputFile, String keyString, int mode) throws Exception
    {
    	// OFB mode
        Boolean[] block = new Boolean[96];
        Boolean[] vector = new Boolean[96];
        Boolean[] key = bitToBool(keyString);
        Arrays.fill(vector, true);
        int character =0;
        PrintStream fileWrite = new PrintStream(outputFile);
        int numberOfCharIsRead = 0;
        while((character =bufferedReader.read() )!= -1)	// getting bits up to EOF
        {
            block[numberOfCharIsRead] = character == '1';
            if(numberOfCharIsRead == 95)					// every 96 bit is a block. Process it.
            {
                numberOfCharIsRead = 0;
                feistel(vector, key, 1);
                key = bitToBool(keyString);					// refreshing key for next process
                for (int i=0;i<96;i++)
                    block[i] = block[i] ^ vector[i];		// XORing block and vector
                Output.output(fileWrite,block);
                continue;
            }
            numberOfCharIsRead++;
        }
        if(numberOfCharIsRead != 0)
        {
            for(int a =numberOfCharIsRead+1;a<96;a++)
                block[a] = false;							// completing bits with zeros up to 96 bits
            feistel(vector, key, 1);
            key = bitToBool(keyString);
            for (int i=0;i<96;i++)
                block[i] = block[i] ^ vector[i];
            Output.output(fileWrite,block);
        }
    }

    public static void feistel(Boolean[] block, Boolean[] key, int mode) // feistel function
    {
        Boolean[][] dividedBlocks = new Boolean[2][48];
        Boolean[] temp = new Boolean[48];
        int padding;									// padding for encryption or decryption
        if (mode == 1)
            padding = 0;
        else
            padding = 48;
        if (mode == 0)
        	for (int i=0;i<11;i++)
        		generateSubkey(key, i, 1);			// if its decryption, do left circular shift 10 times.
        											// Because in decryption we must give keys reverse
        for (int j=0;j<10;j++)
        {
            for (int i=0;i<96;i++)
                dividedBlocks[i/48][i%48] = block[i];	// divide this block to 2 parts (left,right)
            for(int i =0;i<48;i++)
                temp[i] = dividedBlocks[mode][i];		// this part directly going to next opposite part.
            Scramble scramble = new Scramble();
            dividedBlocks[mode] = scramble.scramble(dividedBlocks[mode],generateSubkey(key,j+mode+1,mode));
            											// Scramble function with this part
            for (int i=0;i<48;i++)
            {
                block[i+padding] = temp[i];
                dividedBlocks[(mode+1)%2][i] = dividedBlocks[0][i] ^ dividedBlocks[1][i]; // XORing
                block[(48+i+padding)%96] = dividedBlocks[(mode+1)%2][i];	// writing new round block
            }
        }
    }

    public static Boolean[] generateSubkey(Boolean[] key, int round, int mode)
    {
        Boolean[] subkey = new Boolean[48];
        if (mode == 1)						// decryption --> right circular shift for reverse subkeys
        {									// ( Already shifted left 10 times )
        	boolean temp = key[0];
            for(int i =0;i<95;i++){
                key[i] = key[i+1];
            }
            key[95] = temp;
        }
        if (mode == 0)						// encryption --> left circular shift
        {
        	boolean temp = key[95];
            for(int i =95;i>0;i--){
                key[i] = key[i-1];
            }
            key[0] = temp;
        }
        if (round %2 == 0)					// if round is even, take even bits
            for (int i=0;i<48;i++)
                subkey[i] = key[i*2];
        else								// if round is odd, take odd bits
            for (int i=0;i<48;i++)
                subkey[i] = key[i*2+1];
        return subkey;
    }
}
