import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class YugabyteDBService {

    @Autowired
    private YBDatabaseUtil yBDatabaseUtil;

    // Method to Create a Table
    public void createTable() throws SQLException {
        String createTableSql = "CREATE TABLE IF NOT EXISTS SAMPLE_TABLE (" +
                "id SERIAL PRIMARY KEY, " +
                "name VARCHAR(50))";
        
        // Call the YBDatabaseUtil to execute the SQL
        yBDatabaseUtil.createTable(createTableSql);
    }

    // Method to Insert Data
    public void insertData(String name) throws SQLException {
        String insertSql = "INSERT INTO SAMPLE_TABLE (name) VALUES (?)";
        
        // Call the YBDatabaseUtil to execute the SQL
        yBDatabaseUtil.insertData(insertSql, name);
    }

    // Method to Read Data (generic return type)
    public <T> List<T> readData(Class<T> clazz, String sqlQuery, Object... params) throws SQLException {
        // Call the YBDatabaseUtil to fetch the data
        return yBDatabaseUtil.selectData(sqlQuery, clazz, params);
    }
}
