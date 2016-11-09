/**
 * The MIT License
 * Copyright © 2010 JmxTrans team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.googlecode.jmxtrans.connections;

import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.junit.Test;

import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MBeanServerConnectionFactoryTest {

	private MBeanServerConnectionFactory factory = new MBeanServerConnectionFactory();

	@Test
	public void connectionIsCreatedForRemoteServer() throws Exception {
		JmxConnectionProvider server = mock(JmxConnectionProvider.class);
		JMXConnector jmxConnector = mock(JMXConnector.class);
		MBeanServerConnection mBeanServerConnection = mock(MBeanServerConnection.class);
		when(server.isLocal()).thenReturn(false);
		when(server.getServerConnection()).thenReturn(jmxConnector);
		when(jmxConnector.getMBeanServerConnection()).thenReturn(mBeanServerConnection);

		JMXConnection jmxConnection = factory.makeObject(server).getObject();

		assertThat(jmxConnection.getMBeanServerConnection()).isSameAs(mBeanServerConnection);
	}

	@Test
	public void connectionIsCreatedForLocalServer() throws Exception {
		JmxConnectionProvider server = mock(JmxConnectionProvider.class);
		MBeanServer mBeanServerConnection = mock(MBeanServer.class);
		when(server.isLocal()).thenReturn(true);
		when(server.getLocalMBeanServer()).thenReturn(mBeanServerConnection);

		JMXConnection jmxConnection = factory.makeObject(server).getObject();

		assertThat(jmxConnection.getMBeanServerConnection()).isSameAs(mBeanServerConnection);
	}

	@Test
	public void connectionIsClosedOnDestroy() throws Exception {
		JMXConnection connection = mock(JMXConnection.class);

		factory.destroyObject(null, new DefaultPooledObject(connection));

		verify(connection).close();
	}

}
