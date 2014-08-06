/*******************************************************************************
 * Copyright (c) 2008-2011 Chair for Applied Software Engineering,
 * Technische Universitaet Muenchen.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * chodnick
 ******************************************************************************/
package org.eclipse.emf.emfstore.internal.client.model.changeTracking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.emfstore.internal.client.model.util.WorkspaceUtil;
import org.eclipse.emf.emfstore.internal.common.model.IdEObjectCollection;
import org.eclipse.emf.emfstore.internal.common.model.ModelElementId;
import org.eclipse.emf.emfstore.internal.common.model.impl.IdEObjectCollectionImpl;
import org.eclipse.emf.emfstore.internal.common.model.impl.ProjectImpl;
import org.eclipse.emf.emfstore.internal.common.model.util.IdEObjectCollectionChangeObserver;
import org.eclipse.emf.emfstore.internal.common.model.util.ModelUtil;
import org.eclipse.emf.emfstore.internal.common.model.util.NotificationInfo;
import org.eclipse.emf.emfstore.internal.server.model.versioning.operations.AbstractOperation;
import org.eclipse.emf.emfstore.internal.server.model.versioning.operations.AttributeOperation;
import org.eclipse.emf.emfstore.internal.server.model.versioning.operations.ContainmentType;
import org.eclipse.emf.emfstore.internal.server.model.versioning.operations.FeatureOperation;
import org.eclipse.emf.emfstore.internal.server.model.versioning.operations.MultiAttributeMoveOperation;
import org.eclipse.emf.emfstore.internal.server.model.versioning.operations.MultiAttributeOperation;
import org.eclipse.emf.emfstore.internal.server.model.versioning.operations.MultiAttributeSetOperation;
import org.eclipse.emf.emfstore.internal.server.model.versioning.operations.MultiReferenceMoveOperation;
import org.eclipse.emf.emfstore.internal.server.model.versioning.operations.MultiReferenceOperation;
import org.eclipse.emf.emfstore.internal.server.model.versioning.operations.MultiReferenceSetOperation;
import org.eclipse.emf.emfstore.internal.server.model.versioning.operations.OperationsFactory;
import org.eclipse.emf.emfstore.internal.server.model.versioning.operations.ReferenceOperation;
import org.eclipse.emf.emfstore.internal.server.model.versioning.operations.SingleReferenceOperation;
import org.eclipse.emf.emfstore.internal.server.model.versioning.operations.UnsetType;

/**
 * Converts an EMF notification to an Operation.
 * 
 * @author chodnick
 */
public final class NotificationToOperationConverter implements IdEObjectCollectionChangeObserver {

	private final IdEObjectCollectionImpl project;
	public Set<EObject> added = new LinkedHashSet<EObject>();
	public Set<EObject> removed = new LinkedHashSet<EObject>();

	private final Map<Tuple<ModelElementId, EStructuralFeature>, SingleReferenceOperation> opps =
		new LinkedHashMap<Tuple<ModelElementId, EStructuralFeature>, SingleReferenceOperation>();
	private final Set<EObject> justAdded = new LinkedHashSet<EObject>();
	private final Set<EObject> processed = new LinkedHashSet<EObject>();;

	/**
	 * Default constructor.
	 * 
	 * @param project
	 *            project
	 */
	public NotificationToOperationConverter(IdEObjectCollectionImpl project) {
		this.project = project;
	}

	/**
	 * Converts given notification to an operation. May return null if the
	 * notification signifies a no-op.
	 * 
	 * @param n
	 *            the notification to convert
	 * @return the operation or null
	 */
	// BEGIN COMPLEX CODE
	public List<AbstractOperation> convert(NotificationInfo n) {

		if (n.isTouch() || n.isTransient() || !n.isValid()) {
			return null;
		}

		final List<AbstractOperation> ops = new ArrayList<AbstractOperation>();

		switch (n.getEventType()) {

		case Notification.SET:

			if (processed.contains(n.getNotifierModelElement())) {
				processed.remove(n.getNotifierModelElement());
				return Collections.emptyList();
			}

			if (added.contains(n.getNotifierModelElement())) {
				added.remove(n.getNotifierModelElement());
				if (n.isAttributeNotification()) {
					added.clear();
					return Collections.singletonList(handleSetAttribute(n));
				}
				return Collections.singletonList(handleSetReference(n));
				// return Collections.emptyList();
			}

			if (removed.contains(n.getNotifierModelElement())) {

				boolean isAddFollowing = false;
				try {
					isAddFollowing = n.isAddFollowing((EReference) n.getFeature());
				} catch (final SecurityException ex) {
					System.out.println();
				} catch (final IllegalArgumentException ex) {
					System.out.println();
				} catch (final NoSuchFieldException ex) {
					System.out.println();
				} catch (final IllegalAccessException ex) {
					System.out.println();
				}

				// must be remvoe
				if (!isAddFollowing) {
					// let add() updated removed
					removed.remove(n.getNotifierModelElement());
					final EReference r = (EReference) n.getFeature();
					ModelElementId modelElementId = project.getModelElementId(n.getNotifierModelElement());
					if (modelElementId == null) {
						modelElementId = project.getDeletedModelElementId(n.getNotifierModelElement());
					}
					final Tuple<ModelElementId, EStructuralFeature> tuple = new Tuple<ModelElementId, EStructuralFeature>(
						modelElementId, r);
					final SingleReferenceOperation singleReferenceOperation = opps.get(tuple);
					if (singleReferenceOperation != null) {
						opps.remove(tuple);
						ops.add(singleReferenceOperation);
					} else {
						if (n.isAttributeNotification()) {
							added.clear();
							return Collections.singletonList(handleSetAttribute(n));
						}

						return Collections.singletonList(handleSetReference(n));
					}
					return ops;
				}
				return Collections.EMPTY_LIST;
			}

			if (n.isAttributeNotification()) {
				return Collections.singletonList(handleSetAttribute(n));
			}

			return Collections.singletonList(handleSetReference(n));

		case Notification.UNSET:
			if (n.isAttributeNotification()) {
				added.clear();
				return Collections.singletonList(handleUnsetAttribute(n));
			}
			added.clear();
			return Collections.singletonList(handleUnsetReference(n));

		case Notification.ADD:
			if (n.isAttributeNotification()) {
				added.clear();
				return Collections.singletonList(handleMultiAttribute(n));
			}

			final AbstractOperation add = add(n.getReference(), n.getNotifierModelElement(),
				n.getNewModelElementValue());
			if (add != null) {
				ops.add(add);
			}

			ops.add(handleMultiReference(n));

			return ops;

		case Notification.ADD_MANY:
			if (n.isAttributeNotification()) {
				added.clear();
				return Collections.singletonList(handleMultiAttribute(n));
			}
			added.clear();

			ops.addAll(addMany(n, ops));
			ops.add(handleMultiReference(n));

			return ops;

		case Notification.REMOVE:
			if (n.isAttributeNotification()) {
				added.clear();
				return Collections.singletonList(handleMultiAttribute(n));
			}

			remove(n, ops);
			// else {
			// ops.add(createSingleReferenceOperation(project,
			// oldModelElementId2,
			// null,
			// ref2.getEOpposite(),
			// n.getOldModelElementValue()));
			// removed.add(n.getOldModelElementValue());
			// }

			// added.clear();
			ops.add(handleMultiReference(n));
			return ops;

		case Notification.REMOVE_MANY:
			if (n.isAttributeNotification()) {
				added.clear();
				return Collections.singletonList(handleMultiAttribute(n));
			}
			added.clear();

			removeMany(n);

			return Collections.singletonList(handleMultiReference(n));

		case Notification.MOVE:
			if (n.isAttributeNotification()) {
				added.clear();
				return Collections.singletonList(handleAttributeMove(n));
			}
			added.clear();
			return Collections.singletonList(handleReferenceMove(n));

		default:
			added.clear();
			return null;
		}
	}

	/**
	 * @param n
	 */
	private void remove(NotificationInfo n, List<AbstractOperation> ops) {
		final EReference ref2 = (EReference) n.getFeature();

		ModelElementId oldModelElementId3 = project.getModelElementId(n.getOldModelElementValue());

		if (oldModelElementId3 == null) {
			oldModelElementId3 = project.getDeletedModelElementId(n.getOldModelElementValue());
		}

		final ModelElementId oldModelElementId2 = project.getModelElementId(n.getNotifierModelElement());

		if (ref2.getEOpposite() != null) {
			final SingleReferenceOperation singleReferenceOperation = createSingleReferenceOperation(project,
				oldModelElementId2,
				null,
				ref2.getEOpposite(),
				n.getOldModelElementValue());
			if (n.hasNext()) {
				// move
				opps.put(new Tuple<ModelElementId, EStructuralFeature>(oldModelElementId3, ref2.getEOpposite()),
					singleReferenceOperation);
				removed.add(n.getOldModelElementValue());
			} else {
				// let SET notification handle it
			}
		}
	}

	/**
	 * @param n
	 */
	private void removeMany(NotificationInfo n) {
		final EReference ref2 = (EReference) n.getFeature();

		final List<? extends EObject> oldModelElementValue = (List<? extends EObject>) n.getOldValue();

		for (final EObject oldValue : oldModelElementValue) {
			ModelElementId oldModelElementId3 = project.getModelElementId(oldValue);

			if (oldModelElementId3 == null) {
				oldModelElementId3 = project.getDeletedModelElementId(oldValue);
			}

			final ModelElementId oldModelElementId2 = project.getModelElementId(n.getNotifierModelElement());

			if (ref2.getEOpposite() != null) {
				final SingleReferenceOperation singleReferenceOperation = createSingleReferenceOperation(project,
					oldModelElementId2,
					null,
					ref2.getEOpposite(),
					oldValue);
				opps.put(new Tuple<ModelElementId, EStructuralFeature>(oldModelElementId3, ref2.getEOpposite()),
					singleReferenceOperation);
				removed.add(oldValue);
			}
		}
	}

	/**
	 * @param n
	 * @param ops
	 */
	private AbstractOperation add(EReference ref, EObject notifier, EObject newValue) {

		final ModelElementId modelElementId = project.getModelElementId(notifier);

		final ModelElementId newModelElementId = ((ProjectImpl) project).getModelElementId(newValue);

		final Tuple tuple = new Tuple(newModelElementId, ref.getEOpposite());

		if (ref.getEOpposite() != null) {
			// MOVE SAME FEATURE
			if (opps.containsKey(tuple)) {

				final SingleReferenceOperation singleReferenceOperation = opps.get(tuple);
				opps.remove(tuple);
				singleReferenceOperation.setNewValue(modelElementId);
				// do not emit set anymore
				removed.remove(newValue);
				processed.add(newValue);
				return singleReferenceOperation;
			}

			// NEW ELEMENT?
			if (justAdded.contains(newValue)) {
				// do not emit single reference in case model element has just been added
				justAdded.remove(newValue);
				added.add(newValue);
			}

			// OTHER FEATUER
			if (removed.contains(newValue)) {
				removed.remove(newValue);
				final Set<Map.Entry<Tuple<ModelElementId, EStructuralFeature>, SingleReferenceOperation>> entrySet =
					opps
						.entrySet();
				Tuple<ModelElementId, EStructuralFeature> key = null;
				for (final Map.Entry<Tuple<ModelElementId, EStructuralFeature>, SingleReferenceOperation> entry : entrySet) {
					if (entry.getKey().x.equals(newModelElementId) && ref.getEOpposite().isContainer()
						&& ((EReference) entry.getKey().y).isContainer()) {
						key = entry.getKey();
						return entry.getValue();
					}
				}
				opps.remove(key);
			}
		}

		return null;
	}

	private List<AbstractOperation> addMany(NotificationInfo n, final List<AbstractOperation> ops) {

		final List<AbstractOperation> o = new ArrayList<AbstractOperation>();
		final EReference ref = (EReference) n.getFeature();
		final ModelElementId modelElementId = project.getModelElementId(n.getNotifierModelElement());

		final List<? extends EObject> newValues = (List<? extends EObject>) n.getNewValue();

		for (final EObject newValue : newValues) {
			final ModelElementId newModelElementId = ((ProjectImpl) project).getModelElementId(newValue);
			final AbstractOperation possibleSingleRef = add(ref, n.getNotifierModelElement(), newValue);
			if (possibleSingleRef != null) {
				o.add(possibleSingleRef);
			}
		}

		return o;
	}

	// END COMPLEX CODE

	@SuppressWarnings("unchecked")
	private AbstractOperation handleMultiAttribute(NotificationInfo n) {
		final MultiAttributeOperation operation = OperationsFactory.eINSTANCE.createMultiAttributeOperation();
		setCommonValues(project, operation, n.getNotifierModelElement());
		operation.setFeatureName(n.getAttribute().getName());
		operation.setAdd(n.isAddEvent() || n.isAddManyEvent());
		// operation.setIndex(n.getPosition());

		List<Object> list = null;

		switch (n.getEventType()) {

		case Notification.ADD:
			list = new ArrayList<Object>();
			operation.getIndexes().add(n.getPosition());
			list.add(n.getNewValue());
			break;
		case Notification.ADD_MANY:
			list = (List<Object>) n.getNewValue();
			for (int i = 0; i < list.size(); i++) {
				operation.getIndexes().add(n.getPosition() + i);
			}
			break;
		case Notification.REMOVE:
			list = new ArrayList<Object>();
			operation.getIndexes().add(n.getPosition());
			list.add(n.getOldValue());
			break;
		case Notification.REMOVE_MANY:
			list = (List<Object>) n.getOldValue();
			if (n.getNewValue() == null) {
				for (int i = 0; i < list.size(); i++) {
					operation.getIndexes().add(i);
				}
			} else {
				for (final int value : (int[]) n.getNewValue()) {
					operation.getIndexes().add(value);
				}
			}
			break;
		default:
			break;
		}

		if (list != null) {
			for (final Object valueElement : list) {
				operation.getReferencedValues().add(valueElement);
			}
		}

		if (n.wasUnset()) {
			operation.setUnset(UnsetType.WAS_UNSET);
		}

		return operation;
	}

	@SuppressWarnings("unchecked")
	private AbstractOperation handleMultiReference(NotificationInfo n) {

		List<EObject> list = new ArrayList<EObject>();

		switch (n.getEventType()) {
		case Notification.ADD:
			list.add(n.getNewModelElementValue());
			break;
		case Notification.ADD_MANY:
			list = (List<EObject>) n.getNewValue();
			break;
		case Notification.REMOVE:
			list.add(n.getOldModelElementValue());
			break;
		case Notification.REMOVE_MANY:
			list = (List<EObject>) n.getOldValue();
			break;
		default:
			break;
		}

		final boolean isAdd = n.isAddEvent() || n.isAddManyEvent();
		final MultiReferenceOperation multiRefOp = createMultiReferenceOperation(project, n.getNotifierModelElement(),
			n.getReference(), list, isAdd,
			n.getPosition());

		if (n.wasUnset()) {
			multiRefOp.setUnset(UnsetType.WAS_UNSET);
		}

		return multiRefOp;
	}

	/**
	 * Creates a multi reference operation based on the given information.
	 * 
	 * @param collection
	 *            the collection the <code>modelElement</code> is contained in
	 * @param modelElement
	 *            the model element holding the reference
	 * @param reference
	 *            the actual reference
	 * @param referencedElements
	 *            the elements referenced by the reference
	 * @param isAdd
	 *            whether any referenced model elements were added to the <code>collection</code>
	 * @param position
	 *            the index of the model element within the <code>referenceElements</code> affected by
	 *            the generated operation
	 * @return a multi reference operation
	 */
	public static MultiReferenceOperation createMultiReferenceOperation(IdEObjectCollectionImpl collection,
		EObject modelElement, EReference reference, List<EObject> referencedElements, boolean isAdd, int position) {
		final MultiReferenceOperation op = OperationsFactory.eINSTANCE.createMultiReferenceOperation();
		setCommonValues(collection, op, modelElement);
		setBidirectionalAndContainmentInfo(op, reference);
		op.setFeatureName(reference.getName());
		op.setAdd(isAdd);
		op.setIndex(position);
		final List<ModelElementId> referencedModelElements = op.getReferencedModelElements();

		for (final EObject valueElement : referencedElements) {
			ModelElementId id = collection.getModelElementId(valueElement);
			if (id == null) {
				id = collection.getDeletedModelElementId(valueElement);
			}
			if (id != null) {
				referencedModelElements.add(id);
			} else if (ModelUtil.getProject(valueElement) == collection) {
				throw new IllegalStateException(
					Messages.NotificationToOperationConverter_Element_Has_No_ID + valueElement);
			}
			// ignore value elements outside of the current project, they are
			// not tracked
		}
		return op;

	}

	private AbstractOperation handleReferenceMove(NotificationInfo n) {

		final MultiReferenceMoveOperation op = OperationsFactory.eINSTANCE.createMultiReferenceMoveOperation();
		setCommonValues(project, op, n.getNotifierModelElement());
		op.setFeatureName(n.getReference().getName());
		op.setReferencedModelElementId(project.getModelElementId(n.getNewModelElementValue()));
		op.setNewIndex(n.getPosition());
		op.setOldIndex((Integer) n.getOldValue());

		return op;
	}

	private AbstractOperation handleAttributeMove(NotificationInfo n) {
		final MultiAttributeMoveOperation operation = OperationsFactory.eINSTANCE.createMultiAttributeMoveOperation();
		setCommonValues(project, operation, n.getNotifierModelElement());
		operation.setFeatureName(n.getAttribute().getName());
		operation.setNewIndex(n.getPosition());
		operation.setOldIndex((Integer) n.getOldValue());
		operation.setReferencedValue(n.getNewValue());
		return operation;
	}

	private AbstractOperation handleSetAttribute(NotificationInfo n) {

		if (!n.getAttribute().isMany()) {
			AttributeOperation op = null;
			// special handling for diagram layout changes
			op = OperationsFactory.eINSTANCE.createAttributeOperation();

			setCommonValues(project, op, n.getNotifierModelElement());
			op.setFeatureName(n.getAttribute().getName());
			op.setNewValue(n.getNewValue());
			op.setOldValue(n.getOldValue());

			if (n.wasUnset()) {
				op.setUnset(UnsetType.WAS_UNSET);
			}
			return op;
		}
		final MultiAttributeSetOperation setOperation = OperationsFactory.eINSTANCE
			.createMultiAttributeSetOperation();
		setCommonValues(project, setOperation, n.getNotifierModelElement());
		setOperation.setFeatureName(n.getAttribute().getName());
		setOperation.setNewValue(n.getNewValue());
		setOperation.setOldValue(n.getOldValue());
		setOperation.setIndex(n.getPosition());

		if (n.wasUnset()) {
			setOperation.setUnset(UnsetType.WAS_UNSET);
		}

		return setOperation;
	}

	/**
	 * Creates a single reference operation based on the given information.
	 * 
	 * @param collection
	 *            the collection the <code>modelElement</code> is contained in
	 * @param oldReference
	 *            the {@link ModelElementId} of the model element the reference was pointing to
	 * @param newReference
	 *            the {@link ModelElementId} of the model element the reference is now pointing to
	 * @param reference
	 *            the actual reference
	 * @param modelElement
	 *            the model element holding the reference
	 * @return a single reference operation
	 */
	public static SingleReferenceOperation createSingleReferenceOperation(IdEObjectCollectionImpl collection,
		ModelElementId oldReference, ModelElementId newReference, EReference reference, EObject modelElement) {

		final SingleReferenceOperation op = OperationsFactory.eINSTANCE.createSingleReferenceOperation();
		setCommonValues(collection, op, modelElement);
		op.setFeatureName(reference.getName());
		setBidirectionalAndContainmentInfo(op, reference);

		op.setOldValue(oldReference);
		op.setNewValue(newReference);

		return op;
	}

	private AbstractOperation handleSetReference(NotificationInfo n) {

		ModelElementId oldModelElementId = project.getModelElementId(n.getOldModelElementValue());
		ModelElementId newModelElementId = project.getModelElementId(n.getNewModelElementValue());

		if (oldModelElementId == null) {
			oldModelElementId = ((ProjectImpl) project).getDeletedModelElementId(n.getOldModelElementValue());
		}

		if (newModelElementId == null) {
			newModelElementId = ((ProjectImpl) project).getDeletedModelElementId(n.getNewModelElementValue());
		}

		if (!n.getReference().isMany()) {
			final SingleReferenceOperation singleRefOperation = createSingleReferenceOperation(project,
				oldModelElementId,
				newModelElementId, n.getReference(),
				n.getNotifierModelElement());

			if (n.wasUnset()) {
				singleRefOperation.setUnset(UnsetType.WAS_UNSET);
			}

			return singleRefOperation;

		}
		final MultiReferenceSetOperation setOperation = OperationsFactory.eINSTANCE
			.createMultiReferenceSetOperation();
		setCommonValues(project, setOperation, (EObject) n.getNotifier());
		setOperation.setFeatureName(n.getReference().getName());
		setBidirectionalAndContainmentInfo(setOperation, n.getReference());

		setOperation.setIndex(n.getPosition());

		if (n.getOldValue() != null) {
			setOperation.setOldValue(oldModelElementId);
		}

		if (n.getNewValue() != null) {
			setOperation.setNewValue(newModelElementId);
		}

		if (n.wasUnset()) {
			setOperation.setUnset(UnsetType.WAS_UNSET);
		}

		return setOperation;
	}

	// utility methods
	private static void setCommonValues(IdEObjectCollectionImpl collection, AbstractOperation operation,
		EObject modelElement) {
		operation.setClientDate(new Date());
		ModelElementId id = collection.getModelElementId(modelElement);
		if (id == null) {
			id = collection.getDeletedModelElementId(modelElement);
		}
		if (id == null) {
			WorkspaceUtil.handleException(new IllegalStateException(
				Messages.NotificationToOperationConverter_Element_Has_No_ID
					+ modelElement));
		}
		operation.setModelElementId(id);
	}

	private static void setBidirectionalAndContainmentInfo(ReferenceOperation referenceOperation, EReference reference) {
		if (reference.getEOpposite() != null) {
			referenceOperation.setBidirectional(true);
			referenceOperation.setOppositeFeatureName(reference.getEOpposite().getName());
		} else {
			referenceOperation.setBidirectional(false);
		}
		if (reference.isContainer()) {
			referenceOperation.setContainmentType(ContainmentType.CONTAINER);
		}
		if (reference.isContainment()) {
			referenceOperation.setContainmentType(ContainmentType.CONTAINMENT);
		}
	}

	private AbstractOperation handleUnsetAttribute(NotificationInfo n) {
		final FeatureOperation op = (FeatureOperation) handleSetAttribute(n);
		op.setUnset(UnsetType.IS_UNSET);
		return op;
	}

	private AbstractOperation handleUnsetReference(NotificationInfo n) {
		FeatureOperation op;
		if (!n.getReference().isMany()) {
			op = (FeatureOperation) handleSetReference(n);
		} else {
			op = (FeatureOperation) handleMultiReference(n);
		}
		op.setUnset(UnsetType.IS_UNSET);
		return op;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.emfstore.internal.common.model.util.IdEObjectCollectionChangeObserver#notify(org.eclipse.emf.common.notify.Notification,
	 *      org.eclipse.emf.emfstore.internal.common.model.IdEObjectCollection, org.eclipse.emf.ecore.EObject)
	 */
	public void notify(Notification notification, IdEObjectCollection collection, EObject modelElement) {
		// TODO Auto-generated method stub

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.emfstore.internal.common.model.util.IdEObjectCollectionChangeObserver#modelElementAdded(org.eclipse.emf.emfstore.internal.common.model.IdEObjectCollection,
	 *      org.eclipse.emf.ecore.EObject)
	 */
	public void modelElementAdded(IdEObjectCollection collection, EObject eObject) {
		justAdded.add(eObject);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.emfstore.internal.common.model.util.IdEObjectCollectionChangeObserver#modelElementRemoved(org.eclipse.emf.emfstore.internal.common.model.IdEObjectCollection,
	 *      org.eclipse.emf.ecore.EObject)
	 */
	public void modelElementRemoved(IdEObjectCollection collection, EObject eObject) {
		removed.add(eObject);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.emfstore.internal.common.model.util.IdEObjectCollectionChangeObserver#collectionDeleted(org.eclipse.emf.emfstore.internal.common.model.IdEObjectCollection)
	 */
	public void collectionDeleted(IdEObjectCollection collection) {
		// TODO Auto-generated method stub

	}

}
