package trivia;

import trivia.Question;

import org.javalite.activejdbc.Base;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class QuestionTest {

  @Before
  public void before(){
      Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/sparkTest_test", "root", "root");
      System.out.println("Question DB Connect");
      Base.openTransaction();
  }

  @After
  public void after(){
      System.out.println("Question DB Disconnect");
      Base.rollbackTransaction();
      Base.close();
  }

  @Test
  public void nullNotBlank() {
    Question deleteOne = new Question();

    deleteOne.set("pregunta", "String of Question");
    deleteOne.set("respuestaCorrecta", "Correct Answer");
    deleteOne.set("wrong1", "First Bad Answer");
    deleteOne.set("wrong2", "Second Bad Answer");
    deleteOne.saveIt();

    deleteOne.set("wrong2", "  ");

    deleteOne.saveIt();

    assertEquals(deleteOne.get("wrong2") == "  ", false);
    assertEquals(deleteOne.get("wrong2") == null, true);


  }

  @Test
  public void notAllUsed1() {
    Question incomplete = new Question();

    incomplete.set("pregunta", "String of Question");
    incomplete.set("respuestaCorrecta", "Correct Answer");
    incomplete.set("wrong1", "First Bad Answer");
    incomplete.set("wrong2", "Second Bad Answer");

    assertEquals(incomplete.get("wrong3") == null, true);
  }

  @Test
  public void notAllUsed2() {
    Question incomplete = new Question();

    incomplete.set("pregunta", "String of Question");
    incomplete.set("respuestaCorrecta", "Correct Answer");
    incomplete.set("wrong1", "First Bad Answer");

    assertEquals(incomplete.get("wrong2") == null, true);
    assertEquals(incomplete.get("wrong3") == null, true);
  }

  @Test
  public void AlmostACorrect() {

      Question noOneCorrect = new Question();

      noOneCorrect.set("pregunta", "String of Question");
      noOneCorrect.set("wrong1", "First Bad Answer");
      noOneCorrect.set("wrong2", "Second Bad Answer");

      assertEquals(noOneCorrect.isValid(), false);
  }

  @Test
  public void AlmostAIncorrect() {

      Question noOneIncorrect = new Question();

      noOneIncorrect.set("pregunta", "String of Question");
      noOneIncorrect.set("respuestaCorrecta", "Correct Answer");

      assertEquals(noOneIncorrect.isValid(), false);
  }

  @Test
  public void noQuestion() {

      Question noQuestion = new Question();

      noQuestion.set("respuestaCorrecta", "Correct Answer");
      noQuestion.set("wrong1", "First Bad Answer");

      assertEquals(noQuestion.isValid(), false);
  }

}
