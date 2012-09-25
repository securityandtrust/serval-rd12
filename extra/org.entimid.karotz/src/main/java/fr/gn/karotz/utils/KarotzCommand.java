/*
 * Copyright (c) 2011. Gregory Nain.
 */
package fr.gn.karotz.utils;

import java.net.UnknownHostException;

/**
 * Created by IntelliJ IDEA.
  * User: Gregory NAIN
 * Date: 21/05/11
 * Time: 10:04
 */
public interface KarotzCommand {

    public String getCommand() throws UnknownHostException;
}
