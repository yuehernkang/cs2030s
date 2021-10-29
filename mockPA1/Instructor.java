class Instructor {
    private String name;

    Instructor(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override 
    public String toString() {
        return this.name;
    }

    @Override
    public boolean equals(Object o) {
        if(o == this){
            return true;
        } else {
            if(o instanceof Instructor) {
                Instructor i = (Instructor) o;
                if(this.name == i.name) {
                    return true;
                }
                else {
                    return false;
                }
            }
            else {
                return false;
            }
        }
    }
}
