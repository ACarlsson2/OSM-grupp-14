
package Server;

import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JLabel;

import Common.Blob;
import Common.Network;
import Common.Network.Blobs;
import Common.Network.ChatMessage;
import Common.Network.RegisterName;
import Common.Network.ServerInput;
import Common.Network.UpdateNames;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;

public class Blobserver {
	Server server;
	int blobIDs = 0;
	int FPS = 30;
	

	public Blobserver () throws IOException {
		server = new Server() {
			protected Connection newConnection () {
				// By providing our own connection implementation, we can store per
				// connection state without a connection ID to state look up.
				return new ChatConnection();
			}
		};

		// For consistency, the classes to be sent over the network are
		// registered by the same method for both the client and server.
		Network.register(server);

		server.addListener(new Listener() {
			public void received (Connection c, Object object) {
				// We know all connections for this server are actually ChatConnections.
				ChatConnection connection = (ChatConnection)c;

				if (object instanceof ServerInput) {
					if (connection.name == null) return;
					Character input = ((ServerInput)object).input;		
						switch(input){
						case 'w':				// Do this if the input is the up button.
							connection.blob.move(1);
							break;
						case 's':               // Do this if the input is the down button.
							connection.blob.move(2);
							break;
						case 'a':               // Do this if the input is the left button.
							connection.blob.move(3);
							break;
						case 'd':               // Do this if the input is the right button.
							connection.blob.move(4);
							break;
						}					
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
					connection.blob = new Blob();
					connection.blob.setId(blobIDs);
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

		});
		server.bind(Network.port);
		server.start();

		// Open a window to provide an easy way to stop the server.
		JFrame frame = new JFrame("Blobserver");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosed (WindowEvent evt) {
				server.stop();
			}
		});
		frame.getContentPane().add(new JLabel("Close to stop the Blobserver."));
		frame.setSize(320, 200);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		while(true){
			
			Connection[] connections = server.getConnections();
			ArrayList blobs = new ArrayList(connections.length);
			for (int i = 0; i < connections.length; i++) {
				ChatConnection connection = (ChatConnection)connections[i];
				if (connection.name == null) continue;
				blobs.add(connection.blob);
			}
			Blobs blobArray = new Blobs();
			blobArray.blobs = (Blob[])blobs.toArray(new Blob[blobs.size()]);
			server.sendToAllTCP(blobArray);
			try {
				Thread.sleep(1000/FPS);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	void updateNames () {
		// Collect the names for each connection.
		Connection[] connections = server.getConnections();
		ArrayList names = new ArrayList(connections.length);
		for (int i = connections.length - 1; i >= 0; i--) {
			ChatConnection connection = (ChatConnection)connections[i];
			names.add(connection.name);
		}
		// Send the names to everyone.
		UpdateNames updateNames = new UpdateNames();
		updateNames.names = (String[])names.toArray(new String[names.size()]);
		server.sendToAllTCP(updateNames);
	}

	// This holds per connection state.
	public static class ChatConnection extends Connection {
		public String name;
		public Blob blob;
	}

	public static void main (String[] args) throws IOException {
		Log.set(Log.LEVEL_DEBUG);
		new Blobserver();
	}
}
