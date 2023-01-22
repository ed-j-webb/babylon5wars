package aog.b5w.event;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ChangeNotifier {

	private static final String ALL_STATUSES = "** ALL STATUSES **";
	
	protected Map<ChangeListener, Set<String>> byListener = new HashMap<ChangeListener, Set<String>>();
	protected Map<String, Set<ChangeListener>> byStatus = new HashMap<String, Set<ChangeListener>>();
	
	public void addChangeListener(ChangeListener listener, String status) {
		synchronized (byListener) {
			if (!byListener.containsKey(listener)) {
				Set<String> statuses = new HashSet<String>();
				byListener.put(listener, statuses);
			}
			Set<String> statuses = byListener.get(listener);
			statuses.add(status);

			if (!byStatus.containsKey(status)) {
				Set<ChangeListener> listeners = new HashSet<ChangeListener>();
				byStatus.put(status, listeners);
			}
			Set<ChangeListener> set = byStatus.get(status);
			set.add(listener);
		}
	}

	public void addChangeListener(ChangeListener listener) {
		addChangeListener(listener, ALL_STATUSES);
	}
	
	public void removeChangeListener(ChangeListener listener, String status) {
		synchronized (byListener) {
			Set<String> statuses = byListener.get(listener);
			statuses.remove(status);
			Set<ChangeListener> listeners = byStatus.get(status);
			listeners.remove(listener);
		}
	}
	
	public void removeChangeListener(ChangeListener listener) {
		synchronized (byListener) {
			Set<String> statuses = byListener.get(listener);
			if (statuses != null) {
				Iterator<String> it = statuses.iterator();
				while (it.hasNext()) {
					Set<ChangeListener> listeners = byStatus.get(it.next());
					if (listeners != null) {
						listeners.remove(listener);
					}
				}
			}
			byListener.remove(listener);
		}
	}
	
	public void notifyChangeListeners(ChangeEvent e) {
		synchronized (byListener) {
			notifyChangeListeners(e, e.getStatus());
			notifyChangeListeners(e, ALL_STATUSES);
		}
	}

	private void notifyChangeListeners(ChangeEvent e, String status) {
		Set<ChangeListener> listeners = byStatus.get(status);
		if (listeners != null) {
			Iterator<ChangeListener> it = listeners.iterator();
			while (it.hasNext()) {
				it.next().stateChanged(e);
			}
		}
	}
		
}
