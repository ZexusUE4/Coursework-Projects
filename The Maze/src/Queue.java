
public class Queue {

	Node head = null;
	Node tail = null;
	int size =0;

	public void push(Object item) {
	
		Node temp = new Node(item,null);
		if(head==null)
		{
			head=temp;
		}
		else
		{
			tail.setNext(temp);
		}
		tail=temp ;
		size++;
	}
	
	public Object pop() {
			Object temp = head.getElement();
			head=head.getNext();
			size--;
			return temp;
	}
	public boolean isEmpty() {
		return size==0;
	}
	
	public int size() {
		return size;
	}

	public void print() {
		Node temp = head ;
		while (temp!=null)
		{
			System.out.print(temp.getElement() + " ");
			temp = temp.getNext();
		}
		System.out.println();
	}
		
}
