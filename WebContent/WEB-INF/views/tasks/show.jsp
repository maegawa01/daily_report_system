<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">
        <c:choose>
            <c:when test="${task != null}">
                <h2>タスク 詳細</h2>

                <table>
                    <tbody>
                        <tr>
                            <th>期日</th>
                            <td><fmt:formatDate value="${task.limitdate}" pattern="yyyy-MM-dd" /></td>
                        </tr>
                        <tr>
                            <th>作成日時</th>
                            <td><fmt:formatDate value="${task.created_at}" pattern="yyyy-MM-dd HH:mm" /></td>
                        </tr>
                        <tr>
                            <th>更新日時</th>
                            <td><fmt:formatDate value="${task.updated_at}" pattern="yyyy-MM-dd HH:mm" /></td>
                        </tr>
                        <tr>
                            <th>タスク内容</th>
                            <td><c:out value="${task.title}" /></td>
                        </tr>
                        <tr>
                            <th>メモ</th>
                            <td><c:out value="${task.content}" /></td>
                        </tr>
                    </tbody>
                </table>

                <c:if test="${sessionScope.login_employee.id == task.employee.id}">
                    <p><a href="<c:url value="/tasks/edit?id=${task.id}" />">このタスクを編集する</a></p>

                </c:if>
            </c:when>
            <c:otherwise>
                <h2>お探しのデータは見つかりませんでした。</h2>
            </c:otherwise>
        </c:choose>

        <p><a href="<c:url value="/tasks/index" />">一覧に戻る</a></p>
    </c:param>
</c:import>