import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class YBDatabaseUtil {

    private final DataSource dataSource;
    private final ModelMapper modelMapper;

    public YBDatabaseUtil(DataSource dataSource, ModelMapper modelMapper) {
        this.dataSource = dataSource;
        this.modelMapper = modelMapper;
    }

    // Generic SELECT method
    public <T> List<T> read(String query, Class<T> clazz, Object... params) throws SQLException {
        List<T> results = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            setParameters(ps, params);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    T mappedObject = mapResultSetToObject(rs, clazz);
                    results.add(mappedObject);
                }
            }
        }
        return results;
    }

    // Generic UPDATE/INSERT/DELETE method
    public int execute(String query, Object... params) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            setParameters(ps, params);
            return ps.executeUpdate();
        }
    }

    // Maps ResultSet to an object of specified class using ModelMapper
    private <T> T mapResultSetToObject(ResultSet rs, Class<T> clazz) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        T mappedObject = modelMapper.map(new Object(), clazz); // Initialize target object

        for (int i = 1; i <= columnCount; i++) {
            String columnLabel = metaData.getColumnLabel(i);
            Object columnValue = rs.getObject(columnLabel);
            modelMapper.map(columnValue, clazz);
        }
        return mappedObject;
    }

    // Helper to set parameters in PreparedStatement
    private void setParameters(PreparedStatement ps, Object[] params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            ps.setObject(i + 1, params[i]);
        }
    }
}
