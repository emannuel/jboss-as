package org.jboss.as.test.integration.ejb.iiop.naming;

import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.util.Properties;
import javax.ejb.RemoveException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assume.assumeThat;
import static org.junit.matchers.JUnitMatchers.containsString;

/**
 * @author Stuart Douglas
 */
@RunWith(Arquillian.class)
@RunAsClient
public class IIOPNamingTestCase {


    @Deployment
    public static Archive<?> deploy() {
        System.setProperty("com.sun.CORBA.ORBUseDynamicStub", "true");
        return ShrinkWrap.create(JavaArchive.class, "test.jar")
                .addPackage(IIOPNamingTestCase.class.getPackage());
    }

    private static String property(final String name) {
        return System.getProperty(name);
    }

    @Test
    public void testIIOPNamingInvocation() throws NamingException, RemoteException {
        final Properties prope = new Properties();
        prope.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.cosnaming.CNCtxFactory");
        prope.put(Context.PROVIDER_URL, "corbaloc::localhost:3528/JBoss/Naming/root");
        final InitialContext context = new InitialContext(prope);
        final Object iiopObj = context.lookup("test/IIOPNamingBean");
        final IIOPNamingHome object = (IIOPNamingHome) PortableRemoteObject.narrow(iiopObj, IIOPNamingHome.class);
        final IIOPRemote result = object.create();
        Assert.assertEquals("hello", result.hello());
    }

    @Test
    public void testStatefulIIOPNamingInvocation() throws NamingException, RemoteException, RemoveException {
        final Properties prope = new Properties();
        prope.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.cosnaming.CNCtxFactory");
        prope.put(Context.PROVIDER_URL, "corbaloc::localhost:3528/JBoss/Naming/root");
        final InitialContext context = new InitialContext(prope);
        final Object iiopObj = context.lookup("test/IIOPStatefulNamingBean");
        final IIOPStatefulNamingHome object = (IIOPStatefulNamingHome) PortableRemoteObject.narrow(iiopObj, IIOPStatefulNamingHome.class);
        final IIOPStatefulRemote result = object.create(10);
        Assert.assertEquals(11, result.increment());
        Assert.assertEquals(12, result.increment());
        result.remove();
        try {
            result.increment();
            Assert.fail("Expected NoSuchObjectException");
        } catch (NoSuchObjectException expected) {

        }
    }

    @Test
    public void testIIOPNamingCorbanameInvocation() throws NamingException, RemoteException {
        // AS7-2593: test hangs on OpenJDK
        assumeThat(property("java.runtime.name"), not(containsString("OpenJDK")));
        final Properties prope = new Properties();
        prope.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.cosnaming.CNCtxFactory");
        prope.put(Context.PROVIDER_URL, "corbaloc::localhost:3528");
        final InitialContext context = new InitialContext(prope);
        final Object iiopObj = context.lookup("corbaname:iiop:localhost:3528#test/IIOPNamingBean");
        final IIOPNamingHome object = (IIOPNamingHome) PortableRemoteObject.narrow(iiopObj, IIOPNamingHome.class);
        final IIOPRemote result = object.create();
        Assert.assertEquals("hello", result.hello());
    }

    @Test
    public void testStatefulIIOPNamingCorbanameInvocation() throws NamingException, RemoteException, RemoveException {
        final Properties prope = new Properties();
        prope.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.cosnaming.CNCtxFactory");
        prope.put(Context.PROVIDER_URL, "corbaloc::localhost:3528");
        final InitialContext context = new InitialContext(prope);
        final Object iiopObj = context.lookup("test/IIOPStatefulNamingBean");
        final IIOPStatefulNamingHome object = (IIOPStatefulNamingHome) PortableRemoteObject.narrow(iiopObj, IIOPStatefulNamingHome.class);
        final IIOPStatefulRemote result = object.create(10);
        Assert.assertEquals(11, result.increment());
        Assert.assertEquals(12, result.increment());
        result.remove();
        try {
            result.increment();
            Assert.fail("Expected NoSuchObjectException");
        } catch (NoSuchObjectException expected) {

        }
    }

    @Test
    public void testIIOPNamingIIOPInvocation() throws NamingException, RemoteException {
        final Properties prope = new Properties();
        prope.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.cosnaming.CNCtxFactory");
        prope.put(Context.PROVIDER_URL, "iiop://localhost:3528");
        final InitialContext context = new InitialContext(prope);
        final Object iiopObj = context.lookup("test/IIOPNamingBean");
        final IIOPNamingHome object = (IIOPNamingHome) PortableRemoteObject.narrow(iiopObj, IIOPNamingHome.class);
        final IIOPRemote result = object.create();
        Assert.assertEquals("hello", result.hello());
    }

    @Test
    public void testStatefulIIOPNamingIIOPInvocation() throws NamingException, RemoteException, RemoveException {
        final Properties prope = new Properties();
        prope.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.cosnaming.CNCtxFactory");
        prope.put(Context.PROVIDER_URL, "iiop://localhost:3528");
        final InitialContext context = new InitialContext(prope);
        final Object iiopObj = context.lookup("test/IIOPStatefulNamingBean");
        final IIOPStatefulNamingHome object = (IIOPStatefulNamingHome) PortableRemoteObject.narrow(iiopObj, IIOPStatefulNamingHome.class);
        final IIOPStatefulRemote result = object.create(10);
        Assert.assertEquals(11, result.increment());
        Assert.assertEquals(12, result.increment());
        result.remove();
        try {
            result.increment();
            Assert.fail("Expected NoSuchObjectException");
        } catch (NoSuchObjectException expected) {
        }
    }
}
