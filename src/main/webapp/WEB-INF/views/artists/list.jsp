<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
  <head>
    <title>Artists</title>
  </head>
  <body>
    <h1>Artists</h1>
    <a href="${pageContext.request.contextPath}/artists/new">Add New Artist</a>
    <table border="1" cellpadding="5" cellspacing="0">
      <tr>
        <th>Name</th>
        <th>Real Name</th>
        <th>Contact Info</th>
        <th>URL</th>
        <th>Actions</th>
      </tr>
      <c:forEach var="a" items="${artists}">
        <tr>
          <td><c:out value="${a.name}" /></td>
          <td><c:out value="${a.realName}" /></td>
          <td><c:out value="${a.contactInfo}" /></td>
          <td>
            <c:choose>
              <c:when test="${not empty a.url}">
                <a href="${a.url}" target="_blank"><c:out value="${a.url}" /></a>
              </c:when>
              <c:otherwise>—</c:otherwise>
            </c:choose>
          </td>
          <td>
            <a href="${pageContext.request.contextPath}/artists/edit?id=${a.id}">Edit</a>
             |
            <form action="${pageContext.request.contextPath}/artists/delete"
                  method="post" style="display:inline">
              <input type="hidden" name="id" value="${a.id}" />
              <button type="submit">Delete</button>
            </form>
          </td>
        </tr>
      </c:forEach>
    </table>
  </body>
</html>