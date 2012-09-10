package lu.snt.serval.cloud.test;/*
* Author : Gregory Nain (developer.name@uni.lu)
* Date : 17/07/12
* (c) 2012 University of Luxembourg – Interdisciplinary Centre for Security Reliability and Trust (SnT)
* All rights reserved
*/

import lu.serializer.CloudSerializer;
import lu.serializer.ModelSerializer;
import lu.snt.serval.cloud.Cloud;
import lu.snt.serval.cloud.CloudFactory;
import lu.snt.serval.cloud.PhysicalMachine;
import org.junit.Test;

public class MyMainTest {

    @Test
    public void cloudCreationTest() {
        Cloud c = CloudFactory.createCloud();

        ModelSerializer serializer = new ModelSerializer();


    }



}
