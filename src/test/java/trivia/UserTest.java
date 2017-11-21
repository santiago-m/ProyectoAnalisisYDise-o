package trivia;

import trivia.User;

import org.javalite.activejdbc.Base;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class UserTest{
    @Before
    public void before(){
        Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/sparkTest_test", "root", "root");
        System.out.println("UserTest setup");
        Base.openTransaction();
    }

    @After
    public void after(){
        System.out.println("UserTest tearDown");
        Base.rollbackTransaction();
        Base.close();
    }

    @Test
    public void validateUniqueUsername(){
        User user1 = new User();
        user1.set("username", "probe1");
        user1.set("password", "probe1");
        user1.saveIt();

        User user2 = new User();
        List<User> list = User.where("username = 'probe1'" );
        if (list.isEmpty()){
          user2.set("username", "probe1");
          user2.set("password", "asd");
        }

        assertEquals(user2.isValid(), false);
    }

    @Test
    public void nonNegativePoints() {
        User user = new User("asd", "dsa");

        user.setPoints(-1);

        assertEquals(user.getPoints()>=0 , true);
    }

    @Test
    public void morePoints() {
        User user1 = new User ("user1", "prueba");
        User user2 = new User ("user2", "prueba");

        user1.incPoints();

        assertEquals(user1.getPoints(), (user2.getPoints()+5) );
    }

    @Test
    public void morePoints2() {
        User user1 = new User ("user1", "prueba");
        User user2 = new User ("user2", "prueba");

        user1.incPoints();
        user2.setPoints(5);

        assertEquals(user1.getPoints(), user2.getPoints());

    }

    @Test
    public void needUsernameNotBlank(){
        User userNoName = new User();
        userNoName.set("username", "probe1");
        userNoName.set("password", "   ");

        assertEquals(userNoName.isValid(), false);
    }

    @Test
    public void needPasswordNotBlank(){
        User user = new User();
        user.set("username", "  ");
        user.set("password", "probe1");

        assertEquals(user.isValid(), false);
    }

}
