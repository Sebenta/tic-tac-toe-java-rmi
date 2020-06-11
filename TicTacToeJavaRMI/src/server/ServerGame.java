package server;

import common.GameInterface;
import common.logic.Board;
import common.logic.Match;
import common.logic.MatchState;
import common.logic.Player;
import org.jetbrains.annotations.Nullable;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class ServerGame implements GameInterface {

    private final ArrayList<Player> players;
    private final ArrayList<Match> matches;

    public ServerGame() {
        players = new ArrayList<>();
        matches = new ArrayList<>();
    }

    /**
     * Check if a player with a given username already exists
     *
     * @param username Name to search
     * @return Return <b>{@code true}</b> if a player with the same username
     * already exists, otherwise <b>{@code false}</b>
     */
    private boolean alreadyExists(String username) {
        if (players.isEmpty())
            return false;
        return this.players.stream().map(Player::getUsername).anyMatch(username::equalsIgnoreCase);
    }

    /**
     * Search for a particular game in the list of registered players on the server.
     *
     * @param player The Player you want to search for.
     * @return If the intended player is found, it will be returned to you, otherwise, an exception is thrown.
     * @see Player
     */
    private @Nullable Player findPlayer(Player player) {
        if (players.isEmpty()) return null;
        try {
            return players.stream()
                    .filter(p -> p.getUsername().toLowerCase().equals(player.getUsername().toLowerCase())).findFirst()
                    .get();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Search for a match in standby mode.
     *
     * @return If found return to <b>match</b> found otherwise returns <b>{@code null}</b>.
     * @see Match
     */
    private @Nullable Match findMatch() {
        try {
            return matches.stream().filter(m -> m.getState() == MatchState.WAIT).findFirst().get();
        } catch (Exception e) {
            return null;
        }
    }

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
    @Override
    public Player registerPlayer(String username) throws RemoteException, IllegalArgumentException {
        if (alreadyExists(username))
            throw new IllegalArgumentException("this username already exists");
        Player player = new Player(username);
        this.players.add(player);
        return player;
    }

    /**
     * Removes the player from the player list.
     *
     * @param player The player you want to remove from the player list
     * @throws RemoteException if failed to export object
     * @see Player
     */
    @Override
    public void logout(Player player) throws RemoteException {
        Player p = findPlayer(player);
        players.remove(p);
    }

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
    @Override
    public Player join(Player player) throws RemoteException, InterruptedException, IllegalArgumentException {
        Match match = findMatch();
        Player p = findPlayer(player);

        if (p == null) throw new IllegalArgumentException("this player already exists");
        if (match == null) {
            match = p.createMatch();
            matches.add(match);
        } else {
            match.setAdversary(p);
            p.setMatch(match);
            match.setState(MatchState.RUN);
        }

        while (match.getState() != MatchState.RUN) Thread.sleep(1000);
        return p;
    }

    /**
     * The player waits for your turn to play
     *
     * @param player The Player you want to know when it's your turn.
     * @return corresponding player object
     * @throws RemoteException      if failed to export object
     * @throws InterruptedException if any thread has interrupted the current thread
     */
    @Override
    public Player waitMyTurn(Player player) throws RemoteException, InterruptedException {
        Player p = findPlayer(player);
        while (p != p.getMatch().getCurrent()) {
            Thread.sleep(1000);
        }
        return p;
    }

    /**
     * Register the player movement
     *
     * @param player The player who wants to perform the move.
     * @param row    Selected board line. Possible values: 1, 2 or 3.
     * @param col    Selected board column. Possible values: 1, 2 or 3.
     * @return Player
     * @throws RemoteException                if failed to export object.
     * @throws ArrayIndexOutOfBoundsException One of the selected values ​​(row or column) corresponds to a position outside the board.
     * @throws IllegalAccessException         The correspondent field is not free.
     * @see Board
     * @see Match
     */
    @Override
    public Player move(Player player, int row, int col) throws RemoteException, ArrayIndexOutOfBoundsException, IllegalAccessException {
        Player p = findPlayer(player);
        if (p != null)
            p.move(row, col);
        return getPlayerInfo(player);
    }

    /**
     * Return the list of players who are online.
     *
     * @return Players {@code ArrayList<Player>}
     * @throws RemoteException if failed to export object.
     * @see ArrayList
     * @see Player
     */
    @Override
    public ArrayList<Player> getPlayers() throws RemoteException {
        return players;
    }

    /**
     * Return a player's current state
     *
     * @param player The player you want to know the current status
     * @return Returns an updated player
     * @throws RemoteException if failed to export object.
     */
    @Override
    public Player getPlayerInfo(Player player) throws RemoteException {
        return findPlayer(player);
    }
}
