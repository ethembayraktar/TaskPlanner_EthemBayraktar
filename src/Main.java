public class Main {

   public static String[] sqlData = new String[]{
            "jdbc:mysql://localhost:3306/taskplannerdb", "root", "ethem2002"
    }; // Sql connection data

    public static void main(String[] args) {
        new TaskPlannerLogin();
    }
}

/** Ethem Bayraktar - Create Table statement for Sql
 *
 * CREATE TABLE `taskplannerdb`.`tasks` (
 `Id` INT NOT NULL AUTO_INCREMENT,
 `EntryDate` DATETIME NOT NULL,
 `TaskName` VARCHAR(45) NOT NULL,
 `ShortDesc` VARCHAR(200) NOT NULL,
 `Deadline` DATETIME NOT NULL,
 `Priority` INT NULL,
 `HasReminderImage` TINYINT NOT NULL,
 PRIMARY KEY (`Id`));
 *
 *
 * */