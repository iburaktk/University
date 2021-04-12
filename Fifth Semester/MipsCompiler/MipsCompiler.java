import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

public class MipsCompiler 
{
	public static void main(String[] args) throws FileNotFoundException 
	{
		File inputFile = new File(args[0]);
		Scanner input = new Scanner(inputFile);
		ArrayList<Instruction> instructions = new ArrayList<Instruction>();
		String output = "";	
		String dataHazards = "";
		
		while (true)			// reading input file
		{
			input.next();
			String instruction = input.next();
			if (instruction.equals("end"))		//when see "end" stop reading
				break;
			String arguments = input.nextLine().substring(1);
			Instruction newInstruction = new Instruction(instruction, arguments);	//creating new instruction
			instructions.add(newInstruction);										//adding to list
			System.out.println(instruction+" "+arguments);
		}
		
		System.out.println("\n***** PART 1 *****");
		int i=-1;
		Queue<String> dependencyQueue = new LinkedList<String>();		// queue for dependent registers
		while (instructions.size() > ++i)
		{
			int j = instructions.get(i).checkDependency(dependencyQueue);	//checking dependency
			while (j>0)												// if there is dependency, j will greater than zero
			{
				output = output.concat("NOP\n");					// adding NOP
				dataHazards = dataHazards.concat((i-j+1)+"-"+(i+1)+" on "+instructions.get(i).getHazard()+"\n");	//adding to data hazard output
				j = instructions.get(i).checkDependency(dependencyQueue);
			}
			output = output.concat(instructions.get(i).print()+"\n");
			if (instructions.size() != i+1)
				instructions.get(i).setDependency(dependencyQueue,false);		// adding register to queue for next instructions
		}
		System.out.println(dataHazards+"\n"+output);
		
		System.out.println("***** PART 2 *****");					// similar algorithm and functions
		output="";
		dataHazards = "";
		i=-1;
		dependencyQueue = new LinkedList<String>();
		while (instructions.size() > ++i)
		{
			if (instructions.get(i).checkDependency(dependencyQueue) != 0)
			{
				output = output.concat("NOP\n");
				dataHazards = dataHazards.concat(i+"-"+(i+1)+" on "+instructions.get(i).getHazard()+"\n");
			}
			output = output.concat(instructions.get(i).print()+"\n");
			if (instructions.size() != i+1)
				instructions.get(i).setDependency(dependencyQueue,true);		// forwarding = true
		}
		System.out.println(dataHazards+"\n"+output);
		
		System.out.println("***** BONUS *****");
		i=0;
		while (instructions.size() != 0)						// repeat until list is empty
		{
			if (isDependent(instructions, i))					// found first dependent instruction
			{
				Instruction thisInstruction = instructions.get(i);
				for (int j=0;j<i-1;j++)						// finding dependent instructions prior to this instruction
				{
					Instruction otherInstruction = instructions.get(j);
					for (String dependentRegister : thisInstruction.sources)
					{
						if (otherInstruction.destination.equals(dependentRegister))
						{
							System.out.println(otherInstruction.print());
							instructions.remove(otherInstruction);		// printing that instruction and removing from list
							j--;
							i--;
							break;
						}
					}
				}
				Instruction prevInstruction = instructions.get(i-1);
				if (prevInstruction.instruction.equals("lw"))				// if previous instruciton is "lw",
				{
					Instruction rescheduledInstruction = find(instructions,i-1);	// find independent instruction
					System.out.println(prevInstruction.print());			// print previous lw instruction
					if (rescheduledInstruction != null)							// if found
					{
						System.out.println(rescheduledInstruction.print());		//then print it and remove from list	
						instructions.remove(rescheduledInstruction);
					}
					else 
						System.out.println("NOP");							// if couldnt find, insert NOP
					instructions.remove(prevInstruction);
				}
				else 
				{
					System.out.println(prevInstruction.print());		// if previous instruction is not "lw"
					instructions.remove(prevInstruction);				// then there is no dependency, print and remove
				}
				i=0;
				System.out.println(thisInstruction.print());				// print and remove last instruction
				instructions.remove(thisInstruction);
			}
			else 
				i++;
			if (instructions.size() == i)						// if rest of instructions are independent, then print it
			{
				for (Instruction instruction : instructions)
					System.out.println(instruction.print());
				break;
			}
		}
		input.close();
	}
	
	public static Instruction find(ArrayList<Instruction> instructions, int num)	// finds independent instruction to prevent data hazard
	{
		if (num != 0)
			return instructions.get(0);
		else {
			Set<String> sourceSet = new HashSet<String>();
			Set<String> destinationSet = new HashSet<String>();
			instructions.get(num+1).addToSet(sourceSet, destinationSet);
			for (int i=num+2;i<instructions.size();i++)
				if (! instructions.get(i).checkFromSet(sourceSet,destinationSet))	// checks dependency
					return instructions.get(i);
			return null;
		}
	}
	
	public static boolean isDependent(ArrayList<Instruction> instructions, int num)
	{
		Instruction thisInstruction = instructions.get(num);
		String[] dependentRegisters = thisInstruction.sources;
		for (int i=0;i<num;i++)		
		{									// checking that this instruction dependent to others
			Instruction otherInstruction = instructions.get(i);
			for (String source : dependentRegisters)
				if (otherInstruction.destination.equals(source))
					return true;
		}
		return false;
	}
}

class Instruction
{
	public String instructionType ;			// lw - sw - I type - R type
	public String instruction;
	public String arguments;
	public String destination;
	public String[] sources;
	String hazard = "";
	
	public Instruction(String instruction, String arguments)
	{
		this.instruction = instruction;
		this.arguments = arguments;
		sources = new String[3];
		switch (instruction) {			// some instructions have different syntax. some are same.
		case "lw":
			destination = arguments.split(",")[0];
			sources[0] = arguments.replaceAll("[(]", ",").replaceAll("[) ]", "").split(",")[1];
			sources[1] = arguments.replaceAll("[(]", ",").replaceAll("[)]", "").split(",")[2];
			instructionType = "lw";
			break;
		case "sw":
			instructionType = "sw ";
			destination = "nope";
			sources[2] = arguments.split(",")[0];
			sources[0] = arguments.replaceAll("[(]", ",").replaceAll("[) ]", "").split(",")[1];
			sources[1] = arguments.replaceAll("[(]", ",").replaceAll("[)]", "").split(",")[2];
			break;
		case "addi":					// you can add I type instructions here
			instructionType = "I";
			destination = arguments.split(",")[0];
			sources[0] = arguments.replaceAll(" ", "").split(",")[1];
			sources[1] = arguments.replaceAll(" ", "").split(",")[2];
			break;
		case "add":						// you can add R type instructions here
		case "sub":
		case "and":
		case "or":
			instructionType = "R";
			destination = arguments.split(",")[0];
			sources[0] = arguments.replaceAll(" ", "").split(",")[1];
			sources[1] = arguments.replaceAll(" ", "").split(",")[2];
			break;
		}
	}
	
	public int checkDependency(Queue<String> queue)
	{
		if (!queue.isEmpty())
		{
			hazard = queue.poll();			// pop first register
			String dep2 = queue.peek();		// look second register
			int i = 2;
			if (hazard.contains("D"))		// remove "D" from registers
			{
				hazard = hazard.replaceAll("[D]", "");
				if (dep2 != null && dep2.contains("D"))		// is it 1 before instruction causes to hazard?
					i=1;
				if (dep2 == null)
					i=1;
			}
			else if (dep2 == null)
				i=1;
			if (sources[0].equals(hazard))			// instruction which causes hazard is 2 before
				return i;
			else if (sources[1].equals(hazard))
				return i;
			else if (sources[2] != null && sources[2].equals(hazard))
				return i;
			else if (sources[0].equals(dep2))		// instruction which causes hazard is 1 before
			{
				hazard = dep2;
				return 1;
			}
			else if (sources[1].equals(dep2))
			{
				hazard = dep2;
				return 1;
			}
			else if (sources[2] != null && sources[2].equals(dep2))
			{
				hazard = dep2;
				return 1;
			}
			else 
				return 0;
		}
		else 
			return 0;
	}
	
	public void setDependency(Queue<String> queue, boolean forwarding)
	{
		if (!forwarding)
		{
			if (instruction.equals("sw"))		//sw doesnt cause dependency
				;
			else if (queue.isEmpty()) 			// if queue is empty
			{
				queue.add(destination+"D");
				queue.add(destination+"D");
			}
			else 								// if queue is not empty
				queue.add(destination);
		}
		else if (instructionType.equals("lw"))	// forwarding enabled. lw causes dependency
		{
			queue.add(destination);
		}
	}
	
	public void addToSet(Set<String> sourceSet, Set<String> destinationSet)
	{
		for (String source : sources)
			sourceSet.add(source);
		destinationSet.add(destination);
	}
	
	public boolean checkFromSet(Set<String> sourceSet, Set<String> destinationSet)
	{
		for (String source : sources)
			if (source != null && destinationSet.contains(source))
				return true;
		if (destinationSet.contains(destination) || sourceSet.contains(destination))
			return true;
		return  false;
	}
	
	public String print()
	{
		return instruction+" "+arguments;
	}
	
	public String getHazard()
	{
		return hazard;
	}
}
//Ibrahim Burak Tanrikulu