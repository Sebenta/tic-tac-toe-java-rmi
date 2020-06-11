package cliente;

import java.util.Scanner;

public class Util {

    static void showLogo() {
        String title = "                __                                  ____                  ______            __       \n"
                + "               / /  ____    ____ _  ____           / __ \\  ____          / ____/  ____ _   / /  ____ \n"
                + "          __  / /  / __ \\  / __ `/ / __ \\         / / / / / __ \\        / / __   / __ `/  / /  / __ \\\n"
                + "         / /_/ /  / /_/ / / /_/ / / /_/ /        / /_/ / / /_/ /       / /_/ /  / /_/ /  / /  / /_/ /\n"
                + "         \\____/   \\____/  \\__, /  \\____/        /_____/  \\____/        \\____/   \\__,_/  /_/   \\____/\n"
                + "                         /____/                                                                      \n";
        System.out.println(title);
    }

    static void showLogo(String username) {
        String title = "                __                                  ____                  ______            __       \n"
                + "               / /  ____    ____ _  ____           / __ \\  ____          / ____/  ____ _   / /  ____ \n"
                + "          __  / /  / __ \\  / __ `/ / __ \\         / / / / / __ \\        / / __   / __ `/  / /  / __ \\\n"
                + "         / /_/ /  / /_/ / / /_/ / / /_/ /        / /_/ / / /_/ /       / /_/ /  / /_/ /  / /  / /_/ /\n"
                + "         \\____/   \\____/  \\__, /  \\____/        /_____/  \\____/        \\____/   \\__,_/  /_/   \\____/\n"
                + "                         /____/                                                                      \n"
                + "                                                                                                   " + username + "\n";
        System.out.println(title);
    }

    static void showWinner(String username) {
        String msg = "     _______________\n" +
                "    |@@@@|     |####|\n" +
                "    |@@@@|     |####|\n" +
                "    |@@@@|     |####|\n" +
                "    \\@@@@|     |####/\n" +
                "     \\@@@|     |###/" + " Congratulations " + username + "\n"+
                "      `@@|_____|##'" + "     You are the big winner!    \n" +
                "           (O)\n" +
                "        .-'''''-.\n" +
                "      .'  * * *  `.\n" +
                "     : *         * :\n" +
                "    :  W I N N E R :\n" +
                "     : *         *  :\n" +
                "      `.  * * *  .'\n" +
                "        `-.....-'\n";
        System.out.println(msg);
    }

    static void showTied() {
        String msg = "     _______________\n" +
                "    |@@@@|     |####|\n" +
                "    |@@@@|     |####|\n" +
                "    |@@@@|     |####|\n" +
                "    \\@@@@|     |####/\n" +
                "     \\@@@|     |###/" + "  Congratulations to both. \n" +
                "      `@@|_____|##'" + "     What a great game, it's fair to have ended in a draw.\n" +
                "           (O)\n" +
                "        .-'''''-.\n" +
                "      .'  * * *  `.\n" +
                "     : *         * :\n" +
                "    :    T I E D    :\n" +
                "     : *         *  :\n" +
                "      `.  * * *  .'\n" +
                "        `-.....-'\n";
        System.out.println(msg);
    }

    static void showMenu() {
        String menu = "Select one of the options below:\n\n"
                + "1 - Join in a party\n" + "2 - Show Players Online\n"
                + "0 - Leave the game\n"
                + "Select: ";
        System.out.print(menu);
    }

    static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    static void pressToContinue() {
        Scanner input = new Scanner(System.in);
        System.out.print("Press any key to cont...");
        input.nextLine();
    }

    static void shutdownHook(App app) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {
                    Thread.sleep(200);
                    app.game.logout();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                }
            }
        });
    }
}
