/*
 * Copyright 2002 - 2013 Pentaho Corporation.  All rights reserved.
 * 
 * This software was developed by Pentaho Corporation and is provided under the terms
 * of the Mozilla Public License, Version 1.1, or any later version. You may not use
 * this file except in compliance with the license. If you need a copy of the license,
 * please go to http://www.mozilla.org/MPL/MPL-1.1.txt. TThe Initial Developer is Pentaho Corporation.
 *
 * Software distributed under the Mozilla Public License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to
 * the license for the specific language governing your rights and limitations.
 */

package org.pentaho.platform.scheduler3.blockout;

import java.util.Date;
import java.util.Map;

import org.osgi.service.log.LogService;
import org.pentaho.platform.api.action.IVarArgsAction;
import org.pentaho.platform.api.scheduler3.IBlockoutManager;

/**
 * @author wseyler This is the job that executes when the a block out trigger fires. This job essentially does nothing
 *         more than logging the firing of the trigger.
 */
public class BlockoutAction implements IVarArgsAction {

  private volatile LogService logger;

  long duration;
  Date scheduledFireTime;

  @Override
  public void execute() throws Exception {
    Date startDate = new Date();
    long effectiveDuration = duration - ( startDate.getTime() - scheduledFireTime.getTime() );
    if ( effectiveDuration < 0 ) {
      logger.log(LogService.LOG_WARNING, "Blocking Scheduled for " + scheduledFireTime + " for " + this.duration
          + " milliseconds has already expired" );
    } else {
      logger.log(LogService.LOG_WARNING, "Blocking Started at: " + startDate + " and will last: " + effectiveDuration + " milliseconds" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
      Thread.sleep( effectiveDuration );
      logger.log(LogService.LOG_WARNING, "Blockout that started at: " + startDate + " has ended at: " + new Date() ); //$NON-NLS-1$ //$NON-NLS-2$
    }
  }

  @Override
  public void setVarArgs( Map<String, Object> args ) {
    if ( args.containsKey( IBlockoutManager.DURATION_PARAM ) ) {
      this.duration = ( (Number) args.get( IBlockoutManager.DURATION_PARAM ) ).longValue();
    }
    if ( args.containsKey( IBlockoutManager.SCHEDULED_FIRE_TIME ) ) {
      this.scheduledFireTime = ( (Date) args.get( IBlockoutManager.SCHEDULED_FIRE_TIME ) );
    }
  }

}
