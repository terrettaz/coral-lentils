package ch.pterrettaz.corallentis;

import java.awt.Dimension;
import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;
import javax.swing.JFrame;

import lombok.SneakyThrows;

public class MBeanConsole {

	@SneakyThrows
	public static void main(String[] args) {

		MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
		final JMXConnectorServer cs = JMXConnectorServerFactory.newJMXConnectorServer(new JMXServiceURL("jmxmp", null, 12345), null, mbs);
		cs.start();

		JMXServiceURL url = new JMXServiceURL("service:jmx:jmxmp://localhost:" + 12345);
		var jmxConnector = JMXConnectorFactory.connect(url);
		MBeanServerConnection mbeanServerConnection = jmxConnector.getMBeanServerConnection();

		var listModelMBean = new ListModelMBean(mbeanServerConnection);

		JFrame main = new JFrame();
		main.setTitle("MBean Console");
		main.setPreferredSize(new Dimension(1000, 800));
		main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		main.setContentPane(new MainPane(listModelMBean, mbeanServerConnection));
		main.pack();
		main.setVisible(true);
	}

}
