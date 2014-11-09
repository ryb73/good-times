fun   complement nil = nil
	| complement n =
		let
			fun   comp2 (0::xs) true = (0 :: (comp2 xs true))
				| comp2 (1::xs) true = (1 :: (comp2 xs false))
				| comp2 (0::xs) false = (1 :: (comp2 xs false))
				| comp2 (1::xs) false = (0 :: (comp2 xs false))
				| comp2 xs copy = []
		in
			rev (comp2 (rev n) true)
		end;

fun   binToInt (x::xs) =
		let
			fun   calcValues (y::ys) = [y] @ (map (fn n => n * 2) (calcValues ys))
				| calcValues nil = nil
			fun sum l = foldl (op +) 0 l
            fun decode bin = sum (calcValues (rev bin))
		in
			if length (x::xs) > 8 then binToInt (tl (x::xs))
            else if length (x::xs) = 8 andalso x = 1 then ~(decode (complement (x::xs)))
			else decode (x::xs)
		end
	| binToInt nil = 0;

fun pad8 x = 	if length x < 8 then pad8 (0::x)
				else x

fun   intToBin 0 = []
	| intToBin n =
		if n < 0 then complement (pad8 (intToBin (~n)))
		else (intToBin (n div 2)) @ [n mod 2];