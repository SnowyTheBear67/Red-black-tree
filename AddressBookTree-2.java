/* Lance Boza
 * Dr. Andrew Steinberg
 * COP3503 Summer 2022
 * Programming Assignment 1
 */


public class AddressBookTree <T extends Comparable<T>, U> {
	
	private Node<T, U> root;
	
	private Node<T, U>  nil;
	
	public AddressBookTree() {
		//when the class is called, the root is a nil node
		this.root = new Node<>();
		this.nil = root;
		
	}
	
	public void insert(T name, U office) {
		
		Node<T, U > z = new Node<>(name, office);
		
		Node<T, U> y = nil;
		Node<T, U> x = root;
		
		
		while(!(x.equals(nil))) {
			y = x;
			//find where the node should be inserted
			if(z.getName().compareTo(x.getName()) < 0) 
				x = x.getLeft();
			else 
				x = x.getRight();	
		}
		
		//set the new node's parent to y
		z.setParent(y);
		if(y.equals(nil))
			root = z;
		else if(z.getName().compareTo(y.getName()) < 0)
			y.setLeft(z);
		else
			y.setRight(z);
		
		z.setLeft(nil);
		z.setRight(nil);
		z.setColor(1);
		
		//call fixup for any red-black violations
		insertFix(z);
	}
	
	public void insertFix(Node<T, U> node) {
		Node<T, U> z = node; 
		
		// while the parent is not a nil node and the parent is red
		while(!(z.getParent().equals(nil)) && z.getParent().getColor() == 1) {
			
			//if z's parent is a left child
			if(z.getParent().equals(z.getParent().getParent().getLeft())){
				Node<T, U> y = z.getParent().getParent().getRight();
				//if uncle is red
				if(y.getColor() == 1) {
					z.getParent().setColor(0);
					y.setColor(0);
					z.getParent().getParent().setColor(1);
					z = z.getParent().getParent();
				}
				else {
					if(z.equals(z.getParent().getRight())) {
						z = z.getParent();
						rotateLeft(z);
					}
					z.getParent().setColor(0);
					z.getParent().getParent().setColor(1);
					rotateRight(z.getParent().getParent());
				}
					
			}
			else {
				
				//if z's parent is a right child
				if(z.getParent().equals(z.getParent().getParent().getRight())) {
					Node<T, U> y = z.getParent().getParent().getLeft();
					//if uncle is red
					if(y.getColor() == 1) {
						z.getParent().setColor(0);
						y.setColor(0);
						z.getParent().getParent().setColor(1);
						z = z.getParent().getParent();
					}
					//if uncle is black
					else {
						if(z.equals(z.getParent().getLeft())) {
							z = z.getParent();
							rotateRight(z);
						}
						z.getParent().setColor(0);
						z.getParent().getParent().setColor(1);
						rotateLeft(z.getParent().getParent());
					}
				}
			}
		}
		root.setColor(0);
	}
	
	public void deleteNode(T name) {
		//find the node to be deleted
		Node<T, U> z = search(root, name);
		Node<T, U> y = z;
		
		Node<T, U> x = z.getRight();
		
		int yColor = y.getColor();
		
		// if z's left child is a nil node 
		if(z.getLeft().equals(nil)) {
			x = z.getRight();
			rbTransplant(z, z.getRight());
		}
		//if z's right child is a nil node then transplant z and z's left child
		else if(z.getRight().equals(nil)){
			x = z.getLeft();
			rbTransplant(z, z.getLeft());
		}
		else {
			//find successor to replace
			y = treeMinimum(z.getRight());
			yColor = y.getColor();
			x = y.getRight();
			
			if(y.getParent().equals(z)) {
				x.setParent(y);
			}
			else {
				rbTransplant(y, y.getRight());
				y.setRight(z.getRight());
				y.getRight().setParent(y);
			}
			
			rbTransplant(z, y);
			y.setLeft(z.getLeft());
			y.getLeft().setParent(y);
			y.setColor(z.getColor());
		}
		if(yColor == 0) {
			deleteFix(x);
		}
		
	}
	
	public void deleteFix(Node<T, U> node) {
		//while the node is not the root and the color is black
		while(node != root && node.getColor() == 0) {
			//if the node is a left child
			if(node.equals(node.getParent().getLeft())) {
				Node<T, U> w = node.getParent().getRight();
				if(w.getColor() == 1) {
					w.setColor(0);
					node.getParent().setColor(1);
					rotateLeft(node.getParent());
					w = node.getParent().getRight();
				}
				//if the sibling's left child is black
				if(w.getLeft().getColor() == 0 && w.getRight().getColor() == 0) {
					w.setColor(1);
					node = node.getParent();
				}
				else {
					// if the sibling is black
					if(w.getRight().getColor() == 0) {
						w.getLeft().setColor(0);
						w.setColor(1);
						rotateRight(w);
						w = node.getParent().getRight();
					}
					w.setColor(node.getParent().getColor());
					node.getParent().setColor(0);
					w.getRight().setColor(0);
					rotateLeft(node.getParent());
					node = root;
				}
			}
			else {
				Node<T, U> w = node.getParent().getLeft();
				if(w.getColor() == 1) {
					w.setColor(0);
					node.getParent().setColor(1);
					rotateRight(node.getParent());
					w = node.getParent().getLeft();
				}
				if(w.getRight().getColor() == 0 && w.getLeft().getColor() == 0) {
					w.setColor(1);
					node = node.getParent();
				}
				else {
					if(w.getLeft().getColor() == 0) {
						w.getRight().setColor(0);
						w.setColor(1);
						rotateLeft(w);
						w = node.getParent().getLeft();
					}
					w.setColor(node.getParent().getColor());
					node.getParent().setColor(0);
					w.getLeft().setColor(0);
					rotateRight(node.getParent());
					node = root;
				}
			}
		}
		node.setColor(0);
	}
	
	public void rotateLeft(Node<T,U> node) {
		//sets y and turns y's left subtree into x's right subtree
		Node<T, U > y = node.getRight(); 
		node.setRight(y.getLeft());
		
		if(!(y.getLeft().equals(nil))) 
			y.getLeft().setParent(node);
		
		// link x's parent to y
		y.setParent(node.getParent());
		
		if(node.getParent().equals(nil)) 
			root = y;
		else if(node.equals(node.getParent().getLeft())) 
			node.getParent().setLeft(y);
		else 
			node.getParent().setRight(y);
		
		y.setLeft(node); //put x on y's left
		node.setParent(y);
	}
	
	public void rotateRight(Node<T, U> node) {
		//rotates the the nodes around the node parameter to blanace the tree
		Node<T, U > y = node.getLeft();
		node.setLeft(y.getRight());
		if(!(y.getRight().equals(nil))) 
			y.getRight().setParent(node);
		
		y.setParent(node.getParent());
		
		if(node.getParent().equals(nil)) 
			root = y;
		else if(node.equals(node.getParent().getRight())) 
			node.getParent().setRight(y);
		else 
			node.getParent().setLeft(y);
		
		y.setRight(node);
		node.setParent(y);
	}
	
	public void rbTransplant(Node<T, U> u, Node<T, U> v) {
		if(u.getParent().equals(nil)) 
			root = v;
		else if(u.equals(u.getParent().getLeft())) 
			u.getParent().setLeft(v);
		else 
			u.getParent().setRight(v);
		
		v.setParent(u.getParent());
	}
	
	public void display() {
		//prints out and calls an in-order traversal function 
		inOrder(root);
	}
	
	private void inOrder(Node<T, U> node) {
		//recursive function to print an in-order traversal of the tree
	    if (node.equals(nil)) {
	      return;
	    }

	    //gets left child, prints, then prints, gets right child
	    inOrder(node.getLeft());
	    System.out.println(node.getName() + " " + node.getOffice());
	    inOrder(node.getRight());
	}
	
	public int countBlack(Node<T, U> root) {
		//recursive function to traverse the tree and count black nodes
		int black = 0;
		
		if(root.equals(nil)) {
			return 0;
		}
		else {
			if(root.getColor() == 0) {
				black++;
			}
			black += countBlack(root.getLeft());
			black += countBlack(root.getRight());
		}
		
		return black;
		
	}
	
	public int countRed(Node<T, U> root) {
		//recursive function to traverse the tree and count red nodes
		int red = 0;
		if(root.equals(nil)) {
			return 0;
		}
		else {
			if(root.getColor() == 1) {
				red++;
			}
			red += countRed(root.getLeft());
			red += countRed(root.getRight());
		}
		return red;
				
	}

	public Node<T, U> getRoot() {
		return root;
	}
	
	public Node<T, U> search(Node<T, U> rooti, T key)  { 
        // Base Cases: root is null or key is present at root 
        if (rooti.equals(nil) || rooti.getName().equals(key)) {
            return rooti; 
        }
        // val is greater than root's key 
        if (rooti.getName().compareTo((T) key) > 0) 
            return search(rooti.getLeft(), key); 
        // val is less than root's key 
        return search(rooti.getRight(), key); 
    } 
	
	public Node<T, U> treeMinimum(Node<T, U> rooti){
		//traverses the subtree and gets the leftmost node
		Node<T, U> current = rooti;
		
		while(current.getLeft() != nil) {
			current = current.getLeft();
		}
		return current;
	}
	
	

}

class Node <T, U>{
	
	private T name;
	private U office;
	
	private Node<T, U> left;
	private Node<T, U> right;
	private Node<T, U> parent;
	
	//0 for black and 1 for red
	private int color;
	
	private int nil;
	
	//node constructor with the name and office passed as a parameter
	public Node(T name, U office) {
		this.name = name;
		this.office = office;
		this.left =  new Node<>();
		this.right = new Node<>();
		this.nil = 0;
		this.color = 1;
	}
	
	//node constructor of a nil node
	public Node() {
		this.name = null;
		this.office = null;
		this.left = null;
		this.right = null;
		this.nil = 1;
		this.color = 0;
	}
	
	
	//getters and setter methods
	public T getName() {
		return name;
	}

	public void setName(T name) {
		this.name = name;
	}

	public U getOffice() {
		return office;
	}

	public void setOffice(U office) {
		this.office = office;
	}

	public Node<T, U> getLeft() {
		return left;
	}

	public void setLeft(Node<T, U> left) {
		this.left = left;
	}

	public Node<T, U> getRight() {
		return right;
	}

	public void setRight(Node<T, U> right) {
		this.right = right;
	}

	public Node<T, U> getParent() {
		return parent;
	}

	public void setParent(Node<T, U> parent) {
		this.parent = parent;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getNil() {
		return nil;
	}

	public void setNil(int nil) {
		this.nil = nil;
	}
	
}