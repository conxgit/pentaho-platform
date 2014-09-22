package org.pentaho.platform.util.osgi;

import java.beans.Transient;

import org.apache.felix.dm.tracker.ServiceTracker;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.log.LogService;

public abstract class OSGUtilSupport {
	
	protected transient LogService logger;
	
	public void init()  {
		try {
			logger = (LogService)getService(LogService.class,null);
		} catch (InvalidSyntaxException e1) {
			throw new RuntimeException("Error getting log service ref",e1);
		}
	}
	
	@Transient
	protected <T> T getService(Class<T> serviceClass, String filterString)
			throws InvalidSyntaxException {
		BundleContext context = FrameworkUtil.getBundle(OSGUtilSupport.class).getBundleContext();
		
		T serviceInstance = null;

		ServiceTracker serviceTracker;
		if (filterString == null) {
			serviceTracker = new ServiceTracker(context,
					serviceClass.getName(), null);
		} else {
			String classFilter = "(" + Constants.OBJECTCLASS + "="
					+ serviceClass.getName() + ")";
			filterString = "(&" + classFilter + filterString + ")";
			serviceTracker = new ServiceTracker(context,
					context.createFilter(filterString), null);
		}
		serviceTracker.open();
		try {
			serviceInstance = (T) serviceTracker.waitForService(10 * 1000);

			if (serviceInstance == null) {
				throw new RuntimeException("Failed to locate "+serviceClass + " service.");
			} else {
				return serviceInstance;
			}
		} catch (InterruptedException e) {
			throw new RuntimeException(serviceClass + " service not available: " + e.toString(),e);
		}

	}

}
