package Client;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import Common.Blob;
import Common.InfoNPB;
import Common.Network;
import Common.Network.Blobs;
import Common.Network.NPBlobs;
import Common.Network.RegisterName;
import Common.Network.ServerInput;
import Common.Network.UpdateNames;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;

public class Blobclient implements KeyListener {
	// Fields
	ChatFrame chatFrame;
	Client client;
	String name;
	ArrayList<Integer> existingBlobIDs = new ArrayList<Integer>();
	ArrayList<BlobView> blobViews = new ArrayList<BlobView>();
	ArrayList<NPBlobView> NPBlobViews = new ArrayList<NPBlobView>();
	ServerInput output = new ServerInput();

	// Constructor
	public Blobclient() {
		client = new Client();
		client.start();

		// For consistency, the classes to be sent over the network are
		// registered by the same method for both the client and server.
		Network.register(client);

		client.addListener(new Listener() {
			public void connected(Connection connection) {
				RegisterName registerName = new RegisterName();
				registerName.name = name;
				client.sendTCP(registerName);
			}

			public void received(Connection connection, Object object) {
				if (object instanceof UpdateNames) {
					UpdateNames updateNames = (UpdateNames) object;
					chatFrame.setNames(updateNames.names);
					return;
				}

				if (object instanceof Blobs) {
					Blobs blobArray = (Blobs) object;
					ArrayList<Blob> blobinfo = new ArrayList<Blob>(
							blobArray.blobs.length);

					for (int i = 0; i < blobArray.blobs.length; i++) {
						blobinfo.add(blobArray.blobs[i]);
					}

					if (blobinfo.size() > 0) {
						chatFrame.animate();
						checkNewBlobs(blobinfo);
						removeDeadBlobs(blobinfo);
						updateBlobs(blobinfo);
						
						client.sendTCP(output);
					}
				}
				
				if (object instanceof NPBlobs) {
					NPBlobs npblobs = (NPBlobs) object;
					ArrayList<InfoNPB> NPBArray = new ArrayList<InfoNPB>(
							npblobs.blobs.length);					
					for (int i = 0; i < npblobs.blobs.length; i++) {
						NPBArray.add(npblobs.blobs[i]);
					}
					while(NPBlobViews.size() < NPBArray.size()){ // Not enough NPBlobViews
						NPBlobView newBV = new NPBlobView();
						NPBlobViews.add(newBV);
						chatFrame.getPanel().add(newBV.getJComponent());
					    chatFrame.getPanel().setComponentZOrder(newBV.getJComponent(), 0);
					}
					while(NPBlobViews.size() > NPBArray.size()){ // Too many NPBlobViews
						NPBlobViews.remove(NPBlobViews.get(NPBlobViews.size()-1));
						chatFrame.getPanel().remove(NPBlobViews.get(NPBlobViews.size()-1).getJComponent());
					}
					
					chatFrame.getPanel().repaint();
					updateNPBs(NPBArray);
				}
			}

			private void updateNPBs(ArrayList<InfoNPB> NPBArray) {
				for(InfoNPB npb : NPBArray){
					if(npb != null){
					 NPBlobView newBV = NPBlobViews.get(NPBArray.indexOf(npb));
				     newBV.set(npb.getPosition(),npb.getDirection());
					}
				}
			}

			private void updateBlobs(ArrayList<Blob> blobinfo) {
				for (int i = 0; i < blobinfo.size(); i++) {
					BlobView currBlob = findBlobView(blobinfo,(blobinfo.get(i).getID()));
					currBlob.setAlive(blobinfo.get(i).getAlive());
						currBlob.update(blobinfo.get(i).getPosition(),	
							blobinfo.get(i).getDirection());
					
				}
			}

			private void checkNewBlobs(ArrayList<Blob> blobinfo) {
				for (int i = 0; i < blobinfo.size(); i++) {
					if (!existingBlobIDs.contains(blobinfo.get(i))) {
						existingBlobIDs.add((blobinfo.get(i)).getID());
						BlobView newBlob = new BlobView(blobinfo.get(i).getID(),blobinfo.get(i).getName());
						blobViews.add(newBlob);
						chatFrame.getPanel().add(
								newBlob.getJComponent());
						chatFrame.getPanel().add(
								newBlob.getNameLabel());
						chatFrame.getPanel().setComponentZOrder(
								newBlob.getJComponent(), 0);
						chatFrame.getPanel().setComponentZOrder(
								newBlob.getNameLabel(), 0);
						
					}
				}
			}
			
			private void removeDeadBlobs(ArrayList<Blob> blobinfo){
				for(BlobView blobPointer : blobViews)
				{
					if(findBlob(blobinfo,blobPointer.getID()) == null)
					{
						chatFrame.getPanel().remove(blobPointer.getJComponent());
						chatFrame.getPanel().remove(blobPointer.getNameLabel());
						chatFrame.getPanel().repaint();
					}
				}
			}

			private BlobView findBlobView(ArrayList<Blob> blobinfo, int blobID){
				for(BlobView blobPointer : blobViews)
				{
					if(blobPointer.getID() == blobID){
						return blobPointer;
					}
				}
				return null;
			}
			
			private Blob findBlob(ArrayList<Blob> blobinfo, int blobID){
				for(Blob blobPointer : blobinfo)
				{
					if(blobPointer.getID() == blobID){
						return blobPointer;
					}
				}
				return null;
			}
			
			
			public void disconnected(Connection connection) {
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						// Closing the frame calls the close listener which will
						// stop the client's update thread.
						chatFrame.dispose();
					}
				});
			}
		});

		// Request the host from the user.
		String input = (String) JOptionPane.showInputDialog(null, "Host:",
				"Connect to Blobserver", JOptionPane.QUESTION_MESSAGE, null,
				null, "localhost");
		if (input == null || input.trim().length() == 0)
			System.exit(1);
		final String host = input.trim();

		// Request the user's name.
		input = (String) JOptionPane.showInputDialog(null, "Name:",
				"Connect to Blobserver", JOptionPane.QUESTION_MESSAGE, null,
				null, "Test");
		if (input == null || input.trim().length() == 0)
			System.exit(1);
		name = input.trim();

		// All the ugly Swing stuff is hidden in ChatFrame so it doesn't clutter
		// the KryoNet example code.
		chatFrame = new ChatFrame(host);

		chatFrame.addKeyListener(this);

		// This listener is called when the chat window is closed.
		chatFrame.setCloseListener(new Runnable() {
			public void run() {
				client.stop();
			}
		});
		chatFrame.setVisible(true);

		// We'll do the connect on a new thread so the ChatFrame can show a
		// progress bar.
		// Connecting to localhost is usually so fast you won't see the progress
		// bar.
		new Thread("Connect") {
			public void run() {
				try {
					client.connect(5000, host, Network.port);
					// Server communication after connection can go here, or in
					// Listener#connected().
				} catch (IOException ex) {
					ex.printStackTrace();
					System.exit(1);
				}
			}
		}.start();

	}
	
	//Methods
	public void keyPressed(KeyEvent e) {
		if(e.getKeyChar()=='w')output.up = true;
		if(e.getKeyChar()=='s')output.down = true;
		if(e.getKeyChar()=='a')output.left = true;
		if(e.getKeyChar()=='d')output.right = true;
	}

	public void keyTyped(KeyEvent e) {

	}

	public void keyReleased(KeyEvent e) {
		if(e.getKeyChar()=='w')output.up = false;
		if(e.getKeyChar()=='s')output.down = false;
		if(e.getKeyChar()=='a')output.left = false;
		if(e.getKeyChar()=='d')output.right = false;
	}

	static private class ChatFrame extends JFrame {
		CardLayout cardLayout;
		JProgressBar progressBar;
		JPanel thePanel;
		JLabel backgroundLabel;
		ImageIcon[] BGimages;
		int BGimage = 0;
		int animateQueue = 1;

		public ChatFrame(String host) {
			super("Blobswarm");
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setSize(1200, 600);
			setLocationRelativeTo(null);

			Container contentPane = getContentPane();
			cardLayout = new CardLayout();
			contentPane.setLayout(cardLayout);
			{
				JPanel panel = new JPanel(new BorderLayout());
				contentPane.add(panel, "progress");
				panel.add(new JLabel("Connecting to " + host + "..."));
				{
					panel.add(progressBar = new JProgressBar(),
							BorderLayout.SOUTH);
					progressBar.setIndeterminate(true);
				}
			}
			{
				JPanel panel = new JPanel(new BorderLayout());
				thePanel = panel;
				BGimages = new ImageIcon[3];
				JLabel background = new JLabel("");
				backgroundLabel = background;
				background.setBackground(Color.WHITE);
				background.setOpaque(true);
				background.setBorder(BorderFactory
						.createLineBorder(Color.BLACK));
				background.setBounds(0, 0, 600, 600);
				try {
					BGimages[0] = new ImageIcon(ImageIO.read(new File("Background0.png")));
					BGimages[1] = new ImageIcon(ImageIO.read(new File("Background1.png")));
					BGimages[2] = new ImageIcon(ImageIO.read(new File("Background2.png")));
					background.setIcon(BGimages[BGimage]);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				panel.add(background);
				contentPane.add(panel, "blob");

			}
		}

		public void setCloseListener(final Runnable listener) {
			addWindowListener(new WindowAdapter() {
				public void windowClosed(WindowEvent evt) {
					listener.run();
				}

			});
		}

		public JPanel getPanel() {
			return thePanel;
		}

		public void animate(){
			   if(animateQueue > (BGimages.length)*10) {
				   animateQueue = 1;
			   }
			   if(animateQueue % 10 == 0){
			    backgroundLabel.setIcon(BGimages[(animateQueue / 10)-1]);
			   }
			    getPanel().repaint();
			    animateQueue++;   
			  }
		
		public void setNames(final String[] names) {
			// This listener is run on the client's update thread, which was
			// started by client.start().
			// We must be careful to only interact with Swing components on the
			// Swing event thread.
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					cardLayout.show(getContentPane(), "blob");

				}
			});

		}
	}

	public static void main(String[] args) {
		Log.set(Log.LEVEL_DEBUG);
		new Blobclient();
	}
}
