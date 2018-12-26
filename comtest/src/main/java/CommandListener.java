import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CommandListener {

    public static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws IOException, SQLException {
        String command;
        JDBC.createTables();
        addProfessors();
        System.out.println("Для добавления студентов наберите addStudent");
        System.out.println("Для переклички наберите rollCall");
        System.out.println("Для выбора старосты наберите voteChef");
        System.out.println("Для завершения наберите exit");
        while(true){
            command = reader.readLine();
            if (command.equals("addStudent")) {
                addStudents();
            }else if (command.equals("rollCall")) {
                rollCall();
            }else if (command.equals("voteChef")) {
                voteChef();
            }else if (command.equals("exit")){
                break;
            } else System.out.println("Повторите ввод!");
        }
    }

    public static void addStudents() throws IOException, SQLException {
        String groupName;
        String feature;
        System.out.println("Давайте добавим студентов.");
        while (true){
            System.out.println("Для завершения наберите exit, для добавления add.");
            String command = reader.readLine();
            if (command.equals("add")){
                System.out.println("Введите имя студента");
                String firstName = reader.readLine();
                System.out.println("Введите фамилию студента");
                String lastName = reader.readLine();
                System.out.println("Умный, добрый или красивый?");
                while (true){
                    feature = reader.readLine();
                    if(feature.equals("Умный") || feature.equals("Добрый") || feature.equals("Красивый")){
                        break;
                    }
                    else System.out.println("Повторите ввод!");
                }
                while (true){
                    System.out.println("Введите группу в которой учится студент");
                    ResultSet groups = JDBC.execute("select * from Students.professor");
                    System.out.println("Список доступных групп");
                    while (groups.next()){
                        System.out.println(groups.getString("groupName"));
                    }
                    groupName = reader.readLine();
                    ResultSet resultSet = JDBC.execute("select * from Students.professor where groupName='"+ groupName +"'");
                    if (!resultSet.next()){
                        System.out.println("Такой группы не существует повторите ввод!");
                    } else break;
                }
                JDBC.command("insert into Students.student (firstName, lastName, groupName, isHeadman, professor, isEmpty, feature, votes) " +
                        "values ('"+firstName+"', '"+lastName+"', '"+groupName+"', '0', (select lastName from Students.professor where groupName='"+groupName+"'), '1', '"+feature+"', '0')");
            } else if(command.equals("exit")){
                break;
            } else System.out.println("Повторите ввод.");
        }
    }

    public static void addProfessors() throws IOException {
        System.out.println("Добавим кураторов и группы которые они ведут.");
        while (true) {
            System.out.println("Для завершения наберите exit, для добавления add.");
            String command = reader.readLine();
            if (command.equals("add")) {
                System.out.println("Введите имя куратора");
                String firstName = reader.readLine();
                System.out.println("Введите фамилию куратора");
                String lastName = reader.readLine();
                System.out.println("Введите группу которую ведет куратор");
                String groupName = reader.readLine();
                JDBC.command("insert into Students.professor " +
                        "(firstName, lastName, groupName) values ('" + firstName + "', '" + lastName + "', '" + groupName + "')");
            } else if (command.equals("exit")) {
                break;
            } else System.out.println("Повторите ввод.");
        }
    }

    public static void rollCall() throws IOException, SQLException {
        JDBC.command("update Students.student set isEmpty='0'");
        System.out.println("Введите название группы в которой проведем перекличку.");
        String groupName;
        while (true){
            groupName = reader.readLine();
            ResultSet resultSet = JDBC.execute("select * from Students.student where groupName='" + groupName + "'");
            while (resultSet.next()) {
                String lastName = resultSet.getString("lastName");
                System.out.println("Студент "+ lastName +" присутствует? Вводите yes или no.");
                while (true) {
                    String result = reader.readLine();
                    if (result.equals("yes")){
                        JDBC.command("update Students.student set isEmpty='0' where lastName='"+lastName+"'");
                        break;
                    } else if (result.equals("no")){
                        JDBC.command("update Students.student set isEmpty='1' where lastName='"+lastName+"'");
                        break;
                    } else System.out.println("Повторите ввод!");
                }
            }
            if (!resultSet.next()){
                break;
            }
        }
        ResultSet resultSet = JDBC.execute("select * from Students.student where groupName='" + groupName + "' and isEmpty='1'");
        while (resultSet.next()){
            System.out.println("Студент " + resultSet.getString("lastName")  + " отсутствует");
        }
    }
    public static void voteChef() throws SQLException, IOException {
        String group;
        String voteCharacter;
        String finalVoteCharacter;
        ArrayList groups = new ArrayList();
        ArrayList candidates = new ArrayList();
        ArrayList students = new ArrayList();
        System.out.println("В какой группе выберем старосту? Список групп:");
        ResultSet groups_rc = JDBC.execute("select distinct student.groupName from Students.student");
        while (groups_rc.next()){
            groups.add(groups_rc.getString("groupName"));
            System.out.println(groups_rc.getString("groupName")+" ");
        }
        while (true){
            group = reader.readLine();
            if(groups.contains(group)){
                break;
            }
            else System.out.println("Группа не существует, повторите ввод.");
        }
        System.out.println("Будем голосовать за умного(1), доброго(2) или красивого(3)?");
        while (true){
            voteCharacter = reader.readLine();
            if (voteCharacter.equals("1")){
                finalVoteCharacter = "Умный";
                break;
            } else if (voteCharacter.equals("2")){
                finalVoteCharacter = "Добрый";
                break;
            } else if (voteCharacter.equals("3")){
                finalVoteCharacter = "Красивый";
                break;
            } else System.out.println("Повторите ввод!");
        }
        ResultSet studentsFromGroup = JDBC.execute("select * from Students.student where groupName='"+group+"'");
        while (studentsFromGroup.next()){
            students.add(studentsFromGroup.getString("lastName"));
        }
        System.out.println("Претендеты на старосту:");
        ResultSet candidatesFromGroup = JDBC.execute("select * from Students.student where groupName='"+group+"' and feature='"+finalVoteCharacter+"'");
        if (!candidatesFromGroup.next()){
            System.out.println("Нет кандидатов по заданному критерию!");
        }
        while (candidatesFromGroup.next()){
            System.out.println(candidatesFromGroup.getString("lastName"));
            candidates.add(candidatesFromGroup.getString("lastName"));
        }
        studentsFromGroup = JDBC.execute("select * from Students.student where groupName='"+group+"'");
        JDBC.command("update Students.student set votes=0");
        while (studentsFromGroup.next()){
            System.out.println(studentsFromGroup.getString("lastName") + " голосует за? (вводите фамилию кандидата)");
            String vote;
            while (true){
                vote = reader.readLine();
                if(candidates.contains(vote)){
                    JDBC.command("update Students.student set votes=votes+1 where lastName='"+vote+"'");
                    break;
                }else System.out.println("Повторите ввод, такого кандидата нет.");
            }
        }
        ResultSet res = JDBC.execute("select lastName from Students.student where votes=(select max(votes) from Students.student)");
        while (res.next()){
            System.out.println("Старостой назначен " + res.getString("lastName"));
        }
    }
}
