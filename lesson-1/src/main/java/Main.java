public class Main {
    public static void main(String[] args) {
        Movable[] teamMembers = {new Cat("Vaska", 100, 1000),
                                 new Human("Forest", 80, Integer.MAX_VALUE),
                                 new Robot("Verter", 0, 1),
                                 new Robot("Alesha", 5, 10)};
        Barrier[] barriers = {new Wall(7), new RunnigTrack(899), new Wall(78), new RunnigTrack(40000)};

        Team team = new Team(teamMembers);
        Course course = new Course(barriers);

        course.doIt(team);
    }
}
