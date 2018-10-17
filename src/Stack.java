import java.util.*;
public class Stack<E> {
	
	private Node<E> head;
	
	public Stack() {
		head = null;
	}
	
	public void push(E item) {
		head = new Node<E>(item, head);
	}
	
	public E peek() {
		return head.data;
	}
	
	public E pop() throws EmptyStackException {
		if(empty()) throw new EmptyStackException();
		E temp = head.data;
		head = head.next;
		return temp;
	}
	
	public boolean empty() {
		return (head == null);
	}
	
}
