import java.security.InvalidParameterException;

import org.omg.CORBA.Any;

/**
 * LinkedList class implements a doubly-linked list.
 */
public class MyLinkedList<AnyType> implements Iterable<AnyType>
{
    /**
     * Construct an empty LinkedList.
     */
    public MyLinkedList( )
    {
        doClear( );
    }
    
    private void clear( )
    {
        doClear( );
    }
    
    /**
     * Change the size of this collection to zero.
     */
    public void doClear( )
    {
        beginMarker = new Node<>( null, null, null );
        endMarker = new Node<>( null, beginMarker, null );
        beginMarker.next = endMarker;
        
        theSize = 0;
        modCount++;
    }
    
    /**
     * Returns the number of items in this collection.
     * @return the number of items in this collection.
     */
    public int size( )
    {
        return theSize;
    }
    
    public boolean isEmpty( )
    {
        return size( ) == 0;
    }
    
    /**
     * Adds an item to this collection, at the end.
     * @param x any object.
     * @return true.
     */
    public boolean add( AnyType x )
    {
        add( size( ), x );   
        return true;         
    }
    
    /**
     * Adds an item to this collection, at specified position.
     * Items at or after that position are slid one position higher.
     * @param x any object.
     * @param idx position to add at.
     * @throws IndexOutOfBoundsException if idx is not between 0 and size(), inclusive.
     */
    public void add( int idx, AnyType x )
    {
        addBefore( getNode( idx, 0, size( ) ), x );
    }
    
    /**
     * Adds an item to this collection, at specified position p.
     * Items at or after that position are slid one position higher.
     * @param p Node to add before.
     * @param x any object.
     * @throws IndexOutOfBoundsException if idx is not between 0 and size(), inclusive.
     */    
    private void addBefore( Node<AnyType> p, AnyType x )
    {
        Node<AnyType> newNode = new Node<>( x, p.prev, p );
        newNode.prev.next = newNode;
        p.prev = newNode;         
        theSize++;
        modCount++;
    }   
    
    
    /**
     * Returns the item at position idx.
     * @param idx the index to search in.
     * @throws IndexOutOfBoundsException if index is out of range.
     */
    public AnyType get( int idx )
    {
        return getNode( idx ).data;
    }
        
    /**
     * Changes the item at position idx.
     * @param idx the index to change.
     * @param newVal the new value.
     * @return the old value.
     * @throws IndexOutOfBoundsException if index is out of range.
     */
    public AnyType set( int idx, AnyType newVal )
    {
        Node<AnyType> p = getNode( idx );
        AnyType oldVal = p.data;
        
        p.data = newVal;   
        return oldVal;
    }
    
    /**
     * Gets the Node at position idx, which must range from 0 to size( ) - 1.
     * @param idx index to search at.
     * @return internal node corresponding to idx.
     * @throws IndexOutOfBoundsException if idx is not between 0 and size( ) - 1, inclusive.
     */
    private Node<AnyType> getNode( int idx )
    {
        return getNode( idx, 0, size( ) - 1 );
    }

    /**
     * Gets the Node at position idx, which must range from lower to upper.
     * @param idx index to search at.
     * @param lower lowest valid index.
     * @param upper highest valid index.
     * @return internal node corresponding to idx.
     * @throws IndexOutOfBoundsException if idx is not between lower and upper, inclusive.
     */    
    private Node<AnyType> getNode( int idx, int lower, int upper )
    {
        Node<AnyType> p;
        
        if( idx < lower || idx > upper )
            throw new IndexOutOfBoundsException( "getNode index: " + idx + "; size: " + size( ) );
            
        if( idx < size( ) / 2 )
        {
            p = beginMarker.next;
            for( int i = 0; i < idx; i++ )
                p = p.next;            
        }
        else
        {
            p = endMarker;
            for( int i = size( ); i > idx; i-- )
                p = p.prev;
        } 
        
        return p;
    }
    
    /**
     * Removes an item from this collection.
     * @param idx the index of the object.
     * @return the item was removed from the collection.
     */
    public AnyType remove( int idx )
    {
        return remove( getNode( idx ) );
    }
    
    /**
     * Removes the object contained in Node p.
     * @param p the Node containing the object.
     * @return the item was removed from the collection.
     */
    private AnyType remove( Node<AnyType> p )
    {
        p.next.prev = p.prev;
        p.prev.next = p.next;
        theSize--;
        modCount++;
        
        return p.data;
    }
 
    /**
     * Swaps data of objects at index1 and index 2
     * @param index1 the index of the object
     * @param index2 the index of the object
     */
    public void swap(int index1, int index2){
  	
    	if( index1 < 0 || index1 >= size( ) || index2 < 0 || index2 >= size( )){
    		throw new IndexOutOfBoundsException( "swap index1: " + index1 + ", index2: " + index2 );
    	}
    	
    	Node<AnyType> firstNode = getNode( index1 );
    	Node<AnyType> secondNode = getNode( index2 );
    	AnyType temp = firstNode.data;
    	firstNode.data = secondNode.data;
    	secondNode.data = temp;
    }
    
    /**
     * Creates and returns a new List that has the elements in reverse order
     * @return
     */
    public MyLinkedList<AnyType> reverse(){
    	 MyLinkedList<AnyType> newList = new MyLinkedList<>( );
    	 for(AnyType data : this){
    		 newList.add(0,data);
    	 }       
    	return newList;
    }
    
    /**
     * Erase number of elements from the list, starting from the given index position
     * @param index beginning index
     * @param noOfElements number of elements to be removed
     */
    public void erase(int index, int noOfElements ){
    	
    	if(noOfElements < 0 ){
    		throw new InvalidParameterException("Number of element must be positive ");
    	}
    	
    	Node<AnyType> firstNode = getNode( index );
    	Node<AnyType> lastNode = getNode( index + noOfElements );
    	firstNode.prev.next = lastNode;
    	lastNode.prev = firstNode.prev;
    			
    	theSize -= noOfElements;
        modCount += noOfElements;
    }
    
    public void insertList(int index, MyLinkedList<AnyType> list){
    	 java.util.Iterator<AnyType> itr = list.iterator( );  	    	 
         while( itr.hasNext( ) )
         {
                add(index++, itr.next());             
         }
    	
    }
    
    /**
     * Returns a String representation of this collection.
     */
    public String toString( )
    {
        StringBuilder sb = new StringBuilder( "[ " );

        for( AnyType x : this )
            sb.append( x + " " );
        sb.append( "]" );

        return new String( sb );
    }

    /**
     * Obtains an Iterator object used to traverse the collection.
     * @return an iterator positioned prior to the first element.
     */
    public java.util.Iterator<AnyType> iterator( )
    {
        return new LinkedListIterator( );
    }

    /**
     * This is the implementation of the LinkedListIterator.
     * It maintains a notion of a current position and of
     * course the implicit reference to the MyLinkedList.
     */
    private class LinkedListIterator implements java.util.Iterator<AnyType>
    {
        private Node<AnyType> current = beginMarker.next;
        private int expectedModCount = modCount;
        private boolean okToRemove = false;
        
        public boolean hasNext( )
        {
            return current != endMarker;
        }
        
        public AnyType next( )
        {
            if( modCount != expectedModCount )
                throw new java.util.ConcurrentModificationException( );
            if( !hasNext( ) )
                throw new java.util.NoSuchElementException( ); 
                   
            AnyType nextItem = current.data;
            current = current.next;
            okToRemove = true;
            return nextItem;
        }
        
        public void remove( )
        {
            if( modCount != expectedModCount )
                throw new java.util.ConcurrentModificationException( );
            if( !okToRemove )
                throw new IllegalStateException( );
                
            MyLinkedList.this.remove( current.prev );
            expectedModCount++;
            okToRemove = false;       
        }
    }
    
    /**
     * This is the doubly-linked list node.
     */
    private static class Node<AnyType>
    {
        public Node( AnyType d, Node<AnyType> p, Node<AnyType> n )
        {
            data = d; prev = p; next = n;
        }
        
        public AnyType data;
        public Node<AnyType>   prev;
        public Node<AnyType>   next;
    }
    
    private int theSize;
    private int modCount = 0;
    private Node<AnyType> beginMarker;
    private Node<AnyType> endMarker;
}

class TestLinkedList
{
    public static void main( String [ ] args )
    {
        MyLinkedList<Integer> lst = new MyLinkedList<>( );

        for( int i = 10; i < 21; i++ ){
                lst.add( i );
        }
        
        System.out.println("Original List");
        System.out.println( lst );
        
        lst.swap(2, 5);
        System.out.println("List after swapping element at index " + 2 + " and " +  5 );    
        System.out.println( lst );
        
        lst.swap(5, 2);
        System.out.println("List after swapping element at index " + 5 + " and " +  2 );    
        System.out.println( lst );
       
        
        System.out.println("Original List");
        System.out.println( lst );
        System.out.println("Reversed List");
        System.out.println( lst.reverse() );

        
        MyLinkedList<Integer> lst2 = new MyLinkedList<>( );
        for( int i = 5; i < 10; i++ ){
        	lst2.add( i ); 
        }
        
        System.out.println("Original List");
        System.out.println( lst );
        
        System.out.println( "List to insert ");
        System.out.println( lst2 );
        
        System.out.println("List after performing insertList operation at index "+lst.size()/2);
        lst.insertList(lst.size()/2, lst2);
        System.out.println( lst );
        
        System.out.println("List after performing erase.  start Index = "+ (lst.size()/2) + " and no of elements = " + lst2.size() );
        lst.erase(lst.size()/2, lst2.size());
        System.out.println(lst);
    }
}
