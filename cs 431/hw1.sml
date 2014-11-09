datatype btree =
	Empty |
	Node of btree * int * btree;

fun   addNode n Empty = Node(Empty, n, Empty)
	| addNode n (Node(left, value, right)) =
		if n < value then Node((addNode n left), value, right)
		else Node(left, value, (addNode n right));

fun   addList (x::xs) tree = addList xs (addNode x tree)
	| addList [] tree = tree

fun listToTree l = addList l Empty;

fun   treeToList Empty = []
	| treeToList (Node(left, value, right)) = (treeToList left) @ value :: (treeToList right);