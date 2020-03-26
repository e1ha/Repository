/****************************************************************************

                                                        Elaine Ha
							Teresa Truong
                                                        CSE 12, Fall 2019
                                                        December 1, 2019
                                                        cs12fa19ep
							cs12fa19cv
							
                                Assignment Nine

File Name:      Tree.c
Description:    This program is an implementation of a binary tree on disk.
		Unlike the binary tree data structure where TNodes go in
		the heap, the nodes are held in an immediate search path
		in memory and everything else is in the filesystem.  

****************************************************************************/

#include <stdlib.h>
#include <string.h>
#include "Tree.h"

// Debug messages
static const char ALLOCATE[] = " - Allocating]\n";
static const char COST_READ[] = "[Cost Increment (Disk Access): Reading ";
static const char COST_WRITE[] = "[Cost Increment (Disk Access): Writing ";
static const char DEALLOCATE[] = " - Deallocating]\n";
static const char TREE[] = "[Tree ";

template <class Whatever>
int Tree<Whatever>::debug_on = 0;

template <class Whatever>
long Tree<Whatever>::cost = 0;

template <class Whatever>
long Tree<Whatever>::operation = 0;

#ifndef TRUE
#define TRUE 1
#endif

#ifndef FALSE
#define FALSE 0
#endif

#define THRESHOLD 2

/*=========================================================================
Tree
Description:   This struct includes the constructor to initialize the 
	data fields of a tree and the destructors to delete all TNodes in the
       	tree. It also includes the functions called when the user wants to 
	insert, lookup, remove, and output the TNodes to a tree.


 Data Fields:
 	friend struct TNode<Whatever> : the TNode struct
 	occupancy: number of nodes that the tree has
	root: the tree's root	
	tree_count: the tree number
	debug: whether debug mode is on or off

 Public functions:
     Tree(void) - initializes each object in the TNode
     ~Tree (void) - destructor function to deallocate tree and all the TNodes
     Set_Debug_On - turns debug on
     Set_Debug_Off - turns debug off
     Insert - inserts a TNode
     Lookup - searches for a TNode
     Remove - removes a TNode
     ostream & Write - displays all the TNodes
==========================================================================*/
template <class Whatever>
ostream & operator << (ostream &, const TNode<Whatever> &);

/*=========================================================================
TNode
Description:   This struct includes the constructor to initialize the 
	data fields of a TNode and the destructors to decrease the occupancy.
       	It also includes the functions called when the user wants to insert, 
	lookup, remove, and output the TNodes in the tree.

 Data Fields:
 	balance: left child's height - right child's height
        data: the data to be inserted 
	height: 1 + height of tallest child or 0 for leaf	
	left: left node
	occupancy: number of nodes that the tree has	
	right: right node
	tree_count: the tree number

 Public functions:
     	TNode(const Whatever & element, Tree<Whatever> & theTree) - 
     		initializes each object in the TNode for an empty tree
     	TNode(const Whatever & element, TNode<Whatever> & parentTNode) - 
     		initializes each object in the TNode for a tree with a root
     	~TNode (void) - destructor function to decrement occupancy data
     	delete_allTNodes - deletes all nodes of tree
	Insert - inserts the TNode into the tree
	Lookup - searches for the TNode
	ReplaceAndRemoveMin - reorganizes Tree if a TNode has a balance
			      greater than two
	Remove - Removes the TNode
	SetHeightAndBalance - calculates height and balance for each TNode
	ostream & WriteAllTNodes - displays all the TNodes
==========================================================================*/
template <class Whatever>
struct  TNode {
// Friends:

// Data fields:
	Whatever data;		// Whatever data
	long height;		// 1 + height of tallest child
	long balance;		// Left child height - right child height
	offset left;		// Left TNode
	offset right;		// Right TNode
	offset this_position;	// Current position

// Function fields:
	TNode () : height (0), balance (0), left (0), right (0), 
		this_position (0) {}

	// To declare the working TNode in Tree's Remove
	TNode (Whatever & element) : data (element), height (0), balance (0),
		left (0), right (0), this_position (0) {}
	
	TNode (Whatever &, fstream *, long &);	// To add new node to disk
	TNode (const offset &, fstream *);	// To read node from disk
	
	unsigned long Insert (Whatever &, fstream *, long &, offset &);
	// Optional recursive Lookup declaration would go here
	void Read (const offset &, fstream *);	// Read node from disk
	unsigned long Remove (TNode<Whatever> &, fstream *, long &, offset &,
		long fromSHB = FALSE);
	void ReplaceAndRemoveMin (TNode<Whatever> &, fstream *, offset &);
	void SetHeightAndBalance (fstream *, offset &);
	void Write (fstream *) const;		// Update node to disk

	ostream & Write_AllTNodes (ostream &, fstream *) const;
	unsigned long Lookup(Whatever &, fstream *) const; // Look ups a node	
};


template <class Whatever>
void Tree<Whatever> :: Set_Debug_Off(void) 
/***************************************************************************
% Routine Name : Tree <Whatever> :: Set_Debug_Off 
% File :         Tree.c
% 
% Description :  This function sets debug mode off
%
***************************************************************************/
{
	debug_on = FALSE; 
}

template <class Whatever>
void Tree<Whatever> :: Set_Debug_On(void) 
/***************************************************************************
% Routine Name : Tree <Whatever>:: Set_Debug_On
% File :         Tree.c
% 
% Description :  This function sets debug mode on
%
***************************************************************************/
{
	debug_on = TRUE; 
}

template <class Whatever>
unsigned long Tree<Whatever> :: Insert (Whatever & element)
/***************************************************************************
% Routine Name : Tree <Whatever> :: Insert
% File :         Tree.c
% 
% Description : This function will insert create the root if it does not 
%		exist. If root already exists, then it will call 
%		TNode's Insert. 
%
% Parameters descriptions :
%  
% name               description
% ------------------ ------------------------------------------------------
% element            The element to insert.
% <return>           1 or 0 indicating success or failure of insertion
***************************************************************************/
{
	// Determines if tree is empty
	if (occupancy <= 0) {

		// Creates new node if tree is empty
		TNode<Whatever>rootTNode(element, fio, occupancy);

	} else {

		// If tree is not empty, insert a node under root
		TNode<Whatever>rootTNode(root, fio); 
		rootTNode.Insert(element, fio, occupancy, root); 
	}

	// Call IncrementOperation
	Tree<Whatever>::IncrementOperation(); 

	return 1; 
}

template <class Whatever>
unsigned long TNode<Whatever> :: Lookup(Whatever & element,
					fstream * fio) const
/***************************************************************************
% Routine Name : TNode <Whatever>:: Lookup
% File :         Tree.c
% 
% Description : This function will lookup the element in the binary tree. If
%               element is found then lookup was successful. 
%
% Parameters descriptions :
%  
% name               description
% ------------------ ------------------------------------------------------
% element            The element to lookup.
% <return>           1 or 0 depending on success status of lookup
***************************************************************************/
{
	// Save the success of lookup
	long status; 

	// Determine if the element is found
	if (data == element) {

		// Update data
		element = data; 
		return 1;

	} else {

		// If element is alphanumerically less than data
		if (element < data) {

			// Determine if left node exists
			if (left){

				TNode<Whatever> leftTNode(left, fio);
				status = leftTNode.Lookup(element, fio);

			} else {

				return 0;  
			}

		// If element is alphanumerically greater than data
		} else {

			// Determine if right node exists
			if (right) {
				
				TNode<Whatever> rightTNode(right, fio);
				status = rightTNode.Lookup(element, fio);

			} else {

				return 0;  
			}
		}
	}

	return status; 
}

template <class Whatever>
void TNode<Whatever> :: ReplaceAndRemoveMin (TNode<Whatever> & targetTNode, 
	fstream * fio, offset & PositionInParent) 
/***************************************************************************
% Routine Name : TNode<Whatever> :: ReplaceAndRemoveMin
% File :         Tree.c
% 
% Description : This function will replace the TNode that is removed
%               with the minimum TNode. It will find first find the 
%		minimum TNode by recursing to the most left node and 
%		update the POinterInParent and replace the data of the 
%		node with the successor node's data. Finally, it will 
%		delete the successor node.
%
% Parameters descriptions :
%  
% name               description
% --------------------------------------------------------------------
% targetTNode	     TNode to remove
% PointerInParent    pointer in the parent TNode used to get to the 
%		     current node
***************************************************************************/
{
	// Determines if left node exists
	if (left) {

		TNode<Whatever>leftTNode(left,fio); 	
		leftTNode.ReplaceAndRemoveMin(targetTNode, fio, left);
		SetHeightAndBalance(fio, PositionInParent); 
	} else {

		// Determines if right node exists
		if (right) {

			// Update PositionInParent
			PositionInParent = right;

		} else {

			// Update PositionInParent
			PositionInParent = 0;
		}

		// Replace data of node deleted with successor data 
		targetTNode.data = data; 
	}
}

template <class Whatever>
unsigned long TNode<Whatever> :: Remove (TNode<Whatever> & elementTNode,
	fstream * fio, long & occupancy, offset & PositionInParent,
	long fromSHB)
/***************************************************************************
% Routine Name : TNode<Whatever> :: Remove
% File :         Tree.c
% 
% Description : This function will remove the element from the binary 
%		tree. If element is found, delete it and then reset 
%		the data. If element is alphabetically greater than 
%		the temporary, then check the right node. If 
%		element is alphabetically greater than the node being 
%		checked, then right node will be checked. Continues 
%		checking until there is nothing under that node.
%
% Parameters descriptions :
%  
% name               description
% ------------------ -------------------------------------------------
% elementTNode        The element to be removed.
% PointerInParent     TNode pointer in the parent TNode used to get to
%		      to the current TNode
% fromSHB	      keeps track of whether Remove was called from 
%		      SetHeightAndBalance or Remove
% <return>            1 if removed, else 0
***************************************************************************/
{
	// Saves the success of the Remove
	long status= 0;

	// Determines if the node to be removed is found
	if (elementTNode.data == data) {

		// Decrement occupancy
		occupancy--; 
			
		// If leaf node
		if (!left && !right) {

			// Update output parameter and PositionInParent
			elementTNode.data = data;
			PositionInParent = 0;

			return 1;
		
		// If only left child exists
		} else if (left && !right) {

			// Update output parameter and PositionInParent
			elementTNode.data = data; 
			PositionInParent = left; 

			return 1; 
		
		// If only right child exists
		} else if (!left && right) {

			// Update output parameter and PositionInParent
			elementTNode.data = data; 
			PositionInParent = right; 

			return 1; 
		
		// If both children exist
		} else if (left && right) {

			// Update output parameter
			elementTNode.data = data;

			// Call RARM
			TNode<Whatever>rightTNode(right, fio);
			rightTNode.ReplaceAndRemoveMin(*this, fio, right);	

			// If called from SHAB
			if (fromSHB == FALSE) {

				SetHeightAndBalance(fio, PositionInParent); 
			} else {

				Write(fio);
			}
	
			return 1; 
		}

	} else {

		// If alphanumerically greater than current node 
		if (data < elementTNode.data) {

			// Determines if right node exists
			if (right) {

				TNode<Whatever>rChild(right, fio);
				status = rChild.Remove(elementTNode, fio,
						       occupancy, right);
			} else {

				return 0;	
			}

		// If alphanumerically less than current node
		} else { 

			// Determines if left node exists
			if (left) {
				TNode<Whatever>lChild(right, fio);
				status = lChild.Remove(elementTNode, fio,
						       occupancy, left);

			} else {

				return 0; 
			}
		}
	}

	// Call SHAB
	if (fromSHB == FALSE) { 
		SetHeightAndBalance(fio, PositionInParent); 
	}
	
	return status; 	
}
	
template <class Whatever>
unsigned long Tree<Whatever> :: Remove (Whatever & element)
/***************************************************************************
% Routine Name : Tree <Whatever>:: Remove
% File :         Tree.c
% 
% Description : This function will remove the element in the binary tree.  If
%               it is not an empty tree, call TNode's Remove method and 
%		update the output parameter
%
% Parameters descriptions :
%  
% name               description
% ------------------ ------------------------------------------------------
% element            The element to remove.
% <return>           1 or 0 depending on success of removal
***************************************************************************/
{
	// Success of remove
	long status = 0; 
	
	// Determines if list is empty	
	if (occupancy > 0) {

		// Create a new TNode with the element and call Remove
	 	TNode <Whatever> tmp (element);  
		TNode<Whatever> rootTNode(root, fio); 
		status = rootTNode.Remove(tmp, fio, occupancy, root, 0);

		// Updates the output parameter
		element = tmp.data; 
	}

	// If root is removed
	if (occupancy == 0) {

		ResetRoot();
	}

	// Call IncrementOperation
	Tree<Whatever>::IncrementOperation(); 

	return status;  
}

template <class Whatever>
void TNode<Whatever> :: SetHeightAndBalance (fstream * fio,
	offset & PositionInParent)
/***************************************************************************
% Routine Name : TNode<Whatever> :: SetHeightAndBalance 
% File :         Tree.c
% 
% Description : This function calculates the height and the balanced
%		 depending on the number of children the node has. It 
%	 	 makes adjustments if the balance is greater than two.
%
% Parameters descriptions :
% 
% name               description
% -------------------------------------------------------------------
% PointerInParent     TNode pointer in the parent TNode used to get 
%		      to the current node
***************************************************************************/
{	
	// Initialize fakeOccupancy local variable
	long fakeOccupancy = 0;
	
	// If only right node exists
	if (!left && right) {

		TNode<Whatever>rightTNode(right, fio);
			
		// Calculate height and balance
		balance = -1 - rightTNode.height; 
		height = 1 + rightTNode.height; 			 	
						
	// If only left node exists	
	} else if (!right && left) {

		TNode<Whatever>leftTNode(left, fio);

		// Calculate height and balance
		balance = leftTNode.height - -1; 
		height = 1 + leftTNode.height; 
			
	// If both children exist
	} else if (left && right) {

		TNode<Whatever>leftTNode(left, fio);
		TNode<Whatever>rightTNode(right, fio);
			
		// Calculate balance
		balance = leftTNode.height - rightTNode.height;

		// Calculate height
		if (leftTNode.height >= rightTNode.height) {

			height = 1 + leftTNode.height;

		} else if (leftTNode.height < rightTNode.height) {

			height = 1 + rightTNode.height; 	
		}

	// If leaf node
	} else if (!right && !left) {

		// Set height and balance to 0
		balance = 0; 
		height = 0;
	}
	
	// Checks if threshold is violated
	if (abs(balance) > THRESHOLD) {

		// Saves the current node
		Whatever savedElement = data;

		// Removes the node
		Remove(*this, fio, fakeOccupancy,
				 PositionInParent, TRUE);

		// Reinserts the node
		TNode<Whatever>parentTNode(PositionInParent,fio);
		parentTNode.Insert(savedElement, fio, fakeOccupancy,
				   PositionInParent);

	// Write to file
	} else {

		Write(fio);
	}	
}

template <class Whatever>
long Tree <Whatever> :: GetCost () 
/***************************************************************************
% Routine Name : Tree <Whatever> :: GetCost
% File :         Tree.c
% 
% Description : This function returns the value of the Tree<Whatever>::cost
%		variable.
%
% Parameters descriptions :
%  
% name               description
% ------------------------------------------------------------------------
% <return>           Value of cost
***************************************************************************/
{
	return cost; 
}

template <class Whatever>
long Tree <Whatever> :: GetOperation () 
/***************************************************************************
% Routine Name : Tree <Whatever> :: GetOperation
% File :         Tree.c
% 
% Description : This function returns the value of the 
%		Tree<Whatever>::operation variable.
%
% Parameters descriptions :
%  
% name               description
% ------------------------------------------------------------------------
% <return>           Value of operation
***************************************************************************/
{
	return operation; 
}

template <class Whatever>
void Tree <Whatever> :: IncrementCost ()
/***************************************************************************
% Routine Name : Tree <Whatever> :: IncrementCost
% File :         Tree.c
% 
% Description : This function increments the value of the 
%		Tree<Whatever>::cost variable. This function should be called
%		when a read or write to disk occurs.
%
% Parameters descriptions :
%  
% name               description
% ------------------------------------------------------------------------
% <return>           Void
***************************************************************************/
{
	cost++; 
}

template <class Whatever>
void Tree <Whatever> :: IncrementOperation () 
/***************************************************************************
% Routine Name : Tree <Whatever> :: IncrementOperation
% File :         Tree.c
% 
% Description : This function increments the value of the 
%		Tree<Whatever>::operation variable. This function should
%		be called when a read or write to disk occurs.
%
% Parameters descriptions :
%  
% name               description
% ------------------------------------------------------------------------
% <return>           Void
***************************************************************************/
{
	operation++; 
}

template <class Whatever>
void Tree <Whatever> :: ResetRoot ()
/***************************************************************************
% Routine Name : Tree <Whatever> :: ResetRoot
% File :         Tree.c
% 
% Description : This function resets the root datafield of this tree to
%		be at the end of the datafile. This should be called when
%		the last TNode has been removed from the Tree.
%
% Parameters descriptions :
%  
% name               description
% ------------------------------------------------------------------------
% <return>           Void
***************************************************************************/
{
	// Move root field to point to EOF
	fio->seekp(0, ios::end);
	root = fio->tellp(); 
}

template <class Whatever>
unsigned long TNode<Whatever> :: Insert (Whatever & element, fstream * fio,
	long & occupancy, offset & PositionInParent)
/***************************************************************************
% Routine Name : TNode <Whatever>:: Insert
% File :         Tree.c
% 
% Description : This function will insert the element in the hash table.
%               If element is on the tree, update it. If element is not on
%		the tree, call the function recursively until it reaches
%		the appropriate location and insert the new node after.
%		Finally, update the height and the balance. 
%
% Parameters descriptions :
%  
% name               description
% ------------------ ------------------------------------------------------
% element            The element to insert.
% PointerInParent    pointer to parent TNode used to get the current TNode
% <return>           1 or 0 indicating success or failure of insertion
***************************************************************************/
{
	// If element is found
        if (data == element) {

		// Update data
		data = element;

	// Find 
	} else {

		// If element is alphanumerically less than data
		if (element < data) {

			// If left exists, recursively call Insert
			if (left) {

				TNode<Whatever>leftTNode(left, fio); 
				leftTNode.Insert(element, fio, occupancy,
						 left);

			// If left does not exist, insert new node
			} else {

				TNode<Whatever>leftTNode(element, fio,
						         occupancy); 
				left = leftTNode.this_position; 
			}

		// If element is alphanumerically greater than data
		} else {

			// If right exists, recursively call Insert
			if (right) {

				TNode<Whatever>rightTNode(right, fio); 
				rightTNode.Insert(element, fio, occupancy,
						  right);

			// If right does not exist, insert new node
			} else {

				TNode<Whatever>rightTNode(element, fio,
						          occupancy); 
				right = rightTNode.this_position;
			}	
		}
	}
	
	// Call SHAB
	SetHeightAndBalance(fio, PositionInParent); 
	
	return 1; 
}

template <class Whatever>
unsigned long Tree<Whatever> :: Lookup (Whatever & element) const
/***************************************************************************
% Routine Name : Tree <Whatever>:: Lookup
% File :         Tree.c
% 
% Description : This function will lookup the element in the binary tree.
%               If it is not an empty tree, call TNode's Lookup method.  
%
% Parameters descriptions :
%  
% name               description
% ------------------ ------------------------------------------------------
% element            The element to lookup.
% <return>           1 or 0 depending on success of lookup
***************************************************************************/
{
	// Initialize local variables
	long status = 0; 
	 

	// Call IncrementOperation
	Tree<Whatever>::IncrementOperation(); 

	// Determines if tree is empty
	if (occupancy > 0) {
		TNode<Whatever>rootTNode(root, fio);
		status = rootTNode.Lookup(element, fio);
	}

	return status;  
}

template <class Whatever>
void TNode<Whatever> :: Read (const offset & position, fstream * fio)
/***************************************************************************
% Routine Name : TNode<Whatever> :: Read 
% File :         Tree.c
% 
% Description : This functions reads a TNode which is present on the
%		datafile into memory. The TNode is read from position.
%		The TNode's information in the datafile overwrites this
%		TNode's data.
%		
% Parameters descriptions :
% 
% name               description
% -------------------------------------------------------------------
% PointerInParent     TNode pointer in the parent TNode used to get 
%		      to the current node
***************************************************************************/
{
	// Read from file
	fio->seekg(position);
	fio->read((char *) this, sizeof(*this));
	Tree < Whatever> :: IncrementCost(); 

	// Debug message: cost read
	if (Tree<Whatever>::debug_on) {
		cerr << COST_READ << (const char *) data << "]" << endl;
	}	
}

template <class Whatever> // read constructor
TNode<Whatever> :: TNode (const offset & position, fstream * fio) 
/***************************************************************************
% Routine Name : TNode<Whatever> :: TNode read constructor
% File :         Tree.c
%
% Description :  Called when reading a TNode present on disk into memory
***************************************************************************/
{
	Read (position, fio);
}

template <class Whatever> //write
TNode<Whatever> :: TNode (Whatever & element, fstream * fio, long & occupancy): 
			data (element), height (0), balance (0), left (0), 
			right (0)
/***************************************************************************
% Routine Name : TNode<Whatever> :: TNode write constructor
% File :         Tree.c
%
% Description :  Called when creating a TNode for the first time.
***************************************************************************/
{
	// Increment occupancy
	occupancy++;

	// Write to file
	fio->seekp(0, ios :: end); 
	this_position = fio->tellp(); 
	Write(fio);
}

template <class Whatever>
void TNode<Whatever> :: Write (fstream * fio) const
/***************************************************************************
% Routine Name : TNode<Whatever> :: Write
% File :         Tree.c
% 
% Description : This functions writes this TNode object to disk at
		this_position in the datafile.
		
% Parameters descriptions :
% 
% name               description
% -------------------------------------------------------------------
% PointerInParent     TNode pointer in the parent TNode used to get 
%		      to the current node
***************************************************************************/
{
	// Debug message: cost write
	if (Tree<Whatever>::debug_on) {
		cerr << COST_WRITE << (const char *) data << "]" << endl;
	}

	// Seek and write this
	fio->seekp(this_position);
	fio->write( (const char *) this, sizeof(*this) );
	Tree< Whatever> :: IncrementCost(); 
}

template <class Whatever>
Tree<Whatever> :: Tree (const char * datafile) :
	fio (new fstream (datafile, ios :: out | ios :: in))
/***************************************************************************
% Routine Name : Tree<Whatever> :: Tree  (public)
% File :         Tree.c
%
% Description :  Allocates the tree object. Checks the datafile to see if
%		 it contains Tree data. If it is empty, root and occupancy
%		 fields are written to the file. If there is data in the
%		 datafile, root and occupancy fields are read into memory.
***************************************************************************/
{
	// Tree count	
	static long counter; 
	tree_count = ++counter; 

	// Debug message: allocate 
	if (debug_on) {
		cerr << TREE << tree_count << ALLOCATE;
	}

	// Check for empty file
	fio->seekg (0, ios::beg); 
	offset begin = fio->tellg(); 
	fio->seekg (0, ios::end); 
	offset ending = fio->tellg(); 

	// File is empty
	if (begin == ending) {

		// Initialize local variables
		root = 0;
		occupancy = 0;

		// Reserve space for root and occupancy
		fio->seekp(0, ios::beg); 
		fio->write( (const char*) &root, sizeof(root) ); 
		fio->write( (const char*) &occupancy, sizeof(occupancy) ); 
		root = fio->tellp();

	} else {				// File has contents

		// Read root and occupancy
		fio->seekg (0, ios::beg); 
		fio->read ( (char*) &root, sizeof(root) ); 
		fio->read ( (char*) &occupancy, sizeof(occupancy) ); 
	}
}

template <class Whatever>
Tree<Whatever> :: ~Tree (void)
/***************************************************************************
% Routine Name : Tree<Whatever> :: ~Tree  (public)
% File :         Tree.c
% 
% Description :  Deallocates memory associated with the Tree.  It
%                will also delete all the memory of the elements within
%                the table.
***************************************************************************/
{
	// Write root and occupancy
	fio->seekp (0, ios::beg); 
	fio->write( (const char *) &root, sizeof(root) ); 
	fio->write( (const char *) &occupancy, sizeof(occupancy) ); 
		
	// Deallocate fio
	fio->flush();
	delete fio;

	// Debug message: deallocate 
	if (debug_on) {
		cerr << TREE << tree_count << DEALLOCATE;
	}
}

template <class Whatever>
ostream & operator << (ostream & stream, const TNode<Whatever> & nnn) {
	stream << "at height:  :" << nnn.height << " with balance:  "
		<< nnn.balance << "  ";
	return stream << nnn.data << "\n";
}

template <class Whatever>
ostream & Tree<Whatever> :: Write (ostream & stream) const
/***************************************************************************
% Routine Name : Tree :: Write (public)
% File :         Tree.c
% 
% Description : This funtion will output the contents of the Tree table
%               to the stream specificed by the caller.  The stream could be
%               cerr, cout, or any other valid stream.
%
% Parameters descriptions :
% 
% name               description
% ------------------ ------------------------------------------------------
% stream             A reference to the output stream.
% <return>           A reference to the output stream.
***************************************************************************/
{
        long old_cost = cost;

	stream << "Tree " << tree_count << ":\n"
		<< "occupancy is " << occupancy << " elements.\n";

	fio->seekg (0, ios :: end);
	offset end = fio->tellg ();

	// check for new file
	if (root != end) {
		TNode<Whatever> readRootNode (root, fio);
		readRootNode.Write_AllTNodes (stream, fio);
	}

        // ignore cost when displaying nodes to users
        cost = old_cost;

	return stream;
}

template <class Whatever>
ostream & TNode<Whatever> ::
Write_AllTNodes (ostream & stream, fstream * fio) const {
	if (left) {
		TNode<Whatever> readLeftNode (left, fio);
		readLeftNode.Write_AllTNodes (stream, fio);
	}
	stream << *this;
	if (right) {
		TNode<Whatever> readRightNode (right, fio);
		readRightNode.Write_AllTNodes (stream, fio);
	}

	return stream;
}

