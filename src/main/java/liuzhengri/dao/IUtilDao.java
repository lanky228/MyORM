package liuzhengri.dao;

import java.util.List;

public interface IUtilDao {
    /**
     * 查询 主键
     * @param id
     * @param T
     * @param <T>
     * @return
     * @throws Exception
     */
    <T> T select(Object id, Class T) throws Exception;

    /**
     * 查询所有
     * @param clazz
     * @param <T>
     * @return
     * @throws Exception
     */
    <T> List<T> selectAll(Class clazz) throws Exception;

    /**
     * 保存
     * @param entity
     * @param <T>
     * @return
     */
    <T> T insert(T entity) throws Exception;
}
