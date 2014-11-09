import qualified Data.Text as T

main = interact readLines

readLines :: String -> String
readLines input = unlines $ snd $ foldl (\acc x ->
	if (fst acc) then (triggerOff (snd acc) x)
				 else (triggerOn  (snd acc) x) ) (False, []) $ lines input

triggerOn :: [String] -> String -> (Bool, [String])
triggerOn xs x = if (x `startsWith` "theorem") || (x `startsWith` "lemma") then
					(True, xs ++ [x])
				 else
					(False, xs)

triggerOff :: [String] -> String -> (Bool, [String])
triggerOff xs x = if (x `contains` "exists") then
					(False, xs ++ [x, ""])
				  else
					(True, xs ++ [x])

startsWith :: Eq a => [a] -> [a] -> Bool
xs `startsWith` ys = and $ (zipWith (==) xs ys) ++ [length xs >= length ys]

trim :: String -> String
trim = T.unpack . T.strip . T.pack

contains :: String -> String -> Bool
(x:xs) `contains` ys = (x:xs) `startsWith` ys || (xs `contains` ys)
[] `contains` ys = False