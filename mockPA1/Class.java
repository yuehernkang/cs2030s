abstract class Classes {
    private final String moduleCode;
    private final int classId;
    private final String venueId;
    private final Instructor instructor;
    private final int startTime;

    Classes(String moduleCode, int classId, String venueId, Instructor instructor, int startTime) {
        this.moduleCode = moduleCode;
        this.classId = classId;
        this.venueId = venueId;
        this.instructor = instructor;
        this.startTime = startTime;
    }

    public boolean hasSameModule(Classes c) {
        if(c.moduleCode == this.moduleCode) {
            return true;
        } else {
            return false;
        }
    }

    public boolean hasSameInstructor(Classes c) {
        if(c.instructor.getName() == this.instructor.getName()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean hasSameVenue(Classes c) {
        if(c.venueId == this.venueId) {
            return true;
        } else {
            return false;
        }
    }

    public boolean clashWith(Classes c) {
        if(c instanceof ) {
            return true;
        }
    }
}
