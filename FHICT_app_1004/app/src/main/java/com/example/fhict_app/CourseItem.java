package com.example.fhict_app;

public class CourseItem {
    private String courseName;
    private String room;
    private String teacherAbbreviation;
    private String start;
    private String date;

    //constructor
    public CourseItem(String courseName,String room,String teacherAbbreviation,String start,String date){
        this.courseName=courseName;
        this.room=room;
        this.teacherAbbreviation=teacherAbbreviation;
        this.start=start;
        this.date=date;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getTeacherAbbreviation() {
        return teacherAbbreviation;
    }

    public void setTeacherAbbreviation(String teacherAbbreviation) {
        this.teacherAbbreviation = teacherAbbreviation;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
