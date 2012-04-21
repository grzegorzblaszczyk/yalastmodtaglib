package com.gbc.taglib.yalastmodtaglib;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;

public class LastModified extends BodyTagSupport {
	
	private PageContext pageContext;
	private Tag parent;
	private String id;
	private String format;
	private String file;
	private String body;

	public LastModified() {
		id = null;
		format = null;
		file = null;
		body = null;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getFile() {
		return file;
	}
	
	public void setFile(String file) {
		this.file = file;
	}

	public String getFormat() {
		return format;
	}

	public int doStartTag() throws JspException {
		return 0;
	}

	public int doAfterBody() {
		BodyContent bodycontent = getBodyContent();
		if (bodycontent != null) {
			body = bodycontent.getString();
			bodycontent.clearBody();
		}
		return 0;
	}

	public int doEndTag() throws JspException {
		HttpServletRequest request = (HttpServletRequest) pageContext
				.getRequest();
		
		if (file == null) {
			file = "";
		} else {
			file = file.trim();
		}
		String filePath = null;
		if (file.length() == 0) {
			filePath = request.getServletPath();
		} else {
			filePath = file;
		}
		String realPath = pageContext.getServletContext().getRealPath(filePath);
		File file = new File(realPath);
		Date date = new Date(file.lastModified());
		String id = getId();
		if (id != null) {
			pageContext.setAttribute(id, date, 1);
		} else {
			try {
				String dateStr = null;
				if (format == null) {
					dateStr = date.toString();
				} else {
					SimpleDateFormat sdf = new SimpleDateFormat(format);
					dateStr = sdf.format(date);
				}
				pageContext.getOut().write(dateStr);
			} catch (Exception exception) {
				throw new JspException(exception.toString());
			}
		}
		dropData();
		return 6;
	}

	public void release() {
		dropData();
	}

	private void dropData() {
		id = null;
		format = null;
		file = null;
		body = null;
	}

	public void setPageContext(PageContext pageContext) {
		this.pageContext = pageContext;
	}

	public void setParent(Tag tag) {
		parent = tag;
	}

	public Tag getParent() {
		return parent;
	}
}
