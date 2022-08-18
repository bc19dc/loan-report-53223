package javat;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Component
@Service
public class LoanDao {

    JdbcTemplate template;

    public LoanDao(JdbcTemplate template) {
        this.template = template;
    }
    public void setTemplate(JdbcTemplate template) {
    }


    public List<Loan> display() throws ClassNotFoundException, SQLException {
        //create an array list that will contain the data recovered

        return template.query("select * from loantable", (RowMapper) (rs, row) -> {
            Loan loan = new Loan();
            loan.setClientno(rs.getString(1));
            loan.setClientname(rs.getString(2));
            loan.setLoanamount(rs.getDouble(3));
            loan.setYears(rs.getInt(4));
            loan.setLoantype(rs.getString(5));
            return loan;

        });


    }


    public int insertData(final Loan loan) {
        return template.update(
                "insert into loantable values(?,?,?,?,?)",
                loan.getClientno(), loan.getClientname(), loan.getLoanamount(), loan.getYears(), loan.getLoantype()
        );
    }


    public int deleteData (String clientno) {
        return template.update("delete from loantable where clientno= ?", clientno);
    }

    public int EditData(final Loan loan, String clientno) {
        return template.update(
                "update loantable set clientno= ?, clientname = ?, loanamount = ?, years = ?, loantype = ? where clientno = ?",
                loan.getClientno(), loan.getClientname(), loan.getLoanamount(), loan.getYears(), loan.getLoantype(),
                clientno
        );
    }

    public List<Map<String, Object>> getLoanByClientno (String clientno){
        return template.queryForList("SELECT * from loantable where clientno = ?", clientno);
    }

    public List<Map<String, Object>> getLoanByClientname (String clientname){
        return template.queryForList("SELECT * from loantable where clientname = ?", clientname);
    }
}
