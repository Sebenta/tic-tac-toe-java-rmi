package common;

import common.logic.Board;
import common.logic.Match;
import common.logic.Player;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface GameInterface extends Remote {

    /**
     * Register a new player on the server.
     * <p>The username must be unique, if there is already a play with
     * that username, an exception is thrown.
     *
     * @param username - The username you want to register
     * @return corresponding player object
     * @throws RemoteException          if failed to export object
     * @throws IllegalArgumentException this username already exists
     * @see Player
     */
    Player registerPlayer(String username) throws RemoteException, IllegalArgumentException;

    /**
     * Removes the player from the player list.
     *
     * @param player The player you want to remove from the player list
     * @throws RemoteException if failed to export object
     * @see Player
     */
    void logout(Player player) throws RemoteException;

    /**
     * Join in the match
     * <p>
     * Look for a match that is waiting for another player.
     * If there is no game waiting for another player, the player
     * will create his own game and wait for an opponent.
     *
     * @param player The player you want to add to a match or create a new match.
     * @return corresponding player object
     * @throws RemoteException          if failed to export object
     * @throws InterruptedException     if any thread has interrupted the current thread
     * @throws IllegalArgumentException this player already exists
     * @see Player
     * @see Match
     */
    Player join(Player player) throws RemoteException, InterruptedException, IllegalArgumentException;

    /**
     * The player waits for your turn to play
     *
     * @param player The Player you want to know when it's your turn.
     * @return corresponding player object
     * @throws RemoteException if failed to export object
     * @throws InterruptedException if any thread has interrupted the current thread
     */
    Player waitMyTurn(Player player) throws RemoteException, InterruptedException;

    /**
     * Register the player movement
     *
     * @param player The player who wants to perform the move.
     * @param row    Selected board line. Possible values: 1, 2 or 3.
     * @param col    Selected board column. Possible values: 1, 2 or 3.
     * @return Player
     * @throws RemoteException if failed to export object.
     * @throws ArrayIndexOutOfBoundsException One of the selected values ​​(row or column) corresponds to a position outside the board.
     * @throws IllegalAccessException The correspondent field is not free.
     * @see Board
     * @see Match
     */
    Player move(Player player, int row, int col)
            throws RemoteException, ArrayIndexOutOfBoundsException, IllegalAccessException;

    /**
     * Return the list of players who are online.
     *
     * @return Players {@code ArrayList<Player>}
     * @throws RemoteException if failed to export object.
     * @see ArrayList
     * @see Player
     */
    ArrayList<Player> getPlayers() throws RemoteException;

    /**
     * Return a player's current state
     *
     * @param player The player you want to know the current status
     * @return Returns an updated player
     * @throws RemoteException if failed to export object.
     */
    Player getPlayerInfo(Player player) throws RemoteException;
}
