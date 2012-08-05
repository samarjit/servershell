<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.lang.management.*"%>
<%@ page import="java.util.*"%>
<%-- <% out.println("sent to web server hello world....");	 %> --%>

<html>
<head>
<title>JVM Memory Monitor</title>
</head>

<body>



	<table border="0" width="95%">
		<tr>
			<td colspan="2">
				<table border="0" width="100%" style="border: 1px #98AAB1 solid;" 	cellpadding="5" cellspacing="5">
					<tr>
						<td colspan="2" align="center"><h3>BE - Memory MXBean</h3></td>
					</tr>
					<tr>
						<th width="20%">BE - Heap Memory Usage</th>
						<td><%=ManagementFactory.getMemoryMXBean().getHeapMemoryUsage()%>
						</td>
					</tr>

					<tr>
						<th>BE - Non-Heap Memory Usage</th>
						<td><%=ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage()%></td>
					</tr>
					<tr>
						<td colspan="2">&nbsp;</td>
					</tr>
				</table>
			</td>
		</tr>

		<tr>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td colspan="2" align="center"><h3>BE - Memory Pool MXBeans</h3></td>
		</tr>
		<%
		Iterator iter = ManagementFactory.getMemoryPoolMXBeans().iterator();
		while (iter.hasNext()) {

			MemoryPoolMXBean item = (MemoryPoolMXBean) iter.next();
		%>
		<tr>
			<td colspan="2">
				<table border="0" width="100%" style="border: 1px #98AAB1 solid;" cellpadding="5" cellspacing="5">
					<tr>
						<td colspan="2" align="center"><b>BE - <%=item.getName()%></b></td>
					</tr>
					<tr>
						<td width="20%">Type</td>
						<td><%=item.getType()%></td>
					</tr>
					<tr>
						<td>Usage</td>
						<td><%=item.getUsage()%></td>
					</tr>
					<tr>
						<td>Peak Usage</td>
						<td><%=item.getPeakUsage()%></td>
					</tr>
					<tr>
						<td>Collection Usage</td>
						<td><%=item.getCollectionUsage()%></td>
					</tr>

				</table>
			</td>
		</tr>
		<tr>
			<td colspan="2">&nbsp;</td>
		</tr>
		<%
			}
		%>

	</table>
</body>
</html>