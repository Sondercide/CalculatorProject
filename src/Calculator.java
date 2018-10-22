import java.text.*;
import java.util.*;
import java.util.regex.*;

/**
 * This is a calculator that can take in a user inputed string and solve it It
 * is able to calculate +, -, /, *, large numbers, decimal points and negatives
 * Additionally it will inform the user if they have 
 * 1.Entered an operator the calculator does not recognize
 * 2.Entered an equation that will end up with a division by zero
 * 3.Entered an equation that does'nt make sense												
 * 
 * @author Jacob Brown
 *
 */
public class Calculator {
	static HashMap<String, Integer> operators = new HashMap<String, Integer>();

	public static void main(String[] args) {
		DecimalFormat df = new DecimalFormat("####0.00");
		Scanner scan = new Scanner(System.in);
		boolean again = true;
		String input;
		
		operators.put("-", 1);
		operators.put("+", 1);
		operators.put("*", 2);
		operators.put("/", 2);
		
		System.out.println("Please enter an equation. Type no when you wish to stop.");
		do {
			input = scan.nextLine();
			if (input.equals("no"))
				again = false;
			else {
				try {
				System.out.println(df.format(calculate(input)));
				}catch(RuntimeException Failed){
					System.err.println("RuntimeException: " + Failed.getMessage());
				}
			}
			
		} while (again);

		scan.close();
	}

	/**
	 * takes a string eqaution and solves it rounding to two decimal points
	 * 
	 * @param equation
	 * @return
	 */
	public static double calculate(String equation) throws RuntimeException{
		ArrayList<String> sepEquation = SplitUp(equation);
		System.out.println(sepEquation);
		ArrayList<String> postfixEquation = InfixToPostfix(sepEquation);
		System.out.println(postfixEquation);
		Stack<Double> calc = new Stack<Double>();
		Pattern d = Pattern.compile("-?[\\d.]+");
	
		
		for (int i = 0; i < postfixEquation.size(); i++) {
			String s = postfixEquation.get(i);
			Matcher n = d.matcher(s);
			// if element is a number push it onto stack
			if (n.matches())
				calc.push(Double.parseDouble(s));

			// else take the two previous numbers and use the calculation on them
			else {
				double val1 = calc.pop();
				double val2 = calc.pop();

				switch (s.charAt(0)) {
				case '+':
					calc.push(val2 + val1);
					break;
				case '-':
					calc.push(val2 - val1);
					break;
				case '/':
					if(val1 == 0) 
						throw new RuntimeException("You can not divide by zero. Sorry try again.");
					else
						calc.push(val2 / val1);
					break;
				case '*':
					calc.push(val2 * val1);
					break;
				}
				
			}
		}

		return calc.pop();
	}

	/**
	 * Takes in a ArrayList with an infix equation and converts it into a ArrayList
	 * with the same equation in post fix notation
	 * 
	 * @param infix :Any ArrayList containing and infix equation
	 * @return
	 */
	public static ArrayList<String> InfixToPostfix(ArrayList<String> infix) {
		Pattern p = Pattern.compile("-?[\\d.]+");
		ArrayList<String> postfix = new ArrayList<String>();
		Stack<String> converter = new Stack<String>();
		for (int i = 0; i < infix.size(); i++) {
			String s = infix.get(i);
			Matcher a = p.matcher(s);
			// checks if the element is a number and adds to postfix ArrayList
			if (a.matches())
				postfix.add(s);

			// If the element is an '(' push the the stack
			else if (infix.get(i).equals("("))
				converter.push(s);

			// If the element is an ')', pop and output from the stack until an '(' is
			// encountered.
			else if (infix.get(i).equals(")")) {
				while (!converter.empty() && !converter.peek().equals("("))
					postfix.add(converter.pop());
				converter.pop();
			}

			// if an operator is encountered pops out until the next operator is less than
			// or equal to the current being looked at
			else {
				while (!converter.empty() && operators.getOrDefault(infix.get(i), -1) <= operators.getOrDefault(converter.peek(), -1))
					postfix.add(converter.pop());

				converter.push(s);
			}
		}
		// adds all remaining operators from the stack into the postfix ArrayList
		while (!converter.empty())
			postfix.add(converter.pop());

		return postfix;
	}

	/**
	 * Converts a string into an ArrayList that contains the operators and operands
	 * by splitting a string at every operator and adding both the numbers and
	 * operators to an ArrayList
	 * 
	 * @param e :Any equation
	 * @return
	 */
	public static ArrayList<String> SplitUp(String e) throws RuntimeException {
		ArrayList<String> sepE = new ArrayList<String>();
		String number = "";
		Pattern p = Pattern.compile("[+-/*]");
		for (int i = 0; i < e.length(); i++) {
			
			//skip spaces
			if(e.charAt(i) == ' ')
				continue;
			
			//check for negatives
			else if(e.charAt(i) == '-' && number.equals("")) 
				number += e.charAt(i);
			
			//check for operators, if yes add number to the array then the operator
			else if (e.charAt(i) == '+' || e.charAt(i) == '-' || e.charAt(i) == '*' || e.charAt(i) == '/'|| e.charAt(i) == '(' || e.charAt(i) == ')') {
				if(!number.isEmpty()) {
					sepE.add((number));
					number = "";
				}
				
				//ensures there are not two operators in a row excluding the case of ( and )
				Matcher m = p.matcher(sepE.get(sepE.size() - 1));
				if(m.matches() && (e.charAt(i) != ')' && e.charAt(i) != '(' )) 
					throw new RuntimeException("You have two or more operators in a row. Please try again.");
				sepE.add(e.substring(i, i + 1));
			} 
			
			//if the character is a digit or decimal add the character the the current number
			else if(Character.isDigit(e.charAt(i)) || e.charAt(i) == '.')
					number += e.charAt(i);
			
			//if none of the above the input contains something this calculator is unable to compute
			else 
				throw new RuntimeException("One or more character in the input is not compatible with this calculator. Please try again.");
		}
		
		//add the remnants of number if it is not empty
		if(!number.equals("")) 
			sepE.add((number));
		return sepE;
	}

}
