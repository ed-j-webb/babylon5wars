package aog.b5w.components;

/**
 * Interface that all systems that need to change their state when the craft has rolled
 * must implement. The roll() method makes the necessary changes to the system.
 * 
 * @author edw
 *
 */
public interface Rollable {

	public void roll();
}
