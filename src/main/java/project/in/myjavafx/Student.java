package project.in.myjavafx;

/*

 */
public class Student {

    // We have declared instance variables to hold student information
    String usn;//Student USN
    String sname; //Student Name
    Integer sem; //Student Semester
    String branch; //Branch of Study

    String gender; //Gender of the student
    String participation; //Participating event of the student

    //Student constructor to initialize a Student object with the given parameters
    public Student(String usn, String sname, Integer sem, String branch,String gender, String participation) {
        this.usn = usn;
        this.sname = sname;
        this.sem = sem;
        this.branch = branch;
        this.gender=gender;
        this.participation=participation;
    }

    // Setter method to set the USN
    public void setUsn(String usn) {
        this.usn = usn;
    }

    // Setter method to set the Student Name
    public void setName(String sname) {
        this.sname = sname;
    }

    // Setter method to set the Student Semester
    public void setSem(Integer sem) {
        this.sem = sem;
    }

    // Setter method to set the Branch
    public void setBranch(String branch) {
        this.branch = branch;
    }

    // Setter method to set the Gender
    public  void setGender(String gender){
        this.gender=gender;
    }

    // Setter method to set the Participation
    public void setParticipation(String participation){
        this.participation=participation;
    }

    // Getter method to get the USN
    public String getUsn() {
        return usn;
    }


    // Getter method to get the Student Name
    public String getSname() {
        return sname;
    }

    // Getter method to get the Semester
    public Integer getSem() {
        return sem;
    }


    // Getter method to get the Branch
    public String getBranch() {
        return branch;
    }

    // Getter method to get the Gender
    public String getGender(){return gender;}

    // Getter method to get the Participation
    public String getParticipation(){return participation;}
}
