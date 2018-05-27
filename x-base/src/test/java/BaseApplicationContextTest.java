import com.zerox.base.config.ApplicationConfig;
import com.zerox.base.context.ApplicationContext;
import com.zerox.base.demo.PersonController;
import org.junit.Test;

public class BaseApplicationContextTest {

    @Test
    public void test01() throws Exception {
        ApplicationContext applicationContext = new ApplicationContext("/applicationContext.xml");
        System.out.println(applicationContext.getBean("p1"));
        System.out.println(ApplicationConfig.getConfigMap());

        PersonController personController = (PersonController) applicationContext.getBean("PersonController");
        personController.say();
    }

}
