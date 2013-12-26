
public class SplayTree {
	Node root;

	public SplayTree() {
		root = null;
	}

	private static class Node {
		int element;
		Node left, right, parent;

		Node(int element, Node left, Node right, Node parent) {
			this.element = element;
			this.left = left;
			this.right = right;
			this.parent = parent;
		}
	}

	public void add(int element) {
		add(root, element);
	}

	public void remove(int element) {
		remove(root, element,false);
	}

	public boolean find(int element) {
		return find(root, element);
	}

	public int leafCount() {
		return leafCount(root);
	}

	public int leafSum() {
		return leafSum(root);
	}

	@Override
	public String toString() {
		StringBuffer stringBuffer = new StringBuffer();
		printTree(root, stringBuffer);
		return "SplayTree : " + stringBuffer;
	}

	private void printTree(Node currentNode, StringBuffer stringBuffer) {
		if (currentNode == null) {
			return;
		}
		stringBuffer.append(currentNode.element + ", ");
		printTree(currentNode.left, stringBuffer);
		printTree(currentNode.right, stringBuffer);
	}

	private int leafSum(Node currentNode) {
		if (currentNode == null) {
			return 0;
		} else if (currentNode.left == null && currentNode.right == null) {
			return currentNode.element;
		}
		return leafSum(currentNode.left) + leafSum(currentNode.right);
	}

	private int leafCount(Node currentNode) {
		if (currentNode == null) {
			return 0;
		} else if (currentNode.left == null && currentNode.right == null) {
			return 1;
		}
		return leafCount(currentNode.left) + leafCount(currentNode.right);
	}

	private boolean find(Node currentNode, int element) {
		if (currentNode == null) {
			return false;
		}

		Node lastAccessedNode = null;
		while (currentNode != null) {
			if (element < currentNode.element) {
				lastAccessedNode = currentNode;
				currentNode = currentNode.left;
			} else if (element > currentNode.element) {
				lastAccessedNode = currentNode;
				currentNode = currentNode.right;
			} else {
				adjustTree(currentNode);// make current node as root.
				return true;
			}
		}
		adjustTree(lastAccessedNode);// make last accessed node as root.
		return false;
	}

	private void add(Node currentNode, int element) {
		if (currentNode == null) {
			root = new Node(element, null, null, null);
		}

		while (currentNode != null) {
			if (element < currentNode.element) {
				if (currentNode.left == null) {
					currentNode.left = new Node(element, null, null,currentNode);
					adjustTree(currentNode.left);
					return;
				} else {
					currentNode = currentNode.left;
				}
			} else if (element > currentNode.element) {
				if (currentNode.right == null) {
					currentNode.right = new Node(element, null, null,currentNode);
					adjustTree(currentNode.right);
					return;
				} else {
					currentNode = currentNode.right;
				}
			} else {
				return; // duplicate element
			}
		}
	}

	private void remove(Node currentNode, int element, boolean isRecursiveCall) {
		while (currentNode != null) {
			if (element < currentNode.element) {
				currentNode = currentNode.left;
			} else if (element > currentNode.element) {
				currentNode = currentNode.right;
			} else {
				if (currentNode.left != null && currentNode.right != null) {
					currentNode.element = findMin(currentNode.right).element;
					remove(currentNode.right, currentNode.element, true);
					adjustTree(currentNode.parent);
				} else {
					Node child = (currentNode.left != null) ? currentNode.left : currentNode.right;
					if (currentNode.parent == null) { // special case to delete root
						root = child;
					} else if (currentNode.parent.left == currentNode) {
						currentNode.parent.left = child;
					} else {
						currentNode.parent.right = child;
					}
					child.parent = currentNode.parent;
					
					if(!isRecursiveCall){
						adjustTree(currentNode.parent);
					}
				}
				return;
			}
		}
	}

	private Node findMin(Node node) {
		if (node == null)
			return null;
		while (node.left != null) {
			node = node.left;
		}
		return node;
	}

	private void adjustTree(Node current){
		if(current == null || current.parent == null){
			return;
		}
		
		Node grandParent = current.parent.parent;
		Node newCurrent;
		if(current.parent.left == current){ //zig
			if(grandParent == null){
				newCurrent = zigRotation(current.parent); //zig
			} else if(grandParent.left == current.parent){ // zigzig
				newCurrent = zigZigRotation(grandParent);
			} else {
				newCurrent = zagZigRotetion(grandParent); //zagZig
			}
		} else { //zag
			if(grandParent == null){
				newCurrent = zagRotation(current.parent); //zag
			} else if(grandParent.left == current.parent){ // zigzag
				newCurrent = zigZagRotetion(grandParent);
			} else {
				newCurrent = zagZagRotetion(grandParent); //zagZag
			}
		}
		adjustTree(newCurrent);
	}
	
	private Node zigZigRotation(Node k3) {
		Node k2 = zigRotation(k3);
		return zigRotation(k2);
	}

	private Node zagZagRotetion(Node k3) {
		Node k2 = zagRotation(k3);
		return zagRotation(k2);
	}

	private Node zigZagRotetion(Node k3) {
		zagRotation(k3.left);
		return zigRotation(k3);
	}

	private Node zagZigRotetion(Node k3) {
		zigRotation(k3.right);
		return zagRotation(k3);
	}
	
	private Node zigRotation(Node k2) {
		Node k1 = k2.left;
		k2.left = k1.right;
		k1.right = k2;
		updateParent(k1, k2);
		return k1;
	}

	private Node zagRotation(Node k2) {
		Node k1 = k2.right;
		k2.right = k1.left;
		k1.left = k2;
		updateParent(k1, k2);
		return k1;
	}

	private void updateParent(Node k1, Node k2) {
		k1.parent = k2.parent;
		k2.parent = k1;

		if (k1.parent == null) {
			root = k1;
		} else {
			if (k1.parent.left == k2) {
				k1.parent.left = k1;
			} else {
				k1.parent.right = k1;
			}
		}
	}

	public static void main(String[] args) {
		SplayTree tree = new SplayTree();
		tree.add(10); System.out.println("Performed add(10) "+ tree);
		tree.add(5);  System.out.println("Performed add(5) " + tree);
		tree.add(15); System.out.println("Performed add(15) "+ tree);
		tree.add(11); System.out.println("Performed add(11) "+ tree);
		tree.add(19); System.out.println("Performed add(19) "+ tree);
		tree.add(12); System.out.println("Performed add(12) "+ tree);

		tree.remove(10); System.out.println("Performed remove(10) "+ tree);
		System.out.println("Performed leafCount result : " + tree.leafCount());
		System.out.println("Performed leafSum result : " +tree.leafSum());
		
		System.out.println("Performed find(5) result : " + tree.find(5));System.out.println(tree);
		System.out.println("Performed find(20) result : " + tree.find(20));System.out.println(tree);
		
		System.out.println("Performed leafCount result : " + tree.leafCount());
		System.out.println("Performed leafSum result : " +tree.leafSum());
	}
}
