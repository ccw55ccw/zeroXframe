import com.zerox.base.common.Person;
import com.zerox.base.context.ClassXmlApplicationContext;
import org.junit.Test;

public class BaseXmlContextTest {

    @Test
    public void test01() throws Exception {
        ClassXmlApplicationContext applicationContext = new ClassXmlApplicationContext("/applicationContext.xml");
        Person p1 = (Person) applicationContext.getBean("p1");
        System.out.println(p1);

        this.getClass().getClassLoader().loadClass("com.zerox.base.helper.ClassHelper").newInstance();

    }

}
