/*!
 * This program is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License, version 2.1 as published by the Free Software
 * Foundation.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * program; if not, you can obtain a copy at http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 * or from the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * Copyright (c) 2002-2013 Pentaho Corporation..  All rights reserved.
 */

package org.pentaho.platform.scheduler3.blockout;

import java.util.ArrayList;
import java.util.List;

import org.pentaho.platform.api.scheduler3.IBlockoutManager;
import org.pentaho.platform.api.scheduler3.IJobFilter;
import org.pentaho.platform.api.scheduler3.IJobTrigger;
import org.pentaho.platform.api.scheduler3.IScheduler;
import org.pentaho.platform.api.scheduler3.Job;
import org.pentaho.platform.api.scheduler3.SchedulerException;

public class PentahoBlockoutManager implements IBlockoutManager {

  private IScheduler scheduler;

  public PentahoBlockoutManager() {
  }
  
  public PentahoBlockoutManager(IScheduler scheduler) {
	  this.scheduler = scheduler;
  }

  @Override
  public IJobTrigger getBlockOut( String blockOutJobId ) {
    try {
      Job blockOutJob = this.scheduler.getJob( blockOutJobId );
      IJobTrigger blockOutJobTrigger = blockOutJob.getJobTrigger();
      blockOutJobTrigger.setDuration( ( (Number) blockOutJob.getJobParams().get( DURATION_PARAM ) ).longValue() );
      return blockOutJobTrigger;
    } catch ( SchedulerException e ) {
      throw new RuntimeException( e );
    }
  }

  @Override
  public List<Job> getBlockOutJobs() {
    try {
      List<Job> jobs = scheduler.getJobs( new IJobFilter() {
        public boolean accept( Job job ) {
          if ( BLOCK_OUT_JOB_NAME.equals( job.getJobName() ) ) {
            job.getJobTrigger().setDuration( ( (Number) job.getJobParams().get( DURATION_PARAM ) ).longValue() );
            return true;
          }
          return false;
        }
      } );
      return jobs;

    } catch ( SchedulerException e ) {
      throw new RuntimeException( e );
    }

  }

  @Override
  public boolean willFire( IJobTrigger scheduleTrigger ) {

    return BlockoutManagerUtil.willFire( this, scheduleTrigger, getBlockOutJobTriggers(), this.scheduler );
  }

  @Override
  public boolean shouldFireNow() {
    return BlockoutManagerUtil.shouldFireNow( this, getBlockOutJobTriggers(), this.scheduler );
  }

  @Override
  public List<IJobTrigger> willBlockSchedules( IJobTrigger testBlockOutJobTrigger ) {
    List<IJobTrigger> blockedSchedules = new ArrayList<IJobTrigger>();

    List<Job> scheduledJobs = new ArrayList<Job>();
    try {
      scheduledJobs = this.scheduler.getJobs( new IJobFilter() {

        @Override
        public boolean accept( Job job ) {
          return !BLOCK_OUT_JOB_NAME.equals( job.getJobName() );
        }
      } );
    } catch ( SchedulerException e ) {
      throw new RuntimeException( e );
    }

    // Loop over trigger group names
    for ( Job scheduledJob : scheduledJobs ) {

      // Add schedule to list if block out conflicts at all
      if ( BlockoutManagerUtil.willBlockSchedule( this, scheduledJob.getJobTrigger(),
              testBlockOutJobTrigger, this.scheduler ) ) {
        blockedSchedules.add( scheduledJob.getJobTrigger() );
      }
    }

    return blockedSchedules;
  }

  @Override
  public boolean isPartiallyBlocked( IJobTrigger scheduleJobTrigger ) {
    return BlockoutManagerUtil.isPartiallyBlocked( this, scheduleJobTrigger, getBlockOutJobTriggers(), this.scheduler );
  }

  private List<IJobTrigger> getBlockOutJobTriggers() {
    List<IJobTrigger> blockOutJobTriggers = new ArrayList<IJobTrigger>();

    for ( Job blockOutJob : getBlockOutJobs() ) {
      blockOutJobTriggers.add( blockOutJob.getJobTrigger() );
    }

    return blockOutJobTriggers;
  }

}
