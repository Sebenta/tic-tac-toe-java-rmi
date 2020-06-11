package common.logic;

import java.io.Serializable;

public class Player implements Serializable {

    //region Attributes
    private String username;
    private PlayerState state;
    private MatchState symbol;
    private Match match;
    //endregion

    //region Constructor
    public Player(String username) {
        this.username = username;
        this.state = PlayerState.FREE;
    }
    //endregion

    //region Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public PlayerState getState() { return state; }
    public void setState(PlayerState state) { this.state = state; }

    public Match getMatch() { return match; }
    public void setMatch(Match match) { this.match = match; }

    public MatchState getSymbol() { return symbol; }
    public void setSymbol(MatchState symbol) { this.symbol = symbol; }
    //endregion

    /**
     * Execute the move
     *
     * @param row    Selected row
     * @param col    Selected column
     * @throws IllegalAccessException         This exception will be thrown when:
     *                                        <ul>
     *                                        <li>Try to make a move but the game is not running.</li>
     *                                        <li>The player trying to make the move is not authorized.</li>
     *                                        <li>The field is busy</li>
     *                                        </ul>
     *
     * @throws ArrayIndexOutOfBoundsException One of the selected values ​​(row or column) corresponds to a position outside the board.
     */
    public void move(int row, int col) throws ArrayIndexOutOfBoundsException, IllegalAccessException {
        match.move(this, row, col);
    }

    /**
     * Create a new game
     *
     * @return Returns the created match
     * @see Match
     */
    public Match createMatch() {
        this.match = new Match(this);
        this.match.setState(MatchState.WAIT);
        return this.match;
    }

    @Override
    public String toString() {
        return "Player:{" + "name:" + this.username + "," + "state:" + this.state + "," + "symbol:" + this.symbol + ","
                + "match: " + this.match + "}";
    }

    public String toBeautifyString(){
        return ""
                +"Username: " + getUsername() + " ; "
                +"State: " + getState()
                +"";
    }
}
