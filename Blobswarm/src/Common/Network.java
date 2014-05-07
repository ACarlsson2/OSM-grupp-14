
package Common;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

import Server.NPBlob;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

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
		kryo.register(ServerInput.class);
		kryo.register(java.util.HashSet.class);
		kryo.register(Character.class);
		kryo.register(NPBlobs.class);
		kryo.register(NPBlob.class);
		
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
	
	static public class NPBlobs{
		public List npblobs;
	}
	
	static public class ServerInput {
		public Character input;
	}
}
