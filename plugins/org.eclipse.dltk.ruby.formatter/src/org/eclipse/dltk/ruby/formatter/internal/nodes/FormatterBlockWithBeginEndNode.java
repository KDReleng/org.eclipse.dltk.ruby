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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.dltk.formatter.FormatterBlockNode;
import org.eclipse.dltk.formatter.FormatterUtils;
import org.eclipse.dltk.formatter.IFormatterContext;
import org.eclipse.dltk.formatter.IFormatterDocument;
import org.eclipse.dltk.formatter.IFormatterNode;
import org.eclipse.dltk.formatter.IFormatterTextNode;
import org.eclipse.dltk.formatter.IFormatterWriter;

public abstract class FormatterBlockWithBeginEndNode extends FormatterBlockNode {

	/**
	 * @param document
	 */
	public FormatterBlockWithBeginEndNode(IFormatterDocument document) {
		super(document);
	}

	private List begin = null;
	private IFormatterTextNode end;

	public void accept(IFormatterContext context, IFormatterWriter visitor)
			throws Exception {
		context.setBlankLines(getBlankLinesBefore(context));
		if (begin != null) {
			for (Iterator i = begin.iterator(); i.hasNext();) {
				((IFormatterNode) i.next()).accept(context, visitor);
			}
		}
		context.resetBlankLines();
		final boolean indenting = isIndenting();
		if (indenting) {
			context.incIndent();
		}
		super.accept(context, visitor);
		if (indenting) {
			context.decIndent();
		}
		if (end != null) {
			visitor.write(context, end.getStartOffset(), end.getEndOffset());
		}
		context.setBlankLines(getBlankLinesAfter(context));
	}

	protected int getBlankLinesBefore(IFormatterContext context) {
		return -1;
	}

	protected int getBlankLinesAfter(IFormatterContext context) {
		return -1;
	}

	/**
	 * @return the begin
	 */
	public IFormatterNode[] getBegin() {
		return FormatterUtils.toTextNodeArray(begin);
	}

	/**
	 * @param begin
	 *            the begin to set
	 */
	public void setBegin(IFormatterTextNode begin) {
		if (this.begin == null) {
			this.begin = new ArrayList();
		}
		this.begin.add(begin);
	}

	public void insertBefore(List nodes) {
		if (this.begin == null) {
			this.begin = new ArrayList();
		}
		this.begin.addAll(0, nodes);
	}

	/**
	 * @return the end
	 */
	public IFormatterTextNode getEnd() {
		return end;
	}

	/**
	 * @param node
	 */
	public void setEnd(IFormatterTextNode node) {
		this.end = node;
	}

	/*
	 * @see
	 * org.eclipse.dltk.ruby.formatter.node.FormatterBlockNode#getStartOffset()
	 */
	public int getStartOffset() {
		if (begin != null) {
			return ((IFormatterTextNode) begin.get(0)).getStartOffset();
		}
		return super.getStartOffset();
	}

	/*
	 * @see
	 * org.eclipse.dltk.ruby.formatter.node.FormatterBlockNode#getEndOffset()
	 */
	public int getEndOffset() {
		if (end != null) {
			return end.getEndOffset();
		}
		if (!super.isEmpty()) {
			return super.getEndOffset();
		}
		if (begin != null) {
			return ((IFormatterTextNode) begin.get(begin.size() - 1))
					.getEndOffset();
		}
		return DEFAULT_OFFSET;
	}

	/*
	 * @see org.eclipse.dltk.ruby.formatter.node.FormatterBlockNode#isEmpty()
	 */
	public boolean isEmpty() {
		return begin == null && end == null && super.isEmpty();
	}

	/*
	 * @see org.eclipse.dltk.formatter.nodes.FormatterBlockNode#getChildren()
	 */
	public List getChildren() {
		if (begin == null && end == null) {
			return super.getChildren();
		} else {
			List result = new ArrayList();
			if (begin != null) {
				result.addAll(begin);
			}
			result.addAll(super.getChildren());
			if (end != null) {
				result.add(end);
			}
			return result;
		}
	}

	/*
	 * @see org.eclipse.dltk.ruby.formatter.node.FormatterBlockNode#toString()
	 */
	public String toString() {
		return begin + "\n" + super.toString() + "\n" + end; //$NON-NLS-1$ //$NON-NLS-2$
	}

}
