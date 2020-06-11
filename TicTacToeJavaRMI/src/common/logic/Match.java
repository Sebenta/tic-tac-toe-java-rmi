package common.logic;

import java.io.Serializable;
import java.util.Random;

public class Match implements Serializable {

    //region Attributes
    private Player host;
    private Player adversary;
    private Player current;
    private MatchState state;
    private Board board;
    //endregion

    //region Constructor
    public Match(final Player player) {
        this.host = player;
        this.host.setSymbol(MatchState.get("host"));
        this.host.setState(PlayerState.WAIT);
        this.adversary = null;
        this.state = MatchState.DOWN;
        this.board = new Board(3, 3);
    }
    //endregion

    //region Getters and Setters
    public Player getHost() {
        return host;
    }

    public void setHost(Player host) {
        this.host = host;
    }

    public Player getAdversary() {
        return adversary;
    }

    /**
     * Add the opponent who is missing the match.
     * Put the host player and the opposing player in busy mode.
     * And decide who will be the first player to play
     *
     * @param adversary a player who will take on the role of opponent
     */
    public void setAdversary(Player adversary) {
        this.adversary = adversary;
        this.host.setState(PlayerState.BUSY);
        this.adversary.setState(PlayerState.BUSY);
        this.current = new Random().nextBoolean() ? this.host : this.adversary;
        this.adversary.setSymbol(MatchState.get("adversary"));
    }

    public Player getCurrent() {
        return current;
    }

    public void setCurrent(Player current) {
        this.current = current;
    }

    public MatchState getState() {
        return state;
    }

    public void setState(MatchState state) {
        this.state = state;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }
    //endregion

    /**
     * Changes the player who can play
     */
    private void changeCurrentPlayer() {
        current = current == host ? adversary : host;
    }

    /**
     * Checks whether the move is valid
     *
     * <ol>
     *     <li>Is the game running?</li>
     *     <li>Is this player's turn?</li>
     *     <li>Is the field already occupied?</li>
     * </ol>
     *
     * @param player The player who intends to make the move
     * @param row    Selected row
     * @param col    Selected column
     * @throws IllegalAccessException         This exception will be thrown when:
     *                                        <ul>
     *                                        <li>Try to make a move but the game is not running.</li>
     *                                        <li>The player trying to make the move is not authorized.</li>
     *                                        <li>The field is busy</li>
     *                                        </ul>
     * @throws ArrayIndexOutOfBoundsException One of the selected values ​​(row or column) corresponds to a position outside the board.
     */
    public void validateMove(Player player, int row, int col) throws IllegalAccessException, ArrayIndexOutOfBoundsException {
        if (state != MatchState.RUN)
            throw new IllegalAccessException("This movement should not be happening! Match State: " + state);

        if (!player.equals(this.current))
            throw new IllegalAccessException("This player is not authorized to perform this action.");

        if (board.getFieldState(row, col) != FieldState.FREE)
            throw new IllegalAccessException("This field is not free.");
    }

    /**
     * Perform the player's movement
     *
     * @param row Selected row
     * @param col Selected column
     */
    private void executeMovement(int row, int col) {
        FieldState fState = FieldState.get(current.getSymbol());
        board.setFieldState(row, col, fState);
    }

    /**
     * Check if the current player is the winner
     *
     * @return If it is a winner it returns true, otherwise, it returns false
     */
    private boolean playerWins() {
        FieldState fState = FieldState.get(current.getSymbol());
        FieldState[][] field = board.getFields();
        for (int i = 0; i < 3; i++) {
            if (field[i][0] == fState && field[i][1] == fState && field[i][2] == fState) return true;
            if (field[0][i] == fState && field[1][i] == fState && field[2][i] == fState) return true;
        }
        if (field[0][0] == fState && field[1][1] == fState && field[2][2] == fState) return true;
        return field[0][2] == fState && field[1][1] == fState && field[2][0] == fState;
    }

    /**
     * Make the player's move
     *
     * <p>
     * <ol>
     *     <li>Checks whether the move is valid. If the play is not valid, throw an exception</li>
     *     <li>Make the move.</li>
     *     <li>Checks whether the move resulted in a winner or a draw.</li>
     *     <li>If there was no winner or tie, assigns the authorization to play to the other player.</li>
     * </ol>
     *
     * @param player The player who intends to make the move
     * @param row    Selected row
     * @param col    Selected column
     * @throws IllegalAccessException         This exception will be thrown when:
     *                                        <ul>
     *                                        <li>Try to make a move but the game is not running.</li>
     *                                        <li>The player trying to make the move is not authorized.</li>
     *                                        <li>The field is busy</li>
     *                                        </ul>
     * @throws ArrayIndexOutOfBoundsException One of the selected values ​​(row or column) corresponds to a position outside the board.
     */
    public void move(Player player, int row, int col)
            throws IllegalAccessException, ArrayIndexOutOfBoundsException {

        validateMove(player, row, col);
        executeMovement(row, col);

        // this move a revolt in the winner or a tie ???
        if (playerWins()) {  // Player win??
            state = player.getSymbol();
        } else if (board.isLocked()) { // Tied Players
            state = MatchState.LOCKED;
        }

        changeCurrentPlayer();
    }

    @Override
    public String toString() {
        return "Match:{" + "host:" + this.host.getUsername() + "," + "adversary:"
                + (this.adversary == null ? "Undefined" : this.adversary.getUsername()) + "," + "current:"
                + (this.current == null ? "Undefined" : this.adversary.getUsername()) + "," + "state:" + this.state
                + "}";
    }
}
