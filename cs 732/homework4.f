scc = lambda n. lambda s. lambda z. s (n s z);

zero = lambda s. lambda z. z;
one = scc zero;
two = scc one;
three = scc two;

plus = lambda m. lambda n. lambda s. lambda z. m s (n s z);
times = lambda m. lambda n. m (plus n) zero;
powr = lambda m. lambda n. n (times m) one;

realnat = lambda m. m (lambda x. succ x) 0;

powr three two;
realnat (powr three two);