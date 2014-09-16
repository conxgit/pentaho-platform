package org.pentaho.platform.api.action;

public interface IActionPluginManager {
	public IActionPlugin getActionPluginByActionId(String actionId);
	public IActionPlugin getActionPluginByClassName(String className);
}
