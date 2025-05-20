<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
  <head>
    <title><c:out value="${album.id == null ? 'New' : 'Edit'}"/> Album</title>
  </head>
  <body>
    <h1><c:out value="${album.id == null ? 'New' : 'Edit'}"/> Album</h1>

    <c:if test="${not empty error}">
      <div style="color:red">${error}</div>
    </c:if>

    <form method="post"
          action="${album.id == null
            ? pageContext.request.contextPath + '/albums/create'
            : pageContext.request.contextPath + '/albums/update'}">

      <input type="hidden" name="id" value="${album.id}" />

      Title:<br/>
      <input type="text" name="title" value="${album.title}" /><br/><br/>

      URL:<br/>
      <input type="text" name="url" value="${album.url}" /><br/><br/>

      Contact Info:<br/>
      <input type="text" name="contactInfo" value="${album.contactInfo}" /><br/><br/>

      Year:<br/>
      <input type="text" name="year" value="${album.year}" /><br/><br/>

      Artist:<br/>
      <select name="artistId">
        <option value="">-- none --</option>
        <c:forEach var="art" items="${artists}">
          <option value="${art.id}"
            <c:if test="${album.artist != null && art.id == album.artist.id}">
              selected
            </c:if>>
            <c:out value="${art.name}" />
          </option>
        </c:forEach>
      </select>
      <br/><br/>

      Genres:<br/>
      <c:forEach var="g" items="${allGenres}">
        <label>
          <input type="checkbox" name="genreIds"
                 value="${g.id}"
                 <c:if test="${album.genres != null && album.genres.contains(g)}">
                   checked
                 </c:if> />
          <c:out value="${g.name}" />
        </label>
      </c:forEach>
      <br/><br/>

      Styles:<br/>
      <c:forEach var="s" items="${allStyles}">
        <label>
          <input type="checkbox" name="styleIds"
                 value="${s.id}"
                 <c:if test="${album.styles != null && album.styles.contains(s)}">
                   checked
                 </c:if> />
          <c:out value="${s.name}" />
        </label>
      </c:forEach>
      <br/><br/>

      <button type="submit">Save</button>
      &nbsp;<a href="${pageContext.request.contextPath}/albums">Cancel</a>
    </form>
  </body>
</html>
