public class Course {
    private Barrier[] barriers;

    public Course(Barrier[] barriers) {
        this.barriers = barriers;
    }

    public void doIt(Team team) {
        Movable[] teamMembers = team.getTeamMembers();

        for (Movable member : teamMembers) {
            for (Barrier barrier : barriers) {
                if (!barrier.overcome(member)) {
                    break;
                }
            }
        }

    }
}
