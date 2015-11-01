
public class Node {
	private Object element;
	private Node next;
	public Node(Object e,Node n){
		element = e;
		next=n;
	}
	public Object getElement(){return element;}
	public Node getNext(){return next;}
	public void setElement(Object newElem){element = newElem;}
	public void setNext(Node newNext){next = newNext;}
}
