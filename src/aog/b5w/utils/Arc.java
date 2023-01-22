package aog.b5w.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import aog.b5w.space.SpaceObject;

public class Arc {
	
	private static final int[] x11x00 = new int[] {11, 0};
	private static final int[] x00x01 = new int[] {0, 1};
	private static final int[] x01x02 = new int[] {1, 2};
	private static final int[] x02x03 = new int[] {2, 3};
	private static final int[] x03x04 = new int[] {3, 4};
	private static final int[] x04x05 = new int[] {4, 5};
	private static final int[] x05x06 = new int[] {5, 6};
	private static final int[] x06x07 = new int[] {6, 7};
	private static final int[] x07x08 = new int[] {7, 8};
	private static final int[] x08x09 = new int[] {8, 9};
	private static final int[] x09x10 = new int[] {9, 10};
	private static final int[] x10x11 = new int[] {10, 11};
	private static final int[] x00 = new int[] {0};
	private static final int[] x01 = new int[] {1};
	private static final int[] x02 = new int[] {2};
	private static final int[] x03 = new int[] {3};
	private static final int[] x04 = new int[] {4};
	private static final int[] x05 = new int[] {5};
	private static final int[] x06 = new int[] {6};
	private static final int[] x07 = new int[] {7};
	private static final int[] x08 = new int[] {8};
	private static final int[] x09 = new int[] {9};
	private static final int[] x10 = new int[] {10};
	private static final int[] x11 = new int[] {11};

	private Map<String, Map<Integer, Set<TargetingSolution>>> cache = new HashMap<String, Map<Integer, Set<TargetingSolution>>>();
	
	public Arc(List<SpaceObject> objects) {
		getAllArcs(objects);
	}
	
	public Map<Integer, Set<TargetingSolution>> getArcs(int x, int y) {
		String xy = makeXY(x, y);
		return cache.get(xy);
	}
	
	public Set<TargetingSolution> getTargetsInArc(int x, int y, int from, int to) {
		Map<Integer, Set<TargetingSolution>> arcs = getArcs(x, y);
		Set<TargetingSolution> objects = new HashSet<TargetingSolution>();
		if (arcs != null) {
			to = Die.plus(to, 1);
			if (to == from) {
				// Asked for the whole 360 degrees
				Iterator<Set<TargetingSolution>> it = arcs.values().iterator();
				while (it.hasNext()) {
					objects.addAll(it.next());
				}
			}
			for (int i = from; i != to; i = Die.plus(i, 1)) {
				Set<TargetingSolution> arc = arcs.get(i);
				if (arc != null) {
					objects.addAll(arc);
				}
			}
		}
		return new HashSet<TargetingSolution>(objects);
	}
	
	private String makeXY(int x, int y) {
		StringBuilder sb = new StringBuilder();
		sb.append(x);
		sb.append(",");
		sb.append(y);
		return sb.toString();
	}
	
	private void getAllArcs(List<SpaceObject> objects) {
		Collections.sort(objects, new PositionComparator());
		Iterator<SpaceObject> it = objects.iterator();
		while (it.hasNext()) {
			SpaceObject obj = it.next();
			getArcs(obj.getX(), obj.getY(), objects);
		}
	}
	
	private Map<Integer, Set<TargetingSolution>> getArcs(int x, int y, List<SpaceObject> objects) {
		String xy = makeXY(x, y);
		if (cache.containsKey(xy)) {
			return cache.get(xy);
		} else {
			Map<Integer, Set<TargetingSolution>> arcs = calculateArcs(x, y, objects); 
			cache.put(xy, arcs);
			return arcs;
		}
	}
	
	private Map<Integer, Set<TargetingSolution>> calculateArcs(int x, int y, List<SpaceObject> objects) {
		Collections.sort(objects, new PositionComparator());
		int lastX = Integer.MIN_VALUE; 
		int lastY = Integer.MIN_VALUE;
		int[] lastArc = new int[0];
		int lastRange = 0;
		Iterator<SpaceObject> it = objects.iterator();
		Map<Integer, Set<TargetingSolution>> arcs = new HashMap<Integer, Set<TargetingSolution>>();
		while (it.hasNext()) {
			SpaceObject obj = it.next();
			if (obj.getX() == lastX && obj.getY() == lastY) {
				addToArc(arcs, lastArc, obj, lastRange);
			} else {
				lastArc = findArcs(x, y, obj.getX(), obj.getY());
				lastX = obj.getX();
				lastY = obj.getY();
				
				lastRange = Die.distance(x, y, lastX, lastY);
				addToArc(arcs, lastArc, obj, lastRange);
			}
		}
		return arcs;
	}
	
	private void addToArc(Map<Integer, Set<TargetingSolution>> arcs, int[] pos, SpaceObject obj, int range) {
		for (int i = 0; i < pos.length; i++) {
			Set<TargetingSolution> arc = arcs.get(pos[i]);
			if (arc == null) {
				arc = new HashSet<TargetingSolution>();
				arcs.put(pos[i], arc);
			}
			arc.add(new TargetingSolution(obj, pos, range));
		}
	}
	
	private int[] findArcs(int x, int y, int i, int j) {
		//TODO deal with ships in the same hex
		if (x == i && y == j) {
			return new int[0];
		}
		
		int z = -(x + y);
		int k = -(i + j);

		int a = i - x;
		int b = j - y;
		int c = k - z;

		if (a == 0) {
			if (y < j) {
				return x11x00;
			} else {
				return x05x06;
			}
		}
		if (b == 0) {
			if (x < i) {
				return x01x02;
			} else {
				return x07x08;
			}
		}
		if (c == 0) {
			if (x < i) {
				return x03x04;
			} else {
				return x09x10;
			}
		}
		
		if (a == b) {
			if (i > x) {
				return x00x01;
			} else {
				return x06x07;
			}
		}
		if (b == c) {
			if (i > x) {
				return x02x03;
			} else {
				return x08x09;
			}
		}
		if (a == c) {
			if (y > j) {
				return x04x05;
			} else {
				return x10x11;
			}
		}
		
		if (a > 0 && a < b) {
			return x00;
		}
		if (a > b && b > 0) {
			return x01;
		}
		if (b < 0 && b > c) {
			return x02;
		} 
		if (b < c && c < 0) {
			return x03;
		}
		if (c > 0 && a > c) {
			return x04;
		}
		if (a < c && a > 0) {
			return x05;
		}
		if (a < 0 && b < a) {
			return x06;
		}
		if (b > a && b < 0) {
			return x07;
		}
		if (b > 0 && b < c) {
			return x08;
		}
		if (b > c && c > 0) {
			return x09;
		}
		if (c < 0 && a < c) {
			return x10;
		}
		if (a > c && a < 0) {
			return x11;
		}
		return new int[0];
	}
	
}

class PositionComparator implements Comparator<SpaceObject> {

	@Override
	public int compare(SpaceObject o1, SpaceObject o2) {
		if (o1.getX() == o2.getX()) {
			return o1.getY() - o2.getY();
		} else {
			return o1.getX() - o2.getX();
		}
	}
	
}
