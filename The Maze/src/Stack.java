
public class Stack implements MyLinkedList {
	Node head;
	int size = 0;
	public Stack(){
		head = new Node (null , null);
		size = 0;
	}
	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		if(size>0)
			return false;
		else
			return true;
	}
	public boolean IndexCheck(int index) throws IllegalAccessError {
        if (index < 0 || index > size){
            throw new IllegalAccessError("Invalid Index");
        }
        else
        	return true;
	}
	public void add(int index, Object element) {
		if(IndexCheck(index)){
			int counter = 0;
			if(index == 0)
				head.setElement(element);
			else{
			Node g = head;
			while(counter < index-1){
				counter++;
				g = g.getNext();
			}
			Node n = new Node(element,g.getNext());
			g.setNext(n);
			}
			size++;
		}
	}
	@Override
	public int size() {
		// TODO Auto-generated method stub
		return size;
	}
	public Object pop() throws IllegalAccessError {
		// TODO Auto-generated method stub
		if(size == 0)
			throw new IllegalAccessError("Empty Stack!");
		else{
			Object s = head.getElement();
			head = head.getNext();
			size--;
			return s;
		}
	}
	public Object peek() {
		// TODO Auto-generated method stub
		if(size == 0)
			throw new IllegalAccessError("Empty Stack Or No space between Tokens!");
		else{
			return head.getElement();
		}
	}
	public void push(Object element) {
		// TODO Auto-generated method stub
		if(size==0)
			head = new Node (element,null);
		else{
			Node v = new Node(element,head);
			head = v;
		}
		size++;
	}
	public void print(){
		Node x = head;
		for(int i=0;i<size;i++){
			System.out.print(x.getElement() + " ");
			x = x.getNext();
		}
		System.out.println();
	}
	@Override
	public void add(Object element) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Object get(int index) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void set(int index, Object element) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void remove(int index) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public MyLinkedList sublist(int fromIndex, int toIndex) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean contains(Object o) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void Print() {
		// TODO Auto-generated method stub
		
	}
}
