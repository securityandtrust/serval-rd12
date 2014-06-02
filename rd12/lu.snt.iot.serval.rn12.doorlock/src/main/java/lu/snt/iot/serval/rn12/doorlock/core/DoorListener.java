package lu.snt.iot.serval.rn12.doorlock.core;/*
* Author : Gregory Nain (developer.name@uni.lu)
* Date : 30/11/12
* (c) 2012 University of Luxembourg – Interdisciplinary Centre for Security Reliability and Trust (SnT)
* All rights reserved
*/

import lu.snt.iot.serval.rn12.doorlock.core.DoorClass;

public interface DoorListener {
  public void doorStateChanged(DoorClass door);
}