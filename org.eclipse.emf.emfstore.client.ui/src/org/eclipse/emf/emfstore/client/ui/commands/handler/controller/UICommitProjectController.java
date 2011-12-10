package org.eclipse.emf.emfstore.client.ui.commands.handler.controller;

import org.eclipse.emf.emfstore.client.model.ProjectSpace;
import org.eclipse.emf.emfstore.client.model.controller.callbacks.CommitCallback;
import org.eclipse.emf.emfstore.client.ui.commands.handler.AbstractEMFStoreUIController;
import org.eclipse.emf.emfstore.client.ui.dialogs.CommitDialog;
import org.eclipse.emf.emfstore.server.model.versioning.ChangePackage;
import org.eclipse.emf.emfstore.server.model.versioning.LogMessage;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

public class UICommitProjectController extends AbstractEMFStoreUIController implements CommitCallback {

	public UICommitProjectController(Shell shell) {
		super(shell);
	}

	public void commit(ProjectSpace projectSpace) {
		commit(projectSpace, null);
	}

	public void commit(ProjectSpace projectSpace, LogMessage logMessage) {
		openProgress();
		projectSpace.commit(logMessage, this, getProgressMonitor());
	}

	public void noLocalChanges(ProjectSpace projectSpace) {
		MessageDialog.openInformation(getShell(), null, "No local changes in your project. No need to commit.");
		closeProgress();
	}

	public boolean baseVersionOutOfDate(ProjectSpace projectSpace) {
		String message = "Your project is outdated, you need to update before commit. Do you want to update now?";
		if (confirmationDialog(message)) {
			// TODO results?
			new UIUpdateProjectController(getShell()).update(projectSpace);
			return true;
		}
		closeProgress();
		return false;
	}

	public boolean inspectChanges(ProjectSpace projectSpace, ChangePackage changePackage) {
		if (changePackage.getOperations().isEmpty()) {
			MessageDialog.openInformation(getShell(), "No local changes",
				"Your local changes were mutually exclusive.\nThey are no changes pending for commit.");
			return false;
		}
		CommitDialog commitDialog = new CommitDialog(getShell(), changePackage, projectSpace);
		// if (predefinedCommitMessage != null) {
		// if (changePackage.getLogMessage() == null) {
		// changePackage.setLogMessage(logMessage);
		// }
		// changePackage.getLogMessage().setMessage(predefinedCommitMessage);
		// }
		if (commitDialog.open() == Dialog.OK) {
			// logMessage.setAuthor(usersession.getUsername());
			// logMessage.setClientDate(new Date());
			// logMessage.setMessage(commitDialog.getLogText());
			return true;
		}
		return false;
	}
}
