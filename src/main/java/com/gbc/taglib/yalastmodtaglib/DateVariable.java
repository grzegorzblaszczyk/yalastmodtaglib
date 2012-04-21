package com.gbc.taglib.yalastmodtaglib;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

public class DateVariable extends TagExtraInfo
{

    public DateVariable()
    {
    }

    public VariableInfo[] getVariableInfo(TagData tagdata)
    {
        String s = tagdata.getAttributeString("id");
        if(s == null)
        {
            return null;
        } else
        {
            VariableInfo variableinfo = new VariableInfo(s, "java.util.Date", true, 2);
            VariableInfo avariableinfo[] = {
                variableinfo
            };
            return avariableinfo;
        }
    }
}
