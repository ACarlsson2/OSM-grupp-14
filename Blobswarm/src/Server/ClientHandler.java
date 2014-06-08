package Server;

import java.util.ArrayList;

import Common.Blob;
import Common.NPBlob;
import Common.Network.ChatMessage;
import Common.Network.RegisterName;
import Common.Network.ServerInput;
import Common.Network.UpdateNames;
import Server.Blobserver.BlobConnection;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public class ClientHandler extends Listener {
	private Server server;
	private World world;
	private static int blobIDs = 0;
	
	public ClientHandler(World world, Server server){
		this.world = world;
		this.server = server;
	}
	
	public void received (Connection c, Object object) {
		// We know all connections for this server are actually BlobConnections.
		BlobConnection connection = (BlobConnection)c;

		if (object instanceof ServerInput) {
			if (connection.name == null) return;
			if (!connection.blob.getAlive()) return;
			ServerInput input = ((ServerInput)object);		
			if (input.up)
					world.attemptMove(connection.blob, 1);
			if (input.down)             // Do this if the input is the down button.
					world.attemptMove(connection.blob, 2);
			if (input.left)            // Do this if the input is the left button.
					world.attemptMove(connection.blob, 3);
			if (input.right)         // Do this if the input is the right button.
					world.attemptMove(connection.blob, 4);
									
			return;
			}
		
		
		if (object instanceof RegisterName) {
			// Ignore the object if a client has already registered a name. This is
			// impossible with our client, but a hacker could send messages at any time.
			if (connection.name != null) return;
			// Ignore the object if the name is invalid.
			String name = ((RegisterName)object).name;
			if (name == null) return;
			name = name.trim();
			if (name.length() == 0) return;
			// Store the name on the connection.
			connection.name = name;
			connection.blob = new Blob(connection.name);
			connection.blob.setID(blobIDs);
			world.spawnLocation(connection.blob);
			blobIDs += 1;
			// Send a "connected" message to everyone except the new client.
			ChatMessage chatMessage = new ChatMessage();
			chatMessage.text = name + " connected.";
			server.sendToAllExceptTCP(connection.getID(), chatMessage);
			// Send everyone a new list of connection names.
			updateNames();
			return;
		}

	}

	//TODO
	private void updateNames () {
		Connection[] connections = server.getConnections();
		int totalConnections = connections.length; //x
		ArrayList<String> names = new ArrayList<String>(totalConnections);//x
		world.setPlayers(new Blob[totalConnections]);//x	
		for (int i = connections.length - 1; i >= 0; i--) {
			BlobConnection connection = (BlobConnection)connections[i];
			names.add(connection.name);
			world.getPlayers()[i] = connection.blob;//x
		}
		// Send the names to everyone.
		UpdateNames updateNames = new UpdateNames();
		updateNames.names = (String[])names.toArray(new String[names.size()]);
		NPBlob.setPlayerBlobs(world.getPlayers()); //XXX update but if someone disconnect he is still there IMPORTANT TO NOT FORGET
		server.sendToAllTCP(updateNames);
	}
}
