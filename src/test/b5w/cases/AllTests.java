package test.b5w.cases;

import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@SuiteClasses({
	ArcTest.class,
	DamageTest.class,
	HangarTest.class,
    JumpTest.class,
    MoveTest.class,
	PhaseTest.class,
    PivotTest.class,
    RollTest.class,
    SensorsTest.class,
    SlideTest.class,
    TargetTest.class,
    ToHitTest.class,
    TurnTest.class,
    WeaponTest.class
})

public class AllTests {}
