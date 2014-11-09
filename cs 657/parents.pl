male(tom).
male(peter).
male(doug).
male(david).
female(susan).
parent(doug, susan).
parent(tom, william).
parent(doug, david).
parent(doug, tom).
grandfather(GP, GC) :- male(GP), parent(GP, X), parent(X, GC).
child(X, Y):-parent(Y,X).