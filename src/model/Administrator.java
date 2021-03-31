package model;

import java.io.Serializable;
import java.util.ArrayList;

public class Administrator extends User implements Serializable {

  private String phoneNumber;
  private ArrayList<Course> myCreatedCourses = new ArrayList<>();

  public Administrator() {
  }

  public Administrator(
      String login,
      String psw,
      String email,
      String phoneNumber,
      ArrayList<Course> myCreatedCourses) {
    super(login, psw, email);
    this.phoneNumber = phoneNumber;
    this.myCreatedCourses = myCreatedCourses;
  }

  public Administrator(int id, String login, String psw, String email, String phoneNumber, int course_is) {
    super(id, login, psw, email, course_is);
    this.phoneNumber = phoneNumber;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public ArrayList<Course> getMyCreatedCourses() {
    return myCreatedCourses;
  }

  public void setMyCreatedCourses(ArrayList<Course> myCreatedCourses) {
    this.myCreatedCourses = myCreatedCourses;
  }

  @Override
  public void greetUser() {
    System.out.println("Hello admin");
  }

  @Override
  public String toString() {
    return "login: " + getLogin() + ", psw: " + getPsw() + ", email: " + getEmail() + ", phone number: " + getPhoneNumber();
  }
}
