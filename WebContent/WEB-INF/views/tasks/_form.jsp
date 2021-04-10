<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:if test="${errors != null}">
    <div id="flush_error">
        入力内容にエラーがあります。<br />
        <c:forEach var="error" items="${errors}">
            ・<c:out value="${error}" /><br />
        </c:forEach>

    </div>
</c:if>
<label for="limitdate">期日</label><br />
<input type="date" name="limitdate" value="<fmt:formatDate value='${task.limitdate}' pattern='yyyy-MM-dd' />" />
<br /><br />

<label for="title">タスク内容</label><br />
<textarea name="title" rows="10" cols="30">${task.title}</textarea>
<br /><br />

<label for="content">メモ</label><br />
<textarea name="content" rows="10" cols="50">${task.content}</textarea>
<br /><br />



<input type="hidden" name="_token" value="${_token}"/>
<button type="submit">登録</button>
