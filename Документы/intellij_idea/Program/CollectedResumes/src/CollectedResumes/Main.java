package CollectedResumes;

import CollectedResumes.WindowApp.WindowApp;

/**
 * Program: Main.Parser "https://zarplata.ru/resume" saving to database
 * @author tematwelve@gmail.com
 * - first name: Artem
 * - last name: Komarov
 * @version 1.0
 * zawotobuxa@hostcalls.com
 * icebeer
 *
 * ;)
 */

public class Main {

//    private static DataBase dataBase;

    public static void main(String[] args) {
//        collectedResume();
        new WindowApp();
    }

//    private static void collectedResume() {
//        try {
//            dataBase = new DataBase();
//            dataBase.setPrioritySize(100);
//            dataBase.run();
//            Parser parser = new Parser(dataBase);
//            parser.run();
//            new WindowApp(dataBase, parser);
//        } catch (Exception e) {
//            e.printStackTrace();

            //PageSource.close();
//            dataBase.close();
//        }
//    }

}
