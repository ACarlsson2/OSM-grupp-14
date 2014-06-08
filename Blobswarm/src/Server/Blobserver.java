
package Server;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;

import Common.Blob;
import Common.InfoNPB;
import Common.NPBlob;
import Common.Network;
import Common.Network.Blobs;
import Common.Network.NPBlobs;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;

public class Blobserver {
	Server server;
	int FPS = 30;
	World world;
	

	public Blobserver () throws IOException {
		this.world = new World();
		world.setHeight(600);
		world.setWidth(1200);
		this.world.spawnNPB();
		server = new Server() {
			protected Connection newConnection () {
				// By providing our own connection implementation, we can store per
				// connection state without a connection ID to state look up.
				return new BlobConnection();
			}
		};

		// For consistency, the classes to be sent over the network are
		// registered by the same method for both the client and server.
		Network.register(server);

		server.addListener(new ClientHandler(world, server));
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
			ArrayList<Blob> blobs = new ArrayList<Blob>(connections.length);
			for (int i = 0; i < connections.length; i++) {
				BlobConnection connection = (BlobConnection)connections[i];
				if (connection.name == null) continue;
				blobs.add(connection.blob);
			}
					
			NPBlobs npbInfo = new NPBlobs();
			InfoNPB[] infoNPB = new InfoNPB[world.getNPB().size()];

			for (NPBlob npBlob : world.getNPB()) {
				infoNPB[world.getNPB().indexOf(npBlob)] = npBlob.getInfo();
			}
			npbInfo.blobs = infoNPB;
			server.sendToAllTCP(npbInfo);
			
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

	

	
	// This holds per connection state.
	public static class BlobConnection extends Connection {
		public String name;
		public Blob blob;
	}

	public static void main (String[] args) throws IOException {
		Log.set(Log.LEVEL_DEBUG);
		new Blobserver();
	}
}
