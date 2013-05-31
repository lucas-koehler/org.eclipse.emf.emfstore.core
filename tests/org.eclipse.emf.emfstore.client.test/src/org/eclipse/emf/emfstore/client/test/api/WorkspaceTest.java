package org.eclipse.emf.emfstore.client.test.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.emfstore.client.ESLocalProject;
import org.eclipse.emf.emfstore.client.ESServer;
import org.eclipse.emf.emfstore.client.ESWorkspace;
import org.eclipse.emf.emfstore.client.ESWorkspaceProvider;
import org.eclipse.emf.emfstore.internal.client.model.connectionmanager.KeyStoreManager;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class WorkspaceTest {

	private static ESWorkspace workspace;
	private ESLocalProject localProject;

	public static int port = 8080;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		workspace = ESWorkspaceProvider.INSTANCE.getWorkspace();
	}

	@Before
	public void setUp() throws Exception {
		assertEquals(0, workspace.getLocalProjects().size());
		localProject = workspace.createLocalProject("TestProject");
	}

	@After
	public void tearDown() throws Exception {
		for (ESLocalProject lp : workspace.getLocalProjects())
			lp.delete(new NullProgressMonitor());
	}

	@Test
	public void testCreateLocalProject() {
		assertNotNull(localProject);
		assertEquals(1, workspace.getLocalProjects().size());
		workspace.createLocalProject("TestProject2");
		assertEquals(2, workspace.getLocalProjects().size());
	}

	@Test
	public void testAddExistingServer() {
		ESServer server = ESServer.FACTORY.getServer("localhost", port, KeyStoreManager.DEFAULT_CERTIFICATE);
		workspace.addServer(server);
		int servers = workspace.getServers().size();
		server = ESServer.FACTORY.getServer("localhost", port, KeyStoreManager.DEFAULT_CERTIFICATE);
		workspace.addServer(server);
		assertEquals(servers, workspace.getServers().size());

		workspace.removeServer(server);
		assertEquals(servers, workspace.getServers().size() + 1);
	}

	@Test
	public void testAddServer() {
		int servers = workspace.getServers().size();
		ESServer server = ESServer.FACTORY.getServer("foo.net", 1234, KeyStoreManager.DEFAULT_CERTIFICATE);
		workspace.addServer(server);
		assertEquals(servers + 1, workspace.getServers().size());
		workspace.removeServer(server);
		assertEquals(servers, workspace.getServers().size());
	}

}
