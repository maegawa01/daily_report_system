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

        String status = request.getParameter("status");
        String taskId = request.getParameter("taskId");

        System.out.println(status);
        System.out.println(taskId);

      // if(status != null && status.equals(request.getSession().getId())) {
            EntityManager em = DBUtil.createEntityManager();


            Task s = em.find(Task.class, Integer.parseInt(taskId));
            //Integer.parseInt(str);

            s.setStatus(status);


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

                //request.getSession().removeAttribute("task.id");
                response.sendRedirect(request.getContextPath() + "/tasks/index");
            }
    //}




        // ステータス　ラジオボタン
        /* List<String> status1 = new ArrayList<String>();

         String status = request.getParameter("status");
         if(status != null || status1.equals("complete")) {

             request.setAttribute(status, "status");
         }else if(status == null || status.equals("unfinished")) {
             request.setAttribute(status,  "status");
         }*/

        //HttpSession set = request.getSession();
        //set.setAttribute("status", status);

        //String status1 = (String)set.getAttribute("status");

        //System.out.println(status1);

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






        //
        // クエリ・パラメータの取得処理
        //
        String order = request.getParameter("order"); // 変数order にクエリ・パラメータの値を代入
        // これはいらない
        // request.setCharacterEncoding("UTF-8");
        // OK、クエリ・パラメータの変数と合わせる（例：order）
        //order = request.getParameter("desc");
        // setAttributeはここじゃない
        // request.setAttribute("desc",  tasks_orderby);
        //order = request.getParameter("asc");
        // request.setAttribute("asc",  asc);
        //（デバック用）
        // System.out.println(order);
        //RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/tasks/index.jsp");
        //rd.forward(request.response);
        //
        // NamedQueryの呼び出し
        //
        // 変数 orderの中身をチェック
        // ①空＝初期表示
        // ②asc＝（▲をクリックされた）
        // 　昇順で日報を取得するNamedQueryを呼び出し、tasksに結果を代入
        // ③desc=(▼をクリックされた）
        //　 降順で日報を取得するNamedQueryを呼び出し、tasksに結果を代入
        // tasksには条件によって、昇順　or 降順でタスク一覧が入っていることになる。
        if (order == null || order.equals("asc")) {
            // ascの場合

            List<Task> tasks = em.createNamedQuery("getOrderAsc", Task.class)
                    .setFirstResult(15 * (page - 1))
                    .setMaxResults(15)
                    .getResultList();

            request.setAttribute("tasks", tasks);

        } else if (order.equals("desc")) {
            // descの場合

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