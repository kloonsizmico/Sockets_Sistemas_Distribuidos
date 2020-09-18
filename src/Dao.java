import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Dao {
    Connection connection = (Connection) DriverManager.getConnection("jdbc:sqlserver://localhost:1433", "nombre de usuario", "password");

    public Dao() throws SQLException {
    }

    public void GetUsers() {
        CallableStatement calls;
        {
            try {
                calls = connection.prepareCall("{call BancoRevoluxion.dbo.sp_getUsers()}");
                ResultSet rs = calls.executeQuery();
                while (rs.next()) {
                    System.out.println(rs.getString("UsuarioId") + "," + rs.getString("Nombre"));
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public List<Object> UpdateSaldo(int saldo,int condicion) {
        CallableStatement calls;
       // boolean rs = Boolean.parseBoolean(null);
        List<Object> saldoNew = null;
        try {
            calls = connection.prepareCall("{call BancoRevoluxion.dbo.sp_updateSaldo( ?, ?)}");
            calls.setInt(1, saldo);
            calls.setInt(2, condicion);
            calls.execute();
            Dao getCuena = new Dao();
            saldoNew = getCuena.GetCuenta();
            calls.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return saldoNew;
    }

    public List<Object> GetCuenta() throws SQLException {
        CallableStatement calls;

        ResultSet rs = null;
        ArrayList<Object> dt = new ArrayList<>();
        try {
            calls = connection.prepareCall("{call BancoRevoluxion.dbo.sp_getCuenta()}");
            rs = calls.executeQuery();
            while (rs.next()) {
                int saldo = rs.getInt("Saldo");
                //int cuenta = rs.getInt("NumCuenta");
                dt.add(saldo);
                //dt.add(cuenta);
            }
            rs.close();
            calls.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return dt;
    }

}
