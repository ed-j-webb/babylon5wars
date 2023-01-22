package test.b5w.objects;

import aog.b5w.space.SmallCraft;

public class TestSmallCraft extends SmallCraft {

	public TestSmallCraft(String name, int facing, int x, int y) {
		super(name, facing, x, y);
		
		objectClass = CLASS_SMALLCRAFT;
	}

	
}
