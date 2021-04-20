package model;

import java.io.Serializable;
import java.util.ArrayList;

public class Student extends User implements Serializable {

  private String name;
  private String surname;
  private String accNumber;
  private ArrayList<Course> myEnrolledCourses = new ArrayList<>();

  public Student(
      int id, String login, String psw, String email, String name, String surname, String accNumber, int course_is) {
    super(id, login, psw, email, course_is);
    this.name = name;
    this.surname = surname;
    this.accNumber = accNumber;
  }

  public Student(String login, String psw, String email, String accNumber) {
    super(login, psw, email);
    this.accNumber = accNumber;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSurname() {
    return surname;
  }

  public void setSurname(String surname) {
    this.surname = surname;
  }

  public String getAccNumber() {
    return accNumber;
  }

  public void setAccNumber(String accNumber) {
    this.accNumber = accNumber;
  }

  public ArrayList<Course> getMyEnrolledCourses() {
    return myEnrolledCourses;
  }

  public void setMyEnrolledCourses(ArrayList<Course> myEnrolledCourses) {
    this.myEnrolledCourses = myEnrolledCourses;
  }

  @Override
  public void greetUser() {
    System.out.println("Hello student " + name);
  }

  @Override
  public String toString() {
    return "login: " + getLogin() + ", psw: " + getPsw() + ", email: " + getEmail() + ", account number: " + getAccNumber();
  }
}
