import qualified Data.Map as Map
import Data.Char

letterFreq :: Map.Map Char Float
letterFreq = Map.fromAscList [	('a',0.08), ('b',0.015), ('c',0.03),  ('d',0.04),  ('e',0.13),  ('f',0.02), ('g',0.015),
								('h',0.06), ('i',0.065), ('j',0.005), ('k',0.005), ('l',0.035), ('m',0.03), ('n',0.07),
								('o',0.08), ('p',0.02),  ('q',0.002), ('r',0.065), ('s',0.06),  ('t',0.09), ('u',0.03),
								('v',0.01), ('w',0.015), ('x',0.005), ('y',0.02),  ('z', 0.002) ]

getCorr :: Char -> Float -> Float
getCorr c f = case (Map.lookup c letterFreq) of
					Nothing -> 0
					Just fp -> f * fp

countOccurences :: Eq a => a -> [a] -> Float
countOccurences x xs = fromIntegral $ length $ filter (==x) xs

shiftChar :: Int -> Char -> Char
shiftChar i c = chr $ (((ord c - ord 'a') - i + 26) `mod` 26) + ord 'a'

shiftList :: Int -> [Char] -> [Char]
shiftList i xs = map (shiftChar i) xs

freqCorrelation :: String -> Int -> Float
freqCorrelation xs i = sum $ zipWith getCorr (shiftList i ['a'..]) freqInCipher
	where freqInCipher = foldr (\x a -> (countOccurences x xs / (fromIntegral $ length xs)):a) [] ['a'..'z']