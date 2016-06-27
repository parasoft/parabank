#!/bin/bash
cd ../webapp/WEB-INF/
cat parabank-servlet.xml | grep -E "formView|viewName" | cut -d'=' -f 3 | sed -e 's/[\"\/\>]//g' | sed -e 's/^\([^ ]*\)[ ]*$/static final String \U\1\E = "\1";/g'
echo 
cat parabank-servlet.xml | grep -E "commandName" | cut -d'=' -f 3 | sed -e 's/[\"\/\>]//g' | sed -e 's/^\([^ ]*\)[ ]*$/\static final String \U\1\E = "\1";/g'
echo 
cat parabank-servlet.xml | grep -E "commandClass" | cut -d'=' -f 3 | sed -e 's/[\"\/\>]//g' | sed -e 's/\(.*\(form\.\|domain\.\)\)\([^ ]*\)/static final String CLASS_\U\3\E = "\1\3";/'