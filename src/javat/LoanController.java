package javat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@RequestMapping
@Controller
@SessionAttributes({"id", "errorMessage"})

public class LoanController {

    @Autowired
    LoanDao dao;

    @GetMapping(path="/")
    public String showLoanPage(ModelMap model) throws ClassNotFoundException, SQLException {

        List<Loan> list = dao.display();
        model.addAttribute("loanlist", list);

//        model.put("id", list.get(0).getCatcode());
//        model.put("desc", list.get(0).getCatdesc());

        return "loan";
    }

    @GetMapping(path="add-todo")
    public String showtodopage() {
        return "loanser";
    }

    @PostMapping(path="add-todo")
    public String addTodo(ModelMap model, @RequestParam String clientno, @RequestParam String clientname, @RequestParam double loanamount, @RequestParam int years, @RequestParam String loantype) throws SQLException, ClassNotFoundException {
        List<Map<String, Object>> x = dao.getLoanByClientno(clientno);
        // String iid1, ccdesc1;

//        x.forEach(rowMap -> {
//            String iid = (String) rowMap.get("catcode");
//            model.put("id", iid);
//            String ccdesc = (String) rowMap.get("catdesc");
//            model.put("desc", ccdesc);
//        });
//
        if (x.isEmpty() == false) {
            model.put("errorMessage", "The record you are trying to add is already existing. Choose a different customer number");
            return "redirect:/";
        }

        model.put("errorMessage", "");

        Loan loan = new Loan();
        loan.setClientno(clientno);
        loan.setClientname(clientname);
        loan.setLoanamount(loanamount);
        loan.setYears(years);
        loan.setLoantype(loantype);
        dao.insertData(loan);

//        model.addAttribute("cc", loan);

        return "redirect:/";
    }


    @GetMapping(path="/update-todo")
    public String showUpdateTodoPage(ModelMap model, @RequestParam(defaultValue = "") String id) throws SQLException, ClassNotFoundException {
        model.put("id", id);

        List<Map<String, Object>> x = dao.getLoanByClientno(id);
        // String iid1, ccdesc1;

        x.forEach(rowMap -> {
            String _clientno = (String) rowMap.get("clientno");
            model.put("clientno", _clientno);
            String _clientname = (String) rowMap.get("clientname");
            model.put("clientname", _clientname);
            double _loanamount = (double) rowMap.get("loanamount");
            model.put("loanamount", _loanamount);
            int _years = (int) rowMap.get("years");
            model.put("years", _years);
            String _loantype = (String) rowMap.get("loantype");
            model.put("loantype", _loantype);
        });

        return "loanedit";
    }

    @PostMapping(path="/update-todo")
    public String showUpdate(ModelMap model, @RequestParam String clientno, @RequestParam String clientname, @RequestParam double loanamount, @RequestParam int years, @RequestParam String loantype) throws SQLException, ClassNotFoundException {
        String originalClientno = (String) model.get("id");

        List<Map<String, Object>> x = dao.getLoanByClientno(clientno);
        if (x.isEmpty() == false) {
            model.put("errorMessage", "Choose a different customer number to try edit again. This number already been used.");
            return "redirect:/";
        }

        model.put("errorMessage", "");

        Loan loan = new Loan();
        loan.setClientno(clientno);
        loan.setClientname(clientname);
        loan.setLoanamount(loanamount);
        loan.setYears(years);
        loan.setLoantype(loantype);

        dao.EditData(loan, originalClientno);

        return "redirect:/";
    }

    @GetMapping(path="/delete-todo")
    public String deleteTodo(ModelMap model, @RequestParam String id) throws SQLException, ClassNotFoundException {
        int rowCount = dao.deleteData(id);

        if (rowCount == 0) {
            model.put("errorMessage", "Failed to delete client number " + id);
        } else {
            model.put("errorMessage", "");
        }
        return "redirect:/";
    }

    @GetMapping(path="/see-todo")
    public String seetodo(ModelMap model, @RequestParam(defaultValue = "") String id) throws SQLException, ClassNotFoundException {
        List<Map<String, Object>> x = dao.getLoanByClientno(id);

        if (x.size() == 0) {
            model.put("errorMessage", "Unable to identify the client record");
            return "redirect:/";
        }

        model.put("errorMessage", "");

        x.forEach(rowMap -> {
            String clientno = (String) rowMap.get("clientno");
            model.put("clientno", clientno);
            String clientname = (String) rowMap.get("clientname");
            model.put("clientname", clientname);
            double loanamount = (double) rowMap.get("loanamount");
            int years = (int) rowMap.get("years");
            String loantype = (String) rowMap.get("loantype");
        });

        model.addAttribute("itemlist", x);
        return "loanreport";
    }

    @PostMapping(path="/see-todo")
    public String seetodo2(ModelMap model) throws SQLException, ClassNotFoundException {
        return "redirect:/";
    }
}
