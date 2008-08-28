package org.eclipse.dltk.ruby.testing.internal;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.ruby.core.RubyNature;
import org.eclipse.dltk.ruby.testing.ITestingEngine;

public final class TestingEngineManager extends NatureExtensionManager {

	private static final String EXTENSION_POINT = RubyTestingPlugin.PLUGIN_ID
			+ ".engine"; //$NON-NLS-1$

	private static class Descriptor {

		final IConfigurationElement element;
		final int priority;

		/**
		 * @param confElement
		 * @param priority
		 */
		public Descriptor(IConfigurationElement confElement, int priority) {
			this.element = confElement;
			this.priority = priority;
		}

	}

	private TestingEngineManager() {
		super(EXTENSION_POINT, ITestingEngine.class);
	}

	private static final String PRIORITY_ATTR = "priority"; //$NON-NLS-1$

	protected Object createDescriptor(IConfigurationElement confElement) {
		final String strPriority = confElement.getAttribute(PRIORITY_ATTR);
		int priority = NumberUtils.toInt(strPriority);
		return new Descriptor(confElement, priority);
	}

	private final Comparator descriptorComparator = new Comparator() {

		public int compare(Object o1, Object o2) {
			Descriptor descriptor1 = (Descriptor) o1;
			Descriptor descriptor2 = (Descriptor) o2;
			return descriptor1.priority - descriptor2.priority;
		}

	};

	protected void initializeDescriptors(List descriptors) {
		Collections.sort(descriptors, descriptorComparator);
	}

	protected Object createInstanceByDescriptor(Object descriptor)
			throws CoreException {
		Descriptor engineDescriptor = (Descriptor) descriptor;
		return super.createInstanceByDescriptor(engineDescriptor.element);
	}

	protected Object[] createEmptyResult() {
		return new ITestingEngine[0];
	}

	private static TestingEngineManager instance = null;

	private static TestingEngineManager getInstance() {
		if (instance == null) {
			instance = new TestingEngineManager();
		}
		return instance;
	}

	public static ITestingEngine[] getEngines() {
		return (ITestingEngine[]) getInstance().getInstances(
				RubyNature.NATURE_ID);
	}

	/**
	 * Returns the {@link ITestingEngine} with the specified engineId or
	 * <code>null</code>.
	 * 
	 * @param engineId
	 * @return
	 */
	public static ITestingEngine getEngine(String engineId) {
		if (engineId != null) {
			final ITestingEngine[] engines = getEngines();
			for (int i = 0; i < engines.length; ++i) {
				final ITestingEngine engine = engines[i];
				if (engineId.equals(engine.getId())) {
					return engine;
				}
			}
		}
		return null;
	}

	public static TestingEngineDetection detect(ITestingEngine[] engines,
			ISourceModule module) {
		IStatus infoStatus = null;
		ITestingEngine infoEngine = null;
		for (int i = 0; i < engines.length; i++) {
			final ITestingEngine engine = engines[i];
			final IStatus status = engine.validateSourceModule(module);
			if (status != null) {
				if (status.isOK()) {
					return new TestingEngineDetection(engine, status);
				} else if (status.getSeverity() == IStatus.INFO
						&& infoStatus == null) {
					infoStatus = status;
					infoEngine = engine;
				}
			}
		}
		if (infoEngine != null) {
			return new TestingEngineDetection(infoEngine, infoStatus);
		} else {
			return null;
		}
	}

}
