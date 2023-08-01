import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class WeatherApp {

    private static final String API_BASE_URL = "https://samples.openweathermap.org/data/2.5/forecast/hourly";
    private static final String API_KEY = "b6907d289e10d714a6e88b30761fae22";

    public static void main(String[] args) {
        Scanner sc= new Scanner(System.in);
        String city = "London,us";

        int choice;

        do {
            printMenu();
            choice = sc.nextInt();
            sc.nextLine(); 

            switch (choice) 
            {
                case 1:
                    getAndPrintWeatherData(city, "temp", sc);
                    break;
                case 2:
                    getAndPrintWeatherData(city, "wind.speed", sc);
                    break;
                case 3:
                    getAndPrintWeatherData(city, "pressure", sc);
                    break;
                case 0:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        while (choice != 0);
    }

    private static void printMenu() 
    {
        System.out.println("1. Get weather");
        System.out.println("2. Get Wind Speed");
        System.out.println("3. Get Pressure");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void getAndPrintWeatherData(String city, String field, Scanner sc) 
    {
        System.out.print("Enter the date (YYYY-MM-DD): ");
        String date = sc.nextLine();
        String apiUrl = API_BASE_URL + "?q=" + city + "&appid=" + API_KEY;

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) 
            {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) 
                {
                    response.append(line);
                }
                reader.close();

                String json = response.toString();
                int index = json.indexOf("\"dt_txt\":\"" + date);
                if (index != -1) {
                    int startIndex = json.indexOf("\"" + field + "\":", index);
                    int endIndex = json.indexOf(",", startIndex);
                    if (startIndex != -1 && endIndex != -1) 
                    {
                        String valueStr = json.substring(startIndex + 3 + field.length(), endIndex).trim();
                        System.out.println(field + ": " + valueStr);
                    } 
                    else 
                    {
                        System.out.println("Field '" + field + "' not found in the weather data.");
                    }
                } 
                else
                {
                    System.out.println("Date '" + date + "' not found in the weather data.");
                }
            }
            else 
            {
                System.out.println("Error fetching weather data. Status Code: " + responseCode);
            }

            connection.disconnect();
        } 
        catch (IOException e) 
        {
            System.out.println("Error connecting to the API: " + e.getMessage());
        }
    }
}
