# Harry Potter Characters App

An Android application that showcases characters from the Harry Potter universe, built with a modern Android tech stack and adhering to Clean Architecture principles.

## Screenshots

- Character List

  <img width="277" height="590" alt="image" src="https://github.com/user-attachments/assets/a450b11b-1c8d-43eb-81db-641bcf2d8cf8" />

- Character Details

  <img width="277" height="593" alt="image" src="https://github.com/user-attachments/assets/0369cea2-a9a5-4bfa-86ee-6c82d961dd23" />

- Character Search

  <img width="276" height="593" alt="image" src="https://github.com/user-attachments/assets/f20fff10-eb82-49ad-bbdd-c60e97f1998f" />


## Features

- **Browse Characters**: View a comprehensive list of characters from the Harry Potter universe.
- **Search Functionality**: Easily find specific characters by their name.
- **Detailed Information**: Get more details about each character, including their house, actor, and more.
- **Offline Caching**: The app uses a local database to cache data, providing a seamless offline experience.

## Architecture

This project follows the Clean Architecture pattern, which separates the code into distinct layers, making it more organized, scalable, and easier to test.

- **`app` (Presentation Layer)**: Handles the UI and user interactions. It is built with Jetpack Compose and uses a Model-View-ViewModel (MVVM) pattern. This layer observes the data from the Domain layer and displays it on the screen.
- **`domain` (Domain Layer)**: Contains the core business logic of the application. It is independent of any Android-specific frameworks and defines the use cases and entities for the app.
- **`data` (Data Layer)**: Manages the data sources for the application. It implements the Repository pattern to provide a single source of truth for the app's data, fetching it from either a remote API or a local database.

## Tech Stack

- **UI**: Jetpack Compose
- **Architecture**: MVVM with Clean Architecture
- **Dependency Injection**: Hilt
- **Asynchronous Operations**: Kotlin Coroutines & Flow
- **Networking**: Retrofit
- **Data Caching**: Room
- **Image Loading**: Coil
- **Navigation**: Jetpack Navigation for Compose
- **Testing**: JUnit, MockK, and Turbine

## Getting Started

To build and run this project, follow these steps:

1.  **Clone the repository**:
    ```
    git clone https://github.com/your-username/harry-potter.git
    ```
2.  **Open in Android Studio**:
    - Open the project in the latest stable version of Android Studio.
    - Let Gradle sync and download the necessary dependencies.

3.  **Run the app**:
    - Select the `app` configuration and run it on an emulator or a physical device.

## Running Tests

You can run tests from the command line or directly within Android Studio.

- **Unit Tests**: To run all local unit tests, execute the following Gradle command:
  ```
  ./gradlew test
  ```

- **Instrumented Tests**: To run UI and integration tests on an Android device or emulator, use:
  ```
  ./gradlew connectedAndroidTest
  ```

## License

This project is licensed under the Apache License 2.0. See the LICENSE file for more details.
