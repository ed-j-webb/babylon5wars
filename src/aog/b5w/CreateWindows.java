package aog.b5w;

import java.util.Set;

import javax.swing.JFrame;

import test.b5w.cases.AbstractTest;

import aog.b5w.components.systems.weapons.Volley;
import aog.b5w.components.systems.weapons.Weapon;
import aog.b5w.exception.PhaseException;
import aog.b5w.gui.BasePanel;
import aog.b5w.gui.ComponentPanel;
import aog.b5w.gui.PhasePanel;
import aog.b5w.gui.PowerPanel;
import aog.b5w.gui.ThrustPanel;
import aog.b5w.utils.TargetingSolution;

public class CreateWindows extends AbstractTest {

	public void sleep() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void showJumpEngine() {
		BasePanel p = new ComponentPanel(jump);
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		f.add(p);
		f.pack();
		f.setVisible(true);
	}

	public void showReactor() {
		BasePanel p = new ComponentPanel(reactor);
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		f.add(p);
		f.pack();
		f.setVisible(true);
	}
	
	public void showSensors() {
		BasePanel p = new ComponentPanel(sensors);
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		f.add(p);
		f.pack();
		f.setVisible(true);
	}

	public void showEngine() {
		BasePanel p = new ComponentPanel(engine);
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		f.add(p);
		f.pack();
		f.setVisible(true);
	}

	public void showThruster() {
		BasePanel p = new ComponentPanel(thruster);
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		f.add(p);
		f.pack();
		f.setVisible(true);
	}
	
	public void showWeapon() {
		BasePanel p = new ComponentPanel(cannon);
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		f.add(p);
		f.pack();
		f.setVisible(true);
	}

	public void showHangar() {
		BasePanel p = new ComponentPanel(hangar);
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		f.add(p);
		f.pack();
		f.setVisible(true);
	}

	public void showStructure() {
		BasePanel p = new ComponentPanel(craft.getStructure(PRIMARY));
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		f.add(p);
		f.pack();
		f.setVisible(true);
	}
	
	public void showPower() {
		BasePanel p = new PowerPanel(craft);
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		f.add(p);
		f.pack();
		f.setVisible(true);
	}
	
	public void showThrust() {
		BasePanel p = new ThrustPanel(craft, SLIDE);
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		f.add(p);
		f.pack();
		f.setVisible(true);
	}

	public void showPhase() {
		PhasePanel p = new PhasePanel();
		JFrame f = new JFrame("Phase");
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		f.add(p);
		f.pack();
		f.setVisible(true);
	}

	public void runSimulation() throws PhaseException {
		sleep();
		sleep();
				
		//POWER PHASE
		advancePhase(1);
		neutron.disable();
		sleep();
		sensors.produceEW(1);
		sleep();

		//INITIATIVE
		advancePhase(1);
		
		//EW
		advancePhase(1);
		sensors.target(craft, 3);
		sleep();
		sensors.closeCombat(2);
		sleep();
		sensors.target(craft, -3);
		sleep();
		sensors.target(craft, 1);
		sleep();
		sensors.target(craft, 1);
		sleep();
		
		//ACCELERATION
		advancePhase(1);
		thruster.thrust(3, ACCELERATE);
		sleep();
		
		//JUMP POINT
		advancePhase(1);
		jump.openJumpPoint(1, 1, 3);
		sleep();
		
		//MOVE
		advancePhase(1);
		showThrust();
		//TARGET
		advancePhase(1);
		Set<TargetingSolution> set = Space.getTargetingArcs().getTargetsInArc(craft.getX(), craft.getY(), cannon.getArcStart(), cannon.getArcFinish());
		cannon.target(set.iterator().next());
		sleep();
		
		//FIRE
		advancePhase(1);
		cannon.fire();
		sleep();
		jump.takeVolley(new Volley(8, Weapon.STANDARD));
		sleep();
		//reactor.takeVolley(new Volley(8, Weapon.STANDARD));
		sleep();
		thruster.takeVolley(new Volley(8, Weapon.STANDARD));
		sleep();
		jump.takeVolley(new Volley(8, Weapon.STANDARD));
		sleep();
		
		//CRITICAL
		advancePhase(1);
		
		//LAUNCH
		advancePhase(1);
		hangar.launch(hangar.getContents().get(1));

		for (int i = 0; i < 20; i++) {
			advancePhase(1);
			sleep();
		}
		
	}
	
	public static void main(String[] args) throws Exception {
		CreateWindows cw = new CreateWindows();
		cw.setUp();
		cw.addOneArcCraft();
		cw.showJumpEngine();
		cw.showEngine();
		cw.showReactor();
		cw.showSensors();
		cw.showThruster();
		cw.showWeapon();
		cw.showHangar();
		cw.showStructure();
		cw.showPower();
		cw.showPhase();
		//cw.showThrust();

		cw.runSimulation();
	}
}
