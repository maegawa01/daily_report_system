<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

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
                    <th class="limitdate">期日
                    <%-- 日報の並び順が昇順　or　降順の情報をクエリパラメーターで送りつける --%>
                    <%-- order=desc or order=asc --%>
                        <a href="<c:url value='/tasks/index?order=desc' />">▼</a>
                        <a href="<c:url value='/tasks/index?order=asc' />">▲</a>
                    </th>
                    <th class="title">タスク内容</th>
                    <th class="status">ステータス</th>
                </tr>
                <c:forEach var="task" items="${tasks}" varStatus="status">
                    <tr class="row${status.count % 2}">
                        <td class="limitdate"><fmt:formatDate
                                value='${task.limitdate}' pattern='yyyy-MM-dd' /></td>
                        <td class="title"><a
                            href="<c:url value='/tasks/show?id=${task.id}' />">${task.title}</a></td>


                      <%-- ステータスの登録 --%>
                  <td class="status">
                            <form action="./index" method="post">

                                <label>
                                    <input type="radio" name="status"
                                   <c:if test="${task.status == 'complete'}"> <c:out value="checked" /> </c:if> value="complete" /> 完了

                                </label>

                                <label>
                                    <input type="radio" name="status"



                               <c:if test="${task.status == 'unfinished'}"> <c:out value="checked" /> </c:if>

                                 <c:if test="${task.status == null}"> <c:out value="checked" /> </c:if> value="unfinished" /> 未完


                                </label>

                                <input type="hidden" name="taskId" value="${task.id}" />
                                   <input type="submit"  value="更新"/>


                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>





        <div id="pagination">
            (全 ${tasks_count} 件) <br />
            <c:forEach var="i" begin="1" end="${((tasks_count - 1) / 15) + 1}"
                step="1">
                <c:choose>
                    <c:when test="${i == page}">
                        <c:out value="${i}" />&nbsp;
                    </c:when>
                    <c:otherwise>
                        <a href="<c:url value='/task/index?page=${i}' />"><c:out
                                value="${i}" /></a>&nbsp;
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </div>
        <p>
            <a href="<c:url value='/tasks/new' />">新規タスクの登録</a>
        </p>

    </c:param>
</c:import>
