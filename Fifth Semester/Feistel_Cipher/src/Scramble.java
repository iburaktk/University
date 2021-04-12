public class Scramble {
	
    String[][] subBox;
    
    public Scramble()
    {
    	sBox = new String[][] 
    			{
    	        {"0010", "1100", "0100", "0001", "0111", "1010", "1011", "0110", "1000", "0101", "0011", "1111", "1101", "0000", "1110", "1001"},
    	        {"1110", "1011", "0010", "1100", "0100", "0111", "1101", "0001", "0101", "0000", "1111", "1010", "0011", "1001", "1000", "0110"},
    	        {"0100", "0010", "0001", "1011", "1010", "1101", "0111", "1000", "1111", "1001", "1100", "0101", "0110", "0011", "0000", "1110"},
    	        {"1011", "1000", "1100", "0111", "0001", "1110", "0010", "1101", "0110", "1111", "0000", "1001", "1010", "0100", "0101", "0011"}
    			};
    }
    
    public Boolean[] scramble(Boolean[] right,Boolean[] key) // scramble function
    {
        Boolean[][] temp = new Boolean[12][6];
        int i =0;
        for(boolean keybit:key)								// writing first 8 part to temp array 
        {
            temp[i/6][i%6] = keybit^right[i];
            i++;
        }
        for(int j = 0; j<8 ; j=j+2)							// writing last 4 part to temp array
        {
            for(Boolean bit:temp[j]){
                temp[i/6][i%6] = bit^temp[j+1][i%6];
                i++;
            }
        }
        Boolean[] temp2 = substitution(temp);				// getting whole array's substitution box result
        return temp2;
    }
    
    public Boolean[] substitution(Boolean[][]temp)			// returns substitution box's outcome
    {
        Boolean[] outer = new Boolean[2];
        Boolean[] inner = new Boolean[4];
        Boolean[] result = new Boolean[48];
        int length =0;
        for(Boolean[] tempArray:temp){
            int j =0;
            outer[0] = tempArray[0];						// outer bits
            outer[1] = tempArray[5];
            for(int i =1;i<tempArray.length-1;i++)			// inner bits
            {
                inner[j] = tempArray[i];
                j++;
            }
            Boolean[] resultTemp = bitToBool(subBox[bitToNum(boolToBit(outer))][bitToNum(boolToBit(inner))]);
            // changed outer and inner bits to integer value and found correct outcome
            for(Boolean bit:resultTemp)
            {
                result[length] = bit;						// writing result
                length++;
            }
        }
        for(int a=0;a<result.length;a=a+2)					// permutation function
        {
            Boolean tempBool = result[a];
            result[a] = result[a+1];						// changing odd bits and even bits
            result[a+1] = tempBool;
        }
        return result;
    }
    
    public String boolToBit(Boolean[] booleanBits)			// Boolean array bits to String
    {
        StringBuilder result =new StringBuilder();
        for(Boolean bit:booleanBits)
        {
            if(bit){
                result.append(1);
            }
            else{
                result.append(0);
            }
        }
        return result.toString();
    }
    
    public Boolean[] bitToBool(String bits)					// String bits to Boolean array
    {
        Boolean[] boolBits = new Boolean[4];
        for(int i =0;i<boolBits.length;i++)
        {
            boolBits[i] = bits.charAt(i) == '1';
        }
        return boolBits;
    }
    
    public int bitToNum(String bits)						// String bits to integer value
    {
        String temp="";
        if(bits.length() ==2)
        {
            temp = "00"+bits;
        }
        else
        {
            temp = bits;
        }
        int firstBit =Integer.parseInt(Character.toString(temp.charAt(0)))*(int)Math.pow(2.0,3.0);
        int secondBit =Integer.parseInt(Character.toString(temp.charAt(1)))*(int)Math.pow(2.0,2.0);
        int thirdBit =Integer.parseInt(Character.toString(temp.charAt(2)))*(int)Math.pow(2.0,1.0);
        int fourthBit =Integer.parseInt(Character.toString(temp.charAt(3)));
        return firstBit+secondBit+thirdBit+fourthBit;
    }
    
    public String numToBit(int number)						// Integer value to bits
    {
        StringBuilder result = new StringBuilder();
        while(number >0)
        {
            result.append(number%2);
            number/=2;
        }
        result.append(number%2);
        return result.toString();
    }
}
