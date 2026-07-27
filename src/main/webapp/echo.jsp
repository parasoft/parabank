<%
String requestContentType = request.getContentType();
response.setContentType(requestContentType != null ? requestContentType : "application/octet-stream");

java.io.InputStream inputStream = request.getInputStream();
java.io.OutputStream outputStream = response.getOutputStream();
inputStream.transferTo(outputStream);
outputStream.flush();
%>