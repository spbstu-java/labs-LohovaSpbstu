package Main;

import Hero.*;
import MovingType.*;

public class Main {
    public static void main(String[] args){
        Hero hero = new Hero();

        while(true) {
            System.out.println("Выберите способ перемещения героя: ");
            System.out.println("1 - пешком");
            System.out.println("2 - на лошади");
            System.out.println("3 - на машине");
            System.out.println("4 - лететь");
            System.out.println("0 - выход");
            System.out.println("Ведите цифру: ");

            try {
                String input = System.console().readLine();
                int choice = Integer.parseInt(input);

                if (choice == 0) break;

                switch (choice) {
                    case 1:
                        hero.setMoving(new OnFootMove());
                        System.out.println("Вы выбрали движение: пешком");
                        break;
                    case 2:
                        hero.setMoving(new HorseMove());
                        System.out.println("Вы выбрали движение: на лошади");
                        break;
                    case 3:
                        hero.setMoving(new CarMove());
                        System.out.println("Вы выбрали движение: на машине");
                        break;
                    case 4:
                        hero.setMoving(new FlightMove());
                        System.out.println("Вы выбрали движение: лететь");
                        break;
                    default:
                        hero.setMoving(new OnFootMove());
                        System.out.println("Движение по умолчанию: пешком");
                }

                System.out.println("Герой перемещается: ");
                hero.move();
                System.out.println(" ");
            } catch (NumberFormatException e) {
                System.out.println("Ошибка! Введите цифру!");
                System.out.println(" ");
            } catch (Exception e) {
                System.out.println("Ошибка! Произошла непредвиденная ошибка!" + e.getMessage());
                System.out.println(" ");
            }
        }
        System.out.println("Игра закончена, до новых встреч!");
    }
}
