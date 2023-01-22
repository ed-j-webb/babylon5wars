package aog.b5w.components.systems.weapons;

import java.util.ArrayList;
import java.util.List;

public class Salvo {

	protected List<Volley> volleys = new ArrayList<Volley>();
	
	protected String fireMode;
	
	protected int arc;

	public Salvo (List<Volley> volleys, String fireMode, int arc) {
		this.volleys = volleys;
		this.fireMode = fireMode;
		this.arc = arc;
	}
	
	public List<Volley> getVolleys() {
		return volleys;
	}

	public String getFireMode() {
		return fireMode;
	}
	
	public int getArc() {
		return arc;
	}
	
	public Salvo createSubSalvo(int damage) {
		return createSubSalvo(new int[] {damage});
	}
	
	public Salvo createSubSalvo(int[] damage) {
		List<Volley> vs = new ArrayList<Volley>();
		for (int i = 0; i < damage.length; i++) {
			Volley v = new Volley(damage[i], this.fireMode);
			vs.add(v);
		}
		Salvo s = new Salvo(vs, this.fireMode, this.arc);
		return s;
	}
	
	public Salvo extractVolley(int index) {
		List<Volley> vs = new ArrayList<Volley>();
		vs.add(volleys.get(index));
		return new Salvo(vs, this.fireMode, this.arc);
	}

	public Salvo extractVolley(Volley v) {
		List<Volley> vs = new ArrayList<Volley>();
		vs.add(v);
		return new Salvo(vs, this.fireMode, this.arc);
	}
}
