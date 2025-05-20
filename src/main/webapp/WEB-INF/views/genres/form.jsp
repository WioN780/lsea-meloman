<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html><body>
  <h1><c:out value="${genre.id == null ? 'New' : 'Edit'}"/> Genre</h1>
  <c:if test="${not empty error}">
    <div style="color:red">${error}</div>
  </c:if>
  <form method="post"
        action="${genre.id == null
          ? pageContext.request.contextPath + '/genres/create'
          : pageContext.request.contextPath + '/genres/update'}">
    <input type="hidden" name="id" value="${genre.id}" />
    Name:<br/>
    <input name="name" value="${genre.name}" /><br/><br/>
    <button type="submit">Save</button>
    <a href="${pageContext.request.contextPath}/genres">Cancel</a>
  </form>
</body></html>