/**
 * Created by Kitabayashi Akira (16302016001)  on 11/2016
 * Project-1 Animal
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    //Main method,contains main value and main structure.
    public static void main(String[] args) {
        //load txt into string.
        File animalFile = new File("animal.txt");
        String[] animalString = read(animalFile);
        File tileFile = new File("tile.txt");
        String[] tileString = read(tileFile);
        char[][] tileMap = new char[7][9];
        char[][][] mapHistroy = new char[100][7][9];

        // initialize char[][].
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 9; j++) {
                tileMap[i][j] = '0';
                mapHistroy[0][i][j] = '0';
            }
        }

        // String to char[][]mapHistroy.
        tileMap = load(tileMap, tileString);
        mapHistroy[0] = load(mapHistroy[0], animalString);
        mapHistroy[0] = load(mapHistroy[0], tileString);

        //Main structure.
        printHelp();
        int currentStep = 0, lastStep = 0;//Used to log mapHistroy.
        boolean player = true;//True:left player;False:right player.
        Scanner scanner = new Scanner(System.in);
        while (mapHistroy[currentStep][3][0] == '3' & mapHistroy[currentStep][3][8] == '5') {//whether the home is occupied.
            //whether there is no animal exists.
            int left = 0, right = 0;
            for (int i = 0; i < 7; i++) {
                for (int j = 0; j < 9; j++) {
                    if (mapHistroy[currentStep][i][j] >= 'a' & mapHistroy[currentStep][i][j] <= 'h') {
                        left++;
                    } else if (mapHistroy[currentStep][i][j] >= 'i' & mapHistroy[currentStep][i][j] <= 'p') {
                        right++;
                    }
                }
            }
            if (left == 0) {
                System.out.println("Right win");
                System.exit(0);
            } else if (right == 0) {
                System.out.println("Left win");
                System.exit(0);
            }

            printBoard(mapHistroy[currentStep]);
            if (player == true) {
                System.out.println("Left turn:");
            } else if (player == false) {
                System.out.println("Right turn:");
            }
            int nextStep;
            String order = scanner.next();
            //classify order.
            if (order.equals("end")) {
                System.exit(0);
            } else if (order.equals("restart")) {
                currentStep = 0;
                lastStep = 0;
                printHelp();
            } else if (order.equals("help")) {
                printHelp();
            } else if (order.charAt(0) == 'u') { //undo  steps.
                nextStep = currentStep - order.length();
                currentStep = undo(currentStep, nextStep);
                if (order.length() % 2 == 1) {// Adjust the turn.
                    player = !player;
                }
            } else if (order.charAt(0) == 'r') {// cancel  undo .
                nextStep = currentStep + order.length();
                currentStep = redo(currentStep, lastStep, nextStep);
                if (order.length() % 2 == 1) {//Adjust the turn.
                    player = !player;
                }
            } else {
                if ((order.charAt(0) <= '8' & order.charAt(0) >= '1') & order.length() == 2) {
                    lastStep = ++currentStep;
                    mapHistroy[currentStep] = copyArray(mapHistroy[currentStep - 1]);
                    animalMove(mapHistroy[currentStep], tileMap, order, player);
                } else {
                    System.out.println("Please input animal number + direction.");
                }
            }
            //If the map did'nt change,it means the player met some trouble ,so the turn should'nt change.
            int x = 0;//"x" is a value to log how many animals did'nt change between currentStep and currentStep-1.If x=7*9,it means map did'nt change.
            if (currentStep > 0) {
                for (int i = 0; i < 7; i++) {
                    for (int j = 0; j < 9; j++) {
                        if (mapHistroy[currentStep][i][j] == mapHistroy[currentStep - 1][i][j]) {
                            x++;
                        } else {
                        }
                    }
                }
                if (x == 63) {
                    currentStep--;
                } else {
                    player = !player;
                }
            }
        }
        System.out.print("Game over");
    }

    /**
     * This method is used to read txt.
     */
    private static String[] read(File file) {
        String[] string = new String[9];
        Scanner input = null;
        try {
            input = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        string[0] = input.next();
        string[1] = input.next();
        string[2] = input.next();
        string[3] = input.next();
        string[4] = input.next();
        string[5] = input.next();
        string[6] = input.next();
        return string;
    }

    /**
     * Load string into char[7][9].
     */
    private static char[][] load(char[][] map, String[] string) {
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 9; j++) {
                if (map[i][j] == '0') {
                    map[i][j] = string[i].charAt(j);
                } else {
                }
            }
        }
        return map;
    }

    /**
     * Print char[7][9].
     */
    private static void printBoard(char[][] map) {
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 9; j++) {
                if (map[i][j] == '0') {
                    System.out.print(" 　 ");
                } else if (map[i][j] == '1') {
                    System.out.print(" 水 ");
                } else if ((map[i][j] == '2' || map[i][j] == '4')) {
                    System.out.print(" 陷 ");
                } else if (map[i][j] == '3' || map[i][j] == '5') {
                    System.out.print(" 家 ");
                } else if (map[i][j] == 'a') {
                    System.out.print("1鼠 ");
                } else if (map[i][j] == 'b') {
                    System.out.print("2猫 ");
                } else if (map[i][j] == 'c') {
                    System.out.print("3狼 ");
                } else if (map[i][j] == 'd') {
                    System.out.print("4狗 ");
                } else if (map[i][j] == 'e') {
                    System.out.print("5豹 ");
                } else if (map[i][j] == 'f') {
                    System.out.print("6虎 ");
                } else if (map[i][j] == 'g') {
                    System.out.print("7狮 ");
                } else if (map[i][j] == 'h') {
                    System.out.print("8象 ");
                } else if (map[i][j] == 'i') {
                    System.out.print(" 鼠1");
                } else if (map[i][j] == 'j') {
                    System.out.print(" 猫2");
                } else if (map[i][j] == 'k') {
                    System.out.print(" 狼3");
                } else if (map[i][j] == 'l') {
                    System.out.print(" 狗4");
                } else if (map[i][j] == 'm') {
                    System.out.print(" 豹5");
                } else if (map[i][j] == 'n') {
                    System.out.print(" 虎6");
                } else if (map[i][j] == 'o') {
                    System.out.print(" 狮7");
                } else if (map[i][j] == 'p') {
                    System.out.print(" 象8");
                }
            }
            System.out.println();
        }
    }

    /**
     * Copy one array to another.
     */
    private static char[][] copyArray(char[][] array) {
        char[][] newArray = new char[7][9];
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 9; j++) {
                newArray[i][j] = array[i][j];
            }
        }
        return newArray;
    }

    /**
     * This method is used to control animals' movement.
     * It will return current map(char[][]) after the player played one step.
     */
    private static char[][] animalMove(char[][] map, char[][] tileMap, String order, boolean player) {
        char animal = '0';
        char order1 = order.charAt(0);
        char order2 = order.charAt(1);
        //Decide animal's level.
        if (order1 < '9' & order1 >= '1') {// This "if()" is used to judge order's format.
            if (order2 == 'w' || order2 == 'a' || order2 == 's' || order2 == 'd') {
                if (player == true) {
                    if (order1 == '1') {
                        animal = 'a';
                    } else if (order1 == '2') {
                        animal = 'b';
                    } else if (order1 == '3') {
                        animal = 'c';
                    } else if (order1 == '4') {
                        animal = 'd';
                    } else if (order1 == '5') {
                        animal = 'e';
                    } else if (order1 == '6') {
                        animal = 'f';
                    } else if (order1 == '7') {
                        animal = 'g';
                    } else if (order1 == '8') {
                        animal = 'h';
                    }
                } else {//Right player.
                    if (order1 == '1') {
                        animal = 'i';
                    } else if (order1 == '2') {
                        animal = 'j';
                    } else if (order1 == '3') {
                        animal = 'k';
                    } else if (order1 == '4') {
                        animal = 'l';
                    } else if (order1 == '5') {
                        animal = 'm';
                    } else if (order1 == '6') {
                        animal = 'n';
                    } else if (order1 == '7') {
                        animal = 'o';
                    } else if (order1 == '8') {
                        animal = 'p';
                    }
                }
            } else {
                System.out.println("Cannot read your order.");
            }
        } else {
            System.out.println("No such animal.");
        }
        int x = 0, y = 0;
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 9; j++) {
                if (map[i][j] == animal) {
                    x = i;
                    y = j;
                }
            }
        }
        //Out of map judgement.
        if (order2 == 'w' & x == 0) {
            movementWarning();
        } else if (order2 == 'a' & y == 0) {
            movementWarning();
        } else if (order2 == 's' & x == 6) {
            movementWarning();
        } else if (order2 == 'd' & y == 8) {
            movementWarning();
        }
        //usual animal
        if ((animal > 'a' & animal < 'f') || (animal > 'i' & animal < 'n') || animal == 'h' || animal == 'p') {
            if (order2 == 'w' & x != 0) {
                if (compare(order1, map[x - 1][y], tileMap, x - 1, y) || tileMap[x - 1][y] == '1' || leftOrRight(map[x - 1][y], map[x][y])) {
                    movementWarning();
                } else {
                    map[x - 1][y] = map[x][y];
                    map[x][y] = tileMap[x][y];
                }
            } else if (order2 == 'a' & y != 0) {
                if (compare(order1, map[x][y - 1], tileMap, x, y - 1) || tileMap[x][y - 1] == '1' || leftOrRight(map[x][y - 1], map[x][y])) {
                    movementWarning();
                } else {
                    map[x][y - 1] = map[x][y];
                    map[x][y] = tileMap[x][y];
                }
            } else if (order2 == 's' & x != 6) {
                if (compare(order1, map[x + 1][y], tileMap, x + 1, y) || tileMap[x + 1][y] == '1' || leftOrRight(map[x + 1][y], map[x][y])) {
                    movementWarning();
                } else {
                    map[x + 1][y] = map[x][y];
                    map[x][y] = tileMap[x][y];
                }
            } else if (order2 == 'd' & y != 8) {
                if (compare(order1, map[x][y + 1], tileMap, x, y + 1) || tileMap[x][y + 1] == '1' || leftOrRight(map[x][y + 1], map[x][y])) {
                    movementWarning();
                } else {
                    map[x][y + 1] = map[x][y];
                    map[x][y] = tileMap[x][y];
                }
            }
            //Tiger,Lion.
        } else if ((animal >= 'f' & animal < 'h') || (animal < 'p' & animal >= 'n')) {
            if (order2 == 'w' & x != 0) {
                if (tileMap[x - 1][y] != '1') {
                    if (compare(order1, map[x - 1][y], tileMap, x - 1, y) || leftOrRight(map[x - 1][y], map[x][y])) {
                        movementWarning();
                    } else {
                        map[x - 1][y] = map[x][y];
                        map[x][y] = tileMap[x][y];
                    }
                } else if (tileMap[x - 1][y] == '1') {
                    if (compare(order1, map[x - 3][y], tileMap, x - 3, y) || leftOrRight(map[x - 3][y], map[x][y])) {
                        movementWarning();
                    } else if (((animal < 'h') & (map[x - 1][y] == 'i' || map[x - 2][y] == 'i')) || ((animal > 'h') & (map[x - 1][y] == 'a' || map[x - 2][y] == 'a'))) {
                        movementWarning();
                    } else {
                        map[x - 3][y] = map[x][y];
                        map[x][y] = tileMap[x][y];
                    }
                }
            } else if (order2 == 'a' & y != 0) {
                if (tileMap[x][y - 1] != '1') {
                    if (compare(order1, map[x][y - 1], tileMap, x, y - 1) || leftOrRight(map[x][y - 1], map[x][y])) {
                        movementWarning();
                    } else {
                        map[x][y - 1] = map[x][y];
                        map[x][y] = tileMap[x][y];
                    }
                } else if (tileMap[x][y - 1] == '1') {
                    if (compare(order1, map[x][y - 4], tileMap, x, y - 4) || leftOrRight(map[x][y - 4], map[x][y])) {
                        movementWarning();
                    } else if (((animal < 'h') & (map[x][y - 1] == 'i' || map[x][y - 2] == 'i' || map[x][y - 3] == 'i')) || ((animal > 'h') & (map[x][y - 1] == 'a' || map[x][y - 2] == 'a' || map[x][y - 3] == 'a'))) {
                        movementWarning();
                    } else {
                        map[x][y - 4] = map[x][y];
                        map[x][y] = tileMap[x][y];
                    }
                }
            } else if (order2 == 's' & x != 6) {
                if (tileMap[x + 1][y] != '1') {
                    if (compare(order1, map[x + 1][y], tileMap, x + 1, y) || leftOrRight(map[x + 1][y], map[x][y])) {
                        movementWarning();
                    } else {
                        map[x + 1][y] = map[x][y];
                        map[x][y] = tileMap[x][y];
                    }
                } else if (tileMap[x + 1][y] == '1') {
                    if (compare(order1, map[x + 3][y], tileMap, x + 3, y) || leftOrRight(map[x + 3][y], map[x][y])) {
                        movementWarning();
                    } else if (((animal < 'h') & (map[x + 1][y] == 'i' || map[x + 2][y] == 'i')) || ((animal > 'h') & (map[x + 1][y] == 'a' || map[x + 2][y] == 'a'))) {
                        movementWarning();
                    } else {
                        map[x + 3][y] = map[x][y];
                        map[x][y] = tileMap[x][y];
                    }
                }
            } else if (order2 == 'd' & y != 8) {
                if (tileMap[x][y + 1] != '1') {
                    if (compare(order1, map[x][y + 1], tileMap, x, y + 1) || leftOrRight(map[x][y + 1], map[x][y])) {
                        movementWarning();
                    } else {
                        map[x][y + 1] = map[x][y];
                        map[x][y] = tileMap[x][y];
                    }
                } else if (tileMap[x][y + 1] == '1') {
                    if (compare(order1, map[x][y + 4], tileMap, x, y + 4) || leftOrRight(map[x][y + 4], map[x][y])) {
                        movementWarning();
                    } else if (((animal < 'h') & (map[x][y + 1] == 'i' || map[x][y + 2] == 'i' || map[x][y + 3] == 'i')) || ((animal > 'h') & (map[x][y + 1] == 'a' || map[x][y + 2] == 'a' || map[x][y + 3] == 'a'))) {
                        movementWarning();
                    } else {
                        map[x][y + 4] = map[x][y];
                        map[x][y] = tileMap[x][y];
                    }
                }
            }
            //Mouse
        } else if (animal == 'a' || animal == 'i') {
            if (order2 == 'w' & x != 0) {
                if (compare(order1, map[x - 1][y], tileMap, x - 1, y) || leftOrRight(map[x - 1][y], map[x][y])) {
                    movementWarning();
                } else {
                    map[x - 1][y] = map[x][y];
                    map[x][y] = tileMap[x][y];
                }
            } else if (order2 == 'a' & y != 0) {
                if (compare(order1, map[x][y - 1], tileMap, x, y - 1) || leftOrRight(map[x][y - 1], map[x][y])) {
                    movementWarning();
                } else {
                    map[x][y - 1] = map[x][y];
                    map[x][y] = tileMap[x][y];
                }
            } else if (order2 == 's' & x != 6) {
                if (compare(order1, map[x + 1][y], tileMap, x + 1, y) || leftOrRight(map[x + 1][y], map[x][y])) {
                    movementWarning();
                } else {
                    map[x + 1][y] = map[x][y];
                    map[x][y] = tileMap[x][y];
                }
            } else if (order2 == 'd' & y != 8) {
                if (compare(order1, map[x][y + 1], tileMap, x, +1) || leftOrRight(map[x][y + 1], map[x][y])) {
                    movementWarning();
                } else {
                    map[x][y + 1] = map[x][y];
                    map[x][y] = tileMap[x][y];
                }
            }
        }

        return map;
    }

    /**
     * This method is used to judge whether the animal will move to an ally.
     * If it is will move to an ally ,return true.
     */
    private static boolean leftOrRight(char target, char mover) {
        if (mover >= 'a' & mover <= 'h') {
            if (target >= 'a' & target <= 'h' || target == '3') {
                return true;
            } else {
                return false;
            }
        } else {
            if (target >= 'i' & target <= 'p' || target == '5') {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * This method is used to judge whether the animal will move to an animal whose level is higher than its.
     * If higher ,return true.
     * i,j is target location.
     */
    private static boolean compare(char order1, char target, char[][] tileMap, int i, int j) {
        //Change animal char to level number.
        char targetLevel = '0';
        if (target >= 'a' & target <= 'h') {
            targetLevel = (char) (target - 48);//Example: (char)1 = a - 48. 1 = i - 48- 8.
        } else if (target >= 'i' & target <= 'p') {
            targetLevel = (char) (target - 48 - 8);
        }
        if (tileMap[i][j] == '2' & (target >= 'i' & target <= 'p')) {//When there is a trap.
            targetLevel = '0';
        } else {
        }
        if (tileMap[i][j] == '4' & (target >= 'a' & target <= 'h')) {
            targetLevel = '0';
        }
        // mouse can defeat elephant.
        if ((targetLevel == '1' & order1 == '1') || (targetLevel == '8' & order1 == '1') || (targetLevel == '8' & order1 == '8')) {
            return false;
        } else if ((targetLevel == '1' & order1 == '8')) {
            return true;
        } else {//usual animal.
            return targetLevel > order1;
        }
    }

    /**
     * Print help message.
     */
    private static void printHelp() {
        System.out.print("指令介绍:\n\n1. 移动指令\n\t移动指令由两个部分组成。\n\t第一个部分是数字1-8,根据战斗力分别对应鼠(1),猫(2),狼(3),狗(4),豹(5),虎(6),狮(7),象(8)\n\t第二个部分是字母wasd中的一个,w对应上方向,a对应左方向,s对应下方向,d对应右方向\n\t比如指令 \"1d\" 表示鼠向右走, \"4w\" 表示狗向上走\n\n2. 游戏指令\n\t输入 restart 重新开始游戏\n\t输入u悔棋，输入u撤销悔棋\n\t输入end退出\n\t输入 help 查看帮助\n\n");
    }

    /**
     * Print cannot move message.
     */
    private static void movementWarning() {
        System.out.println("Cannot move there.");
    }

    /**
     * Undo:return currentStep after undo.
     */
    private static int undo(int currentStep, int nextStep) {
        if (nextStep >= 0) {
            currentStep = nextStep;
        } else {
            System.out.println("Cannot undo.");
        }
        return currentStep;
    }

    /**
     * Redo
     */
    private static int redo(int currentStep, int lastStep, int nextStep) {
        if (nextStep <= lastStep) {
            currentStep = nextStep;
        } else {
            System.out.println("Cannot redo.");
        }
        return currentStep;
    }


}
