(defun answer-1 (L1 L2)
	(let ((L (remove-duplicates (append L1 L2))))
		(if (null L)
			nil
			(if (find (* -1 (car L)) L)
				(answer-1 (remove-if (lambda (x) (= (abs (car L)) (abs x))) L) ())
				(cons (car L) (answer-1 (cdr L) ()))
			)
		)
	)
)

(defun all (x) (every (lambda (y) y) x))

(defun any (x) (not (every #'null x)))

(defun clause-satisfies (A c) (any (mapcar (lambda (x) (find x A)) c)))

(defun answer-2 (A L) (all (mapcar (lambda (x) (clause-satisfies A x)) L)))

(defun attacks-horiz (a b) (= (car a) (car b)))

(defun attacks-vert (a b) (= (car (cdr a)) (car (cdr b))))

(defun attacks-diag (a b) (= (abs (- (car a) (car b))) (abs (- (car (cdr a)) (car (cdr b))))))

(defun attacks (a b) (or (attacks-horiz a b) (attacks-vert a b) (attacks-diag a b)))

(defun answer-3 (L)
	(if (null L)
		nil
		(if (any (mapcar (lambda (x) (attacks (car L) x)) (cdr L)))
			T
			(answer-3 (cdr L))
		)
	)
)

(defun distance (a b) (sqrt (+ (expt (- (car a) (car b)) 2) (expt (- (car (cdr a)) (car (cdr b))) 2))))

(defun linearize (L)
	(cond	((null L)	())
			((atom L)	(cons L ()))
			(t			(append (linearize (car L))
							(linearize (cdr L))))))

(defun answer-4 (L1 L2)
	(apply #'min
		(linearize (mapcar (lambda (x)
			(mapcar (lambda (y) (distance x y)) L2)
		) L1))
	)
)