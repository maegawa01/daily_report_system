package controllers.tasks;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
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
 * Servlet implementation class TasksUpdateServlet
 */
@WebServlet("/tasks/update")
public class TasksUpdateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public TasksUpdateServlet() {
        super();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String _token = request.getParameter("_token");

        if (_token != null && _token.equals(request.getSession().getId())) {
            EntityManager em = DBUtil.createEntityManager();

            Task r = em.find(Task.class, (Integer) (request.getSession().getAttribute("task_id")));

            r.setLimitdate(Date.valueOf(request.getParameter("limitdate")));
            r.setTitle(request.getParameter("title"));
            r.setContent(request.getParameter("content"));
            r.setUpdated_at(new Timestamp(System.currentTimeMillis()));

            List<String> errors = TaskValidator.validate(r);
            if (errors.size() > 0) {
                em.close();

                request.setAttribute("_token", request.getSession().getId());
                request.setAttribute("task", r);
                request.setAttribute("errors", errors);

                RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/tasks/edit.jsp");
                rd.forward(request, response);
            } else {
                em.getTransaction().begin();
                em.getTransaction().commit();
                em.close();
                request.getSession().setAttribute("flush", "更新が完了しました。");

                request.getSession().removeAttribute("task_id");
                response.sendRedirect(request.getContextPath() + "/tasks/index");

            }
        }
    }

}
