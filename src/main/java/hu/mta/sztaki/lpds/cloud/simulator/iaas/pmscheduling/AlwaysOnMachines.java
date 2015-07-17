/*
 *  ========================================================================
 *  DIScrete event baSed Energy Consumption simulaTor 
 *    					             for Clouds and Federations (DISSECT-CF)
 *  ========================================================================
 *  
 *  This file is part of DISSECT-CF.
 *  
 *  DISSECT-CF is free software: you can redistribute it and/or modify it
 *  under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or (at
 *  your option) any later version.
 *  
 *  DISSECT-CF is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser
 *  General Public License for more details.
 *  
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with DISSECT-CF.  If not, see <http://www.gnu.org/licenses/>.
 *  
 *  (C) Copyright 2014, Gabor Kecskemeti (gkecskem@dps.uibk.ac.at,
 *   									  kecskemeti.gabor@sztaki.mta.hu)
 */

package hu.mta.sztaki.lpds.cloud.simulator.iaas.pmscheduling;

import hu.mta.sztaki.lpds.cloud.simulator.iaas.IaaSService;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.PhysicalMachine;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.VMManager;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.constraints.ResourceConstraints;
import hu.mta.sztaki.lpds.cloud.simulator.iaas.vmscheduling.Scheduler.QueueingEvent;

import java.util.List;

public class AlwaysOnMachines extends PhysicalMachineController {
	public AlwaysOnMachines(final IaaSService parent) {
		super(parent);
	}

	@Override
	protected VMManager.CapacityChangeEvent<PhysicalMachine> getHostRegEvent() {
		return new VMManager.CapacityChangeEvent<PhysicalMachine>() {
			@Override
			public void capacityChanged(ResourceConstraints newCapacity,
					List<PhysicalMachine> alteredPMs) {
				final boolean newRegistration = parent
						.isRegisteredHost(alteredPMs.get(0));
				if (newRegistration) {
					final int size = alteredPMs.size();
					for (int i = 0; i < size; i++) {
						PhysicalMachine pm = alteredPMs.get(i);
						if (PhysicalMachine.ToOfforOff.contains(pm.getState())) {
							pm.turnon();
						}
					}
				}
			}
		};
	}

	@Override
	protected QueueingEvent getQueueingEvent() {
		return new QueueingEvent() {
			@Override
			public void queueingStarted() {
				// do nothing, we already have all the machines running
			}
		};
	}
}
