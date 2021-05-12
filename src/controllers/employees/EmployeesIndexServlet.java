package controllers.employees;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import utils.DBUtil;

/**
 * Servlet implementation class EmployeesIndexServlet
 */
@WebServlet("/employees/index")
public class EmployeesIndexServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmployeesIndexServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();

        int page = 1;
        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (NumberFormatException e) {
            page = 1;
        }

        // 検索機能
        // jspからクエリパラメで渡された"saerch"をString search に格納
        String search = request.getParameter("search");

        // 検索後の画面に遷移後に検索窓に検索ワードを表示する
        // jspからクエリパラメタで渡された値を"search"リクエストスコープにセット
        request.setAttribute("search",  search);

        // デバック用
        System.out.println(search);

        long employees_count;

        // search if 文
        // search が null 若しくは 空白 の場合（デフォルトの表示）
        if (search == null || search.equals("")) {

            List<Employee> employees = em.createNamedQuery("getAllEmployees", Employee.class)
                    .setFirstResult(15 * (page - 1))
                    .setMaxResults(15)
                    .getResultList();
            request.setAttribute("employees", employees);

            employees_count = (long) em.createNamedQuery("getEmployeesCount", Long.class)
                    .getSingleResult();

        } else {
            List<Employee> employeeSearch = em.createNamedQuery("getEmployeeSearch", Employee.class)
                    .setParameter("name", "%" + search + "%") // 検索クエリの呼びだし
                    .setFirstResult(15 * (page - 1))
                    .setMaxResults(15)
                    .getResultList();

            // listを拡張for文で展開
            for (Employee employee : employeeSearch) {
                System.out.println(employee.getName());
            }
            request.setAttribute("employees", employeeSearch);


        employees_count = (long) em.createNamedQuery("getEmployeeSearchCount", Long.class)
                .setParameter("name", "%" + search + "%")
                .getSingleResult();

    }

        em.close();

        request.setAttribute("employees_count", employees_count);
        request.setAttribute("page", page);
        if (request.getSession().getAttribute("flush") != null) {
            request.setAttribute("flush", request.getSession().getAttribute("flush"));
            request.getSession().removeAttribute("flush");
        }

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/employees/index.jsp");
        rd.forward(request, response);
    }

}
