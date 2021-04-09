<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">
        <c:if test="${flush != null}">
            <div id="flush_success">
                <c:out value="${flush}"></c:out>
            </div>
        </c:if>
        <h2>タスク 一覧</h2>
        <table id="task_list">
            <tbody>
                <tr>
                    <th class="task_limitdate">期日</th>
                    <th class="task_title">タスク</th>
                    <th class="task_status">ステータス</th>
                </tr>
                <c:forEach var="report" items="${reports}" varStatus="status">
                    <tr class="row${status.count % 2}">
                        <td class="task_limitdate"><fmt:formatDate value='${task.task_limitdate}' pattern='yyyy-MM-dd' /></td>
                        <td class="task_title">${task.title}</td>
                        <td class="task_status"></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <div id="pagination">
            (全 ${task_count} 件) <br />
            <c:forEach var="i" begin="1" end="${((task_count - 1) / 15) + 1}" step="1">
                <c:choose>
                    <c:when test="${i == page}">
                        <c:out value="${i}" />&nbsp;
                    </c:when>
                    <c:otherwise>
                        <a href="<c:url value='/task/index?page=${i}' />"><c:out value="${i}" /></a>&nbsp;
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </div>
        <p><a href="<c:url value='/task/new' />">新規タスクの登録</a></p>

    </c:param>
</c:import>
