package server;

import common.GameInterface;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ServerRMI {

    public static void main(String[] args) {
        try {

            //Create and export a registry instance on localhost. Accepts requests on port 1099.
            Registry registry = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);

            //Game Server Instance
            ServerGame serverGame = new ServerGame();

            //Creates and exports a new UnicastRemoteObject object using the port 1099.
            GameInterface gameInterface = (GameInterface) UnicastRemoteObject.exportObject(serverGame, 0);

            //Link a remote reference to the name "game"
            registry.bind("game", serverGame);

            System.out.println("Server is ready");
        } catch (RemoteException | AlreadyBoundException e) {
            e.printStackTrace();
        }
    }
}
