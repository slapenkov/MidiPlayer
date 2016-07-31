package midiplayer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.sound.midi.ControllerEventListener;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MiniMusicPlayer3 {

	@SuppressWarnings("serial")
	public class MyDrawPanel extends JPanel implements ControllerEventListener {
		boolean msg = false;

		@Override
		public void paintComponent(Graphics g) {
			if (msg) {

				Graphics2D g2 = (Graphics2D) g;

				int r = (int) (Math.random() * 250);
				int gr = (int) (Math.random() * 250);
				int b = (int) (Math.random() * 250);

				g2.setColor(new Color(r, gr, b));

				int ht = 10 + (int) (Math.random() * 120);
				int width = 10 + (int) (Math.random() * 120);

				int x = 10 + (int) (Math.random() * 40);
				int y = 10 + (int) (Math.random() * 40);

				g2.fillRect(x, y, width, ht);
				msg = false;
			}
		}

		@Override
		public void controlChange(ShortMessage event) {
			msg = true;
			repaint();
		}

	}

	static JFrame f = new JFrame("My first music clip");
	static MyDrawPanel m1;

	public static void main(String[] args) {
		MiniMusicPlayer3 mini = new MiniMusicPlayer3();
		mini.go();

	}

	private void go() {
		setUpGUI(0);

		try {
			Sequencer sequencer = MidiSystem.getSequencer();
			sequencer.open();
			sequencer.addControllerEventListener(m1, new int[] { 127 });
			Sequence seq = new Sequence(Sequence.PPQ, 4);
			Track track = seq.createTrack();

			int r = 0;
			for (int i = 0; i < 60; i += 4) {
				r = (int) ((Math.random() * 50) + 1);
				track.add(makeEvent(144, 1, r, 100, i));
				track.add(makeEvent(176, 1, 127, 0, i));
				track.add(makeEvent(128, 1, r, 100, i + 2));
			}

			sequencer.setSequence(seq);
			sequencer.start();
			sequencer.setTempoInBPM(120);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static MidiEvent makeEvent(int comd, int chan, int one, int two,
			int tick) {
		MidiEvent event = null;
		try {
			ShortMessage a = new ShortMessage();
			a.setMessage(comd, chan, one, two);
			event = new MidiEvent(a, tick);
		} catch (Exception e) {

		}
		return event;
	}

	public void setUpGUI(int i) {
		m1 = new MyDrawPanel();
		f.setContentPane(m1);
		f.setBounds(30, 30, 300, 300);
		f.setVisible(true);
	}

}
