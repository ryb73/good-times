youngerThan(X, Y) :- age(X, 4); age(X, 5), age(Y, 6).
youngerThan(huey, X) :- shirtColor(X, green).

age(X, 5) :- shirtDesign(X, camel).
shirtDesign(X, camel) :- age(X, 5).

shirtColor(dewey, yellow).

shirtDesign(louie, giraffe).

shirtDesign(X, panda) :- not_shirtColor(X, white).
not_shirtColor(X, white) :- shirtDesign(X, panda).
not_shirtColor(X, Y) :- not(shirtColor(X, Y)).

switchorder([], New).
switchorder([X|L], [X|New]) :- switchorder(L, New).