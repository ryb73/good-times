sibling(X, Y) :-
	parent(P1, X), parent(P1, Y),
	parent(P2, X), parent(P2, Y),
	not(X=Y), not(P1=P2).
brother(X, Y) :- sibling(X, Y), gender(X, male).
sister(X, Y) :- sibling(X, Y), gender(X, female).

father(X, Y) :- parent(X, Y), gender(X, male).
mother(X, Y) :- parent(X, Y), gender(X, female).

uncle(X, Y) :- sibling(X, Z), parent(Z, Y), gender(X, male).
uncle(X, Y) :- married(X, S), (aunt(S, Y); uncle(S, Y)).
aunt(X, Y) :- sibling(X, Z), parent(Z, Y), gender(X, female).
aunt(X, Y) :- married(X, S), (aunt(S, Y); uncle(S, Y)).

nephew(X, Y) :- (aunt(Y, X); uncle(Y, X)), gender(X, male).
niece(X, Y) :- (aunt(Y, X); uncle(Y, X)), gender(X, female).

halfSibling(X, Y) :-
	parent(P1, X), parent(P1, Y), parent(P2, X),
	parent(P3, Y), not(P2=P3), not(P1=P2).
halfBrother(X, Y) :- halfSibling(X, Y), gender(X, male).
halfSister(X, Y) :- halfSibling(X, Y), gender(X, female).

stepParent(X, Y) :- married(X, S), parent(S, Y), not(parent(X, Y)).
stepFather(X, Y) :- stepParent(X, Y), gender(X, male).
stepMother(X, Y) :- stepParent(X, Y), gender(X, female).

stepSibling(X, Y) :- stepParent(SP, X), parent(SP, Y).
stepBrother(X, Y) :- stepSibling(X, Y), gender(X, male).
stepSister(X, Y) :- stepSibling(X, Y), gender(X, female).

bloodRelative(X, Y) :- sibling(X, Y); parent(X, Y); parent(Y, X).
bloodRelative(X, Y) :- bloodRelative(X, Z), bloodRelative(Z, Y), not(X=Y).

relative(X, Y) :-
	uncle(X, Y); aunt(X, Y); nephew(X, Y); niece(X, Y);
	stepParent(X, Y); stepParent(Y, X); stepSibling(X, Y);
	bloodRelative(X, Y).
relative(X, Y) :- relative(X, Z), relative(Z, Y), not(X=Y).

inLaw(X, Y) :- relative(X, Y), not(bloodRelative(X, Y)).

single(X) :- not(married(X, Y)).
% married(X, Y) :- married(Y, X).
notMarriedOrDivorced(X, Y) :- not(married(X, Y)), not(divorced(X, Y)).

parent(birger, johan).
parent(birger, fredrik).
married(fredrik, ulrika).
parent(fredrik, richard).
parent(fredrik, harald).
parent(ulrika, harald).
parent(fredrik, greger).
parent(ulrika, greger).
parent(fredrik, gustav).
parent(ulrika, gustav).
parent(ulrika, henrik).
married(richard, margareta).
parent(richard, gottfried).
parent(margareta, gottfried).
divorced(gottfried, isabella).
parent(gottfried, martin).
parent(isabella, martin).
parent(gottfried, harriet).
parent(isabella, harriet).
married(harald, ingrid).
parent(harald, cecilia).
parent(ingrid, cecilia).
parent(harald, anita).
parent(ingrid, anita).
married(greger, gerda).
parent(greger, alexander).
parent(gerda, alexander).
married(henrik, edith).

gender(birger, male).
gender(johan, male).
gender(fredrik, male).
gender(ulrika, female).
gender(richard, male).
gender(harald, male).
gender(greger, male).
gender(gustav, male).
gender(henrik, male).
gender(margareta, female).
gender(gottfried, male).
gender(isabella, female).
gender(martin, male).
gender(harriet, female).
gender(ingrid, female).
gender(cecilia, female).
gender(anita, female).
gender(gerda, female).
gender(alexander, male).
gender(edith, female).