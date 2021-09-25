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

        Room livingRoom, kitchen, frontGate, frontYard;

        livingRoom = new Room("livingRoom", "in the living room", false);
        kitchen = new Room("kitchen", "in the kitchen", true);
        frontGate = new Room("frontGate", "in the front gate", true);
        frontYard = new Room("frontYard", "in the front yard, there is a giant ogre, use the sword to kill him", true);
        
        
        livingRoom.setExit("east", kitchen);
        kitchen.setExit("west", livingRoom);   //kitchen is in the east of the castle
        
        livingRoom.setExit("south", frontGate);
        frontGate.setExit("north", livingRoom);
        
        frontGate.setExit("west", frontYard);
        frontYard.setExit("east", frontGate);
         
//        kitchen.setLockedStatus(true);
//        frontGate.setLockedStatus(true);
        
        livingRoom.addItemInRoom(itemCreation.getItem("excaliburSword"));
        livingRoom.addItemInRoom(itemCreation.getItem("key"));
        kitchen.addItemInRoom(itemCreation.getItem("frontGateKey"));

        allRoomInGame.add(livingRoom);
        allRoomInGame.add(frontGate);
        allRoomInGame.add(kitchen);
        allRoomInGame.add(frontYard);

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
