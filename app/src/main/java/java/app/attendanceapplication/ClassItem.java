package java.app.attendanceapplication;

public class ClassItem {
    private  long cid;

    public ClassItem(long cid, String className, String courseName) {
        this.cid = cid;
        this.className = className;
        this.courseName = courseName;
    }

    private String className;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    private String courseName;

    public ClassItem(String className, String courseName) {
        this.className = className;
        this.courseName = courseName;
    }

    public long getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }
}
