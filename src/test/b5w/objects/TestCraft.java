package test.b5w.objects;

import java.util.ArrayList;
import java.util.List;

import aog.b5w.components.Structure;
import aog.b5w.components.systems.Engine;
import aog.b5w.components.systems.Hangar;
import aog.b5w.components.systems.JumpEngine;
import aog.b5w.components.systems.Reactor;
import aog.b5w.components.systems.Sensors;
import aog.b5w.components.systems.Thruster;
import aog.b5w.components.systems.weapons.HeavyLaserCannon;
import aog.b5w.components.systems.weapons.HeavyPulseCannon;
import aog.b5w.components.systems.weapons.InterceptorMkII;
import aog.b5w.components.systems.weapons.NeutronCannon;
import aog.b5w.components.systems.weapons.StandardParticleBeam;
import aog.b5w.space.LargeCraft;
import aog.b5w.space.SmallCraft;

public class TestCraft extends LargeCraft {

	public TestCraft(String name) {
		this(name, 0, 0, 0);
	}
	public TestCraft(String name, int facing, int x, int y) {
		super(name, facing, x, y);

		structureFacing = CAPITAL_STRUCTURE_FACING;
		
		turnCost[NUMERATOR] = 1;
		turnCost[DENOMINATOR] = 1;
		turnDelay[NUMERATOR] = 1;
		turnDelay[DENOMINATOR] = 1;
		
		pivotStartCost = 2;
		pivotEndCost = 2;
		
		rollStartCost = 3;
		rollEndCost = 4;
				
		objectClass = CLASS_CAPITALSHIP;

		defence[FORE] = 16;
		defence[PORT] = 18;
		defence[AFT] = 16;
		defence[STARBOARD] = 18;
		
		addStructure(createPrimary(), PRIMARY);
		addStructure(createFore(), FORE);
		addStructure(createStarboard(), STARBOARD);
		addStructure(createPort(), PORT);
		addStructure(createAft(), AFT);

		commission();
	}

	private Structure createPrimary() {
		Structure primary = new Structure("Primary", 60, 6, 1, 8);
		
		JumpEngine jump = new JumpEngine("Jump Engine", 20, 6, 3, 20);
		primary.addSystem(jump, 9, 10);
		
		Reactor reactor = new Reactor("Reactor", 24, 6, 9);
		primary.addSystem(reactor, 19, 19);
		
		Sensors sensors = new Sensors("Sensors", 20, 6, 4, 8);
		primary.addSystem(sensors, 11, 14);
		
		TestSmallCraft c1 = new TestSmallCraft("Fighter1", 0, 0, 0);
		TestSmallCraft c2 = new TestSmallCraft("Fighter2", 0, 0, 0);
		TestSmallCraft c3 = new TestSmallCraft("Fighter3", 0, 0, 0);
		TestSmallCraft c4 = new TestSmallCraft("Fighter4", 0, 0, 0);
		TestSmallCraft c5 = new TestSmallCraft("Fighter5", 0, 0, 0);
		TestSmallCraft c6 = new TestSmallCraft("Fighter6", 0, 0, 0);

		List<SmallCraft> c = new ArrayList<SmallCraft>();
		c.add(c1);
		c.add(c2);
		c.add(c3);
		c.add(c4);
		c.add(c5);
		c.add(c6);
		
		Hangar hangar = new Hangar("Hangar", 26, 6, 2, c, new int[] {0, 6});
		primary.addSystem(hangar, 17, 18);
		
		Engine engine = new Engine("Engine", 21, 6, 8, 3);
		primary.addSystem(engine, 15, 16);
		
		return primary;
	}
	
	private Structure createFore() {
		Structure fore = new Structure("Fore", 60, 6, 12, 18);

		// Stolen from the Minbari
		NeutronCannon c = new NeutronCannon("Neutron Cannon", 20, 4, 10, 2);
		fore.addSystem(c, 0, 0);
		
		Thruster thruster = new Thruster("Port Retro", 10, 3, 8);
		fore.addSystem(thruster, 1, 3);
		thruster = new Thruster("Starboard Retro", 10, 3, 8);
		fore.addSystem(thruster, 1, 3);

		HeavyLaserCannon hvyLC = new HeavyLaserCannon("Fore Hvy Laser Cannon 1", 8, 4, 10, 0);
		fore.addSystem(hvyLC, 4, 6);
		hvyLC = new HeavyLaserCannon("Fore Hvy Laser Cannon 2", 8, 4, 0, 2);
		fore.addSystem(hvyLC, 4, 6);

		HeavyPulseCannon hvyPC = new HeavyPulseCannon("Fore Hvy Pulse Cannon 1", 6, 4, 4, 10, 0);
		fore.addSystem(hvyPC, 7, 8);
		hvyPC = new HeavyPulseCannon("Fore Hvy Pulse Cannon 2", 6, 4, 4, 0, 2);
		fore.addSystem(hvyPC, 7, 8);

		InterceptorMkII inter = new InterceptorMkII("Interceptor Fore Starboard", 4, 2, 1, 8, 2);
		fore.addSystem(inter, 9, 11);
		inter = new InterceptorMkII("Interceptor Fore Port", 4, 2, 1, 10, 4);
		fore.addSystem(inter, 9, 11);
		
		return fore;
	}
	
	private Structure createStarboard() {
		Structure starboard = new Structure("Starboard", 70, 4, 13, 18);

		Thruster thruster3 = new Thruster("Starboard Thruster", 15, 3, 8);
		starboard.addSystem(thruster3, 1, 4);

		StandardParticleBeam stdPB = new StandardParticleBeam("Port Std Particle Beam 1", 4, 2, 1, 0, 6);
		starboard.addSystem(stdPB, 5, 9);
		stdPB = new StandardParticleBeam("Port Std Particle Beam 2", 4, 2, 1, 0, 6);
		starboard.addSystem(stdPB, 5, 9);
		stdPB = new StandardParticleBeam("Port Std Particle Beam 3", 4, 2, 1, 0, 6);
		starboard.addSystem(stdPB, 5, 9);
		stdPB = new StandardParticleBeam("Port Std Particle Beam 4", 4, 2, 1, 0, 6);
		starboard.addSystem(stdPB, 5, 9);
		stdPB = new StandardParticleBeam("Port Std Particle Beam 5", 4, 2, 1, 0, 6);
		starboard.addSystem(stdPB, 5, 9);
		stdPB = new StandardParticleBeam("Port Std Particle Beam 6", 4, 2, 1, 0, 6);
		starboard.addSystem(stdPB, 5, 9);
		
		InterceptorMkII inter = new InterceptorMkII("Interceptor Starboard", 4, 2, 1, 0, 6);
		starboard.addSystem(inter, 10, 12);
		
		return starboard;
	}

	private Structure createPort() {
		Structure port = new Structure("Port", 70, 4, 13, 18);

		Thruster thruster3 = new Thruster("Port Thruster", 15, 3, 8);
		port.addSystem(thruster3, 1, 4);

		StandardParticleBeam stdPB = new StandardParticleBeam("Port Std Particle Beam 1", 4, 2, 1, 6, 0);
		port.addSystem(stdPB, 5, 9);
		stdPB = new StandardParticleBeam("Port Std Particle Beam 2", 4, 2, 1, 6, 0);
		port.addSystem(stdPB, 5, 9);
		stdPB = new StandardParticleBeam("Port Std Particle Beam 3", 4, 2, 1, 6, 0);
		port.addSystem(stdPB, 5, 9);
		stdPB = new StandardParticleBeam("Port Std Particle Beam 4", 4, 2, 1, 6, 0);
		port.addSystem(stdPB, 5, 9);
		stdPB = new StandardParticleBeam("Port Std Particle Beam 5", 4, 2, 1, 6, 0);
		port.addSystem(stdPB, 5, 9);
		stdPB = new StandardParticleBeam("Port Std Particle Beam 6", 4, 2, 1, 6, 0);
		port.addSystem(stdPB, 5, 9);
		
		InterceptorMkII inter = new InterceptorMkII("Interceptor Port", 4, 2, 1, 6, 0);
		port.addSystem(inter, 10, 12);
		
		return port;
	}

	private Structure createAft() {
		Structure aft = new Structure("Aft", 50, 4, 13, 18);

		Thruster thruster = new Thruster("Main Outer Starboard Thruster", 9, 4, 8);
		aft.addSystem(thruster, 1, 6);
		thruster = new Thruster("Main Inner Starboard Thruster", 9, 4, 8);
		aft.addSystem(thruster, 1, 6);
		thruster = new Thruster("Main Inner Port Thruster", 9, 4, 8);
		aft.addSystem(thruster, 1, 6);
		thruster = new Thruster("Main Outer Port Thruster", 9, 4, 8);
		aft.addSystem(thruster, 1, 6);

		HeavyLaserCannon hvyLC = new HeavyLaserCannon("Aft Hvy Laser Cannon 1" , 8, 3, 6, 8);
		aft.addSystem(hvyLC, 7, 9);
		hvyLC = new HeavyLaserCannon("Aft Hvy Laser Cannon 2" , 8, 3, 4, 6);
		aft.addSystem(hvyLC, 7, 9);

		InterceptorMkII inter = new InterceptorMkII("Interceptor Aft Starboard", 4, 2, 1, 2, 8);
		aft.addSystem(inter, 10, 12);
		inter = new InterceptorMkII("Interceptor Aft Port", 4, 2, 1, 4, 10);
		aft.addSystem(inter, 10, 12);
		
		return aft;
	}
	
	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void setDirectionOfTravel(int dir) {
		directionOfTravel = dir;
	}
	
	public int getTurnWait() {
		return moveWait[TURN];
	}
	
}
