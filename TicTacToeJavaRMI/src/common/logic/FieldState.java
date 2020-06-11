package common.logic;

public enum  FieldState {
    X,      // Corresponding an "X" symbol
    O,      // Corresponding an "O" symbol
    FREE;   // Corresponding a free space

    /**
     * Returns the Field State that corresponds to a given MatchStat
     *
     * @param symbol The MatchState for which we want to know the corresponding
     * @return Returns the corresponding FieldState
     * @see MatchState
     */
    static FieldState get(MatchState symbol) {
        switch (symbol) {
            case X: return X;
            case O: return O;
            default: return null;
        }
    }
}
