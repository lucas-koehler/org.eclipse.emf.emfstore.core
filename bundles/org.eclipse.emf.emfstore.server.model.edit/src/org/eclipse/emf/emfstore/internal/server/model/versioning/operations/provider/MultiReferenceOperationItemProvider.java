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
package org.eclipse.emf.emfstore.internal.server.model.versioning.operations.provider;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;
import org.eclipse.emf.emfstore.internal.common.model.ModelFactory;
import org.eclipse.emf.emfstore.internal.server.model.versioning.operations.ContainmentType;
import org.eclipse.emf.emfstore.internal.server.model.versioning.operations.MultiReferenceOperation;
import org.eclipse.emf.emfstore.internal.server.model.versioning.operations.OperationsPackage;

/**
 * This is the item provider adapter for a
 * {@link org.eclipse.emf.emfstore.internal.server.model.versioning.operations.MultiReferenceOperation} object. <!--
 * begin-user-doc --> <!-- end-user-doc -->
 *
 * @generated
 */
public class MultiReferenceOperationItemProvider extends ReferenceOperationItemProvider {
	/**
	 * This constructs an instance from a factory and a notifier. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public MultiReferenceOperationItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * This returns the property descriptors for the adapted class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object) {
		if (itemPropertyDescriptors == null)
		{
			super.getPropertyDescriptors(object);

			addAddPropertyDescriptor(object);
			addIndexPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Add feature.
	 * <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected void addAddPropertyDescriptor(Object object) {
		itemPropertyDescriptors
		.add
		(createItemPropertyDescriptor
			(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(),
				getResourceLocator(),
				getString("_UI_MultiReferenceOperation_add_feature"), //$NON-NLS-1$
				getString(
					"_UI_PropertyDescriptor_description", "_UI_MultiReferenceOperation_add_feature", "_UI_MultiReferenceOperation_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					OperationsPackage.Literals.MULTI_REFERENCE_OPERATION__ADD,
					true,
					false,
					false,
					ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
					null,
					null));
	}

	/**
	 * This adds a property descriptor for the Index feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected void addIndexPropertyDescriptor(Object object) {
		itemPropertyDescriptors
		.add
		(createItemPropertyDescriptor
			(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(),
				getResourceLocator(),
				getString("_UI_MultiReferenceOperation_index_feature"), //$NON-NLS-1$
				getString(
					"_UI_PropertyDescriptor_description", "_UI_MultiReferenceOperation_index_feature", "_UI_MultiReferenceOperation_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					OperationsPackage.Literals.MULTI_REFERENCE_OPERATION__INDEX,
					true,
					false,
					false,
					ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
					null,
					null));
	}

	/**
	 * This specifies how to implement {@link #getChildren} and is used to deduce an appropriate feature for an
	 * {@link org.eclipse.emf.edit.command.AddCommand}, {@link org.eclipse.emf.edit.command.RemoveCommand} or
	 * {@link org.eclipse.emf.edit.command.MoveCommand} in {@link #createCommand}.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Collection<? extends EStructuralFeature> getChildrenFeatures(Object object) {
		if (childrenFeatures == null)
		{
			super.getChildrenFeatures(object);
			childrenFeatures.add(OperationsPackage.Literals.MULTI_REFERENCE_OPERATION__REFERENCED_MODEL_ELEMENTS);
		}
		return childrenFeatures;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	protected EStructuralFeature getChildFeature(Object object, Object child) {
		// Check the type of the specified child object and return the proper feature to use for
		// adding (see {@link AddCommand}) it as a child.

		return super.getChildFeature(object, child);
	}

	// begin of custom code
	/**
	 * @param object
	 *            the object
	 * @return This returns the image.
	 * @generated NOT
	 */
	@Override
	public Object getImage(Object object) {
		return super.getImage(object);
	}

	// end of custom code

	/**
	 * {@inheritDoc} This returns the label text for the adapted class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated NOT
	 */
	@Override
	public String getText(Object object) {
		if (object instanceof MultiReferenceOperation) {
			final MultiReferenceOperation op = (MultiReferenceOperation) object;

			final boolean isContainment = op.getContainmentType().equals(ContainmentType.CONTAINMENT);
			final String featureType = AbstractOperationItemProvider.REFERENCE_TYPE_TAG_SEPARATOR;

			final String elemNames = getModelElementClassesAndNames(op.getReferencedModelElements(), featureType);
			final String elementNameAndClass = getModelElementClassAndName(op.getModelElementId());
			final String children = op.getReferencedModelElements().size() > 1 ?
				Messages.MultiReferenceOperationItemProvider_Children
				: Messages.MultiReferenceOperationItemProvider_Child;

			final boolean isAdd = op.isAdd();

			if (isAdd && isContainment) {
				return MessageFormat.format(
					Messages.MultiReferenceOperationItemProvider_Add_Containment,
					elemNames, children, elementNameAndClass);
			} else if (isAdd && !isContainment) {
				return MessageFormat.format(
					Messages.MultiReferenceOperationItemProvider_Add_NonContainment,
					elemNames, op.getFeatureName(), elementNameAndClass);
			} else if (!isAdd && isContainment) {
				return MessageFormat.format(Messages.MultiReferenceOperationItemProvider_Remove_Containment,
					elemNames, children, elementNameAndClass);
			} else {
				return MessageFormat.format(Messages.MultiReferenceOperationItemProvider_Remove_NonContainment,
					elemNames, op.getFeatureName(), elementNameAndClass);
			}

		}
		return super.getText(object);
	}

	/**
	 * This handles model notifications by calling {@link #updateChildren} to update any cached
	 * children and by creating a viewer notification, which it passes to {@link #fireNotifyChanged}.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void notifyChanged(Notification notification) {
		updateChildren(notification);

		switch (notification.getFeatureID(MultiReferenceOperation.class))
		{
		case OperationsPackage.MULTI_REFERENCE_OPERATION__ADD:
		case OperationsPackage.MULTI_REFERENCE_OPERATION__INDEX:
			fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
			return;
		case OperationsPackage.MULTI_REFERENCE_OPERATION__REFERENCED_MODEL_ELEMENTS:
			fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, false));
			return;
		}
		super.notifyChanged(notification);
	}

	/**
	 * This adds {@link org.eclipse.emf.edit.command.CommandParameter}s
	 * describing the children that can be created under this object. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object) {
		super.collectNewChildDescriptors(newChildDescriptors, object);

		newChildDescriptors.add
		(createChildParameter
			(OperationsPackage.Literals.MULTI_REFERENCE_OPERATION__REFERENCED_MODEL_ELEMENTS,
				ModelFactory.eINSTANCE.createModelElementId()));
	}

}