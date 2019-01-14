package CollectedResumes.Parser;

import CollectedResumes.DataBase.DataBase;
import CollectedResumes.WindowApp.WindowApp;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parser the values. Saves in the database.
 */
public class Parser {

    private WindowApp windowApp;
    private DataBase dataBase;

    private PageSource pageSource = new PageSource();

    /**
     * Parser the runs.
     */
    private boolean parser = false;

    /**
     * Save the ID of the parser.
     */
    private int id = 0;

    /**
     * Save the title resume of the parser.
     */
    private String titleResume = "null";

    /**
     * Save the URL resume of the parser.
     */
    private String urlResume = "null";

    /**
     * Save the salary of the worker of the parser.
     */
    private int salary = 0;

    /**
     * Save the experience in years of the worker of the parser.
     */
    private String experienceYear = "null";

    /**
     * Save the education of the worker of the parser.
     */
    private String education = "null";

    /**
     * Save the name of the worker of the parser.
     */
    private String name = "null";

    /**
     * Save the age of the worker of the parser.
     */
    private int age = 0;

    /**
     * Save the city of the worker of the parser.
     */
    private String city = "null";

    /**
     * Save glued the line the location of the old work of the parser.
     */
    private String oldWorkLocation = "null";

    /**
     * Save glued the line what worked of the last job of the parser.
     */
    private String oldWorkWho = "null";

    /**
     * Save glued the line the time work of the old work of the parser.
     */
    private String oldWorkTime = "null";

    /**
     * Save glued the line the specification of the old work of the parser.
     */
    private String oldWorkSpecification = "null";

    /**
     * Save the key skill of the worker of the parser.
     */
    private String keySkill = "null";

    /**
     * Save update time resume of the parser.
     */
    private long timeUpdate = 0;

    /**
     * Constructor.
     */
    public Parser(WindowApp windowApp, DataBase dataBase) {
        this.windowApp = windowApp;
        this.dataBase = dataBase;
    }

    public void run() {
        if (dataBase.getSize() < dataBase.getPrioritySize()) {
            for (int page = 0; page <= 9975; page++) {
                parser = true;
                windowApp.getPanelInformation().visibilityPanelCondition(true);

                parser(page);

                parser = false;
                windowApp.getPanelInformation().visibilityPanelCondition(false);

                if (dataBase.getSize() == dataBase.getPrioritySize()) {
                    break;
                }
            }
            pageSource.close();
        }
    }

    /**
     * Parser the elements.
     *
     * @param page The number of page.
     */
    private void parser(final int page) {
        parserPrepare();

        String str = pageSource.resume(page);
        System.out.println(str);
        Document pageResume = Jsoup.parse(str);
        Elements elements = pageResume.getElementsByAttributeValue("class", "ui segment resume resume-item");
        for (Element element : elements) {
            parserPrepare();

            parserId(element);

            if (dataBase.isEqual(id)) {
                continue;
            }

            parserUrlResume(element);
            parserTitleResume(element);
            parserSalary(element);
            parserExperienceYear(element);
            parserEducation(element);
            parserNameAge(element);
            parserCity(element);
            parserOldWork(element);
            parserKeySkillTimeUpdate(element);

            System.out.println(id + " " +
                    urlResume + " " +
                    titleResume + " " +
                    salary + " " +
                    experienceYear + " " +
                    education + " " +
                    name + " " +
                    age + " " +
                    city + " " +
                    oldWorkLocation + " " +
                    oldWorkWho + " " +
                    oldWorkTime + " " +
                    oldWorkSpecification + " " +
                    keySkill + " " +
                    timeUpdate);

            dataBase.write(id,
                    urlResume,
                    titleResume,
                    salary,
                    experienceYear,
                    education,
                    name,
                    age,
                    city,
                    oldWorkLocation,
                    oldWorkWho,
                    oldWorkTime,
                    oldWorkSpecification,
                    keySkill,
                    timeUpdate);

            if (dataBase.getSize() == dataBase.getPrioritySize()) {
                break;
            }
        }
    }

    private final Object monitor = new Object();

    /**
     * Open thread for the parser.
     */
    public void parserSend() {
        synchronized (monitor) {
            if (!windowApp.isClosing() && !windowApp.getPanelSearch().isSearching() && !windowApp.getPanelContent().isShow()) {
                monitor.notify();
            }
        }
    }

    /**
     * Waiting while the other thread.
     */
    private void parserPrepare() {
        try {
            prepareClosing();
            prepareSearching();
            prepareVisibility();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Waiting while the app closing.
     *
     * @throws InterruptedException Monitor wait.
     */
    private void prepareClosing() throws InterruptedException {
        synchronized (monitor) {
            while (windowApp.isClosing()) {
                parser = false;

                pageSource.close();

                windowApp.getPanelInformation().visibilityPanelCondition(false);

                windowApp.closingSend();

                monitor.wait();
            }
        }
    }

    /**
     * Waiting while the searching.
     *
     * @throws InterruptedException Monitor wait.
     */
    private void prepareSearching() throws InterruptedException {
        synchronized (monitor) {
            while (windowApp.getPanelSearch().isSearching()) {
                parser = false;
                windowApp.getPanelInformation().visibilityPanelCondition(false);

                windowApp.getPanelSearch().wake();

                monitor.wait();
            }
        }
    }

    /**
     * Waiting while of content the visibility.
     *
     * @throws InterruptedException Monitor wait.
     */
    private void prepareVisibility() throws InterruptedException {
        synchronized (monitor) {
            while (windowApp.getPanelContent() != null && windowApp.getPanelContent().isShow()) {
                parser = false;
                windowApp.getPanelInformation().visibilityPanelCondition(false);

                windowApp.getPanelContent().displayedSend();

                monitor.wait();
            }
        }
    }

    /**
     * Parser the id.
     *
     * @param element The element found on the page.
     */
    private void parserId(final Element element) {
        String parserId = element.child(0).attr("name");
        parserId = parserId.trim();
        id = Integer.valueOf(parserId);
    }

    /**
     * Parser the URL resumes.
     *
     * @param element The element found on the page.
     */
    private void parserUrlResume(final Element element) {
        urlResume = element.child(1).child(1).child(0).child(0).
                child(0).child(0).child(0).child(0).child(0).child(0).attr("href");

        urlResume = urlResume.trim();
    }

    /**
     * Parser the title resumes.
     *
     * @param element The element found on the page.
     */
    private void parserTitleResume(final Element element) {
        titleResume = element.child(1).child(1).child(0).child(0).
                child(0).child(0).child(0).child(0).child(0).child(0).text();

        titleResume = titleResume.trim();
        titleResume = checkApostrophe(titleResume);
    }

    /**
     * Parser the salary of the worker.
     *
     * @param element The element found on the page.
     */
    private void parserSalary(final Element element) {
        String parserSalary = element.child(1).child(1).child(0).child(0).
                child(0).child(0).child(1).child(0).child(0).text();

        parserSalary = parserSalary.trim();

        patternSalary(parserSalary);
    }

    /**
     * Find the number in the line (salary).
     *
     * @param parserSalary The line (salary) parser.
     */
    private void patternSalary(final String parserSalary) {
        StringBuilder builderSalary = new StringBuilder();

        Pattern p = Pattern.compile("-?\\d+");
        Matcher m = p.matcher(parserSalary);

        while (m.find()) {
            builderSalary.append(m.group());
        }

        if (builderSalary.length() != 0) {
            salary = Integer.valueOf(builderSalary.toString());
        }
    }

    /**
     * Parser the experience in years of the worker.
     *
     * @param element The element found on the page.
     */
    private void parserExperienceYear(final Element element) {
        experienceYear = element.child(1).child(1).child(0).child(0).
                child(0).child(0).child(1).child(0).child(1).text();

        experienceYear = experienceYear.trim();
    }

    /**
     * Parser the education of the worker.
     *
     * @param element The element found on the page.
     */
    private void parserEducation(final Element element) {
        education = element.child(1).child(1).child(0).child(0).
                child(0).child(0).child(1).child(0).child(2).text();

        education = education.trim();
    }

    /**
     * Parser the name and the age of the worker.
     *
     * @param element The element found on the page.
     */
    private void parserNameAge(final Element element) {
        parserName(element);
        parserAge();
    }

    /**
     * Parser the name of the worker.
     *
     * @param element The element found on the page.
     */
    private void parserName(final Element element) {
        name = element.child(1).child(1).child(0).child(0).
                child(0).child(0).child(0).child(1).text();

        name = name.trim();
        name = checkApostrophe(name);
    }

    /**
     * Parser the age of the worker.
     */
    private void parserAge() {
        if (name.matches(".+\\d+.*")) {
            String[] split = name.split("[^0-9]+");
            age = Integer.valueOf(split[1]);

            split = name.split(",");
            name = split[0];
        }
    }

    /**
     * Parser the city of the worker.
     *
     * @param element The element found on the page.
     */
    private void parserCity(final Element element) {
        city = element.child(1).child(1).child(0).child(0).
                child(0).child(0).child(0).child(2).child(0).child(1).text();

        city = city.trim();
        city = checkApostrophe(city);
    }

    /**
     * Parser the old work.
     *
     * @param element The element found on the page.
     */
    private void parserOldWork(final Element element) {
        int numberOldWork = element.child(1).child(1).child(0).child(0).
                child(2).child(0).child(1).childNodeSize(); // number of the old work

        if (numberOldWork > 1) {
            int typesOldWork = element.child(1).child(1).child(0).child(0).
                    child(2).child(0).child(1).child(0).childNodeSize();

            if (typesOldWork == 4) {
                parserOldWorkLocation(element);
                parserOldWorkWho(element);
                parserOldWorkTime(element);
                parserOldWorkSpecification(element);
            }
        }
    }

    /**
     * Parser the location of the old work.
     *
     * @param element The element found on the page.
     */
    private void parserOldWorkLocation(final Element element) {
        oldWorkLocation = element.child(1).child(1).child(0).child(0).
                child(2).child(0).child(1).child(0).child(0).child(1).text();

        oldWorkLocation = oldWorkLocation.trim();
        oldWorkLocation = checkApostrophe(oldWorkLocation);
    }

    /**
     * Parser what worked of last job.
     *
     * @param element The element found on the page.
     */
    private void parserOldWorkWho(final Element element) {
        oldWorkWho = element.child(1).child(1).child(0).child(0).
                child(2).child(0).child(1).child(0).child(1).child(1).text();

        oldWorkWho = oldWorkWho.trim();
        oldWorkWho = checkApostrophe(oldWorkWho);
    }

    /**
     * Parser the time work of the old work.
     *
     * @param element The element found on the page.
     */
    private void parserOldWorkTime(final Element element) {
        oldWorkTime = element.child(1).child(1).child(0).child(0).
                child(2).child(0).child(1).child(0).child(2).child(1).child(0).text();

        oldWorkTime = oldWorkTime.trim();
    }

    /**
     * Parser the specification of the old work.
     *
     * @param element The element found on the page.
     */
    private void parserOldWorkSpecification(final Element element) {
        oldWorkSpecification = element.child(1).child(1).child(0).child(0).
                child(2).child(0).child(1).child(0).child(3).child(0).text();

        oldWorkSpecification = oldWorkSpecification.trim();
        oldWorkSpecification = checkApostrophe(oldWorkSpecification);
    }

    /**
     * Parser the key skill of the worker and the update time resume.
     *
     * @param element The element found on the page.
     */
    private void parserKeySkillTimeUpdate(final Element element) {
        if (element.child(1).child(1).child(0).child(0).child(2).child(0).child(2).childNodeSize() == 2) {
            parserKeySkill(element);
            parserTimeUpdate(element, 1);
        } else {
            parserTimeUpdate(element, 0);
        }
    }

    /**
     * Parser the key skill of the worker.
     *
     * @param element The element found on the page.
     */
    private void parserKeySkill(final Element element) {
        keySkill = element.child(1).child(1).child(0).child(0).
                child(2).child(0).child(2).child(0).child(1).text();

        keySkill = keySkill.trim();
        keySkill = checkApostrophe(keySkill);
    }

    /**
     * Parser the update time resume.
     *
     * @param element The element found on the page;
     * @param childLocation the location of the child.
     */
    private void parserTimeUpdate(final Element element, final int childLocation) {
        String parserTimeUpdate = element.child(1).child(1).child(0).child(0).
                child(2).child(0).child(2).child(childLocation).child(0).text();

        parserTimeUpdate = parserTimeUpdate.trim();

        try {
            timeUpdate = new SimpleDateFormat("dd MMMM yyyy").parse(parserTimeUpdate).getTime() / 1000L;
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private String checkApostrophe(String str) {
        return str.replace("'", "");
    }

    public boolean isParser() {
        return parser;
    }

}
