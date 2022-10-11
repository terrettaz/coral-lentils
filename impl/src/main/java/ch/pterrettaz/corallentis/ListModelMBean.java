package ch.pterrettaz.corallentis;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.swing.AbstractListModel;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ListModelMBean extends AbstractListModel<ObjectName> {

	private final MBeanServerConnection mBeanServerConnection;

	private final List<ObjectName> objectNames = new CopyOnWriteArrayList<>();

	public void refresh() {
		try {
			var refreshed = mBeanServerConnection.queryNames(null, null)
					.stream()
					.sorted()
					.collect(Collectors.toList());

			refreshed.forEach(System.out::println);

			fireIntervalRemoved(this, 0, objectNames.size());
			objectNames.clear();
			objectNames.addAll(refreshed);
			fireIntervalAdded(this, 0, objectNames.size());
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<ObjectName> getObjectNames() {
		return objectNames;
	}

	@Override
	public int getSize() {
		return objectNames.size();
	}

	@Override
	public ObjectName getElementAt(int index) {
		return objectNames.get(index);
	}
}
