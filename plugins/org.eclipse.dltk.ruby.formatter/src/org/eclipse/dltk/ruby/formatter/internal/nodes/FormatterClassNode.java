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
package org.eclipse.dltk.ruby.formatter.internal.nodes;

import org.eclipse.dltk.formatter.nodes.FormatterBlockWithBeginEndNode;
import org.eclipse.dltk.formatter.nodes.IFormatterCommentableNode;
import org.eclipse.dltk.formatter.nodes.IFormatterContext;
import org.eclipse.dltk.formatter.nodes.IFormatterDocument;
import org.eclipse.dltk.ruby.formatter.RubyFormatterConstants;

public class FormatterClassNode extends FormatterBlockWithBeginEndNode
		implements IFormatterCommentableNode {

	/**
	 * @param document
	 */
	public FormatterClassNode(IFormatterDocument document) {
		super(document);
	}

	protected boolean isIndenting() {
		return getDocument().getBoolean(RubyFormatterConstants.INDENT_CLASS);
	}

	protected int getBlankLinesBefore(IFormatterContext context) {
		if (context.getParent() == null) {
			return getInt(RubyFormatterConstants.LINES_FILE_BETWEEN_CLASS);
		} else if (context.getChildIndex() == 0) {
			return getInt(RubyFormatterConstants.LINES_BEFORE_FIRST);
		} else {
			return getInt(RubyFormatterConstants.LINES_BEFORE_CLASS);
		}
	}

	protected int getBlankLinesAfter(IFormatterContext context) {
		if (context.getParent() == null) {
			return getInt(RubyFormatterConstants.LINES_FILE_BETWEEN_CLASS);
		} else {
			return super.getBlankLinesAfter(context);
		}
	}

}
