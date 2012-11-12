/**
 * Sonatype Nexus (TM) Open Source Version
 * Copyright (c) 2007-2012 Sonatype, Inc.
 * All rights reserved. Includes the third-party code listed at http://links.sonatype.com/products/nexus/oss/attributions.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License Version 1.0,
 * which accompanies this distribution and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Sonatype Nexus (TM) Professional Version is available from Sonatype, Inc. "Sonatype" and "Sonatype Nexus" are trademarks
 * of Sonatype, Inc. Apache Maven is a trademark of the Apache Software Foundation. M2eclipse is a trademark of the
 * Eclipse Foundation. All other trademarks are the property of their respective owners.
 */
package org.sonatype.nexus.repository.yum.internal.task;

import java.io.IOException;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class CommandLineExecutor
{

    private static final Logger LOG = LoggerFactory.getLogger( CommandLineExecutor.class );

    public int exec( String command )
        throws IOException
    {
        LOG.debug( "Execute command : {}", command );

        CommandLine cmdLine = CommandLine.parse( command );
        DefaultExecutor executor = new DefaultExecutor();
        executor.setStreamHandler( new PumpStreamHandler() );

        int exitValue = executor.execute( cmdLine );
        LOG.debug( "Execution finished with exit code : {}", exitValue );
        return exitValue;
    }
}
