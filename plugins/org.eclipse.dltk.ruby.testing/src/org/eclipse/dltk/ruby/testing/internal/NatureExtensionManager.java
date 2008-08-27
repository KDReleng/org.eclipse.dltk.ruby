/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.ruby.testing.internal;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;

public class NatureExtensionManager {

	private final String extensionPoint;

	private final String natureAttr = "nature"; //$NON-NLS-1$
	private final String classAttr = "class"; //$NON-NLS-1$
	private final String universalNatureId = null;
	private final Class elementType;

	/**
	 * @param extensionPoint
	 * @param elementType
	 */
	public NatureExtensionManager(String extensionPoint, Class elementType) {
		this.extensionPoint = extensionPoint;
		this.elementType = elementType;
	}

	// Contains list of instances for selected nature.
	private Map extensions;

	private synchronized void initialize() {
		if (extensions != null) {
			return;
		}

		extensions = new HashMap(5);
		IConfigurationElement[] confElements = Platform.getExtensionRegistry()
				.getConfigurationElementsFor(extensionPoint);

		for (int i = 0; i < confElements.length; i++) {
			final IConfigurationElement confElement = confElements[i];
			final String nature = confElement.getAttribute(natureAttr);
			List elements = (List) extensions.get(nature);
			if (elements == null) {
				elements = new ArrayList();
				extensions.put(nature, elements);
			}
			elements.add(createDescriptor(confElement));
		}
		for (Iterator i = extensions.values().iterator(); i.hasNext();) {
			final List descriptors = (List) i.next();
			initializeDescriptors(descriptors);
		}
	}

	/**
	 * @param natureExtensions
	 */
	protected void initializeDescriptors(List descriptors) {
		// empty
	}

	/**
	 * Return array of instances for the specified natureId. If there are no
	 * contributed instances for the specified natureId the empty array is
	 * returned.
	 * 
	 * @param natureId
	 * @return
	 * @throws CoreException
	 */
	public Object[] getInstances(String natureId) {
		initialize();
		final Object[] nature = getByNature(natureId);
		final Object[] all = universalNatureId != null ? getByNature(universalNatureId)
				: null;
		if (nature != null) {
			if (all != null) {
				final Object[] result = createArray(all.length + nature.length);
				System.arraycopy(nature, 0, result, 0, nature.length);
				System.arraycopy(all, 0, result, nature.length, all.length);
				return result;
			} else {
				return nature;
			}
		} else if (all != null) {
			return all;
		} else {
			return createEmptyResult();
		}
	}

	protected Object[] createEmptyResult() {
		return null;
	}

	private Object[] createArray(int length) {
		return (Object[]) Array.newInstance(elementType, length);
	}

	protected boolean isInstance(Object e) {
		return elementType.isAssignableFrom(e.getClass());
	}

	private Object[] getByNature(String natureId) {
		final Object ext = extensions.get(natureId);
		if (ext != null) {
			if (ext instanceof Object[]) {
				return (Object[]) ext;
			} else if (ext instanceof List) {
				final List elements = (List) ext;
				final List result = new ArrayList(elements.size());
				for (int i = 0; i < elements.size(); ++i) {
					final Object element = elements.get(i);
					if (isInstance(element)) {
						result.add(element);
					} else {
						try {
							result.add(createInstanceByDescriptor(element));
						} catch (Exception e) {
							RubyTestingPlugin
									.error(
											RubyTestingMessages.NatureExtensionManager_instanceCreateError,
											e);
						}
					}
				}
				final Object[] resultArray = createArray(result.size());
				result.toArray(resultArray);
				extensions.put(natureId, resultArray);
				return resultArray;
			}
		}
		return null;
	}

	/**
	 * @param confElement
	 * @return
	 */
	protected Object createDescriptor(IConfigurationElement confElement) {
		return confElement;
	}

	/**
	 * @param descriptor
	 * @throws CoreException
	 */
	protected Object createInstanceByDescriptor(Object descriptor)
			throws CoreException {
		final IConfigurationElement cfg = (IConfigurationElement) descriptor;
		return cfg.createExecutableExtension(classAttr);
	}
}
