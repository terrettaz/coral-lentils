package ch.pterrettaz.corallentis;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.MouseInputAdapter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class MainPane extends JPanel {

	private final ListModelMBean listModelMBean;
	private final DefaultTreeModel treeModel = new DefaultTreeModel(new DefaultMutableTreeNode());
	private final MBeanServerConnection mBeanServerConnection;

	public MainPane(ListModelMBean listModelMBean, MBeanServerConnection mBeanServerConnection) {
		this.listModelMBean = listModelMBean;
		this.mBeanServerConnection = mBeanServerConnection;
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.VERTICAL;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 0;
		add(refreshButton(), c);

		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 1;
		c.weighty = 1;
		add(new JScrollPane(tree()), c);
	}

	private JButton refreshButton() {
		var button = new JButton("Refresh");
		button.addActionListener(e -> {
			listModelMBean.refresh();
			List<ObjectName> names = listModelMBean.getObjectNames();
			var root = new DefaultMutableTreeNode();
			names.forEach(on -> {
				List<String> path = new ArrayList<>();
				path.add(on.getDomain());
				path.addAll(Arrays.stream(on.getKeyPropertyListString().split(","))
						.map(part -> part.split("="))
						.map(parts -> {
							if (parts.length < 2) {
								return "na";
							}
							return parts[1];
						})
						.collect(Collectors.toList()));
				addPath(root, path, on);
			});
			treeModel.setRoot(root);
		});
		return button;
	}

	private JTree tree() {
		var tree = new JTree(treeModel);
		tree.addMouseListener(new MouseInputAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				var selected = tree.getLastSelectedPathComponent();
				if (selected instanceof DefaultMutableTreeNode) {
					var userObject = ((DefaultMutableTreeNode)selected).getUserObject();
					if (userObject instanceof ObjectName) {
						System.out.println(userObject);
					}
				}
			}
		});
		return tree;
	}

	void addPath(DefaultMutableTreeNode parent, List<String> path, ObjectName on) {
		if (path.size() > 0) {
			var head = path.get(0);
			DefaultMutableTreeNode node = null;
			for (int i = 0; i < parent.getChildCount(); i++) {
				if (parent.getChildAt(i).toString().equals(head)) {
					node = (DefaultMutableTreeNode) parent.getChildAt(i);
				}
			}
			if (node == null) {
				node = new DefaultMutableTreeNode(head);
				parent.add(node);
			}
			addPath(node, path.subList(1, path.size()), on);
		} else {
			// TODO add Lazy Managed Operation
			parent.setUserObject(on);
		}
	}

}
