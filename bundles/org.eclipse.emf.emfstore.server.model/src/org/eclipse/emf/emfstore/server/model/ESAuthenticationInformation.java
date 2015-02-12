/*******************************************************************************
 * Copyright (c) 2011-2015 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Edgar Mueller - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.emfstore.server.model;

/**
 * Contains authentication information for an authenticated EMFStore user.
 *
 * @author emueller
 * @since 1.5
 *
 */
public interface ESAuthenticationInformation {

	/**
	 * Returns the authenticated user.
	 *
	 * @return the authenticated user
	 */
	ESUser getUser();

	/**
	 * Sets the authenticated user.
	 *
	 * @param user
	 *            the authenticated user
	 */
	void setUser(ESUser user);

}
