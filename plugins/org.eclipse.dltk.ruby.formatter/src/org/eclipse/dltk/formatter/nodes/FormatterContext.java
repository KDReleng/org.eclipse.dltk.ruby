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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.dltk.ruby.formatter.internal.RubyFormatterPlugin;
import org.eclipse.dltk.ruby.formatter.internal.nodes.FormatterRequireNode;

public class FormatterContext implements IFormatterContext, Cloneable {

	private static class PathEntry {
		final IFormatterNode node;
		int childIndex = 0;

		/**
		 * @param node
		 */
		public PathEntry(IFormatterNode node) {
			this.node = node;
		}

	}

	private int indent;
	private boolean indenting = true;
	private int blankLines = 0;
	private final List path = new ArrayList();

	public FormatterContext() {
		this(0);
	}

	public FormatterContext(int indent) {
		this.indent = indent;
	}

	/*
	 * @see org.eclipse.dltk.ruby.formatter.node.IFormatterContext#copy()
	 */
	public IFormatterContext copy() {
		try {
			return (FormatterContext) clone();
		} catch (CloneNotSupportedException e) {
			RubyFormatterPlugin.error("FormatterContext.copy() error", e);
			throw new IllegalStateException();
		}
	}

	/*
	 * @see org.eclipse.dltk.ruby.formatter.node.IFormatterContext#decIndent()
	 */
	public void decIndent() {
		--indent;
		assert indent >= 0;
	}

	/*
	 * @see org.eclipse.dltk.ruby.formatter.node.IFormatterContext#incIndent()
	 */
	public void incIndent() {
		++indent;
	}

	/*
	 * @see org.eclipse.dltk.ruby.formatter.node.IFormatterContext#resetIndent()
	 */
	public void resetIndent() {
		indent = 0;
	}

	/*
	 * @see org.eclipse.dltk.ruby.formatter.node.IFormatterContext#getIndent()
	 */
	public int getIndent() {
		return indent;
	}

	public boolean isIndenting() {
		return indenting;
	}

	public void setIndenting(boolean value) {
		this.indenting = value;
	}

	public int getBlankLines() {
		return blankLines;
	}

	public void resetBlankLines() {
		blankLines = -1;
	}

	public void setBlankLines(int value) {
		if (value >= 0 && value > blankLines) {
			blankLines = value;
		}
	}

	public void enter(IFormatterNode node) {
		path.add(new PathEntry(node));
	}

	public void leave(IFormatterNode node) {
		final PathEntry entry = (PathEntry) path.remove(path.size() - 1);
		if (entry.node != node) {
			throw new IllegalStateException("leave() - node mismatch");
		}
		if (!path.isEmpty() && isCountable(node)) {
			final PathEntry parent = (PathEntry) path.get(path.size() - 1);
			++parent.childIndex;
		}
	}

	private boolean isCountable(IFormatterNode node) {
		return node instanceof IFormatterContainerNode
				|| node instanceof FormatterRequireNode;
	}

	public IFormatterNode getParent() {
		if (path.size() > 1) {
			final PathEntry entry = (PathEntry) path.get(path.size() - 2);
			return entry.node;
		} else {
			return null;
		}
	}

	public int getChildIndex() {
		if (path.size() > 1) {
			final PathEntry entry = (PathEntry) path.get(path.size() - 2);
			return entry.childIndex;
		} else {
			return -1;
		}
	}

}
