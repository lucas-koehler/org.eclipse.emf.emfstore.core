/*******************************************************************************
 * Copyright (c) 2008-2011 Chair for Applied Software Engineering,
 * Technische Universitaet Muenchen.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Otto von Wesendonk - initial API and implementation
 ******************************************************************************/
package org.eclipse.emf.emfstore.internal.client.model.changeTracking.merging.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.emfstore.internal.common.observer.ESPrioritizedObserver;

/**
 * Label provider for merge.
 * 
 * @author ovonwesen
 */
public interface MergeLabelProvider extends ESPrioritizedObserver {

	/**
	 * ID of the {@link MergeLabelProvider} option.
	 */
	String ID = "org.eclipse.emf.emfstore.client.mergeLabelProvider"; //$NON-NLS-1$

	/**
	 * 
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.emfstore.internal.common.observer.ESPrioritizedObserver#getPriority()
	 */
	int getPriority();

	/**
	 * Returns a textual description of the given model element.
	 * 
	 * @param modelElement
	 *            the model element whose description is requested
	 * @return a textual description for the given model element
	 */
	String getText(EObject modelElement);

	/**
	 * Disposes the label provider.
	 */
	void dispose();
}
