<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:import url="../layout/app.jsp">
    <c:param name="content">
        <c:if test="${flush != null}">
            <div id="flush_success">
                <c:out value="${flush}"></c:out>
            </div>
        </c:if>
        <h2>従業員 一覧</h2>

        <%-- 検索機能 --%>
         <form action="./index" method="get">
        <input type="text" name="search" value="${search}" >
        <input type="submit" value="従業員検索" >
        </form><br />

        <table id="employee_list">
            <tbody>
                <tr>
                    <th>社員番号</th>
                    <th>氏名</th>
                    <th>権限</th>
                    <th>操作</th>
                    <th>フォロー状況</th>
                </tr>
                <c:forEach var="employee" items="${employees}" varStatus="status">
                    <tr class="row${status.count % 2}">
                        <td><c:out value="${employee.code}" /></td>
                        <td><c:out value="${employee.name}" /></td>
                        <td><c:choose>
                                    <c:when test="${employee.admin_flag == 1}">管理者</c:when>
                                    <c:otherwise>一般</c:otherwise>
                                </c:choose>

                        <td>
                            <c:choose>
                                <c:when test="${employee.delete_flag == 1}">
                                (削除済み)
                                </c:when>
                                <c:otherwise>
                                    <a href="<c:url value='/employees/show?id=${employee.id}' />">詳細を表示</a>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
     <%--                       <c:choose>
                                <c:when test="${employee.follow_flag == 1}">
                                フォロー中
                                </c:when>
                                <c:otherwise>
                                    <a href="<c:url value='/employees/follow?id=${employee.id}' />">フォローする</a>
                                </c:otherwise>
                            </c:choose>
--%>                    </td>
                     </tr>
                </c:forEach>
            </tbody>
        </table>

        <div id="pagination">
            (全 ${employees_count} 件) <br />
            <c:forEach var="i" begin="1"
                end="${((employees_count -1 ) / 15 ) + 1}" step="1">
                <c:choose>
                    <c:when test="${i == page}">
                        <c:out value="${i}" /> &nbsp;
                    </c:when>
                    <c:otherwise>
                     <%-- ページネーション機能 --%>
                    <%-- ${i}でページ情報を&{search}で検索ボックスの値をservletに渡す --%>
                        <a href="<c:url value='/employees/index?page=${i}&search=${search}' />"><c:out value="${i}" /></a> &nbsp;
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </div>
        <p>
            <a href="<c:url value='/employees/new' />">新規従業員の登録</a>
        </p>

    </c:param>
</c:import>
