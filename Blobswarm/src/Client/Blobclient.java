
package Client;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import Common.Blob;
import Common.Network;
import Common.Network.Blobs;
import Common.Network.ChatMessage;
import Common.Network.RegisterName;
import Common.Network.ServerInput;
import Common.Network.UpdateNames;
import Server.Blobserver.ChatConnection;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;

public class Blobclient implements KeyListener {
	ChatFrame chatFrame;
	Client client;
	String name;
	ArrayList<Integer> existingBlobIDs = new ArrayList();
	ArrayList<BlobView> blobViews = new ArrayList();
		
	public Blobclient () {
		client = new Client();
		client.start();
		
		// For consistency, the classes to be sent over the network are
		// registered by the same method for both the client and server.
		Network.register(client);

		client.addListener(new Listener() {
			public void connected (Connection connection) {
				RegisterName registerName = new RegisterName();
				registerName.name = name;
				client.sendTCP(registerName);
			}

			public void received (Connection connection, Object object) {
				if (object instanceof UpdateNames) {
					UpdateNames updateNames = (UpdateNames)object;
					chatFrame.setNames(updateNames.names);
					return;
				}

				if (object instanceof Blobs) {
					Blobs blobArray = (Blobs)object;
					ArrayList<Blob> blobinfo = new ArrayList(blobArray.blobs.length);
					
					for (int i = blobArray.blobs.length - 1; i >= 0; i--) {
						blobinfo.add(blobArray.blobs[i]);
					}
					
					if(blobinfo.size() > 0){
						
					for(int i = blobinfo.size() - 1; i >= 0; i--)
					{
						if(!existingBlobIDs.contains(blobinfo.get(i)))
						{
						existingBlobIDs.add(( blobinfo.get(i)).getId());
						BlobView newBlob = new BlobView();
						blobViews.add(newBlob);
						chatFrame.getPanel().add(newBlob.getJComponent());
						chatFrame.getPanel().setComponentZOrder(newBlob.getJComponent(), 0);
						}
					}
					
					for(int i = 0; i < existingBlobIDs.size(); i++)
					{
						boolean found = false;
						for(int a = 0; a < blobinfo.size(); a++)
						{
							if (blobinfo.get(a).getId() == existingBlobIDs.get(i)) 
								{
								found = true;
								}
						}
						if(!found) 
							{
							chatFrame.getPanel().remove(((BlobView) blobViews.get(i)).getJComponent());
							}
					}
					
					for(int i = blobinfo.size() - 1; i >= 0; i--)
					{					
						BlobView currBall = (BlobView)blobViews.get(i);
						currBall.update(blobinfo.get(i).getPosition(), blobinfo.get(i).getDirection());
						
					}
					}
					
				}
			}

			public void disconnected (Connection connection) {
				EventQueue.invokeLater(new Runnable() {
					public void run () {
						// Closing the frame calls the close listener which will stop the client's update thread.
						chatFrame.dispose();
					}
				});
			}
		});

		
		
		// Request the host from the user.
		String input = (String)JOptionPane.showInputDialog(null, "Host:", "Connect to Blobserver", JOptionPane.QUESTION_MESSAGE,
			null, null, "localhost");
		if (input == null || input.trim().length() == 0) System.exit(1);
		final String host = input.trim();

		// Request the user's name.
		input = (String)JOptionPane.showInputDialog(null, "Name:", "Connect to Blobserver", JOptionPane.QUESTION_MESSAGE, null,
			null, "Test");
		if (input == null || input.trim().length() == 0) System.exit(1);
		name = input.trim();

		// All the ugly Swing stuff is hidden in ChatFrame so it doesn't clutter the KryoNet example code.
		chatFrame = new ChatFrame(host);

		chatFrame.addKeyListener(this);
		
		// This listener is called when the chat window is closed.
		chatFrame.setCloseListener(new Runnable() {
			public void run () {
				client.stop();
			}
		});
		chatFrame.setVisible(true);

		// We'll do the connect on a new thread so the ChatFrame can show a progress bar.
		// Connecting to localhost is usually so fast you won't see the progress bar.
		new Thread("Connect") {
			public void run () {
				try {
					client.connect(5000, host, Network.port);
					// Server communication after connection can go here, or in Listener#connected().
				} catch (IOException ex) {
					ex.printStackTrace();
					System.exit(1);
				}
			}
		}.start();
		
	}

	public void keyPressed(KeyEvent e) { 
		ServerInput key = new ServerInput();
		key.input = e.getKeyChar();
		client.sendTCP(key);
		
	}

    public void keyTyped(KeyEvent e) {
    	
    }

    public void keyReleased(KeyEvent e) {
    }
	
	static private class ChatFrame extends JFrame {
		CardLayout cardLayout;
		JProgressBar progressBar;
		JPanel thePanel;

		public ChatFrame (String host) {
			super("Blobswarm");
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setSize(600, 600);
			setLocationRelativeTo(null);
			
			
			Container contentPane = getContentPane();
			cardLayout = new CardLayout();
			contentPane.setLayout(cardLayout);
			{
				JPanel panel = new JPanel(new BorderLayout());
				contentPane.add(panel, "progress");
				panel.add(new JLabel("Connecting to " + host + "..."));
				{
					panel.add(progressBar = new JProgressBar(), BorderLayout.SOUTH);
					progressBar.setIndeterminate(true);
				}
			}
			{
				JPanel panel = new JPanel(new BorderLayout());
				thePanel = panel;
				JLabel background = new JLabel("");
                background.setBackground(Color.WHITE);
                background.setOpaque(true);
                background.setBorder(BorderFactory.createLineBorder(Color.WHITE));
                background.setBounds(0,0,600,600);
                panel.add(background);
				contentPane.add(panel, "blob");
				
			}
		}

		public void setCloseListener (final Runnable listener) {
			addWindowListener(new WindowAdapter() {
				public void windowClosed (WindowEvent evt) {
					listener.run();
				}

				
			});
		}

		public JPanel getPanel () {
			return thePanel;
		}

		public void setNames (final String[] names) {
			// This listener is run on the client's update thread, which was started by client.start().
			// We must be careful to only interact with Swing components on the Swing event thread.
			EventQueue.invokeLater(new Runnable() {
				public void run () {
					cardLayout.show(getContentPane(), "blob");
					
				}
			});
		
		
		}
	}

	public static void main (String[] args) {
		Log.set(Log.LEVEL_DEBUG);
		new Blobclient();
	}
}

class BlobView {
    public static final int SIZE = 50;
    BufferedImage image = null;
    private JComponent comp;
    String path0 = "Blob0.png";
    File file0 = new File(path0);
    String path1 = "Blob1.png";
    File file1 = new File(path1);
    String path2 = "Blob2.png";
    File file2 = new File(path2);
    String path3 = "Blob3.png";
    File file3 = new File(path3);
    String path4 = "Blob4.png";
    File file4 = new File(path4);

    public BlobView() {
        

		try {
			image = ImageIO.read(file0);
			comp = new JLabel(new ImageIcon(image));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
        this.comp.setBackground(Color.WHITE);
        this.comp.setOpaque(true);
    }
    public void update(Point loc, int dir) {
        this.comp.setBounds((int)loc.getX() - SIZE/2, (int)loc.getY() - SIZE/2, SIZE, SIZE);
        File tempfile;
        switch(dir){
        case 0: tempfile = file0;
        break;
        case 1: tempfile = file1;
        break;
        case 2: tempfile = file2;
        break;
        case 3: tempfile = file3;
        break;
        default: tempfile = file4;
        break;
        }
        try {
			image = ImageIO.read(tempfile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        JLabel temp = (JLabel) comp;
        if(temp.getIcon()!=image)
        temp.setIcon(new ImageIcon(image));
    }

    public JComponent getJComponent() {
        return this.comp;
    }
}
