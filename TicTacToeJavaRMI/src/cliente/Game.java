package cliente;

import common.GameInterface;
import common.logic.Board;
import common.logic.FieldState;
import common.logic.MatchState;
import common.logic.Player;

import javax.security.auth.login.LoginException;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Game {

    private Registry registry;
    private GameInterface game;
    private Player player;
    private boolean connected;

    public Game() {
        this.connected = false;
    }

    public Player getPlayer() { return player; }

    public boolean isConnected() {
        return connected;
    }

    public boolean isLogged() {
        return player != null;
    }

    public boolean isInProgress() { return player.getMatch().getState() == MatchState.RUN; }

    public boolean isMyTurn(){ return player.getMatch().getCurrent() == player; }

    public boolean isValid(int row, int col)
    {
        if (!isMyTurn()) return false;
        if (row >= 3 || col >= 3) return false;
        System.out.println("é menos que 3");
        return player.getMatch().getBoard().getFieldState(row, col) == FieldState.FREE;
    }

    public boolean iCanPlay() {
        return !(haveWinner() || isLocked());
    }

    public boolean haveWinner() {
        MatchState mState = player.getMatch().getState();
        return mState == MatchState.O || mState == MatchState.X;
    }

    public boolean isLocked() {
        MatchState mState = player.getMatch().getState();
        return mState == MatchState.LOCKED;
    }

    public Player Winner() {
        if (haveWinner()) {
            MatchState mState = player.getMatch().getState();

            if (player.getMatch().getHost().getSymbol() == mState)
                return player.getMatch().getHost();

            if (player.getMatch().getAdversary().getSymbol() == mState)
                return player.getMatch().getAdversary();
        }
        return null;
    }

    public void connect() throws ConnectException {
        try {
            this.registry = LocateRegistry.getRegistry("localhost", Registry.REGISTRY_PORT);
            this.game = (common.GameInterface) registry.lookup("game");
            this.connected = true;
        } catch (NotBoundException | RemoteException e) {
            throw new ConnectException(
                    "Problems connecting to the server. " + "The server or service may be offline or may not exist.");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public void login(String username) throws LoginException {
        try {
            player = game.registerPlayer(username);
        } catch (RemoteException | IllegalArgumentException e) {
            throw new LoginException(e.getMessage());
        }
    }

    public void logout() {
        try {
            game.logout(player);
        } catch (RemoteException e) {
            System.err.println(e.getMessage());
        }
    }

    public boolean join() {
        try {
            player = game.join(player);
            if (player == null) return false;
            return true;
        } catch (RemoteException | InterruptedException | IllegalArgumentException e) {
            System.out.println("Server error. It was impossible to find a match.");
            return false;
        }
    }

    public void waitMyTurn(){
        try {
            this.player = game.waitMyTurn(player);
        } catch (RemoteException | InterruptedException e) {
            System.out.println("[ Game - WaitMyTurn ] : " + e.getMessage());
        }
    }

    private void updateInfo() {
        try {
            player = game.getPlayerInfo(player);
        } catch (RemoteException e) {
            System.err.println(e.getMessage());
        }
    }

    public void move(int row, int col) {
        try {
            player = game.move(player, row, col);
        } catch (RemoteException | ArrayIndexOutOfBoundsException | IllegalAccessException e) {
            System.err.println(e.getMessage());
        }
    }

    public void showPlayers() {
        try {
            System.out.println("\nPlayers Online:\n");
            game.getPlayers().forEach(p -> System.out.println(p.toBeautifyString()));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void showCurrentPlayer() {
        Player p = player.getMatch().getCurrent();
        System.out.println("Is the turn of: " + p.getUsername() + "( " + p.getSymbol() + " )");
    }

    public void showBoard() {
        FieldState[][] board = player.getMatch().getBoard().getFields();
        for (int row = 0; row < 3; row++) {
            System.out.print(" | ");
            for (int col = 0; col < 3; col++) {
                System.out.print(board[row][col] == FieldState.FREE ? "_" : board[row][col]);
                System.out.print(" | ");
            }
            System.out.println();
        }
    }


}
