class Tutorial extends Classes {
    private final String moduleCode;
    private final int classId;
    private final String venueId;
    private final Instructor instructor;
    private final int startTime;

    Tutorial(String moduleCode, int classId, String venueId, Instructor instructor, int startTime) {
        super(moduleCode, classId, venueId, instructor, startTime);
        this.moduleCode = moduleCode;
        this.classId = classId;
        this.venueId = venueId;
        this.instructor = instructor;
        this.startTime = startTime;
    }
}
