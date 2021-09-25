/*
 * To change this license header, choose License Headers in Project
Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ass3.mygame2;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 *
 *
 *
 */
public class Game {

    private Parser parser;
    private Player player;
    private Room currentRoom;
    private RoomCreation rooms;

    private HashMap<Item, Room> roomItem;

    private HashMap<Item, Room> roomKey;

    private int timeCounter; // to count the steps
    long timeStart;
    long currentTime;

    /**
     * Create the game and initialise its internal map.
     */
    public Game() {
        timeStart = System.currentTimeMillis(); // use the real time
        timeCounter = 50;
        parser = new Parser();
        player = new Player();
        rooms = new RoomCreation();
        currentRoom = rooms.getRoom("livingRoom");  // start game outside
        //System.out.println(createRoom.getcurrentRoom().getName());
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public String convertTime(long time) {
        Date date = new Date(time);
        Format format = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
        return format.format(date);
    }

    /**
     * Main play routine. Loops until end of play.
     */
    public void play() {
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
        boolean finished = false;
        while (!finished) {
            currentTime = System.currentTimeMillis();
            Command command = parser.getCommand();
            // count the delta (currentTome - startTime)            
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome() {
        System.out.println();
        System.out.println("World of Zuul is an adventure game including player, monster,rooms and items.");
        System.out.println("The player will begin in a room and need to find a way to beat the monster in order to win the game.");
        System.out.println("If you need help, type 'help', you will get some hints!");
        System.out.println("Game is now starting! Good Luck!");
        System.out.println(" Time Start: " + convertTime(timeStart));
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
    }

    /**
     * Print Message for winning
     */
    private void printWinMessage() {
        System.out.println();
        //System.out.println("******************************************************");
        System.out.println("*****************You Won the Game ************************");
        System.out.println("*****************Gongratulations! ********************");
        System.out.println("******************************************************");
        System.out.println(" Time Start: " + convertTime(timeStart));
        System.out.println();
        System.exit(0);
    }

    /**
     * Given a command, process (that is: execute) the command.
     *
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    public boolean processCommand(Command command) {

        boolean wantToQuit = false;
        if (command.isUnknown()) {
            System.out.println("*****************************************");
            System.out.println("I don't get what you mean... type 'help' !");
            System.out.println("*****************************************");
            return false;
        }
        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) {
            printHelp();
        } else if (commandWord.equals("inventory")) {
            printInventory(); // printVeggies
        } else if (commandWord.equals("go")) {
            goRoom(command);
        } else if (commandWord.equals("take")) {
            takeItem(command);
        } else if (commandWord.equals("drop")) {
            dropItem(command);
        } else if (commandWord.equals("use")) {
            useItem(command);
        } else if (commandWord.equals("inspect")) {
        lookItem(command);

        } else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
        } else {
            System.out.println("*****************************************");
            System.out.println("Unable to rescognise command");
            System.out.println("*****************************************");
        }

        return wantToQuit;
    }

    /**
     * Print out some help information. Here we print some stupid, cryptic
     * message and a list of the command words.
     */
    private void printHelp() {
        System.out.println("");

        // implement random Hints -> massive bonus points 
        System.out.println("*****************************************");
        System.out.println("Your command words are:");
        parser.showCommands();
        System.out.println("Each room may contains useful items,remember to take it if there are!");
        System.out.println("You need to find the matching key to open the door");
        System.out.println("You need to find the weapon to beat the giant ogre");
        System.out.println("*****************************************");
    }

    private void printInventory() {
        System.out.println(player.printAllInventory());
    }

    /**
     * Try to in to one direction. If there is an exit, enter the new room,
     * otherwise print an error message.
     */
    private void goRoom(Command command) {
        if (!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("*****************************************");
            System.out.println("There is no door!");
            System.out.println("*****************************************");
        } else {
            if (currentRoom.getLockedStatus() == true) { // the door is locked
                System.out.println("*****************************************");
                System.out.println("The door is locked, you need to find a way to open it");

                System.out.println(currentRoom.getLongDescription());
                System.out.println("*****************************************");
            } else {
                currentRoom = nextRoom;
                System.out.println("*****************************************");
                System.out.println(currentRoom.getLongDescription());
                System.out.println("*****************************************");
                //System.out.println(currentRoom.printAllRoomItems());
                // increment the timeCounter
            }
        }
    }

    private void takeItem(Command command) {
        if (!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Take what?");
            return;
        }

        String itemFromCommand = command.getSecondWord();
        Item currentItem = currentRoom.getRoomItem(itemFromCommand);
        //getPlayerItem(itemFromCommand);

        if (currentItem == null) {
            System.out.println("*****************************************");
            System.out.println("You can't take nothing, no?");
            System.out.println("*****************************************");
        } else {
            // Do the transaction here
            currentRoom.removeItemInRoom(currentItem);
            player.addItemInventory(currentItem);

            //roomItem.remove(currentItem);
            //addItemInventory(currentItem);
            //System.out.println(currentRoom.getLongDescription());
        }
    }

    private void dropItem(Command command) {
        if (!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Drop what?");
            return;
        }

        String itemFromCommand = command.getSecondWord();
        Item currentItem = player.getPlayerItem(itemFromCommand);
        //getPlayerItem(itemFromCommand);

        if (currentItem == null) {
            System.out.println("*****************************************");
            System.out.println("You can't Drop nothing, no?");
            System.out.println("*****************************************");
        } else {
            // Do the transaction here
            player.removeItemInventory(currentItem);
            currentRoom.addItemInRoom(currentItem);

            //removeItemInventory(currentItem);
            //roomItem.put(currentItem, currentRoom);
            //System.out.println(currentRoom.getLongDescription());
        }
    }

    private void useItem(Command command) // use key
    {
        if (!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Use what?");
            return;
        }

        String itemFromCommand = command.getSecondWord();
//        Item currentItem = currentRoom.getRoomItem(itemFromCommand);
        Item currentItem = player.getPlayerItem(itemFromCommand);

        if (currentItem == null) {
            System.out.println("*****************************************");
            System.out.println("You can't use nothing, no?");
            System.out.println("*****************************************");
        } else {
            // you want make sure that the currentRoom is the room where you want to open the door (before the nextdoor).
            // you want to make sure the currentItem matches the key to open the next door.

            if (currentRoom.getName().equals("kitchen") && currentItem.getName().equals("key")) {
                currentRoom.setLockedStatus(false);
                System.out.println("*****************************************");
                System.out.println("You just used the " + currentItem.getName() + " to open the door!");
                System.out.println("*****************************************");
            } else if (currentRoom.getName().equals("frontYard") && currentItem.getName().equals("excaliburSword")) {
//            Command command1= new Command("quit",null);
                System.out.println("*****************************************");
                System.out.println("You just used the " + currentItem.getName() + " to fight with the giant ogre.");
                System.out.println("You killed the giant ogre!");
                //System.out.println("*****************************************");
                printWinMessage();
            } else if (currentRoom.getName().equals("frontGate") && currentItem.getName().equals("frontGateKey")) {
                currentRoom.setLockedStatus(false);
                System.out.println("*****************************************");
                System.out.println("You just used the " + currentItem.getName());
                System.out.println("You Open the Gate!");
                System.out.println("OutSide the gate there is a giant ogre, you need to kill it to escape.");
                System.out.println("*****************************************");

// if(currentRoom.getName().equals("castle")){
                // //currentRoom.checkRoom("castle");
                // roomKey.get(currentItem).setLockedStatus(false);
            } else {
                System.out.println("*****************************************");
                System.out.println("You cannot use this item here" + currentRoom.getName());
                System.out.println("*****************************************");
            }
              System.out.println("*****************************************");
            System.out.println(currentRoom.getLongDescription());
              System.out.println("*****************************************");

        }

    }
 private void lookItem(Command command) {
        if (command.hasSecondWord()) {
            System.out.println("Inspect what? just type inspect to see items in the room");

        } else {
             System.out.println("*****************************************");
            System.out.println(currentRoom.getLongDescription());
              System.out.println("*****************************************");
        }
    }
    /**
     * "Quit" was entered. Check the rest of the command to see whether we
     * really quit the game.
     *
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) {
        if (command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        } else {
            return true;  // signal that we want to quit
        }
    }

}
