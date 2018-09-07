package cm.base.framework.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Property;

/**
 * 数据库实体类
 */

@Entity
public class Student {

    @Property(nameInDb = "id")//通过@Property()这个注解定义我外部数据库的字段名才能解决
    @Id(autoincrement = true)
    private Long id;//id必须Long否则无法自增
    private String name;//姓名
    private int age;//年龄
    private int sex;//性别
    private String suject;//主修科目
    private String remark;//备注

    @Generated(hash = 1614647151)
    public Student(Long id, String name, int age, int sex, String suject,
            String remark) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.suject = suject;
        this.remark = remark;
    }
    @Generated(hash = 1556870573)
    public Student() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getAge() {
        return this.age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public int getSex() {
        return this.sex;
    }
    public void setSex(int sex) {
        this.sex = sex;
    }
    public String getSuject() {
        return this.suject;
    }
    public void setSuject(String suject) {
        this.suject = suject;
    }
    public String getRemark() {
        return this.remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    @Override
    public String toString() {
        return "[id:"+id+"   name:"+name+"   age:"+age+"   sex:"+(sex==0 ? "男":"女")+"   suject:"+suject+"  remark:"+remark+"]";
    }
}
