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
package org.eclipse.dltk.ruby.formatter.tests;

import junit.framework.TestSuite;

public class ClassesAndMethodsTest extends AbstractFormatterTest {

	public static TestSuite suite() {
		return createScriptedSuite(ClassesAndMethodsTest.class.getName(),
				"scripts/classes-n-methods.rb");
	}

}
