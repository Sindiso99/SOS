package com.swansea.sindiso;

import java.util.Date;
import java.util.List;

public class Arrangement {
    private Integer arrangementId;
    private User holder;
    private List<User> students;
    private Date collectionDate;
    private Date returnDate;

    public Arrangement(Integer arrangementId, User holder) {
        this.arrangementId = arrangementId;
        this.holder = holder;
    }

    public Integer getArrangementId() {
        return arrangementId;
    }

    public User getHolder() {
        return holder;
    }

    public List<User> getStudents() {
        return students;
    }

    public void addStudent(User student){
        students.add(student);
    }

    public Date getCollectionDate() {
        return collectionDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setCollectionDate(Date collectionDate) {
        this.collectionDate = collectionDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }
}
