package controllers.reports;

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
import models.Report;
import utils.DBUtil;

/**
 * Servlet implementation class ReportsIndexServlet
 */
@WebServlet("/reports/index")
public class ReportsIndexServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportsIndexServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        EntityManager em = DBUtil.createEntityManager();

        Employee login_employee = (Employee) request.getSession().getAttribute("login_employee");

        int page;
        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (Exception e) {
            page = 1;
        }

        // 検索機能
        // jspからクエリパラメタで渡された"search"をString search 格納
        String search = request.getParameter("search");

        // 検索後の画面に遷移後に検索窓に検索ワードを表示する
        //jspからクエリパラメタで渡された値を"search"リクエストスコープにセット
        request.setAttribute("search", search);

        // デバック用
        System.out.println(search);

        long reports_count;

        // saerch if文
        // searchがnull もしくは 空白 の場合 （デフォルトの表示）
        if (search == null || search.equals("")) {
            List<Report> reports = em.createNamedQuery("getMyAllReports", Report.class)
                    .setParameter("employee", login_employee)
                    .setFirstResult(15 * (page - 1))
                    .setMaxResults(15)
                    .getResultList();
            request.setAttribute("reports", reports);

            reports_count = (long) em.createNamedQuery("getMyReportsCount", Long.class)
                    .setParameter("employee", login_employee)
                    .getSingleResult();

            // searchがnull もしくは 空白 の場合以外 （検索結果の表示）
        } else {
            List<Report> reportSearch = em.createNamedQuery("getReportSearch", Report.class)
                    .setParameter("employee", login_employee) // ログイン情報の取得
                    .setParameter("word", "%" + search + "%") // 検索クエリ呼び出し
                    .setFirstResult(15 * (page - 1))
                    .setMaxResults(15)
                    .getResultList();

            // listを拡張for文で展開
            for (Report report : reportSearch) {
                System.out.println(report.getTitle());
            }
            request.setAttribute("reports", reportSearch);

            reports_count = (long) em.createNamedQuery("getReportSearchCount", Long.class)
                    .setParameter("employee", login_employee)
                    .setParameter("word", "%" + search + "%")
                    .getSingleResult();
        }

        em.close();

        request.setAttribute("reports_count", reports_count);
        request.setAttribute("page", page);
        if (request.getSession().getAttribute("flush") != null) {
            request.setAttribute("flush", request.getSession().getAttribute("flush"));
            request.getSession().removeAttribute("flush");
        }

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/reports/index.jsp");
        rd.forward(request, response);

    }

}
