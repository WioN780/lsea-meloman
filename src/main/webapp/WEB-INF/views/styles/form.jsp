<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html><body>
  <h1>
    <c:out value="${style.id == null ? 'New' : 'Edit'}"/> Style
  </h1>

  <c:if test="${not empty error}">
    <div style="color:red"><c:out value="${error}"/></div>
  </c:if>

  <form method="post"
        action="${pageContext.request.contextPath}/styles/${style.id == null ? 'create' : 'update'}">
    <input type="hidden" name="id" value="${style.id}" />

    Name:<br/>
    <input name="name" value="${style.name}" /><br/><br/>

    <button type="submit">Save</button>
    &nbsp;<a href="${pageContext.request.contextPath}/styles">Cancel</a>
  </form>
</body></html>
