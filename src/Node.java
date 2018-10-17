
public class Node <E>{
	public E data;
	public Node<E> next;
	
	public Node() {
		data = null;
		next = null;
	}
	
	public Node(E item) {
		data = item;
		next = null;
	}
	
	public Node(E item, Node<E> next) {
		data = item;
		this.next =next;
	}
	
}
