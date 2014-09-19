package org.pentaho.platform.scheduler3.osgi;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;
import org.pentaho.platform.api.scheduler2.IBlockoutManager;
import org.pentaho.platform.scheduler3.blockout.PentahoBlockoutManager;

public class Activator extends DependencyActivatorBase {
	@Override
	public synchronized void init(BundleContext context,
			DependencyManager manager) throws Exception {
		
		
		//-- Blockout manager
		manager.add(createComponent()
				.setInterface(IBlockoutManager.class.getName(), null)
				.setImplementation(PentahoBlockoutManager.class)
				.add(createServiceDependency().setService(LogService.class).setRequired(false))
		);
	}

	@Override
	public synchronized void destroy(BundleContext context,
			DependencyManager manager) throws Exception {
	}
}
