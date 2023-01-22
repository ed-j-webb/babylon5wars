package test.b5w.cases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import aog.b5w.Space;
import aog.b5w.components.systems.Thruster;
import aog.b5w.exception.PhaseException;

public abstract class AbstractMoveTest extends AbstractTest {

	public void accelerate(Thruster t) {
		advancePhase(4);

		assertEquals(PHASE_ACCELERATION, Space.getPhase());
		
		try {
			t.thrust(4, ACCELERATE);
		} catch (PhaseException e) {
			fail("Thrusters can thrust in Accel phase");
		}

		advancePhase(2);

		assertEquals(PHASE_MOVE, Space.getPhase());

	}

}
