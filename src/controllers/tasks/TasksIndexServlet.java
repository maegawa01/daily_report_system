package controllers.tasks;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Task;
import models.validators.TaskValidator;
import utils.DBUtil;

/**
 * Servlet implementation class TasksIndexServlet
 */
@WebServlet("/tasks/index")
public class TasksIndexServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public TasksIndexServlet() {
        super();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // status状況の取得、更新
        // "status"のパラメタを取得しstatusに格納
        String status = request.getParameter("status");

        // "taskId"のパラメタを取得しtaskIdに格納
        String taskId = request.getParameter("taskId");

        // デバック用
        System.out.println(status);
        System.out.println(taskId);

        EntityManager em = DBUtil.createEntityManager();

        // taskIdの主キーを基に該当のデーターをsに格納
        Task s = em.find(Task.class, Integer.parseInt(taskId));
        // taskIdの主キーのstatusをsに格納
        s.setStatus(status);

        // バリデーション
        List<String> errors = TaskValidator.validate(s);
        if (errors.size() > 0) {
            em.close();
            request.setAttribute("string", request.getSession().getId());
            request.setAttribute("task", s);
            request.setAttribute("errors", errors);

            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/tasks/index.jsp");
            rd.forward(request, response);
        } else {
            em.getTransaction().begin();
            em.getTransaction().commit();
            em.close();
            request.getSession().setAttribute("flush", "更新が完了しました。");
            response.sendRedirect(request.getContextPath() + "/tasks/index");
        }
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();
        int page;
        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (Exception e) {
            page = 1;
        }

        // ラジオボタン
        // クエリ・パラメータの取得処理
        //
        String order = request.getParameter("order"); // 変数order にクエリ・パラメータの値を代入

        //（デバック用）
        System.out.println(order);
        //
        // NamedQueryの呼び出し
        // ascの場合
        if (order == null || order.equals("asc")) {
            List<Task> tasks = em.createNamedQuery("getOrderAsc", Task.class)
                    .setFirstResult(15 * (page - 1))
                    .setMaxResults(15)
                    .getResultList();
            request.setAttribute("tasks", tasks);

        // descの場合
        } else if (order.equals("desc")) {
            List<Task> tasks = em.createNamedQuery("getOrderDesc", Task.class)
                    .setFirstResult(15 * (page - 1))
                    .setMaxResults(15)
                    .getResultList();
            request.setAttribute("tasks", tasks);
        }

        long tasks_count = (long) em.createNamedQuery("getTasksCount", Long.class)
                .getSingleResult();
        request.setAttribute("tasks_count", tasks_count);
        request.setAttribute("page", page);
        if (request.getSession().getAttribute("flush") != null) {
            request.setAttribute("flush", request.getSession().getAttribute("flush"));
            request.getSession().removeAttribute("flush");
        }
        em.close();

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/tasks/index.jsp");
        rd.forward(request, response);

    }
}