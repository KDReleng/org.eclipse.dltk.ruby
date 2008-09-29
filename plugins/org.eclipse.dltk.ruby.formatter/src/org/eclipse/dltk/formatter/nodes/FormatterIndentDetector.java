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
package org.eclipse.dltk.formatter.nodes;

import org.eclipse.jface.text.IRegion;

public class FormatterIndentDetector implements IFormatterVisitor {

	private final int offset;
	private boolean indentDetected = false;
	private int level;

	/**
	 * @param offset
	 */
	public FormatterIndentDetector(int offset) {
		this.offset = offset;
	}

	public void addNewLineCallback(IFormatterCallback callback) {
		// empty
	}

	public void excludeRegion(IRegion region) {
		// empty

	}

	public void preVisit(IFormatterContext context, IFormatterTextNode node)
			throws Exception {
		// empty
	}

	public void visit(IFormatterContext context, IFormatterTextNode node)
			throws Exception {
		if (!indentDetected && node.getStartOffset() >= offset) {
			level = context.getIndent();
			indentDetected = true;
		}
	}

	/**
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}

}
