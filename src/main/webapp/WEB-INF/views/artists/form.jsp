<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
  <head>
    <title><c:out value="${artist.id == null ? 'New' : 'Edit'}"/> Artist</title>
  </head>
  <body>
    <h1><c:out value="${artist.id == null ? 'New' : 'Edit'}"/> Artist</h1>

    <c:if test="${not empty error}">
      <div style="color:red"><c:out value="${error}" /></div>
    </c:if>

    <form method="post"
          action="${artist.id == null
            ? pageContext.request.contextPath + '/artists/create'
            : pageContext.request.contextPath + '/artists/update'}">

      <input type="hidden" name="id" value="${artist.id}" />

      Name:<br/>
      <input type="text" name="name" value="${artist.name}" /><br/><br/>

      Real Name:<br/>
      <input type="text" name="realName" value="${artist.realName}" /><br/><br/>

      Contact Info:<br/>
      <input type="text" name="contactInfo" value="${artist.contactInfo}" /><br/><br/>

      URL:<br/>
      <input type="text" name="url" value="${artist.url}" /><br/><br/>

      <button type="submit">Save</button>
      &nbsp;<a href="${pageContext.request.contextPath}/artists">Cancel</a>
    </form>
  </body>
</html>