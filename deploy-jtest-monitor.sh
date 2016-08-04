#!/bin/bash

if [ -f "set-vars.sh" ]; then
 
	source set-vars.sh

	echo ===================================================================
	echo "Deploying Jtest Application Coverage Monitor for build.id="$BUILD_ID
	echo ===================================================================

	echo **1/2** Cleaning up old coverage data
	rm -f $APP_COVERAGE_DIR/monitor/runtime_coverage/runtime_coverage*

	echo **2/2** Copy and unzip monitor package
	cp target/jtest/monitor/monitor.zip $APP_COVERAGE_DIR

	pushd $APP_COVERAGE_DIR
	unzip -o monitor.zip
	popd

	echo ===================================================================
	echo "Finished deploying Jtest Application Coverage Monitor for build.id="$BUILD_ID
	echo ===================================================================

else
	echo "Incorrect usage ... running script in wrong directory, cannot find setvars.sh"
fi
