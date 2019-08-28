public class Cat implements Movable{
    private String name;
    private int abilityJump;
    private int abilityRun;
    private boolean pass = true;

    public Cat(String name, int abilityJump, int abilityRun) {
        this.name = name;
        this.abilityJump = abilityJump;
        this.abilityRun = abilityRun;
    }

    public boolean isPass() {
        return pass;
    }

    public void jump(int complexity) {
        if (abilityJump < complexity) {
            pass = false;
        }
        System.out.println("Cat name is " +name+ " jumping..." + (pass? "wow" : "faaail"));
    }

    public void run(int complexity) {
        if (abilityRun < complexity) {
            pass = false;
        }
        System.out.println("Cat name is " +name+ " running..." + (pass? "wow" : "faaail"));
    }
}
