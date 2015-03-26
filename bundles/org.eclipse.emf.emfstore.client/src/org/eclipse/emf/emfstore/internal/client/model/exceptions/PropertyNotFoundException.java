/*******************************************************************************
 * Copyright (c) 2008-2011 Chair for Applied Software Engineering,
 * Technische Universitaet Muenchen.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Aleksander Shterev - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.emfstore.internal.client.model.exceptions;

/**
 * Represents exceptional condition where an OrgUnitProperty cannot be found in project space.
 *
 * @author shterev
 */
public class PropertyNotFoundException extends Exception {

	private static final long serialVersionUID = -6818317309825518165L;

	/**
	 * Constructor.
	 *
	 * @param message
	 *            an error message
	 */
	public PropertyNotFoundException(String message) {
		super(message);
	}

}
