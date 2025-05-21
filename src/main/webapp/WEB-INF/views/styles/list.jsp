<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html><body>
  <h1>Styles</h1>
  <a href="${pageContext.request.contextPath}/styles/new">Add New Style</a>
  <table border="1">
    <tr><th>Name</th><th>Actions</th></tr>
    <c:forEach var="s" items="${styles}">
      <tr>
        <td><c:out value="${s.name}"/></td>
        <td>
          <a href="${pageContext.request.contextPath}/styles/edit?id=${s.id}">Edit</a>
          <form action="${pageContext.request.contextPath}/styles/delete"
                method="post" style="display:inline">
            <input type="hidden" name="id" value="${s.id}" />
            <button type="submit">Delete</button>
          </form>
        </td>
      </tr>
    </c:forEach>
  </table>
</body></html>
