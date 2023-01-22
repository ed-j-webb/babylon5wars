package test.b5w.cases;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import aog.b5w.Space;
import aog.b5w.exception.PhaseException;
import aog.b5w.space.SpaceObject;

public class PhaseTest extends AbstractTest {

	@Test
	public void testOneTurn() {
		 assertEquals(0, Space.getTurn());
		 assertEquals(0, Space.getPhase());
		 
		 assertEquals(9, reactor.getPower());
		 assertEquals(9, reactor.getSurplus());
		 
		 List<SpaceObject> list;
		 
		 // Power Phase
		 list = Space.advancePhase();

		 assertEquals(0, list.size());

		 assertEquals(1, Space.getTurn());
		 assertEquals(PHASE_POWER, Space.getPhase());
		 assertEquals(9, reactor.getPower());
		 assertEquals(9, reactor.getSurplus());
		 
		 try {
			 cannon.disable();
		 } catch (PhaseException e) {
			 fail("Optional Systems can be disabled in Power phase");
		 }
		 
		 assertEquals(false, cannon.isEnabled());
		 assertEquals(15, reactor.getSurplus());

		 assertEquals(8, sensors.getCurrentEw());
		 assertEquals(8, sensors.getDefensiveEw());

		 assertEquals(15, reactor.getSurplus());

		 try {
			 sensors.produceEW(1);
		 } catch (PhaseException e) {
			 fail("Sensors can produce EW in Power phase");
		 }

		 assertEquals(6, reactor.getSurplus());
		 assertEquals(9, sensors.getCurrentEw());
		 assertEquals(9, sensors.getDefensiveEw());
		 
		 // Initiative Phase
		 list = Space.advancePhase();
		 
		 assertEquals(0, list.size());

		 assertEquals(1, Space.getTurn());
		 assertEquals(PHASE_INITIATIVE, Space.getPhase());
		 
		 // EW Phase
		 list = Space.advancePhase();
		 
		 assertEquals(0, list.size());

		 assertEquals(1, Space.getTurn());
		 assertEquals(PHASE_EW, Space.getPhase());
		 assertEquals(9, sensors.getCurrentEw());
		 
		 try {
			 sensors.target(craft, 3);
		 } catch(PhaseException e) {
			 fail("Sensors can target in EW phase");
		 }

		 assertEquals(9, sensors.getCurrentEw());
		 assertEquals(3, sensors.getOffensiveEw(craft));
		 assertEquals(6, sensors.getDefensiveEw());
		 // Acceleration Phase
		 list = Space.advancePhase();
		 assertEquals(0, list.size());

		 assertEquals(1, Space.getTurn());
		 assertEquals(PHASE_ACCELERATION, Space.getPhase());

		 assertEquals(0, craft.getVelocity());
		 assertEquals(0, thruster.getCurrentThrust());
		 assertEquals(8, engine.getCurrentThrust());
		 
		 try {
			 thruster.thrust(3, SLIDE);
			 fail("Cannot slide in Accel phase");
		 } catch (PhaseException e) {
			 // Expected
		 }

		 assertEquals(0, craft.getVelocity());
		 assertEquals(0, thruster.getCurrentThrust());
		 assertEquals(8, engine.getCurrentThrust());

		 try {
			 thruster.thrust(4, ACCELERATE);
		 } catch (PhaseException e) {
			 fail("Fore Thrusters can thrust in Accel phase");
		 }
		 
		 assertEquals(4, craft.getVelocity());
		 assertEquals(4, thruster.getCurrentThrust());
		 assertEquals(4, engine.getCurrentThrust());
		 
		 //TODO Test acceleration phase

		 // Jump Point Phase
		 list = Space.advancePhase();
		 assertEquals(0, list.size());

		 assertEquals(1, Space.getTurn());
		 assertEquals(PHASE_JUMP_POINT, Space.getPhase());

		 //TODO Test jump point phase
		 
		 // Move Phase
		 list = Space.advancePhase();
		 assertEquals(0, list.size());

		 assertEquals(1, Space.getTurn());
		 assertEquals(PHASE_MOVE, Space.getPhase());

		 assertEquals(4, thruster.getCurrentThrust());
		 assertEquals(4, engine.getCurrentThrust());
		 assertEquals(0, craft.getX());
		 assertEquals(0, craft.getY());
		 assertEquals(4, craft.getMoves());
		 
			try {
				craft.move(1);
			} catch (PhaseException e) {
				e.printStackTrace();
				fail("Can move in move phase");
			}
			
		 assertEquals(0, craft.getX());
		 assertEquals(1, craft.getY());
		 assertEquals(3, craft.getMoves());
		 
		 try {
			 thruster.thrust(3, SLIDE);
			 fail("Fore thruster can't slide");
		 } catch (PhaseException e) {
			 //Expected
		 }

		 assertEquals(4, thruster.getCurrentThrust());
		 assertEquals(0, stbdThruster.getCurrentThrust());
		 assertEquals(4, engine.getCurrentThrust());
		 assertEquals(3, craft.getMoves());
		 
		 try {
			 stbdThruster.thrust(1, SLIDE);
		 } catch (PhaseException e) {
			 System.out.println(e.getMessage());
			 fail("Can slide in Move phase");
		 }

		 assertEquals(4, thruster.getCurrentThrust());
		 assertEquals(1, stbdThruster.getCurrentThrust());
		 assertEquals(3, engine.getCurrentThrust());
		 assertEquals(-1, craft.getX());
		 assertEquals(2, craft.getY());
		 assertEquals(2, craft.getMoves());

		 try {
			 portThruster.thrust(2, SLIDE);
			 fail("Can only slide one way in Move phase");
		 } catch (PhaseException e) {
			 //Expected
		 }

		 assertEquals(4, thruster.getCurrentThrust());
		 assertEquals(1, stbdThruster.getCurrentThrust());
		 assertEquals(3, engine.getCurrentThrust());
		 assertEquals(-1, craft.getX());
		 assertEquals(2, craft.getY());
		 assertEquals(2, craft.getMoves());

		 try {
			 stbdThruster.thrust(1, SLIDE);
			 fail("Must move one hex before sliding again");
		 } catch (PhaseException e) {
			 //Expected
		 }

		 assertEquals(4, thruster.getCurrentThrust());
		 assertEquals(1, stbdThruster.getCurrentThrust());
		 assertEquals(3, engine.getCurrentThrust());
		 assertEquals(-1, craft.getX());
		 assertEquals(2, craft.getY());
		 assertEquals(2, craft.getMoves());

			try {
				craft.move(1);
			} catch (PhaseException e) {
				e.printStackTrace();
				fail("Can move in move phase");
			}

		 assertEquals(4, thruster.getCurrentThrust());
		 assertEquals(1, stbdThruster.getCurrentThrust());
		 assertEquals(3, engine.getCurrentThrust());
		 assertEquals(-1, craft.getX());
		 assertEquals(3, craft.getY());
		 assertEquals(1, craft.getMoves());

		 try {
			 stbdThruster.thrust(1, SLIDE);
		 } catch (PhaseException e) {
			 fail("Has moved 1 hex");
		 }
		 
		 assertEquals(4, thruster.getCurrentThrust());
		 assertEquals(2, stbdThruster.getCurrentThrust());
		 assertEquals(2, engine.getCurrentThrust());
		 assertEquals(-2, craft.getX());
		 assertEquals(4, craft.getY());
		 assertEquals(0, craft.getMoves());
		 
		 
		 
		 //TODO Test move phase

		 // Screen Phase
		 list = Space.advancePhase();
		 assertEquals(0, list.size());

		 assertEquals(1, Space.getTurn());
		 assertEquals(PHASE_TARGET, Space.getPhase());

		 //TODO Test screen phase

		 // Fire Phase
		 list = Space.advancePhase();
		 assertEquals(0, list.size());

		 assertEquals(1, Space.getTurn());
		 assertEquals(PHASE_FIRE, Space.getPhase());

		 //TODO Test fire phase

		 // Critical Phase
		 list = Space.advancePhase();
		 assertEquals(0, list.size());

		 assertEquals(1, Space.getTurn());
		 assertEquals(PHASE_CRITICAL, Space.getPhase());

		 //TODO Test critical phase

		 // Launch Phase
		 list = Space.advancePhase();
		 assertEquals(0, list.size());

		 assertEquals(1, Space.getTurn());
		 assertEquals(PHASE_LAUNCH, Space.getPhase());

		 //TODO Test launch phase
		 
		 // Acceleration Phase
		 list = Space.advancePhase();
		 assertEquals(0, list.size());

		 assertEquals(2, Space.getTurn());
		 assertEquals(PHASE_POWER, Space.getPhase());
 
	}

}
