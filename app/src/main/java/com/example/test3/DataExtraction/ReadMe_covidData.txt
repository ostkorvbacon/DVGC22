HOW TO USE:
Global:
    private CovidData covidData;
In MainActivity:
    DataExtractor data = new DataExtractor();
    Thread downloadCovidDataThread = new Thread(data);
    downloadCovidDataThread.start();
    try {
        downloadCovidDataThread.join();
        covidData = data.getCovidData();
    } catch (InterruptedException e) {
        e.printStackTrace();
    }

Data strukturen:
CovidData - en klass som innehåller 3 listor: 
 - worldVaccineData (tom ATM) (wish, not requirement)

 - swedenCasesAndDeaths (innehåller info om antal fall och dödsfall per region och åldersgrupp)
 --- String region (namnet på länet, ex Värmland, innehåller också regionen Sverige som har totalen av alla län)
 --- ArrayList ageGroupReports (en lista där varje entry är en åldersgrupp med info om den)
 ----- String ageGroup
 ----- int cases
 ----- int deaths
 --- Finns en funktion getAgeGroupReport(String ageGroup) för att få ut en specifik åldersgrupp från regionen du är i, returnerar ett entry objekt från listan om 
       gruppen finns, annars null

 - swedenVaccine (innehåller info om vaccinationer i sverige)
 --- String region (namnet på länet, ex Värmland, innehåller också regionen Sverige som har totalen av alla län)
 --- ArrayList weeklyReports (en lista med kummulativ vaccinations data, hur många dos 1 och dos 2 har gjorts upp till och med den veckan)
 ----- int week
 ----- int year
 ----- int dose1
 ----- int dose1Quota (hur stor andel har fått minst en dos)
 ----- int dose2
 ----- int dose2Quota (hur stor andel har fått minst två doser)
 --- ArrayList ageGroupReports (en lista med info om totalt administrerade doser)
 ----- int week
 ----- int year
 ----- int dose1
 ----- int dose1Quota (hur stor andel har fått minst en dos)
 ----- int dose2
 ----- int dose2Quota (hur stor andel har fått minst två doser)
 ----- int dose1Pfizer (antal tagna första doser med Pfizer)
 ----- int dose1Moderna (antal tagna första doser med Moderna)
 ----- int dose1AstraZeneca (antal tagna första doser med AstraZeneca)
 ----- int dose2Pfizer (antal tagna andra doser med Pfizer)
 ----- int dose2Moderna (antal tagna andra doser med Moderna)
 ----- int dose2AstraZeneca (antal tagna andra doser med AstraZeneca)
 --- ArrayList distributedWeekly (en lista med info om veckovis distribuerade doser)
 ----- int pfizer
 ----- int moderna
 ----- int astraZeneca
 ----- int week
 ----- int year
 - Finns funktion weeklyReportsHasWeek(int week, int year) som returnerar true om inmatade veckan och året finns i datan annars false
 - Finns funktion weeklyReportsFindWeek(int week, int year) som returnerar indexet till veckan om den finns i datan, annars -1
 - Finns funktion distributedWeeklyHasWeek(int week, int year) som returnerar true om inmatade veckan och året finns i datan annars false
 - Finns funktion distributedWeeklyFindWeek(int week, int year) som returnerar indexet till veckan om den finns i datan, annars -1


Det finns funktion för att hitta en specifik region i listorna swedenCasesAndDeaths och swedenVaccine där man matar in länet som parameter och får tillbaka vilket index länet är på i listan, finns inte länet med i listan returnerar den -1 (alla län finns, men kan vara felstavat eller något annat fel)

All data hämtas med getters.