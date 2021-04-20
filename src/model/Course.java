package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class Course implements Serializable {

  private int id;
  private String name;
  private LocalDate startDate;
  private LocalDate endDate;
  private Administrator administrator;
  private ArrayList<Student> enrolledUsers;
  private ArrayList<Folder> folders = new ArrayList<>();
  private double coursePrice;

  public Course(
      String name,
      LocalDate startDate,
      LocalDate endDate,
      Administrator administrator,
      ArrayList<Student> enrolledUsers,
      ArrayList<Folder> folders,
      double coursePrice) {
    this.name = name;
    this.startDate = startDate;
    this.endDate = endDate;
    this.administrator = administrator;
    this.enrolledUsers = enrolledUsers;
    this.folders = folders;
    this.coursePrice = coursePrice;
  }

  public Course(String name, LocalDate startDate, LocalDate endDate, double coursePrice) {
    this.name = name;
    this.startDate = startDate;
    this.endDate = endDate;
    this.coursePrice = coursePrice;
  }

  public Course(String name, LocalDate startDate, LocalDate endDate, Administrator administrator, double coursePrice) {
    this.name = name;
    this.startDate = startDate;
    this.endDate = endDate;
    this.administrator = administrator;
    this.coursePrice = coursePrice;
    this.enrolledUsers = new ArrayList<>();
  }

  public Course(int id, String name, LocalDate startDate, LocalDate endDate, double coursePrice) {
    this.id = id;
    this.name = name;
    this.startDate = startDate;
    this.endDate = endDate;
    this.coursePrice = coursePrice;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public LocalDate getStartDate() {
    return startDate;
  }

  public void setStartDate(LocalDate startDate) {
    this.startDate = startDate;
  }

  public LocalDate getEndDate() {
    return endDate;
  }

  public void setEndDate(LocalDate endDate) {
    this.endDate = endDate;
  }

  public Administrator getAdministrator() {
    return administrator;
  }

  public void setAdministrator(Administrator administrator) {
    this.administrator = administrator;
  }

  public ArrayList<Student> getEnrolledUsers() {
    return enrolledUsers;
  }

  public void setEnrolledUsers(ArrayList<Student> enrolledUsers) {
    this.enrolledUsers = enrolledUsers;
  }

  public ArrayList<Folder> getFolders() {
    return folders;
  }

  public void setFolders(ArrayList<Folder> folders) {
    this.folders = folders;
  }

  public double getCoursePrice() {
    return coursePrice;
  }

  public void setCoursePrice(double coursePrice) {
    this.coursePrice = coursePrice;
  }

  @Override
  public String toString() {
    return "This is " + name + " course, current number of students is " + enrolledUsers.size() + " course creator: " + administrator.getLogin();
  }
}
