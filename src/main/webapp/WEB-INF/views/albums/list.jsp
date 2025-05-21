<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
  <head>
    <title>Albums</title>
  </head>
  <body>
    <h1>Albums</h1>
    <a href="${pageContext.request.contextPath}/albums/new">Add New Album</a>
    <table border="1" cellpadding="5">
      <tr>
        <th>Title</th>
        <th>Artist</th>
        <th>Genres</th>
        <th>Styles</th>
        <th>Actions</th>
      </tr>
      <c:forEach var="a" items="${albums}">
        <tr>
          <td><c:out value="${a.title}" /></td>
          <td>
            <c:out value="${a.artist != null ? a.artist.name : '—'}" />
          </td>
          <td>
            <c:forEach var="g" items="${a.genres}" varStatus="st">
              <c:out value="${g.name}" />
              <c:if test="${!st.last}">, </c:if>
            </c:forEach>
          </td>
          <td>
            <c:forEach var="s" items="${a.styles}" varStatus="st">
              <c:out value="${s.name}" />
              <c:if test="${!st.last}">, </c:if>
            </c:forEach>
          </td>
          <td>
            <a href="${pageContext.request.contextPath}/albums/edit?id=${a.id}">Edit</a>
            &nbsp;|&nbsp;
            <form action="${pageContext.request.contextPath}/albums/delete" method="post" style="display:inline">
              <input type="hidden" name="id" value="${a.id}" />
              <button type="submit">Delete</button>
            </form>
          </td>
        </tr>
      </c:forEach>
    </table>
  </body>
</html>
