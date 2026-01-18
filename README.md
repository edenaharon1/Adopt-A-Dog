## Adopt a Dog - Android Application
Adopt a Dog is a professional native Android application designed to connect people with dogs available for adoption. Built with a focus on performance, real-time security, and location-based services, the app provides a smooth social experience for dog lovers.

## Key Features
Secure Social Authentication: Integrated with Firebase Authentication for secure and seamless user sign-in and account management.

Live Maps & Geo-Location: Advanced integration with Google Maps API that detects the user's current location and displays nearby dog adoption centers on an interactive map.

Offline Capabilities: Powered by Room Database for local data persistence, ensuring the app remains functional even without an internet connection.

Social Ecosystem: Full social networking features allowing users to create, edit, and view posts and comments.

Cloud Communication: Uses Volley for handling external API requests and managing remote data synchronization.

## Architecture & Tech Stack
The application architecture follows the MVVM (Model-View-ViewModel) pattern, promoting clean code and a clear separation of concerns.

## Frontend & UI
Kotlin - Primary language for modern Android development.

Fragments & XML - Modular UI design with a single-activity architecture using NavHostActivity.

Google Maps SDK - Providing real-time location services and map visualizations.

Jetpack Navigation Component - For smooth transitions between app destinations.

## Backend & Storage
Firebase Auth - Industry-standard authentication service.

Room Database - Local SQLite abstraction for structured data storage (Users, Posts).

Volley - Networking library for reliable HTTP requests.

LiveData & ViewModel - Reactive data handling to keep the UI in sync with data sources.

## Getting Started
Clone the repository:
git clone <your-repository-url>
Setup Credentials:
Add your google-services.json file to the app/ directory for Firebase.
Ensure your Google Maps API Key is correctly configured in your AndroidManifest.xml or local.properties.
Open in Android Studio:
Sync the project with Gradle files.
Run:
Deploy to an emulator or physical device.

## Contributors
This project was developed by:
Eden Aharon
Korin Leck
Sapir Indig
