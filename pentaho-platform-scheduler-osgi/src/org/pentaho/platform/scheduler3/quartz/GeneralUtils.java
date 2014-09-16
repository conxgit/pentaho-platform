package org.pentaho.platform.scheduler3.quartz;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class GeneralUtils {
	  public static Map<String, Serializable> toSerializableValueMap(
				Map<String, Object> wrappedMap) {
			  Map<String, Serializable> map = new HashMap<String, Serializable>();
			  for (String key: wrappedMap.keySet()) {
				  map.put(key, (Serializable)wrappedMap.get(key));
			  }
			return map;
		}
}
