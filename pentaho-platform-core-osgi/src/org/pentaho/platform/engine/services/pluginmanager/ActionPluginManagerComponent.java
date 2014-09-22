package org.pentaho.platform.engine.services.pluginmanager;


import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;
import org.pentaho.platform.api.action.IAction;
import org.pentaho.platform.api.action.IActionPlugin;
import org.pentaho.platform.api.action.IActionPluginManager;


public class ActionPluginManagerComponent  implements IActionPluginManager {
	private   DependencyManager dm;
	protected LogService logger;

	
	private Map<String,IActionPlugin> actionPluginsByClassName = new ConcurrentHashMap<String,IActionPlugin>();
	private String tenantPid;

	
	public ActionPluginManagerComponent(String tenantPid, DependencyManager dm, LogService logger)  {
		this.tenantPid = tenantPid;
		this.dm = dm;
		this.logger = logger;
	}
	
	//{{
    // DM callback method
    //}}
	public void init() {
	}
	
    //{{
    // DM callback method
    //}}
	public void start() {
	}	
	

	//{{
    // DM callback method
    //}}
	public void stop() {
		logger.log(LogService.LOG_INFO,"Stopping...PluginManagerComponent-"+tenantPid);
	}	
	//{{
	// DM callbacks
	//}}
	public void addActionPlugin(IActionPlugin actPlugin) {
		logger.log(LogService.LOG_INFO,"Adding action plugin "+actPlugin.toString()+" for pid "+this.tenantPid); 
		IAction bean = actPlugin.newInstance();
		actionPluginsByClassName.put(bean.getClass().getName(), actPlugin);
	}
	
	public void removeActionPlugin(IActionPlugin actPlugin) {
		logger.log(LogService.LOG_INFO,"Removing action plugin "+actPlugin.toString()+" for pid "+this.tenantPid); 
	}	
	//}}

	@Override
	public IActionPlugin getActionPluginByActionId(String actionId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IActionPlugin getActionPluginByClassName(String className) {
		return actionPluginsByClassName.get(className);
	}
}
