#!/bin/bash
pwd
cd ../webapp/WEB-INF/
cat parabank-servlet.xml | grep -E "formView|viewName" | cut -d'=' -f 3 | sed -e 's/[\"\/\>]//g' | sed -e 's/^\([^ ]*\)[ ]*$/\<util:constant id=\"\1\" static-field="com.parasoft.parabank.util.Constants.\U\1\E" \/\>/g'
echo 
cat parabank-servlet.xml | grep -E "commandName" | cut -d'=' -f 3 | sed -e 's/[\"\/\>]//g' | sed -e 's/^\([^ ]*\)[ ]*$/\<util:constant id=\"\1\" static-field="com.parasoft.parabank.util.Constants.\U\1\E" \/\>/g'
echo 
cat parabank-servlet.xml | grep -E "commandClass" | cut -d'=' -f 3 | sed -e 's/[\"\/\>]//g' | sed -e 's/\(.*\(form\.\|domain\.\)\)\([^ ]*\)/<util:constant id="class\3" static-field="com.parasoft.parabank.util.Constants.CLASS_\U\3\E" \/>/'