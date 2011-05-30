/**
 * Copyright (c) 2008-2011 Sonatype, Inc.
 * All rights reserved. Includes the third-party code listed at http://www.sonatype.com/products/nexus/attributions.
 *
 * This program is free software: you can redistribute it and/or modify it only under the terms of the GNU Affero General
 * Public License Version 3 as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License Version 3
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License Version 3 along with this program.  If not, see
 * http://www.gnu.org/licenses.
 *
 * Sonatype Nexus (TM) Open Source Version is available from Sonatype, Inc. Sonatype and Sonatype Nexus are trademarks of
 * Sonatype, Inc. Apache Maven is a trademark of the Apache Foundation. M2Eclipse is a trademark of the Eclipse Foundation.
 * All other trademarks are the property of their respective owners.
 */
package org.sonatype.nexus.test;

import java.io.File;
import java.io.InputStream;
import org.codehaus.plexus.ContainerConfiguration;
import org.codehaus.plexus.DefaultContainerConfiguration;
import org.codehaus.plexus.DefaultPlexusContainer;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.PlexusContainerException;
import org.codehaus.plexus.configuration.PlexusConfiguration;
import org.codehaus.plexus.context.Context;
import org.codehaus.plexus.context.DefaultContext;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

/**
 * A Support PlexusTestCase clone that does not extend JUnit TestCase, thereby allowing us to extend
 * this class like we did with JUnit 3x and use JUnit 4x annotations instead to design our tests.
 * <p>
 * This source is meant to be a near copy of the original {@link org.codehaus.plexus.PlexusTestCase}, sisu-2.1.1
 * <p>
 * The supporting asserts derived from JUnit's Assert class are deprecated here to encourage use of
 * the more modern alternative Hamcrest libraries.
 * <p>
 * TODO: integrate this directly with sisu-inject-plexus
 */
public abstract class PlexusTestCaseSupport {
    private PlexusContainer container;

    private static String basedir;

    protected void setUp()
        throws Exception
    {
        basedir = getBasedir();
    }

    protected void setupContainer()
    {
        // ----------------------------------------------------------------------------
        // Context Setup
        // ----------------------------------------------------------------------------

        final DefaultContext context = new DefaultContext();

        context.put( "basedir", getBasedir() );

        customizeContext( context );

        final boolean hasPlexusHome = context.contains( "plexus.home" );

        if ( !hasPlexusHome )
        {
            final File f = getTestFile( "target/plexus-home" );

            if ( !f.isDirectory() )
            {
                f.mkdir();
            }

            context.put( "plexus.home", f.getAbsolutePath() );
        }

        // ----------------------------------------------------------------------------
        // Configuration
        // ----------------------------------------------------------------------------

        final String config = getCustomConfigurationName();

        final ContainerConfiguration containerConfiguration =
            new DefaultContainerConfiguration().setName( "test" ).setContext( context.getContextData() );

        if ( config != null )
        {
            containerConfiguration.setContainerConfiguration( config );
        }
        else
        {
            final String resource = getConfigurationName( null );

            containerConfiguration.setContainerConfiguration( resource );
        }

        customizeContainerConfiguration( containerConfiguration );

        try
        {
            container = new DefaultPlexusContainer( containerConfiguration );
        }
        catch ( final PlexusContainerException e )
        {
            e.printStackTrace();
            fail( "Failed to create plexus container." );
        }
    }

    /**
     * Allow custom test case implementations do augment the default container configuration before executing tests.
     *
     * @param containerConfiguration
     */
    protected void customizeContainerConfiguration( final ContainerConfiguration containerConfiguration )
    {
    }

    protected void customizeContext( final Context context )
    {
    }

    protected PlexusConfiguration customizeComponentConfiguration()
    {
        return null;
    }

    protected void tearDown()
        throws Exception
    {
        if ( container != null )
        {
            container.dispose();

            container = null;
        }
    }

    protected PlexusContainer getContainer()
    {
        if ( container == null )
        {
            setupContainer();
        }

        return container;
    }

    protected InputStream getConfiguration()
        throws Exception
    {
        return getConfiguration( null );
    }

    protected InputStream getConfiguration( final String subname )
        throws Exception
    {
        return getResourceAsStream( getConfigurationName( subname ) );
    }

    protected String getCustomConfigurationName()
    {
        return null;
    }

    /**
     * Allow the retrieval of a container configuration that is based on the name of the test class being run. So if you
     * have a test class called org.foo.FunTest, then this will produce a resource name of org/foo/FunTest.xml which
     * would be used to configure the Plexus container before running your test.
     *
     * @param subname
     * @return
     */
    protected String getConfigurationName( final String subname )
    {
        return getClass().getName().replace( '.', '/' ) + ".xml";
    }

    protected InputStream getResourceAsStream( final String resource )
    {
        return getClass().getResourceAsStream( resource );
    }

    protected ClassLoader getClassLoader()
    {
        return getClass().getClassLoader();
    }

    // ----------------------------------------------------------------------
    // Container access
    // ----------------------------------------------------------------------

    protected Object lookup( final String componentKey )
        throws Exception
    {
        return getContainer().lookup( componentKey );
    }

    protected Object lookup( final String role, final String roleHint )
        throws Exception
    {
        return getContainer().lookup( role, roleHint );
    }

    protected <T> T lookup( final Class<T> componentClass )
        throws Exception
    {
        return getContainer().lookup( componentClass );
    }

    protected <T> T lookup( final Class<T> componentClass, final String roleHint )
        throws Exception
    {
        return getContainer().lookup( componentClass, roleHint );
    }

    protected void release( final Object component )
        throws Exception
    {
        getContainer().release( component );
    }

    // ----------------------------------------------------------------------
    // Helper methods for sub classes
    // ----------------------------------------------------------------------

    public static File getTestFile( final String path )
    {
        return new File( getBasedir(), path );
    }

    public static File getTestFile( final String basedir, final String path )
    {
        File basedirFile = new File( basedir );

        if ( !basedirFile.isAbsolute() )
        {
            basedirFile = getTestFile( basedir );
        }

        return new File( basedirFile, path );
    }

    public static String getTestPath( final String path )
    {
        return getTestFile( path ).getAbsolutePath();
    }

    public static String getTestPath( final String basedir, final String path )
    {
        return getTestFile( basedir, path ).getAbsolutePath();
    }

    public static String getBasedir()
    {
        if ( basedir != null )
        {
            return basedir;
        }

        basedir = System.getProperty( "basedir" );

        if ( basedir == null )
        {
            basedir = new File( "" ).getAbsolutePath();
        }

        return basedir;
    }

    public String getTestConfiguration()
    {
        return getTestConfiguration( getClass() );
    }

    public static String getTestConfiguration( final Class<?> clazz )
    {
        final String s = clazz.getName().replace( '.', '/' );

        return s.substring( 0, s.indexOf( "$" ) ) + ".xml";
    }

    // ========================= CUSTOM NEXUS =====================
    /**
     * Helper to call old JUnit 3x style {@link #setUp()}
     * @throws Exception
     */
    @Before
    final public void setUpJunit() throws Exception {
        setUp();
    }

    /**
     * Helper to call old JUnit 3x style {@link #tearDown()}
     * @throws Exception
     */
    @After
    final public void tearDownJunit() throws Exception {
        tearDown();
    }

    /**
     *
     * @deprecated Use {@link org.hamcrest.Assert#fail()} directly instead.
     */
    @Deprecated
    protected void fail() {
        Assert.fail();
    }

    /**
     *
     * @deprecated Use {@link org.hamcrest.Assert#fail(java.lang.String)} directly instead.
     */
    @Deprecated
    protected void fail(String message) {
        Assert.fail(message);
    }

    /**
     *
     * @deprecated Use {@link org.hamcrest.MatcherAssert} directly instead.
     */
    protected void assertTrue(boolean condition) {
        Assert.assertTrue(condition);
    }

    /**
     *
     * @deprecated Use {@link org.hamcrest.MatcherAssert} directly instead.
     */
    protected void assertTrue(String message, boolean condition) {
        Assert.assertTrue(message, condition);
    }

    /**
     *
     * @deprecated Use {@link org.hamcrest.MatcherAssert} directly instead.
     */
    protected void assertFalse(boolean condition) {
        Assert.assertFalse(condition);
    }

    /**
     *
     * @deprecated Use {@link org.hamcrest.MatcherAssert} directly instead.
     */
    protected void assertFalse(String message, boolean condition) {
        Assert.assertFalse(message, condition);
    }

    /**
     *
     * @deprecated Use {@link org.hamcrest.MatcherAssert} directly instead.
     */
    protected void assertNotNull(Object obj) {
        Assert.assertNotNull( obj );
    }

    /**
     *
     * @deprecated Use {@link org.hamcrest.MatcherAssert} directly instead.
     */
    protected void assertNotNull(String message, Object obj) {
        Assert.assertNotNull(message, obj);
    }

    /**
     *
     * @deprecated Use {@link org.hamcrest.MatcherAssert} directly instead.
     */
    protected void assertNull(Object obj) {
        Assert.assertNull( obj );
    }

    /**
     *
     * @deprecated Use {@link org.hamcrest.MatcherAssert} directly instead.
     */
    protected void assertNull(String message, Object obj) {
        Assert.assertNull(message, obj );
    }

    /**
     *
     * @deprecated Use {@link org.hamcrest.MatcherAssert} directly instead.
     */
    protected void assertEquals(String message, Object expected, Object actual) {
        // don't use junit framework Assert due to autoboxing bug
        MatcherAssert.assertThat(message, actual, Matchers.equalTo( expected ));

    }

    /**
     *
     * @deprecated Use {@link org.hamcrest.MatcherAssert} directly instead.
     */
    protected void assertEquals(Object expected, Object actual) {
        // don't use junit framework Assert
        MatcherAssert.assertThat( actual, Matchers.equalTo( expected ));
    }
}
