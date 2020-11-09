package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

public class Main {
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();

        userService.dropUsersTable();
        userService.createUsersTable();

        userService.saveUser("Folko", "Brandybuck", (byte) 25);
        userService.saveUser("Frodo", "Baggins",  (byte) 60);
        userService.saveUser("Bilbo", "Baggins", (byte) 100);
        userService.saveUser("Gendalf", "the Gray", (byte) 120);

        System.out.println();
        for (User user : userService.getAllUsers()) {
            System.out.println(user.toString());
        }

        userService.cleanUsersTable();
//        userService.dropUsersTable();
    }
}
