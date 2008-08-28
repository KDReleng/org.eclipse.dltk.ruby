/*******************************************************************************
 * Copyright (c) 2008 xored software, Inc.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     xored software, Inc. - initial API and Implementation (Alex Panchenko)
 *******************************************************************************/
package org.eclipse.dltk.ruby.testing.internal;

import org.eclipse.osgi.util.NLS;

public class RubyTestingMessages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.dltk.ruby.testing.internal.RubyTestingMessages"; //$NON-NLS-1$
	public static String Delegate_errorExtractingRunner;
	public static String Delegate_internalErrorExtractingRunner;
	public static String Delegate_runnerNotFound;
	public static String validate_notTestUnit;
	public static String validate_probablyTestUnit;
	public static String validate_runtimeError;
	public static String validate_sourceErrors;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, RubyTestingMessages.class);
	}

	private RubyTestingMessages() {
	}
}
