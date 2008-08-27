package org.eclipse.dltk.ruby.testing;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.launching.InterpreterConfig;

public interface ITestingEngine {
	String getId();

	String getName();

	IStatus validateSourceModule(ISourceModule module);

	IStatus validateContainer(IModelElement element);

	void run(InterpreterConfig config, ILaunchConfiguration configuration,
			ILaunch launch) throws CoreException;
}