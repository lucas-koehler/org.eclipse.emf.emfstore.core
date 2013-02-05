/*******************************************************************************
 * Copyright (c) 2008-2011 Chair for Applied Software Engineering,
 * Technische Universitaet Muenchen.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 ******************************************************************************/
package org.eclipse.emf.emfstore.client.test.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.emf.emfstore.client.model.Configuration;
import org.eclipse.emf.emfstore.client.model.ProjectSpace;
import org.eclipse.emf.emfstore.client.model.Workspace;
import org.eclipse.emf.emfstore.client.model.WorkspaceProvider;
import org.eclipse.emf.emfstore.client.model.util.EMFStoreCommand;
import org.eclipse.emf.emfstore.client.test.WorkspaceTest;
import org.eclipse.emf.emfstore.client.test.testmodel.TestmodelFactory;
import org.eclipse.emf.emfstore.common.model.Project;
import org.eclipse.emf.emfstore.common.model.util.ModelUtil;
import org.junit.Test;

public class PersistenceTest extends WorkspaceTest {

	@Override
	public void beforeHook() {
		setCompareAtEnd(false);
	}

	@Test
	public void testReinitWorkspace() {
		Configuration.setAutoSave(false);
		Project originalProject = ModelUtil.clone(((Workspace) WorkspaceProvider.getInstance().getWorkspace())
			.getProjectSpaces().get(0).getProject());

		new EMFStoreCommand() {

			@Override
			protected void doRun() {
				getProject().addModelElement(TestmodelFactory.eINSTANCE.createTestElement());
			}
		}.run(false);

		assertEquals(WorkspaceProvider.getInstance().getWorkspace().getProjects().get(0).getModelElements().size(), 1);
		WorkspaceProvider.getInstance().dispose();
		WorkspaceProvider.getInstance().reinit();
		assertTrue(ModelUtil.areEqual(
			((ProjectSpace) WorkspaceProvider.getInstance().getWorkspace().getProjects().get(0)).getProject(),
			originalProject));
	}

}