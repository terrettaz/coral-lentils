package ch.pterrettaz.corallentis;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.management.ObjectName;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class MBeanTreeModel implements TreeModel {

	private List<ObjectName> names = new CopyOnWriteArrayList<>();


	public void refresh(List<ObjectName> names) {
		this.names = names;
	}

	@Override
	public Object getRoot() {
		return "beans";
	}

	@Override
	public Object getChild(Object parent, int index) {
		return null;
	}

	@Override
	public int getChildCount(Object parent) {
		return 0;
	}

	@Override
	public boolean isLeaf(Object node) {
		return false;
	}

	@Override
	public void valueForPathChanged(TreePath path, Object newValue) {

	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		return 0;
	}

	@Override
	public void addTreeModelListener(TreeModelListener l) {

	}

	@Override
	public void removeTreeModelListener(TreeModelListener l) {

	}
}
