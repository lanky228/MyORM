package liuzhengri;

import liuzhengri.dao.impl.UtilDao;
import liuzhengri.model.file.entity.FileEntity;
import org.junit.*;

import java.util.Date;
import java.util.List;

public class DaoTest {
    UtilDao dao = new UtilDao();

    @Test
    public void test() throws Exception {
        insert();
        selectAll();
        select();
    }

    void insert() throws Exception {
        FileEntity file = dao.insert(new FileEntity(new Date().toString(), "testData2"));
        System.out.println(file.toString());
    }

    void selectAll() throws Exception {
        List<FileEntity> list = dao.selectAll(FileEntity.class);
        System.out.println(list.toString());
    }

    void select() throws Exception {
        FileEntity file = dao.select("testId", FileEntity.class);
        System.out.println(file.toString());
    }
}