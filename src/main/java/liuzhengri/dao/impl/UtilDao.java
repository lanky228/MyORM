package liuzhengri.dao.impl;

import liuzhengri.dao.IUtilDao;
import liuzhengri.dao.annotation.Column;
import liuzhengri.dao.annotation.Id;
import liuzhengri.dao.annotation.Table;
import org.sqlite.util.StringUtils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UtilDao implements IUtilDao {
    private String sqlClassName = "org.sqlite.JDBC";
    private String sqlConnectionUrl = "jdbc:sqlite:test.db";

    @Override
    public <T> T select(Object id, Class clazz) throws Exception {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            Class.forName(sqlClassName);
            connection = DriverManager.getConnection(sqlConnectionUrl);
            statement = connection.createStatement();
            String sql = "SELECT * FROM %s where %s = '%s';";
            if (getTableName(clazz) == null) {
                return null;
            }
            sql = String.format(sql, getTableName(clazz), getIdName(clazz), id);
            resultSet = statement.executeQuery(sql);
            T result = (T) clazz.newInstance();
            while (resultSet.next()) {
                Field[] fields = clazz.getDeclaredFields();
                for (Field item : fields) {
                    item.setAccessible(true);
                    item.set(result, resultSet.getString(getColumnName(item)));
                }
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            resultSet.close();
            statement.close();
            connection.close();
        }
        return null;
    }

    @Override
    public <T> List<T> selectAll(Class clazz) throws Exception {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            Class.forName(sqlClassName);
            connection = DriverManager.getConnection(sqlConnectionUrl);
            statement = connection.createStatement();
            String sql = "SELECT * FROM %s;";
            if (getTableName(clazz) == null) {
                return null;
            }
            sql = String.format(sql, getTableName(clazz), getIdName(clazz));
            resultSet = statement.executeQuery(sql);
            List<T> resultList = new ArrayList<>();
            while (resultSet.next()) {
                T db = (T) clazz.newInstance();
                Field[] fields = clazz.getDeclaredFields();
                for (Field item : fields) {
                    item.setAccessible(true);
                    item.set(db, resultSet.getString(getColumnName(item)));
                }
                resultList.add(db);
            }
            return resultList;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            resultSet.close();
            statement.close();
            connection.close();
        }
        return null;
    }


    @Override
    public <T> T insert(T entity) throws Exception {
        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName(sqlClassName);
            connection = DriverManager.getConnection(sqlConnectionUrl);
            statement = connection.createStatement();
            String sql = "insert into file_entity" +
                    "(%s) " +
                    "values " +
                    "(%s)" +
                    ";";
            Field[] fields = entity.getClass().getDeclaredFields();
            List<String> nameList = new ArrayList<>();
            List<String> valueList = new ArrayList<>();
            for (Field item : fields) {
                item.setAccessible(true);
                nameList.add(getColumnName(item));
                valueList.add("'" + item.get(entity) + "'");
            }
            sql = String.format(sql, StringUtils.join(nameList, ","), StringUtils.join(valueList, ","));
            statement.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            statement.close();
            connection.close();
        }
        return entity;
    }


    /**
     * 获取表名
     *
     * @param clazz
     * @return
     */
    private String getTableName(Class clazz) {
        Table table = (Table) clazz.getAnnotation(Table.class);
        if (table == null) {
            return null;
        }
        return table.name();
    }

    /**
     * 获取主键名
     *
     * @param clazz
     * @return
     */
    private String getIdName(Class clazz) {
        String idName = null;
        Field[] fields = clazz.getDeclaredFields();
        for (Field item : fields) {
            if (!item.isAnnotationPresent(Id.class)) {
                continue;
            }
            Column column = item.getAnnotation(Column.class);
            if (column == null) {
                continue;
            }
            idName = column.name();
            break;
        }
        return idName;
    }

    /**
     * 获取表字段名称
     *
     * @param field
     * @return
     */
    private String getColumnName(Field field) {
        if (!field.isAnnotationPresent(Column.class)) {
            return null;
        }
        Column column = field.getAnnotation(Column.class);
        if (column == null) {
            return null;
        }
        return column.name();
    }
}
