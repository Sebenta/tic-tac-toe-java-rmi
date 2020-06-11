package cliente;

import javax.security.auth.login.LoginException;
import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class App {

    Game game = new Game();

    public static void main(String[] args) {
        App app = new App();
        try {

            // Initiate the game config
            app.init();

            // Registers a new virtual-machine shutdown hook.
            Util.shutdownHook(app);

            // Initializes the game itself
            app.run();

        } catch (Exception e) {
            System.out.println("Unexpected Exception: ");
            System.out.println(e.getCause() + " : " + e.getMessage());
        }
    }

    /**
     * <p>Connects to the server and allows you to register a new player.</p>
     *
     * <ol>
     *     <li>Connect server</li>
     *     <li>Register the player on the server</li>
     * </ol>
     * - Register Player
     */
    private void init() {
        Scanner input = new Scanner(System.in);
        Util.clearScreen();
        Util.showLogo();

        // Connect server
        do {
            connectServerGame();
            if (!game.isConnected()) {
                System.out.println("Do you want to try again? yes(y) to try again");
                String op = input.nextLine().toLowerCase();
                if (!(op.equals("yes") || op.equals("y"))) {
                    System.out.println("The game was forced to close. See you...");
                    System.exit(0);
                }
            }
        } while (!game.isConnected());

        // Register the player on the server
        do {
            System.out.print("Enter your username: ");
            String username = input.nextLine().trim();
            if (username.length() < 3) System.out.println("Invalid username. Min length is 3 characters.");
            else login(username);
        } while (!game.isLogged());

    }

    /**
     * <p>Initializes the game itself.</p>
     * <br>
     * Repeat the topics:
     * <ol>
     *     <li>Show the menu and read the selected player option.</li>
     *     <li>
     *         Run one of the options:
     *         <ol>
     *             <li>Join a match.</li>
     *             <li>List all players online.</li>
     *             <li>Leave the game.</li>
     *         </ol>
     *     </li>
     * </ol>
     */
    public void run() {
        while (true) {
            Scanner input = new Scanner(System.in);
            try {

                // Show the menu and read the selected player option
                Util.clearScreen();
                Util.showLogo(game.getPlayer().getUsername());
                Util.showMenu();
                int op = input.nextInt();

                switch (op) {

                    case 1: // Join a match
                        while (true) {
                            System.out.println("Wait for a match to start.");
                            // In this version, the player waits until the start of the game.
                            // This can cause problems as the game will be blocked for an uncertain time.
                            if (game.join()) {
                                playMatch();
                                break;
                            } else // This code snippet should be used when the logic is changed.
                            {
                                System.out.println("No matches found yet.");
                                System.out.print("Do you want to try again? yes(y) to try again: ");
                                String cont = input.nextLine().toLowerCase();
                                if (!(cont.equals("yes") || cont.equals("y"))) {
                                    break;
                                }
                            }
                        }
                        break;

                    case 2: // List all players online.
                        game.showPlayers();
                        Util.pressToContinue();
                        break;

                    case 0: // Leave the game.
                        game.logout();
                        System.out.println("See later! Have a nice day...");
                        System.exit(0);

                    default:
                        System.out.println("Invalid option...");
                        Util.pressToContinue();

                }
            } catch (InputMismatchException e) {
                System.out.println("Attention! Input values ​​are not valid.");
                Util.pressToContinue();
            }
        }
    }

    /**
     * It starts and manages the game.
     * <br>
     * Summary of the procedure used.
     * <ol>
     *     <li>Is the game running?</li>
     *     <li>It's my turn?</li>
     * </ol>
     *
     * @throws InterruptedException
     * @throws RemoteException
     */
    public void playMatch() {
        boolean showWellcome = true;

        // Is the game running?
        while (game.isInProgress()) {

            // Display arts
            Util.clearScreen();
            Util.showLogo(game.getPlayer().getUsername());

            if (showWellcome) {
                System.out.println("Start The Game. Good luck!");
                showWellcome = !showWellcome;
            }
            game.showBoard();

            //It's my turn?
            game.showCurrentPlayer();
            if (game.isMyTurn()){
                if(game.iCanPlay()) performMove();
            }
            else {
                game.waitMyTurn();
            }
        }

        Util.clearScreen();
        Util.showLogo(game.getPlayer().getUsername());
        if (game.haveWinner()) Util.showWinner(game.Winner().getUsername());
        else if (game.isLocked()) Util.showTied();
        Util.pressToContinue();
    }

    private void performMove() {
        Scanner input = new Scanner(System.in);
        int col, row;
        try {
            System.out.print("Row [1-3]: ");
            row = input.nextInt() - 1;
            System.out.print("Col [1-3]: ");
            col = input.nextInt() - 1;

            if (game.isValid(row, col)) {
                game.move(row, col);
            } else {
                System.out.println("This field is not free. Try again.");
                Util.pressToContinue();
            }
        } catch (NoSuchElementException e) {
            System.out.println("Invalid input values. Try again.");
            Util.pressToContinue();
        } catch (Exception e) {
            System.out.println("[ APP - Move ] : " + e.getMessage());
            Util.pressToContinue();
        }
    }

    private boolean connectServerGame() {
        try {
            game.connect();
            return game.isConnected();
        } catch (ConnectException e) {
            System.out.println(e.getMessage());
            return game.isConnected();
        }
    }

    private void login(String username) {
        try {
            game.login(username);
        } catch (LoginException e) {
            System.out.println(e.getMessage());
        }
    }

}
