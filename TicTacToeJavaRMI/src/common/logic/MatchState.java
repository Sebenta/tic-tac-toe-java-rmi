package common.logic;

public enum MatchState {
    X,           // Player "X" win
    O,           // Player "O" win
    LOCKED,      // A tie has occurred, no further moves are possible.
    WAIT,        // Wait for another player for start the match
    RUN,         // The match is in progress
    DOWN;        // The match is in down

    static MatchState get(String symbol) {
        switch (symbol.toLowerCase()) {
            case "host": return X;      // Return the host correspondent symbol
            case "adversary": return O; // Return the adversary correspondent symbol
            default: return null;
        }
    }

}
