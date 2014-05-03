
package Blobswarm;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;


import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

// This class is a convenient place to keep things common to both the client and server.
public class Network {
	static public final int port = 54555;

	// This registers objects that are going to be sent over the network.
	static public void register (EndPoint endPoint) {
		Kryo kryo = endPoint.getKryo();
		kryo.register(RegisterName.class);
		kryo.register(String[].class);
		kryo.register(Blob.class);
		kryo.register(Blob[].class);
		kryo.register(Point.class);
		kryo.register(UpdateNames.class);
		kryo.register(ChatMessage.class);
		kryo.register(Blobs.class);
		kryo.register(ServerInputs.class);
		kryo.register(java.util.HashSet.class);
		kryo.register(Character.class);
	}

	static public class RegisterName {
		public String name;
	}

	static public class UpdateNames {
		public String[] names;
	}

	static public class ChatMessage {
		public String text;
	}
	static public class Blobs {
		public Blob[] blobs;
	}
	static public class ServerInputs {
		public Character input;
	}
}
