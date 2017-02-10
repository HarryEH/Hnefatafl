# Hnefatafl
The Viking Game

## Questions and meeting points:
We need to decide how to utilize the time that the other AI spends processing. - Can we assume that this will be 10 seconds?
^^ We need to think about making it a robust piece of software early - don't get too bogged down in algos - We are SET ^^ -(Andy Baker)
We need to be sure of the rules - ask questions such as can a pawn take a piece against 5,5 while the king is on it?
Optimization? Rapid prototyping in Java - what will final implementation be written in - C/C++ for optimization. Might have limitations on the VM - ie no JVM etc. So should be a compiled executable. Doesn't exclude much.
Functional? - Haskell?? ;)
Randomness is probably a good enough solution if we can get good enough performance for a Monte Carlo approach. Assumption of a greedy algo is too naive.
Current solution uses 99% of 8 core i7... Need to be sure of VM specage
@Harry - more work over fewer threads?
Current version easily outplayed by black corner blocking
Checks many more moves when the RandomDepthPlayer is black? Why would this be? Still sucks.
State machine strategy at the start of the game. End game weight good board positions highly? eg  WxxxPkPxxxW
																								  xxxxPxPxxxx
																								  xxxxPPPxxxx	
