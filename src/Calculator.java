import java.text.*;
import java.util.*;
import java.util.regex.*;

/**
 * This is a calculator that can take in a user inputed string and solve it It
 * is able to calculate +, -, /, *, large numbers and decimal points
 * 
 * @author jacob
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

		do {
			System.out.println("Please enter an equation:");
			input = scan.nextLine();
			if (input.equals("no"))
				again = false;
			else
				System.out.println(df.format(calculate(input)));
		} while (again);

		scan.close();
	}

	/**
	 * takes a string eqaution and solves it rounding to two decimal points
	 * 
	 * @param equation
	 * @return
	 */
	public static double calculate(String equation) {
		ArrayList<String> sepEquation = SplitUp(equation);
		System.out.println(sepEquation);
		ArrayList<String> postfixEquation = InfixToPostfix(sepEquation);
		System.out.println(postfixEquation);
		Stack<Double> calc = new Stack<Double>();
		Pattern p = Pattern.compile("[-\\d.]+");

		for (int i = 0; i < postfixEquation.size(); i++) {
			String s = postfixEquation.get(i);
			Matcher m = p.matcher(s);

			// if element is a number push it onto stack
			if (m.matches())
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
		Pattern p = Pattern.compile("[\\d.]+");
		ArrayList<String> postfix = new ArrayList<String>();
		Stack<String> converter = new Stack<String>();
		Matcher a;
		Matcher b;
		Matcher c;
		for (int i = 0; i < infix.size(); i++) {
			String s = infix.get(i);
			a = p.matcher(s);

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
	public static ArrayList<String> SplitUp(String e) {
		ArrayList<String> sepE = new ArrayList<String>();
		String number = "";
		for (int i = 0; i < e.length(); i++) {
			if (e.charAt(i) == '+' || e.charAt(i) == '-' || e.charAt(i) == '*' || e.charAt(i) == '/'
					|| e.charAt(i) == '(' || e.charAt(i) == ')') {
				if (number.length() != 0) {
					sepE.add((number));
					number = "";
				}
				sepE.add(e.substring(i, i + 1));
			} else if (!(e.charAt(i) == ' '))
				number += e.charAt(i);
		}
		sepE.add((number));
		return sepE;
	}

}
