package ass3.mygame2;


import java.util.ArrayList;

public class RoomCreation {

    private ArrayList<Room> allRoomInGame = new ArrayList();

    private ItemCreation itemCreation;

    public RoomCreation() {
        itemCreation = new ItemCreation();
        createRooms();
    }
    
    
    private void createRooms() {

        Room castle, kitchen, frontGate, frontyard;

        castle = new Room("castle", "You are at the castle", false);
        kitchen = new Room("kitchen", "The kitchen door has a shape of a heart", true);
        frontGate = new Room("frontGate", "You are in the front gate", true);
        frontyard = new Room("frontYard", "You are in the front yard, There is a giant ogre. use Sword to kill him.", true);
        
        
        castle.setExit("east", kitchen);
        kitchen.setExit("west", castle);   // kitchen se bahr aane k door 
        
        castle.setExit("south", frontGate);
        frontGate.setExit("north", castle);
        
        frontGate.setExit("west", frontyard);
        frontyard.setExit("east", frontGate);
         
//        kitchen.setLockedStatus(true);
//        frontGate.setLockedStatus(true);
        
        castle.addItemInRoom(itemCreation.getItem("excaliburSword"));
        castle.addItemInRoom(itemCreation.getItem("key"));
        kitchen.addItemInRoom(itemCreation.getItem("frontGateKey"));

        allRoomInGame.add(castle);
        allRoomInGame.add(frontGate);
        allRoomInGame.add(kitchen);

    }

    public Room getRoom(String stringRoom) {
        Room roomToReturn = null;
        for (Room room : allRoomInGame) {
            if (room.getName().contains(stringRoom)) {
                roomToReturn = room;
            }
        }
        return roomToReturn;
    }

}
