package liuzhengri.model.file.entity;

import liuzhengri.dao.annotation.Column;
import liuzhengri.dao.annotation.Id;
import liuzhengri.dao.annotation.Table;

@Table(name = "file_entity")
public class FileEntity {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    public FileEntity() {}

    public FileEntity(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "FileEntity{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
