/**
 * Copyright (c) 2012-2013 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Edgar Mueller - initial API and implementation
 */
package org.eclipse.emf.emfstore.test.provider;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.edit.provider.ChangeNotifier;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IChangeNotifier;
import org.eclipse.emf.edit.provider.IDisposable;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.INotifyChangedListener;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.emfstore.test.model.util.TestmodelAdapterFactory;

/**
 * This is the factory that is used to provide the interfaces needed to support Viewers.
 * The adapters generated by this factory convert EMF adapter notifications into calls to {@link #fireNotifyChanged
 * fireNotifyChanged}.
 * The adapters also support Eclipse property sheets.
 * Note that most of the adapters are shared among multiple instances.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class TestmodelItemProviderAdapterFactory extends TestmodelAdapterFactory implements ComposeableAdapterFactory,
	IChangeNotifier, IDisposable
{
	/**
	 * This keeps track of the root adapter factory that delegates to this adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected ComposedAdapterFactory parentAdapterFactory;

	/**
	 * This is used to implement {@link org.eclipse.emf.edit.provider.IChangeNotifier}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected IChangeNotifier changeNotifier = new ChangeNotifier();

	/**
	 * This keeps track of all the supported types checked by {@link #isFactoryForType isFactoryForType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected Collection<Object> supportedTypes = new ArrayList<Object>();

	/**
	 * This constructs an instance.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public TestmodelItemProviderAdapterFactory()
	{
		supportedTypes.add(IEditingDomainItemProvider.class);
		supportedTypes.add(IStructuredItemContentProvider.class);
		supportedTypes.add(ITreeItemContentProvider.class);
		supportedTypes.add(IItemLabelProvider.class);
		supportedTypes.add(IItemPropertySource.class);
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.eclipse.emf.emfstore.test.model.TestElement}
	 * instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected TestElementItemProvider testElementItemProvider;

	/**
	 * This creates an adapter for a {@link org.eclipse.emf.emfstore.test.model.TestElement}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Adapter createTestElementAdapter()
	{
		if (testElementItemProvider == null)
		{
			testElementItemProvider = new TestElementItemProvider(this);
		}

		return testElementItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link java.util.Map.Entry} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected TestElementToStringMapItemProvider testElementToStringMapItemProvider;

	/**
	 * This creates an adapter for a {@link java.util.Map.Entry}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Adapter createTestElementToStringMapAdapter()
	{
		if (testElementToStringMapItemProvider == null)
		{
			testElementToStringMapItemProvider = new TestElementToStringMapItemProvider(this);
		}

		return testElementToStringMapItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link java.util.Map.Entry} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected StringToStringMapItemProvider stringToStringMapItemProvider;

	/**
	 * This creates an adapter for a {@link java.util.Map.Entry}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Adapter createStringToStringMapAdapter()
	{
		if (stringToStringMapItemProvider == null)
		{
			stringToStringMapItemProvider = new StringToStringMapItemProvider(this);
		}

		return stringToStringMapItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link java.util.Map.Entry} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected TestElementToTestElementMapItemProvider testElementToTestElementMapItemProvider;

	/**
	 * This creates an adapter for a {@link java.util.Map.Entry}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Adapter createTestElementToTestElementMapAdapter()
	{
		if (testElementToTestElementMapItemProvider == null)
		{
			testElementToTestElementMapItemProvider = new TestElementToTestElementMapItemProvider(this);
		}

		return testElementToTestElementMapItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link java.util.Map.Entry} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected StringToTestElementMapItemProvider stringToTestElementMapItemProvider;

	/**
	 * This creates an adapter for a {@link java.util.Map.Entry}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Adapter createStringToTestElementMapAdapter()
	{
		if (stringToTestElementMapItemProvider == null)
		{
			stringToTestElementMapItemProvider = new StringToTestElementMapItemProvider(this);
		}

		return stringToTestElementMapItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.eclipse.emf.emfstore.test.model.TestType} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected TestTypeItemProvider testTypeItemProvider;

	/**
	 * This creates an adapter for a {@link org.eclipse.emf.emfstore.test.model.TestType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Adapter createTestTypeAdapter()
	{
		if (testTypeItemProvider == null)
		{
			testTypeItemProvider = new TestTypeItemProvider(this);
		}

		return testTypeItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all
	 * {@link org.eclipse.emf.emfstore.test.model.TypeWithFeatureMapNonContainment} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected TypeWithFeatureMapNonContainmentItemProvider typeWithFeatureMapNonContainmentItemProvider;

	/**
	 * This creates an adapter for a {@link org.eclipse.emf.emfstore.test.model.TypeWithFeatureMapNonContainment}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Adapter createTypeWithFeatureMapNonContainmentAdapter()
	{
		if (typeWithFeatureMapNonContainmentItemProvider == null)
		{
			typeWithFeatureMapNonContainmentItemProvider = new TypeWithFeatureMapNonContainmentItemProvider(this);
		}

		return typeWithFeatureMapNonContainmentItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all
	 * {@link org.eclipse.emf.emfstore.test.model.TypeWithFeatureMapContainment} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected TypeWithFeatureMapContainmentItemProvider typeWithFeatureMapContainmentItemProvider;

	/**
	 * This creates an adapter for a {@link org.eclipse.emf.emfstore.test.model.TypeWithFeatureMapContainment}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Adapter createTypeWithFeatureMapContainmentAdapter()
	{
		if (typeWithFeatureMapContainmentItemProvider == null)
		{
			typeWithFeatureMapContainmentItemProvider = new TypeWithFeatureMapContainmentItemProvider(this);
		}

		return typeWithFeatureMapContainmentItemProvider;
	}

	/**
	 * This returns the root adapter factory that contains this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public ComposeableAdapterFactory getRootAdapterFactory()
	{
		return parentAdapterFactory == null ? this : parentAdapterFactory.getRootAdapterFactory();
	}

	/**
	 * This sets the composed adapter factory that contains this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public void setParentAdapterFactory(ComposedAdapterFactory parentAdapterFactory)
	{
		this.parentAdapterFactory = parentAdapterFactory;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object type)
	{
		return supportedTypes.contains(type) || super.isFactoryForType(type);
	}

	/**
	 * This implementation substitutes the factory itself as the key for the adapter.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Adapter adapt(Notifier notifier, Object type)
	{
		return super.adapt(notifier, this);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Object adapt(Object object, Object type)
	{
		if (isFactoryForType(type))
		{
			final Object adapter = super.adapt(object, type);
			if (!(type instanceof Class<?>) || ((Class<?>) type).isInstance(adapter))
			{
				return adapter;
			}
		}

		return null;
	}

	/**
	 * This adds a listener.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public void addListener(INotifyChangedListener notifyChangedListener)
	{
		changeNotifier.addListener(notifyChangedListener);
	}

	/**
	 * This removes a listener.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public void removeListener(INotifyChangedListener notifyChangedListener)
	{
		changeNotifier.removeListener(notifyChangedListener);
	}

	/**
	 * This delegates to {@link #changeNotifier} and to {@link #parentAdapterFactory}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public void fireNotifyChanged(Notification notification)
	{
		changeNotifier.fireNotifyChanged(notification);

		if (parentAdapterFactory != null)
		{
			parentAdapterFactory.fireNotifyChanged(notification);
		}
	}

	/**
	 * This disposes all of the item providers created by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public void dispose()
	{
		if (testElementItemProvider != null) {
			testElementItemProvider.dispose();
		}
		if (testElementToStringMapItemProvider != null) {
			testElementToStringMapItemProvider.dispose();
		}
		if (stringToStringMapItemProvider != null) {
			stringToStringMapItemProvider.dispose();
		}
		if (testElementToTestElementMapItemProvider != null) {
			testElementToTestElementMapItemProvider.dispose();
		}
		if (stringToTestElementMapItemProvider != null) {
			stringToTestElementMapItemProvider.dispose();
		}
		if (testTypeItemProvider != null) {
			testTypeItemProvider.dispose();
		}
		if (typeWithFeatureMapNonContainmentItemProvider != null) {
			typeWithFeatureMapNonContainmentItemProvider.dispose();
		}
		if (typeWithFeatureMapContainmentItemProvider != null) {
			typeWithFeatureMapContainmentItemProvider.dispose();
		}
	}

}
