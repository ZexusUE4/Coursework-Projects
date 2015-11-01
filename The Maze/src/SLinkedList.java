
public class SLinkedList implements MyLinkedList {
	Node head;
	int size = 0;
	public SLinkedList(){
		head = new Node (null , null);
		size = 0;
	}
	public Node getNodeAt(int index){
		if(IndexCheck(index)){
		Node v = head;
		int counter=0;
		while(counter < index){
			v = v.getNext();
			counter ++;
		}
		return v;
		}
		return null;
	}
	public boolean IndexCheck(int index) throws IllegalAccessError {
        if (index < 0 || index > size){
            throw new IllegalAccessError("Invalid Index");
        }
        else
        	return true;
	}
	@Override
	public void Print(){
		Node x = head;
		for(int i=0;i<size;i++){
			System.out.print(x.getElement() + " ");
			x = x.getNext();
		}
		System.out.println();
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
	public void add(Object element) {
		// TODO Auto-generated method stub
		add(size,element);
	}

	@Override
	public Object get(int index) {
		// TODO Auto-generated method stub
		if(IndexCheck(index)){
			int counter = 0;
			Node k = head;
			while(counter < index){
				k = k.getNext();
				counter++;
			}
			return k.getElement();
		}
		return null;
	}

	@Override
	public void set(int index, Object element) {
		// TODO Auto-generated method stub
		Node n = getNodeAt(index);
		n.setElement(element);
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		head.setNext(null);
		size = 0;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		if(size>0)
			return true;
		else
			return false;
	}

	@Override
	public void remove(int index) {
		// TODO Auto-generated method stub
		if(IndexCheck(index)){
			Node prev = getNodeAt(index-1);
			Node r = prev.getNext().getNext();
			prev.setNext(r);
			size--;
		}
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return size;
	}

	@Override
	public MyLinkedList sublist(int from, int to) {
		// TODO Auto-generated method stub
		if(IndexCheck(from) && IndexCheck(to)){
			Node c = getNodeAt(from);
			SLinkedList Lis = new SLinkedList();
			for(int j = from ; j < to ; j++){
				Lis.add(c.getElement());
				c = c.getNext();
			}
			return Lis;
		}
		return null;
	}

	@Override
	public boolean contains(Object o) {
		// TODO Auto-generated method stub
		Node x = head;
		while(x.getNext()!=null){
			if(x.getElement().equals(o))
				return true;
			x = x.getNext();
		}
		return false;
	}

}
