<%@ include file="../include/include.jsp"%>


<object id="TestProject" width="1000" height="700"
	codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab">
	<param name="movie" value="<c:url value="/flash/FlashApp.swf"/>" />
	<param name="quality" value="high" />
	<param name="allowScriptAccess" value="sameDomain" />
	<embed src="<c:url value="/flash/FlashApp.swf"/>" quality="high"
		width="1000" height="700" name="ParabankPortfolioFlashApp"
		allowScriptAccess="sameDomain" type="application/x-shockwave-flash"
		pluginspage="http://www.macromedia.com/go/getflashplayer">
	</embed>
</object>
