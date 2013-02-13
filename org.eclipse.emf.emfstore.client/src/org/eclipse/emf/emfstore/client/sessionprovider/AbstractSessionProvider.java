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
package org.eclipse.emf.emfstore.client.sessionprovider;

import org.eclipse.emf.emfstore.client.ESLocalProject;
import org.eclipse.emf.emfstore.client.ESServer;
import org.eclipse.emf.emfstore.client.IUsersession;
import org.eclipse.emf.emfstore.internal.server.exceptions.EMFStoreException;

/**
 * <p>
 * This is the abstract super class for SessionProviders. All SessionProvider should extend this class. SessionProvider
 * derives a user session for a given server request (IServerCall). When overriding
 * {@link #provideUsersession(IServerCall)} , it is possible to gain more context for the {@link IUsersession}
 * selection.
 * </p>
 * <p>
 * However, in most usecases most, users will use the session provider to open a login dialog of some kind. For this
 * purpose it is better to use {@link #provideUsersession(ESServer)}. SessionProviders can be registered via an extension
 * point.<br/>
 * </p>
 * 
 * <p>
 * <b>Note</b>: Implementations of SessionProviders must not assume that they are executed within the UI-Thread.
 * </p>
 * 
 * @author wesendon
 * 
 */
public abstract class AbstractSessionProvider {

	/**
	 * <p>
	 * The SessionManager calls this method in order to obtain a user session. In its default implementation it first
	 * looks for specified user session in the {@link IServerCall}, then it checks whether the local project is
	 * associated with a user session (e.g. in case of update). If there is still no user session,
	 * {@link #provideUsersession(ESServer)} is called, which is meant to be used when implementing an UI to select a UI.
	 * </p>
	 * 
	 * <p>
	 * In most cases it is sufficient to implement {@link #provideUsersession(ESServer)}. There should be no need to
	 * change this implementation.
	 * </p>
	 * 
	 * @param serverCall
	 *            current server call
	 * @return an user session. It is not specified whether this session is logged in or logged out.
	 * 
	 * @throws EMFStoreException in case an exception occurred while obtaining the user session
	 */
	public IUsersession provideUsersession(IServerCall serverCall) throws EMFStoreException {

		IUsersession usersession = serverCall.getUsersession();

		if (usersession == null) {
			usersession = getUsersessionFromProject(serverCall.getLocalProject());
		}

		if (usersession == null) {
			usersession = provideUsersession(serverCall.getServer());
		}

		return usersession;
	}

	/**
	 * Tries to obtain a user session from a given {@link ESLocalProject}.
	 * 
	 * @param project
	 *            the local project to obtain the user session from
	 * @return the user session associated with the project or {@code null}, if no session is available
	 */
	protected IUsersession getUsersessionFromProject(ESLocalProject project) {

		if (project != null && project.getUsersession() != null) {
			return project.getUsersession();
		}

		return null;
	}

	/**
	 * <p>
	 * This is the template method for {@link #provideUsersession(ESServer)}. It is called, if the latter couldn't
	 * determine a suitable user session. Use this in order to implement a session selection UI or a headless selection
	 * logic.
	 * </p>
	 * 
	 * @param server
	 *            This parameter is a hint from the {@link ESServer}. For that reason it can be null. A common
	 *            example is share, where the user first has to select the server before logging in. If {@link ESServer}
	 *            is set you should allow the user to select the account for the given server.
	 * 
	 * @return an user session. It is not specified whether this session is logged in or logged out.
	 * @throws EMFStoreException in case an exception occurred while obtaining the user session
	 */
	public abstract IUsersession provideUsersession(ESServer server) throws EMFStoreException;

	/**
	 * This method is called by the SessionManager in order to login a given user session. Either you are able to
	 * login the given session or you should throw an exception.
	 * 
	 * @param usersession
	 *            the session to be logged in
	 * 
	 * @throws EMFStoreException in case an exception occurred while logging in the given session
	 */
	public abstract void login(IUsersession usersession) throws EMFStoreException;
}