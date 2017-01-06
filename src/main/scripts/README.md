# Scrips
The scripts in this folder are to help during development:

1. These scripts will help by providing the values that can be added to @Resource(name="") definitions so that more precise auto-wiring can be used.  The idea here is that the use of these definitions form  `com.parasoft.parabank.util.Constants` will prevent miss-typing of names ...   

Script | Description
:--- |:---
`extract.constant.defs.sh` | Extracts the list of values for `viewName` and `formView` to generate constant references to be added to `com.parasoft.parabank.util.Constants` interface
`extract.constant.java.defs.sh` | Extracts the list of values for `viewName` and `formView` to generate constant declarations to be added to `com.parasoft.parabank.util.Constants` interface

TODO: Add the windows cmd wrappers (this will require CYGWIN)