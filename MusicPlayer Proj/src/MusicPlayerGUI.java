import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JComboBox;
import com.fazecast.jSerialComm.*;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JToolBar;
import javax.swing.JSeparator;
import javax.swing.JInternalFrame;
import java.awt.Button;
import javax.swing.JSlider;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;

public class MusicPlayerGUI extends JFrame {
	
	static SerialPort[] PortsAvailable = SerialPort.getCommPorts();
	private int index = 0;
	private static SerialPort PortChosen;
	static byte[] Send = {0x7E, (byte) 0xFF, 0x06, 0, 0, 0, 0, (byte) 0xEF};
	
	private JPanel contentPane;
	private JComboBox<String> COMcomboBox;
	private JButton Pause;
	private JButton Play;
	private JButton Stop;
	private JButton Load;
	private JButton NextSong;
	private JButton PreviousSong;
	private JSlider VolumeSlider;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MusicPlayerGUI frame = new MusicPlayerGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MusicPlayerGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton OpenPort = new JButton("OpenPort");
		OpenPort.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				index = COMcomboBox.getSelectedIndex();
	      		PortChosen = PortsAvailable[index];
	      		PortChosen.openPort();
	      		CMDSender(Commands.setVolume, (byte) 0, (byte) 16);
	      		Play.setEnabled(true);
	      		Pause.setEnabled(true);
	      		Stop.setEnabled(true);
	      		Load.setEnabled(true);
	      		NextSong.setEnabled(true);
	      		PreviousSong.setEnabled(true);
	      		OpenPort.setEnabled(false);
			}
		});
		OpenPort.setBounds(335, 42, 89, 23);
		contentPane.add(OpenPort);
		
		COMcomboBox = new JComboBox<String>();
		COMcomboBox.setBounds(275, 11, 149, 20);
		for(int i = 0; i<PortsAvailable.length; i++) {
			COMcomboBox.addItem(PortsAvailable[i].getDescriptivePortName());  
	      }
		contentPane.add(COMcomboBox);
		
		Play = new JButton("\t\u25B6");
		Play.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CMDSender(Commands.play, (byte) 0, (byte) 00);
			}
		});
		Play.setEnabled(false);
		Play.setBounds(190, 119, 53, 23);
		contentPane.add(Play);
		
		Pause = new JButton("\u2225");
		Pause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CMDSender(Commands.pause, (byte) 0, (byte) 0);
			}
		});
		Pause.setEnabled(false);
		Pause.setBounds(132, 119, 53, 23);
		contentPane.add(Pause);
		
		Stop = new JButton("\u25A0");
		Stop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CMDSender(Commands.stopPlay, (byte) 0, (byte) 0);
			}
		});
		Stop.setEnabled(false);
		Stop.setBounds(249, 119, 53, 23);
		contentPane.add(Stop);
		
		Load = new JButton("\tLoad");
		Load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CMDSender(Commands.playWithFolder, (byte) 01, (byte) 01);	
			}
		});
		Load.setEnabled(false);
		Load.setBounds(176, 153, 82, 23);
		contentPane.add(Load);
		
		NextSong = new JButton("\t\u25B6\u25B6");
		NextSong.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CMDSender(Commands.nextSong, (byte) 0, (byte) 0);
			}
		});
		NextSong.setEnabled(false);
		NextSong.setBounds(310, 119, 59, 23);
		contentPane.add(NextSong);
		
		PreviousSong = new JButton("\u25C0\u25C0");
		PreviousSong.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CMDSender(Commands.previousSong, (byte) 0, (byte) 0);
			}
		});
		PreviousSong.setEnabled(false);
		PreviousSong.setBounds(67, 119, 59, 23);
		contentPane.add(PreviousSong);
		
		VolumeSlider = new JSlider();
		VolumeSlider.setMaximum(31);
		VolumeSlider.setValue(16);
		VolumeSlider.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent arg0) {
					CMDSender(Commands.setVolume, (byte) 0, (byte) VolumeSlider.getValue());
			}
		});
		VolumeSlider.setBounds(117, 187, 200, 26);
		contentPane.add(VolumeSlider);
	}
	
	enum Commands{
		nextSong(1), previousSong(2), playWithIndex(3), volumeUp(4), volumeDown(5), setVolume(6), singleCyclePlay(8), selectDevice(9), 
		sleepMode(0x0A), wakeUp(0x0B), reset(0x0C), play(0x0D), pause(0x0E), playWithFolder(0x0F), stopPlay(0x16), cyclePlayWithFolder(0x17), 
		shufflePlay(0x18), setSingleCyclePlay(0x19), setDac(0x20), playWithVolume(0x22);	
					  
		private final int value;
		private Commands(int value) {
			this.value = value;
		}
		
		public byte getValue() {
			return (byte) this.value;
		}
	};
	
	public static void CMDSender(Commands cmd, byte data1, byte data2) {
		Send[3] = cmd.getValue();
		Send[4] = 0x00;
		Send[5] = data1;
		Send[6] = data2;
		PortChosen.writeBytes(Send, Send.length);
	}
}
