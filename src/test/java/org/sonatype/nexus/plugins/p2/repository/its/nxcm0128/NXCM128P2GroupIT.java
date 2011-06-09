/**
 * Copyright (c) 2008-2011 Sonatype, Inc.
 *
 * All rights reserved. Includes the third-party code listed at http://www.sonatype.com/products/nexus/attributions.
 * Sonatype and Sonatype Nexus are trademarks of Sonatype, Inc. Apache Maven is a trademark of the Apache Foundation.
 * M2Eclipse is a trademark of the Eclipse Foundation. All other trademarks are the property of their respective owners.
 */
package org.sonatype.nexus.plugins.p2.repository.its.nxcm0128;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;
import org.sonatype.nexus.plugins.p2.repository.its.AbstractNexusProxyP2IntegrationIT;

public class NXCM128P2GroupIT
    extends AbstractNexusProxyP2IntegrationIT
{

    @Test
    public void make()
        throws Exception
    {
        final File installDir = new File( "target/eclipse/nxcm0128" );

        installUsingP2( getGroupUrl( "p2group" ), "com.sonatype.nexus.p2.its.feature2.feature.group",
            installDir.getCanonicalPath() );

        final File feature = new File( installDir, "features/com.sonatype.nexus.p2.its.feature_1.0.0" );
        Assert.assertTrue( feature.exists() && feature.isDirectory() );

        final File feature2 = new File( installDir, "features/com.sonatype.nexus.p2.its.feature2_1.0.0" );
        Assert.assertTrue( feature2.exists() && feature2.isDirectory() );

        final File bundle = new File( installDir, "plugins/com.sonatype.nexus.p2.its.bundle_1.0.0.jar" );
        Assert.assertTrue( bundle.canRead() );
    }

}
