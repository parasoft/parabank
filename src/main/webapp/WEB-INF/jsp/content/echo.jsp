<%@page import="java.io.Reader"%><%
response.setContentType(request.getContentType());
StringBuffer builder = new java.lang.StringBuffer();
java.io.Reader reader = request.getReader();
int c = -1;
while ((c = reader.read()) != -1) {
    builder.append((char) c);
}
%><%=builder.toString()%>