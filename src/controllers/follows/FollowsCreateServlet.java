package controllers.follows;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.Follow;
import utils.DBUtil;

/**
 * Servlet implementation class FollowsCreateServlet
 */
@WebServlet("/follows/create")
public class FollowsCreateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public FollowsCreateServlet() {
        super();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String follow = request.getParameter("follow");
        System.out.println(follow);

        EntityManager em = DBUtil.createEntityManager();

        Follow f = new Follow();
        Employee e;

        // ログイン情報をセッションスコープから取得user_idに格納
        f.setUser_id((Employee) request.getSession().getAttribute("login_employee"));
        // jspからpost送信されたfollowクエリパラメタ(employee.id)のデータを検索followに格納
        e = em.find(Employee.class, Integer.parseInt(request.getParameter("follow")));
        f.setFollow(e);

        System.out.print(f);

        em.getTransaction().begin();
        em.persist(f);
        em.getTransaction().commit();
        em.close();
        request.getSession().setAttribute("flush", "登録が完了しました。");

        response.sendRedirect(request.getContextPath() + "/employees/index");

    }

}
