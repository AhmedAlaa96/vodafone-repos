# Vodafone Repo

Vodafone Repo is an Android application that allows users to explore GitHub repositories. 

## Features
- Display a list of GitHub repositories
- View detailed information about each repository
- Pagination-like behavior for loading more repositories
- Repository details screen with owner, description, and star count
- Issues screen displaying a list of issues for a repository
- MVVM architecture for a clear separation of UI, ViewModel, and data logic
- Coroutines for asynchronous operations
- Room Database for efficient caching
- Unit testing for ViewModel logic and database operations
- Error handling for network errors and empty responses
- Jetpack Compose for an attractive and user-friendly interface
- Smooth transitions and animations for enhanced user experience

## Installation
1. Clone the repository from GitHub: `git clone https://github.com/AhmedAlaa96/vodafone-repos.git`
2. Open the project in Android Studio.
3. Build and run the app on an emulator or a physical device.

### Prerequisites
- Android Studio (version Electric Eel (2022.1.1) or higher)

## Technologies Used
- Kotlin
- Jetpack Compose
- Coroutines
- Retrofit (for GitHub API requests)
- Room (for local storage)
- MVVM and StateFlow
- Unit Testing (JUnit, Mockito)

## Project Structure
- **app**: Main Android application module
- **data**: Handles data-related operations, including data fetching, caching, and storage.
- **domain**: Defines the business logic and use cases of the application.
- **ui**: Contains ViewModels and UI-related logic.
- **utils**: Utility classes and helper functions used throughout the project.

## Usage
1. Launch the app on your Android device or emulator.
2. The app will display a list of GitHub repositories on the home screen.
3. Scroll through the list to browse repositories.
4. Tap on a repository to view more details, including owner, description, and star count.
5. Navigate to the Issues screen to view a list of issues for the selected repository.

## Testing
The project includes unit tests to ensure the correctness of implemented features. To run the tests:
1. Open the project in Android Studio.
2. Navigate to the desired test class under the `test` directory.
3. Right-click on the test class and select "Run" to execute the tests.

## Release History
- **v1.0.0 (2023-12-26):** Initial release
    - Implemented main functionality, including repository list, details, and issues screens.

### You can find the latest build [here ↗](https://install.appcenter.ms/users/ahmedalaa/apps/vodafone-repos/distribution_groups/public/releases/1)

## Contributing
Contributions are welcome! If you find any bugs or have suggestions for improvements, please submit an issue or create a pull request.

## Acknowledgments
- [GitHub API](https://api.github.com) - API used for fetching GitHub repository data.

## Contact
If you have any questions or inquiries, please contact:

Ahmed Alaa\
Email: ahmedalaahussein00@gmail.com\
LinkedIn: [Ahmed Alaa ↗](https://www.linkedin.com/in/ahmed-alaa-hussein/)

