import com.fazecast.jSerialComm.*;
import java.util.Scanner;
public class MusicPlayer {
	
	static SerialPort[] PortsAvailable = SerialPort.getCommPorts();
	static SerialPort PortChosen;
	static Scanner reader = new Scanner(System.in);
	static byte[] Send = {0x7E, (byte) 0xFF, 0x06, 0, 0, 0, 0, (byte) 0xEF};
	
	public static SerialPort portchooser() {
		for(int i = 0; i<PortsAvailable.length; i++) {
			System.out.println(i + ": " + PortsAvailable[i].getDescriptivePortName());
		}
		System.out.println("Pick your port:");
		int pick = reader.nextInt();
		PortChosen = PortsAvailable[pick];
		System.out.println("Choosen port it " + PortChosen.getDescriptivePortName());
		PortChosen.openPort();
		return PortChosen;
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
	
	public static void main(String[] args) {
		portchooser();
		CMDSender(Commands.playWithFolder, (byte) 01, (byte) 01);
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		CMDSender(Commands.stopPlay, (byte) 0, (byte) 0);
	}

}
