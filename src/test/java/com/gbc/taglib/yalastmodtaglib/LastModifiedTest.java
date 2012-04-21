package com.gbc.taglib.yalastmodtaglib;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import org.junit.Before;
import org.junit.Test;

public class LastModifiedTest {
	
	private static final String DEFAULT_FORMAT = "yyyyMMddhhmmss";

	private static final Map<String, String> FILE_DATA;

	private LastModified lastModified;
	
	private SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_FORMAT);

	private JspWriter jspWriter;
	
	static {
		FILE_DATA = new LinkedHashMap<String, String>();
		FILE_DATA.put("/somepath/foobar.js", "/src/test/resources/foobar.js");
		FILE_DATA.put("/somepath/foobar.css", "/src/test/resources/foobar.css");
	}
	
	@Before
	public void setUp() {
	}

	private LastModified setupLastModified() {
		ServletContext servletContext = mock(ServletContext.class);
		
		for (Entry<String, String> entry : FILE_DATA.entrySet()) {
			when(servletContext.getRealPath(entry.getKey())).thenReturn(getAbsolutePath(entry.getValue()));
		}
		
		jspWriter = mock(JspWriter.class);

		PageContext pageContext = mock(PageContext.class);
		when(pageContext.getOut()).thenReturn(jspWriter);
		when(pageContext.getServletContext()).thenReturn(servletContext);

		LastModified lastModified = new LastModified();
		lastModified.setPageContext(pageContext);
		lastModified.setFormat(DEFAULT_FORMAT);
		return lastModified;
	}

	private String getAbsolutePath(String path) {
		return new File(".").getAbsolutePath() + path;
	}
	
	@Test 
	public void shouldAddProperDatesForFiles() throws JspException, IOException {

		for (Entry<String, String> fileData : FILE_DATA.entrySet()) {
			lastModified = setupLastModified();
			lastModified.setFile(fileData.getKey());
			
			String absolutePath = getAbsolutePath(fileData.getValue());
			File file = new File(absolutePath);
			
			long fileDateLastModified = file.lastModified();
			System.out.println("Last modified date of file: " + file.getName() + " should be: " + sdf.format(fileDateLastModified));
			assertEquals(6, lastModified.doEndTag());
			verify(jspWriter).write(sdf.format(fileDateLastModified));
		}
	}
	
}