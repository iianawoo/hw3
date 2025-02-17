import java.util.Random;

public class Lesson_4_1 {
    public static int bossHealth = 700;
    public static int bossDamage = 50;
    public static String bossDefence;
    public static int[] heroesHealth = {270, 280, 250, 300, 700, 260, 270, 290};
    public static int[] heroesDamage = {20, 10, 15, 0, 5, 25, 0, 20};
    public static String[] heroesAttackType = {"Physical", "Magical", "Kinetic", "Medic", "Golem", "Lucky", "Witcher", "Thor"};
    public static int roundNumber = 0;

    public static void main(String[] args) {
        printStatistics();
        while (!isGameOver()) {
            playRound();
        }
    }

    public static boolean isGameOver() {
        if (bossHealth <= 0) {
            System.out.println("Heroes won!!!");
            return true;
        }
        boolean allHeroesDead = true;
        for (int health : heroesHealth) {
            if (health > 0) {
                allHeroesDead = false;
                break;
            }
        }
        if (allHeroesDead) {
            System.out.println("Boss won!!!");
            return true;
        }
        return false;
    }

    public static void playRound() {
        roundNumber++;
        chooseBossDefence();
        if (!thorStun()) { // проверяем, оглушен ли босс
            bossAttack();
        }
        medicHeal(); // лечение
        heroesAttack();
        printStatistics();
    }

    public static void chooseBossDefence() {
        Random random = new Random();
        int randomIndex = random.nextInt(heroesAttackType.length);
        bossDefence = heroesAttackType[randomIndex];
    }

    public static void heroesAttack() {
        for (int i = 0; i < heroesDamage.length; i++) {
            if (heroesHealth[i] > 0 && bossHealth > 0) {
                int damage = heroesDamage[i];
                if (bossDefence.equals(heroesAttackType[i])) {
                    Random random = new Random();
                    int coeff = random.nextInt(2, 11);
                    damage *= coeff;
                    System.out.println("Critical damage: " + damage);
                }
                bossHealth = Math.max(0, bossHealth - damage);
            }
        }
    }

    public static void bossAttack() {
        int golemIndex = 4;
        boolean golemAlive = heroesHealth[golemIndex] > 0;
        int golemAbsorbedDamage = 0;

        for (int i = 0; i < heroesHealth.length; i++) {
            if (heroesHealth[i] > 0) {
                if (i == 5 && new Random().nextInt(4) == 0) { // Lucky (25% шанс уклонения)
                    System.out.println("Lucky dodged the attack!");
                    continue;
                }
                int damageTaken = bossDamage;
                if (golemAlive && i != golemIndex) {
                    damageTaken -= bossDamage / 5;
                    golemAbsorbedDamage += bossDamage / 5;
                }
                heroesHealth[i] = Math.max(0, heroesHealth[i] - damageTaken);
            }
        }
        if (golemAlive) {
            heroesHealth[golemIndex] = Math.max(0, heroesHealth[golemIndex] - golemAbsorbedDamage);
        }
    }

    public static void medicHeal() {
        int medicIndex = 3;
        if (heroesHealth[medicIndex] > 0) {
            for (int i = 0; i < heroesHealth.length; i++) {
                if (i != medicIndex && heroesHealth[i] > 0 && heroesHealth[i] < 100) {
                    heroesHealth[i] += 30;
                    System.out.println("Medic healed " + heroesAttackType[i] + " by 30 HP");
                    break;
                }
            }
        }
    }

    public static boolean thorStun() {
        int thorIndex = 7;
        if (heroesHealth[thorIndex] > 0) {
            boolean isStunSuccessful = new Random().nextBoolean(); // 50% шанс оглушения
            if (isStunSuccessful) {
                System.out.println("Thor stunned the boss!");
                return true;
            }
        }
        return false;
    }

    public static void printStatistics() {
        System.out.println("ROUND " + roundNumber + " -----------------");
        System.out.println("Boss health: " + bossHealth + " damage: " +
                bossDamage + " defence: " +
                (bossDefence == null ? "No Defence" : bossDefence));
        for (int i = 0; i < heroesHealth.length; i++) {
            System.out.println(heroesAttackType[i] +
                    " health: " + heroesHealth[i] + " damage: " +
                    heroesDamage[i]);
        }
    }
}
