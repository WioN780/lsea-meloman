<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html><body>
  <h1>Genres</h1>
  <a href="${pageContext.request.contextPath}/genres/new">Add New Genre</a>
  <table border="1">
    <tr><th>Name</th><th>Actions</th></tr>
    <c:forEach var="g" items="${genres}">
      <tr>
        <td><c:out value="${g.name}"/></td>
        <td>
          <a href="${pageContext.request.contextPath}/genres/edit?id=${g.id}">Edit</a>
          <form action="${pageContext.request.contextPath}/genres/delete"
                method="post" style="display:inline">
            <input type="hidden" name="id" value="${g.id}" />
            <button type="submit">Delete</button>
          </form>
        </td>
      </tr>
    </c:forEach>
  </table>
</body></html>